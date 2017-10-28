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

// TODO

/**
 * Tests the class {@link GccParser}.
 */
public class GccParserTest extends ParserTester {
    private static final String TYPE = new GccParser().getId();
    private static final String GCC_ERROR = GccParser.GCC_ERROR;
    private static final String GCC_WARNING = "GCC warning";

    /**
     * Checks that a false positive is not reported anymore.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-34141">Issue 34141</a>
     */
    @Test
    public void issue34141() throws IOException {
        Issues warnings = new GccParser().parse(openFile("issue34141.txt"));


        IssuesSoftAssertions softlyWarnings = new IssuesSoftAssertions();
        softlyWarnings.assertThat(warnings)
                .hasSize(0);
        softlyWarnings.assertAll();
    }

    /**
     * Verifies that the message contains escaped XML characters.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-17309">Issue 17309</a>
     */
    @Test
    public void issue17309() throws IOException {
        Issues warnings = new GccParser().parse(openFile("issue17309.txt"));


        IssuesSoftAssertions softlyWarnings = new IssuesSoftAssertions();
        softlyWarnings.assertThat(warnings)
                .hasSize(1);
        softlyWarnings.assertAll();

        Issue annotation = warnings.iterator().next();
        IssueSoftAssertions softlyIssue1 = new IssueSoftAssertions();
        softlyIssue1.assertThat(annotation)
                .hasLineStart(4)
                .hasLineEnd(4)
                .hasMessage("dereferencing pointer &apos;&lt;anonymous&gt;&apos; does break strict-aliasing rules")
                .hasFileName("foo.cc")
                .hasType(TYPE)
                .hasCategory(GCC_ERROR)
                .hasPriority(Priority.HIGH);
        softlyIssue1.assertAll();
    }

    /**
     * Parses a file with one warning that are started by ant.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-9926">Issue 9926</a>
     */
    @Test
    public void issue9926() throws IOException {
        Issues warnings = new GccParser().parse(openFile("issue9926.txt"));


        IssuesSoftAssertions softlyWarnings = new IssuesSoftAssertions();
        softlyWarnings.assertThat(warnings)
                .hasSize(1);
        softlyWarnings.assertAll();

        Issue annotation = warnings.iterator().next();
        IssueSoftAssertions softlyIssue1 = new IssueSoftAssertions();
        softlyIssue1.assertThat(annotation)
                .hasLineStart(52)
                .hasLineEnd(52)
                .hasMessage("large integer implicitly truncated to unsigned type")
                .hasFileName("src/test_simple_sgs_message.cxx")
                .hasType(TYPE)
                .hasCategory(GCC_WARNING)
                .hasPriority(Priority.NORMAL);
        softlyIssue1.assertAll();
    }

    /**
     * Parses a file with two GCC warnings.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void testWarningsParser() throws IOException {
        Issues warnings = new GccParser().parse(openFile());

        IssuesSoftAssertions softlyWarnings = new IssuesSoftAssertions();
        softlyWarnings.assertThat(warnings)
                .hasSize(8);
        softlyWarnings.assertAll();

        Iterator<Issue> iterator = warnings.iterator();

        IssueSoftAssertions softlyIssue1 = new IssueSoftAssertions();
        softlyIssue1.assertThat(iterator.next())
                .hasLineStart(451)
                .hasLineEnd(451)
                .hasMessage("`void yyunput(int, char*)&apos; defined but not used")
                .hasFileName("testhist.l")
                .hasType(TYPE)
                .hasCategory(GCC_WARNING)
                .hasPriority(Priority.NORMAL);
        softlyIssue1.assertAll();

        IssueSoftAssertions softlyIssue2 = new IssueSoftAssertions();
        softlyIssue2.assertThat(iterator.next())
                .hasLineStart(73)
                .hasLineEnd(73)
                .hasMessage("implicit typename is deprecated, please see the documentation for details")
                .hasFileName("/u1/drjohn/bfdist/packages/RegrTest/V00-03-01/RgtAddressLineScan.cc")
                .hasType(TYPE)
                .hasCategory(GCC_ERROR)
                .hasPriority(Priority.HIGH);
        softlyIssue2.assertAll();

        IssueSoftAssertions softlyIssue3 = new IssueSoftAssertions();
        softlyIssue3.assertThat(iterator.next())
                .hasLineStart(4)
                .hasLineEnd(4)
                .hasMessage("foo.h: No such file or directory")
                .hasFileName("foo.cc")
                .hasType(TYPE)
                .hasCategory(GCC_ERROR)
                .hasPriority(Priority.HIGH);
        softlyIssue3.assertAll();

        IssueSoftAssertions softlyIssue4 = new IssueSoftAssertions();
        softlyIssue4.assertThat(iterator.next())
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage( "undefined reference to &apos;missing_symbol&apos;")
                .hasFileName( "foo.so")
                .hasType(TYPE)
                .hasCategory(GCC_ERROR)
                .hasPriority(Priority.HIGH);
        softlyIssue4.assertAll();

        IssueSoftAssertions softlyIssue5 = new IssueSoftAssertions();
        softlyIssue5.assertThat(iterator.next())
                .hasLineStart(678)
                .hasLineEnd(678)
                .hasMessage("missing initializer for member sigaltstack::ss_sp")
                .hasFileName("../../lib/linux-i686/include/boost/test/impl/execution_monitor.ipp")
                .hasType(TYPE)
                .hasCategory(GCC_WARNING)
                .hasPriority(Priority.NORMAL);
        softlyIssue5.assertAll();

        IssueSoftAssertions softlyIssue6 = new IssueSoftAssertions();
        softlyIssue6.assertThat(iterator.next())
                .hasLineStart(678)
                .hasLineEnd(678)
                .hasMessage("missing initializer for member sigaltstack::ss_flags")
                .hasFileName("../../lib/linux-i686/include/boost/test/impl/execution_monitor.ipp")
                .hasType(TYPE)
                .hasCategory(GCC_WARNING)
                .hasPriority(Priority.NORMAL);
        softlyIssue6.assertAll();

        IssueSoftAssertions softlyIssue7 = new IssueSoftAssertions();
        softlyIssue7.assertThat(iterator.next())
                .hasLineStart(678)
                .hasLineEnd(678)
                .hasMessage("missing initializer for member sigaltstack::ss_size")
                .hasFileName("../../lib/linux-i686/include/boost/test/impl/execution_monitor.ipp")
                .hasType(TYPE)
                .hasCategory(GCC_WARNING)
                .hasPriority(Priority.NORMAL);
        softlyIssue7.assertAll();

        IssueSoftAssertions softlyIssue8 = new IssueSoftAssertions();
        softlyIssue8.assertThat(iterator.next())
                .hasLineStart(52)
                .hasLineEnd(52)
                .hasMessage("large integer implicitly truncated to unsigned type")
                .hasFileName("src/test_simple_sgs_message.cxx")
                .hasType(TYPE)
                .hasCategory(GCC_WARNING)
                .hasPriority(Priority.NORMAL);
        softlyIssue8.assertAll();
    }

    /**
     * Parses a warning log with 2 new GCC warnings.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-3897">Issue 3897</a>
     */
    @Test
    public void issue3897and3898() throws IOException {
        Issues warnings = new GccParser().parse(openFile("issue3897.txt"));

        IssuesSoftAssertions softlyWarnings = new IssuesSoftAssertions();
        softlyWarnings.assertThat(warnings)
                .hasSize(3);
        softlyWarnings.assertAll();

        Iterator<Issue> iterator = warnings.iterator();

        IssueSoftAssertions softlyIssue1 = new IssueSoftAssertions();
        softlyIssue1.assertThat(iterator.next())
                .hasLineStart(12)
                .hasLineEnd(12)
                .hasMessage("file.h: No such file or directory")
                .hasFileName("/dir1/dir2/file.c")
                .hasType(TYPE)
                .hasCategory(GccParser.GCC_ERROR)
                .hasPriority(Priority.HIGH);
        softlyIssue1.assertAll();

        IssueSoftAssertions softlyIssue2 = new IssueSoftAssertions();
        softlyIssue2.assertThat(iterator.next())
                .hasLineStart(233)
                .hasLineEnd(233)
                .hasMessage("undefined reference to `MyInterface::getValue() const&apos;")
                .hasFileName("/dir1/dir3/file.cpp")
                .hasType(TYPE)
                .hasCategory(GccParser.GCC_ERROR)
                .hasPriority(Priority.HIGH);
        softlyIssue2.assertAll();

        IssueSoftAssertions softlyIssue3 = new IssueSoftAssertions();
        softlyIssue3.assertThat(iterator.next())
                .hasLineStart(20)
                .hasLineEnd(20)
                .hasMessage("invalid preprocessing directive #incldue")
                .hasFileName("/dir1/dir2/file.cpp")
                .hasType(TYPE)
                .hasCategory(GccParser.GCC_ERROR)
                .hasPriority(Priority.HIGH);
        softlyIssue3.assertAll();
    }

    /**
     * Parses a warning log with 2 GCC warnings, one of them a note.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-4712">Issue 4712</a>
     */
    @Test
    public void issue4712() throws IOException {
        Issues warnings = new GccParser().parse(openFile("issue4712.txt"));

        IssuesSoftAssertions softlyWarnings = new IssuesSoftAssertions();
        softlyWarnings.assertThat(warnings)
                .hasSize(2);
        softlyWarnings.assertAll();

        Iterator<Issue> iterator = warnings.iterator();

        IssueSoftAssertions softlyIssue1 = new IssueSoftAssertions();
        softlyIssue1.assertThat(iterator.next())
                .hasLineStart(352)
                .hasLineEnd(352)
                .hasMessage("&apos;s2.mepSector2::lubrications&apos; may be used")
                .hasFileName("main/mep.cpp")
                .hasType(TYPE)
                .hasCategory(GCC_WARNING)
                .hasPriority(Priority.NORMAL);
        softlyIssue1.assertAll();

        IssueSoftAssertions softlyIssue2 = new IssueSoftAssertions();
        softlyIssue2.assertThat(iterator.next())
                .hasLineStart(1477)
                .hasLineEnd(1477)
                .hasMessage("&apos;s2.mepSector2::lubrications&apos; was declared here")
                .hasFileName("main/mep.cpp")
                .hasType(TYPE)
                .hasCategory("GCC note")
                .hasPriority(Priority.LOW);
        softlyIssue2.assertAll();
    }

    /**
     * Parses a warning log with a ClearCase command line that should not be parsed as a warning.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-4712">Issue 4712</a>
     */
    @Test
    public void issue4700() throws IOException {
        Issues warnings = new GccParser().parse(openFile("issue4700.txt"));

        IssuesSoftAssertions softlyWarnings = new IssuesSoftAssertions();
        softlyWarnings.assertThat(warnings)
                .hasSize(0);
        softlyWarnings.assertAll();
    }

    /**
     * Parses a warning log with [exec] prefix.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-4712">Issue 4707</a>
     */
    @Test
    public void issue4707() throws IOException {
        Issues warnings = new GccParser().parse(openFile("issue4707.txt"));

        IssuesSoftAssertions softlyWarnings = new IssuesSoftAssertions();
        softlyWarnings.assertThat(warnings)
                .hasSize(22);
        softlyWarnings.assertAll();

        Iterator<Issue> iterator = warnings.iterator();
        IssueSoftAssertions softlyIssue = new IssueSoftAssertions();
        softlyIssue.assertThat(iterator.next())
                .hasLineStart(1128)
                .hasLineEnd(1128)
                .hasMessage("NULL used in arithmetic")
                .hasFileName("/Users/rthomson/hudson/jobs/Bryce7-MacWarnings/workspace/bryce7/src/Bryce/Plugins/3DSExport/3DSExport.cpp")
                .hasType(TYPE)
                .hasCategory(GCC_WARNING)
                .hasPriority(Priority.NORMAL);
        softlyIssue.assertAll();
    }

    /**
     * Parses a linker error.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-4010">Issue 4010</a>
     */
    @Test
    public void issue4010() throws IOException {
        Issues warnings = new GccParser().parse(openFile("issue4010.txt"));

        IssuesSoftAssertions softlyWarnings = new IssuesSoftAssertions();
        softlyWarnings.assertThat(warnings)
                .hasSize(1);
        softlyWarnings.assertAll();

        Iterator<Issue> iterator = warnings.iterator();
        IssueSoftAssertions softlyIssue = new IssueSoftAssertions();
        softlyIssue.assertThat(iterator.next())
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("cannot find -lMyLib")
                .hasFileName("MyLib")
                .hasType(TYPE)
                .hasCategory(GccParser.LINKER_ERROR)
                .hasPriority(Priority.HIGH);
        softlyIssue.assertAll();
    }

    /**
     * Parses a warning log with 6 new objective C warnings.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-4274">Issue 4274</a>
     */
    @Test
    public void issue4274() throws IOException {
        Issues warnings = new GccParser().parse(openFile("issue4274.txt"));

        IssuesSoftAssertions softlyWarnings = new IssuesSoftAssertions();
        softlyWarnings.assertThat(warnings)
                .hasSize(4);
        softlyWarnings.assertAll();

        Iterator<Issue> iterator = warnings.iterator();
        IssueSoftAssertions softlyIssue = new IssueSoftAssertions();
        softlyIssue.assertThat(iterator.next())
                .hasLineStart(638)
                .hasLineEnd(638)
                .hasMessage("local declaration of &quot;command&quot; hides instance variable")
                .hasFileName("folder1/file1.m")
                .hasType(TYPE)
                .hasCategory(GCC_WARNING)
                .hasPriority(Priority.NORMAL);
        softlyIssue.assertAll();

        IssueSoftAssertions softlyIssue1 = new IssueSoftAssertions();
        softlyIssue1.assertThat(iterator.next())
                .hasLineStart(640)
                .hasLineEnd(640)
                .hasMessage("instance variable &quot;command&quot; accessed in class method")
                .hasFileName("folder1/file1.m")
                .hasType(TYPE)
                .hasCategory(GCC_WARNING)
                .hasPriority(Priority.NORMAL);
        softlyIssue1.assertAll();

        IssueSoftAssertions softlyIssue2 = new IssueSoftAssertions();
        softlyIssue2.assertThat(iterator.next())
                .hasLineStart(47)
                .hasLineEnd(47)
                .hasMessage("&quot;oldGeb&quot; might be used uninitialized in this function")
                .hasFileName("file1.m")
                .hasType(TYPE)
                .hasCategory(GCC_WARNING)
                .hasPriority(Priority.NORMAL);
        softlyIssue2.assertAll();

        IssueSoftAssertions softlyIssue3 = new IssueSoftAssertions();
        softlyIssue3.assertThat(iterator.next())
                .hasLineStart(640)
                .hasLineEnd(640)
                .hasMessage("local declaration of &quot;command&quot; hides instance variable")
                .hasFileName("file1.m")
                .hasType(TYPE)
                .hasCategory(GCC_WARNING)
                .hasPriority(Priority.NORMAL);
        softlyIssue3.assertAll();
    }

    /**
     * Parses a file with one warning and matching warning that will be excluded afterwards.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-4260">Issue 4260</a>
     */
    @Test
    public void issue4260() throws IOException {
        Issues warnings = new GccParser().parse(openFile("issue4260.txt"));

        IssuesSoftAssertions softlyWarnings = new IssuesSoftAssertions();
        softlyWarnings.assertThat(warnings)
                .hasSize(1);
        softlyWarnings.assertAll();
    }

    @Override
    protected String getWarningsFile() {
        return "gcc.txt";
    }
}

