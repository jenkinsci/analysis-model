package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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

    @ParameterizedTest
    @ValueSource(
            strings = {
                "-- Build files have been written to: C:/workspace/build",
                "22>-- Build files have been written to: C:/workspace/build",
                "[timestamp] C/C++: -- Build files have been written to: C:/workspace/build",
                "make[1]: Entering directory 'C:/workspace/build'",
                "make[1]: Leaving directory 'C:/workspace/build'",
                "-- Build files have an unrelated message",
                "Entering directory without a valid path"
            })
    void shouldIgnoreBuildDirectoryMessages(final String directoryMessage) {
        var report = parseStringContent(directoryMessage + """

                CMake Warning at subproject/CMakeLists.txt:14 (message):
                  Configure warning
                """);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(report).hasSize(1).doesNotHaveErrors();
            softly.assertThat(report.get(0))
                    .hasFileName("subproject/CMakeLists.txt")
                    .hasPath("-")
                    .hasLineStart(14)
                    .hasMessage("Configure warning");
        }
    }

    @Test
    void shouldPreserveAbsoluteSourcePaths() {
        var report = parseStringContent("""
                -- Build files have been written to: C:/workspace/build
                CMake Warning at C:/workspace/source/cmake/options.cmake:7 (message):
                  Warning with an absolute source path
                """);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(report).hasSize(1).doesNotHaveErrors();
            softly.assertThat(report.get(0))
                    .hasFileName("C:/workspace/source/cmake/options.cmake")
                    .hasLineStart(7)
                    .hasMessage("Warning with an absolute source path");
        }
    }

    @Test
    void shouldNotCarryDirectoriesBetweenProjects() {
        var report = parseStringContent("""
                make: Entering directory 'C:/workspace/build'
                -- Build files have been written to: C:/workspace/build/external
                CMake Warning at external/CMakeLists.txt:3 (message):
                  External warning
                make: Leaving directory 'C:/workspace/build'
                -- Build files have been written to: C:/workspace/build/root
                CMake Warning at CMakeLists.txt:9 (message):
                  Root warning
                """);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(report).hasSize(2).doesNotHaveErrors();
            softly.assertThat(report.get(0))
                    .hasFileName("external/CMakeLists.txt")
                    .hasMessage("External warning");
            softly.assertThat(report.get(1)).hasFileName("CMakeLists.txt").hasMessage("Root warning");
        }
    }

    @Test
    void shouldReadPrefixedMultilineMessages() {
        var report = parseStringContent("""
                [step] CMake Warning at CMakeLists.txt:9 (message):
                [step]   First line
                [step]
                [step]   A wrapped explanation, continued
                [step]   on another line.
                [step]
                [step] CMake Warning at nested/CMakeLists.txt:4 (message):
                [step]   Next warning
                """);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(report).hasSize(2).doesNotHaveErrors();
            softly.assertThat(report.get(0)).hasFileName("CMakeLists.txt").hasMessage("""
                        First line

                        A wrapped explanation, continued
                        on another line.""");
            softly.assertThat(report.get(1))
                    .hasFileName("nested/CMakeLists.txt")
                    .hasMessage("Next warning");
        }
    }

    @Test
    void shouldExcludeCallStackFromMessage() {
        var report = parseStringContent("""
                CMake Warning at CMakeLists.txt:14 (message):
                  Warning raised by the nested project
                Call Stack (most recent call first):
                  external-project/CMakeLists.txt:4 (include)
                  CMakeLists.txt:38 (ExternalProject_Add)
                CMake Warning at after.cmake:3 (message):
                  Warning after the call stack
                """);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(report).hasSize(2).doesNotHaveErrors();
            softly.assertThat(report.get(0))
                    .hasFileName("CMakeLists.txt")
                    .hasMessage("Warning raised by the nested project");
            softly.assertThat(report.get(1)).hasFileName("after.cmake").hasMessage("Warning after the call stack");
        }
    }

    @Test
    void shouldNotConsumeWarningAfterEmptyMessage() {
        var report = parseStringContent("""
                CMake Warning at empty.cmake:3 (message):
                CMake Warning at after.cmake:4 (message):
                  Next warning
                """);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(report).hasSize(2).doesNotHaveErrors();
            softly.assertThat(report.get(0)).hasFileName("empty.cmake").hasMessage("");
            softly.assertThat(report.get(1)).hasFileName("after.cmake").hasMessage("Next warning");
        }
    }

    @Override
    protected CMakeParser createParser() {
        return new CMakeParser();
    }
}
