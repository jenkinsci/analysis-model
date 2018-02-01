package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractIssueParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link MavenConsoleParser}.
 *
 * @author Ullrich Hafner
 */
class MavenConsoleParserTest extends AbstractIssueParserTest {
    /**
     * Creates a new instance of {@link MavenConsoleParserTest}.
     */
    protected MavenConsoleParserTest() {
        super("maven-console.txt");
    }

    /**
     * Parses a file with three warnings, two of them will be ignored beacuse they are blank.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-16826">Issue 16826</a>
     */
    @Test
    void issue16826() {
        Issues<Issue> warnings = parse("issue16826.txt");

        assertThat(warnings).hasSize(1);
    }

    /**
     * Parses a file with three warnings, two of them will be ignored because they are blank.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-25278">Issue 25278</a>
     */
    @Test
    @Disabled("Until JENKINS-25278 is fixed")
    void largeFile() {
        Issues<Issue> warnings = parse("maven-large.log");

        assertThat(warnings).hasSize(1);
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
    protected MavenConsoleParser createParser() {
        return new MavenConsoleParser();
    }
}

