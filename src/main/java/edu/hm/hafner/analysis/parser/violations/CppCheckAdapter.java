package edu.hm.hafner.analysis.parser.violations;

import java.io.Serial;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.util.LineRange;
import edu.hm.hafner.util.LineRangeList;

import se.bjurr.violations.lib.model.Violation;
import se.bjurr.violations.lib.parsers.CPPCheckParser;

/**
 * Parses CPPCheck files.
 *
 * @author Ullrich Hafner
 */
public class CppCheckAdapter extends AbstractViolationAdapter {
    @Serial
    private static final long serialVersionUID = 2244442395053328008L;
    private static final String MISSING_OVERRIDE = "missingOverride";
    private static final String DERIVED_CLASS_MARKER = "derived class";

    @Override
    CPPCheckParser createParser() {
        return new CPPCheckParser();
    }

    @Override
    Report convertToReport(final Set<Violation> violations) {
        try (var issueBuilder = new IssueBuilder()) {
            var violationsPerGroup = new LinkedHashSet<>(violations).stream()
                    .collect(Collectors.groupingBy(Violation::getGroup));

            var report = new Report();
            for (List<Violation> group : violationsPerGroup.values()) {
                var primaryViolation = selectPrimaryViolation(group);
                updateIssueBuilder(primaryViolation, issueBuilder);
                var lineRanges = new LineRangeList();
                for (Violation violation : group) {
                    if (!violation.equals(primaryViolation)) {
                        lineRanges.add(new LineRange(violation.getStartLine()));
                    }
                }
                issueBuilder.setLineRanges(lineRanges);
                report.add(issueBuilder.buildAndClean());
            }
            return report;
        }
    }

    /**
     * Selects the primary violation from a group of violations that share the same error ID.
     * For missingOverride errors, prefers the location in the derived class over the base class.
     *
     * @param group
     *         the list of violations in the same group
     *
     * @return the primary violation to use as the main issue
     */
    private Violation selectPrimaryViolation(final List<Violation> group) {
        if (group.isEmpty()) {
            throw new IllegalArgumentException("Violation group cannot be empty");
        }

        // For missingOverride, prefer the derived class location over base class
        if (MISSING_OVERRIDE.equals(group.get(0).getRule())) {
            for (Violation violation : group) {
                var message = violation.getMessage();
                if (message != null && containsIgnoreCase(message, DERIVED_CLASS_MARKER)) {
                    return violation;
                }
            }
        }

        return group.get(0);
    }

    /**
     * Checks if the source string contains the target string, ignoring case considerations.
     *
     * @param source
     *         the string to search in
     * @param target
     *         the string to search for
     *
     * @return true if the source contains the target (case-insensitive), false otherwise
     */
    private boolean containsIgnoreCase(final String source, final String target) {
        if (source == null || target == null) {
            return false;
        }
        return source.toLowerCase(Locale.ENGLISH).contains(target.toLowerCase(Locale.ENGLISH));
    }
}
