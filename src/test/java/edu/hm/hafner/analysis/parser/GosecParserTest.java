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
 * Tests the class {@link GosecParser}.
 *
 * @author Akash Manna
 */
class GosecParserTest extends AbstractParserTest {
    GosecParserTest() {
        super("gosec-report.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(3);

        softly.assertThat(report.get(0))
                .hasFileName("cmd/server/main.go")
                .hasLineStart(42)
                .hasLineEnd(42)
                .hasColumnStart(13)
                .hasColumnEnd(13)
                .hasType("G304")
                .hasMessage("Potential file inclusion via variable")
                .hasSeverity(Severity.WARNING_HIGH);

        softly.assertThat(report.get(0).getDescription())
                .contains("CWE-22")
                .contains("https://cwe.mitre.org/data/definitions/22.html")
                .contains("os.Open(path)");

        softly.assertThat(report.get(1))
                .hasFileName("internal/db/query.go")
                .hasLineStart(15)
                .hasLineEnd(15)
                .hasColumnStart(10)
                .hasColumnEnd(10)
                .hasType("G201")
                .hasMessage("SQL string formatting detected")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(2))
                .hasFileName("pkg/http/client.go")
                .hasLineStart(9)
                .hasLineEnd(9)
                .hasColumnStart(0)
                .hasColumnEnd(0)
                .hasType("-")
                .hasMessage("TLS MinVersion too low")
                .hasSeverity(Severity.WARNING_HIGH);
    }

    @Override
    protected IssueParser createParser() {
        return new GosecParser();
    }

    @Test
    void accepts() {
        var parser = new GosecParser();
        assertThat(parser.accepts(new FileReaderFactory(FileSystems.getDefault().getPath("gosec-report.json")))).isTrue();
        assertThat(parser.accepts(new FileReaderFactory(FileSystems.getDefault().getPath("foo.txt")))).isFalse();
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt"))
                .isInstanceOf(ParsingException.class);
    }

    @Test
    void emptyInput() {
        var report = parse("issues-no-issues.json");

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleMissingFields() {
        var report = parseStringContent("""
                {
                  "Issues": [
                    {
                      "details": "fallback handling"
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
                .hasColumnEnd(0)
                .hasDescription("");
    }

    @Test
    void shouldParseSingleIssueObject() {
        var report = parseStringContent("""
                {
                  "rule_id": "G101",
                  "details": "single issue object",
                  "severity": "LOW",
                  "file": "main.go",
                  "line": 8,
                  "column": 3
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("main.go")
                .hasType("G101")
                .hasSeverity(Severity.WARNING_LOW)
                .hasMessage("single issue object")
                .hasLineStart(8)
                .hasLineEnd(8)
                .hasColumnStart(3)
                .hasColumnEnd(3);
    }

    @Test
    void shouldParseRootArray() {
        var report = parseStringContent("""
                [
                  {
                    "rule_id": "G401",
                    "details": "weak crypto primitive",
                    "severity": "HIGH",
                    "file": "crypto.go",
                    "line": 11,
                    "column": 5
                  }
                ]
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("crypto.go")
                .hasType("G401")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasMessage("weak crypto primitive")
                .hasLineStart(11)
                .hasColumnStart(5);
    }

    @Test
    void shouldIgnoreNonIssueRootObject() {
        var report = parseStringContent("""
                {
                  "Stats": {
                    "found": 0
                  }
                }
                """);

        assertThat(report).isEmpty();
    }

    @Test
    void shouldProvideDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("gosec");

        assertThat(descriptor.getPattern()).isEqualTo("**/gosec-report.json");
        assertThat(descriptor.getHelp()).contains("gosec -fmt=json -out=gosec-report.json");
        assertThat(descriptor.getUrl()).isEqualTo("https://github.com/securego/gosec");
        assertThat(descriptor.hasHelp()).isTrue();
        assertThat(descriptor.hasUrl()).isTrue();
    }
}
