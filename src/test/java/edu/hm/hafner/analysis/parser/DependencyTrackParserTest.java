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
 * Tests the class {@link DependencyTrackParser}.
 *
 * @author Akash Manna
 */
class DependencyTrackParserTest extends AbstractParserTest {
    DependencyTrackParserTest() {
        super("dependency-track-findings.json");
    }

    /**
     * Verifies that all 4 non-suppressed findings from the default file are parsed correctly,
     * covering CRITICAL, HIGH, MEDIUM, and LOW severities, as well as different sources
     * (NPM, NVD, GITHUB) and aliases.
     *
     * <p>The 5th finding (log4j CRITICAL) is suppressed and must NOT appear in the report.
     */
    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(4);

        // Finding 0: lodash - CRITICAL, NPM, has aliases and CWE
        softly.assertThat(report.get(0))
                .hasFileName("pkg:npm/lodash@4.17.15")
                .hasPackageName("lodash@4.17.15")
                .hasSeverity(Severity.ERROR)
                .hasType("NPM:1673")
                .hasMessage("Prototype Pollution")
                .hasCategory("NPM");

        // Finding 1: commons-text - HIGH, NVD, has CWE and alias
        softly.assertThat(report.get(1))
                .hasFileName("pkg:maven/org.apache.commons/commons-text@1.9")
                .hasPackageName("commons-text@1.9")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasType("NVD:CVE-2022-42889")
                .hasMessage("Apache Commons Text RCE via StringLookup")
                .hasCategory("NVD");

        // Finding 2: jackson-databind - MEDIUM, NVD, multiple CWEs, no aliases array
        softly.assertThat(report.get(2))
                .hasFileName("pkg:maven/com.fasterxml.jackson.core/jackson-databind@2.13.0")
                .hasPackageName("jackson-databind@2.13.0")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasType("NVD:CVE-2022-42003")
                .hasMessage("Uncontrolled Resource Consumption in Jackson Databind")
                .hasCategory("NVD");

        // Finding 3: moment - LOW, GITHUB, alias with CVE, no analysis state
        softly.assertThat(report.get(3))
                .hasFileName("pkg:npm/moment@2.29.1")
                .hasPackageName("moment@2.29.1")
                .hasSeverity(Severity.WARNING_LOW)
                .hasType("GITHUB:GHSA-wc69-rhjr-hc9v")
                .hasMessage("Path Traversal in moment.js")
                .hasCategory("GITHUB");
    }

    /**
     * Verifies the description of the first finding (lodash) contains all expected sections:
     * vulnerability ID, source, description text, recommendation, CWE, CVE alias, component
     * details and PURL.
     */
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

    /**
     * Verifies the description of the jackson-databind finding correctly lists
     * multiple CWEs (CWE-400 and CWE-502) separated by a comma.
     */
    @Test
    void shouldListMultipleCwesInDescription() {
        var report = parse("dependency-track-findings.json");

        var description = report.get(2).getDescription();

        assertThat(description)
                .contains("CWE-400 (Uncontrolled Resource Consumption)")
                .contains("CWE-502 (Deserialization of Untrusted Data)");
    }

    /**
     * Verifies that the finding for moment.js (aliases with a CVE) includes the
     * CVE alias in the description.
     */
    @Test
    void shouldIncludeCveAliasInDescription() {
        var report = parse("dependency-track-findings.json");

        var description = report.get(3).getDescription();

        assertThat(description).contains("CVE Alias(es): CVE-2022-24785");
    }

    /**
     * Verifies that a suppressed finding (log4j CVE-2021-44228) is not included
     * in the parsed report, and total count remains 4.
     */
    @Test
    void shouldSkipSuppressedFindings() {
        var report = parse("dependency-track-findings.json");

        assertThat(report).hasSize(4);
        assertThat(report.stream())
                .map(i -> i.getMessage())
                .doesNotContain("Log4Shell - Remote Code Execution in Log4j");
    }

    /**
     * Verifies the severity distribution across all non-suppressed findings:
     * 1 CRITICAL (→ ERROR), 1 HIGH, 1 MEDIUM, 1 LOW.
     */
    @Test
    void shouldProduceSeveritiesCorrectly() {
        var report = parse("dependency-track-findings.json");

        assertThatReportHasSeverities(report, 1, 1, 1, 1);
    }

    // -----------------------------------------------------------------------
    // Edge-case test resource: dependency-track-edge-cases.json
    // -----------------------------------------------------------------------

    /**
     * Verifies edge-case parsing: component without purl (fallback to packageName as fileName),
     * component without version (name only), empty cwes/aliases arrays.
     */
    @Test
    void shouldHandleEdgeCases() {
        var report = parse("dependency-track-edge-cases.json");

        assertThat(report).hasSize(3);

        // timespan: no purl → fileName == packageName
        assertThat(report.get(0))
                .hasFileName("timespan@2.3.0")
                .hasPackageName("timespan@2.3.0")
                .hasSeverity(Severity.WARNING_LOW)
                .hasType("NPM:533")
                .hasMessage("Regular Expression Denial of Service")
                .hasCategory("NPM");

        // minimatch: has purl, empty cwes and aliases arrays
        assertThat(report.get(1))
                .hasFileName("pkg:npm/minimatch@3.0.4")
                .hasPackageName("minimatch@3.0.4")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasType("GITHUB:GHSA-f8q6-p94x-37v3")
                .hasMessage("ReDoS in minimatch")
                .hasCategory("GITHUB");

        // no-version-component: name only (no version) → packageName == "no-version-component"
        assertThat(report.get(2))
                .hasFileName("no-version-component")
                .hasPackageName("no-version-component")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasType("OSSINDEX:CVE-2019-0001")
                .hasMessage("Some Vulnerability Without Version")
                .hasCategory("OSSINDEX");
    }

    /**
     * Verifies that a finding with no isSuppressed field in analysis (only state) is treated
     * as not suppressed and is included in the report.
     */
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

    /**
     * Verifies that a finding with no analysis object at all is treated as not suppressed.
     */
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

    /**
     * Verifies that a finding with isSuppressed=true is completely excluded from the report.
     */
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

    /**
     * Verifies that a missing component object results in fileName="-" and packageName="-".
     */
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

    /**
     * Verifies that a missing vulnerability object results in type="-", empty message/category,
     * and WARNING_NORMAL severity.
     */
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

    /**
     * Verifies that a component with an empty name string falls back to packageName="-",
     * and without purl, fileName also becomes "-".
     */
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

    /**
     * Verifies that when a component has no version field, the packageName is just the name
     * without an "@version" suffix.
     */
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

    /**
     * Verifies that when vulnId is blank/absent, the type is set to "-".
     */
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

    /**
     * Verifies that when source is absent, type is just the vulnId without a colon prefix.
     */
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

    /**
     * Verifies behaviour when the "findings" key is absent — report must be empty.
     */
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

    /**
     * Verifies behaviour when findings array is empty — report must be empty.
     */
    @Test
    void shouldReturnEmptyReportWhenFindingsArrayIsEmpty() {
        var report = parseStringContent("""
                {
                    "findings": []
                }
                """);

        assertThat(report).isEmpty();
    }

    /**
     * Verifies that an unknown severity string (e.g. "INFO") does not crash the parser
     * and produces a non-null severity.
     */
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

    /**
     * Verifies that a CRITICAL severity string maps to {@link Severity#ERROR}.
     */
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

    /**
     * Verifies that aliases without a cveId field (only ghsaId, for example) do not appear
     * in the CVE Alias(es) description section.
     */
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

    /**
     * Verifies that multiple CVE aliases are comma-separated in the description.
     */
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

    /**
     * Verifies that CWE entries with cweId == 0 or negative are skipped.
     */
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

    /**
     * Verifies that CWE entries missing the name field still produce "CWE-NNN" without parentheses.
     */
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

    /**
     * Verifies that a component purl takes precedence over packageName as the fileName.
     */
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

    /**
     * Verifies parsing a real-world multiple-finding mix: some suppressed, some not,
     * checking exact count is returned correctly.
     */
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

    /**
     * Verifies the description does not include blank/null sections (e.g. no "Description:"
     * label when description is absent from vulnerability).
     */
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

    /**
     * Verifies that the parser rejects non-JSON files.
     */
    @Test
    void shouldAcceptOnlyJsonFiles() {
        assertThat(new DependencyTrackParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("dependency-track-findings.json")))).isTrue();
        assertThat(new DependencyTrackParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("report.xml")))).isFalse();
        assertThat(new DependencyTrackParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("output.txt")))).isFalse();
    }

    /**
     * Verifies that malformed JSON throws a {@link ParsingException}.
     */
    @Test
    void shouldThrowParsingExceptionForMalformedJson() {
        assertThatThrownBy(() -> parse("eclipse.txt"))
                .isInstanceOf(ParsingException.class);
    }

    /**
     * Verifies that the descriptor is registered in the {@link ParserRegistry} with the
     * expected ID, pattern, URL, icon URL, and help text.
     */
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
        assertThat(descriptor.getType()).isEqualTo(edu.hm.hafner.analysis.Report.IssueType.VULNERABILITY);
    }

    /**
     * Verifies that the component Version field (without purl) is included in the description
     * when set, and is absent when no version is provided.
     */
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