package edu.hm.hafner.analysis.parser;

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
                .hasDescription("for further information visit <a href=\""
                        + "https://rust-lang.github.io/rust-clippy/master/index.html#needless_borrow"
                        + "\">cargo clippy documentation</a>")
                .hasCategory("clippy::needless_borrow")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(9)
                .hasColumnStart(15)
                .hasColumnEnd(17);

        softly.assertThat(report.get(2))
                .hasFileName("src/fs.rs")
                .hasMessage("using `clone` on a double-reference; this will copy the reference of type `&str`"
                        + " instead of cloning the inner type")
                .hasDescription("for further information visit <a href=\""
                        + "https://rust-lang.github.io/rust-clippy/master/index.html#clone_double_ref"
                        + "\">cargo clippy documentation</a>")
                .hasCategory("clippy::clone_double_ref")
                .hasLineStart(11)
                .hasColumnStart(31)
                .hasColumnEnd(40)
                .hasSeverity(Severity.ERROR);

        softly.assertThat(report.get(EXPECTED_ISSUE_COUNT - 1))
                .hasFileName("src/main.rs")
                .hasMessage("use of `unwrap_or` followed by a function call")
                .hasDescription("for further information visit <a href=\""
                        + "https://rust-lang.github.io/rust-clippy/master/index.html#or_fun_call"
                        + "\">cargo clippy documentation</a>")
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
