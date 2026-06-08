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
 * Tests the class {@link DetectSecretsParser}.
 *
 * @author Akash Manna
 */
class DetectSecretsParserTest extends AbstractParserTest {
    DetectSecretsParserTest() {
        super("detect-secrets.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(4);

        var types = report.stream().map(issue -> issue.getType()).toList();
        assertThat(types).containsExactlyInAnyOrder(
                "AWS Access Key", "Basic Auth Credentials", "Secret Keyword", "Private Key");

        var awsIssue = report.stream()
                .filter(i -> "AWS Access Key".equals(i.getType()))
                .findFirst()
                .orElseThrow();
        softly.assertThat(awsIssue)
                .hasFileName("src/config.py")
                .hasLineStart(42)
                .hasType("AWS Access Key")
                .hasMessage("AWS Access Key detected")
                .hasSeverity(Severity.ERROR);

        var basicAuthIssue = report.stream()
                .filter(i -> "Basic Auth Credentials".equals(i.getType()))
                .findFirst()
                .orElseThrow();
        softly.assertThat(basicAuthIssue)
                .hasFileName("config/database.yml")
                .hasLineStart(7)
                .hasType("Basic Auth Credentials")
                .hasMessage("Basic Auth Credentials detected")
                .hasSeverity(Severity.WARNING_NORMAL);

        var keywordIssue = report.stream()
                .filter(i -> "Secret Keyword".equals(i.getType()))
                .findFirst()
                .orElseThrow();
        softly.assertThat(keywordIssue)
                .hasFileName("config/database.yml")
                .hasLineStart(15)
                .hasType("Secret Keyword")
                .hasMessage("Secret Keyword detected")
                .hasSeverity(Severity.WARNING_NORMAL);

        var privateKeyIssue = report.stream()
                .filter(i -> "Private Key".equals(i.getType()))
                .findFirst()
                .orElseThrow();
        softly.assertThat(privateKeyIssue)
                .hasFileName("deploy/id_rsa")
                .hasLineStart(1)
                .hasType("Private Key")
                .hasMessage("Private Key detected")
                .hasSeverity(Severity.ERROR);
    }

    @Override
    protected IssueParser createParser() {
        return new DetectSecretsParser();
    }

    @Test
    void accepts() {
        assertThat(new DetectSecretsParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("detect-secrets.json")))).isTrue();
        assertThat(new DetectSecretsParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath(".secrets.baseline")))).isFalse();
        assertThat(new DetectSecretsParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("foo.txt")))).isFalse();
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt"))
                .isInstanceOf(ParsingException.class);
    }

    @Test
    void emptyInput() throws ParsingException {
        var report = parse("detect-secrets-no-issues.json");

        assertThat(report).isEmpty();
    }

    @Test
    void shouldProvideDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("detect-secrets");

        assertThat(descriptor.getPattern()).isEqualTo("**/.secrets.baseline");
        assertThat(descriptor.getHelp()).contains("detect-secrets scan");
        assertThat(descriptor.getUrl()).isEqualTo("https://github.com/Yelp/detect-secrets");
        assertThat(descriptor.hasHelp()).isTrue();
        assertThat(descriptor.hasUrl()).isTrue();
    }

    @Test
    void shouldVerifyDescriptorType() {
        var descriptor = new ParserRegistry().get("detect-secrets");

        assertThat(descriptor.getType()).isEqualTo(Report.IssueType.VULNERABILITY);
    }

    @Test
    void shouldMapVerifiedSecretsToErrorSeverity() {
        var report = parseStringContent("""
                {
                  "version": "1.4.0",
                  "results": {
                    "app/config.py": [
                      {
                        "type": "Stripe Access Key",
                        "filename": "app/config.py",
                        "hashed_secret": "abc123",
                        "is_verified": true,
                        "line_number": 5
                      }
                    ]
                  }
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("app/config.py")
                .hasLineStart(5)
                .hasType("Stripe Access Key")
                .hasMessage("Stripe Access Key detected")
                .hasSeverity(Severity.ERROR);
    }

    @Test
    void shouldMapUnverifiedSecretsToNormalSeverity() {
        var report = parseStringContent("""
                {
                  "version": "1.4.0",
                  "results": {
                    "src/secrets.yml": [
                      {
                        "type": "Generic Secret",
                        "filename": "src/secrets.yml",
                        "hashed_secret": "def456",
                        "is_verified": false,
                        "line_number": 10
                      }
                    ]
                  }
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("src/secrets.yml")
                .hasLineStart(10)
                .hasType("Generic Secret")
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Test
    void shouldHandleMultipleFilesWithMultipleSecrets() {
        var report = parseStringContent("""
                {
                  "version": "1.4.0",
                  "results": {
                    "file1.py": [
                      {
                        "type": "AWS Access Key",
                        "filename": "file1.py",
                        "hashed_secret": "aaa",
                        "is_verified": false,
                        "line_number": 1
                      },
                      {
                        "type": "Secret Keyword",
                        "filename": "file1.py",
                        "hashed_secret": "bbb",
                        "is_verified": true,
                        "line_number": 20
                      }
                    ],
                    "file2.env": [
                      {
                        "type": "Basic Auth Credentials",
                        "filename": "file2.env",
                        "hashed_secret": "ccc",
                        "is_verified": false,
                        "line_number": 3
                      }
                    ]
                  }
                }
                """);

        assertThat(report).hasSize(3);
        assertThat(report.stream().map(i -> i.getFileName()).toList())
                .containsExactlyInAnyOrder("file1.py", "file1.py", "file2.env");
    }

    @Test
    void shouldHandleMissingType() {
        var report = parseStringContent("""
                {
                  "version": "1.4.0",
                  "results": {
                    "unknown.txt": [
                      {
                        "hashed_secret": "xyz789",
                        "is_verified": false,
                        "line_number": 3
                      }
                    ]
                  }
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("unknown.txt")
                .hasType("-")
                .hasMessage("Secret detected")
                .hasLineStart(3)
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Test
    void shouldHandleMissingLineNumber() {
        var report = parseStringContent("""
                {
                  "version": "1.4.0",
                  "results": {
                    "config.ini": [
                      {
                        "type": "High Entropy String",
                        "filename": "config.ini",
                        "hashed_secret": "qqq111",
                        "is_verified": false
                      }
                    ]
                  }
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("config.ini")
                .hasType("High Entropy String")
                .hasLineStart(0);
    }

    @Test
    void shouldHandleMissingIsVerified() {
        var report = parseStringContent("""
                {
                  "version": "1.4.0",
                  "results": {
                    "app.properties": [
                      {
                        "type": "Slack Token",
                        "filename": "app.properties",
                        "hashed_secret": "rrr222",
                        "line_number": 8
                      }
                    ]
                  }
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("app.properties")
                .hasType("Slack Token")
                .hasMessage("Slack Token detected")
                .hasLineStart(8)
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Test
    void shouldHandleJsonWithoutResultsKey() {
        var report = parseStringContent("""
                {
                  "version": "1.4.0",
                  "plugins_used": [],
                  "generated_at": "2024-01-15T10:30:00Z"
                }
                """);

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleNullEntryInSecretsArray() {
        var report = parseStringContent("""
                {
                  "version": "1.4.0",
                  "results": {
                    "src/main.py": [
                      null,
                      {
                        "type": "GitHub Token",
                        "filename": "src/main.py",
                        "hashed_secret": "sss333",
                        "is_verified": true,
                        "line_number": 25
                      }
                    ]
                  }
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("src/main.py")
                .hasType("GitHub Token")
                .hasLineStart(25)
                .hasSeverity(Severity.ERROR);
    }

    @Test
    void shouldHandleEmptySecretsArrayForFile() {
        var report = parseStringContent("""
                {
                  "version": "1.4.0",
                  "results": {
                    "no-secrets.py": []
                  }
                }
                """);

        assertThat(report).isEmpty();
    }

    @Test
    void shouldCorrectlyBuildMessageForKnownSecretTypes() {
        var report = parseStringContent("""
                {
                  "version": "1.4.0",
                  "results": {
                    "test.py": [
                      {
                        "type": "RSA Private Key",
                        "filename": "test.py",
                        "hashed_secret": "ttt444",
                        "is_verified": false,
                        "line_number": 1
                      }
                    ]
                  }
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasMessage("RSA Private Key detected");
    }
}
