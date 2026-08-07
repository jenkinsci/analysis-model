package edu.hm.hafner.analysis.parser;

import java.nio.file.FileSystems;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.FileReaderFactory;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Report.IssueType;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;
import edu.hm.hafner.analysis.registry.ParserRegistry;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link PyrightParser}.
 *
 * @author Akash Manna
 */
class PyrightParserTest extends AbstractParserTest {
    PyrightParserTest() {
        super("pyright-report.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(4);

        softly.assertThat(report.get(0))
                .hasFileName("src/main.py")
                .hasLineStart(10)
                .hasLineEnd(10)
                .hasColumnStart(14)
                .hasColumnEnd(21)
                .hasType("reportArgumentType")
                .hasMessage("Argument of type 'str' cannot be assigned to parameter 'count' of type 'int'")
                .hasSeverity(Severity.ERROR);

        softly.assertThat(report.get(1))
                .hasFileName("src/utils.py")
                .hasLineStart(25)
                .hasLineEnd(25)
                .hasColumnStart(4)
                .hasColumnEnd(10)
                .hasType("reportReturnType")
                .hasMessage("Return type 'None' is not compatible with declared return type 'str'")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(2))
                .hasFileName("src/models/user.py")
                .hasLineStart(42)
                .hasLineEnd(42)
                .hasColumnStart(8)
                .hasColumnEnd(12)
                .hasType("-")
                .hasMessage("Type of 'data' is 'dict[str, Any]'")
                .hasSeverity(Severity.WARNING_LOW);

        softly.assertThat(report.get(3))
                .hasFileName("tests/test_service.py")
                .hasLineStart(68)
                .hasLineEnd(68)
                .hasColumnStart(8)
                .hasColumnEnd(20)
                .hasType("reportOptionalMemberAccess")
                .hasMessage("Cannot access member 'send' for type 'None'")
                .hasSeverity(Severity.ERROR);
    }

    @Override
    protected IssueParser createParser() {
        return new PyrightParser();
    }

    @Test
    void accepts() {
        assertThat(new PyrightParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("pyright-report.json")))).isTrue();
        assertThat(new PyrightParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("foo.txt")))).isFalse();
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
    void shouldHandleMissingRuleAndRange() {
        var report = parseStringContent("""
                {
                    "generalDiagnostics": [
                        {
                            "file": "src/app.py",
                            "severity": "warning",
                            "message": "Type is partially unknown"
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("src/app.py")
                .hasType("-")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("Type is partially unknown")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasColumnStart(0)
                .hasColumnEnd(0);
    }

    @Test
    void shouldHandleInformationSeverity() {
        var report = parseStringContent("""
                {
                    "generalDiagnostics": [
                        {
                            "file": "src/info.py",
                            "severity": "information",
                            "message": "Type of 'x' is 'int'",
                            "rule": "reportUnknownVariableType",
                            "range": {
                                "start": { "line": 4, "character": 3 },
                                "end":   { "line": 4, "character": 4 }
                            }
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("src/info.py")
                .hasType("reportUnknownVariableType")
                .hasSeverity(Severity.WARNING_LOW)
                .hasMessage("Type of 'x' is 'int'")
                .hasLineStart(5)
                .hasColumnStart(3);
    }

    @Test
    void shouldHandleZeroLineNumbers() {
        var report = parseStringContent("""
                {
                    "generalDiagnostics": [
                        {
                            "file": "src/zero.py",
                            "severity": "error",
                            "message": "Cannot assign to type 'int'",
                            "rule": "reportAssignmentToType",
                            "range": {
                                "start": { "line": 0, "character": 2 },
                                "end":   { "line": 0, "character": 7 }
                            }
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("src/zero.py")
                .hasSeverity(Severity.ERROR)
                .hasLineStart(1)
                .hasLineEnd(1)
                .hasColumnStart(2)
                .hasColumnEnd(7);
    }

    @Test
    void shouldHandleEmptyDiagnosticsArray() {
        var report = parseStringContent("""
                {
                    "version": "1.1.394",
                    "generalDiagnostics": [],
                    "summary": {
                        "filesAnalyzed": 5,
                        "errorCount": 0,
                        "warningCount": 0,
                        "informationCount": 0
                    }
                }
                """);

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleMissingGeneralDiagnosticsKey() {
        var report = parseStringContent("""
                {
                    "version": "1.1.394",
                    "summary": {
                        "filesAnalyzed": 0,
                        "errorCount": 0,
                        "warningCount": 0,
                        "informationCount": 0
                    }
                }
                """);

        assertThat(report).isEmpty();
    }

    @Test
    void shouldProvideDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("pyright");

        assertThat(descriptor.getPattern()).isEqualTo("**/pyright-report.json");
        assertThat(descriptor.getHelp()).contains("pyright --outputjson");
        assertThat(descriptor.getUrl()).isEqualTo("https://github.com/microsoft/pyright");
        assertThat(descriptor.getIconUrl()).isEqualTo("https://github.com/microsoft/pyright/blob/main/docs/img/PyrightLarge.png");
        assertThat(descriptor.getType()).isEqualTo(IssueType.WARNING);
        assertThat(descriptor.hasHelp()).isTrue();
        assertThat(descriptor.hasUrl()).isTrue();
    }
}
