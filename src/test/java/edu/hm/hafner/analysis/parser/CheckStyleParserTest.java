package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import edu.hm.hafner.analysis.parser.checkstyle.CheckStyleParser;

/**
 * Tests the extraction of CheckStyle analysis results.
 */
class CheckStyleParserTest extends AbstractParserTest {
    private static final String PREFIX = "checkstyle/";

    CheckStyleParserTest() {
        super(PREFIX + "checkstyle.xml");
    }

    @Override
    protected AbstractParser createParser() {
        return new CheckStyleParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Issues<Issue> issues, final SoftAssertions softly) {
        softly.assertThat(issues).hasSize(6);
        softly.assertThat(issues.getFiles()).hasSize(1);
        softly.assertThat(issues.getFiles()).containsExactly(
                "X:/Build/Results/jobs/Maven/workspace/tasks/src/main/java/hudson/plugins/tasks/parser/CsharpNamespaceDetector.java");

        softly.assertThat(issues.get(2))
                .hasLineStart(22)
                .hasCategory("Design")
                .hasType("DesignForExtensionCheck")
                .hasPriority(Priority.HIGH)
                .hasMessage("Die Methode 'detectPackageName' ist nicht fr Vererbung entworfen - muss abstract, final oder leer sein.");
    }

    /**
     * Parses a file with one fatal error.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-25511">Issue 25511</a>
     */
    @Test
    void issue25511() {
        Issues<Issue> issues = parseFromCheckStyleFolder("issue25511.xml");

        assertThat(issues).hasSize(2);

        assertThat(issues.get(0)).hasMessage("',' is not followed by whitespace.");
        assertThat(issues.get(1)).hasMessage("Type hint \"kEvent\" missing for $event at position 1");
    }

    /**
     * Tests parsing of file with some warnings that are in the same line but different column.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-19122">Issue 19122</a>
     */
    @Test
    void testColumnPositions() {
        Issues<Issue> issues = parseFromCheckStyleFolder("issue19122.xml");

        assertThat(issues).hasSize(58);
    }

    /**
     * Tests parsing of a file with Scala style warnings.
     *
     * @see <a href="http://www.scalastyle.org">Scala Style Homepage</a>
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-17287">Issue 17287</a>
     */
    @Test
    void testParsingOfScalaStyleFormat() {
        Issues<Issue> issues = parseFromCheckStyleFolder("scalastyle-output.xml");

        assertThat(issues).hasSize(2);
    }

    private Issues<Issue> parseFromCheckStyleFolder(final String fileName) {
        return parse(PREFIX + fileName);
    }
}
