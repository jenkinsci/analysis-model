package edu.hm.hafner.analysis.parser.violations;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

/**
 * Tests the class {@link JsHintAdapter}.
 *
 * @author Ullrich Hafner
 */
class JsHintAdapterTest extends AbstractParserTest {
    JsHintAdapterTest() {
        super("jshint.xml");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(6);
        softly.assertThat(report.get(2))
                .hasMessage("Use '===' to compare with 'null'.: if (a == null)")
                .hasFileName("../../../web/js-file.js")
                .hasLineStart(4)
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Override
    protected JsHintAdapter createParser() {
        return new JsHintAdapter();
    }
}
