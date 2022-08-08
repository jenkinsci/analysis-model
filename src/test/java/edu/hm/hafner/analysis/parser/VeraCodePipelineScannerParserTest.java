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
                .hasType("org.slf4j.Logger.info")
                .hasCategory("taint")
                .hasFileName("src/main/java/com/sample/LoggingFilter.java")
                .hasPackageName("com.sample")
                .hasMessage("Improper Output Neutralization for Logs")
                .hasLineStart(28);
        softly.assertThat(report.get(1))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasType("set")
                .hasCategory("crypto")
                .hasFileName("react/dist/esm/data.js")
                .hasPackageName("-")
                .hasMessage("Use of Hard-coded Password")
                .hasLineStart(25);
        softly.assertThat(report.get(2))
                .hasSeverity(Severity.WARNING_LOW)
                .hasType("management:endpoint:health:show-details:")
                .hasCategory("crypto")
                .hasFileName("BOOT-INF/classes/application.yml")
                .hasPackageName("application")
                .hasMessage("Information Exposure Through Sent Data")
                .hasLineStart(1);
        softly.assertThat(report.get(3))
                .hasSeverity(Severity.WARNING_LOW)
                .hasType("nosourcefile")
                .hasCategory("other")
                .hasFileName("-")
                .hasPackageName("-")
                .hasMessage("No source_file present")
                .hasLineStart(0);
        softly.assertThat(report.get(4))
                .hasSeverity(Severity.WARNING_LOW)
                .hasType("nofiles")
                .hasCategory("other")
                .hasFileName("-")
                .hasPackageName("-")
                .hasMessage("No files present")
                .hasLineStart(0);
        softly.assertThat(report.get(5))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasType("child_process.spawn")
                .hasCategory("taint")
                .hasFileName("lib/optimizer/Optimizer.js")
                .hasPackageName("-")
                .hasMessage("Improper Neutralization of Special Elements used in an OS Command ('OS Command Injection')")
                .hasLineStart(24);
        softly.assertThat(report.get(6))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasType("org.slf4j.Logger.info")
                .hasCategory("taint")
                .hasFileName("src/main/java/com/othersample/LoggingFilter.java")
                .hasPackageName("com.othersample")
                .hasMessage("Improper Output Neutralization for Logs")
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
