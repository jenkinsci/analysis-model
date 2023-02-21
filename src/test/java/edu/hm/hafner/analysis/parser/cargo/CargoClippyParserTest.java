package edu.hm.hafner.analysis.parser.cargo;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

/**
 * Test cases for Cargo Clippy output parser.
 *
 * @author Mike Delaney
 */
class CargoClippyParserTest extends AbstractParserTest {

    CargoClippyParserTest() {
        super("cargo-clippy-2018.txt");
    }

    private static final Integer EXPECTED_ISSUE_COUNT = 17;

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(EXPECTED_ISSUE_COUNT);
        softly.assertThat(report.getSizeOf(Severity.ERROR)).isEqualTo(1);
        softly.assertThat(report.getSizeOf(Severity.WARNING_NORMAL)).isEqualTo(16);

        softly.assertThat(report.get(0))
                .hasFileName("build.rs")
                .hasMessage("the borrowed expression implements the required traits")
                .hasCategory("clippy::needless_borrow")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(9)
                .hasColumnStart(15)
                .hasColumnEnd(17);

        softly.assertThat(report.get(2))
                .hasFileName("src/fs.rs")
                .hasMessage("using `clone` on a double-reference; this will copy the reference of type `&str`"
                        + " instead of cloning the inner type")
                .hasCategory("clippy::clone_double_ref")
                .hasLineStart(11)
                .hasColumnStart(31)
                .hasColumnEnd(40)
                .hasSeverity(Severity.ERROR);

        softly.assertThat(report.get(EXPECTED_ISSUE_COUNT - 1))
                .hasFileName("src/main.rs")
                .hasMessage("use of `unwrap_or` followed by a function call")
                .hasCategory("clippy::or_fun_call")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(76)
                .hasColumnStart(53);
    }

    @Override
    protected IssueParser createParser() {
        return new CargoClippyParser();
    }
}
