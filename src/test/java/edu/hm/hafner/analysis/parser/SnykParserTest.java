package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

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
    void testParsingEdgeCases() {
        var report = parse("snyk-report-edge-cases.json");

        try (var softly = new SoftAssertions()) {
            softly.assertThat(report).hasSize(2).hasDuplicatesSize(0);

            softly.assertThat(report.get(0))
                    .hasFileName("requirements.txt")
                    .hasPackageName("PyYAML@5.3.1")
                    .hasSeverity(Severity.ERROR)
                    .hasCategory("python")
                    .hasType("SNYK-PYTHON-PYYAML-590824")
                    .hasMessage("Code Injection");

            softly.assertThat(report.get(0).getDescription())
                    .contains("Code Injection")
                    .contains("CVE-2020-14343")
                    .contains("CWE-94")
                    .contains("Suggested Fix")
                    .contains("PyYAML@5.4");

            softly.assertThat(report.get(1))
                    .hasFileName("pom.xml")
                    .hasPackageName("org.apache.struts:struts-core@2.3.20")
                    .hasSeverity(Severity.ERROR)
                    .hasCategory("java")
                    .hasType("SNYK-JAVA-ORGAPACHESTRUTSJARS-STRUTSSHUTDOWN-2328360")
                    .hasMessage("Arbitrary Code Execution");

            softly.assertThat(report.get(1).getDescription())
                    .contains("Arbitrary Code Execution")
                    .contains("CVE-2015-4807")
                    .contains("OGNL injection");
        }
    }

    @Test
    void testMessageAndDescriptionParsing() {
        var report = parse("snyk-report.json");

        try (var softly = new SoftAssertions()) {
            var issue0 = report.get(0);
            softly.assertThat(issue0.getMessage())
                    .isEqualTo("Prototype Pollution");

            var desc0 = issue0.getDescription();
            softly.assertThat(desc0)
                    .contains("All versions of lodash")
                    .contains("CVE-2021-23337")
                    .contains("CVSS:3.1");
        }
    }

    @Test
    void testPackageNameFormatting() {
        var report = parse("snyk-report.json");

        try (var softly = new SoftAssertions()) {
            softly.assertThat(report.get(0).getPackageName()).isEqualTo("lodash@4.17.15");
            softly.assertThat(report.get(1).getPackageName()).isEqualTo("yargs-parser@13.1.1");
            softly.assertThat(report.get(2).getPackageName()).isEqualTo("minimist@1.2.0");
        }
    }

    @Test
    void testSeverityMapping() {
        var report = parse("snyk-report.json");

        try (var softly = new SoftAssertions()) {
            softly.assertThat(report.get(0).getSeverity()).isEqualTo(Severity.ERROR);
            softly.assertThat(report.get(1).getSeverity()).isEqualTo(Severity.WARNING_HIGH);
            softly.assertThat(report.get(2).getSeverity()).isEqualTo(Severity.WARNING_NORMAL);
        }
    }

    @Override
    protected IssueParser createParser() {
        return new SnykParser();
    }
}
