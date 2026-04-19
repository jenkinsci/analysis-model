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
 * Tests the class {@link PhanParser}.
 *
 * @author Akash Manna
 */
class PhanParserTest extends AbstractParserTest {
    PhanParserTest() {
        super("phan-report.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(3).hasDuplicatesSize(0);

        softly.assertThat(report.get(0))
                .hasFileName("src/Foo.php")
                .hasLineStart(12)
                .hasLineEnd(12)
                .hasColumnStart(4)
                .hasColumnEnd(4)
                .hasType("PhanSyntaxError")
                .hasSeverity(Severity.ERROR)
                .hasMessage("Unexpected token '}'");

        softly.assertThat(report.get(0).getDescription())
                .isEqualTo("Syntax PhanSyntaxError Unexpected token '}'");

        softly.assertThat(report.get(1))
                .hasFileName("src/Bar.php")
                .hasLineStart(27)
                .hasLineEnd(27)
                .hasType("PhanTypeMismatchArgument")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("Argument 1 passed to foo() must be of the type string, int given");

        softly.assertThat(report.get(2))
                .hasFileName("src/Baz.php")
                .hasLineStart(8)
                .hasLineEnd(8)
                .hasColumnStart(9)
                .hasColumnEnd(9)
                .hasType("PhanUndeclaredVariable")
                .hasSeverity(Severity.WARNING_LOW)
                .hasMessage("Variable $value is not defined");
    }

    @Override
    protected IssueParser createParser() {
        return new PhanParser();
    }

    @Test
    void accepts() {
        assertThat(new PhanParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("phan-report.json")))).isTrue();
        assertThat(new PhanParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("foo.txt")))).isFalse();
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt"))
                .isInstanceOf(ParsingException.class);
    }

    @Test
    void emptyInput() {
        var report = parseStringContent("{}");

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleIssueWithoutLocation() {
        var report = parseStringContent("""
                {
                    "type": "issue",
                    "check_name": "PhanBareIssue",
                    "description": "",
                    "severity": 3
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasType("PhanBareIssue")
                .hasSeverity(Severity.WARNING_LOW)
                .hasMessage("");
    }

    @Test
    void shouldHandleColumnOnlyLocation() {
        var report = parseStringContent("""
                {
                    "type": "issue",
                    "check_name": "PhanColumnOnlyIssue",
                    "description": "Syntax PhanColumnOnlyIssue column only",
                    "severity": 4,
                    "location": {
                        "path": "src/ColumnOnly.php",
                        "lines": {
                            "column": 7
                        }
                    }
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("src/ColumnOnly.php")
                .hasColumnStart(7)
                .hasColumnEnd(7)
                .hasType("PhanColumnOnlyIssue")
                .hasSeverity(Severity.WARNING_LOW)
                .hasMessage("column only");
    }

    @Test
    void shouldSkipNonObjectEntriesInIssueArray() {
        var report = parseStringContent("""
                [
                    "skip me",
                    {
                        "type": "issue",
                        "check_name": "PhanArrayIssue",
                        "description": "Syntax PhanArrayIssue valid issue",
                        "severity": 10,
                        "location": {
                            "path": "src/Array.php",
                            "lines": {
                                "begin": 2,
                                "end": 2,
                                "begin_column": 1
                            }
                        }
                    }
                ]
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("src/Array.php")
                .hasLineStart(2)
                .hasLineEnd(2)
                .hasColumnStart(1)
                .hasColumnEnd(1)
                .hasType("PhanArrayIssue")
                .hasSeverity(Severity.ERROR)
                .hasMessage("valid issue");
    }

    @Test
    void shouldHandleIssueDetectionFallbacksAndDefaults() {
        var report = parseStringContent("""
                [
                    {
                        "type": "issue"
                    },
                    {
                        "description": "Standalone description issue"
                    },
                    {
                        "location": {
                            "path": "src/OnlyLocation.php"
                        }
                    },
                    {}
                ]
                """);

        assertThat(report).hasSize(3);

        assertThat(report.get(0))
                .hasType("issue")
                .hasSeverity(Severity.WARNING_LOW)
                .hasMessage("");

        assertThat(report.get(1))
                .hasType("-")
                .hasSeverity(Severity.WARNING_LOW)
                .hasMessage("issue")
                .hasDescription("Standalone description issue");

        assertThat(report.get(2))
                .hasType("-")
                .hasSeverity(Severity.WARNING_LOW)
                .hasFileName("src/OnlyLocation.php")
                .hasMessage("")
                .hasDescription("");
    }

    @Test
    void shouldKeepDescriptionIfCheckNameIsAtEnd() {
        var report = parseStringContent("""
                {
                    "type": "issue",
                    "check_name": "PhanTailIssue",
                    "description": "PrefixOnly PhanTailIssue",
                    "severity": "high",
                    "location": {
                        "path": "src/Tail.php",
                        "lines": {
                            "begin": 3,
                            "end": 3,
                            "begin_column": 1
                        }
                    }
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasType("PhanTailIssue")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasMessage("PrefixOnly PhanTailIssue");
    }

    @Test
    void shouldHandleSingleIssueObjectWithNumericStringSeverity() {
        var report = parseStringContent("""
                {
                    "type": "issue",
                    "check_name": "PhanSyntaxError",
                    "description": "Syntax unrelated fallback message",
                    "severity": "7",
                    "location": {
                        "path": "src/Value.php",
                        "lines": {
                            "begin": 5,
                            "end": 6,
                            "column": 11
                        }
                    }
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("src/Value.php")
                .hasLineStart(5)
                .hasLineEnd(6)
                .hasColumnStart(11)
                .hasColumnEnd(11)
                .hasType("PhanSyntaxError")
                .hasSeverity(Severity.ERROR)
                .hasMessage("fallback message");
    }

    @Test
    void shouldHandleNamedSeverityString() {
        var report = parseStringContent("""
                [
                    {
                        "type": "issue",
                        "check_name": "PhanMediumIssue",
                        "description": "Syntax PhanMediumIssue medium warning",
                        "severity": "medium",
                        "location": {
                            "path": "src/Medium.php",
                            "lines": {
                                "begin": 9,
                                "end": 9,
                                "begin_column": 2
                            }
                        }
                    }
                ]
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("src/Medium.php")
                .hasLineStart(9)
                .hasLineEnd(9)
                .hasColumnStart(2)
                .hasColumnEnd(2)
                .hasType("PhanMediumIssue")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("medium warning");
    }

    @Test
    void shouldHandleRawIssueArray() {
        var report = parseStringContent("""
                [
                    {
                        "type": "issue",
                        "check_name": "PhanSyntaxError",
                        "description": "Syntax PhanSyntaxError fake error",
                        "severity": 10,
                        "location": {
                            "path": "src/Single.php",
                            "lines": {
                                "begin": 1,
                                "end": 1,
                                "begin_column": 3
                            }
                        }
                    }
                ]
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("src/Single.php")
                .hasLineStart(1)
                .hasLineEnd(1)
                .hasColumnStart(3)
                .hasColumnEnd(3)
                .hasType("PhanSyntaxError")
                .hasSeverity(Severity.ERROR)
                .hasMessage("fake error");
    }

    @Test
    void shouldProvideDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("phan");

        assertThat(descriptor.getPattern()).isEqualTo("**/phan-report.json");
        assertThat(descriptor.getHelp()).contains("phan --output-mode json");
        assertThat(descriptor.getUrl()).isEqualTo("https://github.com/phan/phan");
        assertThat(descriptor.hasHelp()).isTrue();
        assertThat(descriptor.hasUrl()).isTrue();
    }
}