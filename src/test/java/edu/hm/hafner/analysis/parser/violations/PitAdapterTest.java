package edu.hm.hafner.analysis.parser.violations;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

import static edu.hm.hafner.analysis.parser.violations.PitAdapter.*;

/**
 * Tests the class {@link PitAdapter}.
 *
 * @author Ullrich Hafner
 */
class PitAdapterTest extends AbstractParserTest {
    PitAdapterTest() {
        super("pit.xml");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(2);
        softly.assertThat(report.get(0))
                .hasMessage("SURVIVED, org.pitest.mutationtest.engine.gregor.mutators.MathMutator, (Ledu/hm/hafner/analysis/Issues;Ledu/hm/hafner/analysis/Issues;)V")
                .hasCategory("SURVIVED")
                .hasType("MathMutator")
                .hasFileName("edu/hm/hafner/analysis/Issues.java")
                .hasLineStart(503)
                .hasSeverity(Severity.WARNING_NORMAL);
        softly.assertThat(report.get(1))
                .hasMessage("NO_COVERAGE, org.pitest.mutationtest.engine.gregor.mutators.VoidMethodCallMutator, (Ljava/util/stream/Stream;)V")
                .hasFileName("edu/hm/hafner/analysis/Issues.java")
                .hasCategory("NO_COVERAGE")
                .hasType("VoidMethodCallMutator")
                .hasLineStart(110)
                .hasSeverity(Severity.WARNING_HIGH);
        softly.assertThat(report.getCounter(TOTAL_MUTATIONS)).isEqualTo(3);
        softly.assertThat(report.getCounter(KILLED_MUTATIONS)).isEqualTo(1);
        softly.assertThat(report.getCounter(UNCOVERED_MUTATIONS)).isEqualTo(1);
        softly.assertThat(report.getCounter(SURVIVED_MUTATIONS)).isEqualTo(1);
    }

    @Override
    protected PitAdapter createParser() {
        return new PitAdapter();
    }
}
