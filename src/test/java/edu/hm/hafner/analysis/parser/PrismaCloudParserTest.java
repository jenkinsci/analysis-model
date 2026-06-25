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
 * Tests the class {@link PrismaCloudParser}.
 *
 * @author Akash Manna
 */
class PrismaCloudParserTest extends AbstractParserTest {
    PrismaCloudParserTest() {
        super("prisma-cloud-report.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(5); // 3 vulnerabilities + 2 compliances

        // First vulnerability: Log4Shell (critical)
        softly.assertThat(report.get(0))
                .hasFileName("myapp:latest")
                .hasType("CVE-2021-44228")
                .hasMessage("Apache Log4j2 2.0-beta9 through 2.14.1 JNDI features do not protect against attacker"
                        + " controlled LDAP and other JNDI related endpoints.")
                .hasSeverity(Severity.ERROR)
                .hasPackageName("log4j-core@2.14.1")
                .hasCategory("Vulnerability");

        softly.assertThat(report.get(0).getDescription())
                .contains("CVE-2021-44228")
                .contains("log4j-core")
                .contains("2.14.1")
                .contains("10.0")
                .contains("https://nvd.nist.gov/vuln/detail/CVE-2021-44228");

        // Second vulnerability: Commons Text (high)
        softly.assertThat(report.get(1))
                .hasFileName("myapp:latest")
                .hasType("CVE-2022-42889")
                .hasMessage("Apache Commons Text performs variable interpolation, allowing properties to be dynamically"
                        + " evaluated and expanded.")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasPackageName("commons-text@1.9")
                .hasCategory("Vulnerability");

        softly.assertThat(report.get(1).getDescription())
                .contains("CVE-2022-42889")
                .contains("commons-text")
                .contains("1.9")
                .contains("9.8");

        // Third vulnerability: Spring (medium)
        softly.assertThat(report.get(2))
                .hasFileName("myapp:latest")
                .hasType("CVE-2023-20860")
                .hasMessage("Spring Framework running version 6.0.0 - 6.0.6 or 5.3.0 - 5.3.25 using \"**\""
                        + " as a pattern in Spring Security configuration with the mvcRequestMatcher creates a mismatch"
                        + " in pattern matching.")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasPackageName("spring-core@5.3.24")
                .hasCategory("Vulnerability");

        // First compliance: high severity
        softly.assertThat(report.get(3))
                .hasFileName("myapp:latest")
                .hasType("41")
                .hasMessage("Do not use update instructions alone in the Dockerfile")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("Compliance");

        softly.assertThat(report.get(3).getDescription())
                .contains("Adding the update instructions in a single line on the Dockerfile will cache the update layer.")
                .contains("Image contains: (13) files with vulnerable setuid/setgid permissions");

        // Second compliance: medium severity
        softly.assertThat(report.get(4))
                .hasFileName("myapp:latest")
                .hasType("422")
                .hasMessage("Ensure that HEALTHCHECK instructions have been added to container images")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("Compliance");
    }

    @Override
    protected IssueParser createParser() {
        return new PrismaCloudParser();
    }

    @Test
    void accepts() {
        var parser = new PrismaCloudParser();

        assertThat(parser.accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("prisma-cloud-report.json")))).isTrue();
        assertThat(parser.accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("prisma-cloud-report.txt")))).isFalse();
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt"))
                .isInstanceOf(ParsingException.class);
    }

    @Test
    void emptyInput() {
        var report = parseStringContent("[]");

        assertThat(report).isEmpty();
    }

    @Test
    void shouldParseEdgeCases() {
        var report = parse("prisma-cloud-report-edge-cases.json");

        assertThat(report).hasSize(1);

        assertThat(report.get(0))
                .hasFileName("nginx:1.21")
                .hasType("CVE-2023-38545")
                .hasSeverity(Severity.ERROR)
                .hasPackageName("curl@7.74.0-1.3+deb11u7")
                .hasCategory("Vulnerability");
    }

    @Test
    void shouldHandleNullVulnerabilities() {
        var report = parseStringContent("""
                [
                    {
                        "entityInfo": {
                            "name": "test-image:1.0"
                        },
                        "results": [
                            {
                                "compliances": []
                            }
                        ]
                    }
                ]
                """);

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleMissingResults() {
        var report = parseStringContent("""
                [
                    {
                        "entityInfo": {
                            "name": "test-image:1.0"
                        }
                    }
                ]
                """);

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleEmptyResultsArray() {
        var report = parseStringContent("""
                [
                    {
                        "entityInfo": {
                            "name": "empty-image:latest"
                        },
                        "results": []
                    }
                ]
                """);

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleVulnerabilityWithMissingFields() {
        var report = parseStringContent("""
                [
                    {
                        "results": [
                            {
                                "vulnerabilities": [
                                    {
                                        "severity": "high"
                                    }
                                ]
                            }
                        ]
                    }
                ]
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasType("-")
                .hasMessage("-")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasFileName("-")
                .hasCategory("Vulnerability");
    }

    @Test
    void shouldHandleComplianceWithMissingFields() {
        var report = parseStringContent("""
                [
                    {
                        "results": [
                            {
                                "compliances": [
                                    {
                                        "severity": "low"
                                    }
                                ]
                            }
                        ]
                    }
                ]
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasType("0")
                .hasMessage("-")
                .hasSeverity(Severity.WARNING_LOW)
                .hasFileName("-")
                .hasCategory("Compliance");
    }

    @Test
    void shouldHandleMultipleImages() {
        var report = parseStringContent("""
                [
                    {
                        "entityInfo": {"name": "image-a:latest"},
                        "results": [
                            {
                                "vulnerabilities": [
                                    {
                                        "id": "CVE-2021-1234",
                                        "severity": "critical",
                                        "description": "Vulnerability in image A",
                                        "packageName": "pkg-a",
                                        "packageVersion": "1.0"
                                    }
                                ]
                            }
                        ]
                    },
                    {
                        "entityInfo": {"name": "image-b:latest"},
                        "results": [
                            {
                                "vulnerabilities": [
                                    {
                                        "id": "CVE-2022-5678",
                                        "severity": "low",
                                        "description": "Vulnerability in image B",
                                        "packageName": "pkg-b",
                                        "packageVersion": "2.0"
                                    }
                                ]
                            }
                        ]
                    }
                ]
                """);

        assertThat(report).hasSize(2);

        assertThat(report.get(0))
                .hasFileName("image-a:latest")
                .hasType("CVE-2021-1234")
                .hasSeverity(Severity.ERROR)
                .hasPackageName("pkg-a@1.0");

        assertThat(report.get(1))
                .hasFileName("image-b:latest")
                .hasType("CVE-2022-5678")
                .hasSeverity(Severity.WARNING_LOW)
                .hasPackageName("pkg-b@2.0");
    }

    @Test
    void shouldHandleMissingEntityInfo() {
        var report = parseStringContent("""
                [
                    {
                        "results": [
                            {
                                "vulnerabilities": [
                                    {
                                        "id": "CVE-2022-0001",
                                        "severity": "medium",
                                        "description": "A medium vulnerability",
                                        "packageName": "some-package",
                                        "packageVersion": "3.1"
                                    }
                                ]
                            }
                        ]
                    }
                ]
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("-")
                .hasType("CVE-2022-0001")
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Test
    void shouldHandlePackageWithoutVersion() {
        var report = parseStringContent("""
                [
                    {
                        "entityInfo": {"name": "test:latest"},
                        "results": [
                            {
                                "vulnerabilities": [
                                    {
                                        "id": "CVE-2023-9999",
                                        "severity": "medium",
                                        "description": "Test vulnerability",
                                        "packageName": "no-version-pkg"
                                    }
                                ]
                            }
                        ]
                    }
                ]
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasPackageName("no-version-pkg");
    }

    @Test
    void shouldHandleNegligibleSeverity() {
        var report = parseStringContent("""
                [
                    {
                        "entityInfo": {"name": "test:latest"},
                        "results": [
                            {
                                "vulnerabilities": [
                                    {
                                        "id": "CVE-2020-1111",
                                        "severity": "negligible",
                                        "description": "Negligible risk",
                                        "packageName": "some-lib",
                                        "packageVersion": "0.1"
                                    }
                                ]
                            }
                        ]
                    }
                ]
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0)).hasSeverity(Severity.WARNING_LOW);
    }

    @Test
    void shouldGuessSeveritiesCorrectly() {
        assertThat(Severity.guessFromString("critical")).isEqualTo(Severity.ERROR);
        assertThat(Severity.guessFromString("CRITICAL")).isEqualTo(Severity.ERROR);
        assertThat(Severity.guessFromString("high")).isEqualTo(Severity.WARNING_HIGH);
        assertThat(Severity.guessFromString("HIGH")).isEqualTo(Severity.WARNING_HIGH);
        assertThat(Severity.guessFromString("medium")).isEqualTo(Severity.WARNING_NORMAL);
        assertThat(Severity.guessFromString("MEDIUM")).isEqualTo(Severity.WARNING_NORMAL);
        assertThat(Severity.guessFromString("low")).isEqualTo(Severity.WARNING_LOW);
        assertThat(Severity.guessFromString("negligible")).isEqualTo(Severity.WARNING_LOW);
    }

    @Test
    void shouldBuildDescriptionWithCvssAndVector() {
        var report = parseStringContent("""
                [
                    {
                        "entityInfo": {"name": "test:latest"},
                        "results": [
                            {
                                "vulnerabilities": [
                                    {
                                        "id": "CVE-2021-44228",
                                        "severity": "critical",
                                        "description": "Remote code execution in Log4j",
                                        "packageName": "log4j",
                                        "packageVersion": "2.14.1",
                                        "cvss": 10.0,
                                        "vector": "CVSS:3.1/AV:N/AC:L/PR:N/UI:N/S:C/C:H/I:H/A:H",
                                        "link": "https://nvd.nist.gov/vuln/detail/CVE-2021-44228",
                                        "riskFactors": ["Critical severity", "Remote execution", "Has fix"]
                                    }
                                ]
                            }
                        ]
                    }
                ]
                """);

        assertThat(report).hasSize(1);
        var description = report.get(0).getDescription();

        assertThat(description)
                .contains("Remote code execution in Log4j")
                .contains("CVSS Score:")
                .contains("10.0")
                .contains("CVSS Vector:")
                .contains("CVSS:3.1/AV:N/AC:L/PR:N/UI:N/S:C/C:H/I:H/A:H")
                .contains("Package:")
                .contains("log4j")
                .contains("2.14.1")
                .contains("Reference:")
                .contains("https://nvd.nist.gov/vuln/detail/CVE-2021-44228")
                .contains("Risk Factors");
    }

    @Test
    void shouldProvideDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("prisma-cloud");

        assertThat(descriptor.getPattern()).isEqualTo("**/prisma-cloud-report.json");
        assertThat(descriptor.getHelp()).contains("twistcli");
        assertThat(descriptor.getUrl()).isEqualTo("https://www.paloaltonetworks.com/prisma/cloud");
        assertThat(descriptor.getIconUrl()).isEqualTo("https://github.com/PaloAltoNetworks/prisma-cloud-docs/blob/master/docs/api/cdn/prisma-cloud-logo.png?raw=true");
        assertThat(descriptor.hasHelp()).isTrue();
        assertThat(descriptor.hasUrl()).isTrue();
    }

    @Test
    void shouldHandleOnlyComplianceFindings() {
        var report = parseStringContent("""
                [
                    {
                        "entityInfo": {"name": "secure-image:1.0"},
                        "results": [
                            {
                                "vulnerabilities": [],
                                "compliances": [
                                    {
                                        "id": 599,
                                        "title": "Ensure no root user exists",
                                        "severity": "high",
                                        "description": "Processes should not run as root.",
                                        "cause": "Image runs as root"
                                    }
                                ]
                            }
                        ]
                    }
                ]
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("secure-image:1.0")
                .hasType("599")
                .hasMessage("Ensure no root user exists")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("Compliance");

        assertThat(report.get(0).getDescription())
                .contains("Processes should not run as root.")
                .contains("Image runs as root");
    }

    @Test
    void shouldHandleComplianceWithoutCause() {
        var report = parseStringContent("""
                [
                    {
                        "entityInfo": {"name": "test:1.0"},
                        "results": [
                            {
                                "compliances": [
                                    {
                                        "id": 100,
                                        "title": "Some compliance check",
                                        "severity": "medium",
                                        "description": "A compliance description."
                                    }
                                ]
                            }
                        ]
                    }
                ]
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0).getDescription())
                .contains("A compliance description.")
                .doesNotContain("Cause:");
    }
}
