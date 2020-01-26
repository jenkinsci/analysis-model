package edu.hm.hafner.analysis.parser.violations;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

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
                .hasMessage(
                        "NO_COVERAGE, org.pitest.mutationtest.engine.gregor.mutators.VoidMethodCallMutator, (Ljava/util/stream/Stream;)V")
                .hasFileName("edu/hm/hafner/analysis/Issues.java")
                .hasCategory("NO_COVERAGE")
                .hasType("org.pitest.mutationtest.engine.gregor.mutators.VoidMethodCallMutator")
                .hasLineStart(110)
                .hasSeverity(Severity.WARNING_NORMAL);
        softly.assertThat(report.get(1))
                .hasMessage(
                        "SURVIVED, org.pitest.mutationtest.engine.gregor.mutators.MathMutator, (Ledu/hm/hafner/analysis/Issues;Ledu/hm/hafner/analysis/Issues;)V")
                .hasCategory("SURVIVED")
                .hasType("org.pitest.mutationtest.engine.gregor.mutators.MathMutator")
                .hasFileName("edu/hm/hafner/analysis/Issues.java")
                .hasLineStart(503)
                .hasSeverity(Severity.WARNING_HIGH);
    }

    @Override
    protected PitAdapter createParser() {
        return new PitAdapter();
    }
}
