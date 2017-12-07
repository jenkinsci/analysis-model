package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.Assert.*;

/**
 * Tests the class {@link PyLintParser}.
 */
public class PylintParserTest extends ParserTester {
    private static final String WARNING_TYPE = new PyLintParser().getId();

    /**
     * Parses a txt file, containing 3 warnings.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void pyLintTest() throws IOException {
        Issues warnings = new PyLintParser().parse(openFile());

        assertEquals(6, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        Issue warning;

        warning = iterator.next();
        checkWarning(warning,
                3,
                "Line too long (85/80)",
                "trunk/src/python/cachedhttp.py",
                WARNING_TYPE, "C", Priority.LOW);

        warning = iterator.next();
        checkWarning(warning,
                28,
                "Invalid name \"seasonCount\" (should match [a-z_][a-z0-9_]{2,30}$)",
                "trunk/src/python/tv.py",
                WARNING_TYPE, "C0103", Priority.LOW);

        warning = iterator.next();
        checkWarning(warning,
                35,
                "Missing docstring",
                "trunk/src/python/tv.py",
                WARNING_TYPE, "C0111", Priority.LOW);

        warning = iterator.next();
        checkWarning(warning,
                39,
                "Method should have \"self\" as first argument",
                "trunk/src/python/tv.py",
                WARNING_TYPE, "E0213", Priority.HIGH);

        warning = iterator.next();
        checkWarning(warning,
                5,
                "Unable to import 'deadbeef'",
                "trunk/src/python/tv.py",
                WARNING_TYPE, "F0401", Priority.HIGH);

        warning = iterator.next();
        checkWarning(warning,
                39,
                "Dangerous default value \"[]\" as argument",
                "trunk/src/python/tv.py",
                WARNING_TYPE, "W0102", Priority.NORMAL);
    }

    @Override
    protected String getWarningsFile() {
        return "pyLint.txt";
    }
}
