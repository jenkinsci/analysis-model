package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;
import edu.hm.hafner.analysis.registry.ParserRegistry;
import edu.hm.hafner.analysis.Report.IssueType;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link OpenScapParser}.
 *
 * @author Akash Manna
 */
class OpenScapParserTest extends AbstractParserTest {
    OpenScapParserTest() {
        super("openscap-report.json");
    }

    @Override
    protected IssueParser createParser() {
        return new OpenScapParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(3).hasDuplicatesSize(0);

        softly.assertThat(report.get(0))
                .hasFileName("config/login.defs")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasType("xccdf_org.ssgproject.content_rule_accounts_umask_etc_login_defs")
                .hasMessage("Set Default umask for Users - umask setting is not restrictive enough")
                .hasCategory("fail");

        softly.assertThat(report.get(0).getDescription())
                .contains("The umask controls the default permissions of newly created files")
                .contains("Evidence: umask setting is not restrictive enough");

        softly.assertThat(report.get(1))
                .hasFileName("config/selinux")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasType("xccdf_org.ssgproject.content_rule_selinux_policy")
                .hasMessage("Ensure SELinux Policy is Configured - SELinux is not enabled")
                .hasCategory("fail");

        softly.assertThat(report.get(1).getDescription())
                .contains("SELinux provides mandatory access control policies")
                .contains("Evidence: SELinux is not enabled");

        softly.assertThat(report.get(2))
                .hasFileName("config/sshd_config")
                .hasSeverity(Severity.ERROR)
                .hasType("xccdf_org.ssgproject.content_rule_ssh_password_authentication")
                .hasMessage("Disable SSH Password Authentication - PasswordAuthentication is enabled in SSH configuration")
                .hasCategory("fail");

        softly.assertThat(report.get(2).getDescription())
                .contains("SSH password authentication should be disabled")
                .contains("Evidence: PasswordAuthentication is enabled in SSH configuration");
    }

    @Test
    void shouldParseEdgeCases() {
        var report = parse("openscap-report-edge-cases.json");

        assertThat(report).hasSize(2).hasDuplicatesSize(0);

        assertThat(report.get(0))
                .hasFileName("config/firewall")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasType("xccdf_org.ssgproject.content_rule_firewall_enabled")
                .hasMessage("Firewall Service Enabled")
                .hasCategory("fail");

        assertThat(report.get(0).getDescription())
                .contains("The firewall service should be enabled to protect the system");

        assertThat(report.get(1))
                .hasFileName("config/audit")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasType("xccdf_org.ssgproject.content_rule_audit_enabled")
                .hasMessage("Audit Daemon Enabled")
                .hasCategory("error");

        assertThat(report.get(1).getDescription())
                .contains("The audit daemon should be enabled to log system activities");
    }

    @Test
    void shouldNotReportPassResults() {
        var report = parse("openscap-report.json");
        assertThat(report.get())
                .map(Issue::getCategory)
                .doesNotContain("pass", "notapplicable");
    }

    @Test
    void shouldHandleNullRuleObject() {
        var report = parse("openscap-report-null-handling.json");

        assertThat(report).hasSize(3).hasDuplicatesSize(0);

        assertThat(report.get(0))
                .hasFileName("config/test.conf")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasType("-")
                .hasMessage("Unknown - Test without rule object")
                .hasCategory("fail");

        assertThat(report.get(1))
                .hasFileName("config/test2.conf")
                .hasSeverity(Severity.ERROR)
                .hasType("test-rule-1")
                .hasMessage("Test Rule With Description")
                .hasCategory("fail");

        assertThat(report.get(1).getDescription())
                .contains("Detailed description of the issue");

        assertThat(report.get(2))
                .hasFileName("config/test3.conf")
                .hasSeverity(Severity.WARNING_LOW)
                .hasType("test-rule-2")
                .hasMessage("Test Rule Without Description")
                .hasCategory("error");
    }

    @Test
    void shouldCombineEvidenceAndDescription() {
        var report = parse("openscap-report-null-handling.json");
        assertThat(report.get(0)).hasDescription("Test without rule object");
        assertThat(report.get(1)).hasDescription("Detailed description of the issue");
    }

    @Test
    void shouldMapSeverityCorrectly() {
        var report = parse("openscap-report-null-handling.json");

        assertThat(report.get(0)).hasSeverity(Severity.WARNING_HIGH);
        assertThat(report.get(1)).hasSeverity(Severity.ERROR);
        assertThat(report.get(2)).hasSeverity(Severity.WARNING_LOW);
    }

    @Test
    void shouldFilterFailAndErrorResults() {
        var report = parse("openscap-report-null-handling.json");
        assertOnlyFailureCategories(report);
    }

    @Test
    void shouldUseDefaultFileNameWhenMissing() {
        var report = parse("openscap-report-null-handling.json");
        assertThat(report.get())
                .map(Issue::getFileName)
                .allSatisfy(fileName -> assertThat(fileName).isNotNull().isNotEmpty());
    }

    @Test
    void shouldParseMainReport() {
        assertOnlyFailureCategories(parse("openscap-report.json"));
    }

    @Test
    void shouldParseEdgeCaseReport() {
        assertOnlyFailureCategories(parse("openscap-report-edge-cases.json"));
    }

    @Test
    void shouldProvideDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("openscap");

        assertThat(descriptor.getPattern()).isEqualTo("**/openscap-report.json");
        assertThat(descriptor.getHelp()).contains("oscap scan --results-arf results.xml --report report.html");
        assertThat(descriptor.getUrl()).isEqualTo("https://github.com/OpenSCAP/openscap");
        assertThat(descriptor.getIconUrl()).isEqualTo("https://github.com/OpenSCAP/openscap/blob/main/docs/manual/images/vertical-logo.png");
        assertThat(descriptor.getType()).isEqualTo(IssueType.WARNING);
        assertThat(descriptor.hasHelp()).isTrue();
        assertThat(descriptor.hasUrl()).isTrue();
    }

    private void assertOnlyFailureCategories(final Report report) {
        assertThat(report.get())
                .map(Issue::getCategory)
                .allSatisfy(category -> assertThat(category).isIn("fail", "error"));
    }
}