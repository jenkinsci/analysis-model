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
 * Tests the class {@link TalismanParser}.
 *
 * @author Akash Manna
 */
class TalismanParserTest extends AbstractParserTest {
    TalismanParserTest() {
        super("talisman-report.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(5);

        softly.assertThat(report.get(0))
                .hasFileName("src/main/resources/application.properties")
                .hasType("filecontent")
                .hasMessage("Potential secret pattern 'password' detected in file")
                .hasSeverity(Severity.WARNING_HIGH);

        softly.assertThat(report.get(1))
                .hasFileName("src/main/resources/application.properties")
                .hasType("filecontent")
                .hasMessage("Potential AWS secret key detected")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(2))
                .hasFileName("config/secrets.pem")
                .hasType("filename")
                .hasMessage("Filename matches pattern 'pem' which is usually considered sensitive")
                .hasSeverity(Severity.WARNING_HIGH);

        softly.assertThat(report.get(3))
                .hasFileName("data/large-file.bin")
                .hasType("filesize")
                .hasMessage("File size (12 MB) exceeds configured threshold (1 MB)")
                .hasSeverity(Severity.WARNING_LOW);

        softly.assertThat(report.get(4))
                .hasFileName("tests/test_helper.rb")
                .hasType("filecontent")
                .hasMessage("Potential credential pattern found, please review")
                .hasSeverity(Severity.WARNING_LOW);
    }

    @Override
    protected IssueParser createParser() {
        return new TalismanParser();
    }

    @Test
    void accepts() {
        assertThat(new TalismanParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("talisman-report.json")))).isTrue();
        assertThat(new TalismanParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("foo.txt")))).isFalse();
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt"))
                .isInstanceOf(ParsingException.class);
    }

    @Test
    void emptyInput() throws ParsingException {
        var report = parse("talisman-report-no-issues.json");

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleMissingResultsKey() {
        var report = parseStringContent("""
                {
                    "summary": {
                        "types": {
                            "filecontent": 0,
                            "filesize": 0,
                            "filename": 0
                        }
                    }
                }
                """);

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleResultWithOnlyWarnings() {
        var report = parseStringContent("""
                {
                    "results": [
                        {
                            "filename": "src/main/java/com/example/Helper.java",
                            "failure_list": [],
                            "warning_list": [
                                {
                                    "type": "filecontent",
                                    "message": "Suspicious token detected",
                                    "commits": ["abc123"],
                                    "severity": "high"
                                }
                            ],
                            "ignore_list": []
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("src/main/java/com/example/Helper.java")
                .hasType("filecontent")
                .hasMessage("Suspicious token detected")
                .hasSeverity(Severity.WARNING_LOW);
    }

    @Test
    void shouldHandleUnknownSeverity() {
        var report = parseStringContent("""
                {
                    "results": [
                        {
                            "filename": "secrets.txt",
                            "failure_list": [
                                {
                                    "type": "filecontent",
                                    "message": "Unknown severity test",
                                    "commits": [],
                                    "severity": "critical"
                                }
                            ],
                            "warning_list": [],
                            "ignore_list": []
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("secrets.txt")
                .hasType("filecontent")
                .hasMessage("Unknown severity test")
                .hasSeverity(Severity.ERROR);
    }

    @Test
    void shouldHandleMissingSeverityField() {
        var report = parseStringContent("""
                {
                    "results": [
                        {
                            "filename": "secrets.txt",
                            "failure_list": [
                                {
                                    "type": "filecontent",
                                    "message": "No severity field",
                                    "commits": []
                                }
                            ],
                            "warning_list": [],
                            "ignore_list": []
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("secrets.txt")
                .hasType("filecontent")
                .hasMessage("No severity field")
                .hasSeverity(Severity.WARNING_LOW);
    }

    @Test
    void shouldHandleMissingFilenameField() {
        var report = parseStringContent("""
                {
                    "results": [
                        {
                            "failure_list": [
                                {
                                    "type": "filename",
                                    "message": "Sensitive file extension",
                                    "commits": [],
                                    "severity": "low"
                                }
                            ],
                            "warning_list": [],
                            "ignore_list": []
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("-")
                .hasType("filename")
                .hasMessage("Sensitive file extension")
                .hasSeverity(Severity.WARNING_LOW);
    }

    @Test
    void shouldHandleMissingFailureAndWarningLists() {
        var report = parseStringContent("""
                {
                    "results": [
                        {
                            "filename": "src/test.java",
                            "ignore_list": []
                        }
                    ]
                }
                """);

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleAllSeverityLevels() {
        var report = parseStringContent("""
                {
                    "results": [
                        {
                            "filename": "file.txt",
                            "failure_list": [
                                {
                                    "type": "filecontent",
                                    "message": "High severity issue",
                                    "commits": [],
                                    "severity": "high"
                                },
                                {
                                    "type": "filecontent",
                                    "message": "Medium severity issue",
                                    "commits": [],
                                    "severity": "medium"
                                },
                                {
                                    "type": "filecontent",
                                    "message": "Low severity issue",
                                    "commits": [],
                                    "severity": "low"
                                }
                            ],
                            "warning_list": [],
                            "ignore_list": []
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(3);
        assertThat(report.get(0)).hasSeverity(Severity.WARNING_HIGH);
        assertThat(report.get(1)).hasSeverity(Severity.WARNING_NORMAL);
        assertThat(report.get(2)).hasSeverity(Severity.WARNING_LOW);
    }

    @Test
    void shouldHandleMultipleFilesWithMultipleFailures() {
        var report = parseStringContent("""
                {
                    "results": [
                        {
                            "filename": "first.properties",
                            "failure_list": [
                                {
                                    "type": "filecontent",
                                    "message": "Password found",
                                    "commits": ["commit1"],
                                    "severity": "high"
                                }
                            ],
                            "warning_list": [],
                            "ignore_list": []
                        },
                        {
                            "filename": "second.pem",
                            "failure_list": [
                                {
                                    "type": "filename",
                                    "message": "Sensitive extension",
                                    "commits": ["commit2"],
                                    "severity": "high"
                                }
                            ],
                            "warning_list": [],
                            "ignore_list": []
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(2);
        assertThat(report.get(0)).hasFileName("first.properties").hasSeverity(Severity.WARNING_HIGH);
        assertThat(report.get(1)).hasFileName("second.pem").hasSeverity(Severity.WARNING_HIGH);
    }

    @Test
    void shouldProvideDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("talisman");

        assertThat(descriptor.getPattern()).isEqualTo("**/talisman-report.json");
        assertThat(descriptor.getHelp()).contains("talisman --scanWithHtml");
        assertThat(descriptor.getUrl()).isEqualTo("https://github.com/thoughtworks/talisman");
        assertThat(descriptor.getIconUrl()).isEqualTo("https://raw.githubusercontent.com/jaydeepc/talisman-html-report/master/img/talisman.png");
        assertThat(descriptor.hasHelp()).isTrue();
        assertThat(descriptor.hasUrl()).isTrue();
        assertThat(descriptor.getType()).isEqualTo(IssueType.VULNERABILITY);
    }
}
