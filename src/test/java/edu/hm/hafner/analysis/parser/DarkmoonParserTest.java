package edu.hm.hafner.analysis.parser;

import static edu.hm.hafner.analysis.assertions.Assertions.assertThat;
import static edu.hm.hafner.analysis.assertions.Assertions.assertThatThrownBy;

import edu.hm.hafner.analysis.FileReaderFactory;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;
import java.nio.file.FileSystems;
import org.junit.jupiter.api.Test;

/** Tests the class {@link DarkmoonParser}. */
class DarkmoonParserTest extends AbstractParserTest {
    DarkmoonParserTest() {
        super("darkmoon-report.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(3);

        softly.assertThat(report.get(0))
                .hasFileName("https://demo.example.com/upload")
                .hasType("remote_code_execution")
                .hasCategory("exploited")
                .hasPackageName("php")
                .hasMessage("Unauthenticated remote code execution in file upload handler")
                .hasSeverity(Severity.ERROR);
        softly.assertThat(report.get(0).getDescription())
                .contains("without sanitisation")
                .contains("Status:")
                .contains("9.8")
                .contains("CVSS:3.1/AV:N/AC:L/PR:N/UI:N/S:U/C:H/I:H/A:H")
                .contains("https://nvd.nist.gov/vuln/detail/CVE-2026-12345")
                .contains("T1190 (Exploit Public-Facing Application)")
                .contains("Remediation:");

        softly.assertThat(report.get(1))
                .hasFileName("https://demo.example.com/profile/42")
                .hasType("xss_stored")
                .hasCategory("confirmed")
                .hasPackageName("nodejs")
                .hasSeverity(Severity.WARNING_HIGH);
        softly.assertThat(report.get(1).getDescription())
                .contains("A script payload stored in the display name executed")
                .contains("T1059.007")
                .doesNotContain("CVE:")
                .doesNotContain("Remediation:");

        softly.assertThat(report.get(2))
                .hasFileName("-")
                .hasType("information_disclosure")
                .hasCategory("unconfirmed")
                .hasPackageName("-")
                .hasSeverity(Severity.WARNING_LOW);
        softly.assertThat(report.get(2).getDescription())
                .contains("Remove the version from the Server header.")
                .doesNotContain("CVSS");
    }

    @Override
    protected IssueParser createParser() {
        return new DarkmoonParser();
    }

    @Test
    void shouldAcceptOnlyJsonFiles() {
        var parser = new DarkmoonParser();

        assertThat(parser.accepts(new FileReaderFactory(FileSystems.getDefault().getPath("darkmoon-report.json"))))
                .isTrue();
        assertThat(parser.accepts(new FileReaderFactory(FileSystems.getDefault().getPath("darkmoon-report.txt"))))
                .isFalse();
    }

    @Test
    void shouldParseSnakeCaseFindingsWrappedInObject() {
        var report = parse("darkmoon-report-snake-case.json");

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("https://demo.example.com/search?q=shoes")
                .hasType("sql_injection")
                .hasCategory("exploited")
                .hasPackageName("wordpress")
                .hasSeverity(Severity.WARNING_NORMAL);
        assertThat(report.get(0).getDescription())
                .contains("6.5")
                .contains("CVSS:3.1/AV:N/AC:L/PR:N/UI:N/S:U/C:H/I:N/A:N")
                .contains("https://nvd.nist.gov/vuln/detail/CVE-2026-55512")
                .contains("T1190 (Exploit Public-Facing Application)")
                .contains("Two payloads returned different result sets.")
                .contains("Use prepared statements.");
    }

    @Test
    void shouldReturnEmptyReportForEmptyFindings() {
        assertThat(parseStringContent("[]")).isEmpty();
        assertThat(parseStringContent("{\"findings\": []}")).isEmpty();
        assertThat(parseStringContent("{}")).isEmpty();
    }

    @Test
    void shouldUseDefaultsForMissingProperties() {
        var report = parseStringContent("[{}, \"not an object\"]");

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("-")
                .hasType("-")
                .hasCategory("-")
                .hasPackageName("-")
                .hasMessage("-")
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Test
    void shouldKeepNonCveIdentifierWithoutLink() {
        var report =
                parseStringContent("[{\"title\": \"t\", \"severity\": \"low\", \"cve\": \"GHSA-xxxx-yyyy-zzzz\"}]");

        assertThat(report).hasSize(1);
        assertThat(report.get(0)).hasSeverity(Severity.WARNING_LOW);
        assertThat(report.get(0).getDescription())
                .contains("GHSA-xxxx-yyyy-zzzz")
                .doesNotContain("nvd.nist.gov");
    }

    @Test
    void shouldRejectBrokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt")).isInstanceOf(ParsingException.class);
    }
}
