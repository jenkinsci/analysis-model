package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import static edu.hm.hafner.analysis.assertj.Assertions.*;

/**
 * Tests the class {@link MavenConsoleParser}.
 *
 * @author Ullrich Hafner
 */
public class MavenConsoleParserTest extends AbstractParserTest {
    /**
     * Creates a new instance of {@link MavenConsoleParserTest}.
     *
     */
    protected MavenConsoleParserTest() {
        super("maven-console.txt");
    }

    /**
     * Verifies that errors and warnings are correctly picked up.
     */
    @Test
    public void testParsing() {
        Issues<Issue> warnings = new MavenConsoleParser().parse(openFile());

        assertThat(warnings)
                .hasSize(4)
                .hasHighPrioritySize(2)
                .hasNormalPrioritySize(2)
                .hasLowPrioritySize(0);
    }

    /**
     * Parses a file with three warnings, two of them will be ignored beacuse they are blank.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-16826">Issue 16826</a>
     */
    @Test
    public void issue16826() {
        Issues<Issue> warnings = new MavenConsoleParser().parse(openFile("issue16826.txt"));

        assertThat(warnings).hasSize(1);
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

        assertThat(warnings).hasSize(1);
    }

    protected String getWarningsFile() {
        return "maven-console.txt";
    }

    @Override
    protected void assertThatIssuesArePresent(final Issues<Issue> issues, final SoftAssertions softly) {
        softly.assertThat(issues)
                .hasSize(4)
                .hasHighPrioritySize(2)
                .hasNormalPrioritySize(2)
                .hasLowPrioritySize(0);
    }

    @Override
    protected AbstractParser createParser() {
        return new MavenConsoleParser();
    }
}

