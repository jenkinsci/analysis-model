package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link MetrowerksCWLinkerParser}.
 */
public class MetrowerksCWLinkerParserTest extends ParserTester {
    private static final String INFO_CATEGORY = "Info";
    private static final String WARNING_CATEGORY = "Warning";
    private static final String ERROR_CATEGORY = "ERROR";

    /**
     * Parses a file with three MetrowerksCWLinker warnings.
     */
    @Test
    public void testWarningsParser() {
        Issues<Issue> warnings = new MetrowerksCWLinkerParser().parse(openFile());

        assertThat(warnings).hasSize(3);
        
        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasPriority(Priority.HIGH)
                    .hasCategory(ERROR_CATEGORY)
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("L1822: Symbol TestFunction in file e:/work/PATH/PATH/PATH/PATH/appl_src.lib is undefined")
                    .hasFileName("See Warning message");
            softly.assertThat(warnings.get(1))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(WARNING_CATEGORY)
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("L1916: Section name TEST_SECTION is too long. Name is cut to 90 characters length")
                    .hasFileName("See Warning message");
            softly.assertThat(warnings.get(2))
                    .hasPriority(Priority.LOW)
                    .hasCategory(INFO_CATEGORY)
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("L2: Message overflow, skipping WARNING messages")
                    .hasFileName("See Warning message");
        });
    }

    @Override
    protected String getWarningsFile() {
        return "MetrowerksCWLinker.txt";
    }
}

