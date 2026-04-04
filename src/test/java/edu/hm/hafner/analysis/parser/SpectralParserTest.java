package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.FileReaderFactory;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;
import edu.hm.hafner.analysis.registry.ParserRegistry;

import java.nio.file.FileSystems;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link SpectralParser}.
 *
 * @author Akash Manna
 */
class SpectralParserTest extends AbstractParserTest {
    SpectralParserTest() {
        super("spectral-report.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(4);

        softly.assertThat(report.get(0))
                .hasFileName("openapi/petstore.yaml")
                .hasLineStart(12)
                .hasColumnStart(4)
                .hasLineEnd(12)
                .hasColumnEnd(16)
                .hasType("oas3-operation-security-defined")
                .hasMessage("Operation must define security requirements.")
                .hasSeverity(Severity.ERROR);

        softly.assertThat(report.get(1))
                .hasFileName("openapi/petstore.yaml")
                .hasLineStart(2)
                .hasColumnStart(2)
                .hasLineEnd(2)
                .hasColumnEnd(14)
                .hasType("info-contact")
                .hasMessage("Info object should contain contact details.")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(2))
                .hasFileName("openapi/petstore.yaml")
                .hasLineStart(25)
                .hasColumnStart(6)
                .hasLineEnd(25)
                .hasColumnEnd(18)
                .hasType("operation-tags")
                .hasMessage("Operation should have non-empty tags array.")
                .hasSeverity(Severity.WARNING_LOW);

        softly.assertThat(report.get(3))
                .hasFileName("-")
                .hasLineStart(0)
                .hasColumnStart(0)
                .hasLineEnd(0)
                .hasColumnEnd(0)
                .hasType("-")
                .hasMessage("Fallback when rule and range are missing.")
                .hasSeverity(Severity.WARNING_LOW);
    }

    @Override
    protected IssueParser createParser() {
        return new SpectralParser();
    }

    @Test
    void accepts() {
        var parser = new SpectralParser();
        assertThat(parser.accepts(read("spectral-report.json"))).isTrue();
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
    void shouldHandleWrappedResultsObject() {
        var report = parseStringContent("""
                {
                    "results": [
                        {
                            "code": "operation-description",
                            "message": "Operation should have a description.",
                            "severity": 3,
                            "source": "openapi/petstore.yaml",
                            "range": {
                                "start": {
                                    "line": 30,
                                    "character": 8
                                },
                                "end": {
                                    "line": 30,
                                    "character": 19
                                }
                            }
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("openapi/petstore.yaml")
                .hasType("operation-description")
                .hasMessage("Operation should have a description.")
                .hasSeverity(Severity.WARNING_LOW)
                .hasLineStart(30)
                .hasColumnStart(8)
                .hasLineEnd(30)
                .hasColumnEnd(19);
    }

    @Test
    void shouldDefaultToWarningForUnknownSeverities() {
        var report = parseStringContent("""
                [
                    {
                        "code": "unmapped-severity",
                        "message": "Unknown string severity should be warning.",
                        "severity": "severe"
                    },
                    {
                        "code": "unmapped-numeric",
                        "message": "Unknown numeric severity should be warning.",
                        "severity": 99
                    }
                ]
                """);

        assertThat(report).hasSize(2);
        assertThat(report.get(0)).hasSeverity(Severity.WARNING_NORMAL);
        assertThat(report.get(1)).hasSeverity(Severity.WARNING_NORMAL);
    }

    @Test
    void shouldHandleNullResultsArray() {
        var report = parseStringContent("""
                {
                    "results": null
                }
                """);

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleNonObjectArrayEntry() {
        var report = parseStringContent("""
                [
                    17
                ]
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasType("-")
                .hasFileName("-")
                .hasMessage("")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(0)
                .hasColumnStart(0)
                .hasLineEnd(0)
                .hasColumnEnd(0);
    }

    @Test
    void shouldDefaultToWarningForMissingAndUnsupportedSeverityValues() {
        var report = parseStringContent("""
                [
                    {
                        "code": "missing-severity",
                        "message": "No severity field"
                    },
                    {
                        "code": "unsupported-type",
                        "message": "JSONArray severity",
                        "severity": []
                    }
                ]
                """);

        assertThat(report).hasSize(2);
        assertThat(report.get(0)).hasSeverity(Severity.WARNING_NORMAL);
        assertThat(report.get(1)).hasSeverity(Severity.WARNING_NORMAL);
    }

    @Test
    void shouldMapAllKnownStringSeverityAliases() {
        var report = parseStringContent("""
                [
                    {
                        "code": "s0",
                        "message": "0 alias",
                        "severity": "0"
                    },
                    {
                        "code": "serror",
                        "message": "error alias",
                        "severity": "error"
                    },
                    {
                        "code": "sfatal",
                        "message": "fatal alias",
                        "severity": "fatal"
                    },
                    {
                        "code": "s1",
                        "message": "1 alias",
                        "severity": "1"
                    },
                    {
                        "code": "swarn",
                        "message": "warn alias",
                        "severity": "warn"
                    },
                    {
                        "code": "swarning",
                        "message": "warning alias",
                        "severity": "warning"
                    },
                    {
                        "code": "s2",
                        "message": "2 alias",
                        "severity": "2"
                    },
                    {
                        "code": "s3",
                        "message": "3 alias",
                        "severity": "3"
                    },
                    {
                        "code": "sinfo",
                        "message": "info alias",
                        "severity": "info"
                    },
                    {
                        "code": "sinformation",
                        "message": "information alias",
                        "severity": "information"
                    },
                    {
                        "code": "shint",
                        "message": "hint alias",
                        "severity": "hint"
                    }
                ]
                """);

        assertThat(report).hasSize(11);
        assertThat(report.get(0)).hasSeverity(Severity.ERROR);
        assertThat(report.get(1)).hasSeverity(Severity.ERROR);
        assertThat(report.get(2)).hasSeverity(Severity.ERROR);
        assertThat(report.get(3)).hasSeverity(Severity.WARNING_NORMAL);
        assertThat(report.get(4)).hasSeverity(Severity.WARNING_NORMAL);
        assertThat(report.get(5)).hasSeverity(Severity.WARNING_NORMAL);
        assertThat(report.get(6)).hasSeverity(Severity.WARNING_LOW);
        assertThat(report.get(7)).hasSeverity(Severity.WARNING_LOW);
        assertThat(report.get(8)).hasSeverity(Severity.WARNING_LOW);
        assertThat(report.get(9)).hasSeverity(Severity.WARNING_LOW);
        assertThat(report.get(10)).hasSeverity(Severity.WARNING_LOW);
    }

    @Test
    void shouldHandleMissingStartAndEndRanges() {
        var report = parseStringContent("""
                [
                    {
                        "code": "range-missing-boundaries",
                        "message": "Range exists but start and end are absent.",
                        "severity": 1,
                        "source": "openapi/petstore.yaml",
                        "range": {}
                    }
                ]
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasLineStart(0)
                .hasColumnStart(0)
                .hasLineEnd(0)
                .hasColumnEnd(0)
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Test
    void shouldHandleOnlyStartOrOnlyEndRange() {
        var report = parseStringContent("""
                [
                    {
                        "code": "only-start",
                        "message": "Only start is present.",
                        "severity": 1,
                        "source": "openapi/petstore.yaml",
                        "range": {
                            "start": {
                                "line": 8,
                                "character": 2
                            }
                        }
                    },
                    {
                        "code": "only-end",
                        "message": "Only end is present.",
                        "severity": 1,
                        "source": "openapi/petstore.yaml",
                        "range": {
                            "end": {
                                "line": 12,
                                "character": 5
                            }
                        }
                    }
                ]
                """);

        assertThat(report).hasSize(2);
        assertThat(report.get(0))
                .hasLineStart(8)
                .hasColumnStart(2)
                .hasLineEnd(8)
                .hasColumnEnd(2);
        assertThat(report.get(1))
                .hasLineStart(12)
                .hasColumnStart(5)
                .hasLineEnd(12)
                .hasColumnEnd(5);
    }

    @Test
    void shouldProvideDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("spectral");

        assertThat(descriptor.getPattern()).isEqualTo("**/spectral-report.json");
        assertThat(descriptor.getHelp()).contains("spectral lint api.yaml --format json");
        assertThat(descriptor.getUrl()).isEqualTo("https://github.com/stoplightio/spectral");
        assertThat(descriptor.hasHelp()).isTrue();
        assertThat(descriptor.hasUrl()).isTrue();
    }
}