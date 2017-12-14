package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link JSLintParser}.
 *
 * @author Gavin Mogan <gavin@kodekoan.com>
 */
public class JSLintParserTest extends ParserTester {
    private static final String EXPECTED_FILE_NAME = "duckworth/hudson-jslint-freestyle/src/prototype.js";

    /**
     * Parses a file with one warning that are started by ant.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-19127">Issue 19127</a>
     */
    @Test
    public void issue19127() {
        Issues<Issue> warnings = new JSLintParser().parse(openFile("jslint/jslint.xml"));

        assertThat(warnings).hasSize(197);

        assertSoftly(softly -> {

            softly.assertThat(warnings.get(0))
                    .hasPriority(Priority.HIGH)
                    .hasCategory(JSLintXMLSaxParser.CATEGORY_UNDEFINED_VARIABLE)
                    .hasLineStart(3)
                    .hasLineEnd(3)
                    .hasMessage("'window' is not defined.")
                    .hasFileName("C:/DVR/lint_Mobile-Localization_ws/evWebService/WebClientApi/api-v1.js")
                    .hasColumnStart(5);


        });

    }

    /**
     * Tests the JS-Lint parsing for warnings in different files.
     */
    @Test
    public void testParse() {
        Issues<Issue> results = createParser().parse(openFile());
        assertThat(results).hasSize(102);

        assertThat(results.getFiles()).hasSize(2);
        assertThat(results.getFiles()).containsExactlyInAnyOrder(EXPECTED_FILE_NAME, "duckworth/hudson-jslint-freestyle/src/scriptaculous.js");

        assertSoftly(softly -> {

            softly.assertThat(results.get(0))
                    .hasPriority(Priority.HIGH)
                    .hasCategory(JSLintXMLSaxParser.CATEGORY_PARSING)
                    .hasLineStart(10)
                    .hasLineEnd(10)
                    .hasMessage("Expected 'Version' to have an indentation at 5 instead at 3.")
                    .hasFileName(EXPECTED_FILE_NAME)
                    .hasColumnStart(3);
        });

    }

    /**
     * Tests the JS-Lint parsing for warnings in a single file.
     */
    @Test
    public void testParseWithSingleFile() {
        Issues<Issue> results = createParser().parse(openFile("jslint/single.xml"));

        assertThat(results).hasSize(51);
    }

    /**
     * Tests parsing of CSS-Lint files.
     */
    @Test
    public void testCssLint() {
        Issues<Issue> results = createParser().parse(openFile("jslint/csslint.xml"));

        assertThat(results).hasSize(51);
    }

    /**
     * Creates the parser.
     *
     * @return the warnings parser
     */
    protected AbstractParser createParser() {
        return new JSLintParser();
    }

    @Override
    protected String getWarningsFile() {
        return "jslint/multi.xml";
    }
}
