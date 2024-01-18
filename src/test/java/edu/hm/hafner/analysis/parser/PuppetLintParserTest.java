package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link PuppetLintParser}.
 *
 * @author Jan Vansteenkiste
 */
class PuppetLintParserTest extends AbstractParserTest {
    PuppetLintParserTest() {
        super("puppet-lint.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(5);

        Iterator<Issue> iterator = report.iterator();

        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("autoloader_layout")
                .hasLineStart(1)
                .hasLineEnd(1)
                .hasMessage("failtest not in autoload module layout")
                .hasFileName("failtest.pp")
                .hasPackageName("-");

        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("80chars")
                .hasLineStart(3)
                .hasLineEnd(3)
                .hasMessage("line has more than 80 characters")
                .hasFileName("./modules/test/manifests/init.pp")
                .hasPackageName("::test");

        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("80chars")
                .hasLineStart(10)
                .hasLineEnd(10)
                .hasMessage("line has more than 80 characters")
                .hasFileName("./modules/test/manifests/sub/class/morefail.pp")
                .hasPackageName("::test::sub::class");

        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("hard_tabs")
                .hasLineStart(4)
                .hasLineEnd(4)
                .hasMessage("tab character found")
                .hasFileName("C:/ProgramData/PuppetLabs/puppet/etc/manifests/site.pp")
                .hasPackageName("-");

        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("80chars")
                .hasLineStart(15)
                .hasLineEnd(15)
                .hasMessage("line has more than 80 characters")
                .hasFileName("C:/CI CD/puppet/modules/jenkins/init.pp")
                .hasPackageName("-");
    }

    /**
     * Creates the parser.
     *
     * @return the warnings parser
     */
    @Override
    protected PuppetLintParser createParser() {
        return new PuppetLintParser();
    }
}
