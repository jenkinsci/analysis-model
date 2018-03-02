package edu.hm.hafner.analysis.parser.violations;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link ErrorProneAdapter}.
 *
 * @author Ullrich Hafner
 */
class ErrorProneAdapterTest extends AbstractParserTest<Issue> {
    ErrorProneAdapterTest() {
        super("error-prone.log");
    }

    @Override
    protected void assertThatIssuesArePresent(final Issues<Issue> issues, final SoftAssertions softly) {
        softly.assertThat(issues).hasSize(5);
        softly.assertThat(issues.get(0))
                .hasMessage("Prefer Splitter to String.split\n"
                        + "\n"
                        + "for (final String part : link.split(\",\")) "
                        + "{^(see http://errorprone.info/bugpattern/StringSplitter)Did you mean "
                        + "'for (final String part : Splitter.on(\",\").split(link)) {'?")
                .hasFileName("/home/bjerre/workspace/git-changelog/git-changelog-lib/src/main/java/se/bjurr/gitchangelog/internal/integrations/github/GitHubHelper.java")
                .hasType("StringSplitter")
                .hasLineStart(51)
                .hasPriority(Priority.NORMAL);
    }

    @Override
    protected AbstractParser<Issue> createParser() {
        return new ErrorProneAdapter();
    }
}