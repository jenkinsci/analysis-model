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
 * Tests the class {@link YoctoScannerParser}.
 */
class YoctoScannerParserTest extends AbstractParserTest {

    YoctoScannerParserTest() {
        super("yocto_scanner_result.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(25);

        softly.assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_LOW)
                .hasPackageName("acl")
                .hasMessage("CVE-2009-4411");
        softly.assertThat(report.get(3))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasPackageName("automake-native")
                .hasMessage("CVE-2012-3386");
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
        return new YoctoScannerParser();
    }
}
