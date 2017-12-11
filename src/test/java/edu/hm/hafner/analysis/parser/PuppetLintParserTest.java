package edu.hm.hafner.analysis.parser;

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
    /**
     * Tests the Puppet-Lint parsing.
     */
    @Test
    public void testParse() {
        Issues<Issue> results = createParser().parse(openFile());

        assertThat(results).hasSize(5);

        Iterator<Issue> iterator = results.iterator();

        assertSoftly(softly -> {
            softly.assertThat(iterator.next())
                    .hasPriority(Priority.HIGH)
                    .hasCategory("autoloader_layout")
                    .hasLineStart(1)
                    .hasLineEnd(1)
                    .hasMessage("failtest not in autoload module layout")
                    .hasFileName("failtest.pp")
                    .hasPackageName("-");

            softly.assertThat(iterator.next())
                    .hasPriority(Priority.NORMAL)
                    .hasCategory("80chars")
                    .hasLineStart(3)
                    .hasLineEnd(3)
                    .hasMessage("line has more than 80 characters")
                    .hasFileName("./modules/test/manifests/init.pp")
                    .hasPackageName("::test");

            softly.assertThat(iterator.next())
                    .hasPriority(Priority.NORMAL)
                    .hasCategory("80chars")
                    .hasLineStart(10)
                    .hasLineEnd(10)
                    .hasMessage("line has more than 80 characters")
                    .hasFileName("./modules/test/manifests/sub/class/morefail.pp")
                    .hasPackageName("::test::sub::class");

            softly.assertThat(iterator.next())
                    .hasPriority(Priority.HIGH)
                    .hasCategory("hard_tabs")
                    .hasLineStart(4)
                    .hasLineEnd(4)
                    .hasMessage("tab character found")
                    .hasFileName("C:/ProgramData/PuppetLabs/puppet/etc/manifests/site.pp")
                    .hasPackageName("-");

            softly.assertThat(iterator.next())
                    .hasPriority(Priority.NORMAL)
                    .hasCategory("80chars")
                    .hasLineStart(15)
                    .hasLineEnd(15)
                    .hasMessage("line has more than 80 characters")
                    .hasFileName("C:/CI CD/puppet/modules/jenkins/init.pp")
                    .hasPackageName("-");
        });
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
