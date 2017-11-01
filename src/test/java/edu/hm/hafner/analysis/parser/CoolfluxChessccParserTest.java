package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;


/**
 * Tests the class {@link CoolfluxChessccParserTest}.
 */
public class CoolfluxChessccParserTest extends ParserTester {
    /**
     * Parses a file with two warnings.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void testWarningsParser() throws IOException {
        Issues warnings = new CoolfluxChessccParser().parse(openFile());
        Iterator<Issue> iterator = warnings.iterator();
        int expectedSize = 2;

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(warnings).hasSize(expectedSize);
        softly.assertThat(iterator.next()).hasLineStart(150)
                .hasLineEnd(150)
                .hasMessage("function `unsigned configureRealCh(unsigned)' was declared static, but was not defined")
                .hasFileName("/nfs/autofs/nett/nessie6/dailies/Monday/src/n6/heidrun/dsp/Modules/LocalChAdmin.c")
                .hasType(new CoolfluxChessccParser().getId())
                .hasCategory(DEFAULT_CATEGORY)
                .hasPriority(Priority.HIGH);
        softly.assertAll();
    }


    @Override
    protected String getWarningsFile() {
        return "coolfluxchesscc.txt";
    }
}

