package edu.hm.hafner.analysis.parser.violations;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link ZptLintAdapter}.
 *
 * @author Ullrich Hafner
 */
class ZptLintAdapterTest extends AbstractParserTest {
    ZptLintAdapterTest() {
        super("zptlint.log");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(2);
        softly.assertThat(report.get(0))
                .hasMessage("abc def ghe '\" 123")
                .hasFileName("cpplint.py")
                .hasLineStart(4796)
                .hasSeverity(Severity.WARNING_HIGH);
    }

    @Override
    protected ZptLintAdapter createParser() {
        return new ZptLintAdapter();
    }
}