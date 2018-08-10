package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractIssueParserTest;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.Report;
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

    @Test
    void shouldLocateCorrectLineNumber() {
        Report warnings = parse("maven-line-number.log");

        assertThat(warnings).hasSize(1);
        assertThat(warnings.get(0)).hasPriority(Priority.NORMAL)
                .hasLineStart(45)
                .hasMessage("The project edu.hm.hafner:analysis-model:jar:1.0.0-SNAPSHOT uses prerequisites which is " 
                        + "only intended for maven-plugin projects but not for non maven-plugin projects. " 
                        + "For such purposes you should use the maven-enforcer-plugin. " 
                        + "See https://maven.apache.org/enforcer/enforcer-rules/requireMavenVersion.html");
    }

    /**
     * Parses a file with three warnings, two of them will be ignored because they are blank.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-16826">Issue 16826</a>
     */
    @Test
    void issue16826() {
        Report warnings = parse("issue16826.txt");

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
        Report warnings = parse("maven-large.log");

        assertThat(warnings).hasSize(1);
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report)
                .hasSize(5)
                .hasPriorities(2, 3, 0);
        report.stream().forEach(issue -> assertThat(issue.getFileName()).endsWith(getFileWithIssuesName()));
    }

    @Override
    protected MavenConsoleParser createParser() {
        return new MavenConsoleParser();
    }
}

