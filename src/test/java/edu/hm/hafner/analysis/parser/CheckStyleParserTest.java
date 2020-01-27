package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.parser.checkstyle.CheckStyleParser;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the extraction of CheckStyle analysis results.
 */
class CheckStyleParserTest extends AbstractParserTest {
    private static final String PREFIX = "checkstyle/";

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
        Report report = parseInCheckStyleFolder("issue25511.xml");

        assertThat(report).hasSize(2);

        assertThat(report.get(0)).hasMessage("',' is not followed by whitespace.");
        assertThat(report.get(1)).hasMessage("Type hint \"kEvent\" missing for $event at position 1");
    }

    /**
     * Tests parsing of file with some warnings that are in the same line but different column.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-19122">Issue 19122</a>
     */
    @Test
    void testColumnPositions() {
        Report report = parseInCheckStyleFolder("issue19122.xml");

        assertThat(report).hasSize(58);
    }

    /**
     * Tests parsing of a file with Scala style warnings.
     *
     * @see <a href="http://www.scalastyle.org">Scala Style Homepage</a>
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-17287">Issue 17287</a>
     */
    @Test
    void testParsingOfScalaStyleFormat() {
        Report report = parseInCheckStyleFolder("scalastyle-output.xml");

        assertThat(report).hasSize(2);
    }

    /**
     * Test parsing a file and checks the correct servity mapping for error.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-56214">Issue 56214</a>
     */
    @Test
    void shouldParseErrorToServityError() {

        Report report = parseInCheckStyleFolder("all-severites.xml");
        assertThat(report.get(0)).hasSeverity(Severity.ERROR);
    }

    /**
     * Test parsing a file and checks the correct servity mapping for warnings.
     */
    @Test
    void shouldParseWarningToServityWarningNormal() {

        Report report = parseInCheckStyleFolder("all-severites.xml");
        assertThat(report.get(1)).hasSeverity(Severity.WARNING_NORMAL);
    }

    /**
     * Test parsing a file and checks the correct servity mapping for infos.
     */
    @Test
    void shouldParseInfoToServityWarningLow() {

        Report report = parseInCheckStyleFolder("all-severites.xml");
        assertThat(report.get(2)).hasSeverity(Severity.WARNING_LOW);
    }

    private Report parseInCheckStyleFolder(final String fileName) {
        return parse(PREFIX + fileName);
    }
}
