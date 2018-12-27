package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link ErrorProneParser}.
 *
 * @author Ullrich Hafner
 */
class ErrorProneParserTest extends AbstractParserTest {
    ErrorProneParserTest() {
        super("errorprone-maven.log");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(9);
    }

    @Override
    protected IssueParser createParser() {
        return new JavacParser();
    }
}
