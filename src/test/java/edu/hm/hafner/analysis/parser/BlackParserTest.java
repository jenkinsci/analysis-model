package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;
import edu.hm.hafner.analysis.registry.ParserRegistry;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link BlackParser}.
 *
 * @author Akash Manna
 */
class BlackParserTest extends AbstractParserTest {
    BlackParserTest() {
        super("black-report.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(5);
        assertThatReportHasSeverities(report, 2, 0, 3, 0);

        // First issue: syntax error in src/main.py
        softly.assertThat(report.get(0))
                .hasFileName("src/main.py")
                .hasLineStart(5)
                .hasColumnStart(1)
                .hasMessage("invalid syntax")
                .hasCategory("syntax-error")
                .hasSeverity(Severity.ERROR);

        // Second issue: syntax error in tests/test_utils.py
        softly.assertThat(report.get(1))
                .hasFileName("tests/test_utils.py")
                .hasLineStart(12)
                .hasColumnStart(5)
                .hasMessage("expected an indented block after function definition on line 11")
                .hasCategory("syntax-error")
                .hasSeverity(Severity.ERROR);

        // Third issue: would reformat src/utils.py
        softly.assertThat(report.get(2))
                .hasFileName("src/utils.py")
                .hasMessage("\"src/utils.py\" would reformat by black")
                .hasCategory("formatting")
                .hasSeverity(Severity.WARNING_NORMAL);

        // Fourth issue: would reformat src/models.py
        softly.assertThat(report.get(3))
                .hasFileName("src/models.py")
                .hasMessage("\"src/models.py\" would reformat by black")
                .hasCategory("formatting")
                .hasSeverity(Severity.WARNING_NORMAL);

        // Fifth issue: reformatted src/config.py
        softly.assertThat(report.get(4))
                .hasFileName("src/config.py")
                .hasMessage("\"src/config.py\" reformatted by black")
                .hasCategory("formatting")
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Override
    protected IssueParser createParser() {
        return new BlackParser();
    }

    /** Verifies that a report with no issues produces an empty result. */
    @Test
    void shouldHandleNoIssues() {
        var report = parse("black-report-no-issues.txt");

        assertThat(report).isEmpty();
    }

    /** Verifies that an empty input produces an empty result. */
    @Test
    void shouldHandleEmptyInput() {
        var report = parseStringContent("");

        assertThat(report).isEmpty();
    }

    /** Verifies parsing of a single syntax error from inline content. */
    @Test
    void shouldParseSingleSyntaxError() {
        var report = parseStringContent(
                "error: cannot format app.py: Cannot parse: 10:3: invalid syntax\n");

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("app.py")
                .hasLineStart(10)
                .hasColumnStart(3)
                .hasMessage("invalid syntax")
                .hasCategory("syntax-error")
                .hasSeverity(Severity.ERROR);
    }

    /** Verifies parsing of a "would reformat" line from inline content. */
    @Test
    void shouldParseWouldReformatNotice() {
        var report = parseStringContent("would reformat my_module.py\n");

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("my_module.py")
                .hasMessage("\"my_module.py\" would reformat by black")
                .hasCategory("formatting")
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    /** Verifies parsing of a "reformatted" line from inline content. */
    @Test
    void shouldParseReformattedNotice() {
        var report = parseStringContent("reformatted path/to/file.py\n");

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("path/to/file.py")
                .hasMessage("\"path/to/file.py\" reformatted by black")
                .hasCategory("formatting")
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    /** Verifies that summary and emoji lines are ignored (not parsed). */
    @Test
    void shouldIgnoreSummaryLines() {
        var report = parseStringContent(
                """
                Oh no!
                💥 💔 💥
                2 files would be reformatted, 1 file would be left unchanged.
                All done! ✨ 🍰 ✨
                """);

        assertThat(report).isEmpty();
    }

    /** Verifies handling of file paths with spaces and nested directories. */
    @Test
    void shouldHandleNestedPathWithSyntaxError() {
        var report = parseStringContent(
                "error: cannot format src/sub/module.py: Cannot parse: 42:10: unexpected EOF while parsing\n");

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("src/sub/module.py")
                .hasLineStart(42)
                .hasColumnStart(10)
                .hasMessage("unexpected EOF while parsing")
                .hasCategory("syntax-error")
                .hasSeverity(Severity.ERROR);
    }

    /** Verifies that the descriptor metadata is correctly registered. */
    @Test
    void shouldProvideDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("black");

        assertThat(descriptor.getPattern()).isEqualTo("**/black-report.txt");
        assertThat(descriptor.getHelp()).contains("black --check");
        assertThat(descriptor.getUrl()).isEqualTo("https://github.com/psf/black");
        assertThat(descriptor.hasHelp()).isTrue();
        assertThat(descriptor.hasUrl()).isTrue();
    }
}
