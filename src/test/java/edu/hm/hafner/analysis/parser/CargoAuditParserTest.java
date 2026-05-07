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

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link CargoAuditParser}.
 *
 * @author Akash Manna
 */
class CargoAuditParserTest extends AbstractParserTest {
    CargoAuditParserTest() {
        super("cargo-audit.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(3).hasDuplicatesSize(0);

        softly.assertThat(report.get(0))
                .hasFileName("chrono")
                .hasPackageName("chrono")
                .hasSeverity(Severity.ERROR)
                .hasType("RUSTSEC-2021-0079")
                .hasMessage("Buffer overflow in chrono before 0.4.19");

        softly.assertThat(report.get(0).getDescription())
                .contains("Buffer overflow in chrono")
                .contains("CVSS:3.1/AV:N/AC:L/PR:N/UI:N/S:U/C:H/I:H/A:H")
                .contains("https://rustsec.org/advisories/RUSTSEC-2021-0079")
                .contains("2021-07-05");

        softly.assertThat(report.get(1))
                .hasFileName("sodiumoxide")
                .hasPackageName("sodiumoxide")
                .hasSeverity(Severity.ERROR)
                .hasType("RUSTSEC-2020-0001")
                .hasMessage("Cryptographic failure in sodiumoxide before 0.2.6");

        softly.assertThat(report.get(1).getDescription())
                .contains("Cryptographic failure in sodiumoxide")
                .contains("https://rustsec.org/advisories/RUSTSEC-2020-0001")
                .contains("2020-01-01");

        softly.assertThat(report.get(2))
                .hasFileName("rusqlite")
                .hasPackageName("rusqlite")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasType("RUSTSEC-2021-0120")
                .hasMessage("SQL injection in rusqlite before 0.25");

        softly.assertThat(report.get(2).getDescription())
                .contains("SQL injection in rusqlite")
                .contains("https://rustsec.org/advisories/RUSTSEC-2021-0120")
                .contains("2021-10-15");
    }

    @Override
    protected IssueParser createParser() {
        return new CargoAuditParser();
    }

    @Test
    void accepts() {
        assertThat(new CargoAuditParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("cargo-audit.json")))).isTrue();
        assertThat(new CargoAuditParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("foo.txt")))).isFalse();
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt"))
                .isInstanceOf(ParsingException.class);
    }

    @Test
    void emptyVulnerabilities() {
        var report = parseStringContent("""
                {
                  "vulnerabilities": {},
                  "meta": {}
                }
                """);

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleMissingAdvisory() {
        var report = parseStringContent("""
                {
                  "vulnerabilities": {
                    "0": {
                      "versions": {
                        "patched": ["1.0"],
                        "unaffected": []
                      }
                    }
                  },
                  "meta": {}
                }
                """);

        assertThat(report).isEmpty();
    }

    @Test
    void shouldParseWithOptionalFields() {
        var report = parseStringContent("""
                {
                  "vulnerabilities": {
                    "0": {
                      "advisory": {
                        "id": "RUSTSEC-2021-9999",
                        "package": "test-package",
                        "title": "Test vulnerability"
                      },
                      "versions": {
                        "patched": ["2.0"],
                        "unaffected": ["1.5"]
                      }
                    }
                  },
                  "meta": {}
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("test-package")
                .hasType("RUSTSEC-2021-9999")
                .hasMessage("Test vulnerability");
    }

    @Test
    void testMessageAndDescriptionParsing() {
        var report = parse("cargo-audit.json");

        var first = report.get(0);
        assertThat(first.getMessage())
                .isEqualTo("Buffer overflow in chrono before 0.4.19");

        assertThat(first.getDescription())
                .contains("Buffer overflow in chrono")
                .contains("RUSTSEC-2021-0079");
    }

    @Test
    void shouldContainVulnerabilityProperties() {
        var report = parse("cargo-audit.json");

        assertThat(report.stream()).map(issue -> issue.getType()).containsExactly(
                "RUSTSEC-2021-0079", "RUSTSEC-2020-0001", "RUSTSEC-2021-0120");
        assertThat(report.stream()).map(issue -> issue.getPackageName()).containsExactly(
                "chrono", "sodiumoxide", "rusqlite");
    }
}
