package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.Assert.*;

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

        assertEquals(1, warnings.size());
        Issue annotation = warnings.iterator().next();

        checkWarning(annotation,
                0, "SOAP-ERROR: Parsing WSDL: Couldn't load from '...' : failed to load external entity \"...\"",
                "-", TYPE, FATAL_ERROR_CATEGORY, Priority.HIGH);
    }

    /**
     * Tests the PHP parsing.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testParse() throws IOException {
        Issues results = createParser().parse(openFile());
        assertEquals(5, results.size());

        Iterator<Issue> iterator = results.iterator();
        Issue annotation = iterator.next();
        checkWarning(annotation,
                25, "include_once(): Failed opening \'RegexpLineParser.php\' for inclusion (include_path=\'.:/usr/share/pear\') in PhpParser.php on line 25",
                "PhpParser.php", TYPE, WARNING_CATEGORY, Priority.NORMAL);

        annotation = iterator.next();
        checkWarning(annotation,
                25, "Undefined index:  SERVER_NAME in /path/to/file/Settings.php on line 25",
                "/path/to/file/Settings.php", TYPE, NOTICE_CATEGORY, Priority.NORMAL);

        annotation = iterator.next();
        checkWarning(annotation,
                35, "Undefined class constant 'MESSAGE' in /MyPhpFile.php on line 35",
                "/MyPhpFile.php", TYPE, FATAL_ERROR_CATEGORY, Priority.HIGH);

        annotation = iterator.next();
        checkWarning(annotation,
                35, "Undefined class constant 'MESSAGE' in /MyPhpFile.php on line 35",
                "/MyPhpFile.php", TYPE, PARSE_ERROR_CATEGORY, Priority.HIGH);

        annotation = iterator.next();
        checkWarning(annotation,
                34, "Missing argument 1 for Title::getText(), called in Title.php on line 22 and defined in Category.php on line 34",
                "Category.php", TYPE, WARNING_CATEGORY, Priority.NORMAL);

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
