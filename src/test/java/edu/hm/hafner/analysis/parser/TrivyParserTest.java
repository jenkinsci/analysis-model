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
 * Tests the class {@link TrivyParser}.
 */
class TrivyParserTest extends AbstractParserTest {
    TrivyParserTest() {
        super("trivy_result.json");
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
    void parseResultsForSchemaVersion2() {
        Report report = parse("trivy_result_0.20.0.json");

        assertThat(report).hasSize(4);

        assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_LOW)
                .hasType("CVE-2017-6519")
                .hasCategory("redhat")
                .hasMessage("avahi: Multicast DNS responds to unicast queries outside of local network");
    }

    @Test
    void shouldHandleEmptyResultsJenkins67296() {
        Report report = parse("issue67296.json");

        assertThat(report).isEmpty();
    }

    @Test
    void shouldMapCorrectly() {
        Report report = parse("trivy_result_0.20.0.json");

        assertThat(report).hasSize(4);

        assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_LOW)
                .hasType("CVE-2017-6519");
        assertThat(report.get(1))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasType("CVE-2020-8619");
        assertThat(report.get(2))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasType("CVE-2020-5555");
        assertThat(report.get(3))
                .hasSeverity(Severity.ERROR)
                .hasType("CVE-2020-9999");
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
