package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link DiabCParser}.
 */
public class DiabCParserTest extends ParserTester {
    private static final String TYPE = new DiabCParser().getId();

    /**
     * Parses a file with 13 warnings.
     */
    @Test
    public void parseDiabCpp() {
        Issues<Issue> warnings = new DiabCParser().parse(openFile());
        assertThat(warnings).hasSize(12).hasDuplicatesSize(1);

        SoftAssertions softly = new SoftAssertions();
        Iterator<Issue> iterator = warnings.iterator();
        softly.assertThat(iterator.next())
                .hasLineStart(7)
                .hasLineEnd(7)
                .hasMessage("missing return expression")
                .hasFileName("lint.c")
                .hasType(TYPE)
                .hasCategory("1521")
                .hasPriority(Priority.NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(22)
                .hasLineEnd(22)
                .hasMessage("narrowing or signed-to-unsigned type conversion found: int to unsigned char")
                .hasFileName("lint.c")
                .hasType(TYPE)
                .hasCategory("1643")
                .hasPriority(Priority.NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(28)
                .hasLineEnd(28)
                .hasMessage("constant out of range")
                .hasFileName("lint.c")
                .hasType(TYPE)
                .hasCategory("1243")
                .hasPriority(Priority.NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(4)
                .hasLineEnd(4)
                .hasMessage("function f4 is never used")
                .hasFileName("lint.c")
                .hasType(TYPE)
                .hasCategory("1517")
                .hasPriority(Priority.NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(11)
                .hasLineEnd(11)
                .hasMessage("function f5 is not found")
                .hasFileName("lint.c")
                .hasType(TYPE)
                .hasCategory("1378")
                .hasPriority(Priority.HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(5)
                .hasLineEnd(5)
                .hasMessage("division by zero")
                .hasFileName("main.c")
                .hasType(TYPE)
                .hasCategory("1025")
                .hasPriority(Priority.NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(5)
                .hasLineEnd(5)
                .hasMessage("division by zero")
                .hasFileName("main.c")
                .hasType(TYPE)
                .hasCategory("1025")
                .hasPriority(Priority.HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(976)
                .hasLineEnd(976)
                .hasMessage("function \"testing\" was declared but never referenced")
                .hasFileName("test.cpp")
                .hasType(TYPE)
                .hasCategory("4177")
                .hasPriority(Priority.NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(427)
                .hasLineEnd(427)
                .hasMessage("pointless comparison of unsigned integer with zero")
                .hasFileName("test.cpp")
                .hasType(TYPE)
                .hasCategory("4186")
                .hasPriority(Priority.NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(38)
                .hasLineEnd(38)
                .hasMessage("expected a \";\"")
                .hasFileName("test.cpp")
                .hasType(TYPE)
                .hasCategory("4065")
                .hasPriority(Priority.HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(443)
                .hasLineEnd(443)
                .hasMessage("external/internal linkage conflict with previous declaration")
                .hasFileName("test.cpp")
                .hasType(TYPE)
                .hasCategory("4172")
                .hasPriority(Priority.LOW);

        softly.assertThat(iterator.next())
                .hasLineStart(293)
                .hasLineEnd(293)
                .hasMessage("access control not specified (\"private\" by default)")
                .hasFileName("test.h")
                .hasType(TYPE)
                .hasCategory("4261")
                .hasPriority(Priority.LOW);

        softly.assertAll();
    }

    @Override
    protected String getWarningsFile() {
        return "diabc.txt";
    }
}

