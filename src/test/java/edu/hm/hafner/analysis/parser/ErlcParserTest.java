package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;
import static org.assertj.core.api.Java6Assertions.*;

/**
 * Tests the class {@link ErlcParser}.
 */
public class ErlcParserTest extends ParserTester {
    /**
     * Parses a file with two Erlc warnings.
     */
    @Test
    public void testWarningsParser() {
        Issues<Issue> issues = new ErlcParser().parse(openFile());

        assertThat(issues).hasSize(2);

        assertSoftly(softly -> {
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
        });
    }

    @Override
    protected String getWarningsFile() {
        return "erlc.txt";
    }
}

