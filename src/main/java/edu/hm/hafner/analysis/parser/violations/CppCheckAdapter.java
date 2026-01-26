package edu.hm.hafner.analysis.parser.violations;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Location;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.util.IntegerParser;
import edu.hm.hafner.util.TreeStringBuilder;

import java.io.Serial;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
            var violationsPerGroup = violations.stream()
                    .collect(Collectors.groupingBy(Violation::getGroup));

            var report = new Report();
            var treeStringBuilder = new TreeStringBuilder();
            for (List<Violation> group : violationsPerGroup.values()) {
                var sortedGroup = sortByInsertionOrder(group);
                updateIssueBuilder(sortedGroup.get(0), issueBuilder);
                for (Violation violation : sortedGroup) {
                    var fileName = treeStringBuilder.intern(violation.getFile());
                    issueBuilder.addLocation(new Location(fileName,
                            toValidInt(violation.getStartLine()), toValidInt(violation.getEndLine()),
                            toValidInt(violation.getColumn()), toValidInt(violation.getEndColumn())));
                }
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
                .toList();
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
        try {
            return IntegerParser.parseInt(violation.getSpecifics().getOrDefault(ORDER_KEY, "0"));
        }
        catch (NumberFormatException e) {
            return Integer.MAX_VALUE;
        }
    }
}
