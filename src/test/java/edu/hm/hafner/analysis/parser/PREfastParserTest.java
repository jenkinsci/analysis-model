package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.Assert.*;

/**
 * Tests the class {@link PREfastParser}.
 *
 * @author Charles Chan
 */
public class PREfastParserTest extends ParserTester {
    private static final String TYPE = new PREfastParser().getId();

    /**
     * Tests the Puppet-Lint parsing.
     *
     * @throws IOException in case of an error
     */
    @Test
    public void testParse() throws IOException {
        Issues results = new PREfastParser().parse(openFile());

        assertEquals(11, results.size());

        Iterator<Issue> iterator = results.iterator();

        Issue annotation = iterator.next();
        checkWarning(annotation,
                102, "The Drivers module has inferred that the current function is a DRIVER_INITIALIZE function:  This is informational only. No problem has been detected.",
                "sys.c", TYPE, "28101", Priority.NORMAL);

        annotation = iterator.next();
        checkWarning(annotation,
                116, "(PFD)Leaking memory 'device'.",
                "sys.c", TYPE, "6014", Priority.NORMAL);

        annotation = iterator.next();
        checkWarning(annotation,
                137, "The function being assigned or passed should be a DRIVER_UNLOAD function:  Add the declaration 'DRIVER_UNLOAD OnUnload;' before the current first declaration of OnUnload.",
                "sys.c", TYPE, "28155", Priority.NORMAL);
    }

    @Override
    protected String getWarningsFile() {
        return "PREfast.xml";
    }
}
