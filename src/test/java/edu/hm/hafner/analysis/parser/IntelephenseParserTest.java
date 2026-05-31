package edu.hm.hafner.analysis.parser;

import java.nio.file.FileSystems;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.FileReaderFactory;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;
import edu.hm.hafner.analysis.registry.ParserRegistry;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link IntelephenseParser}.
 *
 * @author Akash Manna
 */
class IntelephenseParserTest extends AbstractParserTest {
    IntelephenseParserTest() {
        super("intelephense-report.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(3).hasDuplicatesSize(0);

        softly.assertThat(report.get(0))
                .hasFileName("src/Controller/HomeController.php")
                .hasLineStart(9)
                .hasLineEnd(9)
                .hasColumnStart(15)
                .hasColumnEnd(19)
                .hasType("P1002")
                .hasMessage("Undefined variable: $user")
                .hasSeverity(Severity.ERROR)
                .hasCategory("intelephense");

        softly.assertThat(report.get(1))
                .hasFileName("src/Controller/HomeController.php")
                .hasLineStart(22)
                .hasLineEnd(22)
                .hasColumnStart(9)
                .hasColumnEnd(13)
                .hasType("P1003")
                .hasMessage("Unused variable $temp")
                .hasSeverity(Severity.WARNING_LOW)
                .hasCategory("intelephense");

        softly.assertThat(report.get(2))
                .hasFileName("src/Service/AuthService.php")
                .hasLineStart(5)
                .hasLineEnd(5)
                .hasColumnStart(12)
                .hasColumnEnd(19)
                .hasType("P1004")
                .hasMessage("Possible null reference")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("intelephense");
    }

    @Override
    protected IssueParser createParser() {
        return new IntelephenseParser();
    }

    @Test
    void accepts() {
        assertThat(new IntelephenseParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("intelephense-report.json")))).isTrue();
        assertThat(new IntelephenseParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("foo.txt")))).isFalse();
    }

    @Test
    void shouldHandleUriFallback() {
        var filePath = FileSystems.getDefault().getPath("src", "FromUri.php");
        var report = parseStringContent("""
                [
                  {
                    "uri": "%s",
                    "diagnostics": [
                      {
                        "message": "URI fallback",
                        "severity": 2,
                        "code": "P2001",
                        "source": "intelephense",
                        "range": {
                          "start": {
                            "line": 0,
                            "character": 0
                          },
                          "end": {
                            "line": 0,
                            "character": 7
                          }
                        }
                      }
                    ]
                  }
                ]
                """.formatted(filePath.toUri()));

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName(filePath.toAbsolutePath().toString().replace('\\', '/'))
                .hasLineStart(1)
                .hasLineEnd(1)
                .hasColumnStart(1)
                .hasColumnEnd(7)
                .hasType("P2001")
                .hasMessage("URI fallback")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("intelephense");
    }

    @Test
    void shouldHandleMissingFields() {
        var report = parseStringContent("""
                {
                  "file_name": "src/Fallback.php",
                  "diagnostics": [
                    {
                      "message": "Fallback diagnostic"
                    }
                  ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("src/Fallback.php")
                .hasType("-")
                .hasMessage("Fallback diagnostic")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasColumnStart(0)
                .hasColumnEnd(0);
    }

    @Test
    void shouldUseContainerSourceAsFallback() {
        var report = parseStringContent("""
                {
                  "source": "intelephense",
                  "diagnostics": [
                    {
                      "message": "Inherited source"
                    }
                  ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasMessage("Inherited source")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("intelephense");
    }

    @Test
    void shouldKeepMalformedFileUrisUnchanged() {
        var report = parseStringContent("""
                {
                  "uri": "file:relative/path.php",
                  "message": "Malformed URI",
                  "severity": 4,
                  "code": "P5001",
                  "source": "intelephense"
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("file:relative/path.php")
                .hasMessage("Malformed URI")
                .hasSeverity(Severity.WARNING_LOW)
                .hasCategory("intelephense");
    }

    @Test
    void shouldParseDirectDiagnosticObject() {
        var report = parseStringContent("""
                {
                  "file": "src/Direct.php",
                  "message": "Direct diagnostic",
                  "severity": "3",
                  "code": "P3001",
                  "source": "intelephense",
                  "range": {
                    "start": {
                      "line": 1,
                      "character": 2
                    },
                    "end": {
                      "line": 1,
                      "character": 8
                    }
                  }
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("src/Direct.php")
                .hasLineStart(2)
                .hasLineEnd(2)
                .hasColumnStart(3)
                .hasColumnEnd(8)
                .hasType("P3001")
                .hasMessage("Direct diagnostic")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("intelephense");
    }

    @Test
    void shouldHandleDiagnosticArraysAndUnknownEntries() {
        var report = parseStringContent("""
                [
                  {
                    "path": "src/ArrayContainer.php",
                    "diagnostics": [
                      {
                        "message": "Array diagnostic",
                        "severity": 1,
                        "code": "P4001",
                        "source": "intelephense",
                        "range": {
                          "start": {
                            "line": 0,
                            "character": 0
                          },
                          "end": {
                            "line": 0,
                            "character": 1
                          }
                        }
                      },
                      null
                    ]
                  },
                  "skip-me",
                  {
                    "file": "src/Low.php",
                    "message": "Low severity diagnostic",
                    "severity": 4,
                    "code": "P4003",
                    "source": "intelephense"
                  },
                  {
                    "file": "src/FallbackNumeric.php",
                    "message": "Fallback numeric diagnostic",
                    "severity": 99,
                    "code": "P4004",
                    "source": "intelephense"
                  },
                  {
                    "uri": "https://example.com/src/Remote.php",
                    "message": "Remote diagnostic",
                    "severity": "error",
                    "code": "P4002",
                    "source": "intelephense",
                    "range": {
                      "start": {
                        "line": 2,
                        "character": 4
                      },
                      "end": {
                        "line": 2,
                        "character": 9
                      }
                    }
                  },
                  {
                    "ignored": true
                  }
                ]
                """);

        assertThat(report).hasSize(4);

        assertThat(report.get(0))
                .hasFileName("src/ArrayContainer.php")
                .hasLineStart(1)
                .hasLineEnd(1)
                .hasColumnStart(1)
                .hasColumnEnd(1)
                .hasType("P4001")
                .hasMessage("Array diagnostic")
                .hasSeverity(Severity.ERROR)
                .hasCategory("intelephense");

        assertThat(report.get(1))
                .hasFileName("src/Low.php")
                .hasType("P4003")
                .hasMessage("Low severity diagnostic")
                .hasSeverity(Severity.WARNING_LOW)
                .hasCategory("intelephense");

        assertThat(report.get(2))
                .hasFileName("src/FallbackNumeric.php")
                .hasType("P4004")
                .hasMessage("Fallback numeric diagnostic")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("intelephense");

        assertThat(report.get(3))
                .hasFileName("https://example.com/src/Remote.php")
                .hasLineStart(3)
                .hasLineEnd(3)
                .hasColumnStart(5)
                .hasColumnEnd(9)
                .hasType("P4002")
                .hasMessage("Remote diagnostic")
                .hasSeverity(Severity.ERROR)
                .hasCategory("intelephense");
    }

    @Test
    void shouldIgnoreNonDiagnosticObject() {
        var report = parseStringContent("""
                {
                  "name": "not-a-diagnostic",
                  "value": 42
                }
                """);

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandlePartialPositions() {
        var report = parseStringContent("""
                {
                  "file": "src/Partial.php",
                  "message": "Partial positions",
                  "severity": 2,
                  "code": "P6001",
                  "source": "intelephense",
                  "range": {
                    "start": {
                      "line": 3
                    },
                    "end": {
                      "character": 7
                    }
                  }
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("src/Partial.php")
                .hasLineStart(4)
                .hasLineEnd(4)
                .hasColumnStart(7)
                .hasColumnEnd(7)
                .hasType("P6001")
                .hasMessage("Partial positions")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("intelephense");
    }

    @Test
    void shouldProvideDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("intelephense");

        assertThat(descriptor.getPattern()).isEqualTo("**/intelephense-report.json");
        assertThat(descriptor.getHelp()).contains("publishDiagnostics");
        assertThat(descriptor.getUrl()).isEqualTo("https://intelephense.com/");
        assertThat(descriptor.hasHelp()).isTrue();
        assertThat(descriptor.hasUrl()).isTrue();
    }

    @Test
    void shouldHandleFileUriWithoutLeadingSlash() {
        var report = parseStringContent("""
                {
                  "uri": "file://relative/path.php",
                  "message": "Relative file uri",
                  "severity": 3,
                  "code": "P7001",
                  "source": "intelephense"
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("relative/path.php")
                .hasType("P7001")
                .hasMessage("Relative file uri")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("intelephense");
    }

    @Test
    void shouldHandleWindowsFileUri() {
        var report = parseStringContent("""
                {
                  "uri": "file:///C:/project/src/Windows.php",
                  "diagnostics": [
                    {
                      "message": "Windows URI",
                      "severity": 1,
                      "code": "P7002",
                      "source": "intelephense",
                      "range": {
                        "start": {"line": 0, "character": 0},
                        "end": {"line": 0, "character": 5}
                      }
                    }
                  ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("C:/project/src/Windows.php")
                .hasLineStart(1)
                .hasLineEnd(1)
                .hasColumnStart(1)
                .hasColumnEnd(5)
                .hasType("P7002")
                .hasMessage("Windows URI")
                .hasSeverity(Severity.ERROR)
                .hasCategory("intelephense");
    }

    @Test
    void shouldCoverLineStartAndEndBranches() {
        var report = parseStringContent("""
                {
                  "file": "src/CoverLines.php",
                  "diagnostics": [
                    {
                      "message": "Cover lines",
                      "severity": 2,
                      "code": "P9001",
                      "source": "intelephense",
                      "range": {
                        "start": { "line": 0, "character": 0 },
                        "end": { "line": 1, "character": 3 }
                      }
                    }
                  ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("src/CoverLines.php")
                .hasLineStart(1)
                .hasLineEnd(2)
                .hasColumnStart(1)
                .hasColumnEnd(3)
                .hasType("P9001")
                .hasMessage("Cover lines")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("intelephense");
    }
}