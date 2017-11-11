//Sarah Hofstätter
package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

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
        SoftAssertions softly = new SoftAssertions();
        Issues warnings = new Armcc5CompilerParser().parse(openFile());

        softly.assertThat(warnings).hasSize(3);
        softly.assertThat(warnings.get(0)).hasPriority(Priority.HIGH)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(197)
                .hasLineEnd(197)
                .hasMessage("18 - expected a \")\"")
                .hasFileName("../../wnArch/wnDrv/wnDrv_Usbhw.c")
                .hasType(WARNING_TYPE);
        softly.assertThat(warnings.get(1)).hasPriority(Priority.NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(211)
                .hasLineEnd(211)
                .hasMessage("12-D - parsing restarts here after previous syntax error")
                .hasFileName("../../wnArch/wnDrv/wnDrv_Usbhw.c")
                .hasType(WARNING_TYPE);
        softly.assertThat(warnings.get(2)).hasPriority(Priority.NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(211)
                .hasLineEnd(211)
                .hasMessage("940-D - missing return statement at end of non-void function \"wnDrv_Usbhw_GetEPCmdStatusPtr\"")
                .hasFileName("../../wnArch/wnDrv/wnDrv_Usbhw.c")
                .hasType(WARNING_TYPE);
        softly.assertAll();
    }

    @Override
    protected String getWarningsFile() {
        return "armcc5.txt";
    }
}
