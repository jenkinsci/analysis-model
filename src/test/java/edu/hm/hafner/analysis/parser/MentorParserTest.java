package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link MentorParserTest}.
 *
 * @author Derrick Gibelyou
 */
class MentorParserTest extends AbstractParserTest {
    MentorParserTest() {
        super("modelsim.log");
    }

    @Override
    protected MentorParser createParser() {
        return new MentorParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(18);

        softly.assertThat(report.get(0))
                .hasLineStart(312)
                .hasMessage("Simulation ran too long.  Should finish in 200us.  Bad AXI handshaking?")
                .hasFileName("/net/test_tb.sv")
                .hasModuleName("channel_processor_tb_fir_emulation.u_channel_processor_tb")
                .hasSeverity(Severity.ERROR);

        softly.assertThat(report.get(1))
                .hasMessage("Data structure takes 20152432 bytes of memory")
                .hasModuleName("/tb_fir_emulation/u_tb")
                .hasDescription("<br>#          Process time 2.89 seconds<br>#          $stop    : /net/test_tb.sv(313)")
                .hasSeverity(Severity.WARNING_LOW);

        softly.assertThat(report.get(3))
                .hasLineStart(26)
                .hasFileName("/data/tmp/jenkins/workspace/src/comm//ping_pong_control.sv")
                .hasMessage("No condition is true in the unique/priority if/case statement.")
                .hasCategory("vsim-8315")
                .hasModuleName("/tb_g/uut/esa_ping_pong_control")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(5))
                .hasLineStart(0)
                .hasFileName("-")
                .hasMessage("Design is being optimized...")
                .hasCategory("vsim-3812")
                .hasModuleName("-")
                .hasSeverity(Severity.WARNING_LOW);

        softly.assertThat(report.get(6))
                .hasLineStart(71)
                .hasFileName("src/io/ps.v")
                .hasMessage("Undefined variable: fifo_full.")
                .hasCategory("vlog-2623")
                .hasModuleName("-")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(7))
                .hasLineStart(24)
                .hasFileName("src/mux.sv")
                .hasMessage("Variable 'sel' driven in a combinational block, may not be driven by any other process. See src/mux.sv(26).")
                .hasCategory("vlog-7033")
                .hasModuleName("-")
                .hasSeverity(Severity.ERROR);

        softly.assertThat(report.get(8))
                .hasLineStart(19)
                .hasFileName("src/data.sv")
                .hasMessage("Defaulting port 'ctl' kind to 'var' rather than 'wire' due to default compile option setting of -svinputport=relaxed.")
                .hasCategory("vlog-13314")
                .hasModuleName("-")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(9))
                .hasLineStart(2)
                .hasFileName("sim/src/ipcores/glbl.v")
                .hasMessage("empty port name in port list.")
                .hasCategory("vlog-2605")
                .hasModuleName("-")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(10))
                .hasLineStart(0)
                .hasFileName("-")
                .hasMessage("Recognized 1 FSM in module \"fsm_tb(verilog)\".")
                .hasCategory("vlog-143")
                .hasModuleName("-")
                .hasSeverity(Severity.WARNING_LOW);
    }
}
