package edu.hm.hafner.analysis.parser.dry.simian;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.Report;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the extraction of Simian's analysis results.
 */
class SimianParserTest extends AbstractParserTest {
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
    protected void assertThatIssuesArePresent(final Report report,
            final SoftAssertions softly) {
        softly.assertThat(report).hasSize(2);

        Issue firstIssue = report.get(0);
        softly.assertThat(firstIssue)
                .hasLineStart(93).hasLineEnd(98)
                .hasFileName(MAVEN_BUILD)
                .hasPriority(Priority.LOW);
        softly.assertThat(firstIssue.getDescription()).isEmpty();

        Issue secondIssue = report.get(1);
        softly.assertThat(secondIssue)
                .hasLineStart(76).hasLineEnd(81)
                .hasFileName(MAVEN_BUILD)
                .hasPriority(Priority.LOW);
        softly.assertThat(secondIssue.getDescription()).isEmpty();
    }

    @Test
    void shouldFindOneDuplicationInTwoFiles() {
        Report report = parse("twofile.xml");

        assertThat(report).hasSize(2);

        assertThat(report.get(0))
                .hasLineStart(92).hasLineEnd(97)
                .hasFileName(MAVEN_BUILD)
                .hasPriority(Priority.LOW);
        assertThat(report.get(1))
                .hasLineStart(61).hasLineEnd(66)
                .hasFileName(MATRIX_RUN)
                .hasPriority(Priority.LOW);
    }

    @Test
    void shouldFindTwoDuplicationsInTwoFiles() {
        Report report = parse("twosets.xml");

        assertThat(report).hasSize(4);

        assertThat(report.get(0))
                .hasLineStart(92).hasLineEnd(97)
                .hasFileName(MAVEN_BUILD)
                .hasPriority(Priority.LOW);

        assertThat(report.get(1))
                .hasLineStart(61).hasLineEnd(66)
                .hasFileName(MATRIX_RUN)
                .hasPriority(Priority.LOW);

        assertThat(report.get(2))
                .hasLineStart(93).hasLineEnd(98)
                .hasFileName(MAVEN_BUILD)
                .hasPriority(Priority.LOW);

        assertThat(report.get(3))
                .hasLineStart(76).hasLineEnd(81)
                .hasFileName(MAVEN_BUILD)
                .hasPriority(Priority.LOW);
    }

    @Test
    void shouldFindOneDuplicationInFourFiles() {
        Report report = parse("fourfile.xml");

        assertThat(report).hasSize(4);

        assertThat(report.get(0)).hasLineStart(11).hasLineEnd(16).hasFileName(getFileName(1));
        assertThat(report.get(1)).hasLineStart(21).hasLineEnd(26).hasFileName(getFileName(2));
        assertThat(report.get(2)).hasLineStart(31).hasLineEnd(36).hasFileName(getFileName(3));
        assertThat(report.get(3)).hasLineStart(41).hasLineEnd(46).hasFileName(getFileName(4));
    }

    private String getFileName(final int number) {
        return String.format("c:/java/foo%d.java", number);
    }

    @Test
    void shouldSupportSimianParserVersion2331() {
        Report report = parse("simian-2.3.31.xml");

        assertThat(report).hasSize(132);
    }

    @Test
    void shouldIgnoreOtherFile() {
        Report report = parse("otherfile.xml");

        assertThat(report).hasSize(0);
    }

    @Test
    void shouldAssignPriority() {
        Report report;

        report = parse(6, 5);
        assertThat(report).hasSize(2);
        assertThat(report.get(0)).hasPriority(Priority.HIGH);

        report = parse(7, 6);
        assertThat(report).hasSize(2);
        assertThat(report.get(0)).hasPriority(Priority.NORMAL);

        report = parse(100, 6);
        assertThat(report).hasSize(2);
        assertThat(report.get(0)).hasPriority(Priority.NORMAL);

        report = parse(100, 7);
        assertThat(report).hasSize(2);
        assertThat(report.get(0)).hasPriority(Priority.LOW);
    }

    private Report parse(final int highThreshold, final int normalThreshold) {
        return new SimianParser(highThreshold, normalThreshold)
                .parse(getResourceAsFile("twofile.xml"), StandardCharsets.UTF_8);
    }
}
