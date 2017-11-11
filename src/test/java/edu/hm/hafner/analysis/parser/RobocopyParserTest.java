package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link RobocopyParser}.
 */
public class RobocopyParserTest extends ParserTester {
    private static final String TYPE = new RobocopyParser().getId();
    private static final String FILENAME = "a.log";

    /**
     * Parses a file with three Robocopy warnings.
     */
    @Test
    public void testWarningsParser() {
        Issues<Issue> warnings = new RobocopyParser().parse(openFile());

        assertThat(warnings).hasSize(3);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory("EXTRA File")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("b")
                    .hasFileName("b");
            softly.assertThat(warnings.get(1))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory("New File")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage(FILENAME)
                    .hasFileName(FILENAME);
            softly.assertThat(warnings.get(2))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory("same")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage(FILENAME)
                    .hasFileName(FILENAME);
        });
    }

    @Override
    protected String getWarningsFile() {
        return "robocopy.txt";
    }
}

