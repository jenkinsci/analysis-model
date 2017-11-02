package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.Assert.*;

/**
 * Tests the class {@link GoLintParser}.
 */
public class GoVetParserTest extends ParserTester {

    /**
     * Parses a file with multiple go vet warnings
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void testWarningsParser() throws IOException {
        Issues warnings = new GoVetParser().parse(openFile());

        assertEquals(2, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation = iterator.next();


        checkWarning(annotation, 46, "missing argument for Fatalf(\"%#v\"): format reads arg 2, have only 1 args", "ui_colored_test.go", DEFAULT_CATEGORY,
                Priority.NORMAL);
        annotation = iterator.next();

        checkWarning(annotation, 59, "missing argument for Fatalf(\"%#v\"): format reads arg 2, have only 1 args", "ui_colored_test.go", DEFAULT_CATEGORY,
                Priority.NORMAL);

    }

    @Override
    protected String getWarningsFile() {
        return "govet.txt";
    }
}
