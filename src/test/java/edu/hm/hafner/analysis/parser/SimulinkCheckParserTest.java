package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

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
        softly.assertThat(report).hasSize(1494);

        softly.assertThat(report.get(0))
                .hasCategory("Warning")
                .hasDescription("Define the names to avoid")
                .hasSeverity(Severity.WARNING_NORMAL);
        softly.assertThat(report.get(1))
                .hasModuleName("SW01-1996.UseOfCase");
        softly.assertThat(report.get(68))
                .hasModuleName("SW01-125.runVisualizationChecks");

        softly.assertThat(report.get(500))
                .hasCategory("Not Run")
                .hasDescription("Check model mask parameters")
                .hasSeverity(Severity.WARNING_HIGH);
        softly.assertThat(report.get(333))
                .hasModuleName("SW01-181.LUTRangeCheckCode");
        softly.assertThat(report.get(439))
                .hasModuleName("SW01-441.runClockChecks");

        softly.assertThat(report.get(302))
                .hasCategory("Failed")
                .hasDescription("Check model for Stateflow messages")
                .hasSeverity(Severity.ERROR);
        softly.assertThat(report.get(305))
                .hasModuleName("SW02-142.MaskParamInfCheck");
        softly.assertThat(report.get(307))
                .hasModuleName("SW02-061.EventBlockCheck");

        softly.assertThat(report.get(1490))
                .hasCategory("Incomplete")
                .hasDescription("Identify blocks generating inefficient algorithms")
                .hasSeverity(Severity.WARNING_LOW);
        softly.assertThat(report.get(1491))
                .hasModuleName("SW02-493.CodeGenSettings");
        softly.assertThat(report.get(429))
                .hasModuleName("SW02-430.runHDLRecipChecks");

    }
}
