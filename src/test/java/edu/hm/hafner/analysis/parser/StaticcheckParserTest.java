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
 * Tests the class {@link StaticcheckParser}.
 *
 * @author Akash Manna
 */
class StaticcheckParserTest extends AbstractParserTest {
    StaticcheckParserTest() {
        super("staticcheck-report.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(4);

        softly.assertThat(report.get(0))
                .hasFileName("internal/config/config.go")
                .hasLineStart(42)
                .hasColumnStart(2)
                .hasLineEnd(42)
                .hasColumnEnd(13)
                .hasType("SA4006")
                .hasMessage("this value of err is never used")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(1))
                .hasFileName("pkg/http/server.go")
                .hasLineStart(18)
                .hasColumnStart(6)
                .hasLineEnd(18)
                .hasColumnEnd(19)
                .hasType("ST1005")
                .hasMessage("error strings should not be capitalized")
                .hasSeverity(Severity.WARNING_LOW);

        softly.assertThat(report.get(2))
                .hasFileName("cmd/app/main.go")
                .hasLineStart(65)
                .hasColumnStart(3)
                .hasLineEnd(65)
                .hasColumnEnd(21)
                .hasType("S1002")
                .hasMessage("should omit comparison to bool constant")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(3))
                .hasFileName("pkg/cache/cache.go")
                .hasLineStart(90)
                .hasColumnStart(1)
                .hasLineEnd(95)
                .hasColumnEnd(2)
                .hasType("U1000")
                .hasMessage("func warmupCache is unused")
                .hasSeverity(Severity.ERROR);
    }

    @Override
    protected IssueParser createParser() {
        return new StaticcheckParser();
    }

    @Test
    void accepts() {
        var parser = new StaticcheckParser();
        assertThat(parser.accepts(read("staticcheck-report.json"))).isTrue();
        assertThat(parser.accepts(read("foo.txt"))).isFalse();
    }

    private FileReaderFactory read(final String first) {
        return new FileReaderFactory(FileSystems.getDefault().getPath(first));
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
                [
                  {
                    "message": "fallback handling"
                  }
                ]
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
    void shouldHandleWrappedDiagnosticsObject() {
        var report = parseStringContent("""
                {
                  "diagnostics": [
                    {
                      "code": "SA4010",
                      "severity": "warning",
                      "message": "wrapped payload",
                      "location": {
                        "file": "service.go",
                        "line": 7,
                        "column": 4
                      },
                      "end": {
                        "line": 7,
                        "column": 16
                      }
                    }
                  ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("service.go")
                .hasType("SA4010")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("wrapped payload")
                .hasLineStart(7)
                .hasColumnStart(4)
                .hasLineEnd(7)
                .hasColumnEnd(16);
    }

    @Test
    void shouldParseSingleIssueObject() {
        var report = parseStringContent("""
                {
                  "code": "SA9999",
                  "severity": "warning",
                  "message": "single issue object",
                  "location": {
                    "file": "single.go",
                    "line": 11,
                    "column": 2
                  }
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("single.go")
                .hasType("SA9999")
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
                    "tool": "staticcheck"
                  }
                }
                """);

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleNullDiagnosticEntry() {
        var report = parseStringContent("""
                {
                  "diagnostics": [
                    null
                  ]
                }
                """);

              assertThat(report).isEmpty();
    }

    @Test
    void shouldDetectIssueByLocationOnly() {
        var report = parseStringContent("""
                {
                  "location": {
                    "file": "location-only.go",
                    "line": 5,
                    "column": 9
                  }
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("location-only.go")
                .hasType("-")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("")
                .hasLineStart(5)
                .hasColumnStart(9);
    }

    @Test
    void shouldDetectIssueByCodeOnly() {
        var report = parseStringContent("""
                {
                  "code": "SA5000"
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasType("SA5000")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("")
                .hasFileName("-")
                .hasLineStart(0)
                .hasColumnStart(0);
    }

    @Test
    void shouldProvideDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("staticcheck");

        assertThat(descriptor.getPattern()).isEqualTo("**/staticcheck-report.json");
        assertThat(descriptor.getHelp()).contains("staticcheck -f json");
        assertThat(descriptor.getUrl()).isEqualTo("https://github.com/dominikh/go-tools");
        assertThat(descriptor.hasHelp()).isTrue();
        assertThat(descriptor.hasUrl()).isTrue();
    }
}