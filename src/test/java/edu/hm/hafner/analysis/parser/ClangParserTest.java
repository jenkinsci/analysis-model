package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link ClangParser}.
 *
 * @author Neil Davis
 */
class ClangParserTest extends AbstractParserTest {
    ClangParserTest() {
        super("apple-llvm-clang.txt");
    }

    @Override
    protected ClangParser createParser() {
        return new ClangParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        Iterator<Issue> iterator = report.iterator();

        softly.assertThat(report).hasSize(9);
        softly.assertThat(iterator.next()).hasLineStart(28)
                .hasLineEnd(28)
                .hasColumnStart(8)
                .hasColumnEnd(8)
                .hasMessage("extra tokens at end of #endif directive")
                .hasFileName("test.c")
                .hasCategory("-Wextra-tokens")
                .hasSeverity(Severity.WARNING_NORMAL);
        softly.assertThat(iterator.next()).hasLineStart(28)
                .hasLineEnd(28)
                .hasColumnStart(8)
                .hasColumnEnd(8)
                .hasMessage("extra tokens at end of #endif directive")
                .hasFileName("/path/to/test.c")
                .hasCategory("-Wextra-tokens")
                .hasSeverity(Severity.WARNING_NORMAL);
        softly.assertThat(iterator.next()).hasLineStart(128)
                .hasLineEnd(128)
                .hasMessage("extra tokens at end of #endif directive")
                .hasFileName("test.c")
                .hasCategory("-Wextra-tokens")
                .hasSeverity(Severity.WARNING_NORMAL);
        softly.assertThat(iterator.next()).hasLineStart(28)
                .hasLineEnd(28)
                .hasMessage("extra tokens at end of #endif directive")
                .hasFileName("test.c")
                .hasCategory(DEFAULT_CATEGORY)
                .hasSeverity(Severity.WARNING_NORMAL);
        softly.assertThat(iterator.next()).hasLineStart(3)
                .hasLineEnd(3)
                .hasColumnStart(11)
                .hasColumnEnd(11)
                .hasMessage("conversion specifies type 'char *' but the argument has type 'int'")
                .hasFileName("t.c")
                .hasCategory("-Wformat")
                .hasSeverity(Severity.WARNING_NORMAL);
        softly.assertThat(iterator.next()).hasLineStart(3)
                .hasLineEnd(3)
                .hasColumnStart(11)
                .hasColumnEnd(11)
                .hasMessage("conversion specifies type 'char *' but the argument has type 'int'")
                .hasFileName("t.c")
                .hasCategory("-Wformat,1")
                .hasSeverity(Severity.WARNING_NORMAL);
        softly.assertThat(iterator.next()).hasLineStart(3)
                .hasLineEnd(3)
                .hasColumnStart(11)
                .hasColumnEnd(11)
                .hasMessage("conversion specifies type 'char *' but the argument has type 'int'")
                .hasFileName("t.c")
                .hasCategory("-Wformat,Format String")
                .hasSeverity(Severity.WARNING_NORMAL);
        softly.assertThat(iterator.next()).hasLineStart(47)
                .hasLineEnd(47)
                .hasColumnStart(15)
                .hasColumnEnd(15)
                .hasMessage("invalid operands to binary expression ('int *' and '_Complex float')")
                .hasFileName("exprs.c")
                .hasCategory(DEFAULT_CATEGORY)
                .hasSeverity(Severity.WARNING_NORMAL);
        softly.assertThat(iterator.next()).hasLineStart(103)
                .hasLineEnd(103)
                .hasColumnStart(55)
                .hasColumnEnd(55)
                .hasMessage(
                        "passing 'uint8_t [11]' to parameter of type 'const char *' converts between pointers to integer types with different sign")
                .hasFileName("t.c")
                .hasCategory("-Wpointer-sign")
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    /**
     * Parses a file with fatal error message.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-31936">Issue 31936</a>
     */
    @Test
    void issue31936() {
        Report warnings = parse("issue31936.txt");

        assertThat(warnings).hasSize(1);

        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0)).hasLineStart(1211)
                    .hasLineEnd(1211)
                    .hasColumnStart(26)
                    .hasColumnEnd(26)
                    .hasMessage("implicit conversion loses integer precision: 'NSInteger' (aka 'long') to 'int'")
                    .hasFileName("/Volumes/workspace/MyApp/ViewController.m")
                    .hasCategory("-Wshorten-64-to-32")
                    .hasSeverity(Severity.WARNING_NORMAL);
        }
    }

    /**
     * Parses a file with test results. There should be no warning.
     *
     * @see <a href="https://wiki.jenkins.io/display/JENKINS/Warnings+Plugin?focusedCommentId=138447465#comment-138447465">Wiki
     *         Report</a>
     */
    @Test
    void shouldNotDetectTestResults() {
        Report warnings = parse("timestamps.log");

        assertThat(warnings).isEmpty();
    }

    /**
     * Parses a file with fatal error message.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-36817">Issue 36817</a>
     */
    @Test
    void issue36817() {
        Report warnings = parse("issue36817.txt");

        assertThat(warnings).isEmpty();
    }

    /**
     * Parses a file with fatal error message.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-18084">Issue 18084</a>
     */
    @Test
    void issue18084() {
        Report warnings = parse("issue18084.txt");

        assertThat(warnings).hasSize(1);

        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0)).hasLineStart(10)
                    .hasLineEnd(10)
                    .hasColumnStart(10)
                    .hasColumnEnd(10)
                    .hasMessage("'test.h' file not found")
                    .hasFileName("./test.h")
                    .hasCategory(DEFAULT_CATEGORY)
                    .hasSeverity(Severity.WARNING_HIGH);
        }
    }

    /**
     * Parses a file with one warning that are started by ant.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-14333">Issue 14333</a>
     */
    @Test
    void issue14333() {
        Report warnings = parse("issue14333.txt");

        assertThat(warnings).hasSize(1);

        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0)).hasLineStart(1518)
                    .hasLineEnd(1518)
                    .hasColumnStart(28)
                    .hasColumnEnd(28)
                    .hasMessage("Array access (via field 'yy_buffer_stack') results in a null pointer dereference")
                    .hasFileName("scanner.cpp")
                    .hasCategory(DEFAULT_CATEGORY)
                    .hasSeverity(Severity.WARNING_NORMAL);
        }
    }

    @Test
    void assertThatClangPathsAreCorrectForAllPlatforms() {
        Report warnings = parse("llvm-clang.txt");

        assertThat(warnings).hasSize(3);

        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0)).hasLineStart(35)
                    .hasLineEnd(35)
                    .hasColumnStart(15)
                    .hasColumnEnd(15)
                    .hasMessage("unused parameter 'parameter1'")
                    .hasFileName("/project/src/cpp/MyClass.cpp")
                    .hasCategory("-Wunused-parameter")
                    .hasSeverity(Severity.WARNING_NORMAL);
            softly.assertThat(warnings.get(1)).hasLineStart(35)
                    .hasLineEnd(35)
                    .hasColumnStart(15)
                    .hasColumnEnd(15)
                    .hasMessage("unused parameter 'parameter1'")
                    .hasFileName("C:/project/src/cpp/MyClass.cpp")
                    .hasCategory("-Wunused-parameter")
                    .hasSeverity(Severity.WARNING_NORMAL);
            softly.assertThat(warnings.get(2)).hasLineStart(35)
                    .hasLineEnd(35)
                    .hasColumnStart(15)
                    .hasColumnEnd(15)
                    .hasMessage("unused parameter 'parameter1'")
                    .hasFileName("MyClass.cpp")
                    .hasCategory("-Wunused-parameter")
                    .hasSeverity(Severity.WARNING_NORMAL);
        }
    }
}
