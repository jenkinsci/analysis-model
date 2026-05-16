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
 * Tests the class {@link TruffleHogParser}.
 *
 * @author Akash Manna
 */
class TruffleHogParserTest extends AbstractParserTest {
    TruffleHogParserTest() {
        super("truffleHog.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(3);

        softly.assertThat(report.get(0))
                .hasFileName("src/config.py")
                .hasLineStart(42)
                .hasType("Basic Auth Credentials")
                .hasMessage("Basic Auth Credentials (detected by: Basic Auth Credentials)")
                .hasSeverity(Severity.ERROR);

        softly.assertThat(report.get(1))
                .hasFileName("src/aws/credentials.json")
                .hasLineStart(5)
                .hasType("AWS Access Key")
                .hasMessage("AWS Access Key (detected by: AWS Key)")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(2))
                .hasFileName("config/keys/id_rsa")
                .hasLineStart(1)
                .hasType("RSA Private Key")
                .hasMessage("RSA Private Key (detected by: Private Key)")
                .hasSeverity(Severity.ERROR);
    }

    @Override
    protected IssueParser createParser() {
        return new TruffleHogParser();
    }

    @Test
    void accepts() {
        assertThat(new TruffleHogParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("truffleHog.json")))).isTrue();
        assertThat(new TruffleHogParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("foo.txt")))).isFalse();
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt"))
                .isInstanceOf(ParsingException.class);
    }

    @Test
    void emptyInput() throws ParsingException {
        var report = parse("truffleHog-no-issues.json");

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleResultsArray() {
        var report = parse("truffleHog.json");

        assertThat(report).hasSize(3).hasDuplicatesSize(0);
    }

    @Test
    void shouldProvideDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("truffleHog");

        assertThat(descriptor.getPattern()).isEqualTo("**/truffleHog.json");
        assertThat(descriptor.getHelp()).contains("truffleHog filesystem");
        assertThat(descriptor.getUrl()).isEqualTo("https://github.com/trufflesecurity/trufflehog");
        assertThat(descriptor.hasHelp()).isTrue();
        assertThat(descriptor.hasUrl()).isTrue();
    }

    @Test
    void shouldVerifyDescriptorType() {
        var descriptor = new ParserRegistry().get("truffleHog");

        assertThat(descriptor.getType()).isEqualTo(Report.IssueType.VULNERABILITY);
    }

    @Test
    void shouldHandleMissingFields() {
        var report = parseStringContent("""
                {
                  "results": [
                    {
                      "findings": [
                        {
                          "secret": "test_secret"
                        }
                      ]
                    }
                  ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("-")
                .hasLineStart(0)
                .hasType("-")
                .hasMessage("Secret detected")
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Test
    void shouldHandleMultipleResultSources() {
        var report = parseStringContent("""
                {
                  "results": [
                    {
                      "sourceType": "git",
                      "findings": [
                        {
                          "type": "GitHub Token",
                          "found_by": "GitHub",
                          "file_path": "config.yml",
                          "line_number": 10,
                          "is_verified": false
                        }
                      ]
                    },
                    {
                      "sourceType": "filesystem",
                      "findings": [
                        {
                          "type": "Slack Token",
                          "found_by": "Slack",
                          "file_path": "env",
                          "line_number": 2,
                          "is_verified": true
                        }
                      ]
                    }
                  ]
                }
                """);

        assertThat(report).hasSize(2);

        assertThat(report.get(0))
                .hasFileName("config.yml")
                .hasLineStart(10)
                .hasType("GitHub Token")
                .hasSeverity(Severity.WARNING_NORMAL);

        assertThat(report.get(1))
                .hasFileName("env")
                .hasLineStart(2)
                .hasType("Slack Token")
                .hasSeverity(Severity.ERROR);
    }

    @Test
    void shouldVerifyVerificationStatus() {
        var report = parse("truffleHog.json");

        assertThat(report.get(0)).hasSeverity(Severity.ERROR);

        assertThat(report.get(1)).hasSeverity(Severity.WARNING_NORMAL);

        assertThat(report.get(2)).hasSeverity(Severity.ERROR);
    }

    @Test
    void shouldHandleOnlyTypeField() {
        var report = parseStringContent("""
                {
                  "results": [
                    {
                      "findings": [
                        {
                          "type": "Secret Token",
                          "file_path": "app.py",
                          "line_number": 15,
                          "is_verified": true
                        }
                      ]
                    }
                  ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasType("Secret Token")
                .hasMessage("Secret Token");
    }

    @Test
    void shouldHandleOnlyFoundByField() {
        var report = parseStringContent("""
                {
                  "results": [
                    {
                      "findings": [
                        {
                          "found_by": "Custom Detector",
                          "file_path": "secrets.conf",
                          "line_number": 8,
                          "is_verified": false
                        }
                      ]
                    }
                  ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasType("Custom Detector")
                .hasMessage("Secret detected by: Custom Detector");
    }

    @Test
    void shouldHandleJsonWithoutResults() {
        var report = parseStringContent("""
                {
                  "metadata": {
                    "version": "1.0"
                  }
                }
                """);

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleResultWithoutFindings() {
        var report = parseStringContent("""
                {
                  "results": [
                    {
                      "sourceType": "git"
                    },
                    {
                      "findings": [
                        {
                          "type": "API Key",
                          "found_by": "API Detector",
                          "file_path": "config.py",
                          "line_number": 20,
                          "is_verified": true
                        }
                      ]
                    }
                  ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasType("API Key")
                .hasFileName("config.py");
    }

    @Test
    void shouldHandleJsonArray() {
        var report = parseStringContent("""
                [
                  {
                    "findings": [
                      {
                        "type": "Database Password",
                        "found_by": "DB Detector",
                        "file_path": "db_config.yaml",
                        "line_number": 5,
                        "is_verified": false
                      }
                    ]
                  }
                ]
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasType("Database Password")
                .hasFileName("db_config.yaml")
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Test
    void shouldHandleNullResultInArray() {
        var report = parseStringContent("""
                {
                  "results": [
                    null,
                    {
                      "findings": [
                        {
                          "type": "SSH Key",
                          "found_by": "SSH Detector",
                          "file_path": "id_rsa",
                          "line_number": 1,
                          "is_verified": true
                        }
                      ]
                    }
                  ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasType("SSH Key")
                .hasFileName("id_rsa");
    }
}
