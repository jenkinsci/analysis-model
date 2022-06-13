package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

/**
 * Tests the class {@link BluePearlParser}.
 *
 * @author Simon Matthews
 */
class BluePearlParserTest extends AbstractParserTest {
    BluePearlParserTest() {
        super("bluepearl.log");
    }

    @Override
    protected BluePearlParser createParser() {
        return new BluePearlParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(7);

        softly.assertThat(report.get(0))
                .hasLineStart(51)
                .hasMessage(
                        "actual bit length 1 differs from formal bit length 4 for port 'output_we'")
                .hasFileName("top_adapter_bram.v")
                .hasSeverity(Severity.WARNING_LOW);

        softly.assertThat(report.get(1))
                .hasLineStart(32)
                .hasMessage(
                        "actual bit length 1 differs from formal bit length 4 for port 'bram0_addr'")
                .hasFileName("top_adapter_bram.v")
                .hasSeverity(Severity.WARNING_LOW);

        softly.assertThat(report.get(2))
                .hasLineStart(42)
                .hasMessage(
                        "actual bit length 1 differs from formal bit length 4 for port 'input_addr'")
                .hasFileName("top_adapter_bram.v")
                .hasSeverity(Severity.WARNING_LOW);

        softly.assertThat(report.get(3))
                .hasLineStart(12)
                .hasMessage(
                        "Module: 'top_adapter_bram' Signal 'top_adapter_bram.bram1_dout' is used but has no driver(s).")
                .hasFileName("top_adapter_bram.v")
                .hasSeverity(Severity.WARNING_LOW);

        softly.assertThat(report.get(4))
                .hasLineStart(42)
                .hasMessage(
                        "actual bit length 1 differs from formal bit length 4 for port 'input_addr'")
                .hasFileName("C:/lajfakjfka/top_adapter_bram.v")
                .hasSeverity(Severity.WARNING_LOW);

        softly.assertThat(report.get(5))
                .hasLineStart(42)
                .hasMessage(
                        "actual bit length 1 differs from formal bit length 4 for port 'input_addr'")
                .hasFileName("../lajfakjfka/top_adapter_bram.v")
                .hasSeverity(Severity.WARNING_LOW);

        softly.assertThat(report.get(6))
                .hasLineStart(22)
                .hasMessage(
                        "Module: 'top_adapter_bram' Signal 'top_adapter_bram.output_dout' is used but has no driver(s).")
                .hasFileName("top_adapter_bram.v")
                .hasSeverity(Severity.WARNING_LOW);
    }
}
