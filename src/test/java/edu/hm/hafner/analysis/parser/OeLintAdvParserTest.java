package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

/**
 * Tests the class {@link OeLintAdvParser}.
 */
class OeLintAdvParserTest extends AbstractParserTest {
    OeLintAdvParserTest() {
        super("oelint-adv.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(8);
        softly.assertThat(report.get(0))
                .hasFileName("/disk/meta-some/cppcheck-native/cppcheck.inc")
                .hasLineStart(26)
                .hasSeverity(Severity.ERROR)
                .hasCategory("oelint.task.nomkdir")
                .hasMessage("'mkdir' shall not be used in do_install. Use 'install'");
        softly.assertThat(report.get(1))
                .hasFileName("/disk/meta-some/cppcheck-native/cppcheck-native_1.87.bb")
                .hasLineStart(0)
                .hasSeverity(Severity.ERROR)
                .hasCategory("oelint.var.mandatoryvar.SECTION")
                .hasMessage("Variable 'SECTION' should be set");
        softly.assertThat(report.get(2))
                .hasFileName("/disk/meta-some/cppcheck-native/cppcheck.inc")
                .hasLineStart(1)
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("oelint.vars.summary80chars")
                .hasMessage("'SUMMARY' should not be longer than 80 characters");
        softly.assertThat(report.get(3))
                .hasFileName("/disk/meta-some/cppcheck-native/cppcheck.inc")
                .hasLineStart(4)
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("oelint.vars.homepageprefix")
                .hasMessage("'HOMEPAGE' should start with 'http://' or 'https://'");
        softly.assertThat(report.get(4))
                .hasFileName("/disk/meta-some/cppcheck-native/cppcheck.inc")
                .hasLineStart(28)
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("oelint.spaces.lineend")
                .hasMessage("Line shall not end with a space");
        softly.assertThat(report.get(5))
                .hasFileName("/disk/meta-some/cppcheck-native/cppcheck-native_1.87.bb")
                .hasLineStart(0)
                .hasSeverity(Severity.ERROR)
                .hasCategory("oelint.var.mandatoryvar.AUTHOR")
                .hasMessage("Variable 'AUTHOR' should be set");
        softly.assertThat(report.get(6))
                .hasFileName("/disk/meta-some/cppcheck-native/cppcheck.inc")
                .hasLineStart(26)
                .hasSeverity(Severity.ERROR)
                .hasCategory("oelint.task.nocopy")
                .hasMessage("'cp' shall not be used in do_install. Use 'install'");
        softly.assertThat(report.get(7))
                .hasFileName("/disk/meta-some/cppcheck-native/cppcheck.inc")
                .hasLineStart(12)
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("oelint.var.order.DEPENDS")
                .hasMessage("'DEPENDS' should be placed before 'inherit'");
    }

    @Override
    protected IssueParser createParser() {
        return new OeLintAdvParser();
    }
}
