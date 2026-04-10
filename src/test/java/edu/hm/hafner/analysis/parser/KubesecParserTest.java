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
 * Tests the class {@link KubesecParser}.
 *
 * @author Akash Manna
 */
class KubesecParserTest extends AbstractParserTest {
    KubesecParserTest() {
        super("kubesec.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(6).hasDuplicatesSize(0);

        softly.assertThat(report.get(0))
                .hasFileName("Pod/security-context-demo.default")
                .hasSeverity(Severity.ERROR)
                .hasCategory("Kubernetes Security")
                .hasType("containers[] .securityContext .capabilities .add == SYS_ADMIN")
                .hasMessage("CAP_SYS_ADMIN is the most privileged capability and should always be avoided");

        softly.assertThat(report.get(0).getDescription())
                .contains("Pod/security-context-demo.default")
                .contains("containers[] .securityContext .capabilities .add == SYS_ADMIN")
                .contains("-30 points");

        softly.assertThat(report.get(1))
                .hasFileName("Pod/security-context-demo.default")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("Kubernetes Security")
                .hasType("containers[] .securityContext .runAsNonRoot == true")
                .hasMessage("Force the running image to run as a non-root user to ensure least privilege");

        softly.assertThat(report.get(1).getDescription())
                .contains("Pod/security-context-demo.default")
                .contains("containers[] .securityContext .runAsNonRoot == true")
                .contains("+1 points");

        softly.assertThat(report.get(2))
                .hasFileName("Pod/security-context-demo.default")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("Kubernetes Security")
                .hasType("containers[] .resources .limits .cpu")
                .hasMessage("Enforcing CPU limits prevents resource exhaustion");

        softly.assertThat(report.get(3))
                .hasFileName("Deployment/nginx-deployment.default")
                .hasSeverity(Severity.ERROR)
                .hasCategory("Kubernetes Security")
                .hasType("containers[] .securityContext .privileged == true")
                .hasMessage("Privileged containers are dangerous and should only be used when absolutely necessary");

        softly.assertThat(report.get(4))
                .hasFileName("Deployment/nginx-deployment.default")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("Kubernetes Security")
                .hasType("containers[] .securityContext .capabilities .drop | index(\"ALL\")")
                .hasMessage("Drop all capabilities unless they are explicitly required for your application");

        softly.assertThat(report.get(5))
                .hasFileName("Deployment/nginx-deployment.default")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("Kubernetes Security")
                .hasType("containers[] .resources .limits .memory")
                .hasMessage("Enforcing memory limits prevents resource exhaustion");
    }

    @Test
    void shouldParseEdgeCases() {
        var report = parse("kubesec-edge-cases.json");

        assertThat(report).hasSize(7).hasDuplicatesSize(0);

        assertThat(report.get(0))
                .hasFileName("Pod/minimal-pod.default")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("Kubernetes Security")
                .hasType("containers[] .resources .requests .cpu")
                .hasMessage("Setting requests for CPU helps Kubernetes scheduler make better decisions");

        assertThat(report.get(1))
                .hasFileName("DaemonSet/logging-agent.kube-system")
                .hasSeverity(Severity.ERROR)
                .hasCategory("Kubernetes Security")
                .hasType("containers[] .securityContext .runAsUser < 1000")
                .hasMessage("Running as low-numbered UID can be a security risk");

        assertThat(report.get(2))
                .hasFileName("DaemonSet/logging-agent.kube-system")
                .hasSeverity(Severity.ERROR)
                .hasCategory("Kubernetes Security")
                .hasType("containers[] .securityContext .privileged == true")
                .hasMessage("Privileged containers are dangerous");

        assertThat(report.get(3))
                .hasFileName("DaemonSet/logging-agent.kube-system")
                .hasSeverity(Severity.ERROR)
                .hasCategory("Kubernetes Security")
                .hasType("containers[] .hostNetwork == true")
                .hasMessage("Using host network namespace poses security risks");

        assertThat(report.get(3).getDescription())
                .contains("DaemonSet/logging-agent.kube-system")
                .contains("containers[] .hostNetwork == true")
                .contains("-85 points");

        assertThat(report.get(4))
                .hasFileName("CronJob/backup.default")
                .hasSeverity(Severity.ERROR)
                .hasCategory("Kubernetes Security")
                .hasType("kubesec-validation")
                .hasMessage("Resource manifest is not valid");

        assertThat(report.get(4).getDescription())
                .contains("CronJob/backup.default")
                .contains("Score: -1");

        assertThat(report.get(5))
                .hasFileName("-")
                .hasSeverity(Severity.ERROR)
                .hasCategory("Kubernetes Security")
                .hasType("kubesec-validation")
                .hasMessage("Object field is not available");

        assertThat(report.get(6))
                .hasFileName("Pod/no-selector.default")
                .hasSeverity(Severity.ERROR)
                .hasCategory("Kubernetes Security")
                .hasType("-")
                .hasMessage("Selector unavailable in input");

        assertThat(report.get(6).getDescription())
                .contains("Pod/no-selector.default")
                .contains("Score Impact: -2 points")
                .doesNotContain("Selector:");
    }

    @Override
    protected IssueParser createParser() {
        return new KubesecParser();
    }

    @Test
    void accepts() {
        assertThat(new KubesecParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("kubesec.json")))).isTrue();
        assertThat(new KubesecParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("foo.txt")))).isFalse();
    }

    @Test
    void shouldProvideDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("kubesec");

        assertThat(descriptor.getPattern()).isEqualTo("**/kubesec.json");
        assertThat(descriptor.getHelp()).contains("kubesec scan -f deployment.yaml -o json > kubesec.json");
        assertThat(descriptor.getUrl()).isEqualTo("https://kubesec.io/");
        assertThat(descriptor.hasHelp()).isTrue();
        assertThat(descriptor.hasUrl()).isTrue();
    }
}
