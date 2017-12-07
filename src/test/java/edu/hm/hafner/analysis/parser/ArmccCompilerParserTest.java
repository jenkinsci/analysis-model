package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.Assert.*;

/**
 * Tests the class {@link ArmccCompilerParser}.
 */
public class ArmccCompilerParserTest extends ParserTester {
    private static final String WARNING_CATEGORY = DEFAULT_CATEGORY;
    private static final String WARNING_TYPE = new ArmccCompilerParser().getId();

    /**
     * Detects three ARMCC warnings.
     *
     * @throws IOException if file could not be read
     */
    @Test
    public void testWarningsParser() throws IOException {
        Issues warnings = new ArmccCompilerParser().parse(openFile());

        assertEquals(3, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        checkWarning(iterator.next(),
                21,
                "5 - cannot open source input file \"somefile.h\": No such file or directory",
                "/home/test/main.cpp",
                WARNING_TYPE, WARNING_CATEGORY, Priority.HIGH);
        checkWarning(iterator.next(),
                23,
                "5 - cannot open source input file \"somefile.h\": No such file or directory",
                "C:/home/test/main.cpp",
                WARNING_TYPE, WARNING_CATEGORY, Priority.HIGH);
        checkWarning(iterator.next(),
                25,
                "550 - something bad happened here",
                "/home/test/main.cpp",
                WARNING_TYPE, WARNING_CATEGORY, Priority.NORMAL);
    }

    @Override
    protected String getWarningsFile() {
        return "armcc.txt";
    }
}
