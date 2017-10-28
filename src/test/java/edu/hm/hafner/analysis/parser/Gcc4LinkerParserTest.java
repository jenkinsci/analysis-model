package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.edu.hm.hafner.analysis.edu.hm.hafner.analysis.assertions.IssueSoftAssertions;
import edu.hm.hafner.edu.hm.hafner.analysis.edu.hm.hafner.analysis.assertions.IssuesSoftAssertions;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the class {@link Gcc4LinkerParser}.
 */
public class Gcc4LinkerParserTest extends ParserTester {
    private static final String WARNING_CATEGORY = Gcc4LinkerParser.WARNING_CATEGORY;
    private static final String WARNING_TYPE = new Gcc4LinkerParser().getId();
    private static final String THERE_ARE_WARNINGS_FOUND = "There are warnings found";
    private static final String FILE_NAME = "-";

    /**
     * Parses a file with GCC linker errors.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void testWarningsParser() throws IOException {
        Issues warnings = new Gcc4LinkerParser().parse(openFile());

        IssuesSoftAssertions softlyWarnings = new IssuesSoftAssertions();
        softlyWarnings.assertThat(warnings)
                .hasSize(8);
        softlyWarnings.assertAll();

        Iterator<Issue> iterator = warnings.iterator();

        IssueSoftAssertions softlyIssue1 = new IssueSoftAssertions();
        softlyIssue1.assertThat(iterator.next())
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("undefined reference to 'missing_symbol'")
                .hasFileName("foo.so")
                .hasType(WARNING_TYPE)
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.HIGH);
        softlyIssue1.assertAll();

        IssueSoftAssertions softlyIssue2 = new IssueSoftAssertions();
        softlyIssue2.assertThat(iterator.next())
                .hasLineStart(233)
                .hasLineEnd(233)
                .hasMessage("undefined reference to `MyInterface::getValue() const'")
                .hasFileName("/dir1/dir3/file.cpp")
                .hasType(WARNING_TYPE)
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.HIGH);
        softlyIssue2.assertAll();

        IssueSoftAssertions softlyIssue3 = new IssueSoftAssertions();
        softlyIssue3.assertThat(iterator.next())
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("cannot find -lMyLib")
                .hasFileName(FILE_NAME)
                .hasType(WARNING_TYPE)
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.HIGH);
        softlyIssue3.assertAll();

        IssueSoftAssertions softlyIssue4 = new IssueSoftAssertions();
        softlyIssue4.assertThat(iterator.next())
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("undefined reference to `clock_gettime'")
                .hasFileName("foo")
                .hasType(WARNING_TYPE)
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.HIGH);
        softlyIssue4.assertAll();


        IssueSoftAssertions softlyIssue5 = new IssueSoftAssertions();
        softlyIssue5.assertThat(iterator.next())
                .hasLineStart(109)
                .hasLineEnd(109)
                .hasMessage("undefined reference to `main'")
                .hasFileName("/build/buildd/eglibc-2.10.1/csu/../sysdeps/x86_64/elf/start.S")
                .hasType(WARNING_TYPE)
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.HIGH);
        softlyIssue5.assertAll();

        IssueSoftAssertions softlyIssue6 = new IssueSoftAssertions();
        softlyIssue6.assertThat(iterator.next())
                .hasLineStart(109)
                .hasLineEnd(109)
                .hasMessage("undefined reference to `main'")
                .hasFileName("/build/buildd/eglibc-2.10.1/csu/../sysdeps/x86_64/elf/start.S")
                .hasType(WARNING_TYPE)
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.HIGH);
        softlyIssue6.assertAll();

        IssueSoftAssertions softlyIssue7 = new IssueSoftAssertions();
        softlyIssue7.assertThat(iterator.next())
                .hasLineStart(7)
                .hasLineEnd(7)
                .hasMessage("undefined reference to `clock_gettime'")
                .hasFileName("/home/me/foo.cpp")
                .hasType(WARNING_TYPE)
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.HIGH);
        softlyIssue7.assertAll();

        IssueSoftAssertions softlyIssue8 = new IssueSoftAssertions();
        softlyIssue8.assertThat(iterator.next())
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("errno: TLS definition in /lib/libc.so.6 section .tbss mismatches non-TLS reference in /tmp/ccgdbGtN.o")
                .hasFileName(FILE_NAME)
                .hasType(WARNING_TYPE)
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.HIGH);
        softlyIssue8.assertAll();
    }


    /**
     * Parses a warning log with multi line warnings.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-5445">Issue 5445</a>
     */
    @Test
    public void issue5445() throws IOException {
        Issues warnings = new Gcc4LinkerParser().parse(openFile("issue5445.txt"));

        IssuesSoftAssertions softlyWarnings = new IssuesSoftAssertions();
        softlyWarnings.assertThat(warnings)
                .hasSize(0);
        softlyWarnings.assertAll();
    }

    /**
     * Parses a warning log with autoconf messages. There should be no warning.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-5870">Issue 5870</a>
     */
    @Test
    public void issue5870() throws IOException {
        Issues warnings = new Gcc4LinkerParser().parse(openFile("issue5870.txt"));

        IssuesSoftAssertions softlyWarnings = new IssuesSoftAssertions();
        softlyWarnings.assertThat(warnings)
                .hasSize(0);
        softlyWarnings.assertAll();
    }

    /**
     * Parses a warning log with 1 warning.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-6563">Issue 6563</a>
     */
    @Test
    public void issue6563() throws IOException {
        Issues warnings = new Gcc4LinkerParser().parse(openFile("issue6563.txt"));

        IssuesSoftAssertions softlyWarnings = new IssuesSoftAssertions();
        softlyWarnings.assertThat(warnings)
                .hasSize(0);
        softlyWarnings.assertAll();
    }

    @Override
    protected String getWarningsFile() {
        return "gcc4ld.txt";
    }
}

