package edu.hm.hafner.analysis.parser;

import java.nio.file.FileSystems;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.FileReaderFactory;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;
import edu.hm.hafner.analysis.registry.ParserRegistry;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link GolangCiLintParser}.
 *
 * @author Akash Manna
 */
class GolangCiLintParserTest extends AbstractParserTest {
    GolangCiLintParserTest() {
        super("golangci-lint-report.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(4);

        softly.assertThat(report.get(0))
                .hasFileName("pkg/sample/output.go")
                .hasLineStart(6)
                .hasColumnStart(38)
                .hasType("misspell")
                .hasMessage("`occured` is a misspelling of `occurred`")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(1))
                .hasFileName("cmd/server/main.go")
                .hasLineStart(23)
                .hasLineEnd(25)
                .hasColumnStart(2)
                .hasType("govet")
                .hasMessage("unreachable code")
                .hasSeverity(Severity.ERROR);

        softly.assertThat(report.get(2))
                .hasFileName("internal/sample/doc.go")
                .hasLineStart(48)
                .hasColumnStart(1)
                .hasType("revive")
                .hasMessage("package comment should be of the form \"Package sample ...\"")
                .hasSeverity(Severity.WARNING_LOW);

        softly.assertThat(report.get(3))
                .hasFileName("-")
                .hasLineStart(100)
                .hasLineEnd(102)
                .hasType("gocritic")
                .hasMessage("unslice: could simplify expression")
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Override
    protected IssueParser createParser() {
        return new GolangCiLintParser();
    }

    @Test
    void accepts() {
        var parser = new GolangCiLintParser();
        assertThat(parser.accepts(new FileReaderFactory(FileSystems.getDefault().getPath("golangci-lint-report.json")))).isTrue();
        assertThat(parser.accepts(new FileReaderFactory(FileSystems.getDefault().getPath("foo.txt")))).isFalse();
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt"))
                .isInstanceOf(ParsingException.class);
    }

    @Test
    void emptyInput() throws ParsingException {
        var report = parse("issues-no-issues.json");

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleMissingFields() {
        var report = parseStringContent("""
                {
                  "Issues": [
                    {
                      "Text": "fallback handling"
                    }
                  ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasType("-")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("fallback handling")
                .hasFileName("-")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasColumnStart(0)
                .hasColumnEnd(0);
    }

    @Test
    void shouldHandleWrappedIssuesObject() {
        var report = parseStringContent("""
                {
                  "Issues": [
                    {
                      "FromLinter": "govet",
                      "Text": "wrapped payload",
                      "Severity": "warning",
                      "Pos": {
                        "Filename": "service.go",
                        "Line": 7,
                        "Column": 4
                      }
                    }
                  ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("service.go")
                .hasType("govet")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("wrapped payload")
                .hasLineStart(7)
                .hasColumnStart(4);
    }

    @Test
    void shouldParseSingleIssueObject() {
        var report = parseStringContent("""
                {
                  "FromLinter": "revive",
                  "Text": "single issue object",
                  "Severity": "warning",
                  "Pos": {
                    "Filename": "single.go",
                    "Line": 11,
                    "Column": 2
                  }
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("single.go")
                .hasType("revive")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("single issue object")
                .hasLineStart(11)
                .hasColumnStart(2);
    }

    @Test
    void shouldIgnoreNonIssueRootObject() {
        var report = parseStringContent("""
                {
                  "metadata": {
                    "tool": "golangci-lint"
                  }
                }
                """);

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleNullIssueEntry() {
        var report = parseStringContent("""
                {
                  "Issues": [
                    null
                  ]
                }
                """);

        assertThat(report).isEmpty();
    }

    @Test
    void shouldUseLineRangeFallbackWhenPositionMissing() {
        var report = parseStringContent("""
                {
                  "Issues": [
                    {
                      "FromLinter": "gocritic",
                      "Text": "line range only",
                      "Severity": "warning",
                      "LineRange": {
                        "From": 3,
                        "To": 7
                      }
                    }
                  ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("-")
                .hasType("gocritic")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("line range only")
                .hasLineStart(3)
                .hasLineEnd(7)
                .hasColumnStart(0)
                .hasColumnEnd(0);
    }

    @Test
    void shouldProvideDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("golangci-lint");

        assertThat(descriptor.getPattern()).isEqualTo("**/golangci-lint-report.json");
        assertThat(descriptor.getHelp()).contains("golangci-lint run --output.json.path");
        assertThat(descriptor.getUrl()).isEqualTo("https://github.com/golangci/golangci-lint");
        assertThat(descriptor.hasHelp()).isTrue();
        assertThat(descriptor.hasUrl()).isTrue();
    }
}
