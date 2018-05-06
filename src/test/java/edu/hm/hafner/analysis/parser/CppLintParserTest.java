package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractIssueParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link CppLintParser}.
 *
 * @author Ullrich Hafner
 */
class CppLintParserTest extends AbstractIssueParserTest {
    CppLintParserTest() {
        super("cpplint.txt");
    }

    /**
     * Parses a file with CPP Lint warnings in the new format.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-18290">Issue 18290</a>
     */
    @Test
    void issue18290() {
        Report warnings = parse("issue18290.txt");

        assertThat(warnings).hasSize(2);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0)).hasLineStart(399)
                    .hasLineEnd(399)
                    .hasMessage("Missing space before {")
                    .hasFileName("/opt/ros/fuerte/stacks/Mule/Mapping/Local_map/src/LocalCostMap.cpp")
                    .hasCategory("whitespace/braces")
                    .hasPriority(Priority.HIGH);
            softly.assertThat(warnings.get(1)).hasLineStart(400)
                    .hasLineEnd(400)
                    .hasMessage("Tab found; better to use spaces")
                    .hasFileName("/opt/ros/fuerte/stacks/Mule/Mapping/Local_map/src/LocalCostMap.cpp")
                    .hasCategory("whitespace/tab")
                    .hasPriority(Priority.LOW);
        });
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report)
                .hasSize(1031)
                .hasPriorities(81, 201, 749);
        softly.assertThat(report.get(0))
                .hasLineStart(824)
                .hasLineEnd(824)
                .hasMessage("Tab found; better to use spaces")
                .hasFileName("c:/Workspace/Trunk/Project/P1/class.cpp")
                .hasCategory("whitespace/tab")
                .hasPriority(Priority.LOW);
    }

    @Override
    protected CppLintParser createParser() {
        return new CppLintParser();
    }
}

