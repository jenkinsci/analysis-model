package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

/**
 * Tests the class {@link TnsdlParser}.
 */
class TnsdlParserTest extends AbstractParserTest {
    TnsdlParserTest() {
        super("tnsdl.txt");
    }

    @Override
    protected TnsdlParser createParser() {
        return new TnsdlParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(4);

        softly.assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(398)
                .hasLineEnd(398)
                .hasMessage("unused variable sender_pid")
                .hasFileName("tstmasgx.sdl");
        softly.assertThat(report.get(1))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasLineStart(399)
                .hasLineEnd(399)
                .hasMessage("unused variable a_sender_pid")
                .hasFileName("tstmasgx.sdl");
        softly.assertThat(report.get(2))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(3)
                .hasLineEnd(3)
                .hasMessage("Id. length is reserved in PL/M 386 intrinsics")
                .hasFileName("s_dat:dty0132c.sdt");
        softly.assertThat(report.get(3))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasLineStart(4)
                .hasLineEnd(4)
                .hasMessage("Id. length is reserved in PL/M 386 intrinsics")
                .hasFileName("s_dat:dty0132c.sdt");
    }
}

