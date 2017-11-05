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
        assertSoftly(softly -> softly.assertThat(annotation)
                .hasPriority(Priority.HIGH)
                .hasCategory(FATAL_ERROR_CATEGORY)
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("SOAP-ERROR: Parsing WSDL: Couldn't load from '...' : failed to load external entity \"...\"")
                .hasFileName("-")
                .hasType(TYPE)
        );
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
        assertSoftly(softly -> softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(25)
                .hasLineEnd(25)
                .hasMessage("include_once(): Failed opening \'RegexpLineParser.php\' for inclusion (include_path=\'.:/usr/share/pear\') in PhpParser.php on line 25")
                .hasFileName("PhpParser.php")
                .hasType(TYPE)
        );

        assertSoftly(softly -> softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(NOTICE_CATEGORY)
                .hasLineStart(25)
                .hasLineEnd(25)
                .hasMessage("Undefined index:  SERVER_NAME in /path/to/file/Settings.php on line 25")
                .hasFileName("/path/to/file/Settings.php")
                .hasType(TYPE)
        );

        assertSoftly(softly -> softly.assertThat(iterator.next())
                .hasPriority(Priority.HIGH)
                .hasCategory(FATAL_ERROR_CATEGORY)
                .hasLineStart(35)
                .hasLineEnd(35)
                .hasMessage("Undefined class constant 'MESSAGE' in /MyPhpFile.php on line 35")
                .hasFileName("/MyPhpFile.php")
                .hasType(TYPE)
        );

        assertSoftly(softly -> softly.assertThat(iterator.next())
                .hasPriority(Priority.HIGH)
                .hasCategory(PARSE_ERROR_CATEGORY)
                .hasLineStart(35)
                .hasLineEnd(35)
                .hasMessage("Undefined class constant 'MESSAGE' in /MyPhpFile.php on line 35")
                .hasFileName("/MyPhpFile.php")
                .hasType(TYPE)
        );

        assertSoftly(softly -> softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(34)
                .hasLineEnd(34)
                .hasMessage("Missing argument 1 for Title::getText(), called in Title.php on line 22 and defined in Category.php on line 34")
                .hasFileName("Category.php")
                .hasType(TYPE)
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
