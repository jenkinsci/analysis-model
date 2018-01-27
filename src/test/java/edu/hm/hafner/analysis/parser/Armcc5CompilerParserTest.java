package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link Armcc5CompilerParser}.
 */
class Armcc5CompilerParserTest extends AbstractParserTest {
    private static final String WARNING_CATEGORY = DEFAULT_CATEGORY;

    /**
     * Creates a new instance of {@link AbstractParserTest}.
     *
     */
    protected Armcc5CompilerParserTest() {
        super("armcc5.txt");
    }


    @Override
    protected void assertThatIssuesArePresent(final Issues<Issue> issues, final SoftAssertions softly) {

        softly.assertThat(issues).hasSize(3);

        softly.assertThat(issues.get(0)).hasPriority(Priority.HIGH)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(197)
                .hasLineEnd(197)
                .hasMessage("18 - expected a \")\"")
                .hasFileName("../../wnArch/wnDrv/wnDrv_Usbhw.c");
        softly.assertThat(issues.get(1)).hasPriority(Priority.NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(211)
                .hasLineEnd(211)
                .hasMessage("12-D - parsing restarts here after previous syntax error")
                .hasFileName("../../wnArch/wnDrv/wnDrv_Usbhw.c");
        softly.assertThat(issues.get(2)).hasPriority(Priority.NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(211)
                .hasLineEnd(211)
                .hasMessage("940-D - missing return statement at end of non-void function \"wnDrv_Usbhw_GetEPCmdStatusPtr\"")
                .hasFileName("../../wnArch/wnDrv/wnDrv_Usbhw.c");
    }

    @Override
    protected AbstractParser createParser() {
        return new Armcc5CompilerParser();
    }
}
