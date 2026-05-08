package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static j2html.TagCreator.*;

class AnchoreCTLParserTest extends AbstractParserTest {
    AnchoreCTLParserTest() {
        super("anchorectl-report.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(3).hasDuplicatesSize(0);

        softly.assertThat(report.get(0))
                .hasMessage("CVE-2021-1234")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasPackageName("openssl")
                .hasCategory("rpm")
                .hasFileName("/usr/lib64/libssl.so.1.1")
                .hasDescription(join(
                        p(join(b("Fix:"), text(" 1.1.1l"))),
                        p(join(text("Affected version: "), text("1.1.1k"))),
                        p(a("CVE-2021-1234").withHref("https://nvd.nist.gov/vuln/detail/CVE-2021-1234"))
                ).render());

        softly.assertThat(report.get(1))
                .hasMessage("CVE-2021-5678")
                .hasSeverity(Severity.ERROR)
                .hasPackageName("busybox")
                .hasCategory("APKG")
                .hasFileName("/lib/apk/db/installed")
                .hasDescription(join(
                        p(text("No fix available")),
                        p(join(text("Affected version: "), text("1.34.0-r0"))),
                        p(b("CISA Known Exploited Vulnerability (KEV)")),
                        p(a("CVE-2021-5678").withHref("https://nvd.nist.gov/vuln/detail/CVE-2021-5678"))
                ).render());

        softly.assertThat(report.get(2))
                .hasMessage("CVE-2022-9999")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasPackageName("zlib")
                .hasCategory("rpm")
                .hasFileName("/lib64/libz.so.1")
                .hasDescription(join(
                        p(text("No fix available")),
                        p(join(text("Affected version: "), text("1.2.11"))),
                        p(text("Vendor will not fix")),
                        p(a("CVE-2022-9999").withHref("https://nvd.nist.gov/vuln/detail/CVE-2022-9999"))
                ).render());
    }

    @Test
    void shouldParseUnifiedEnvelopeOutput() {
        var report = parse("anchorectl-unified-report.json");

        try (var softly = new SoftAssertions()) {
            softly.assertThat(report).hasSize(1);
            softly.assertThat(report.get(0))
                    .hasMessage("CVE-2025-46394")
                    .hasSeverity(Severity.WARNING_LOW)
                    .hasPackageName("busybox-binsh")
                    .hasFileName("/lib/apk/db/installed");
        }
    }

    @Test
    void shouldParseSnakeCaseJsonRawOutput() {
        var report = parse("anchorectl-report-raw.json");

        try (var softly = new SoftAssertions()) {
            softly.assertThat(report).hasSize(1);
            softly.assertThat(report.get(0))
                    .hasMessage("CVE-2022-3333")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasPackageName("zlib")
                    .hasCategory("rpm")
                    .hasFileName("/lib64/libz.so.1")
                    .hasDescription(join(
                            p(join(b("Fix:"), text(" 1.2.12"))),
                            p(join(text("Affected version: "), text("1.2.11"))),
                            p(a("CVE-2022-3333").withHref("https://nvd.nist.gov/vuln/detail/CVE-2022-3333"))
                    ).render());
        }
    }

    @Test
    void shouldSkipRowWithBlankVulnId() {
        var report = parse("anchorectl-missing-vuln-id.json");

        try (var softly = new SoftAssertions()) {
            softly.assertThat(report).hasSize(1);
            softly.assertThat(report.get(0)).hasMessage("CVE-2021-5678");
        }
    }

    @Test
    void shouldReturnEmptyReportForEmptyArray() {
        var report = parse("anchorectl-empty.json");
        try (var softly = new SoftAssertions()) {
            softly.assertThat(report).isEmpty();
        }
    }

    @Test
    void shouldReturnEmptyReportWhenVulnerabilitiesKeyAbsent() {
        var report = parse("anchorectl-no-vuln-key.json");
        try (var softly = new SoftAssertions()) {
            softly.assertThat(report).isEmpty();
        }
    }

    @Test
    void shouldFallBackToPublUrlWhenPackagePathIsEmpty() {
        var report = parse("anchorectl-purl-fallback.json");

        try (var softly = new SoftAssertions()) {
            softly.assertThat(report).hasSize(1);
            softly.assertThat(report.get(0))
                    .hasMessage("CVE-2021-9999")
                    .hasSeverity(Severity.WARNING_LOW)
                    .hasFileName("pkg:pypi/requests@2.26.0")
                    .hasDescription(join(
                            p(join(b("Fix:"), text(" 2.27.0"))),
                            p(join(text("Affected version: "), text("2.26.0")))
                    ).render());
        }
    }

    @Test
    void shouldMapNegligibleSeverityToWarningLow() {
        var report = parse("anchorectl-negligible.json");

        try (var softly = new SoftAssertions()) {
            softly.assertThat(report).hasSize(1);
            softly.assertThat(report.get(0))
                    .hasMessage("CVE-2021-1111")
                    .hasSeverity(Severity.WARNING_LOW);
        }
    }

    @Test
    void shouldParseBareArrayFromImageVulnOutput() {
        var report = parse("anchorectl-vuln-bare-array.json");

        try (var softly = new SoftAssertions()) {
            softly.assertThat(report).hasSize(2);
            softly.assertThat(report.get(0))
                    .hasMessage("CVE-2024-1234")
                    .hasSeverity(Severity.WARNING_HIGH)
                    .hasPackageName("openssl")
                    .hasFileName("/usr/lib/x86_64-linux-gnu/libssl.so.3");
            softly.assertThat(report.get(1))
                    .hasMessage("CVE-2024-5678")
                    .hasSeverity(Severity.ERROR)
                    .hasPackageName("zlib1g")
                    .hasFileName("/lib/x86_64-linux-gnu/libz.so.1");
        }
    }

    @Test
    void shouldUseDashWhenPackagePathAndPurlAreBlank() {
        var report = parse("anchorectl-no-path-no-purl.json");

        try (var softly = new SoftAssertions()) {
            softly.assertThat(report).hasSize(1);
            softly.assertThat(report.get(0))
                    .hasMessage("CVE-2021-7777")
                    .hasSeverity(Severity.WARNING_HIGH)
                    .hasFileName("-")
                    .hasDescription(join(
                            p(text("No fix available")),
                            p(join(text("Affected version: "), text("1.0.0")))
                    ).render());
        }
    }

    @Override
    protected IssueParser createParser() {
        return new AnchoreCTLParser();
    }
}
