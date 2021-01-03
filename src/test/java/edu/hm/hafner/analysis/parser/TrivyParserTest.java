package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
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
        softly.assertThat(report).hasSize(282);

        softly.assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_LOW)
                .hasType("CVE-2017-6519")
                .hasCategory("redhat")
                .hasMessage("avahi: Multicast DNS responds to unicast queries outside of local network");
    }

    @Override
    protected IssueParser createParser() {
        return new TrivyParser();
    }

}
