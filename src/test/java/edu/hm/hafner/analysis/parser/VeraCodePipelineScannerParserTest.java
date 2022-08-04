package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link VeraCodePipelineScannerParserTest}.
 */
class VeraCodePipelineScannerParserTest extends StructuredFileParserTest {

    VeraCodePipelineScannerParserTest() {
        super("veracode_pipeline_scanner_result.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(7);

        softly.assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasMessage("org.slf4j.Logger.info")
                .hasFileName("src/main/java/com/sample/LoggingFilter.java")
                .hasPackageName("com.sample")
                .hasType("Improper Output Neutralization for Logs")
                .hasLineStart(28);
        softly.assertThat(report.get(1))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("set")
                .hasFileName("react/dist/esm/data.js")
                .hasPackageName("-")
                .hasType("Use of Hard-coded Password")
                .hasLineStart(25);
        softly.assertThat(report.get(2))
                .hasSeverity(Severity.WARNING_LOW)
                .hasMessage("management:endpoint:health:show-details:")
                .hasFileName("BOOT-INF/classes/application.yml")
                .hasPackageName("application")
                .hasType("Information Exposure Through Sent Data")
                .hasLineStart(1);
        softly.assertThat(report.get(3))
                .hasSeverity(Severity.WARNING_LOW)
                .hasMessage("nosourcefile")
                .hasFileName("-")
                .hasPackageName("-")
                .hasType("No source_file present")
                .hasLineStart(0);
        softly.assertThat(report.get(4))
                .hasSeverity(Severity.WARNING_LOW)
                .hasMessage("nofiles")
                .hasFileName("-")
                .hasPackageName("-")
                .hasType("No files present")
                .hasLineStart(0);
        softly.assertThat(report.get(5))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasMessage("child_process.spawn")
                .hasFileName("lib/optimizer/Optimizer.js")
                .hasPackageName("-")
                .hasType("Improper Neutralization of Special Elements used in an OS Command ('OS Command Injection')")
                .hasLineStart(24);
        softly.assertThat(report.get(6))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasMessage("org.slf4j.Logger.info")
                .hasFileName("src/main/java/com/othersample/LoggingFilter.java")
                .hasPackageName("com.othersample")
                .hasType("Improper Output Neutralization for Logs")
                .hasLineStart(55);
    }

    @Test
    void shouldHandleEmptyResultsJenkins67296() {
        Report report = parse("issue67296.json");

        assertThat(report).isEmpty();
    }

    @Override
    protected IssueParser createParser() {
        return new VeraCodePipelineScannerParser();
    }
}
