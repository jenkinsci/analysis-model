package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import static edu.hm.hafner.analysis.assertj.IssuesAssert.*;

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

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(iterator.next())
                .hasPriority(Priority.HIGH)
                .hasCategory("autoloader_layout")
                .hasLineStart(1)
                .hasLineEnd(1)
                .hasMessage("failtest not in autoload module layout")
                .hasFileName("failtest.pp")
                .hasType(TYPE)
                .hasPackageName("-");

        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory("80chars")
                .hasLineStart(3)
                .hasLineEnd(3)
                .hasMessage("line has more than 80 characters")
                .hasFileName("./modules/test/manifests/init.pp")
                .hasType(TYPE)
                .hasPackageName("::test");

        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory("80chars")
                .hasLineStart(10)
                .hasLineEnd(10)
                .hasMessage("line has more than 80 characters")
                .hasFileName("./modules/test/manifests/sub/class/morefail.pp")
                .hasType(TYPE)
                .hasPackageName("::test::sub::class");

        softly.assertThat(iterator.next())
                .hasPriority(Priority.HIGH)
                .hasCategory("hard_tabs")
                .hasLineStart(4)
                .hasLineEnd(4)
                .hasMessage("tab character found")
                .hasFileName("C:/ProgramData/PuppetLabs/puppet/etc/manifests/site.pp")
                .hasType(TYPE)
                .hasPackageName("-");

        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory("80chars")
                .hasLineStart(15)
                .hasLineEnd(15)
                .hasMessage("line has more than 80 characters")
                .hasFileName("C:/CI CD/puppet/modules/jenkins/init.pp")
                .hasType(TYPE)
                .hasPackageName("-");

        softly.assertAll();
    }

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
