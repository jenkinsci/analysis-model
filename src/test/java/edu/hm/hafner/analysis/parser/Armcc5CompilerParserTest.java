package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link Armcc52CompilerParser}.
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
    protected IssueParser createParser() {
        return new Armcc5CompilerParser();
    }

    /**
     * Should recognize Armcc 5.2 warnings while parsing.
     *
     * @see <a href="https://issues.jenkins.io/browse/JENKINS-70065">Issue 70065</a>
     */
    @Test
    void issue70065() {
        var report = parse("issue70065.txt");

        assertThat(report).isEmpty();
    }
}
