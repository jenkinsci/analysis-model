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
 * Tests the class {@link FossaParser}.
 *
 * @author Akash Manna
 */
class FossaParserTest extends AbstractParserTest {
    FossaParserTest() {
        super("fossa-report.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(3);

        softly.assertThat(report.get(0))
                .hasPackageName("npm+lodash$4.17.15")
                .hasSeverity(Severity.ERROR)
                .hasCategory("Security")
                .hasType("vulnerability")
                .hasMessage("Vulnerability");

        softly.assertThat(report.get(0).getDescription())
                .contains("<strong>Priority:</strong>")
                .contains("critical")
                .contains("<strong>Resolved:</strong>")
                .contains("false")
                .contains("<strong>Rule ID:</strong>")
                .contains("101")
                .contains("CVE-2021-23337")
                .contains("<strong>Fixed In:</strong>")
                .contains("4.17.21")
                .contains("Issue URL");

        softly.assertThat(report.get(1))
                .hasPackageName("mvn+org.apache.struts:struts-core$2.3.20")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("Compliance")
                .hasType("unlicensed_dependency")
                .hasMessage("Unlicensed Dependency");

        softly.assertThat(report.get(1).getDescription())
                .contains("<strong>Priority:</strong>")
                .contains("medium")
                .contains("<strong>Rule ID:</strong>")
                .contains("102")
                .contains("<strong>Fixed In:</strong>")
                .contains("2.3.36");

        softly.assertThat(report.get(2))
                .hasPackageName("npm+left-pad$1.3.0")
                .hasSeverity(Severity.WARNING_LOW)
                .hasCategory("Security")
                .hasType("risk_empty_package")
                .hasMessage("Empty Package");

        softly.assertThat(report.get(2).getDescription())
                .contains("<strong>Priority:</strong>")
                .contains("low")
                .contains("<strong>Resolved:</strong>")
                .contains("true");
    }

    @Test
    void accepts() {
        assertThat(new FossaParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("fossa-report.json")))).isTrue();
        assertThat(new FossaParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("foo.txt")))).isFalse();
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt"))
                .isInstanceOf(ParsingException.class);
    }

    @Test
    void emptyInput() throws ParsingException {
        var report = parse("issues-no-issues.json");

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleMissingIssuesArray() {
        var report = parseStringContent("""
                {
                  "count": 0,
                  "status": "SCANNED"
                }
                """);

        assertThat(report).isEmpty();
    }

    @Test
    void shouldSkipNullIssuesAndRenderNonCveText() {
        var report = parseStringContent("""
                {
                  "count": 2,
                  "issues": [
                    null,
                    {
                      "priorityString": "low",
                      "resolved": false,
                      "revisionId": "custom+project$1.0.0",
                      "type": "policy_flag",
                      "cve": "NOT-A-CVE"
                    }
                  ],
                  "status": "SCANNED"
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasPackageName("custom+project$1.0.0")
                .hasSeverity(Severity.WARNING_LOW)
                .hasCategory("Compliance")
                .hasType("policy_flag")
                .hasMessage("Flagged by Policy");

        assertThat(report.get(0).getDescription())
                .contains("<strong>CVE:</strong>")
                .contains("NOT-A-CVE")
                .doesNotContain("nvd.nist.gov/vuln/detail");
    }

    @Test
    void shouldHandleMissingPriorityAndUnknownType() {
        var report = parseStringContent("""
                {
                  "count": 2,
                  "issues": [
                    {
                      "resolved": false,
                      "type": "policy_conflict"
                    },
                    {
                      "priorityString": "high",
                      "resolved": true,
                      "revisionId": "custom+project$1.0.0",
                      "type": "custom_fossa_type",
                      "issueDashURL": "https://app.fossa.com/issues/999"
                    }
                  ],
                  "status": "SCANNED"
                }
                """);

        assertThat(report).hasSize(2);

        assertThat(report.get(0))
                .hasPackageName("-")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("Compliance")
                .hasType("policy_conflict")
                .hasMessage("Denied by Policy");

        assertThat(report.get(1))
                .hasPackageName("custom+project$1.0.0")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("Other")
                .hasType("custom_fossa_type")
                .hasMessage("custom_fossa_type");

        assertThat(report.get(1).getDescription())
                .contains("<strong>Priority:</strong>")
                .contains("high")
                .contains("<strong>Resolved:</strong>")
                .contains("true")
                .contains("Issue URL");
    }

    @Test
    void shouldProvideDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("fossa");

        assertThat(descriptor.getPattern()).isEqualTo("**/fossa-report.json");
        assertThat(descriptor.getHelp()).contains("fossa test --format json");
        assertThat(descriptor.getUrl()).isEqualTo("https://docs.fossa.com/");
        assertThat(descriptor.getIconUrl()).isEqualTo("https://github.com/fossas/fossa-cli/blob/master/docs/assets/header.png?raw=true");
        assertThat(descriptor.hasHelp()).isTrue();
        assertThat(descriptor.hasUrl()).isTrue();
    }

    @Override
    protected IssueParser createParser() {
        return new FossaParser();
    }
}