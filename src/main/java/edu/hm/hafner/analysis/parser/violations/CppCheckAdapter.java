package edu.hm.hafner.analysis.parser.violations;

import java.io.Serial;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.util.IntegerParser;
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
    private static final String ORDER_KEY = "order";

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
                var sortedGroup = sortByInsertionOrder(group);
                updateIssueBuilder(sortedGroup.get(0), issueBuilder);
                var lineRanges = new LineRangeList();
                for (int i = 1; i < sortedGroup.size(); i++) {
                    lineRanges.add(new LineRange(sortedGroup.get(i).getStartLine()));
                }
                issueBuilder.setLineRanges(lineRanges);
                report.add(issueBuilder.buildAndClean());
            }
            return report;
        }
    }

    /**
     * Sorts violations by their insertion order as recorded by the CppCheck parser.
     * The parser adds an "order" property to each violation to preserve the original order from the XML file.
     *
     * @param group
     *         the list of violations in the same group
     *
     * @return the violations sorted by their insertion order
     */
    private List<Violation> sortByInsertionOrder(final List<Violation> group) {
        return group.stream()
                .sorted(Comparator.comparingInt(this::getOrder))
                .collect(Collectors.toList());
    }

    /**
     * Extracts the order property from a violation.
     *
     * @param violation
     *         the violation
     *
     * @return the order of the violation, or Integer.MAX_VALUE if not present
     */
    private int getOrder(final Violation violation) {
        String order = violation.getSpecifics().get(ORDER_KEY);
        int parsed = IntegerParser.parseInt(order);
        return parsed == 0 && order != null && !order.equals("0") ? Integer.MAX_VALUE : parsed;
    }
}
