package edu.hm.hafner.analysis.parser;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

/**
 * Tests the class {@link TrivyParser}.
 */
class Trivy020ParserTest extends AbstractParserTest {

    Trivy020ParserTest() {
        super("trivy_result_0.20.0.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(4);

        softly.assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_LOW)
                .hasType("CVE-2017-6519")
                .hasCategory("redhat")
                .hasMessage("avahi: Multicast DNS responds to unicast queries outside of local network");
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt")).isInstanceOf(ParsingException.class);
    }

    @Override
    protected IssueParser createParser() {
        return new TrivyParser();
    }
}
