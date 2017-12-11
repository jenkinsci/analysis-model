package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import static edu.hm.hafner.analysis.parser.ParserTester.*;

/**
 * Tests the class {@link SbtScalacParser}.
 */
class SbtScalacParserTest extends AbstractParserTest {
    SbtScalacParserTest() {
        super("sbtScalac.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Issues<Issue> issues, final SoftAssertions softly) {
        softly.assertThat(issues).hasSize(4);

        softly.assertThat(issues.get(0))
                .hasPriority(Priority.NORMAL)
                .hasCategory(DEFAULT_CATEGORY)
                .hasLineStart(111)
                .hasLineEnd(111)
                .hasMessage(
                        "method stop in class Thread is deprecated: see corresponding Javadoc for more information.")
                .hasFileName("/home/user/.jenkins/jobs/job/workspace/path/SomeFile.scala");
        softly.assertThat(issues.get(1))
                .hasPriority(Priority.HIGH)
                .hasCategory(DEFAULT_CATEGORY)
                .hasLineStart(9)
                .hasLineEnd(9)
                .hasMessage("';' expected but identifier found.")
                .hasFileName("/home/user/.jenkins/jobs/job/workspace/another/path/SomeFile.scala");
        softly.assertThat(issues.get(2))
                .hasPriority(Priority.NORMAL)
                .hasCategory(DEFAULT_CATEGORY)
                .hasLineStart(4)
                .hasLineEnd(4)
                .hasMessage("implicit numeric widening")
                .hasFileName("/home/user/.jenkins/jobs/job/workspace/Main.scala");
        softly.assertThat(issues.get(3))
                .hasPriority(Priority.HIGH)
                .hasCategory(DEFAULT_CATEGORY)
                .hasLineStart(5)
                .hasLineEnd(5)
                .hasMessage("Invalid literal number")
                .hasFileName("/home/user/.jenkins/jobs/job/workspace/Main.scala");
    }

    @Override
    protected AbstractParser createParser() {
        return new SbtScalacParser();
    }
}
