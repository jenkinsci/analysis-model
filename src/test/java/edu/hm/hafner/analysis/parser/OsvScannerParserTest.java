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

    @Test
    void emptyReport() {
        var report = parse("osv-scanner-no-issues.json");

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleEdgeCases() {
        var report = parse("osv-scanner-edge-cases.json");

        assertThat(report).hasSize(2);

        assertThat(report.get(0))
                .hasFileName("requirements.txt")
                .hasPackageName("PyYAML@5.3.1")
                .hasCategory("PyPI")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasType("GHSA-8q59-q68h-6hv4")
                .hasMessage("Arbitrary code execution in PyYAML");

        assertThat(report.get(1))
                .hasFileName("requirements.txt")
                .hasPackageName("no-severity-package@2.0.0")
                .hasCategory("PyPI")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasType("GHSA-0000-0000-0000")
                .hasMessage("Vulnerability without severity field");
    }

    @Test
    void shouldHandleMissingResultsKey() {
        var report = parseStringContent("{\"other\": []}");

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleEmptyPackages() {
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

    @Test
    void shouldHandleMissingSourcePath() {
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
        assertThat(report.get(0))
                .hasFileName("-")
                .hasPackageName("testpkg@1.0.0")
                .hasCategory("npm")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasType("GHSA-test-0001")
                .hasMessage("Test vulnerability");
    }

    @Test
    void shouldHandlePackageWithNoVersionField() {
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
                .hasFileName("Cargo.lock")
                .hasPackageName("mylib")
                .hasSeverity(Severity.ERROR)
                .hasType("RUSTSEC-2022-0001")
                .hasMessage("Use after free in mylib");
    }

    @Test
    void shouldContainDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("osv-scanner");

        assertThat(descriptor.getPattern()).isEqualTo("**/osv-scanner-report.json");
        assertThat(descriptor.getHelp()).contains("osv-scanner scan --format json");
        assertThat(descriptor.getUrl()).isEqualTo("https://google.github.io/osv-scanner/");
        assertThat(descriptor.hasHelp()).isTrue();
        assertThat(descriptor.hasUrl()).isTrue();
    }

    @Override
    protected IssueParser createParser() {
        return new OsvScannerParser();
    }
}
