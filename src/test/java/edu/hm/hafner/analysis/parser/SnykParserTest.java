package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link SnykParser}.
 *
 * @author Akash Manna
 */
class SnykParserTest extends AbstractParserTest {
    SnykParserTest() {
        super("snyk-report.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(3).hasDuplicatesSize(0);

        softly.assertThat(report.get(0))
                .hasFileName("package.json")
                .hasPackageName("lodash@4.17.15")
                .hasSeverity(Severity.ERROR)
                .hasCategory("javascript")
                .hasType("SNYK-JS-LODASH-73638")
                .hasMessage("Prototype Pollution");

        softly.assertThat(report.get(0).getDescription())
                .contains("Prototype Pollution")
                .contains("CVE-2021-23337")
                .contains("CWE-1321")
                .contains("CVSS:3.1/AV:N/AC:L/PR:N/UI:N/S:U/C:H/I:H/A:H");

        softly.assertThat(report.get(1))
                .hasFileName("package.json")
                .hasPackageName("yargs-parser@13.1.1")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("javascript")
                .hasType("SNYK-JS-YARGS-PARSER-2855821")
                .hasMessage("Command Injection");

        softly.assertThat(report.get(1).getDescription())
                .contains("Command Injection")
                .contains("CVE-2020-7608")
                .contains("CWE-78")
                .contains("Suggested Fix")
                .contains("yargs@16.0.0");

        softly.assertThat(report.get(2))
                .hasFileName("package.json")
                .hasPackageName("minimist@1.2.0")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("javascript")
                .hasType("SNYK-JS-MINIMIST-2429795")
                .hasMessage("Prototype Pollution");
    }

    @Test
    void shouldParseEdgeCases() {
        var report = parse("snyk-report-edge-cases.json");

        assertThat(report).hasSize(2).hasDuplicatesSize(0);

        assertThat(report.get(0))
                .hasFileName("requirements.txt")
                .hasPackageName("PyYAML@5.3.1")
                .hasSeverity(Severity.ERROR)
                .hasCategory("python")
                .hasType("SNYK-PYTHON-PYYAML-590824")
                .hasMessage("Code Injection");

        assertThat(report.get(0).getDescription())
                .contains("Code Injection")
                .contains("CVE-2020-14343")
                .contains("CWE-94")
                .contains("Suggested Fix")
                .contains("PyYAML@5.4");

        assertThat(report.get(1))
                .hasFileName("pom.xml")
                .hasPackageName("org.apache.struts:struts-core@2.3.20")
                .hasSeverity(Severity.ERROR)
                .hasCategory("java")
                .hasType("SNYK-JAVA-ORGAPACHESTRUTSJARS-STRUTSSHUTDOWN-2328360")
                .hasMessage("Arbitrary Code Execution");

        assertThat(report.get(1).getDescription())
                .contains("Arbitrary Code Execution")
                .contains("CVE-2015-4807")
                .contains("OGNL injection");
    }

    @Test
    void testMessageAndDescriptionParsing() {
        var report = parse("snyk-report.json");

        var first = report.get(0);
        assertThat(first.getMessage())
                .isEqualTo("Prototype Pollution");

        assertThat(first.getDescription())
                .contains("All versions of lodash")
                .contains("CVE-2021-23337")
                .contains("CVSS:3.1");
    }

    @Test
    void shouldContainProperties() {
        var report = parse("snyk-report.json");

        assertThat(report.stream()).map(Issue::getPackageName).containsExactly(
                "lodash@4.17.15", "yargs-parser@13.1.1", "minimist@1.2.0");
        assertThat(report.stream()).map(Issue::getSeverity).containsExactly(
                Severity.ERROR, Severity.WARNING_HIGH, Severity.WARNING_NORMAL);
    }

    @Test
    void testHtmlStructureOfDescription() {
        var report = parse("snyk-report.json");
        var description = report.get(0).getDescription();

        assertThat(description)
                .as("Description should contain message section")
                .isEqualToIgnoringWhitespace("""
                        <p><strong>Prototype Pollution</strong></p>
                        <p>All versions of lodash versions before 4.17.21 are vulnerable to Prototype pollution via the toObject converter. This
                            allows attackers to inject arbitrary properties on Object.prototype which may lead to Denial of Service or Remote
                            Code Execution in specific circumstances.</p>
                        <p><strong>CVE ID(s):</strong>&nbsp;<a href="https://nvd.nist.gov/vuln/detail/CVE-2021-23337">CVE-2021-23337</a></p>
                        <p><strong>CWE ID(s):</strong>&nbsp;CWE-1321</p>
                        <p><strong>CVSS:</strong>&nbsp;CVSS:3.1/AV:N/AC:L/PR:N/UI:N/S:U/C:H/I:H/A:H</p>
                        <p><strong>Suggested Fix:</strong>&nbsp;app@1.0.0 → lodash@4.17.21</p>
                        """);
    }

    @Override
    protected IssueParser createParser() {
        return new SnykParser();
    }
}
