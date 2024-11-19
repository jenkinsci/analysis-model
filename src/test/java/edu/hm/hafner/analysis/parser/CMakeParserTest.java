package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

/**
 * Tests the class {@link CMakeParser}.
 *
 * @author Uwe Brandt
 */
class CMakeParserTest extends AbstractParserTest {
    CMakeParserTest() {
        super("cmake.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(8);
        softly.assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("")
                .hasLineStart(0)
                .hasMessage("Manually-specified variables were not used by the project")
                .hasFileName("-");
        softly.assertThat(report.get(1))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("")
                .hasLineStart(0)
                .hasMessage("The build directory is a subdirectory of the source directory.")
                .hasFileName("CMakeLists.txt");
        softly.assertThat(report.get(2))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("option")
                .hasLineStart(10)
                .hasMessage("I'm the message")
                .hasFileName("tools/gtest-1.8/googlemock/CMakeLists.txt");
        softly.assertThat(report.get(3))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("message")
                .hasLineStart(423)
                .hasMessage("Special workaround applied")
                .hasFileName("project/utils/fancy.cmake");
        softly.assertThat(report.get(4))
                .hasSeverity(Severity.ERROR)
                .hasCategory("message")
                .hasLineStart(2)
                .hasMessage("Uh oh !$%@!")
                .hasFileName("error.cmake");
        softly.assertThat(report.get(5))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("message")
                .hasLineStart(23)
                .hasMessage("function foo is deprecated, use bar instead")
                .hasFileName("legacy.cmake");
        softly.assertThat(report.get(6))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("nonexistingcategory")
                .hasLineStart(357)
                .hasMessage("strange things can happen")
                .hasFileName("unlikely.cmake");
        softly.assertThat(report.get(7))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("message")
                .hasLineStart(362)
                .hasMessage("")
                .hasFileName("unlikely.cmake");
    }

    @Override
    protected CMakeParser createParser() {
        return new CMakeParser();
    }
}
