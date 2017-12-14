package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link RobocopyParser}.
 */
public class RobocopyParserTest extends AbstractParserTest {
    RobocopyParserTest() {
        super("robocopy.txt");
    }

    private static final String FILENAME = "a.log";

    @Override
    protected void assertThatIssuesArePresent(final Issues<Issue> issues, final SoftAssertions softly) {
        assertThat(issues).hasSize(3);

        softly.assertThat(issues.get(0))
                .hasPriority(Priority.NORMAL)
                .hasCategory("EXTRA File")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("b")
                .hasFileName("b");
        softly.assertThat(issues.get(1))
                .hasPriority(Priority.NORMAL)
                .hasCategory("New File")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage(FILENAME)
                .hasFileName(FILENAME);
        softly.assertThat(issues.get(2))
                .hasPriority(Priority.NORMAL)
                .hasCategory("same")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage(FILENAME)
                .hasFileName(FILENAME);
    }

    @Override
    protected AbstractParser createParser() {
        return new RobocopyParser();
    }
}

