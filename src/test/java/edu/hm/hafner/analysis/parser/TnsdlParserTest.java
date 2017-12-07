package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.Assert.*;

/**
 * Tests the class {@link TnsdlParser}.
 */
public class TnsdlParserTest extends ParserTester {
    private static final String TYPE = new TnsdlParser().getId();
    private static final String WARNING_CATEGORY = TnsdlParser.WARNING_CATEGORY;

    /**
     * Parses a file with four tnsdl warnings.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void testWarningsParser() throws IOException {
        Issues warnings = new TnsdlParser().parse(openFile());

        assertEquals(4, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation = iterator.next();
        checkWarning(annotation,
                398,
                "unused variable sender_pid",
                "tstmasgx.sdl",
                TYPE, WARNING_CATEGORY, Priority.NORMAL);
        annotation = iterator.next();

        checkWarning(annotation,
                399,
                "unused variable a_sender_pid",
                "tstmasgx.sdl",
                TYPE, WARNING_CATEGORY, Priority.HIGH);
        annotation = iterator.next();

        checkWarning(annotation,
                3,
                "Id. length is reserved in PL/M 386 intrinsics",
                "s_dat:dty0132c.sdt",
                TYPE, WARNING_CATEGORY, Priority.NORMAL);
        annotation = iterator.next();

        checkWarning(annotation,
                4,
                "Id. length is reserved in PL/M 386 intrinsics",
                "s_dat:dty0132c.sdt",
                TYPE, WARNING_CATEGORY, Priority.HIGH);

    }

    @Override
    protected String getWarningsFile() {
        return "tnsdl.txt";
    }

}

