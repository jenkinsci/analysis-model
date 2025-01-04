package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

class SimulinkCheckParserTest extends AbstractParserTest {
    SimulinkCheckParserTest() {
        super("Simulink.html");
    }

    @Override
    protected SimulinkCheckParser createParser() {
        return new SimulinkCheckParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(12);
        softly.assertThat(report.get(1))
                .hasCategory("Warning")
                .hasDescription("Identify Environment Controller blocks to be replaced with Variant Source blocks")
                .hasSeverity(Severity.WARNING_NORMAL);
        softly.assertThat(report.get(2))
                .hasModuleName("SW01-181.NamesToAvoid");
        softly.assertThat(report.get(0))
                .hasModuleName("SW01-125.IntegerWordLengths");

        softly.assertThat(report.get(6))
                .hasCategory("Not Run")
                .hasDescription("Check for equality and inequality operations on floating-point values")
                .hasSeverity(Severity.WARNING_HIGH);
        softly.assertThat(report.get(8))
                .hasModuleName("SW01-181.UseOfCase");
        softly.assertThat(report.get(7))
                .hasModuleName("SW01-441.checkParameterUnits");

        softly.assertThat(report.get(3))
                .hasCategory("Failed")
                .hasDescription("Check Data Store Memory blocks")
                .hasSeverity(Severity.ERROR);
        softly.assertThat(report.get(4))
                .hasModuleName("SW02-142.SFMessagesCheck");
        softly.assertThat(report.get(5))
                .hasModuleName("SW01-181.Comments");

        softly.assertThat(report.get(10))
                .hasCategory("Incomplete")
                .hasDescription("Check if model uses custom code")
                .hasSeverity(Severity.WARNING_LOW);
        softly.assertThat(report.get(11))
                .hasModuleName("SW01-181.NestedComments");
        softly.assertThat(report.get(9))
                .hasModuleName("SW02-430.CompareFloatEquality");

        for (int i = 0; i < report.size(); i++) {
            softly.assertThat(report.get(i)).hasFileName("sldemo_mdladv.slx");
        }
    }
}
