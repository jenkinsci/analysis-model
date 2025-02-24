package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link Armcc5CompilerParser}.
 */
class Armcc5CompilerParserTest extends AbstractParserTest {
    private static final String WARNING_CATEGORY = DEFAULT_CATEGORY;

    /**
     * Creates a new instance of {@link Armcc5CompilerParserTest}.
     */
    protected Armcc5CompilerParserTest() {
        super("armcc5.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(3);

        softly.assertThat(report.get(0)).hasSeverity(Severity.ERROR)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(197)
                .hasLineEnd(197)
                .hasMessage("18 - expected a \")\"")
                .hasFileName("../../wnArch/wnDrv/wnDrv_Usbhw.c");
        softly.assertThat(report.get(1)).hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(211)
                .hasLineEnd(211)
                .hasMessage("12-D - parsing restarts here after previous syntax error")
                .hasFileName("../../wnArch/wnDrv/wnDrv_Usbhw.c");
        softly.assertThat(report.get(2)).hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(211)
                .hasLineEnd(211)
                .hasMessage(
                        "940-D - missing return statement at end of non-void function \"wnDrv_Usbhw_GetEPCmdStatusPtr\"")
                .hasFileName("../../wnArch/wnDrv/wnDrv_Usbhw.c");
    }

    @Override
    protected Armcc5CompilerParser createParser() {
        return new Armcc5CompilerParser();
    }

    /**
     * Should recognize Armcc5 warnings while parsing.
     *
     * @see <a href="https://issues.jenkins.io/browse/JENKINS-70065">Issue 70065</a>
     */
    @Test
    void issue70065() {
        var report = parse("issue70065.txt");

        assertThat(report).hasSize(2);

        assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(354)
                .hasLineEnd(354)
                .hasFileName("sw/dbg/cfg/dbg_trace_acfg.h")
                .hasMessage("1-D - last line of file ends without a newline")
                .hasColumnStart(0)
                .hasColumnEnd(0);

        assertThat(report.get(1))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(45)
                .hasLineEnd(45)
                .hasFileName("out/include/common/dimming_ctrl.h")
                .hasMessage("1-D - last line of file ends without a newline")
                .hasColumnStart(0)
                .hasColumnEnd(0);
    }
}
