package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractIssueParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link MetrowerksCwLinkerParser}.
 */
class MetrowerksCwLinkerParserTest extends AbstractIssueParserTest {
    private static final String INFO_CATEGORY = "Info";
    private static final String WARNING_CATEGORY = "Warning";
    private static final String ERROR_CATEGORY = "ERROR";

    /**
     * Creates a new instance of {@link MetrowerksCwLinkerParserTest}.
     */
    protected MetrowerksCwLinkerParserTest() {
        super("MetrowerksCWLinker.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(3);
        softly.assertThat(report.get(0))
                .hasPriority(Priority.HIGH)
                .hasCategory(ERROR_CATEGORY)
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("L1822: Symbol TestFunction in file e:/work/PATH/PATH/PATH/PATH/appl_src.lib is undefined")
                .hasFileName("See Warning message");
        softly.assertThat(report.get(1))
                .hasPriority(Priority.NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("L1916: Section name TEST_SECTION is too long. Name is cut to 90 characters length")
                .hasFileName("See Warning message");
        softly.assertThat(report.get(2))
                .hasPriority(Priority.LOW)
                .hasCategory(INFO_CATEGORY)
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("L2: Message overflow, skipping WARNING messages")
                .hasFileName("See Warning message");
    }

    @Override
    protected MetrowerksCwLinkerParser createParser() {
        return new MetrowerksCwLinkerParser();
    }
}

