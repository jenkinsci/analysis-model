package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the class {@link MavenConsoleParser}.
 *
 * @author Ullrich Hafner
 */
public class MavenConsoleParserTest extends ParserTester {
    /**
     * Verifies that errors and warnings are correctly picked up.
     */
    @Test
    public void testParsing() {
        Issues<Issue> warnings = new MavenConsoleParser().parse(openFile());

        assertEquals(4, warnings.size());
        assertEquals(2, warnings.getHighPrioritySize());
        assertEquals(2, warnings.getNormalPrioritySize());
    }

    /**
     * Parses a file with three warnings, two of them will be ignored beacuse they are blank.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-16826">Issue 16826</a>
     */
    @Test
    public void issue16826() {
        Issues<Issue> warnings = new MavenConsoleParser().parse(openFile("issue16826.txt"));

        assertEquals(1, warnings.size());
    }

    /**
     * Parses a file with three warnings, two of them will be ignored because they are blank.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-25278">Issue 25278</a>
     */
    @Test
    @Disabled("Until JENKINS-25278 is fixed")
    public void largeFile() {
        Issues<Issue> warnings = new MavenConsoleParser().parse(openFile("maven-large.log"));

        assertEquals(1, warnings.size());
    }

    @Override
    protected String getWarningsFile() {
        return "maven-console.txt";
    }
}

