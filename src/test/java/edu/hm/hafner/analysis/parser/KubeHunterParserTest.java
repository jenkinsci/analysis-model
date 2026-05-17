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
 * Tests the class {@link KubeHunterParser}.
 *
 * @author Akash Manna
 */
class KubeHunterParserTest extends AbstractParserTest {
    KubeHunterParserTest() {
        super("kube-hunter.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(3);

        softly.assertThat(report.get(0))
                .hasFileName("10.0.0.12:10250")
                .hasCategory("Discovery")
                .hasType("KHV002")
                .hasMessage("Kubelet Exposed")
                .hasDescription("The kubelet API is accessible without authentication")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(1))
                .hasFileName("10.0.0.12:6443")
                .hasCategory("Privilege Escalation")
                .hasType("KHV005")
                .hasMessage("Access to API server with weak RBAC")
                .hasDescription("Anonymous users can list cluster roles")
            .hasSeverity(Severity.WARNING_HIGH);

        softly.assertThat(report.get(2))
                .hasFileName("10.0.0.15:8001")
                .hasCategory("Information Disclosure")
                .hasType("KHV053")
                .hasMessage("AWS Metadata Exposure")
                .hasDescription("Access to cloud metadata endpoint from cluster node")
                .hasSeverity(Severity.WARNING_LOW);
    }

    @Test
    void accepts() {
        var parser = new KubeHunterParser();

        assertThat(parser.accepts(new FileReaderFactory(FileSystems.getDefault().getPath("kube-hunter.json"))))
                .isTrue();
        assertThat(parser.accepts(new FileReaderFactory(FileSystems.getDefault().getPath("kube-hunter.txt"))))
                .isFalse();
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt")).isInstanceOf(ParsingException.class);
    }

    @Test
    void emptyInput() {
        var report = parseStringContent("{\"vulnerabilities\": []}");

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleMissingOptionalFields() {
        var report = parse("kube-hunter-missing-fields.json");

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("-")
                .hasCategory("-")
                .hasType("-")
                .hasMessage("-")
                .hasDescription("")
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Test
    void shouldHandleMissingVulnerabilitiesArray() {
        var report = parseStringContent("{}");

        assertThat(report).isEmpty();
    }

    @Test
    void shouldSkipNonObjectEntriesInVulnerabilityArray() {
        var report = parseStringContent("""
                {
                  "vulnerabilities": [
                    42,
                    "text"
                  ]
                }
                """);

        assertThat(report).isEmpty();
    }

    @Test
    void shouldProvideDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("kube-hunter");

        assertThat(descriptor.getPattern()).isEqualTo("**/kube-hunter.json");
        assertThat(descriptor.getHelp()).contains("kube-hunter --report json --dispatch stdout");
        assertThat(descriptor.getUrl()).isEqualTo("https://github.com/aquasecurity/kube-hunter");
        assertThat(descriptor.hasHelp()).isTrue();
        assertThat(descriptor.hasUrl()).isTrue();
    }

    @Override
    protected IssueParser createParser() {
        return new KubeHunterParser();
    }
}
