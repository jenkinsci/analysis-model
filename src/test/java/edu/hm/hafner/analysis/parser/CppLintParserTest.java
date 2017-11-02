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

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(issues).hasSize(1031);
        softly.assertThat(issues).hasHighPrioritySize(81);
        softly.assertThat(issues).hasNormalPrioritySize(201);
        softly.assertThat(issues).hasLowPrioritySize(749);

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

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(warnings).hasSize(2);
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

