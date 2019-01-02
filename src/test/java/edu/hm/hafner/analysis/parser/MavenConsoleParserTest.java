package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

import static edu.hm.hafner.analysis.assertj.Assertions.*;
import static j2html.TagCreator.*;

/**
 * Tests the class {@link MavenConsoleParser}.
 *
 * @author Ullrich Hafner
 */
class MavenConsoleParserTest extends AbstractParserTest {

    private static final String XREF_LINK_DISABLED = "Unable to locate Source XRef to link to - DISABLED";

    /**
     * Creates a new instance of {@link MavenConsoleParserTest}.
     */
    protected MavenConsoleParserTest() {
        super("maven-console.txt");
    }

    @Test
    void shouldAssignTypesFromGoals() {
        Report warnings = parse("maven-goals.log");

        assertThat(warnings).hasSize(3);

        assertThat(warnings.get(0)).hasSeverity(Severity.WARNING_LOW)
                .hasLineStart(44).hasLineEnd(47)
                .hasType("maven-pmd-plugin:pmd");
        assertThatDescriptionIs(warnings, 0,
                XREF_LINK_DISABLED, XREF_LINK_DISABLED, XREF_LINK_DISABLED, XREF_LINK_DISABLED);

        assertThat(warnings.get(1)).hasSeverity(Severity.WARNING_LOW)
                .hasLineStart(50).hasLineEnd(53)
                .hasType("maven-pmd-plugin:cpd");
        assertThatDescriptionIs(warnings, 1,
                XREF_LINK_DISABLED, XREF_LINK_DISABLED, XREF_LINK_DISABLED, XREF_LINK_DISABLED);

        assertThat(warnings.get(2)).hasSeverity(Severity.WARNING_LOW)
                .hasLineStart(56).hasLineEnd(56)
                .hasType("maven-checkstyle-plugin:checkstyle");
        assertThatDescriptionIs(warnings, 2, XREF_LINK_DISABLED);
    }

    private void assertThatDescriptionIs(final Report warnings, final int index, final String... messages) {
        assertThat(warnings.get(index).getDescription()).isEqualTo(pre().with(code().withText(String.join("\n", messages))).render());
    }

    @Test
    void shouldLocateCorrectLineNumber() {
        Report warnings = parse("maven-line-number.log");

        assertThat(warnings).hasSize(1);
        assertThat(warnings.get(0)).hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(45);
        assertThatDescriptionIs(warnings, 0,
                "The project edu.hm.hafner:analysis-model:jar:1.0.0-SNAPSHOT uses prerequisites "
                        + "which is only intended for maven-plugin projects but not for non maven-plugin projects. "
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

