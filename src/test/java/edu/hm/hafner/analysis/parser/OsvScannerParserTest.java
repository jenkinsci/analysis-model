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
 * Tests the class {@link OsvScannerParser}.
 *
 * @author Akash Manna
 */
class OsvScannerParserTest extends AbstractParserTest {
    OsvScannerParserTest() {
        super("osv-scanner-report.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(3);

        softly.assertThat(report.get(0))
                .hasFileName("package-lock.json")
                .hasPackageName("lodash@4.17.15")
                .hasCategory("npm")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasType("GHSA-p6mc-m468-83gw")
                .hasMessage("Prototype Pollution in lodash");

        softly.assertThat(report.get(0).getDescription())
                .contains("Prototype Pollution in lodash")
                .contains("CVE-2020-8203")
                .contains("GHSA-p6mc-m468-83gw")
                .contains("osv.dev/vulnerability/GHSA-p6mc-m468-83gw");

        softly.assertThat(report.get(1))
                .hasFileName("package-lock.json")
                .hasPackageName("minimist@1.2.0")
                .hasCategory("npm")
                .hasSeverity(Severity.ERROR)
                .hasType("GHSA-xvch-5gv4-984h")
                .hasMessage("Prototype Pollution in minimist");

        softly.assertThat(report.get(1).getDescription())
                .contains("Prototype Pollution in minimist")
                .contains("CVE-2021-44906");

        softly.assertThat(report.get(2))
                .hasFileName("go.mod")
                .hasPackageName("github.com/gogo/protobuf@1.3.1")
                .hasCategory("Go")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasType("GHSA-c3h9-896r-86jm")
                .hasMessage("Panic in gogo/protobuf before 1.3.2 allows attackers to cause a denial of service");
    }

    @Test
    void accepts() {
        assertThat(new OsvScannerParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("osv-scanner-report.json")))).isTrue();
        assertThat(new OsvScannerParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("foo.txt")))).isFalse();
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt"))
                .isInstanceOf(ParsingException.class);
    }

    /** Covers: {@code !jsonReport.has("results")} → immediate return. */
    @Test
    void shouldReturnEmptyWhenResultsKeyAbsent() {
        assertThat(parseStringContent("{\"other\": []}")).isEmpty();
    }

    /** Covers: {@code results} array is present but empty. */
    @Test
    void shouldReturnEmptyWhenResultsArrayIsEmpty() {
        assertThat(parse("osv-scanner-no-issues.json")).isEmpty();
    }

    /** Covers: result object has no {@code packages} key → parsePackages early return. */
    @Test
    void shouldReturnEmptyWhenPackagesKeyAbsent() {
        var report = parseStringContent("""
                {
                    "results": [
                        {
                            "source": { "path": "pom.xml", "type": "lockfile" }
                        }
                    ]
                }
                """);
        assertThat(report).isEmpty();
    }

    /** Covers: result has {@code packages} key but the array is empty. */
    @Test
    void shouldReturnEmptyWhenPackagesArrayIsEmpty() {
        var report = parseStringContent("""
                {
                    "results": [
                        {
                            "source": { "path": "pom.xml", "type": "lockfile" },
                            "packages": []
                        }
                    ]
                }
                """);
        assertThat(report).isEmpty();
    }

    /** Covers: package entry has no {@code vulnerabilities} key → parseVulnerabilities early return. */
    @Test
    void shouldSkipPackageWithNoVulnerabilitiesKey() {
        var report = parseStringContent("""
                {
                    "results": [
                        {
                            "source": { "path": "pom.xml", "type": "lockfile" },
                            "packages": [
                                {
                                    "package": { "name": "safe-pkg", "version": "1.0.0", "ecosystem": "maven" }
                                }
                            ]
                        }
                    ]
                }
                """);
        assertThat(report).isEmpty();
    }

    /** Covers: package entry has {@code vulnerabilities} key but value is empty array. */
    @Test
    void shouldSkipPackageWithEmptyVulnerabilitiesArray() {
        var report = parseStringContent("""
                {
                    "results": [
                        {
                            "source": { "path": "pom.xml", "type": "lockfile" },
                            "packages": [
                                {
                                    "package": { "name": "safe-pkg", "version": "1.0.0", "ecosystem": "maven" },
                                    "vulnerabilities": []
                                }
                            ]
                        }
                    ]
                }
                """);
        assertThat(report).isEmpty();
    }

    /** Covers: result has no {@code source} object → sourcePath defaults to {@code "-"}. */
    @Test
    void shouldUseDefaultSourcePathWhenSourceAbsent() {
        var report = parseStringContent("""
                {
                    "results": [
                        {
                            "packages": [
                                {
                                    "package": { "name": "testpkg", "version": "1.0.0", "ecosystem": "npm" },
                                    "vulnerabilities": [
                                        {
                                            "id": "GHSA-test-0001",
                                            "summary": "Test vulnerability",
                                            "details": "Test details",
                                            "database_specific": { "severity": "HIGH" }
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                }
                """);
        assertThat(report).hasSize(1);
        assertThat(report.get(0)).hasFileName("-");
    }

    /** Covers: {@code packageInfo == null} → name = "Unknown", ecosystem = "Unknown". */
    @Test
    void shouldUseUnknownWhenPackageObjectAbsent() {
        var report = parseStringContent("""
                {
                    "results": [
                        {
                            "source": { "path": "go.sum", "type": "lockfile" },
                            "packages": [
                                {
                                    "vulnerabilities": [
                                        {
                                            "id": "GHSA-null-pkg",
                                            "summary": "Null package test",
                                            "database_specific": { "severity": "LOW" }
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                }
                """);
        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasPackageName("Unknown")
                .hasCategory("Unknown")
                .hasSeverity(Severity.WARNING_LOW);
    }

    /** Covers: package has name but no {@code version} field → name only (no {@code @version}). */
    @Test
    void shouldFormatPackageNameWithoutVersionWhenVersionAbsent() {
        var report = parseStringContent("""
                {
                    "results": [
                        {
                            "source": { "path": "Cargo.lock", "type": "lockfile" },
                            "packages": [
                                {
                                    "package": { "name": "mylib", "ecosystem": "crates.io" },
                                    "vulnerabilities": [
                                        {
                                            "id": "RUSTSEC-2022-0001",
                                            "summary": "Use after free in mylib",
                                            "database_specific": { "severity": "CRITICAL" }
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                }
                """);
        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasPackageName("mylib")
                .hasSeverity(Severity.ERROR);
    }

    /** Covers: {@code database_specific} present but has no {@code severity} key → WARNING_NORMAL. */
    @Test
    void shouldDefaultToWarningNormalWhenDbSpecificHasNoSeverityKey() {
        var report = parseStringContent("""
                {
                    "results": [
                        {
                            "source": { "path": "requirements.txt", "type": "lockfile" },
                            "packages": [
                                {
                                    "package": { "name": "pkg", "version": "1.0", "ecosystem": "PyPI" },
                                    "vulnerabilities": [
                                        {
                                            "id": "GHSA-no-sev",
                                            "summary": "No severity key",
                                            "database_specific": {}
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                }
                """);
        assertThat(report).hasSize(1);
        assertThat(report.get(0)).hasSeverity(Severity.WARNING_NORMAL);
    }

    /** Covers: no {@code database_specific} field at all → dbSpecific is null → WARNING_NORMAL. */
    @Test
    void shouldDefaultToWarningNormalWhenDatabaseSpecificAbsent() {
        var report = parseStringContent("""
                {
                    "results": [
                        {
                            "source": { "path": "requirements.txt", "type": "lockfile" },
                            "packages": [
                                {
                                    "package": { "name": "pkg", "version": "1.0", "ecosystem": "PyPI" },
                                    "vulnerabilities": [
                                        {
                                            "id": "GHSA-no-db",
                                            "summary": "No db_specific field"
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                }
                """);
        assertThat(report).hasSize(1);
        assertThat(report.get(0)).hasSeverity(Severity.WARNING_NORMAL);
    }

    /**
     * Covers: {@code summary.isEmpty()} → no title paragraph;
     * {@code details.isEmpty()} → no details paragraph.
     * Both branches hit together when neither field is present.
     */
    @Test
    void shouldBuildDescriptionWithNoSummaryAndNoDetails() {
        var report = parseStringContent("""
                {
                    "results": [
                        {
                            "source": { "path": "package.json", "type": "lockfile" },
                            "packages": [
                                {
                                    "package": { "name": "pkg", "version": "1.0", "ecosystem": "npm" },
                                    "vulnerabilities": [
                                        {
                                            "id": "GHSA-empty-desc",
                                            "database_specific": { "severity": "MODERATE" }
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                }
                """);
        assertThat(report).hasSize(1);
        var issue = report.get(0);
        assertThat(issue).hasMessage("").hasSeverity(Severity.WARNING_NORMAL);
        assertThat(issue.getDescription()).contains("GHSA-empty-desc");
    }

    /** Covers: summary present, details empty → only title paragraph added. */
    @Test
    void shouldBuildDescriptionWithSummaryButNoDetails() {
        var report = parseStringContent("""
                {
                    "results": [
                        {
                            "source": { "path": "package.json", "type": "lockfile" },
                            "packages": [
                                {
                                    "package": { "name": "pkg", "version": "1.0", "ecosystem": "npm" },
                                    "vulnerabilities": [
                                        {
                                            "id": "GHSA-sum-only",
                                            "summary": "A summary only"
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                }
                """);
        assertThat(report).hasSize(1);
        assertThat(report.get(0).getDescription())
                .contains("A summary only")
                .contains("GHSA-sum-only");
    }

    /** Covers: vuln has no {@code aliases} key → Optional.empty(). */
    @Test
    void shouldBuildDescriptionWithNoAliasesKey() {
        var report = parseStringContent("""
                {
                    "results": [
                        {
                            "source": { "path": "go.mod", "type": "lockfile" },
                            "packages": [
                                {
                                    "package": { "name": "pkg", "version": "1.0", "ecosystem": "Go" },
                                    "vulnerabilities": [
                                        {
                                            "id": "GHSA-no-alias",
                                            "summary": "No alias vulnerability",
                                            "database_specific": { "severity": "HIGH" }
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                }
                """);
        assertThat(report).hasSize(1);
        assertThat(report.get(0).getDescription())
                .doesNotContain("Aliases:")
                .contains("GHSA-no-alias");
    }

    /** Covers: {@code aliases} present but is an empty array → Optional.empty(). */
    @Test
    void shouldBuildDescriptionWithEmptyAliasesArray() {
        var report = parseStringContent("""
                {
                    "results": [
                        {
                            "source": { "path": "go.mod", "type": "lockfile" },
                            "packages": [
                                {
                                    "package": { "name": "pkg", "version": "1.0", "ecosystem": "Go" },
                                    "vulnerabilities": [
                                        {
                                            "id": "GHSA-empty-alias",
                                            "summary": "Empty aliases array",
                                            "aliases": [],
                                            "database_specific": { "severity": "HIGH" }
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                }
                """);
        assertThat(report).hasSize(1);
        assertThat(report.get(0).getDescription()).doesNotContain("Aliases:");
    }

    /**
     * Covers: alias entries are blank strings → {@code alias.isEmpty()} continue;
     * resulting {@code links} list is empty → {@code links.isEmpty()} → Optional.empty().
     */
    @Test
    void shouldSkipBlankAliasEntriesAndReturnNoAliasSection() {
        var report = parseStringContent("""
                {
                    "results": [
                        {
                            "source": { "path": "go.mod", "type": "lockfile" },
                            "packages": [
                                {
                                    "package": { "name": "pkg", "version": "1.0", "ecosystem": "Go" },
                                    "vulnerabilities": [
                                        {
                                            "id": "GHSA-blank-alias",
                                            "summary": "Blank alias entries",
                                            "aliases": ["   ", ""],
                                            "database_specific": { "severity": "HIGH" }
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                }
                """);
        assertThat(report).hasSize(1);
        assertThat(report.get(0).getDescription()).doesNotContain("Aliases:");
    }

    /**
     * Covers: non-CVE alias → rendered as plain {@code text()} node, not an anchor;
     * single alias → joinWithSeparator with one item (no separator added).
     */
    @Test
    void shouldRenderNonCveAliasAsPlainText() {
        var report = parseStringContent("""
                {
                    "results": [
                        {
                            "source": { "path": "go.mod", "type": "lockfile" },
                            "packages": [
                                {
                                    "package": { "name": "pkg", "version": "1.0", "ecosystem": "Go" },
                                    "vulnerabilities": [
                                        {
                                            "id": "GHSA-non-cve",
                                            "summary": "Non-CVE alias",
                                            "aliases": ["GHSA-xxxx-yyyy-zzzz"],
                                            "database_specific": { "severity": "MODERATE" }
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                }
                """);
        assertThat(report).hasSize(1);
        var description = report.get(0).getDescription();
        assertThat(description)
                .contains("Aliases:")
                .contains("GHSA-xxxx-yyyy-zzzz")
                .doesNotContain("nvd.nist.gov");
    }

    /**
     * Covers: CVE alias → rendered as NVD hyperlink;
     * multiple aliases → the {@code it.hasNext()} true branch in joinWithSeparator adds ", ".
     */
    @Test
    void shouldRenderMultipleAliasesWithSeparatorAndCveLinkToNvd() {
        var report = parseStringContent("""
                {
                    "results": [
                        {
                            "source": { "path": "package-lock.json", "type": "lockfile" },
                            "packages": [
                                {
                                    "package": { "name": "lodash", "version": "4.17.15", "ecosystem": "npm" },
                                    "vulnerabilities": [
                                        {
                                            "id": "GHSA-multi-alias",
                                            "summary": "Multi alias vuln",
                                            "aliases": ["CVE-2021-0001", "GHSA-xxxx-1111"],
                                            "database_specific": { "severity": "HIGH" }
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                }
                """);
        assertThat(report).hasSize(1);
        var description = report.get(0).getDescription();
        assertThat(description)
                .contains("Aliases:")
                .contains("CVE-2021-0001")
                .contains("nvd.nist.gov/vuln/detail/CVE-2021-0001")
                .contains("GHSA-xxxx-1111")
                .contains(", ");
    }

    /** Covers: {@code id} field absent → optString returns "" → no OSV link rendered. */
    @Test
    void shouldNotRenderOsvLinkWhenIdIsEmpty() {
        var report = parseStringContent("""
                {
                    "results": [
                        {
                            "source": { "path": "package.json", "type": "lockfile" },
                            "packages": [
                                {
                                    "package": { "name": "pkg", "version": "1.0", "ecosystem": "npm" },
                                    "vulnerabilities": [
                                        {
                                            "summary": "No id field at all",
                                            "database_specific": { "severity": "LOW" }
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                }
                """);
        assertThat(report).hasSize(1);
        assertThat(report.get(0).getDescription()).doesNotContain("OSV Entry:");
    }

    /** Covers: {@code id} is exactly {@code "-"} (parser's default fallback) → no OSV link. */
    @Test
    void shouldNotRenderOsvLinkWhenIdIsDash() {
        var report = parseStringContent("""
                {
                    "results": [
                        {
                            "source": { "path": "package.json", "type": "lockfile" },
                            "packages": [
                                {
                                    "package": { "name": "pkg", "version": "1.0", "ecosystem": "npm" },
                                    "vulnerabilities": [
                                        {
                                            "id": "-",
                                            "summary": "Dash id vuln",
                                            "database_specific": { "severity": "LOW" }
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                }
                """);
        assertThat(report).hasSize(1);
        assertThat(report.get(0).getDescription()).doesNotContain("OSV Entry:");
    }

    @Test
    void shouldContainDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("osv-scanner");

        assertThat(descriptor.getPattern()).isEqualTo("**/osv-scanner-report.json");
        assertThat(descriptor.getHelp()).contains("osv-scanner scan --format json");
        assertThat(descriptor.getUrl()).isEqualTo("https://google.github.io/osv-scanner/");
        assertThat(descriptor.getIconUrl()).isEqualTo("https://github.com/google/osv-scanner/blob/main/docs/images/osv-scanner-full-logo-darkmode.png");
        assertThat(descriptor.hasHelp()).isTrue();
        assertThat(descriptor.hasUrl()).isTrue();
    }

    @Override
    protected IssueParser createParser() {
        return new OsvScannerParser();
    }
}
