package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertions.IssueSoftAssertions;
import edu.hm.hafner.analysis.assertions.IssuesSoftAssertions;

/**
 * Tests the class {@link Gcc4CompilerParser}.
 */
public class Gcc4CompilerParserTest extends ParserTester {
    private static final String THERE_ARE_WARNINGS_FOUND = "There are warnings found";
    private static final String WARNING_CATEGORY = "Warning";
    private static final String ERROR_CATEGORY = "Error";
    private static final String WARNING_TYPE = new Gcc4CompilerParser().getId();

    /**
     * Parses a file with one fatal error.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-18081">Issue 18081</a>
     */
    @Test
    public void issue18081() throws IOException {
        Issues warnings = new Gcc4CompilerParser().parse(openFile("issue18081.txt"));
        IssuesSoftAssertions softlyWarnings = new IssuesSoftAssertions();
        softlyWarnings.assertThat(warnings)
                .hasSize(1);
        softlyWarnings.assertAll();

        IssueSoftAssertions softlyIssue = new IssueSoftAssertions();
        softlyIssue.assertThat(warnings.iterator().next())
                .hasLineStart(10)
                .hasLineEnd(10)
                .hasMessage("'test.h' file not found")
                .hasFileName("./test.h")
                .hasType(WARNING_TYPE)
                .hasCategory(ERROR_CATEGORY)
                .hasPriority(Priority.HIGH);
        softlyIssue.assertAll();

    }

    /**
     * Parses a file with one warning that are started by ant.
     *
     * @throws IOException if the file could not be read
     * @sehttps://www.google.de/search?q=java+lock+priority&ei=OKjxWZXCN8LPwALPwbKgAQ&start=50&sa=N&biw=1280&bih=899e <a href="http://issues.jenkins-ci.org/browse/JENKINS-9926">Issue 9926</a>
     */
    @Test
    public void issue9926() throws IOException {
        Issues warnings = new Gcc4CompilerParser().parse(openFile("issue9926.txt"));

        IssuesSoftAssertions softlyWarnings = new IssuesSoftAssertions();
        softlyWarnings.assertThat(warnings)
                .hasSize(1);
        softlyWarnings.assertAll();

        IssueSoftAssertions softlyIssue = new IssueSoftAssertions();
        softlyIssue.assertThat(warnings.iterator().next())
                .hasLineStart(52)
                .hasLineEnd(52)
                .hasMessage("large integer implicitly truncated to unsigned type")
                .hasFileName("src/test_simple_sgs_message.cxx")
                .hasType(WARNING_TYPE)
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.NORMAL);
        softlyIssue.assertAll();
    }

    /**
     * Parses a warning log with 1 warning.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-6563">Issue 6563</a>
     */
    @Test
    public void issue6563() throws IOException {
        Issues warnings = new Gcc4CompilerParser().parse(openFile("issue6563.txt"));


        IssuesSoftAssertions softlyWarnings = new IssuesSoftAssertions();
        softlyWarnings.assertThat(warnings)
                .hasSize(10);
        softlyWarnings.assertAll();
    }

    /**
     * Parses a file with GCC warnings.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void testWarningsParser() throws IOException {
        Issues warnings = new Gcc4CompilerParser().parse(openFile());

        IssuesSoftAssertions softlyWarnings = new IssuesSoftAssertions();
        softlyWarnings.assertThat(warnings)
                .hasSize(14);
        softlyWarnings.assertAll();

        Iterator<Issue> iterator = warnings.iterator();

        IssueSoftAssertions softlyIssue1 = new IssueSoftAssertions();
        softlyIssue1.assertThat(iterator.next())
                .hasLineStart(451)
                .hasLineEnd(451)
                .hasMessage("'void yyunput(int, char*)' defined but not used")
                .hasFileName("testhist.l")
                .hasType(WARNING_TYPE)
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.NORMAL);
        softlyIssue1.assertAll();


        IssueSoftAssertions softlyIssue2 = new IssueSoftAssertions();
        softlyIssue2.assertThat(iterator.next())
                .hasLineStart(73)
                .hasLineEnd(73)
                .hasMessage("implicit typename is deprecated, please see the documentation for details")
                .hasFileName("/u1/drjohn/bfdist/packages/RegrTest/V00-03-01/RgtAddressLineScan.cc")
                .hasType(WARNING_TYPE)
                .hasCategory(ERROR_CATEGORY)
                .hasPriority(Priority.HIGH);
        softlyIssue2.assertAll();


        IssueSoftAssertions softlyIssue3 = new IssueSoftAssertions();
        softlyIssue3.assertThat(iterator.next())
                .hasLineStart(4)
                .hasLineEnd(4)
                .hasColumnStart(39)
                .hasColumnEnd(39)
                .hasMessage("foo.h: No such file or directory")
                .hasFileName("foo.cc")
                .hasType(WARNING_TYPE)
                .hasCategory(ERROR_CATEGORY)
                .hasPriority(Priority.HIGH);
        softlyIssue3.assertAll();

        IssueSoftAssertions softlyIssue4 = new IssueSoftAssertions();
        softlyIssue4.assertThat(iterator.next())
                .hasLineStart(678)
                .hasLineEnd(678)
                .hasMessage("missing initializer for member sigaltstack::ss_sp")
                .hasFileName("../../lib/linux-i686/include/boost/test/impl/execution_monitor.ipp")
                .hasType(WARNING_TYPE)
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.NORMAL);
        softlyIssue4.assertAll();


        IssueSoftAssertions softlyIssue5 = new IssueSoftAssertions();
        softlyIssue5.assertThat(iterator.next())
                .hasLineStart(678)
                .hasLineEnd(678)
                .hasMessage("missing initializer for member sigaltstack::ss_flags")
                .hasFileName("../../lib/linux-i686/include/boost/test/impl/execution_monitor.ipp")
                .hasType(WARNING_TYPE)
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.NORMAL);
        softlyIssue5.assertAll();

        IssueSoftAssertions softlyIssue6 = new IssueSoftAssertions();
        softlyIssue6.assertThat(iterator.next())
                .hasLineStart(678)
                .hasLineEnd(678)
                .hasMessage("missing initializer for member sigaltstack::ss_size")
                .hasFileName("../../lib/linux-i686/include/boost/test/impl/execution_monitor.ipp")
                .hasType(WARNING_TYPE)
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.NORMAL);
        softlyIssue6.assertAll();

        IssueSoftAssertions softlyIssue7 = new IssueSoftAssertions();
        softlyIssue7.assertThat(iterator.next())
                .hasLineStart(52)
                .hasLineEnd(52)
                .hasMessage("large integer implicitly truncated to unsigned type")
                .hasFileName("src/test_simple_sgs_message.cxx")
                .hasType(WARNING_TYPE)
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.NORMAL);
        softlyIssue7.assertAll();

        IssueSoftAssertions softlyIssue8 = new IssueSoftAssertions();
        softlyIssue8.assertThat(iterator.next())
                .hasLineStart(352)
                .hasLineEnd(352)
                .hasMessage("'s2.mepSector2::lubrications' may be used uninitialized in this function")
                .hasFileName("main/mep.cpp")
                .hasType(WARNING_TYPE)
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.NORMAL);
        softlyIssue8.assertAll();

        IssueSoftAssertions softlyIssue9 = new IssueSoftAssertions();
        softlyIssue9.assertThat(iterator.next())
                .hasLineStart(6)
                .hasLineEnd(6)
                .hasMessage("passing 'Test' chooses 'int' over 'unsigned int'")
                .hasFileName("warnings.cc")
                .hasType(WARNING_TYPE)
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.NORMAL);
        softlyIssue9.assertAll();


        IssueSoftAssertions softlyIssue10 = new IssueSoftAssertions();
        softlyIssue10.assertThat(iterator.next())
                .hasLineStart(6)
                .hasLineEnd(6)
                .hasMessage("in call to 'std::basic_ostream<_CharT, _Traits>& std::basic_ostream<_CharT, _Traits>::operator<<(int) [with _CharT = char, _Traits = std::char_traits<char>]'")
                .hasFileName("warnings.cc")
                .hasType(WARNING_TYPE)
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.NORMAL);
        softlyIssue10.assertAll();


        IssueSoftAssertions softlyIssue11 = new IssueSoftAssertions();
        softlyIssue11.assertThat(iterator.next())
                .hasLineStart(33)
                .hasLineEnd(33)
                .hasMessage("#warning This file includes at least one deprecated or antiquated header which may be removed without further notice at a future date. Please use a non-deprecated interface with equivalent functionality instead. For a listing of replacement headers and interfaces, consult the file backward_warning.h. To disable this warning use -Wno-deprecated.")
                .hasFileName("/usr/include/c++/4.3/backward/backward_warning.h")
                .hasType(WARNING_TYPE)
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.NORMAL);
        softlyIssue11.assertAll();

        IssueSoftAssertions softlyIssue12 = new IssueSoftAssertions();
        softlyIssue12.assertThat(iterator.next())
                .hasLineStart(8)
                .hasLineEnd(8)
                .hasMessage("'bar' was not declared in this scope")
                .hasFileName("fo:oo.cpp")
                .hasType(WARNING_TYPE)
                .hasCategory(ERROR_CATEGORY)
                .hasPriority(Priority.HIGH);
        softlyIssue12.assertAll();

        IssueSoftAssertions softlyIssue13 = new IssueSoftAssertions();
        softlyIssue13.assertThat(iterator.next())
                .hasLineStart(12)
                .hasLineEnd(12)
                .hasMessage("expected ';' before 'return'")
                .hasFileName("fo:oo.cpp")
                .hasType(WARNING_TYPE)
                .hasCategory(ERROR_CATEGORY)
                .hasPriority(Priority.HIGH);
        softlyIssue13.assertAll();


        IssueSoftAssertions softlyIssue14 = new IssueSoftAssertions();
        softlyIssue14.assertThat(iterator.next())
                .hasLineStart(23)
                .hasLineEnd(23)
                .hasMessage("unused variable 'j' [-Wunused-variable]")
                .hasFileName("warner.cpp")
                .hasType(WARNING_TYPE)
                .hasCategory("Warning:unused-variable")
                .hasPriority(Priority.NORMAL);
        softlyIssue14.assertAll();
    }

    /**
     * Parses a warning log with 10 template warnings.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-5606">Issue 5606</a>
     */
    @Test
    public void issue5606() throws IOException {
        Issues warnings = new Gcc4CompilerParser().parse(openFile("issue5606.txt"));

        IssuesSoftAssertions softlyWarnings = new IssuesSoftAssertions();
        softlyWarnings.assertThat(warnings)
                .hasSize(10);
        softlyWarnings.assertAll();
    }

    /**
     * Parses a warning log with multi line warnings.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-5605">Issue 5605</a>
     */
    @Test
    public void issue5605() throws IOException {
        Issues warnings = new Gcc4CompilerParser().parse(openFile("issue5605.txt"));

        IssuesSoftAssertions softlyWarnings = new IssuesSoftAssertions();
        softlyWarnings.assertThat(warnings)
                .hasSize(6);
        softlyWarnings.assertAll();
    }

    /**
     * Parses a warning log with multi line warnings.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-5445">Issue 5445</a>
     */
    @Test
    public void issue5445() throws IOException {
        Issues warnings = new Gcc4CompilerParser().parse(openFile("issue5445.txt"));

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
        Issues warnings = new Gcc4CompilerParser().parse(openFile("issue5870.txt"));

        IssuesSoftAssertions softlyWarnings = new IssuesSoftAssertions();
        softlyWarnings.assertThat(warnings)
                .hasSize(0);
        softlyWarnings.assertAll();
    }

    /**
     * Classify warnings by gcc 4.6 or later.
     *
     * @throws IOException if the file could not be read
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-11799">Issue 11799</a>
     */
    @Test
    public void issue11799() throws IOException {
        Issues warnings = new Gcc4CompilerParser().parse(openFile("issue11799.txt"));

        IssuesSoftAssertions softlyWarnings = new IssuesSoftAssertions();
        softlyWarnings.assertThat(warnings)
                .hasSize(4);
        softlyWarnings.assertAll();

        Iterator<Issue> iterator = warnings.iterator();


        IssueSoftAssertions softlyIssue1 = new IssueSoftAssertions();
        softlyIssue1.assertThat(iterator.next())
                .hasLineStart(4)
                .hasLineEnd(4)
                .hasMessage("implicit declaration of function 'undeclared_function' [-Wimplicit-function-declaration]")
                .hasFileName("gcc4warning.c")
                .hasType(WARNING_TYPE)
                .hasCategory(WARNING_CATEGORY + ":implicit-function-declaration")
                .hasPriority(Priority.NORMAL);
        softlyIssue1.assertAll();

        IssueSoftAssertions softlyIssue2 = new IssueSoftAssertions();
        softlyIssue2.assertThat(iterator.next())
                .hasLineStart(3)
                .hasLineEnd(3)
                .hasMessage("unused variable 'unused_local' [-Wunused-variable]")
                .hasFileName("gcc4warning.c")
                .hasType(WARNING_TYPE)
                .hasCategory(WARNING_CATEGORY + ":unused-variable")
                .hasPriority(Priority.NORMAL);
        softlyIssue2.assertAll();

        IssueSoftAssertions softlyIssue3 = new IssueSoftAssertions();
        softlyIssue3.assertThat(iterator.next())
                .hasLineStart(1)
                .hasLineEnd(1)
                .hasMessage("unused parameter 'unused_parameter' [-Wunused-parameter]")
                .hasFileName("gcc4warning.c")
                .hasType(WARNING_TYPE)
                .hasCategory(WARNING_CATEGORY + ":unused-parameter")
                .hasPriority(Priority.NORMAL);
        softlyIssue3.assertAll();


        IssueSoftAssertions softlyIssue4 = new IssueSoftAssertions();
        softlyIssue4.assertThat(iterator.next())
                .hasLineStart(5)
                .hasLineEnd(5)
                .hasMessage("control reaches end of non-void function [-Wreturn-type]")
                .hasFileName("gcc4warning.c")
                .hasType(WARNING_TYPE)
                .hasCategory(WARNING_CATEGORY + ":return-type")
                .hasPriority(Priority.NORMAL);
        softlyIssue3.assertAll();
    }

    @Override
    protected String getWarningsFile() {
        return "gcc4.txt";
    }

}

