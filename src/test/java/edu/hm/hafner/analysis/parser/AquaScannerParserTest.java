package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link AquaScannerParser}.
 */
class AquaScannerParserTest extends AbstractParserTest {
    private static final String EXPECTED_FILENAME = "/opt/app/app-runner.jar:org/jose4j/json/JsonHeaderUtil.class";

    AquaScannerParserTest() {
        super("aqua_scanner_result.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(14);

        softly.assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_LOW)
                .hasFileName("unzip")
                .hasMessage("CVE-2022-0530");
        softly.assertThat(report.get(8))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasFileName(EXPECTED_FILENAME)
                .hasMessage("CVE-1111-1234");
        softly.assertThat(report.get(9))
                .hasSeverity(Severity.ERROR)
                .hasFileName(EXPECTED_FILENAME)
                .hasMessage("CVE-1112-1234");
        softly.assertThat(report.get(10))
                .hasSeverity(Severity.ERROR)
                .hasFileName(EXPECTED_FILENAME)
                .hasMessage("CVE-1113-1234");
        softly.assertThat(report.get(11))
                .hasSeverity(Severity.ERROR)
                .hasFileName(EXPECTED_FILENAME)
                .hasMessage("CVE-1114-1234");
        softly.assertThat(report.get(12))
                .hasSeverity(Severity.WARNING_LOW)
                .hasFileName(EXPECTED_FILENAME)
                .hasMessage("CVE-1115-1234");
        softly.assertThat(report.get(13))
                .hasSeverity(Severity.ERROR)
                .hasFileName(EXPECTED_FILENAME)
                .hasMessage("CVE-1116-1234");
    }

    @Test
    void shouldHandleEmptyResultsJenkins67296() {
        Report report = parse("issue67296.json");

        assertThat(report).isEmpty();
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt")).isInstanceOf(ParsingException.class);
    }

    @Override
    protected IssueParser createParser() {
        return new AquaScannerParser();
    }
}
