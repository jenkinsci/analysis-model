package edu.hm.hafner.analysis.parser;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the extraction of Simian's analysis results.
 */
class SimianParserTest extends AbstractParserTest {
    private static final String MATRIX_RUN = "C:/java/hudson/matrix/MatrixRun.java";
    private static final String MAVEN_BUILD = "C:/java/hudson/maven/MavenBuild.java";

    SimianParserTest() {
        super("simian/onefile.xml");
    }

    @Override
    protected SimianParser createParser() {
        return new SimianParser(50, 25);
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report,
            final SoftAssertions softly) {
        softly.assertThat(report).hasSize(2);

        var firstIssue = report.get(0);
        softly.assertThat(firstIssue)
                .hasLineStart(93).hasLineEnd(98)
                .hasFileName(MAVEN_BUILD)
                .hasSeverity(Severity.WARNING_LOW)
                .hasMessage("Found duplicated code.")
                .hasCategory("Code Duplication")
                .hasType("Simian");
        softly.assertThat(firstIssue.getDescription()).isEmpty();

        var secondIssue = report.get(1);
        softly.assertThat(secondIssue)
                .hasLineStart(76).hasLineEnd(81)
                .hasFileName(MAVEN_BUILD)
                .hasSeverity(Severity.WARNING_LOW)
                .hasMessage("Found duplicated code.")
                .hasCategory("Code Duplication")
                .hasType("Simian");
        softly.assertThat(secondIssue.getDescription()).isEmpty();
    }

    @Test
    void shouldFindOneDuplicationInTwoFiles() {
        var report = parseSimian("twofile.xml");

        assertThat(report).hasSize(2);

        assertThat(report.get(0))
                .hasLineStart(92).hasLineEnd(97)
                .hasFileName(MAVEN_BUILD)
                .hasSeverity(Severity.WARNING_LOW);
        assertThat(report.get(1))
                .hasLineStart(61).hasLineEnd(66)
                .hasFileName(MATRIX_RUN)
                .hasSeverity(Severity.WARNING_LOW);
    }

    private Report parseSimian(final String fileName) {
        return parse("simian/" + fileName);
    }

    @Test
    void shouldFindTwoDuplicationsInTwoFiles() {
        var report = parseSimian("twosets.xml");

        assertThat(report).hasSize(4);

        assertThat(report.get(0))
                .hasLineStart(92).hasLineEnd(97)
                .hasFileName(MAVEN_BUILD)
                .hasSeverity(Severity.WARNING_LOW);

        assertThat(report.get(1))
                .hasLineStart(61).hasLineEnd(66)
                .hasFileName(MATRIX_RUN)
                .hasSeverity(Severity.WARNING_LOW);

        assertThat(report.get(2))
                .hasLineStart(93).hasLineEnd(98)
                .hasFileName(MAVEN_BUILD)
                .hasSeverity(Severity.WARNING_LOW);

        assertThat(report.get(3))
                .hasLineStart(76).hasLineEnd(81)
                .hasFileName(MAVEN_BUILD)
                .hasSeverity(Severity.WARNING_LOW);
    }

    @Test
    void shouldFindOneDuplicationInFourFiles() {
        var report = parseSimian("fourfile.xml");

        assertThat(report).hasSize(4);

        assertThat(report.get(0)).hasLineStart(11).hasLineEnd(16).hasFileName(getFileName(1));
        assertThat(report.get(1)).hasLineStart(21).hasLineEnd(26).hasFileName(getFileName(2));
        assertThat(report.get(2)).hasLineStart(31).hasLineEnd(36).hasFileName(getFileName(3));
        assertThat(report.get(3)).hasLineStart(41).hasLineEnd(46).hasFileName(getFileName(4));
    }

    private String getFileName(final int number) {
        return String.format(Locale.ENGLISH, "C:/java/foo%d.java", number);
    }

    @Test
    void shouldSupportSimianParserVersion2331() {
        var report = parseSimian("simian-2.3.31.xml");

        assertThat(report).hasSize(132);
    }

    @Test
    void shouldIgnoreOtherFile() {
        var report = parseSimian("otherfile.xml");

        assertThat(report).hasSize(0);
    }

    @Test
    void shouldAssignPriority() {
        Report report;

        report = parse(6, 5);
        assertThat(report).hasSize(2);
        assertThat(report.get(0)).hasSeverity(Severity.WARNING_HIGH);

        report = parse(7, 6);
        assertThat(report).hasSize(2);
        assertThat(report.get(0)).hasSeverity(Severity.WARNING_NORMAL);

        report = parse(100, 6);
        assertThat(report).hasSize(2);
        assertThat(report.get(0)).hasSeverity(Severity.WARNING_NORMAL);

        report = parse(100, 7);
        assertThat(report).hasSize(2);
        assertThat(report.get(0)).hasSeverity(Severity.WARNING_LOW);
    }

    private Report parse(final int highThreshold, final int normalThreshold) {
        var parser = new SimianParser(highThreshold, normalThreshold);
        return parser.parseReport(createReaderFactory("simian/twofile.xml"));
    }
}
