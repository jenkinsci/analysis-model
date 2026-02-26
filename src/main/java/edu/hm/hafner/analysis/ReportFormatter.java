package edu.hm.hafner.analysis;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Report.IssueType;

import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Utility class to format reports for display in the user interface.
 *
 * @author Ullrich Hafner
 */
public class ReportFormatter {
    /**
     * Formats the size of the given report for display in the user interface. If the report contains no elements, then
     * a message indicating that no elements are present is returned. Otherwise, the number of elements is returned,
     * followed by the pluralized name of the element type.
     *
     * @param report
     *         the report to format
     *
     * @return a formatted string representing the size of the report
     */
    public String formatSizeOfElements(final Report report) {
        var items = getItemName(report);
        var count = report.getSize();
        if (count == 0) {
            return String.format(Locale.ENGLISH, "No %s", items);
        }
        return String.format(Locale.ENGLISH, "%d %s", count, items);
    }

    /**
     * Formats the distribution of severities in the given report for display in the user interface. If the report
     * contains no elements, then an empty string is returned. Otherwise, a comma-separated list of the severities and
     * their respective counts is returned.
     *
     * @param report
     *         the report to format
     *
     * @return a formatted string representing the distribution of severities in the report
     */
    public String formatSeverities(final Report report) {
        return Severity.getPredefinedValues()
                .stream()
                .map(s -> reportSeverity(report, s))
                .flatMap(Optional::stream)
                .collect(Collectors.joining(", "));
    }

    private Optional<String> reportSeverity(final Report report, final Severity severity) {
        var size = report.getSizeOf(severity);
        if (size > 0) {
            return Optional.of(
                    String.format(Locale.ENGLISH, "%s: %d", StringUtils.lowerCase(severity.getName()), size));
        }
        return Optional.empty();
    }

    private String getItemName(final Report report) {
        var elementType = report.getElementType();

        if (report.getSize() == 1) {
            return getSingular(elementType);
        }
        return getPlural(elementType);
    }

    private String getPlural(final IssueType elementType) {
        return switch (elementType) {
            case WARNING -> "warnings";
            case BUG -> "bugs";
            case DUPLICATION -> "duplications";
            case VULNERABILITY -> "vulnerabilities";
        };
    }

    private String getSingular(final IssueType elementType) {
        return switch (elementType) {
            case WARNING -> "warning";
            case BUG -> "bug";
            case DUPLICATION -> "duplication";
            case VULNERABILITY -> "vulnerability";
        };
    }
}
