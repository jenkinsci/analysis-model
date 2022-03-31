package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link AquaScannerParser}.
 */
class AquaScannerParserTest extends AbstractParserTest {
    AquaScannerParserTest() {
        super("aqua_scanner_result.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(9);

        softly.assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_LOW)
                .hasFileName("unzip")
                .hasMessage("CVE-2022-0530");
        softly.assertThat(report.get(8))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasFileName("/opt/app/app-runner.jar:org/jose4j/json/JsonHeaderUtil.class")
                .hasMessage("CVE-1111-1234");
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
