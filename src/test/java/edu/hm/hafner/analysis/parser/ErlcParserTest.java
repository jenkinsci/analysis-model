package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractIssueParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link ErlcParser}.
 */
class ErlcParserTest extends AbstractIssueParserTest {
    /**
     * Creates a new instance of {@link AbstractIssueParserTest}.
     */
    protected ErlcParserTest() {
        super("erlc.txt");
    }


    @Override
    protected void assertThatIssuesArePresent(final Issues<Issue> issues, final SoftAssertions softly) {
        softly.assertThat(issues).hasSize(2);

        softly.assertThat(issues.get(0)).hasPriority(Priority.NORMAL)
                .hasCategory("Warning")
                .hasLineStart(125)
                .hasLineEnd(125)
                .hasMessage("variable 'Name' is unused")
                .hasFileName("./test.erl");
        softly.assertThat(issues.get(1)).hasPriority(Priority.HIGH)
                .hasCategory("Error")
                .hasLineStart(175)
                .hasLineEnd(175)
                .hasMessage("record 'Extension' undefined")
                .hasFileName("./test2.erl");

    }

    @Override
    protected ErlcParser createParser() {
        return new ErlcParser();
    }
}

