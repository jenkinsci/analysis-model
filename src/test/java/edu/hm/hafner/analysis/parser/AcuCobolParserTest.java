package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.Assert.*;

/**
 * Tests the class {@link AcuCobolParser}.
 */
public class AcuCobolParserTest extends ParserTester {
    private static final String TYPE = new AcuCobolParser().getId();

    /**
     * Parses a file with 4 COBOL warnings.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void parseFile() throws IOException {
        Issues warnings = new AcuCobolParser().parse(openFile());

        assertEquals(4, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation = iterator.next();
        checkWarning(annotation,
                39,
                "Imperative statement required",
                "COPY/zzz.CPY",
                TYPE, DEFAULT_CATEGORY, Priority.NORMAL);
        annotation = iterator.next();
        checkWarning(annotation,
                111,
                "Don't run with knives",
                "C:/Documents and Settings/xxxx/COB/bbb.COB",
                TYPE, DEFAULT_CATEGORY, Priority.NORMAL);
        annotation = iterator.next();
        checkWarning(annotation,
                115,
                "Don't run with knives",
                "C:/Documents and Settings/xxxx/COB/bbb.COB",
                TYPE, DEFAULT_CATEGORY, Priority.NORMAL);
        annotation = iterator.next();
        checkWarning(annotation,
                123,
                "I'm a green banana",
                "C:/Documents and Settings/xxxx/COB/ccc.COB",
                TYPE, DEFAULT_CATEGORY, Priority.NORMAL);
    }

    @Override
    protected String getWarningsFile() {
        return "acu.txt";
    }
}

