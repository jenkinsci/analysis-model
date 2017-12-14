package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;
import static org.assertj.core.api.Java6Assertions.*;

/**
 * Tests the class {@link ErlcParser}.
 */
public class ErlcParserTest extends AbstractParserTest {
    /**
     * Creates a new instance of {@link AbstractParserTest}.
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
    protected AbstractParser createParser() {
        return new ErlcParser();
    }
}

