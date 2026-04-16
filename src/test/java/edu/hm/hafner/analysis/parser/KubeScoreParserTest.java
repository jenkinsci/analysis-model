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
 * Tests the class {@link KubeScoreParser}.
 *
 * @author Akash Manna
 */
class KubeScoreParserTest extends AbstractParserTest {
    KubeScoreParserTest() {
        super("kube-score.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(3).hasDuplicatesSize(0);

        softly.assertThat(report.get(0))
                .hasFileName("manifests/deployment.yaml")
                .hasLineStart(7)
                .hasLineEnd(7)
                .hasSeverity(Severity.ERROR)
                .hasCategory("Deployment/web.default")
                .hasType("container-security-context-privileged")
                .hasMessage("Privileged container");

        softly.assertThat(report.get(0).getDescription())
                .contains("Path: spec.template.spec.containers[0].securityContext.privileged")
                .contains("The main container is privileged.")
                .contains("Do not run containers in privileged mode.");

        softly.assertThat(report.get(1))
                .hasFileName("manifests/deployment.yaml")
                .hasLineStart(7)
                .hasLineEnd(7)
                .hasSeverity(Severity.ERROR)
                .hasCategory("Deployment/web.default")
                .hasType("container-security-context-privileged")
                .hasMessage("Privileged sidecar");

        softly.assertThat(report.get(1).getDescription())
                .contains("spec.template.spec.containers[1].securityContext.privileged")
                .contains("Sidecar containers must not be privileged.")
                .contains("Do not run containers in privileged mode.");

        softly.assertThat(report.get(2))
                .hasFileName("manifests/deployment.yaml")
                .hasLineStart(7)
                .hasLineEnd(7)
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("Deployment/web.default")
                .hasType("container-security-context-run-as-non-root")
                .hasMessage("Run containers as non-root");

        softly.assertThat(report.get(2).getDescription())
                .contains("Run containers as non-root.")
                .doesNotContain("Path:");
    }

    @Test
    void shouldParseLegacyObjectFormat() {
        var report = parseStringContent("""
                {
                  "Deployment/web.default": {
                    "ObjectName": "Deployment/web.default",
                    "FileLocation": {
                      "Name": "legacy/deployment.yaml",
                      "Line": 3
                    },
                    "Checks": [
                      {
                        "Check": {
                          "Name": "Legacy object format",
                          "ID": "legacy-check",
                          "Comment": "Use the current JSON v2 format"
                        },
                        "Grade": 1,
                        "Skipped": false,
                        "Comments": [
                          {
                            "Path": "spec.replicas",
                            "Summary": "Legacy format comment",
                            "Description": "Legacy object parsing works"
                          }
                        ]
                      }
                    ]
                  }
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("legacy/deployment.yaml")
                .hasLineStart(3)
                .hasLineEnd(3)
                .hasSeverity(Severity.ERROR)
                .hasCategory("Deployment/web.default")
                .hasType("legacy-check")
                .hasMessage("Legacy format comment");

        assertThat(report.get(0).getDescription())
                .contains("Path: spec.replicas")
                .contains("Legacy object parsing works")
                .contains("Use the current JSON v2 format");
    }

    @Override
    protected IssueParser createParser() {
        return new KubeScoreParser();
    }

    @Test
    void accepts() {
        var parser = new KubeScoreParser();
        assertThat(parser.accepts(new FileReaderFactory(FileSystems.getDefault().getPath("kube-score.json"))))
                .isTrue();
        assertThat(parser.accepts(new FileReaderFactory(FileSystems.getDefault().getPath("kube-score.txt"))))
                .isFalse();
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt")).isInstanceOf(ParsingException.class);
    }

    @Test
    void emptyInput() {
        var report = parseStringContent("[]");

        assertThat(report).isEmpty();
    }

    @Test
    void shouldProvideDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("kube-score");

        assertThat(descriptor.getPattern()).isEqualTo("**/kube-score.json");
        assertThat(descriptor.getHelp()).contains("kube-score score -o json")
                .contains("kube-score.json");
        assertThat(descriptor.getUrl()).isEqualTo("https://github.com/zegl/kube-score");
        assertThat(descriptor.hasHelp()).isTrue();
        assertThat(descriptor.hasUrl()).isTrue();
    }
}