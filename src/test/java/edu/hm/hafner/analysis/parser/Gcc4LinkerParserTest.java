package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link Gcc4LinkerParser}.
 *
 * @author Frederic Chateau
 * @author Raphael Furch
 */
public class Gcc4LinkerParserTest extends AbstractParserTest {
    private static final String WARNING_CATEGORY = Gcc4LinkerParser.WARNING_CATEGORY;
    private static final String FILE_NAME = "-";

    protected Gcc4LinkerParserTest() {
        super("gcc4ld.txt");
    }

    @Override
    protected AbstractParser createParser() {
        return new Gcc4LinkerParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Issues<Issue> issues, final SoftAssertions softly) {
        softly.assertThat(issues).hasSize(7).hasDuplicatesSize(1);

        Iterator<Issue> iterator = issues.iterator();

        softly.assertThat(iterator.next())
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("undefined reference to 'missing_symbol'")
                .hasFileName("foo.so")
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(233)
                .hasLineEnd(233)
                .hasMessage("undefined reference to `MyInterface::getValue() const'")
                .hasFileName("/dir1/dir3/file.cpp")
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("cannot find -lMyLib")
                .hasFileName(FILE_NAME)
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("undefined reference to `clock_gettime'")
                .hasFileName("foo")
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(109)
                .hasLineEnd(109)
                .hasMessage("undefined reference to `main'")
                .hasFileName("/build/buildd/eglibc-2.10.1/csu/../sysdeps/x86_64/elf/start.S")
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(7)
                .hasLineEnd(7)
                .hasMessage("undefined reference to `clock_gettime'")
                .hasFileName("/home/me/foo.cpp")
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("errno: TLS definition in /lib/libc.so.6 section .tbss mismatches non-TLS reference in /tmp/ccgdbGtN.o")
                .hasFileName(FILE_NAME)
                .hasPriority(Priority.HIGH);

    }

    /** Should not report warnings already detected by {@link Gcc4CompilerParser}. */
    @Test
    public void shouldNotReportGccWarnings() {
        Issues<Issue> warnings = parse("gcc4.txt");

        assertThat(warnings).hasSize(2);
        assertThatMessageHasUndefinedReference(warnings, 0);
        assertThatMessageHasUndefinedReference(warnings, 1);
    }

    private void assertThatMessageHasUndefinedReference(final Issues<Issue> warnings, final int index) {
        assertThat(warnings.get(index).getMessage()).startsWith("undefined reference to");
    }

    /**
     * Parses a warning log with multi line warnings.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-5445">Issue 5445</a>
     */
    @Test
    public void issue5445() {
        Issues<Issue> warnings = parse("issue5445.txt");

        assertThat(warnings).isEmpty();
    }

    /**
     * Parses a warning log with autoconf messages. There should be no warning.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-5870">Issue 5870</a>
     */
    @Test
    public void issue5870() {
        Issues<Issue> warnings = parse("issue5870.txt");

        assertThat(warnings).isEmpty();
    }

    /**
     * Parses a warning log with 1 warning.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-6563">Issue 6563</a>
     */
    @Test
    public void issue6563() {
        Issues<Issue> warnings = parse("issue6563.txt");

        assertThat(warnings).isEmpty();
    }
}

