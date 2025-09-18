package edu.hm.hafner.analysis.parser.violations;

import org.apache.commons.lang3.Strings;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

import java.io.Serial;
import java.util.Map;
import java.util.Set;
import se.bjurr.violations.lib.model.SEVERITY;
import se.bjurr.violations.lib.model.Violation;
import se.bjurr.violations.lib.parsers.PiTestParser;

/**
 * Parses PIT results files.
 *
 * @author Ullrich Hafner
 */
public class PitAdapter extends AbstractViolationAdapter {
    @Serial
    private static final long serialVersionUID = -7811207963029906228L;

    private static final String STATUS = "status";
    private static final String SURVIVED = "SURVIVED";
    private static final String NO_COVERAGE = "NO_COVERAGE";
    private static final String KILLED = "KILLED";
    private static final String MUTATORS_PACKAGE = "org.pitest.mutationtest.engine.gregor.mutators.";

    /** Report property key to obtain the total number of mutations. */
    public static final String TOTAL_MUTATIONS = "totalMutations";
    /** Report property key to obtain the number of killed mutations. */
    public static final String KILLED_MUTATIONS = "killedMutations";
    /** Report property key to obtain the number of killed mutations. */
    public static final String UNCOVERED_MUTATIONS = "uncoveredMutations";
    /** Report property key to obtain the number of killed mutations. */
    public static final String SURVIVED_MUTATIONS = "survivedMutations";

    @Override
    PiTestParser createParser() {
        return new PiTestParser();
    }

    @Override
    boolean isValid(final Violation violation) {
        return !KILLED.equals(getMutationStatus(violation));
    }

    @Override
    Severity convertSeverity(final SEVERITY severity, final Violation violation) {
        if (SURVIVED.equals(getMutationStatus(violation))) {
            return Severity.WARNING_NORMAL;
        }
        return Severity.WARNING_HIGH; // not covered
    }

    @Override
    void postProcess(final Report report, final Set<Violation> violations) {
        int total = violations.size();
        Map<String, Integer> issuesByCategory = report.getPropertyCount(Issue::getCategory);
        int noCoverage = issuesByCategory.getOrDefault(NO_COVERAGE, 0);
        int survived = issuesByCategory.getOrDefault(SURVIVED, 0);

        report.setCounter(TOTAL_MUTATIONS, total);
        report.setCounter(UNCOVERED_MUTATIONS, noCoverage);
        report.setCounter(SURVIVED_MUTATIONS, survived);
        report.setCounter(KILLED_MUTATIONS, total - noCoverage - survived);
    }

    @Override
    void extractAdditionalProperties(final IssueBuilder builder, final Violation violation) {
        builder.setCategory(getMutationStatus(violation));
        builder.setType(Strings.CS.removeStart(violation.getRule(), MUTATORS_PACKAGE));
    }

    private String getMutationStatus(final Violation violation) {
        return violation.getSpecifics().getOrDefault(STATUS, KILLED);
    }
}
