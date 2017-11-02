package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.Assert.*;

/**
 * Tests the class {@link ErlcParser}.
 */
public class ErlcParserTest extends ParserTester {
    private static final String TYPE = new ErlcParser().getId();

    /**
     * Parses a file with two Erlc warnings.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void testWarningsParser() throws IOException {
        Issues warnings = new ErlcParser().parse(openFile());

        assertEquals(2, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation = iterator.next();
        checkWarning(annotation,
                125,
                "variable 'Name' is unused",
                "./test.erl",
                TYPE, "Warning", Priority.NORMAL);
        annotation = iterator.next();
        checkWarning(annotation,
                175,
                "record 'Extension' undefined",
                "./test2.erl",
                TYPE, "Error", Priority.HIGH);
    }

    @Override
    protected String getWarningsFile() {
        return "erlc.txt";
    }
}

