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
 * Tests the class {@link DetectifyParser}.
 *
 * @author Akash Manna
 */
class DetectifyParserTest extends AbstractParserTest {
    DetectifyParserTest() {
        super("detectify-report.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(3);

        softly.assertThat(report.get(0))
                .hasFileName("https://app.example.com/login")
                .hasSeverity(Severity.ERROR)
                .hasCategory("application-scanning")
                .hasType("DEF-001")
                .hasMessage("SQL Injection");

        softly.assertThat(report.get(0).getDescription())
                .contains("Description:")
                .contains("Unsanitized input reaches a SQL query.")
                .contains("Remediation:")
                .contains("Use parameterized queries.")
                .contains("Proof of Concept:")
                .contains("CWE-89")
                .contains("OWASP:")
                .contains("A03:2021 Injection")
                .contains("References:");

        softly.assertThat(report.get(1))
                .hasFileName("https://admin.example.com/profile")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("surface-monitoring")
                .hasType("DEF-002")
                .hasMessage("Cross-Site Scripting");

        softly.assertThat(report.get(1).getDescription())
                .contains("Reflected payload is rendered in the response.")
                .contains("HTML-encode user-controlled output.")
                .contains("CWE-79");

        softly.assertThat(report.get(2))
                .hasFileName("https://portal.example.com/redirect")
                .hasSeverity(Severity.WARNING_LOW)
                .hasCategory("api-scanning")
                .hasType("DEF-003")
                .hasMessage("Open Redirect");
    }

    @Test
    void shouldHandleSingleVulnerabilityObjects() {
        var report = parseStringContent("""
                {
                  "vulnerability": {
                    "uuid": "4a70d4a8-5c5a-4f6c-8d4c-0f3d33f1d111",
                    "title": "Directory Listing",
                    "severity": "low",
                    "host": "files.example.com",
                    "scan_source": "application-scanning",
                    "status": "active",
                    "details": {
                      "description": "A directory listing is exposed."
                    }
                  }
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("files.example.com")
                .hasSeverity(Severity.WARNING_LOW)
                .hasCategory("application-scanning")
                .hasType("4a70d4a8-5c5a-4f6c-8d4c-0f3d33f1d111")
                .hasMessage("Directory Listing");
        assertThat(report.get(0).getDescription()).contains("A directory listing is exposed.");
    }

    @Test
    void shouldHandleMissingLocationAndDetails() {
        var report = parseStringContent("""
                {
                  "vulnerabilities": [
                    {
                      "uuid": "2d2c0fbb-5f11-4c2e-b4d1-8e822ceced42",
                      "title": "Weak TLS Configuration",
                      "severity": "critical",
                      "host": "secure.example.com",
                      "scan_source": "surface-monitoring",
                      "status": "regression"
                    }
                  ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("secure.example.com")
                .hasSeverity(Severity.ERROR)
                .hasCategory("surface-monitoring")
                .hasType("2d2c0fbb-5f11-4c2e-b4d1-8e822ceced42")
                .hasMessage("Weak TLS Configuration");
        assertThat(report.get(0).getDescription()).isEmpty();
    }

    @Test
    void shouldParseEdgeCasesAndBasicFields() {
        var report = parse("detectify-report-coverage.json");
        assertThat(report).hasSize(14);

        var edge = report.get(0);
        assertThat(edge).hasFileName("https://edge.com/p");
        var edgeDesc = edge.getDescription();
        assertThat(edgeDesc).contains("References:", "A01:2021").doesNotContain("CWE-");

        var full = report.get(1);
        assertThat(full).hasFileName("https://full.com/i");
        assertThat(full.getDescription()).contains("CWE-89");

        var minimal = report.get(2);
        assertThat(minimal).hasFileName("https://min.com/p").hasMessage("Minimal");
        assertThat(minimal.getDescription()).isEmpty();
    }

    @Test
    void shouldHandleDefinitionFields() {
        var report = parse("detectify-report-coverage.json");
        assertThat(report.get(4)).hasType("slug-id");
        assertThat(report.get(7)).hasType("type-id");
        assertThat(report.get(8)).hasType("id-id");
    }

    @Test
    void shouldHandleReferencesAndLinks() {
        var report = parse("detectify-report-coverage.json");
        assertThat(report.get(5).getDescription()).doesNotContain("OWASP:");
        assertThat(report.get(6).getDescription()).contains("References:", "Label");
        assertThat(report.get(10).getDescription()).contains("References:", "Links:");
        assertThat(report.get(11).getDescription()).contains("OWASP:", "A01:2021");
        assertThat(report.get(12).getDescription()).doesNotContain("References:");
        assertThat(report.get(13).getDescription()).contains("Links:", "https://b.com");
    }

    @Test
    void shouldCoverMissingBranches() {
        var report = parse("detectify-report-coverage.json");
        assertThat(report).hasSize(14);
        assertThat(report.get(0).getDescription()).contains("References:", "A01:2021");
        assertThat(report.get(3).getDescription()).contains("http://p.com/p");
    }

    @Test
    void accepts() {
        assertThat(new DetectifyParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("detectify-report.json")))).isTrue();
        assertThat(new DetectifyParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("foo.txt")))).isFalse();
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt"))
                .isInstanceOf(ParsingException.class);
    }

    @Override
    protected IssueParser createParser() {
        return new DetectifyParser();
    }

    @Test
    void shouldProvideDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("detectify");

        assertThat(descriptor.getPattern()).isEqualTo("**/detectify-report.json");
        assertThat(descriptor.getHelp()).contains("GET /vulnerabilities/");
        assertThat(descriptor.getUrl()).isEqualTo("https://developer.detectify.com/v2");
        assertThat(descriptor.hasHelp()).isTrue();
        assertThat(descriptor.hasUrl()).isTrue();
    }
}