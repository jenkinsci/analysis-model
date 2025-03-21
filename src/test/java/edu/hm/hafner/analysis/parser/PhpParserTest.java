package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link PhpParser}.
 *
 * @author Shimi Kiviti
 */
class PhpParserTest extends AbstractParserTest {
    private static final String PARSE_ERROR_CATEGORY = "PHP Parse error";
    private static final String FATAL_ERROR_CATEGORY = "PHP Fatal error";
    private static final String WARNING_CATEGORY = "PHP Warning";
    private static final String NOTICE_CATEGORY = "PHP Notice";

    PhpParserTest() {
        super("php.txt");
    }

    /**
     * Verifies that FATAL errors are reported.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-27681">Issue 27681</a>
     */
    @Test
    void issue27681() {
        var report = parse("issue27681.txt");

        assertThat(report).hasSize(1);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(report.get(0))
                    .hasSeverity(Severity.WARNING_HIGH)
                    .hasCategory(FATAL_ERROR_CATEGORY)
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("SOAP-ERROR: Parsing WSDL: Couldn't load from '...' : failed to load external entity \"...\"")
                    .hasFileName("-");
        }
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(5);

        Iterator<Issue> iterator = report.iterator();

        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(25)
                .hasLineEnd(25)
                .hasMessage("include_once(): Failed opening 'RegexpLineParser.php' for inclusion (include_path='.:/usr/share/pear') in PhpParser.php on line 25")
                .hasFileName("PhpParser.php");

        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(NOTICE_CATEGORY)
                .hasLineStart(25)
                .hasLineEnd(25)
                .hasMessage("Undefined index:  SERVER_NAME in /path/to/file/Settings.php on line 25")
                .hasFileName("/path/to/file/Settings.php");

        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory(FATAL_ERROR_CATEGORY)
                .hasLineStart(35)
                .hasLineEnd(35)
                .hasMessage("Undefined class constant 'MESSAGE' in /MyPhpFile.php on line 35")
                .hasFileName("/MyPhpFile.php");

        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory(PARSE_ERROR_CATEGORY)
                .hasLineStart(35)
                .hasLineEnd(35)
                .hasMessage("Undefined class constant 'MESSAGE' in /MyPhpFile.php on line 35")
                .hasFileName("/MyPhpFile.php");

        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(34)
                .hasLineEnd(34)
                .hasMessage("Missing argument 1 for Title::getText(), called in Title.php on line 22 and defined in Category.php on line 34")
                .hasFileName("Category.php");
    }

    @Override
    protected PhpParser createParser() {
        return new PhpParser();
    }
}
