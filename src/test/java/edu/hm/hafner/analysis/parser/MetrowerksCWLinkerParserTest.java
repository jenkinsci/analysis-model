package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link MetrowerksCWLinkerParser}.
 */
class MetrowerksCWLinkerParserTest extends AbstractParserTest {
    private static final String INFO_CATEGORY = "Info";
    private static final String WARNING_CATEGORY = "Warning";
    private static final String ERROR_CATEGORY = "ERROR";

    /**
     * Creates a new instance of {@link MetrowerksCWLinkerParserTest}.
     */
    protected MetrowerksCWLinkerParserTest() {
        super("MetrowerksCWLinker.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Issues<Issue> issues, final SoftAssertions softly) {
        softly.assertThat(issues).hasSize(3);
        softly.assertThat(issues.get(0))
                .hasPriority(Priority.HIGH)
                .hasCategory(ERROR_CATEGORY)
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("L1822: Symbol TestFunction in file e:/work/PATH/PATH/PATH/PATH/appl_src.lib is undefined")
                .hasFileName("See Warning message");
        softly.assertThat(issues.get(1))
                .hasPriority(Priority.NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("L1916: Section name TEST_SECTION is too long. Name is cut to 90 characters length")
                .hasFileName("See Warning message");
        softly.assertThat(issues.get(2))
                .hasPriority(Priority.LOW)
                .hasCategory(INFO_CATEGORY)
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("L2: Message overflow, skipping WARNING messages")
                .hasFileName("See Warning message");
    }

    @Override
    protected AbstractParser createParser() {
        return new MetrowerksCWLinkerParser();
    }
}

