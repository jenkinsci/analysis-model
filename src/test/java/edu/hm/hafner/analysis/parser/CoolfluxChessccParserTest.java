package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link CoolfluxChessccParserTest}.
 */
public class CoolfluxChessccParserTest extends ParserTester {
    /**
     * Parses a file with two warnings.
     */
    @Test
    public void testWarningsParser() {
        Issues<Issue> warnings = new CoolfluxChessccParser().parse(openFile());

        assertThat(warnings).hasSize(1).hasDuplicatesSize(1);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasLineStart(150)
                    .hasLineEnd(150)
                    .hasMessage(
                            "function `unsigned configureRealCh(unsigned)' was declared static, but was not defined")
                    .hasFileName("/nfs/autofs/nett/nessie6/dailies/Monday/src/n6/heidrun/dsp/Modules/LocalChAdmin.c")
                    .hasCategory(DEFAULT_CATEGORY)
                    .hasPriority(Priority.HIGH);
        });
    }

    @Override
    protected String getWarningsFile() {
        return "coolfluxchesscc.txt";
    }
}

