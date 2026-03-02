package edu.hm.hafner.analysis;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import edu.hm.hafner.analysis.Report.IssueType;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

class ReportFormatterTest {
    private final ReportFormatter formatter = new ReportFormatter();

    @ParameterizedTest(name = "type {0} should be formatted as {1}")
    @CsvSource({
            "BUG, No bugs",
            "WARNING, No warnings",
            "DUPLICATION, No duplications",
            "VULNERABILITY, No vulnerabilities"
    })
    @DisplayName("Should format size of empty report")
    void shouldFormatSizeForEmptyReport(final IssueType issueType, final String itemName) {
        var report = new Report("id", "name", "origin", issueType);
        assertThat(formatter.formatSizeOfElements(report)).isEqualTo(itemName);
        assertThat(formatter.formatSeverities(report)).isEmpty();

        assertThat(report.toString()).endsWith(itemName);
        assertThat(report.getSeverityDistribution()).isEmpty();
    }

    @ParameterizedTest(name = "type {0} should be formatted as {1}")
    @CsvSource({
            "BUG, 1 bug",
            "WARNING, 1 warning",
            "DUPLICATION, 1 duplication",
            "VULNERABILITY, 1 vulnerability"
    })
    @DisplayName("Should format size of report with single issue")
    void shouldFormatForSingleElement(final IssueType issueType, final String itemName) {
        var report = new Report("id", "name", "origin", issueType);

        try (var builder = new IssueBuilder()) {
            report.add(builder.setSeverity(Severity.WARNING_NORMAL).build());

            assertThat(formatter.formatSizeOfElements(report)).isEqualTo(itemName);
            assertThat(formatter.formatSeverities(report)).isEqualTo("normal: 1");

            var suffix = " (normal: 1)";
            assertThat(report.toString()).endsWith(itemName + suffix);
            assertThat(report.getSeverityDistribution()).isEqualTo(suffix);
        }
    }

    @ParameterizedTest(name = "type {0} should be formatted as {1}")
    @CsvSource({
            "BUG, 6 bugs",
            "WARNING, 6 warnings",
            "DUPLICATION, 6 duplications",
            "VULNERABILITY, 6 vulnerabilities"
    })
    @DisplayName("Should format size of report with multiple issues")
    void shouldFormatForMultipleElements(final IssueType issueType, final String itemName) {
        var report = new Report("id", "name", "origin", issueType);

        try (var builder = new IssueBuilder()) {
            int line = 0;
            report.add(builder.setSeverity(Severity.WARNING_LOW).setLineStart(line).build());
            line++;
            report.add(builder.setSeverity(Severity.WARNING_NORMAL).setLineStart(line).build());
            line++;
            report.add(builder.setSeverity(Severity.WARNING_NORMAL).setLineStart(line).build());
            line++;
            report.add(builder.setSeverity(Severity.ERROR).setLineStart(line).buildAndClean());
            line++;
            report.add(builder.setSeverity(Severity.ERROR).setLineStart(line).buildAndClean());
            line++;
            report.add(builder.setSeverity(Severity.ERROR).setLineStart(line).buildAndClean());

            assertThat(formatter.formatSizeOfElements(report)).isEqualTo(itemName);
            assertThat(formatter.formatSeverities(report)).isEqualTo("error: 3, normal: 2, low: 1");
        }
    }
}
