package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

import static edu.hm.hafner.analysis.assertj.Assertions.*;

/**
 * Tests the class {@link MavenConsoleParser}.
 *
 * @author Ullrich Hafner
 */
class MavenConsoleParserTest extends AbstractParserTest {
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
        assertThat(warnings.get(0)).hasSeverity(Severity.WARNING_NORMAL)
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

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report)
                .hasSize(5)
                .hasSeverities(2, 0, 3, 0);
    }

    @Override
    protected MavenConsoleParser createParser() {
        return new MavenConsoleParser();
    }
}

