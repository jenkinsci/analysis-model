package edu.hm.hafner.analysis.parser.violations;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
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
    protected void assertThatIssuesArePresent(final Issues issues, final SoftAssertions softly) {
        softly.assertThat(issues).hasSize(6);
        softly.assertThat(issues.get(0))
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