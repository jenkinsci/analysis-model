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
 * Tests the class {@link KubeLinterParser}.
 * 
 * @author Akash Manna
 */
class KubeLinterParserTest extends AbstractParserTest {
    KubeLinterParserTest() {
        super("kubelinter.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(3);

        softly.assertThat(report.get(0))
                .hasFileName("k8s/pod.yaml")
                .hasType("no-read-only-root-fs")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("Pod/security-context-demo")
                .hasMessage("container \"sec-ctx-demo\" does not have a read-only root file system")
                .hasDescription("Set readOnlyRootFilesystem to true in your container's securityContext.");

        softly.assertThat(report.get(1))
                .hasFileName("k8s/pod.yaml")
                .hasType("unset-memory-requirements")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("Pod/security-context-demo")
                .hasMessage("container \"sec-ctx-demo\" has memory limit 0")
                .hasDescription("Set your container's memory requests and limits depending on its requirements.");

        softly.assertThat(report.get(2))
                .hasFileName("helm/templates/deployment.yaml")
                .hasType("run-as-non-root")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("Deployment/api (production)")
                .hasMessage("container \"api\" is not set to runAsNonRoot")
                .hasDescription("Set runAsNonRoot to true in your container securityContext.");
    }

    @Test
    void accepts() {
        var parser = new KubeLinterParser();
        assertThat(parser.accepts(new FileReaderFactory(FileSystems.getDefault().getPath("kubelinter.json")))).isTrue();
        assertThat(parser.accepts(new FileReaderFactory(FileSystems.getDefault().getPath("kubelinter.txt")))).isFalse();
    }

    @Test
    void shouldHandleMissingOptionalFields() {
        var report = parse("kubelinter-missing-fields.json");

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("-")
                .hasType("-")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("-")
                .hasMessage("-")
                .hasDescription("");
    }

    @Test
    void shouldHandleMissingReportsArray() {
        var report = parseStringContent("{}");

        assertThat(report).isEmpty();
    }

    @Test
    void shouldParseJsonArrayRootFormat() {
        var report = parseStringContent("""
                [
                  {
                    "Diagnostic": {
                      "Message": "array root format"
                    },
                    "Check": "run-as-non-root",
                    "Object": {
                      "Metadata": {
                        "FilePath": "k8s/deployment.yaml"
                      },
                      "K8sObject": {
                        "Namespace": "",
                        "Name": "api"
                      }
                    }
                  }
                ]
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasType("run-as-non-root")
                .hasFileName("k8s/deployment.yaml")
                .hasCategory("-/api")
                .hasMessage("array root format")
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Test
    void shouldHandleNonObjectReportEntry() {
        var report = parseStringContent("""
                {
                  "Reports": [
                    42
                  ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasType("-")
                .hasFileName("-")
                .hasCategory("")
                .hasMessage("")
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt")).isInstanceOf(ParsingException.class);
    }

    @Test
    void emptyInput() throws ParsingException {
        var report = parseStringContent("{\"Reports\": []}");

        assertThat(report).isEmpty();
    }

    @Test
    void shouldProvideDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("kube-linter");

        assertThat(descriptor.getPattern()).isEqualTo("**/kube-linter.json");
        assertThat(descriptor.getHelp()).contains("kube-linter lint --format json");
        assertThat(descriptor.getUrl()).isEqualTo("https://github.com/stackrox/kube-linter");
        assertThat(descriptor.hasHelp()).isTrue();
        assertThat(descriptor.hasUrl()).isTrue();
    }

    @Override
    protected IssueParser createParser() {
        return new KubeLinterParser();
    }
}
