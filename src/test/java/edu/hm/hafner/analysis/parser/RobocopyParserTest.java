package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.Assert.*;

/**
 * Tests the class {@link RobocopyParser}.
 */
public class RobocopyParserTest extends ParserTester {
    private static final String TYPE = new RobocopyParser().getId();
    private static final String FILENAME = "a.log";

    /**
     * Parses a file with three Robocopy warnings.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void testWarningsParser() throws IOException {
        Issues warnings = new RobocopyParser().parse(openFile());

        assertEquals(3, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation = iterator.next();
        checkWarning(annotation,
                0,
                "b",
                "b",
                TYPE, "EXTRA File", Priority.NORMAL);
        annotation = iterator.next();
        checkWarning(annotation,
                0,
                FILENAME,
                FILENAME,
                TYPE, "New File", Priority.NORMAL);
        annotation = iterator.next();
        checkWarning(annotation,
                0,
                FILENAME,
                FILENAME,
                TYPE, "same", Priority.NORMAL);
    }

    @Override
    protected String getWarningsFile() {
        return "robocopy.txt";
    }
}

