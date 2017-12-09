package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link Pep8Parser}.
 *
 * @author Marvin Schütz, J. Behrmann
 */
@SuppressWarnings("ReuseOfLocalVariable")
public class Pep8ParserTest extends ParserTester {
    /**
     * Parses a file with W and E warnings.
     */
    @Test
    public void testParseSimpleAndComplexMessage() {
        Issues<Issue> warnings = new Pep8Parser().parse(openFile());

        assertSoftly(softly -> {
            softly.assertThat(warnings).hasSize(8);
            softly.assertThat(warnings).hasNormalPrioritySize(6);
            softly.assertThat(warnings).hasLowPrioritySize(2);

            Iterator<Issue> iterator = warnings.iterator();

            Issue warning = iterator.next();
            softly.assertThat(warning)
                    .hasFileName("optparse.py")
                    .hasCategory("E401")
                    .hasPriority(Priority.NORMAL)
                    .hasMessage("multiple imports on one line")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(69)
                    .hasLineEnd(69)
                    .hasColumnStart(11)
                    .hasColumnEnd(11);

            warning = iterator.next();
            softly.assertThat(warning)
                    .hasFileName("optparse.py")
                    .hasCategory("E302")
                    .hasPriority(Priority.NORMAL)
                    .hasMessage("expected 2 blank lines, found 1")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(77)
                    .hasLineEnd(77)
                    .hasColumnStart(1)
                    .hasColumnEnd(1);

            warning = iterator.next();
            softly.assertThat(warning)
                    .hasFileName("optparse.py")
                    .hasCategory("E301")
                    .hasPriority(Priority.NORMAL)
                    .hasMessage("expected 1 blank line, found 0")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(88)
                    .hasLineEnd(88)
                    .hasColumnStart(5)
                    .hasColumnEnd(5);

            warning = iterator.next();
            softly.assertThat(warning)
                    .hasFileName("optparse.py")
                    .hasCategory("W602")
                    .hasPriority(Priority.LOW)
                    .hasMessage("deprecated form of raising exception")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(222)
                    .hasLineEnd(222)
                    .hasColumnStart(34)
                    .hasColumnEnd(34);

            warning = iterator.next();
            softly.assertThat(warning)
                    .hasFileName("optparse.py")
                    .hasCategory("E211")
                    .hasPriority(Priority.NORMAL)
                    .hasMessage("whitespace before '('")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(347)
                    .hasLineEnd(347)
                    .hasColumnStart(31)
                    .hasColumnEnd(31);

            warning = iterator.next();
            softly.assertThat(warning)
                    .hasFileName("optparse.py")
                    .hasCategory("E201")
                    .hasPriority(Priority.NORMAL)
                    .hasMessage("whitespace after '{'")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(357)
                    .hasLineEnd(357)
                    .hasColumnStart(17)
                    .hasColumnEnd(17);

            warning = iterator.next();
            softly.assertThat(warning)
                    .hasFileName("optparse.py")
                    .hasCategory("E221")
                    .hasPriority(Priority.NORMAL)
                    .hasMessage("multiple spaces before operator")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(472)
                    .hasLineEnd(472)
                    .hasColumnStart(29)
                    .hasColumnEnd(29);

            warning = iterator.next();
            softly.assertThat(warning)
                    .hasFileName("optparse.py")
                    .hasCategory("W601")
                    .hasPriority(Priority.LOW)
                    .hasMessage(".has_key() is deprecated, use 'in'")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(544)
                    .hasLineEnd(544)
                    .hasColumnStart(21)
                    .hasColumnEnd(21);
        });
    }

    @Override
    protected String getWarningsFile() {
        return "pep8Test.txt";
    }
}
