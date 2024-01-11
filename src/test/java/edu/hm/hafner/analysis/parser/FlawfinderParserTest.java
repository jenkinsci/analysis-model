package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

/**
 * Tests the class {@link FlawfinderParser}.
 *
 * @author Dom Postorivo
 */
class FlawfinderParserTest extends AbstractParserTest {
    FlawfinderParserTest() {
        super("flawfinder.log");
    }

    @Override
    protected FlawfinderParser createParser() {
        return new FlawfinderParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(3);
        softly.assertThat(report.get(0))
                .hasFileName("src/Bar.c")
                .hasLineStart(779)
                .hasCategory("buffer")
                .hasMessage("strncat:Easily used incorrectly (e.g., incorrectly computing the correct maximum size to add) [MS-banned] (CWE-120).")
                .hasSeverity(Severity.WARNING_HIGH);
        softly.assertThat(report.get(1))
                .hasFileName("src/main.c")
                .hasLineStart(114)
                .hasCategory("format")
                .hasMessage("printf:If format strings can be influenced by an attacker, they can be exploited (CWE-134).  Use a constant for the format specification.")
                .hasSeverity(Severity.WARNING_NORMAL);
        softly.assertThat(report.get(2))
                .hasFileName("src/Bar.c")
                .hasLineStart(246)
                .hasCategory("buffer")
                .hasMessage("scanf:It's unclear if the %s limit in the format string is small enough (CWE-120).")
                .hasSeverity(Severity.WARNING_LOW);
    }
}
