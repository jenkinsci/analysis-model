package edu.hm.hafner.analysis.parser.violations;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

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
                .hasFileName("/whatever/path.c")
                .hasLineStart(123)
                .hasType("Cyclomatic complexity")
                .hasSeverity(Severity.WARNING_HIGH);
        softly.assertThat(report.get(0).getMessage()).matches("asdasd");
        softly.assertThat(report.get(1))
                .hasFileName("/whatever/path.c")
                .hasLineStart(123)
                .hasType("-")
                .hasSeverity(Severity.WARNING_LOW);
        softly.assertThat(report.get(1).getMessage()).matches("asdasd");
    }

    @Test
    void shouldReportDifferentSeverities() {
        Report report = parse("security-scan.sarif");

        assertThat(report).hasSize(51);
        assertThat(report.getSizeOf(Severity.WARNING_HIGH)).isEqualTo(3);
        assertThat(report.getSizeOf(Severity.WARNING_NORMAL)).isEqualTo(45);
        assertThat(report.getSizeOf(Severity.WARNING_LOW)).isEqualTo(3);
    }

    @Override
    protected SarifAdapter createParser() {
        return new SarifAdapter();
    }
}
