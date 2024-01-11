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
class BanditAdapterTest extends AbstractParserTest {
    BanditAdapterTest() {
        super("bandit.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(1);
        softly.assertThat(report.get(0))
                .hasMessage("B307: Use of possibly insecure function - consider using safer ast.literal_eval.")
                .hasFileName("/jenkins/workspace/ure_RC_SMTP_Bridge_flake8-bandit@2/rc_smtp_bridge/rc_smtp_bridge.py")
                .hasLineStart(42)
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Override
    protected BanditAdapter createParser() {
        return new BanditAdapter();
    }
}
