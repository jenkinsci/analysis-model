package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.IssuesAssert.*;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link ClangParser}.
 *
 * @author Neil Davis
 */
public class ClangParserTest extends ParserTester {
    private static final String TYPE = new ClangParser().getId();

    /**
     * Parses a file with fatal error message.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-31936">Issue 31936</a>
     */
    @Test
    public void issue31936() {
        Issues<Issue> warnings = new ClangParser().parse(openFile("issue31936.txt"));

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(warnings).hasSize(1);
        softly.assertThat(warnings.get(0)).hasLineStart(1211)
                .hasLineEnd(1211)
                .hasColumnStart(26)
                .hasColumnEnd(26)
                .hasMessage("implicit conversion loses integer precision: 'NSInteger' (aka 'long') to 'int'")
                .hasFileName("/Volumes/workspace/MyApp/ViewController.m")
                .hasCategory("-Wshorten-64-to-32")
                .hasPriority(Priority.NORMAL);
        softly.assertAll();
    }

    /**
     * Parses a file with fatal error message.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-36817">Issue 36817</a>
     */
    @Test
    public void issue36817() {
        Issues<Issue> warnings = new ClangParser().parse(openFile("issue36817.txt"));

        assertThat(warnings).hasSize(0);
    }

    /**
     * Parses a file with fatal error message.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-18084">Issue 18084</a>
     */
    @Test
    public void issue18084() {
        Issues<Issue> warnings = new ClangParser().parse(openFile("issue18084.txt"));

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(warnings).hasSize(1);
        softly.assertThat(warnings.get(0)).hasLineStart(10)
                .hasLineEnd(10)
                .hasColumnStart(10)
                .hasColumnEnd(10)
                .hasMessage("'test.h' file not found")
                .hasFileName("./test.h")
                .hasCategory(DEFAULT_CATEGORY)
                .hasPriority(Priority.HIGH);
        softly.assertAll();
    }

    /**
     * Parses a file with one warning that are started by ant.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-14333">Issue 14333</a>
     */
    @Test
    public void issue14333() {
        Issues<Issue> warnings = new ClangParser().parse(openFile("issue14333.txt"));

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(warnings).hasSize(1);
        softly.assertThat(warnings.get(0)).hasLineStart(1518)
                .hasLineEnd(1518)
                .hasColumnStart(28)
                .hasColumnEnd(28)
                .hasMessage("Array access (via field 'yy_buffer_stack') results in a null pointer dereference")
                .hasFileName("scanner.cpp")
                .hasCategory(DEFAULT_CATEGORY)
                .hasPriority(Priority.NORMAL);
        softly.assertAll();
    }

    /**
     * Verifies that all messages are correctly parsed.
     */
    @Test
    public void testWarningsParser() {
        Issues<Issue> warnings = new ClangParser().parse(openFile());
        Iterator<Issue> iterator = warnings.iterator();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(warnings).hasSize(9);
        softly.assertThat(iterator.next()).hasLineStart(28)
                .hasLineEnd(28)
                .hasColumnStart(8)
                .hasColumnEnd(8)
                .hasMessage("extra tokens at end of #endif directive")
                .hasFileName("test.c")
                .hasType(TYPE)
                .hasCategory("-Wextra-tokens")
                .hasPriority(Priority.NORMAL);
        softly.assertThat(iterator.next()).hasLineStart(28)
                .hasLineEnd(28)
                .hasColumnStart(8)
                .hasColumnEnd(8)
                .hasMessage("extra tokens at end of #endif directive")
                .hasFileName("/path/to/test.c")
                .hasType(TYPE)
                .hasCategory("-Wextra-tokens")
                .hasPriority(Priority.NORMAL);
        softly.assertThat(iterator.next()).hasLineStart(128)
                .hasLineEnd(128)
                .hasMessage("extra tokens at end of #endif directive")
                .hasFileName("test.c")
                .hasType(TYPE)
                .hasCategory("-Wextra-tokens")
                .hasPriority(Priority.NORMAL);
        softly.assertThat(iterator.next()).hasLineStart(28)
                .hasLineEnd(28)
                .hasMessage("extra tokens at end of #endif directive")
                .hasFileName("test.c")
                .hasType(TYPE)
                .hasCategory(DEFAULT_CATEGORY)
                .hasPriority(Priority.NORMAL);
        softly.assertThat(iterator.next()).hasLineStart(3)
                .hasLineEnd(3)
                .hasColumnStart(11)
                .hasColumnEnd(11)
                .hasMessage("conversion specifies type 'char *' but the argument has type 'int'")
                .hasFileName("t.c")
                .hasType(TYPE)
                .hasCategory("-Wformat")
                .hasPriority(Priority.NORMAL);
        softly.assertThat(iterator.next()).hasLineStart(3)
                .hasLineEnd(3)
                .hasColumnStart(11)
                .hasColumnEnd(11)
                .hasMessage("conversion specifies type 'char *' but the argument has type 'int'")
                .hasFileName("t.c")
                .hasType(TYPE)
                .hasCategory("-Wformat,1")
                .hasPriority(Priority.NORMAL);
        softly.assertThat(iterator.next()).hasLineStart(3)
                .hasLineEnd(3)
                .hasColumnStart(11)
                .hasColumnEnd(11)
                .hasMessage("conversion specifies type 'char *' but the argument has type 'int'")
                .hasFileName("t.c")
                .hasType(TYPE)
                .hasCategory("-Wformat,Format String")
                .hasPriority(Priority.NORMAL);
        softly.assertThat(iterator.next()).hasLineStart(47)
                .hasLineEnd(47)
                .hasColumnStart(15)
                .hasColumnEnd(15)
                .hasMessage("invalid operands to binary expression ('int *' and '_Complex float')")
                .hasFileName("exprs.c")
                .hasType(TYPE)
                .hasCategory(DEFAULT_CATEGORY)
                .hasPriority(Priority.NORMAL);
        softly.assertThat(iterator.next()).hasLineStart(103)
                .hasLineEnd(103)
                .hasColumnStart(55)
                .hasColumnEnd(55)
                .hasMessage("passing 'uint8_t [11]' to parameter of type 'const char *' converts between pointers to integer types with different sign")
                .hasFileName("t.c")
                .hasType(TYPE)
                .hasCategory("-Wpointer-sign")
                .hasPriority(Priority.NORMAL);
        softly.assertAll();


    }

    @Override
    protected String getWarningsFile() {
        return "apple-llvm-clang.txt";
    }
}
