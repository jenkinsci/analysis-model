package edu.hm.hafner.analysis.parser.violations;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link DocFxAdapter}.
 *
 * @author Ullrich Hafner
 */
class DocFxAdapterTest extends AbstractParserTest {
    DocFxAdapterTest() {
        super("docfx.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(3);
        softly.assertThat(report.get(0))
                .hasMessage("Invalid file link:(~/mobiilirajapinta/puuttuu.md).")
                .hasFileName("mobiilirajapinta/json-dateandtime.md")
                .hasType("InvalidFileLink")
                .hasLineStart(18)
                .hasSeverity(Severity.WARNING_NORMAL);
        softly.assertThat(report.get(1))
                .hasMessage("Invalid file link:(~/mobiilirajapinta/joopajoo.md).")
                .hasFileName("mobiilirajapinta/json-nimeämiskäytäntö.md")
                .hasType("InvalidFileLink")
                .hasLineStart(7)
                .hasSeverity(Severity.WARNING_NORMAL);
        softly.assertThat(report.get(2))
                .hasMessage("Invalid file link:(~/missing.md#mobiilisovellus).")
                .hasFileName("sanasto.md")
                .hasType("InvalidFileLink")
                .hasLineStart(63)
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Override
    protected DocFxAdapter createParser() {
        return new DocFxAdapter();
    }

    /**
     * Parses a warning log with DocFX info messages.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-55345">Issue 55345</a>
     */
    @Test
    void issue55750() {
        Report warnings = parse("issue55345.json");
        assertThat(warnings).isEmpty();
    }
}
