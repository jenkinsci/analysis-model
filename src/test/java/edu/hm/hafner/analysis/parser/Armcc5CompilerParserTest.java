//Sarah Hofstätter
package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.AnalysisSoftAssertions;

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
        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        Issues warnings = new Armcc5CompilerParser().parse(openFile());
        Iterator<Issue> iterator = warnings.iterator();
        Issue warning2 = iterator.next();
        Issue warning1 = iterator.next();
        Issue warning = iterator.next();

        softly.assertThat(warnings).hasSize(3);
        softly.assertThat(warning2).hasPriority(Priority.HIGH)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(197)
                .hasLineEnd(197)
                .hasMessage("18 - expected a \")\"")
                .hasFileName("../../wnArch/wnDrv/wnDrv_Usbhw.c")
                .hasType(WARNING_TYPE);
        softly.assertThat(warning1).hasPriority(Priority.NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(211)
                .hasLineEnd(211)
                .hasMessage("12-D - parsing restarts here after previous syntax error")
                .hasFileName("../../wnArch/wnDrv/wnDrv_Usbhw.c")
                .hasType(WARNING_TYPE);
        softly.assertThat(warning).hasPriority(Priority.NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(211)
                .hasLineEnd(211)
                .hasMessage("940-D - missing return statement at end of non-void function \"wnDrv_Usbhw_GetEPCmdStatusPtr\"")
                .hasFileName("../../wnArch/wnDrv/wnDrv_Usbhw.c")
                .hasType(WARNING_TYPE);
    }

    @Override
    protected String getWarningsFile() {
        return "armcc5.txt";
    }
}
