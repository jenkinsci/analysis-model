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
 * Tests the class {@link SaltLintParser}.
 *
 * @author Akash Manna
 */
class SaltLintParserTest extends AbstractParserTest {
    SaltLintParserTest() {
        super("salt-lint-report.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(5);

        softly.assertThat(report.get(0))
                .hasFileName("states/apache.sls")
                .hasLineStart(4)
                .hasColumnStart(1)
                .hasType("201")
                .hasMessage("Trailing whitespace")
                .hasSeverity(Severity.WARNING_LOW);

        softly.assertThat(report.get(1))
                .hasFileName("states/apache.sls")
                .hasLineStart(10)
                .hasColumnStart(5)
                .hasType("202")
                .hasMessage("Jinja statement should have spaces before and after: {% %}")
                .hasSeverity(Severity.WARNING_LOW);

        softly.assertThat(report.get(2))
                .hasFileName("states/mysql.sls")
                .hasLineStart(3)
                .hasColumnStart(1)
                .hasType("207")
                .hasMessage("File modes should be quoted")
                .hasSeverity(Severity.WARNING_HIGH);

        softly.assertThat(report.get(3))
                .hasFileName("states/mysql.sls")
                .hasLineStart(7)
                .hasColumnStart(1)
                .hasType("208")
                .hasMessage("File modes should have a leading zero")
                .hasSeverity(Severity.WARNING_HIGH);

        softly.assertThat(report.get(4))
                .hasFileName("states/nginx.sls")
                .hasLineStart(15)
                .hasColumnStart(3)
                .hasType("801")
                .hasMessage("Deprecated quiet argument for cmd.run")
                .hasSeverity(Severity.WARNING_HIGH);
    }

    @Override
    protected IssueParser createParser() {
        return new SaltLintParser();
    }

    @Test
    void accepts() {
        assertThat(new SaltLintParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("salt-lint-report.json")))).isTrue();
        assertThat(new SaltLintParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("foo.txt")))).isFalse();
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt"))
                .isInstanceOf(ParsingException.class);
    }

    @Test
    void emptyInput() {
        var report = parseStringContent("[]");

        assertThat(report).isEmpty();
    }

    @Test
    void shouldParseEdgeCases() {
        var report = parse("salt-lint-report-edge-cases.json");

        assertThat(report).hasSize(3);

        assertThat(report.get(0))
                .hasFileName("states/test.sls")
                .hasLineStart(1)
                .hasColumnStart(1)
                .hasType("204")
                .hasMessage("Line too long (190 > 160 characters)")
                .hasSeverity(Severity.WARNING_LOW);

        assertThat(report.get(1))
                .hasFileName("states/test.sls")
                .hasLineStart(5)
                .hasColumnStart(1)
                .hasType("-")
                .hasMessage("Rule with no id field")
                .hasSeverity(Severity.WARNING_LOW);

        assertThat(report.get(2))
                .hasLineStart(0)
                .hasColumnStart(0)
                .hasType("213")
                .hasMessage("Use cmd.run with onchanges instead of cmd.wait")
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Test
    void shouldHandleSeverityMapping() {
        var report = parseStringContent("""
                [
                    {
                        "id": "101",
                        "file": "init.sls",
                        "line": 1,
                        "column": 1,
                        "severity": "high",
                        "message": "High severity issue",
                        "rule": { "id": "101", "tags": [], "shortdesc": "High" }
                    },
                    {
                        "id": "102",
                        "file": "init.sls",
                        "line": 2,
                        "column": 1,
                        "severity": "medium",
                        "message": "Medium severity issue",
                        "rule": { "id": "102", "tags": [], "shortdesc": "Medium" }
                    },
                    {
                        "id": "103",
                        "file": "init.sls",
                        "line": 3,
                        "column": 1,
                        "severity": "low",
                        "message": "Low severity issue",
                        "rule": { "id": "103", "tags": [], "shortdesc": "Low" }
                    },
                    {
                        "id": "104",
                        "file": "init.sls",
                        "line": 4,
                        "column": 1,
                        "message": "No severity defaults to low",
                        "rule": { "id": "104", "tags": [], "shortdesc": "No severity" }
                    }
                ]
                """);

        assertThat(report).hasSize(4);
        assertThat(report.get(0)).hasSeverity(Severity.WARNING_HIGH);
        assertThat(report.get(1)).hasSeverity(Severity.WARNING_NORMAL);
        assertThat(report.get(2)).hasSeverity(Severity.WARNING_LOW);
        assertThat(report.get(3)).hasSeverity(Severity.WARNING_LOW);
    }

    @Test
    void shouldProvideDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("salt-lint");

        assertThat(descriptor.getPattern()).isEqualTo("**/salt-lint-report.json");
        assertThat(descriptor.getHelp()).contains("salt-lint --json");
        assertThat(descriptor.getUrl()).isEqualTo("https://github.com/warpnet/salt-lint");
        assertThat(descriptor.hasHelp()).isTrue();
        assertThat(descriptor.hasUrl()).isTrue();
    }

    @Test
    void shouldHandleRuleIdFallback() {
        var report = parseStringContent("""
                [
                    {
                        "id": "999",
                        "file": "fallback.sls",
                        "line": 1,
                        "column": 1,
                        "severity": "low",
                        "message": "Rule id from top-level id field"
                    }
                ]
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasType("999")
                .hasFileName("fallback.sls");
    }
}
