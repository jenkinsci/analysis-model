package edu.hm.hafner.analysis.parser.dry.simian;

import java.util.function.Function;

import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.assertj.Assertions.*;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import edu.hm.hafner.analysis.parser.dry.CodeDuplication;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Tests the extraction of Simian's analysis results.
 */
@SuppressFBWarnings("BC_UNCONFIRMED_CAST_OF_RETURN_VALUE")
class SimianParserTest extends AbstractParserTest<CodeDuplication> {
    private static final String MATRIX_RUN = "c:/java/hudson/matrix/MatrixRun.java";
    private static final String MAVEN_BUILD = "c:/java/hudson/maven/MavenBuild.java";

    SimianParserTest() {
        super("onefile.xml");
    }

    @Override
    protected SimianParser createParser() {
        return new SimianParser(50, 25);
    }

    @Override
    protected void assertThatIssuesArePresent(final Issues<CodeDuplication> issues,
            final SoftAssertions softly) {
        softly.assertThat(issues).hasSize(2);

        CodeDuplication firstIssue = issues.get(0);
        softly.assertThat(firstIssue)
                .hasLineStart(93).hasLineEnd(98)
                .hasFileName(MAVEN_BUILD)
                .hasPriority(Priority.LOW);
        softly.assertThat(firstIssue.getDescription()).isEmpty();

        CodeDuplication secondIssue = issues.get(1);
        softly.assertThat(secondIssue)
                .hasLineStart(76).hasLineEnd(81)
                .hasFileName(MAVEN_BUILD)
                .hasPriority(Priority.LOW);
        softly.assertThat(secondIssue.getDescription()).isEmpty();
    }

    @Test
    void shouldFindOneDuplicationInTwoFiles() {
        Issues<? extends CodeDuplication> issues = parse("twofile.xml");

        assertThat(issues).hasSize(2);

        assertThat(issues.get(0))
                .hasLineStart(92).hasLineEnd(97)
                .hasFileName(MAVEN_BUILD)
                .hasPriority(Priority.LOW);
        assertThat(issues.get(1))
                .hasLineStart(61).hasLineEnd(66)
                .hasFileName(MATRIX_RUN)
                .hasPriority(Priority.LOW);
    }

    @Test
    void shouldFindTwoDuplicationsInTwoFiles() {
        Issues<? extends CodeDuplication> issues = parse("twosets.xml");

        assertThat(issues).hasSize(4);

        assertThat(issues.get(0))
                .hasLineStart(92).hasLineEnd(97)
                .hasFileName(MAVEN_BUILD)
                .hasPriority(Priority.LOW);

        assertThat(issues.get(1))
                .hasLineStart(61).hasLineEnd(66)
                .hasFileName(MATRIX_RUN)
                .hasPriority(Priority.LOW);

        assertThat(issues.get(2))
                .hasLineStart(93).hasLineEnd(98)
                .hasFileName(MAVEN_BUILD)
                .hasPriority(Priority.LOW);

        assertThat(issues.get(3))
                .hasLineStart(76).hasLineEnd(81)
                .hasFileName(MAVEN_BUILD)
                .hasPriority(Priority.LOW);
    }

    @Test
    void shouldFindOneDuplicationInFourFiles() {
        Issues<? extends CodeDuplication> issues = parse("fourfile.xml");

        assertThat(issues).hasSize(4);

        assertThat(issues.get(0)).hasLineStart(11).hasLineEnd(16).hasFileName(getFileName(1));
        assertThat(issues.get(1)).hasLineStart(21).hasLineEnd(26).hasFileName(getFileName(2));
        assertThat(issues.get(2)).hasLineStart(31).hasLineEnd(36).hasFileName(getFileName(3));
        assertThat(issues.get(3)).hasLineStart(41).hasLineEnd(46).hasFileName(getFileName(4));
    }

    private String getFileName(final int number) {
        return String.format("c:/java/foo%d.java", number);
    }

    @Test
    void shouldSupportSimianParserVersion2331() {
        Issues<? extends CodeDuplication> issues = parse("simian-2.3.31.xml");

        assertThat(issues).hasSize(132);
    }

    @Test
    void shouldIgnoreOtherFile() {
        Issues<? extends CodeDuplication> issues = parse("otherfile.xml");

        assertThat(issues).hasSize(0);
    }

    @Test
    void shouldAssignPriority() {
        Issues<? extends CodeDuplication> issues;

        issues = parse(6, 5);
        assertThat(issues).hasSize(2);
        assertThat(issues.get(0)).hasPriority(Priority.HIGH);

        issues = parse(7, 6);
        assertThat(issues).hasSize(2);
        assertThat(issues.get(0)).hasPriority(Priority.NORMAL);

        issues = parse(100, 6);
        assertThat(issues).hasSize(2);
        assertThat(issues.get(0)).hasPriority(Priority.NORMAL);

        issues = parse(100, 7);
        assertThat(issues).hasSize(2);
        assertThat(issues.get(0)).hasPriority(Priority.LOW);
    }

    private Issues<? extends CodeDuplication> parse(final int highThreshold, final int normalThreshold) {
        return new SimianParser(highThreshold, normalThreshold)
                .parse(openFile("twofile.xml"), Function.identity());
    }
}
