package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;

import org.junit.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.Assert.*;

/**
 * Tests the class {@link XlcLinkerParserTest}.
 */
public class XlcLinkerParserTest extends ParserTester {
    private static final String TYPE = new XlcLinkerParser().getId();
    private static final String FILE_NAME = "-";

    /**
     * Parses a string with xlC linker error.
     *
     * @throws IOException if IO error happened
     */
    @Test
    public void testWarningsParserError1() throws IOException {
        Issues warnings = new XlcLinkerParser().parse(
                new StringReader("ld: 0711-987 Error occurred while reading file"));

        assertEquals(1, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation = iterator.next();
        checkWarning(annotation,
                0,
                "Error occurred while reading file",
                FILE_NAME,
                TYPE,
                "0711-987",
                Priority.HIGH);
    }

    /**
     * Parses a string with xlC linker error.
     *
     * @throws IOException if IO error happened
     */
    @Test
    public void testWarningsParserError2() throws IOException {
        Issues warnings = new XlcLinkerParser().parse(
                new StringReader("ld: 0711-317 ERROR: Undefined symbol: nofun()"));

        assertEquals(1, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation = iterator.next();
        checkWarning(annotation,
                0,
                "Undefined symbol: nofun()",
                FILE_NAME,
                TYPE,
                "0711-317",
                Priority.HIGH);
    }

    /**
     * Parses a string with xlC linker error.
     *
     * @throws IOException if IO error happened
     */
    @Test
    public void testWarningsParserSevereError() throws IOException {
        Issues warnings = new XlcLinkerParser().parse(
                new StringReader("ld: 0711-634 SEVERE ERROR: EXEC binder commands nested too deeply."));

        assertEquals(1, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation = iterator.next();
        checkWarning(annotation,
                0,
                "EXEC binder commands nested too deeply.",
                FILE_NAME,
                TYPE,
                "0711-634",
                Priority.HIGH);
    }

    /**
     * Parses a string with xlC linker warning.
     *
     * @throws IOException if IO error happened
     */
    @Test
    public void testWarningsParserWarning1() throws IOException {
        Issues warnings = new XlcLinkerParser().parse(
                new StringReader("ld: 0706-012 The -9 flag is not recognized."));

        assertEquals(1, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation = iterator.next();
        checkWarning(annotation,
                0,
                "The -9 flag is not recognized.",
                FILE_NAME,
                TYPE,
                "0706-012",
                Priority.LOW);
    }

    /**
     * Parses a string with xlC linker warning.
     *
     * @throws IOException if IO error happened
     */
    @Test
    public void testWarningsParserWarning2() throws IOException {
        Issues warnings = new XlcLinkerParser().parse(
                new StringReader("ld: 0711-224 WARNING: Duplicate symbol: dupe"));

        assertEquals(1, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation = iterator.next();
        checkWarning(annotation,
                0,
                "Duplicate symbol: dupe",
                FILE_NAME,
                TYPE,
                "0711-224",
                Priority.NORMAL);
    }

    /**
     * Parses a string with xlC linker informational message.
     *
     * @throws IOException if IO error happened
     */
    @Test
    public void testWarningsParserInfo1() throws IOException {
        Issues warnings = new XlcLinkerParser().parse(
                new StringReader("ld: 0711-345 Use the -bloadmap or -bnoquiet option to obtain more information."));

        assertEquals(1, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation = iterator.next();
        checkWarning(annotation,
                0,
                "Use the -bloadmap or -bnoquiet option to obtain more information.",
                FILE_NAME,
                TYPE,
                "0711-345",
                Priority.LOW);
    }

    @Override
    protected String getWarningsFile() {
        return null;
    }
}

