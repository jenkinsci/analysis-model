package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractIssueParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link TaskingVxCompilerParser}.
 */
class TaskingVxCompilerParserTest extends AbstractIssueParserTest {
    private static final String INFO_CATEGORY = "Info";
    private static final String WARNING_CATEGORY = "Warning";
    private static final String ERROR_CATEGORY = "ERROR";

    TaskingVxCompilerParserTest() {
        super("tasking-vx.txt");
    }

    @Override
    protected TaskingVxCompilerParser createParser() {
        return new TaskingVxCompilerParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report warnings, final SoftAssertions softly) {
        softly.assertThat(warnings).hasSize(8);

        softly.assertThat(warnings.get(0))
                .hasPriority(Priority.NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(8796)
                .hasLineEnd(8796)
                .hasMessage("condition is always true")
                .hasFileName("C:/Projects/a/b/c/d/e/f/g/h/i/src/StbM.c");
        softly.assertThat(warnings.get(1))
                .hasPriority(Priority.LOW)
                .hasCategory(INFO_CATEGORY)
                .hasLineStart(50)
                .hasLineEnd(50)
                .hasMessage("previous definition of macro \"TS_RELOCATABLE_CFG_ENABLE\"")
                .hasFileName("C:/Projects/a/b/c/d/e/f/g/TcpIp_Int.h");
        softly.assertThat(warnings.get(2))
                .hasPriority(Priority.NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(36)
                .hasLineEnd(36)
                .hasMessage("macro \"TS_RELOCATABLE_CFG_ENABLE\" redefined")
                .hasFileName("C:/Projects/a/b/c/d/e/f/g/h/include/ComM_Types_Int.h");
        softly.assertThat(warnings.get(3))
                .hasPriority(Priority.NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(2221)
                .hasLineEnd(2221)
                .hasMessage("conversion of integer to pointer at argument #3")
                .hasFileName("C:/Projects/a/b/c/d/e/f/g/h/src/SoAd.c");
        softly.assertThat(warnings.get(4))
                .hasPriority(Priority.NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(860)
                .hasLineEnd(860)
                .hasMessage("unused static function \"TcpIp_Tcp_checkRemoteAddr\"")
                .hasFileName("C:/Projects/a/b/c/d/e/f/g/TcpIp_Tcp.c");
        softly.assertThat(warnings.get(5))
                .hasPriority(Priority.HIGH)
                .hasCategory(ERROR_CATEGORY)
                .hasLineStart(380)
                .hasLineEnd(380)
                .hasMessage("syntax error - token \";\" inserted before \"{\"")
                .hasFileName("C:/Projects/a/b/c/DmaLib.h");
        softly.assertThat(warnings.get(6))
                .hasPriority(Priority.LOW)
                .hasCategory(INFO_CATEGORY)
                .hasLineStart(42)
                .hasLineEnd(42)
                .hasMessage("start of current function definition")
                .hasFileName("BswM_UserCallouts.c");
    }
}

