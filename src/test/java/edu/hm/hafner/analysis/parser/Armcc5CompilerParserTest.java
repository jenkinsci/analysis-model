package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link Armcc5CompilerParser}.
 */
public class Armcc5CompilerParserTest extends ParserTester {
    private static final String WARNING_CATEGORY = DEFAULT_CATEGORY;

    /**
     * Detects three 5 warnings.
     */
    @Test
    public void testWarningsParser() {
        Issues<Issue> warnings = new Armcc5CompilerParser().parse(openFile());

        assertThat(warnings).hasSize(3);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0)).hasPriority(Priority.HIGH)
                    .hasCategory(WARNING_CATEGORY)
                    .hasLineStart(197)
                    .hasLineEnd(197)
                    .hasMessage("18 - expected a \")\"")
                    .hasFileName("../../wnArch/wnDrv/wnDrv_Usbhw.c");
            softly.assertThat(warnings.get(1)).hasPriority(Priority.NORMAL)
                    .hasCategory(WARNING_CATEGORY)
                    .hasLineStart(211)
                    .hasLineEnd(211)
                    .hasMessage("12-D - parsing restarts here after previous syntax error")
                    .hasFileName("../../wnArch/wnDrv/wnDrv_Usbhw.c");
            softly.assertThat(warnings.get(2)).hasPriority(Priority.NORMAL)
                    .hasCategory(WARNING_CATEGORY)
                    .hasLineStart(211)
                    .hasLineEnd(211)
                    .hasMessage("940-D - missing return statement at end of non-void function \"wnDrv_Usbhw_GetEPCmdStatusPtr\"")
                    .hasFileName("../../wnArch/wnDrv/wnDrv_Usbhw.c");
        });
    }

    @Override
    protected String getWarningsFile() {
        return "armcc5.txt";
    }
}
