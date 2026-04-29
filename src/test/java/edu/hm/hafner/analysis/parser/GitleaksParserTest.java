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
 * Tests the class {@link GitleaksParser}.
 * 
 * @author Akash Manna
 */
class GitleaksParserTest extends AbstractParserTest {
    GitleaksParserTest() {
        super("gitleaks.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(3);

        softly.assertThat(report.get(0))
                .hasFileName("src/main/App.java")
                .hasLineStart(12)
                .hasLineEnd(12)
                .hasType("G101")
                .hasMessage("Potential API key")
                .hasSeverity(Severity.WARNING_HIGH);

        softly.assertThat(report.get(1))
                .hasFileName("config/passwords.yml")
                .hasLineStart(3)
                .hasLineEnd(4)
                .hasType("G102")
                .hasMessage("Hardcoded credentials")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(2))
                .hasFileName("-")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasType("G103")
                .hasMessage("Missing file and line")
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Override
    protected IssueParser createParser() {
        return new GitleaksParser();
    }

    @Test
    void accepts() {
        assertThat(new GitleaksParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("gitleaks.json")))).isTrue();
        assertThat(new GitleaksParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("foo.txt")))).isFalse();
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt"))
                .isInstanceOf(ParsingException.class);
    }

    @Test
    void emptyInput() throws ParsingException {
        var report = parse("gitleaks-no-issues.json");

        assertThat(report).isEmpty();
    }

    @Test
    void shouldProvideDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("gitleaks");

        assertThat(descriptor.getPattern()).isEqualTo("**/gitleaks.json");
        assertThat(descriptor.getHelp()).contains("gitleaks detect --report-format json");
        assertThat(descriptor.getUrl()).isEqualTo("https://github.com/zricethezav/gitleaks");
        assertThat(descriptor.hasHelp()).isTrue();
        assertThat(descriptor.hasUrl()).isTrue();
    }

    @Test
    void shouldHandleMissingFileAndLineInfo() {
        var report = parseStringContent("""
                {
                  "leaks": [
                    {
                      "rule_id": "G104",
                      "description": "No location info"
                    }
                  ],
                  "errors": []
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasType("G104")
                .hasMessage("No location info")
                .hasFileName("-")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Test
    void shouldHandleMissingRuleId() {
        var report = parseStringContent("""
                {
                  "leaks": [
                    {
                      "description": "Missing rule ID",
                      "file": "test.txt",
                      "start_line": 5,
                      "end_line": 5,
                      "severity": "HIGH"
                    }
                  ],
                  "errors": []
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasType("-")
                .hasMessage("Missing rule ID")
                .hasFileName("test.txt")
                .hasLineStart(5)
                .hasLineEnd(5)
                .hasSeverity(Severity.WARNING_HIGH);
    }

    @Test
    void shouldHandleSingleLineNumber() {
        var report = parseStringContent("""
                {
                  "leaks": [
                    {
                      "rule_id": "G105",
                      "description": "Single line number",
                      "file": "config.txt",
                      "line": 42,
                      "severity": "MEDIUM"
                    }
                  ],
                  "errors": []
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasType("G105")
                .hasMessage("Single line number")
                .hasFileName("config.txt")
                .hasLineStart(42)
                .hasLineEnd(42)
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Test
    void shouldMapSeverities() {
        var report = parseStringContent("""
                {
                  "leaks": [
                    {
                      "rule_id": "HIGH_SEV",
                      "description": "High severity",
                      "file": "file1.txt",
                      "line": 1,
                      "severity": "CRITICAL"
                    },
                    {
                      "rule_id": "MEDIUM_SEV",
                      "description": "Medium severity",
                      "file": "file2.txt",
                      "line": 2,
                      "severity": "MEDIUM"
                    },
                    {
                      "rule_id": "LOW_SEV",
                      "description": "Low severity",
                      "file": "file3.txt",
                      "line": 3,
                      "severity": "LOW"
                    }
                  ],
                  "errors": []
                }
                """);

        assertThat(report).hasSize(3);
        assertThat(report.get(0)).hasSeverity(Severity.ERROR);
        assertThat(report.get(1)).hasSeverity(Severity.WARNING_NORMAL);
        assertThat(report.get(2)).hasSeverity(Severity.WARNING_LOW);
    }

    @Test
    void shouldParseArrayFormat() {
        var report = parseStringContent("""
                [
                  {
                    "rule_id": "G106",
                    "description": "Array format leak",
                    "file": "src/main.java",
                    "start_line": 10,
                    "end_line": 10,
                    "severity": "HIGH"
                  },
                  {
                    "rule_id": "G107",
                    "description": "Another leak",
                    "file": "src/config.java",
                    "line": 20
                  }
                ]
                """);

        assertThat(report).hasSize(2);
        assertThat(report.get(0))
                .hasType("G106")
                .hasMessage("Array format leak")
                .hasFileName("src/main.java")
                .hasLineStart(10)
                .hasLineEnd(10);
        assertThat(report.get(1))
                .hasType("G107")
                .hasMessage("Another leak")
                .hasFileName("src/config.java")
                .hasLineStart(20)
                .hasLineEnd(20);
    }
}
