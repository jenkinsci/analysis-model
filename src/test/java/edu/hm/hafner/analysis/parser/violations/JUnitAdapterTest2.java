package edu.hm.hafner.analysis.parser.violations;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

import static org.junit.jupiter.api.Assertions.*;

class JUnitAdapterTest2 extends AbstractParserTest {
    JUnitAdapterTest2() {
        super("TEST-org.jenkinsci.plugins.jvctb.perform.JvctbPerformerTest.xml");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(1);
        softly.assertThat(report.get(0))
                .hasFileName("org/jenkinsci/plugins/jvctb/perform/JvctbPerformerTest.java")
                .hasSeverity(Severity.WARNING_HIGH);
        softly.assertThat(report.get(0).getMessage())
                .startsWith("testThatAll")
                .contains("nondada");
    }

    @Override
    protected JUnitAdapter createParser() {
        return new JUnitAdapter();
    }
}