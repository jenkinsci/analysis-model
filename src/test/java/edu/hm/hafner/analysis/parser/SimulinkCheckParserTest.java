package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

class SimulinkCheckParserTest extends AbstractParserTest {

    SimulinkCheckParserTest(){ super("Simulink.html"); }

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
        softly.assertThat(report.get(500))
                .hasCategory("Not Run")
                .hasDescription("Check if model uses row major algorithms")
                .hasSeverity(Severity.WARNING_HIGH);
        softly.assertThat(report.get(302))
                .hasCategory("Failed")
                .hasDescription("Check model for Stateflow messages")
                .hasSeverity(Severity.ERROR);
        softly.assertThat(report.get(1490))
                .hasCategory("Incomplete")
                .hasDescription("Identify questionable code instrumentation (data I/O)")
                .hasSeverity(Severity.WARNING_LOW);

    }
}
