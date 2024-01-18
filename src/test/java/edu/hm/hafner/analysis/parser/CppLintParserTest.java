package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link CppLintParser}.
 *
 * @author Ullrich Hafner
 */
class CppLintParserTest extends AbstractParserTest {
    CppLintParserTest() {
        super("cpplint.txt");
    }

    /**
     * Parses a file with CPP Lint warnings in the new format.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-18290">Issue 18290</a>
     */
    @Test
    void issue18290() {
        Report warnings = parse("issue18290.txt");

        assertThat(warnings).hasSize(2);

        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0)).hasLineStart(399)
                    .hasLineEnd(399)
                    .hasMessage("Missing space before {")
                    .hasFileName("/opt/ros/fuerte/stacks/Mule/Mapping/Local_map/src/LocalCostMap.cpp")
                    .hasCategory("whitespace/braces")
                    .hasSeverity(Severity.WARNING_HIGH);
            softly.assertThat(warnings.get(1)).hasLineStart(400)
                    .hasLineEnd(400)
                    .hasMessage("Tab found; better to use spaces")
                    .hasFileName("/opt/ros/fuerte/stacks/Mule/Mapping/Local_map/src/LocalCostMap.cpp")
                    .hasCategory("whitespace/tab")
                    .hasSeverity(Severity.WARNING_LOW);
        }
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(1031);
        assertThatReportHasSeverities(report, 0, 81, 201, 749);
        softly.assertThat(report.get(0))
                .hasLineStart(824)
                .hasLineEnd(824)
                .hasMessage("Tab found; better to use spaces")
                .hasFileName("C:/Workspace/Trunk/Project/P1/class.cpp")
                .hasCategory("whitespace/tab")
                .hasSeverity(Severity.WARNING_LOW);
    }

    /**
     * Test CppLint messge with double colon.
     */
    @Test
    void cpplintMessageWithColon() {
        Report warnings = parse("cpplint-message-with-colon.txt");

        assertThat(warnings).hasSize(1);

        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasLineStart(1)
                    .hasLineEnd(1)
                    .hasMessage("Is this a non-const reference? If so, make const or use a pointer: std::vector<int> & indices")
                    .hasFileName("/path/to/file.cpp")
                    .hasCategory("runtime/references")
                    .hasSeverity(Severity.WARNING_LOW);
        }
    }

    @Override
    protected CppLintParser createParser() {
        return new CppLintParser();
    }
}

