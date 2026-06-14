package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

/**
 * Tests the class {@link GradleLintParser}.
 *
 * @author Akash Manna
 */
class GradleLintParserTest extends AbstractParserTest {
    GradleLintParserTest() {
        super("gradle-lint.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(2);

        softly.assertThat(report.get(0))
                .hasFileName("build.gradle")
                .hasLineStart(42)
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("dependency")
                .hasMessage("A newer version of this dependency is available.");

        softly.assertThat(report.get(1))
                .hasFileName("app/src/build.gradle")
                .hasLineStart(15)
                .hasSeverity(Severity.ERROR)
                .hasCategory("deprecated")
                .hasMessage("This API is deprecated and will be removed.");
    }

    @Override
    protected GradleLintParser createParser() {
        return new GradleLintParser();
    }
}
