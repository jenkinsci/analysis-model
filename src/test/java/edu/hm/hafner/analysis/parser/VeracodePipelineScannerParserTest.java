package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link VeracodePipelineScannerParserTest}.
 */
class VeracodePipelineScannerParserTest extends StructuredFileParserTest {

    VeracodePipelineScannerParserTest() {
        super("veracode_pipeline_scanner_result.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(5);

        softly.assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasMessage("org.slf4j.Logger.info")
                .hasFileName("com/sample/LoggingFilter.java")
                .hasType("Improper Output Neutralization for Logs")
                .hasLineStart(28);
        softly.assertThat(report.get(1))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("set")
                .hasFileName("react/dist/esm/data.js")
                .hasType("Use of Hard-coded Password")
                .hasLineStart(25);
        softly.assertThat(report.get(2))
                .hasSeverity(Severity.WARNING_LOW)
                .hasMessage("management:endpoint:health:show-details:")
                .hasFileName("BOOT-INF/classes/application.yml")
                .hasType("Information Exposure Through Sent Data")
                .hasLineStart(1);
        softly.assertThat(report.get(3))
                .hasSeverity(Severity.WARNING_LOW)
                .hasMessage("nosourcefile")
                .hasFileName("-")
                .hasType("No source_file present")
                .hasLineStart(0);
        softly.assertThat(report.get(4))
                .hasSeverity(Severity.WARNING_LOW)
                .hasMessage("nofiles")
                .hasFileName("-")
                .hasType("No files present")
                .hasLineStart(0);
    }

    @Test
    void shouldHandleEmptyResultsJenkins67296() {
        Report report = parse("issue67296.json");

        assertThat(report).isEmpty();
    }

    @Override
    protected IssueParser createParser() {
        return new VeracodePipelineScannerParser();
    }
}
