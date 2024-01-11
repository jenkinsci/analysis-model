package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

/**
 * Tests the class {@link DiabCParser}.
 */
class DiabCParserTest extends AbstractParserTest {
    /**
     * Creates a new instance of {@link DiabCParserTest}.
     */
    protected DiabCParserTest() {
        super("diabc.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(12)
                .hasDuplicatesSize(1);
        Iterator<Issue> iterator = report.iterator();

        softly.assertThat(iterator.next())
                .hasLineStart(7)
                .hasLineEnd(7)
                .hasMessage("missing return expression")
                .hasFileName("lint.c")
                .hasCategory("1521")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(22)
                .hasLineEnd(22)
                .hasMessage("narrowing or signed-to-unsigned type conversion found: int to unsigned char")
                .hasFileName("lint.c")
                .hasCategory("1643")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(28)
                .hasLineEnd(28)
                .hasMessage("constant out of range")
                .hasFileName("lint.c")
                .hasCategory("1243")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(4)
                .hasLineEnd(4)
                .hasMessage("function f4 is never used")
                .hasFileName("lint.c")
                .hasCategory("1517")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(11)
                .hasLineEnd(11)
                .hasMessage("function f5 is not found")
                .hasFileName("lint.c")
                .hasCategory("1378")
                .hasSeverity(Severity.WARNING_HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(5)
                .hasLineEnd(5)
                .hasMessage("division by zero")
                .hasFileName("main.c")
                .hasCategory("1025")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(5)
                .hasLineEnd(5)
                .hasMessage("division by zero")
                .hasFileName("main.c")
                .hasCategory("1025")
                .hasSeverity(Severity.WARNING_HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(976)
                .hasLineEnd(976)
                .hasMessage("function \"testing\" was declared but never referenced")
                .hasFileName("test.cpp")
                .hasCategory("4177")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(427)
                .hasLineEnd(427)
                .hasMessage("pointless comparison of unsigned integer with zero")
                .hasFileName("test.cpp")
                .hasCategory("4186")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(38)
                .hasLineEnd(38)
                .hasMessage("expected a \";\"")
                .hasFileName("test.cpp")
                .hasCategory("4065")
                .hasSeverity(Severity.WARNING_HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(443)
                .hasLineEnd(443)
                .hasMessage("external/internal linkage conflict with previous declaration")
                .hasFileName("test.cpp")
                .hasCategory("4172")
                .hasSeverity(Severity.WARNING_LOW);

        softly.assertThat(iterator.next())
                .hasLineStart(293)
                .hasLineEnd(293)
                .hasMessage("access control not specified (\"private\" by default)")
                .hasFileName("test.h")
                .hasCategory("4261")
                .hasSeverity(Severity.WARNING_LOW);

        softly.assertAll();
    }

    @Override
    protected DiabCParser createParser() {
        return new DiabCParser();
    }
}

