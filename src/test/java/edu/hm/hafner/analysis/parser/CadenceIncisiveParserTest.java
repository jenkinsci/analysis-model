package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractIssueParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link CadenceIncisiveParser}.
 *
 * @author Andrew 'Necromant' Andrianov
 */
class CadenceIncisiveParserTest extends AbstractIssueParserTest {
    CadenceIncisiveParserTest() {
        super("CadenceIncisive.txt");
    }

    @Override
    protected CadenceIncisiveParser createParser() {
        return new CadenceIncisiveParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(3);

        softly.assertThat(report.get(0))
                .hasLineStart(0)
                .hasMessage("Resolved design unit 'dummyram' at 'u_dummyrams' to 'dummysoc.dummyram:v' through a global search of all libraries.")
                .hasFileName("/NotFileRelated")
                .hasCategory("Warning (ncelab): CUSRCH")
                .hasPriority(Priority.LOW);

        softly.assertThat(report.get(1))
                .hasLineStart(313)
                .hasMessage("10 output ports were not connected")
                .hasFileName("/tmp/build-dir/../verilog/placeholder.v")
                .hasCategory("Warning (ncelab): CUVWSP")
                .hasPriority(Priority.NORMAL);

        softly.assertThat(report.get(2))
                .hasLineStart(310)
                .hasMessage("component instance is not fully bound (some.long:placeholder:blah:r1)")
                .hasFileName("/tmp/build-dir/freaking_gbit_astral.vhd")
                .hasCategory("Warning (ncelab): CUNOTB")
                .hasPriority(Priority.NORMAL);
    }
}
