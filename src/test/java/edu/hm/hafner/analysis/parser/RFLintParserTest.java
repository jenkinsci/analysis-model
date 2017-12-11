package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link RFLintParser}. Created by traitanit on 3/27/2017 AD.
 */
public class RFLintParserTest extends ParserTester {
    private static final String WARNING_CATEGORY = "WARNING";
    private static final String ERROR_CATEGORY = "ERROR";
    private static final String IGNORE_CATEGORY = "IGNORE";

    /**
     * Parses a txt file, containing 6 warnings.
     */
    @Test
    public void rfLintTest() {
        RFLintParser sut = new RFLintParser();
        Issues<Issue> warnings = sut.parse(openFile());

        assertThat(warnings).hasSize(6);

        Iterator<Issue> iterator = warnings.iterator();

        assertSoftly(softly -> {
            softly.assertThat(iterator.next())
                    .hasLineStart(25)
                    .hasLineEnd(25)
                    .hasMessage("Line is too long (exceeds 100 characters)")
                    .hasFileName("./Login_to_web.robot")
                    .hasCategory(WARNING_CATEGORY)
                    .hasPriority(Priority.NORMAL);

            softly.assertThat(iterator.next())
                    .hasLineStart(40)
                    .hasLineEnd(40)
                    .hasMessage("No keyword documentation")
                    .hasFileName("./Login_to_web.robot")
                    .hasCategory(ERROR_CATEGORY)
                    .hasPriority(Priority.HIGH);

            softly.assertThat(iterator.next())
                    .hasLineStart(24)
                    .hasLineEnd(24)
                    .hasMessage("Line is too long (exceeds 100 characters)")
                    .hasFileName("./Merchant_Signup.robot")
                    .hasCategory(WARNING_CATEGORY)
                    .hasPriority(Priority.NORMAL);

            softly.assertThat(iterator.next())
                    .hasLineStart(378)
                    .hasLineEnd(378)
                    .hasMessage("No keyword documentation")
                    .hasFileName("./Merchant_Signup.robot")
                    .hasCategory(ERROR_CATEGORY)
                    .hasPriority(Priority.HIGH);

            softly.assertThat(iterator.next())
                    .hasLineStart(73)
                    .hasLineEnd(73)
                    .hasMessage("Too few steps (1) in keyword")
                    .hasFileName("./merchant_common_keyword.txt")
                    .hasCategory(WARNING_CATEGORY)
                    .hasPriority(Priority.NORMAL);

            softly.assertThat(iterator.next())
                    .hasLineStart(123)
                    .hasLineEnd(123)
                    .hasMessage("Ignore Error")
                    .hasFileName("./merchant_common_keyword.txt")
                    .hasCategory(IGNORE_CATEGORY)
                    .hasPriority(Priority.LOW);
        });
    }

    @Override
    protected String getWarningsFile() {
        return "rflint.txt";
    }
}
