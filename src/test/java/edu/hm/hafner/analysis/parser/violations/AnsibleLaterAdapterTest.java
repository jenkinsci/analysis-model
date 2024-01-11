package edu.hm.hafner.analysis.parser.violations;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

/**
 * Tests the class {@link CodeNarcAdapter}.
 *
 * @author Ullrich Hafner
 */
class AnsibleLaterAdapterTest extends AbstractParserTest {
    AnsibleLaterAdapterTest() {
        super("ansible-later.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(2);
        softly.assertThat(report.get(0))
                .hasMessage("[ANSIBLE0004] Standard 'YAML should use consistent number of spaces around variables' not met: simple_role.yml:7: no suitable numbers of spaces (min: 1 max: 1)")
                .hasFileName("simple_role.yml")
                .hasType("ANSIBLE0004")
                .hasLineStart(7)
                .hasSeverity(Severity.WARNING_HIGH);
        softly.assertThat(report.get(1))
                .hasMessage("[ANSIBLE9998] Best practice 'Standards version should be pinned' not met: simple_role.yml: Standards version not set. Using latest standards version 0.2")
                .hasFileName("simple_role.yml")
                .hasType("ANSIBLE9998")
                .hasLineStart(0)
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Override
    protected AnsibleLaterAdapter createParser() {
        return new AnsibleLaterAdapter();
    }
}
