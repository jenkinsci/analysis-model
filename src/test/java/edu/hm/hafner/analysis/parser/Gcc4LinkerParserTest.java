package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import static edu.hm.hafner.analysis.assertj.IssuesAssert.*;

/**
 * Tests the class {@link Gcc4LinkerParser}.
 *
 * @author Raphael Furch
 */
public class Gcc4LinkerParserTest extends ParserTester {
    private static final String WARNING_CATEGORY = Gcc4LinkerParser.WARNING_CATEGORY;
    private static final String WARNING_TYPE = new Gcc4LinkerParser().getId();
    private static final String FILE_NAME = "-";

    /**
     * Parses a file with GCC linker errors.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void testWarningsParser() throws IOException {
        Issues<Issue> warnings = new Gcc4LinkerParser().parse(openFile());

        assertThat(warnings)
                .hasSize(8);

        Iterator<Issue> iterator = warnings.iterator();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(iterator.next())
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("undefined reference to 'missing_symbol'")
                .hasFileName("foo.so")
                .hasType(WARNING_TYPE)
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(233)
                .hasLineEnd(233)
                .hasMessage("undefined reference to `MyInterface::getValue() const'")
                .hasFileName("/dir1/dir3/file.cpp")
                .hasType(WARNING_TYPE)
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("cannot find -lMyLib")
                .hasFileName(FILE_NAME)
                .hasType(WARNING_TYPE)
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("undefined reference to `clock_gettime'")
                .hasFileName("foo")
                .hasType(WARNING_TYPE)
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(109)
                .hasLineEnd(109)
                .hasMessage("undefined reference to `main'")
                .hasFileName("/build/buildd/eglibc-2.10.1/csu/../sysdeps/x86_64/elf/start.S")
                .hasType(WARNING_TYPE)
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.HIGH);


        softly.assertThat(iterator.next())
                .hasLineStart(109)
                .hasLineEnd(109)
                .hasMessage("undefined reference to `main'")
                .hasFileName("/build/buildd/eglibc-2.10.1/csu/../sysdeps/x86_64/elf/start.S")
                .hasType(WARNING_TYPE)
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(7)
                .hasLineEnd(7)
                .hasMessage("undefined reference to `clock_gettime'")
                .hasFileName("/home/me/foo.cpp")
                .hasType(WARNING_TYPE)
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("errno: TLS definition in /lib/libc.so.6 section .tbss mismatches non-TLS reference in /tmp/ccgdbGtN.o")
                .hasFileName(FILE_NAME)
                .hasType(WARNING_TYPE)
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.HIGH);
        softly.assertAll();
    }


    /**
     * Parses a warning log with multi line warnings.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-5445">Issue 5445</a>
     */
    @Test
    public void issue5445() throws IOException {
        Issues<Issue> warnings = new Gcc4LinkerParser().parse(openFile("issue5445.txt"));

        assertThat(warnings)
                .hasSize(0);
    }

    /**
     * Parses a warning log with autoconf messages. There should be no warning.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-5870">Issue 5870</a>
     */
    @Test
    public void issue5870() throws IOException {
        Issues<Issue> warnings = new Gcc4LinkerParser().parse(openFile("issue5870.txt"));

        assertThat(warnings)
                .hasSize(0);
    }

    /**
     * Parses a warning log with 1 warning.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-6563">Issue 6563</a>
     */
    @Test
    public void issue6563() throws IOException {
        Issues<Issue> warnings = new Gcc4LinkerParser().parse(openFile("issue6563.txt"));

        assertThat(warnings)
                .hasSize(0);
    }

    @Override
    protected String getWarningsFile() {
        return "gcc4ld.txt";
    }
}

