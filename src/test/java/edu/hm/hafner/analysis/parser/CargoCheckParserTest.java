package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

/**
 * Tests the class {@link CargoCheckParser}.
 *
 * @author Gary Tierney
 */
class CargoCheckParserTest extends AbstractParserTest {
    CargoCheckParserTest() {
        super("CargoCheck.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(2);

        softly.assertThat(report.get(0))
                .hasFileName("packages/secspc/src/main.rs")
                .hasMessage("unused import: `secsp_analysis::input::FileId`")
                .hasCategory("unused_imports")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(14)
                .hasLineEnd(14)
                .hasColumnStart(5)
                .hasColumnEnd(34);

        softly.assertThat(report.get(1))
                .hasFileName("packages/secspc/src/main.rs")
                .hasMessage("redundant closure found")
                .hasCategory("clippy::redundant_closure")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(68)
                .hasLineEnd(68)
                .hasColumnStart(14)
                .hasColumnEnd(34);
    }

    @Override
    protected IssueParser createParser() {
        return new CargoCheckParser();
    }
}
