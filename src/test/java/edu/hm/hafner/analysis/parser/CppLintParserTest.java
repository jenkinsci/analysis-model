package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import org.junit.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.Assert.*;

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

        assertEquals(1031, issues.size());

        assertEquals(81, issues.getHighPrioritySize());
        assertEquals(201, issues.getNormalPrioritySize());
        assertEquals(749, issues.getLowPrioritySize());

        checkWarning(issues.get(0),
                824,
                "Tab found; better to use spaces",
                "c:/Workspace/Trunk/Project/P1/class.cpp",
                "whitespace/tab", Priority.LOW);
    }

    /**
     * Parses a file with CPP Lint warnings in the new format.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-18290">Issue 18290</a>
     */
    @Test
    public void issue18290() {
        Issues warnings = new CppLintParser().parse(openFile("issue18290.txt"));

        assertEquals(2, warnings.size());
        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation = iterator.next();
        checkWarning(annotation, 399, "Missing space before {",
                "/opt/ros/fuerte/stacks/Mule/Mapping/Local_map/src/LocalCostMap.cpp",
                "whitespace/braces", Priority.HIGH);
        annotation = iterator.next();
        checkWarning(annotation, 400, "Tab found; better to use spaces",
                "/opt/ros/fuerte/stacks/Mule/Mapping/Local_map/src/LocalCostMap.cpp",
                "whitespace/tab", Priority.LOW);
    }
}

