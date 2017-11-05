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
 * Tests the class {@link PuppetLintParser}.
 *
 * @author Jan Vansteenkiste <jan@vstone.eu>
 */
public class PuppetLintParserTest extends ParserTester {
    private static final String TYPE = new PuppetLintParser().getId();

    /**
     * Tests the Puppet-Lint parsing.
     *
     * @throws IOException in case of an error
     */
    @Test
    public void testParse() throws IOException {
        Issues results = createParser().parse(openFile());
        assertThat(results).hasSize(5);

        Iterator<Issue> iterator = results.iterator();
        checkLintWarning(iterator.next(),
                1, "failtest not in autoload module layout",
                "failtest.pp", TYPE, "autoloader_layout", Priority.HIGH, "-");

        checkLintWarning(iterator.next(),
                3, "line has more than 80 characters",
                "./modules/test/manifests/init.pp", TYPE, "80chars", Priority.NORMAL, "::test");

        checkLintWarning(iterator.next(),
                10, "line has more than 80 characters",
                "./modules/test/manifests/sub/class/morefail.pp", TYPE, "80chars", Priority.NORMAL, "::test::sub::class");

        checkLintWarning(iterator.next(),
                4, "tab character found",
                "C:/ProgramData/PuppetLabs/puppet/etc/manifests/site.pp", TYPE, "hard_tabs", Priority.HIGH, "-");

        checkLintWarning(iterator.next(),
                15, "line has more than 80 characters",
                "C:/CI CD/puppet/modules/jenkins/init.pp", TYPE, "80chars", Priority.NORMAL, "-");
    }

    /**
     * Checks the properties of the specified warning.
     *
     * @param annotation  the warning to check
     * @param lineNumber  the expected line number
     * @param message     the expected message
     * @param fileName    the expected filename
     * @param type        the expected type
     * @param category    the expected category
     * @param priority    the expected priority
     * @param packageName the expected package name
     */
    // CHECKSTYLE:OFF
    private void checkLintWarning(final Issue annotation, final int lineNumber, final String message, final String fileName, final String type, final String category, final Priority priority, final String packageName) {
        assertSoftly(softly -> softly.assertThat(annotation)
                .hasPriority(Priority.NORMAL)
                .hasCategory("28101")
                .hasLineStart(1)
                .hasLineEnd(1)
                .hasMessage("failtest not in autoload module layout")
                .hasFileName("failtest.pp")
                .hasType(TYPE)
                .hasPackageName(packageName)
        );
    }
    // CHECKSTYLE:ON

    /**
     * Creates the parser.
     *
     * @return the warnings parser
     */
    protected AbstractParser createParser() {
        return new PuppetLintParser();
    }

    @Override
    protected String getWarningsFile() {
        return "puppet-lint.txt";
    }
}
