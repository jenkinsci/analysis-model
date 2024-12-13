package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.parser.checkstyle.CheckStyleParser;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the extraction of CheckStyle analysis results.
 */
class CheckStyleParserTest extends AbstractParserTest {
    private static final String PREFIX = "checkstyle/";
    private static final String REPORT_WITH_ALL_SEVERITES = "all-severities.xml";

    CheckStyleParserTest() {
        super(PREFIX + "checkstyle.xml");
    }

    @Override
    protected CheckStyleParser createParser() {
        return new CheckStyleParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(6);
        softly.assertThat(report.getFiles()).hasSize(1);
        softly.assertThat(report.getFiles()).containsExactly(
                "X:/Build/Results/jobs/Maven/workspace/tasks/src/main/java/hudson/plugins/tasks/parser/CsharpNamespaceDetector.java");

        softly.assertThat(report.get(2))
                .hasLineStart(22)
                .hasCategory("Design")
                .hasType("DesignForExtensionCheck")
                .hasSeverity(Severity.ERROR)
                .hasMessage(
                        "Die Methode 'detectPackageName' ist nicht fr Vererbung entworfen - muss abstract, final oder leer sein.");
    }

    /**
     * Parses a file with one fatal error.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-25511">Issue 25511</a>
     */
    @Test
    void issue25511() {
        var report = parseInCheckStyleFolder("issue25511.xml");

        assertThat(report).hasSize(2);

        assertThat(report.get(0)).hasMessage("',' is not followed by whitespace.");
        assertThat(report.get(1)).hasMessage("IssueType hint \"kEvent\" missing for $event at position 1");
    }

    /**
     * Tests parsing of file with some warnings that are in the same line but different column.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-19122">Issue 19122</a>
     */
    @Test
    void testColumnPositions() {
        var report = parseInCheckStyleFolder("issue19122.xml");

        assertThat(report).hasSize(58);
    }

    /**
     * Verifies that the error types of hadolint checks are correctly extracted.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-60859">Issue 60859</a>
     */
    @Test
    void shouldExtractTypeEvenIfNoDotPresent() {
        var report = parseInCheckStyleFolder("issue60859.xml");

        assertThat(report).hasSize(3);
        assertThat(report.get(0)).hasMessage(
                "Pin versions in apk add. Instead of `apk add <package>` use `apk add <package>=<version>`")
                .hasFileName("Dockerfile")
                .hasLineStart(13)
                .hasType("DL3018");
        assertThat(report.get(1)).hasMessage(
                "In POSIX sh, set option pipefail is undefined.")
                .hasFileName("Dockerfile")
                .hasLineStart(16)
                .hasType("SC2039");
        assertThat(report.get(2)).hasMessage(
                "Set the SHELL option -o pipefail before RUN with a pipe in it. If you are using /bin/sh in an alpine image or if your shell is symlinked to busybox then consider explicitly setting your SHELL to /bin/ash, or disable this check")
                .hasFileName("Dockerfile")
                .hasLineStart(16)
                .hasType("DL4006");
    }

    /**
     * Tests parsing of a file with Scala style warnings.
     *
     * @see <a href="http://www.scalastyle.org">Scala Style Homepage</a>
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-17287">Issue 17287</a>
     */
    @Test
    void testParsingOfScalaStyleFormat() {
        var report = parseInCheckStyleFolder("scalastyle-output.xml");

        assertThat(report).hasSize(2);
    }

    /**
     * Test parsing a file and checks the correct Severity mapping for error.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-56214">Issue 56214</a>
     */
    @Test
    void shouldParseErrorToSeverityError() {
        var report = parseInCheckStyleFolder(REPORT_WITH_ALL_SEVERITES);
        assertThat(report.get(0)).hasSeverity(Severity.ERROR);
    }

    /**
     * Test parsing a file and checks the correct Severity mapping for warnings.
     */
    @Test
    void shouldParseWarningToSeverityWarningNormal() {
        var report = parseInCheckStyleFolder(REPORT_WITH_ALL_SEVERITES);
        assertThat(report.get(1)).hasSeverity(Severity.WARNING_NORMAL);
    }

    /**
     * Test parsing a file and checks the correct Severity mapping for infos.
     */
    @Test
    void shouldParseInfoToSeverityWarningLow() {
        var report = parseInCheckStyleFolder(REPORT_WITH_ALL_SEVERITES);
        assertThat(report.get(2)).hasSeverity(Severity.WARNING_LOW);
    }

    private Report parseInCheckStyleFolder(final String fileName) {
        return parse(PREFIX + fileName);
    }
}
