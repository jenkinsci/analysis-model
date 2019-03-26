package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link CMakeParser}.
 */
class CMakeParserTest extends AbstractParserTest {
    CMakeParserTest() {
        super("cmake.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(4);
        softly.assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("")
                .hasLineStart(0)
                .hasMessage("[step1]   Manually-specified variables were not used by the project")
                .hasFileName("<none>");
        softly.assertThat(report.get(1))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("")
                .hasLineStart(0)
                .hasMessage("[step2]   The build directory is a subdirectory of the source directory.")
                .hasFileName("CMakeLists.txt");
        softly.assertThat(report.get(2))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("(option)")
                .hasLineStart(10)
                .hasMessage("I'm the message")
                .hasFileName("tools/gtest-1.8/googlemock/CMakeLists.txt");
        softly.assertThat(report.get(3))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("(message)")
                .hasLineStart(423)
                .hasMessage("Special workaround applied")
                .hasFileName("project/utils/fancy.cmake");
    }

    @Override
    protected CMakeParser createParser() {
        return new CMakeParser();
    }
}

