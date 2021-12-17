package edu.hm.hafner.analysis.parser.violations;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

/**
 * Tests the class {@link SarifAdapter}.
 *
 * @author Ullrich Hafner
 */
class SarifAdapterTest extends AbstractParserTest {
    SarifAdapterTest() {
        super("sarif.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(2);
        softly.assertThat(report.get(0))
                .hasMessage("asdasd asdasd")
                .hasFileName("/whatever/path.c")
                .hasLineStart(123)
                .hasType("Cyclomatic complexity")
                .hasSeverity(Severity.WARNING_HIGH);
    }

    @Override
    protected SarifAdapter createParser() {
        return new SarifAdapter();
    }

}
