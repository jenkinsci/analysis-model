package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

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
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(51);
    }

    /**
     * Creates the parser.
     *
     * @return the warnings parser
     */
    @Override
    protected LintParser createParser() {
        return new LintParser();
    }
}
