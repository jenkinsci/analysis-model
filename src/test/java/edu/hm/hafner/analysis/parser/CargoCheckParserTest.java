package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link CargoCheckParser}.
 */
class CargoCheckParserTest extends AbstractParserTest {
    CargoCheckParserTest() {
        super("CargoCheck.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(1);

        softly.assertThat(report.get(0))
                .hasFileName("packages/secspc/src/main.rs")
                .hasMessage("unused import: `secsp_analysis::input::FileId`")
                .hasCategory("unused_imports")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(14)
                .hasLineEnd(14)
                .hasColumnStart(5)
                .hasColumnEnd(34);
    }

    @Override
    protected IssueParser createParser() {
        return new CargoCheckParser();
    }
}
