package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.Assert.*;

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

        assertEquals(2, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation = iterator.next();
        checkWarning(annotation,
                150,
                "function `unsigned configureRealCh(unsigned)' was declared static, but was not defined",
                "/nfs/autofs/nett/nessie6/dailies/Monday/src/n6/heidrun/dsp/Modules/LocalChAdmin.c",
                new CoolfluxChessccParser().getId(), DEFAULT_CATEGORY, Priority.HIGH);

    }


    @Override
    protected String getWarningsFile() {
        return "coolfluxchesscc.txt";
    }
}

