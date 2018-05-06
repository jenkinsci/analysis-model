package edu.hm.hafner.analysis.parser.violations;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

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
        softly.assertThat(report.get(0))
                .hasMessage("Use '===' to compare with 'null'.: if (a == null)")
                .hasFileName("../../../web/js-file.js")
                .hasLineStart(4)
                .hasPriority(Priority.NORMAL);
    }

    @Override
    protected AbstractParser createParser() {
        return new JsHintAdapter();
    }
}