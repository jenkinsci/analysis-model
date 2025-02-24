package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link Armcc5CompilerParser}.
 */
class Armcc52CompilerParserTest extends AbstractParserTest {
    private static final String WARNING_CATEGORY = DEFAULT_CATEGORY;

    /**
     * Creates a new instance of {@link Armcc52CompilerParserTest}.
     */
    protected Armcc52CompilerParserTest() {
        super("issue70065.txt");
    }

    @Override
    protected IssueParser createParser() {
        return new Armcc52CompilerParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(2);

        softly.assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(354)
                .hasLineEnd(354)
                .hasFileName("sw/dbg/cfg/dbg_trace_acfg.h")
                .hasMessage("1-D - last line of file ends without a newline")
                .hasColumnStart(0)
                .hasColumnEnd(0);

        softly.assertThat(report.get(1))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(45)
                .hasLineEnd(45)
                .hasFileName("out/include/common/dimming_ctrl.h")
                .hasMessage("1-D - last line of file ends without a newline")
                .hasColumnStart(0)
                .hasColumnEnd(0);
    }

    /**
     * Should not recognize Armcc < 5.2 warnings while parsing.
     *
     * @see <a href="https://issues.jenkins.io/browse/JENKINS-70065">Issue 70065</a>
     */
    @Test
    void issue70065() {
        var report = parse("armcc5.txt");

        assertThat(report).isEmpty();
    }
}
