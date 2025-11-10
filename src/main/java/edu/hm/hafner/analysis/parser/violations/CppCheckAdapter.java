package edu.hm.hafner.analysis.parser.violations;

import java.io.Serial;
import java.util.LinkedHashSet;
import java.util.List;
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
        if ("missingOverride".equals(group.get(0).getRule())) {
            for (Violation violation : group) {
                var message = violation.getMessage();
                // The derived class location has "Function in derived class" or "function in derived class" in its message
                // The base class location has "Virtual function in base class" or similar
                if (message != null && message.toLowerCase(java.util.Locale.ROOT).contains("derived class")) {
                    return violation;
                }
            }
        }

        // Default: use the first violation
        return group.get(0);
    }
}
