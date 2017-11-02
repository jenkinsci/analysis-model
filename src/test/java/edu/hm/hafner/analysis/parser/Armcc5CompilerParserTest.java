package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.Assert.*;

/**
 * Tests the class {@link Armcc5CompilerParser}.
 */
public class Armcc5CompilerParserTest extends ParserTester {
    private static final String WARNING_CATEGORY = DEFAULT_CATEGORY;
    private static final String WARNING_TYPE = new Armcc5CompilerParser().getId();

    /**
     * Detects three 5 warnings.
     *
     * @throws IOException if file could not be read
     */
    @Test
    public void testWarningsParser() throws IOException {
        Issues warnings = new Armcc5CompilerParser().parse(openFile());

        assertEquals(3, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        checkWarning(iterator.next(),
                197,
                "18 - expected a \")\"",
                "../../wnArch/wnDrv/wnDrv_Usbhw.c",
                WARNING_TYPE, WARNING_CATEGORY, Priority.HIGH);
        checkWarning(iterator.next(),
                211,
                "12-D - parsing restarts here after previous syntax error",
                "../../wnArch/wnDrv/wnDrv_Usbhw.c",
                WARNING_TYPE, WARNING_CATEGORY, Priority.NORMAL);
        checkWarning(iterator.next(),
                211,
                "940-D - missing return statement at end of non-void function \"wnDrv_Usbhw_GetEPCmdStatusPtr\"",
                "../../wnArch/wnDrv/wnDrv_Usbhw.c",
                WARNING_TYPE, WARNING_CATEGORY, Priority.NORMAL);
    }

    @Override
    protected String getWarningsFile() {
        return "armcc5.txt";
    }
}
