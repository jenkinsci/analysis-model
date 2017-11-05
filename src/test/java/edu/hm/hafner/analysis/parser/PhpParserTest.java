package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.IssuesAssert.*;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link PhpParser}.
 *
 * @author Shimi Kiviti
 */
public class PhpParserTest extends ParserTester {
    private static final String TYPE = new PhpParser().getId();

    private static final String PARSE_ERROR_CATEGORY = "PHP Parse error";
    private static final String FATAL_ERROR_CATEGORY = "PHP Fatal error";
    private static final String WARNING_CATEGORY = "PHP Warning";
    private static final String NOTICE_CATEGORY = "PHP Notice";

    /**
     * Verifies that FATAL errors are reported.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-27681">Issue 27681</a>
     */
    @Test
    public void issue27681() throws IOException {
        Issues warnings = new PhpParser().parse(openFile("issue27681.txt"));
        assertThat(warnings).hasSize(1);

        Issue annotation = warnings.get(0);
        checkWarnings(annotation, Priority.HIGH, FATAL_ERROR_CATEGORY, 0,
                "SOAP-ERROR: Parsing WSDL: Couldn't load from '...' : failed to load external entity \"...\"", "-", TYPE);
    }

    /**
     * Tests the PHP parsing.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testParse() throws IOException {
        Issues results = createParser().parse(openFile());
        assertThat(results).hasSize(5);

        Iterator<Issue> iterator = results.iterator();

        checkWarnings(iterator.next(), Priority.NORMAL, WARNING_CATEGORY, 25,
                "include_once(): Failed opening \'RegexpLineParser.php\' for inclusion (include_path=\'.:/usr/share/pear\') in PhpParser.php on line 25", "PhpParser.php", TYPE);

        checkWarnings(iterator.next(), Priority.NORMAL, NOTICE_CATEGORY, 25,
                "Undefined index:  SERVER_NAME in /path/to/file/Settings.php on line 25", "/path/to/file/Settings.php", TYPE);

        checkWarnings(iterator.next(), Priority.HIGH, FATAL_ERROR_CATEGORY, 35,
                "Undefined class constant 'MESSAGE' in /MyPhpFile.php on line 35", "/MyPhpFile.php", TYPE);

        checkWarnings(iterator.next(), Priority.HIGH, PARSE_ERROR_CATEGORY, 35,
                "Undefined class constant 'MESSAGE' in /MyPhpFile.php on line 35", "/MyPhpFile.php", TYPE);

        checkWarnings(iterator.next(), Priority.NORMAL, WARNING_CATEGORY, 34,
                "Missing argument 1 for Title::getText(), called in Title.php on line 22 and defined in Category.php on line 34", "Category.php", TYPE);
    }

    private void checkWarnings(Issue issue, Priority priority, String category, int lineStartAndEnd, String message, String fileName, String type) {
        assertSoftly(softly -> softly.assertThat(issue)
                .hasPriority(priority)
                .hasCategory(category)
                .hasLineStart(lineStartAndEnd)
                .hasLineEnd(lineStartAndEnd)
                .hasMessage(message)
                .hasFileName(fileName)
                .hasType(type)
        );
    }

    /**
     * Creates the parser.
     *
     * @return the warnings parser
     */
    protected AbstractParser createParser() {
        return new PhpParser();
    }

    @Override
    protected String getWarningsFile() {
        return "php.txt";
    }
}
