package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.Assert.*;

/**
 * Tests the class {@link GhsMultiParser}.
 */
public class GhsMultiParserTest extends ParserTester {
    private static final String TYPE = new GhsMultiParser().getId();

    /**
     * Parses a file with two deprecation warnings.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void parseMultiLine() throws IOException {
        Issues warnings = new GhsMultiParser().parse(openFile());

        assertEquals(3, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation = iterator.next();
        checkWarning(annotation, 37,
                "transfer of control bypasses initialization of:\n            variable \"CF_TRY_FLAG\" (declared at line 42)\n            variable \"CF_EXCEPTION_NOT_CAUGHT\" (declared at line 42)\n        CF_TRY_CHECK_EX(ex2);",
                "/maindir/tests/TestCase_0101.cpp\"", TYPE, "#546-D",
                Priority.NORMAL);
        annotation = iterator.next();
        checkWarning(annotation, 29,
                "label\n          \"CF_TRY_LABELex1\" was declared but never referenced\n     CF_TRY_EX(ex1)",
                "/maindir/tests/TestCase_0101.cpp\"", TYPE, "#177-D",
                Priority.NORMAL);
        annotation = iterator.next();
        checkWarning(annotation, 9,
                "extra\n          \";\" ignored\n  TEST_DSS( CHECK_4TH_CONFIG_DATA, 18, 142, 'F');",
                "/maindir/tests/TestCase_1601.cpp\"", TYPE, "#381-D",
                Priority.NORMAL);
    }

    @Override
    protected String getWarningsFile() {
        return "ghsmulti.txt";
    }
}

