package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link TrivyParser}.
 */
class TrivyParserTest extends AbstractParserTest {
    TrivyParserTest() {
        super("trivy_result.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(4);

        softly.assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_LOW)
                .hasType("CVE-2017-6519")
                .hasCategory("redhat")
                .hasMessage("avahi: Multicast DNS responds to unicast queries outside of local network");
    }

    @Test
    void parseResultsForSchemaVersion2() {
        var report = parse("trivy_result_0.20.0.json");

        assertThat(report).hasSize(4);

        assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_LOW)
                .hasType("CVE-2017-6519")
                .hasCategory("redhat")
                .hasMessage("avahi: Multicast DNS responds to unicast queries outside of local network");
    }

    @Test
    void shouldHandleEmptyResultsJenkins67296() {
        var report = parse("issue67296.json");

        assertThat(report).isEmpty();
    }

    @Test
    void shouldMapCorrectly() {
        var report = parse("trivy_result_0.20.0.json");

        assertThat(report).hasSize(4);

        assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_LOW)
                .hasType("CVE-2017-6519");
        assertThat(report.get(1))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasType("CVE-2020-8619");
        assertThat(report.get(2))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasType("CVE-2020-5555");
        assertThat(report.get(3))
                .hasSeverity(Severity.ERROR)
                .hasType("CVE-2020-9999");
    }

    @Test
    void shouldParseMisconfigurations() {
        var report = parse("trivy_misconfigurations.json");

        assertThat(report).hasSize(4);
        assertThat(report.get(0))
                .hasFileName("docker/Dockerfile")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("Dockerfile Security Check")
                .hasType("DS002")
                .hasMessage("Image user should not be 'root'")
                .hasDescription("<p>Running containers with &#x27;root&#x27; user can lead to a container escape situation. It is a best practice to run containers as non-root users, which can be done by adding a &#x27;USER&#x27; statement to the Dockerfile.</p> <p>Specify at least 1 USER command in Dockerfile with non-root user as argument</p> <p>Add &#x27;USER &lt;non root user name&gt;&#x27; line to the Dockerfile</p> <p>References:</p> <ul><li><a href=\"https://avd.aquasec.com/misconfig/ds002\">https://avd.aquasec.com/misconfig/ds002</a></li><li><a href=\"https://docs.docker.com/develop/develop-images/dockerfile_best-practices/\">https://docs.docker.com/develop/develop-images/dockerfile_best-practices/</a></li><li><a href=\"https://avd.aquasec.com/misconfig/ds002\">https://avd.aquasec.com/misconfig/ds002</a></li></ul>");
        assertThat(report.get(1))
                .hasFileName("docker/Dockerfile")
                .hasSeverity(Severity.WARNING_LOW)
                .hasCategory("Dockerfile Security Check")
                .hasType("DS026")
                .hasMessage("No HEALTHCHECK defined")
                .hasDescription("<p>You should add HEALTHCHECK instruction in your docker container images to perform the health check on running containers.</p>");
        assertThat(report.get(2))
                .hasFileName("other/Dockerfile")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("Dockerfile Security Check")
                .hasType("DS002")
                .hasMessage("Image user should not be 'root'")
                .hasDescription("<p>Running containers with &#x27;root&#x27; user can lead to a container escape situation. It is a best practice to run containers as non-root users, which can be done by adding a &#x27;USER&#x27; statement to the Dockerfile.</p> <p>Specify at least 1 USER command in Dockerfile with non-root user as argument</p> <p>Add &#x27;USER &lt;non root user name&gt;&#x27; line to the Dockerfile</p> <p>References:</p> <ul><li><a href=\"https://docs.docker.com/develop/develop-images/dockerfile_best-practices/\">https://docs.docker.com/develop/develop-images/dockerfile_best-practices/</a></li><li><a href=\"https://avd.aquasec.com/misconfig/ds002\">https://avd.aquasec.com/misconfig/ds002</a></li></ul>");
        assertThat(report.get(3))
                .hasFileName("other/Dockerfile")
                .hasSeverity(Severity.WARNING_LOW)
                .hasCategory("Dockerfile Security Check")
                .hasType("DS026")
                .hasMessage("No HEALTHCHECK defined")
                .hasDescription("<p>You should add HEALTHCHECK instruction in your docker container images to perform the health check on running containers.</p> <p>References:</p> <ul><li><a href=\"https://avd.aquasec.com/misconfig/ds026\">https://avd.aquasec.com/misconfig/ds026</a></li></ul>");
    }

    @Test
    void shouldParseTrivyConfWithLineNumbers() {
        var report = parse("trivy_conf.json");

        assertThat(report).hasSize(5);
        
        // Kubernetes misconfigurations with line numbers
        assertThat(report.get(0))
                .hasFileName("kubernetes/deployment.yaml")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("Kubernetes Security Check")
                .hasType("KSV001")
                .hasMessage("Process can elevate its own privileges")
                .hasLineStart(15)
                .hasLineEnd(20);
        
        assertThat(report.get(1))
                .hasFileName("kubernetes/deployment.yaml")
                .hasSeverity(Severity.WARNING_LOW)
                .hasCategory("Kubernetes Security Check")
                .hasType("KSV003")
                .hasMessage("Default capabilities not dropped")
                .hasLineStart(16)
                .hasLineEnd(20);
        
        assertThat(report.get(2))
                .hasFileName("kubernetes/deployment.yaml")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("Kubernetes Security Check")
                .hasType("KSV012")
                .hasMessage("Runs as root user")
                .hasLineStart(16)
                .hasLineEnd(20);
        
        // Terraform misconfigurations with line numbers
        assertThat(report.get(3))
                .hasFileName("terraform/main.tf")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("Terraform Security Check")
                .hasType("AVD-AWS-0001")
                .hasMessage("S3 Bucket has an ACL defined which allows public access.")
                .hasLineStart(5)
                .hasLineEnd(8);
        
        assertThat(report.get(4))
                .hasFileName("terraform/main.tf")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("Terraform Security Check")
                .hasType("AVD-AWS-0002")
                .hasMessage("S3 Bucket does not have encryption enabled")
                .hasLineStart(5)
                .hasLineEnd(8);
    }


    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt")).isInstanceOf(ParsingException.class);
    }

    @Override
    protected IssueParser createParser() {
        return new TrivyParser();
    }
}