package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.Assert.*;

/**
 * Tests the class {@link Pep8Parser}.
 *
 * @author Marvin Schütz
 */
public class Pep8ParserTest extends ParserTester {
    private static final String WARNING_TYPE = new Pep8Parser().getId();

    /**
     * Parses a file with W and E warnings.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void testParseSimpleAndComplexMessage() throws IOException {
        Pep8Parser parser = new Pep8Parser();

        Issues warnings = parser.parse(openFile());

        assertEquals(8, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        Issue warning = iterator.next();

        checkWarning(warning, 69, 11, "multiple imports on one line", "optparse.py",
                WARNING_TYPE, "E401", Priority.NORMAL);
        warning = iterator.next();

        checkWarning(warning, 77, 1, "expected 2 blank lines, found 1", "optparse.py",
                WARNING_TYPE, "E302", Priority.NORMAL);
        warning = iterator.next();

        checkWarning(warning, 88, 5, "expected 1 blank line, found 0", "optparse.py",
                WARNING_TYPE, "E301", Priority.NORMAL);
        warning = iterator.next();

        checkWarning(warning, 222, 34, "deprecated form of raising exception", "optparse.py",
                WARNING_TYPE, "W602", Priority.LOW);
        warning = iterator.next();

        checkWarning(warning, 347, 31, "whitespace before '('", "optparse.py",
                WARNING_TYPE, "E211", Priority.NORMAL);
        warning = iterator.next();

        checkWarning(warning, 357, 17, "whitespace after '{'", "optparse.py",
                WARNING_TYPE, "E201", Priority.NORMAL);
        warning = iterator.next();

        checkWarning(warning, 472, 29, "multiple spaces before operator", "optparse.py",
                WARNING_TYPE, "E221", Priority.NORMAL);
        warning = iterator.next();

        checkWarning(warning, 544, 21, ".has_key() is deprecated, use 'in'", "optparse.py",
                WARNING_TYPE, "W601", Priority.LOW);
    }

    @Override
    protected String getWarningsFile() {
        return "pep8Test.txt";
    }
}
