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
    void shouldAssignTypesFromGoals() {
        Report warnings = parse("maven-goals.log");

        assertThat(warnings).hasSize(5);

        assertThat(warnings.get(0)).hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(18).hasLineEnd(19)
                .hasType("maven-compiler-plugin:compile");
        assertThat(warnings.get(0).getMessage()).isEqualTo(
                "/Users/hafner/Development/git/analysis-model/src/main/java/edu/hm/hafner/analysis/parser/DrMemoryParser.java:[13,30] edu.hm.hafner.analysis.RegexpDocumentParser in edu.hm.hafner.analysis has been deprecated\n"
                        + "/Users/hafner/Development/git/analysis-model/src/main/java/edu/hm/hafner/analysis/Report.java:[82,44] [ConstructorLeaksThis] Constructors should not pass the 'this' reference out in method invocations, since the object may not be fully constructed.");

        assertThat(warnings.get(1)).hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(29).hasLineEnd(29)
                .hasType("maven-compiler-plugin:testCompile");
        assertThat(warnings.get(1).getMessage()).isEqualTo(
                "/Users/hafner/Development/git/analysis-model/src/test/java/edu/hm/hafner/util/EnsureTest.java:[31,25] [NullAway] passing @Nullable parameter 'null' where @NonNull is required");

        assertThat(warnings.get(2)).hasSeverity(Severity.WARNING_LOW)
                .hasLineStart(44).hasLineEnd(47)
                .hasType("maven-pmd-plugin:pmd");
        assertThat(warnings.get(2).getMessage()).isEqualTo(
                "Unable to locate Source XRef to link to - DISABLED\n"
                        + "Unable to locate Source XRef to link to - DISABLED\n"
                        + "Unable to locate Source XRef to link to - DISABLED\n"
                        + "Unable to locate Source XRef to link to - DISABLED");

        assertThat(warnings.get(3)).hasSeverity(Severity.WARNING_LOW)
                .hasLineStart(50).hasLineEnd(53)
                .hasType("maven-pmd-plugin:cpd");
        assertThat(warnings.get(3).getMessage()).isEqualTo(
                "Unable to locate Source XRef to link to - DISABLED\n"
                        + "Unable to locate Source XRef to link to - DISABLED\n"
                        + "Unable to locate Source XRef to link to - DISABLED\n"
                        + "Unable to locate Source XRef to link to - DISABLED");

        assertThat(warnings.get(4)).hasSeverity(Severity.WARNING_LOW)
                .hasLineStart(56).hasLineEnd(56)
                .hasType("maven-checkstyle-plugin:checkstyle");
        assertThat(warnings.get(4).getMessage()).isEqualTo(
                "Unable to locate Source XRef to link to - DISABLED");
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

