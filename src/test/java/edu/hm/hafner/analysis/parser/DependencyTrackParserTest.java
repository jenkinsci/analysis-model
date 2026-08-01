package edu.hm.hafner.analysis.parser;

import java.nio.file.FileSystems;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.FileReaderFactory;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;
import edu.hm.hafner.analysis.registry.ParserRegistry;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link DependencyTrackParser}.
 *
 * @author Akash Manna
 */
class DependencyTrackParserTest extends AbstractParserTest {
    DependencyTrackParserTest() {
        super("dependency-track-findings.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(4);

        softly.assertThat(report.get(0))
                .hasFileName("pkg:npm/lodash@4.17.15")
                .hasPackageName("lodash@4.17.15")
                .hasSeverity(Severity.ERROR)
                .hasType("NPM:1673")
                .hasMessage("Prototype Pollution")
                .hasCategory("NPM");

        softly.assertThat(report.get(1))
                .hasFileName("pkg:maven/org.apache.commons/commons-text@1.9")
                .hasPackageName("commons-text@1.9")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasType("NVD:CVE-2022-42889")
                .hasMessage("Apache Commons Text RCE via StringLookup")
                .hasCategory("NVD");

        softly.assertThat(report.get(2))
                .hasFileName("pkg:maven/com.fasterxml.jackson.core/jackson-databind@2.13.0")
                .hasPackageName("jackson-databind@2.13.0")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasType("NVD:CVE-2022-42003")
                .hasMessage("Uncontrolled Resource Consumption in Jackson Databind")
                .hasCategory("NVD");

        softly.assertThat(report.get(3))
                .hasFileName("pkg:npm/moment@2.29.1")
                .hasPackageName("moment@2.29.1")
                .hasSeverity(Severity.WARNING_LOW)
                .hasType("GITHUB:GHSA-wc69-rhjr-hc9v")
                .hasMessage("Path Traversal in moment.js")
                .hasCategory("GITHUB");
    }

    @Test
    void shouldBuildRichDescriptionForFirstFinding() {
        var report = parse("dependency-track-findings.json");

        var description = report.get(0).getDescription();

        assertThat(description)
                .contains("Vulnerability ID: NPM:1673")
                .contains("Source: NPM")
                .contains("All versions of lodash before 4.17.21")
                .contains("Recommendation: Upgrade lodash to version 4.17.21 or later.")
                .contains("CWE-1321")
                .contains("Improperly Controlled Modification of Object Prototype Attributes")
                .contains("CVE Alias(es): CVE-2021-23337")
                .contains("Component: lodash")
                .contains("Version: 4.17.15")
                .contains("Package URL: pkg:npm/lodash@4.17.15");
    }

    @Test
    void shouldListMultipleCwesInDescription() {
        var report = parse("dependency-track-findings.json");

        var description = report.get(2).getDescription();

        assertThat(description)
                .contains("CWE-400 (Uncontrolled Resource Consumption)")
                .contains("CWE-502 (Deserialization of Untrusted Data)");
    }

    @Test
    void shouldIncludeCveAliasInDescription() {
        var report = parse("dependency-track-findings.json");

        var description = report.get(3).getDescription();

        assertThat(description).contains("CVE Alias(es): CVE-2022-24785");
    }

    @Test
    void shouldSkipSuppressedFindings() {
        var report = parse("dependency-track-findings.json");

        assertThat(report).hasSize(4);
        assertThat(report.stream())
                .map(Issue::getMessage)
                .doesNotContain("Log4Shell - Remote Code Execution in Log4j");
    }

    @Test
    void shouldProduceSeveritiesCorrectly() {
        var report = parse("dependency-track-findings.json");

        assertThatReportHasSeverities(report, 1, 1, 1, 1);
    }

    @Test
    void shouldHandleEdgeCases() {
        var report = parse("dependency-track-edge-cases.json");

        assertThat(report).hasSize(3);

        assertThat(report.get(0))
                .hasFileName("timespan@2.3.0")
                .hasPackageName("timespan@2.3.0")
                .hasSeverity(Severity.WARNING_LOW)
                .hasType("NPM:533")
                .hasMessage("Regular Expression Denial of Service")
                .hasCategory("NPM");

        assertThat(report.get(1))
                .hasFileName("pkg:npm/minimatch@3.0.4")
                .hasPackageName("minimatch@3.0.4")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasType("GITHUB:GHSA-f8q6-p94x-37v3")
                .hasMessage("ReDoS in minimatch")
                .hasCategory("GITHUB");

        assertThat(report.get(2))
                .hasFileName("no-version-component")
                .hasPackageName("no-version-component")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasType("OSSINDEX:CVE-2019-0001")
                .hasMessage("Some Vulnerability Without Version")
                .hasCategory("OSSINDEX");
    }

    @Test
    void shouldTreatMissingIsSuppressedAsFalse() {
        var report = parseStringContent("""
                {
                    "findings": [
                        {
                            "component": { "name": "some-lib", "version": "1.0" },
                            "vulnerability": {
                                "source": "NVD",
                                "vulnId": "CVE-2020-0001",
                                "title": "Test Vuln",
                                "severity": "HIGH"
                            },
                            "analysis": { "state": "NOT_SET" }
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0)).hasMessage("Test Vuln");
    }

    @Test
    void shouldTreatMissingAnalysisObjectAsNotSuppressed() {
        var report = parseStringContent("""
                {
                    "findings": [
                        {
                            "component": { "name": "some-lib", "version": "1.0" },
                            "vulnerability": {
                                "source": "NVD",
                                "vulnId": "CVE-2020-0002",
                                "title": "No Analysis Vuln",
                                "severity": "MEDIUM"
                            }
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0)).hasMessage("No Analysis Vuln");
    }

    @Test
    void shouldExcludeFindingWithIsSuppressedTrue() {
        var report = parseStringContent("""
                {
                    "findings": [
                        {
                            "component": { "name": "bad-lib", "version": "2.0" },
                            "vulnerability": {
                                "source": "NVD",
                                "vulnId": "CVE-2021-9999",
                                "title": "Suppressed Vuln",
                                "severity": "CRITICAL"
                            },
                            "analysis": { "isSuppressed": true }
                        }
                    ]
                }
                """);

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleMissingComponentObject() {
        var report = parseStringContent("""
                {
                    "findings": [
                        {
                            "vulnerability": {
                                "source": "NVD",
                                "vulnId": "CVE-2020-0003",
                                "title": "No Component",
                                "severity": "LOW"
                            },
                            "analysis": { "isSuppressed": false }
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("-")
                .hasPackageName("-")
                .hasMessage("No Component");
    }

    @Test
    void shouldHandleMissingVulnerabilityObject() {
        var report = parseStringContent("""
                {
                    "findings": [
                        {
                            "component": { "name": "some-lib", "version": "1.0" },
                            "analysis": { "isSuppressed": false }
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasType("-")
                .hasMessage("")
                .hasCategory("")
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Test
    void shouldFallbackToHyphenForBlankComponentName() {
        var report = parseStringContent("""
                {
                    "findings": [
                        {
                            "component": { "name": "", "version": "1.0" },
                            "vulnerability": {
                                "source": "NPM",
                                "vulnId": "123",
                                "title": "Blank Name",
                                "severity": "LOW"
                            },
                            "analysis": { "isSuppressed": false }
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("-")
                .hasPackageName("-");
    }

    @Test
    void shouldOmitVersionSuffixWhenVersionIsAbsent() {
        var report = parseStringContent("""
                {
                    "findings": [
                        {
                            "component": { "name": "my-lib" },
                            "vulnerability": {
                                "source": "NPM",
                                "vulnId": "456",
                                "title": "No Version",
                                "severity": "LOW"
                            },
                            "analysis": { "isSuppressed": false }
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasPackageName("my-lib")
                .hasFileName("my-lib");
    }

    @Test
    void shouldFallbackToHyphenTypeWhenVulnIdIsBlank() {
        var report = parseStringContent("""
                {
                    "findings": [
                        {
                            "component": { "name": "lib", "version": "1.0" },
                            "vulnerability": {
                                "source": "NVD",
                                "title": "No ID Vuln",
                                "severity": "MEDIUM"
                            },
                            "analysis": { "isSuppressed": false }
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0)).hasType("-");
    }

    @Test
    void shouldProduceTypeAsVulnIdOnlyWhenSourceIsAbsent() {
        var report = parseStringContent("""
                {
                    "findings": [
                        {
                            "component": { "name": "lib", "version": "1.0" },
                            "vulnerability": {
                                "vulnId": "CVE-2023-0001",
                                "title": "No Source",
                                "severity": "HIGH"
                            },
                            "analysis": { "isSuppressed": false }
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0)).hasType("CVE-2023-0001");
    }

    @Test
    void shouldReturnEmptyReportWhenFindingsKeyAbsent() {
        var report = parseStringContent("""
                {
                    "version": "1.3",
                    "meta": { "application": "Dependency-Track" },
                    "project": { "name": "Empty Project" }
                }
                """);

        assertThat(report).isEmpty();
    }

    @Test
    void shouldReturnEmptyReportWhenFindingsArrayIsEmpty() {
        var report = parseStringContent("""
                {
                    "findings": []
                }
                """);

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleUnknownSeverityGracefully() {
        var report = parseStringContent("""
                {
                    "findings": [
                        {
                            "component": { "name": "lib", "version": "1.0" },
                            "vulnerability": {
                                "source": "NVD",
                                "vulnId": "CVE-2023-9999",
                                "title": "Info Level",
                                "severity": "INFO"
                            },
                            "analysis": { "isSuppressed": false }
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0).getSeverity()).isNotNull();
    }

    @Test
    void shouldMapCriticalSeverityToError() {
        var report = parseStringContent("""
                {
                    "findings": [
                        {
                            "component": { "name": "lib", "version": "1.0" },
                            "vulnerability": {
                                "source": "NVD",
                                "vulnId": "CVE-2023-1111",
                                "title": "Critical Vuln",
                                "severity": "CRITICAL"
                            },
                            "analysis": { "isSuppressed": false }
                        }
                    ]
                }
                """);

        assertThat(report.get(0)).hasSeverity(Severity.ERROR);
    }

    @Test
    void shouldSkipAliasEntriesWithoutCveId() {
        var report = parseStringContent("""
                {
                    "findings": [
                        {
                            "component": { "name": "lib", "version": "1.0" },
                            "vulnerability": {
                                "source": "GITHUB",
                                "vulnId": "GHSA-xxxx-yyyy-zzzz",
                                "title": "GHSA Only",
                                "severity": "MEDIUM",
                                "aliases": [
                                    { "ghsaId": "GHSA-xxxx-yyyy-zzzz" }
                                ]
                            },
                            "analysis": { "isSuppressed": false }
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0).getDescription()).doesNotContain("CVE Alias(es)");
    }

    @Test
    void shouldListMultipleCveAliasesCommaSeparated() {
        var report = parseStringContent("""
                {
                    "findings": [
                        {
                            "component": { "name": "lib", "version": "1.0" },
                            "vulnerability": {
                                "source": "NVD",
                                "vulnId": "CVE-2023-2222",
                                "title": "Multiple Aliases",
                                "severity": "HIGH",
                                "aliases": [
                                    { "cveId": "CVE-2023-2222" },
                                    { "cveId": "CVE-2023-3333" }
                                ]
                            },
                            "analysis": { "isSuppressed": false }
                        }
                    ]
                }
                """);

        assertThat(report.get(0).getDescription())
                .contains("CVE Alias(es): CVE-2023-2222, CVE-2023-3333");
    }

    @Test
    void shouldSkipCweEntriesWithNonPositiveId() {
        var report = parseStringContent("""
                {
                    "findings": [
                        {
                            "component": { "name": "lib", "version": "1.0" },
                            "vulnerability": {
                                "source": "NVD",
                                "vulnId": "CVE-2023-4444",
                                "title": "Bad CWE IDs",
                                "severity": "LOW",
                                "cwes": [
                                    { "cweId": 0, "name": "Zero CWE" },
                                    { "cweId": -1, "name": "Negative CWE" }
                                ]
                            },
                            "analysis": { "isSuppressed": false }
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0).getDescription()).doesNotContain("CWE(s)");
    }

    @Test
    void shouldHandleCweWithoutName() {
        var report = parseStringContent("""
                {
                    "findings": [
                        {
                            "component": { "name": "lib", "version": "1.0" },
                            "vulnerability": {
                                "source": "NVD",
                                "vulnId": "CVE-2023-5555",
                                "title": "CWE No Name",
                                "severity": "MEDIUM",
                                "cwes": [
                                    { "cweId": 79 }
                                ]
                            },
                            "analysis": { "isSuppressed": false }
                        }
                    ]
                }
                """);

        assertThat(report.get(0).getDescription())
                .contains("CWE-79")
                .doesNotContain("CWE-79 (");
    }

    @Test
    void shouldUsePurlAsFileNameWhenPresent() {
        var report = parseStringContent("""
                {
                    "findings": [
                        {
                            "component": {
                                "name": "my-pkg",
                                "version": "3.0",
                                "purl": "pkg:npm/my-pkg@3.0"
                            },
                            "vulnerability": {
                                "source": "NPM",
                                "vulnId": "789",
                                "title": "PURL Test",
                                "severity": "HIGH"
                            },
                            "analysis": { "isSuppressed": false }
                        }
                    ]
                }
                """);

        assertThat(report.get(0))
                .hasFileName("pkg:npm/my-pkg@3.0")
                .hasPackageName("my-pkg@3.0");
    }

    @Test
    void shouldParseOnlyNonSuppressedFromMixedFindings() {
        var report = parseStringContent("""
                {
                    "findings": [
                        {
                            "component": { "name": "a", "version": "1.0" },
                            "vulnerability": { "source": "NVD", "vulnId": "CVE-1", "title": "A", "severity": "HIGH" },
                            "analysis": { "isSuppressed": false }
                        },
                        {
                            "component": { "name": "b", "version": "1.0" },
                            "vulnerability": { "source": "NVD", "vulnId": "CVE-2", "title": "B", "severity": "LOW" },
                            "analysis": { "isSuppressed": true }
                        },
                        {
                            "component": { "name": "c", "version": "1.0" },
                            "vulnerability": { "source": "NVD", "vulnId": "CVE-3", "title": "C", "severity": "CRITICAL" },
                            "analysis": { "isSuppressed": false }
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(2);
        assertThat(report.get(0)).hasMessage("A").hasSeverity(Severity.WARNING_HIGH);
        assertThat(report.get(1)).hasMessage("C").hasSeverity(Severity.ERROR);
    }

    @Test
    void shouldNotIncludeBlankSectionsInDescription() {
        var report = parseStringContent("""
                {
                    "findings": [
                        {
                            "component": { "name": "lib", "version": "1.0" },
                            "vulnerability": {
                                "source": "NVD",
                                "vulnId": "CVE-2023-7777",
                                "title": "Sparse Vuln",
                                "severity": "LOW"
                            },
                            "analysis": { "isSuppressed": false }
                        }
                    ]
                }
                """);

        var description = report.get(0).getDescription();

        assertThat(description)
                .doesNotContain("Description:")
                .doesNotContain("Recommendation:")
                .doesNotContain("CWE(s):")
                .doesNotContain("CVE Alias(es):")
                .contains("Vulnerability ID: NVD:CVE-2023-7777")
                .contains("Component: lib")
                .contains("Version: 1.0");
    }

    @Test
    void shouldAcceptOnlyJsonFiles() {
        assertThat(new DependencyTrackParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("dependency-track-findings.json")))).isTrue();
        assertThat(new DependencyTrackParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("report.xml")))).isFalse();
        assertThat(new DependencyTrackParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("output.txt")))).isFalse();
    }

    @Test
    void shouldThrowParsingExceptionForMalformedJson() {
        assertThatThrownBy(() -> parse("eclipse.txt"))
                .isInstanceOf(ParsingException.class);
    }

    @Test
    void shouldProvideDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("dependency-track");

        assertThat(descriptor.getId()).isEqualTo("dependency-track");
        assertThat(descriptor.getName()).isEqualTo("Dependency-Track");
        assertThat(descriptor.getPattern()).isEqualTo("**/dependency-track-findings.json");
        assertThat(descriptor.getUrl()).isEqualTo("https://dependencytrack.org/");
        assertThat(descriptor.getIconUrl()).contains("DependencyTrack");
        assertThat(descriptor.getHelp()).contains("/api/v1/finding/project/{uuid}/export");
        assertThat(descriptor.hasUrl()).isTrue();
        assertThat(descriptor.hasHelp()).isTrue();

        assertThat(descriptor.getType()).isEqualTo(Report.IssueType.VULNERABILITY);
    }

    @Test
    void shouldIncludeComponentVersionInDescriptionOnlyWhenPresent() {
        var reportWithVersion = parseStringContent("""
                {
                    "findings": [
                        {
                            "component": { "name": "lib", "version": "2.5.0" },
                            "vulnerability": { "source": "NVD", "vulnId": "CVE-A", "title": "T", "severity": "LOW" },
                            "analysis": { "isSuppressed": false }
                        }
                    ]
                }
                """);

        assertThat(reportWithVersion.get(0).getDescription()).contains("Version: 2.5.0");

        var reportWithoutVersion = parseStringContent("""
                {
                    "findings": [
                        {
                            "component": { "name": "lib" },
                            "vulnerability": { "source": "NVD", "vulnId": "CVE-B", "title": "T", "severity": "LOW" },
                            "analysis": { "isSuppressed": false }
                        }
                    ]
                }
                """);

        assertThat(reportWithoutVersion.get(0).getDescription()).doesNotContain("Version:");
    }

    @Override
    protected IssueParser createParser() {
        return new DependencyTrackParser();
    }
}