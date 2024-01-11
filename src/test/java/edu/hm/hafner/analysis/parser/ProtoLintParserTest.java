package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Test class for {@link ProtoLintParser}.
 *
 * @author David Hart
 */
class ProtoLintParserTest extends AbstractParserTest {
    ProtoLintParserTest() {
        super("protolint.txt");
    }

    @Override
    public void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(2591);

        softly.assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(25)
                .hasColumnStart(1)
                .hasMessage("The line length is 91, but it must be shorter than 80")
                .hasFileName("google/ads/googleads/v1/common/ad_asset.proto");

        softly.assertThat(report.get(1))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(30)
                .hasColumnStart(1)
                .hasMessage("The line length is 91, but it must be shorter than 80")
                .hasFileName("google/ads/googleads/v1/common/ad_type_infos.proto");

        softly.assertThat(report.get(403))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(41)
                .hasColumnStart(1)
                .hasMessage("Imports are not sorted.")
                .hasFileName("google/ads/googleads/v1/resources/campaign.proto");
    }

    @Override
    public IssueParser createParser() {
        return new ProtoLintParser();
    }
}
