package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link LintParser}.
 *
 * @author Ullrich Hafner
 */
class CssLintParserTest extends AbstractParserTest {
    /**
     * Creates a new instance of {@link CssLintParserTest}.
     */
    protected CssLintParserTest() {
        super("jslint/csslint.xml");
    }

    @Override
    protected void assertThatIssuesArePresent(final Issues<Issue> issues, final SoftAssertions softly) {
        softly.assertThat(issues).hasSize(51);
    }

    /**
     * Creates the parser.
     *
     * @return the warnings parser
     */
    @Override
    protected AbstractParser createParser() {
        return new LintParser();
    }
}
