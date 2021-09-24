package edu.hm.hafner.analysis.parser;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.Assertions;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

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

        Assertions.assertThat(report).hasSize(4);

        Assertions.assertThat(report.get(0))
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
