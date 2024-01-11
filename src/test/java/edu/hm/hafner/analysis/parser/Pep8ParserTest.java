package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

/**
 * Tests the class {@link Pep8Parser}.
 *
 * @author Marvin Schütz, J. Behrmann
 */
class Pep8ParserTest extends AbstractParserTest {
    Pep8ParserTest() {
        super("pep8Test.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(8);
        assertThatReportHasSeverities(report, 0, 0, 6, 2);

        softly.assertThat(report.get(0))
                .hasFileName("optparse.py")
                .hasCategory("E401")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("multiple imports on one line")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(69)
                .hasLineEnd(69)
                .hasColumnStart(11)
                .hasColumnEnd(11);

        softly.assertThat(report.get(1))
                .hasFileName("optparse.py")
                .hasCategory("E302")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("expected 2 blank lines, found 1")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(77)
                .hasLineEnd(77)
                .hasColumnStart(1)
                .hasColumnEnd(1);

        softly.assertThat(report.get(2))
                .hasFileName("optparse.py")
                .hasCategory("E301")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("expected 1 blank line, found 0")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(88)
                .hasLineEnd(88)
                .hasColumnStart(5)
                .hasColumnEnd(5);

        softly.assertThat(report.get(3))
                .hasFileName("optparse.py")
                .hasCategory("W602")
                .hasSeverity(Severity.WARNING_LOW)
                .hasMessage("deprecated form of raising exception")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(222)
                .hasLineEnd(222)
                .hasColumnStart(34)
                .hasColumnEnd(34);

        softly.assertThat(report.get(4))
                .hasFileName("optparse.py")
                .hasCategory("E211")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("whitespace before '('")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(347)
                .hasLineEnd(347)
                .hasColumnStart(31)
                .hasColumnEnd(31);

        softly.assertThat(report.get(5))
                .hasFileName("optparse.py")
                .hasCategory("E201")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("whitespace after '{'")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(357)
                .hasLineEnd(357)
                .hasColumnStart(17)
                .hasColumnEnd(17);

        softly.assertThat(report.get(6))
                .hasFileName("optparse.py")
                .hasCategory("E221")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("multiple spaces before operator")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(472)
                .hasLineEnd(472)
                .hasColumnStart(29)
                .hasColumnEnd(29);

        softly.assertThat(report.get(7))
                .hasFileName("optparse.py")
                .hasCategory("W601")
                .hasSeverity(Severity.WARNING_LOW)
                .hasMessage(".has_key() is deprecated, use 'in'")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(544)
                .hasLineEnd(544)
                .hasColumnStart(21)
                .hasColumnEnd(21);
    }

    @Override
    protected Pep8Parser createParser() {
        return new Pep8Parser();
    }
}
