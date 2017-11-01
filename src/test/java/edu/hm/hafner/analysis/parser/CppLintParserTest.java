package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;


/**
 * Tests the class {@link CppLintParser}.
 *
 * @author Ullrich Hafner
 */
public class CppLintParserTest extends ParserTester {
    @Override
    protected String getWarningsFile() {
        return "cpplint.txt";
    }

    /** Parses a file with 1031 warnings. */
    @Test
    public void shouldFindAll1031Warnings() {
        Issues issues = new CppLintParser().parse(openFile());
        int expectedIssuesSize = 1031;
        int expectedHighPrioritySize = 81;
        int expectedNormalPrioritySize = 201;
        int expectedLowPrioritySize = 749;


        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(issues).hasSize(expectedIssuesSize);
        softly.assertThat(issues).hasHighPrioritySize(expectedHighPrioritySize);
        softly.assertThat(issues).hasNormalPrioritySize(expectedNormalPrioritySize);
        softly.assertThat(issues).hasLowPrioritySize(expectedLowPrioritySize);

        softly.assertThat(issues.get(0)).hasLineStart(824)
                .hasLineEnd(824)
                .hasMessage("Tab found; better to use spaces")
                .hasFileName("c:/Workspace/Trunk/Project/P1/class.cpp")
                .hasCategory("whitespace/tab")
                .hasPriority(Priority.LOW);
        softly.assertAll();
    }

    /**
     * Parses a file with CPP Lint warnings in the new format.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-18290">Issue 18290</a>
     */
    @Test
    public void issue18290() {
        Issues warnings = new CppLintParser().parse(openFile("issue18290.txt"));
        Iterator<Issue> iterator = warnings.iterator();
        int expectedSize = 2;

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(warnings).hasSize(expectedSize);
        softly.assertThat(iterator.next()).hasLineStart(399)
                .hasLineEnd(399)
                .hasMessage("Missing space before {")
                .hasFileName("/opt/ros/fuerte/stacks/Mule/Mapping/Local_map/src/LocalCostMap.cpp")
                .hasCategory("whitespace/braces")
                .hasPriority(Priority.HIGH);
        softly.assertThat(iterator.next()).hasLineStart(400)
                .hasLineEnd(400)
                .hasMessage("Tab found; better to use spaces")
                .hasFileName("/opt/ros/fuerte/stacks/Mule/Mapping/Local_map/src/LocalCostMap.cpp")
                .hasCategory("whitespace/tab")
                .hasPriority(Priority.LOW);
        softly.assertAll();
    }
}

