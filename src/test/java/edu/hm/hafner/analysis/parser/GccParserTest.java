package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.IssuesAssert;
import edu.hm.hafner.analysis.assertj.SoftAssertions;


/**
 * Tests the class {@link GccParser}.
 *
 * @author Raphael Furch
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

        IssuesAssert.assertThat(warnings)
                .hasSize(0);
    }

    /**
     * Verifies that the message contains escaped XML characters.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-17309">Issue 17309</a>
     */
    @Test
    public void issue17309() throws IOException {
        Issues warnings = new GccParser().parse(openFile("issue17309.txt"));

        IssuesAssert.assertThat(warnings)
                .hasSize(1);

        Issue annotation = warnings.iterator().next();
        SoftAssertions softly1 = new SoftAssertions();
        softly1.assertThat(annotation)
                .hasLineStart(4)
                .hasLineEnd(4)
                .hasMessage("dereferencing pointer &apos;&lt;anonymous&gt;&apos; does break strict-aliasing rules")
                .hasFileName("foo.cc")
                .hasType(TYPE)
                .hasCategory(GCC_ERROR)
                .hasPriority(Priority.HIGH);
        softly1.assertAll();
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

        IssuesAssert.assertThat(warnings)
                .hasSize(1);

        Issue annotation = warnings.iterator().next();
        SoftAssertions softly1 = new SoftAssertions();
        softly1.assertThat(annotation)
                .hasLineStart(52)
                .hasLineEnd(52)
                .hasMessage("large integer implicitly truncated to unsigned type")
                .hasFileName("src/test_simple_sgs_message.cxx")
                .hasType(TYPE)
                .hasCategory(GCC_WARNING)
                .hasPriority(Priority.NORMAL);
        softly1.assertAll();
    }

    /**
     * Parses a file with two GCC warnings.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void testWarningsParser() throws IOException {
        Issues warnings = new GccParser().parse(openFile());

        IssuesAssert.assertThat(warnings)
                .hasSize(8);

        Iterator<Issue> iterator = warnings.iterator();

        SoftAssertions softly1 = new SoftAssertions();
        softly1.assertThat(iterator.next())
                .hasLineStart(451)
                .hasLineEnd(451)
                .hasMessage("`void yyunput(int, char*)&apos; defined but not used")
                .hasFileName("testhist.l")
                .hasType(TYPE)
                .hasCategory(GCC_WARNING)
                .hasPriority(Priority.NORMAL);
        softly1.assertAll();

        SoftAssertions softly2 = new SoftAssertions();
        softly2.assertThat(iterator.next())
                .hasLineStart(73)
                .hasLineEnd(73)
                .hasMessage("implicit typename is deprecated, please see the documentation for details")
                .hasFileName("/u1/drjohn/bfdist/packages/RegrTest/V00-03-01/RgtAddressLineScan.cc")
                .hasType(TYPE)
                .hasCategory(GCC_ERROR)
                .hasPriority(Priority.HIGH);
        softly2.assertAll();

        SoftAssertions softly3 = new SoftAssertions();
        softly3.assertThat(iterator.next())
                .hasLineStart(4)
                .hasLineEnd(4)
                .hasMessage("foo.h: No such file or directory")
                .hasFileName("foo.cc")
                .hasType(TYPE)
                .hasCategory(GCC_ERROR)
                .hasPriority(Priority.HIGH);
        softly3.assertAll();

        SoftAssertions softly4 = new SoftAssertions();
        softly4.assertThat(iterator.next())
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage( "undefined reference to &apos;missing_symbol&apos;")
                .hasFileName( "foo.so")
                .hasType(TYPE)
                .hasCategory(GCC_ERROR)
                .hasPriority(Priority.HIGH);
        softly4.assertAll();

        SoftAssertions softly5 = new SoftAssertions();
        softly5.assertThat(iterator.next())
                .hasLineStart(678)
                .hasLineEnd(678)
                .hasMessage("missing initializer for member sigaltstack::ss_sp")
                .hasFileName("../../lib/linux-i686/include/boost/test/impl/execution_monitor.ipp")
                .hasType(TYPE)
                .hasCategory(GCC_WARNING)
                .hasPriority(Priority.NORMAL);
        softly5.assertAll();

        SoftAssertions softly6 = new SoftAssertions();
        softly6.assertThat(iterator.next())
                .hasLineStart(678)
                .hasLineEnd(678)
                .hasMessage("missing initializer for member sigaltstack::ss_flags")
                .hasFileName("../../lib/linux-i686/include/boost/test/impl/execution_monitor.ipp")
                .hasType(TYPE)
                .hasCategory(GCC_WARNING)
                .hasPriority(Priority.NORMAL);
        softly6.assertAll();

        SoftAssertions softly7 = new SoftAssertions();
        softly7.assertThat(iterator.next())
                .hasLineStart(678)
                .hasLineEnd(678)
                .hasMessage("missing initializer for member sigaltstack::ss_size")
                .hasFileName("../../lib/linux-i686/include/boost/test/impl/execution_monitor.ipp")
                .hasType(TYPE)
                .hasCategory(GCC_WARNING)
                .hasPriority(Priority.NORMAL);
        softly7.assertAll();

        SoftAssertions softly8 = new SoftAssertions();
        softly8.assertThat(iterator.next())
                .hasLineStart(52)
                .hasLineEnd(52)
                .hasMessage("large integer implicitly truncated to unsigned type")
                .hasFileName("src/test_simple_sgs_message.cxx")
                .hasType(TYPE)
                .hasCategory(GCC_WARNING)
                .hasPriority(Priority.NORMAL);
        softly8.assertAll();
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

        IssuesAssert.assertThat(warnings)
                .hasSize(3);

        Iterator<Issue> iterator = warnings.iterator();

        SoftAssertions softly1 = new SoftAssertions();
        softly1.assertThat(iterator.next())
                .hasLineStart(12)
                .hasLineEnd(12)
                .hasMessage("file.h: No such file or directory")
                .hasFileName("/dir1/dir2/file.c")
                .hasType(TYPE)
                .hasCategory(GccParser.GCC_ERROR)
                .hasPriority(Priority.HIGH);
        softly1.assertAll();

        SoftAssertions softly2 = new SoftAssertions();
        softly2.assertThat(iterator.next())
                .hasLineStart(233)
                .hasLineEnd(233)
                .hasMessage("undefined reference to `MyInterface::getValue() const&apos;")
                .hasFileName("/dir1/dir3/file.cpp")
                .hasType(TYPE)
                .hasCategory(GccParser.GCC_ERROR)
                .hasPriority(Priority.HIGH);
        softly2.assertAll();

        SoftAssertions softly3 = new SoftAssertions();
        softly3.assertThat(iterator.next())
                .hasLineStart(20)
                .hasLineEnd(20)
                .hasMessage("invalid preprocessing directive #incldue")
                .hasFileName("/dir1/dir2/file.cpp")
                .hasType(TYPE)
                .hasCategory(GccParser.GCC_ERROR)
                .hasPriority(Priority.HIGH);
        softly3.assertAll();
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

        IssuesAssert.assertThat(warnings)
                .hasSize(2);

        Iterator<Issue> iterator = warnings.iterator();

        SoftAssertions softly1 = new SoftAssertions();
        softly1.assertThat(iterator.next())
                .hasLineStart(352)
                .hasLineEnd(352)
                .hasMessage("&apos;s2.mepSector2::lubrications&apos; may be used")
                .hasFileName("main/mep.cpp")
                .hasType(TYPE)
                .hasCategory(GCC_WARNING)
                .hasPriority(Priority.NORMAL);
        softly1.assertAll();

        SoftAssertions softly2 = new SoftAssertions();
        softly2.assertThat(iterator.next())
                .hasLineStart(1477)
                .hasLineEnd(1477)
                .hasMessage("&apos;s2.mepSector2::lubrications&apos; was declared here")
                .hasFileName("main/mep.cpp")
                .hasType(TYPE)
                .hasCategory("GCC note")
                .hasPriority(Priority.LOW);
        softly2.assertAll();
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

        IssuesAssert.assertThat(warnings)
                .hasSize(0);
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

        IssuesAssert.assertThat(warnings)
                .hasSize(22);

        Iterator<Issue> iterator = warnings.iterator();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(iterator.next())
                .hasLineStart(1128)
                .hasLineEnd(1128)
                .hasMessage("NULL used in arithmetic")
                .hasFileName("/Users/rthomson/hudson/jobs/Bryce7-MacWarnings/workspace/bryce7/src/Bryce/Plugins/3DSExport/3DSExport.cpp")
                .hasType(TYPE)
                .hasCategory(GCC_WARNING)
                .hasPriority(Priority.NORMAL);
        softly.assertAll();
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

        IssuesAssert.assertThat(warnings)
                .hasSize(1);

        Iterator<Issue> iterator = warnings.iterator();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(iterator.next())
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("cannot find -lMyLib")
                .hasFileName("MyLib")
                .hasType(TYPE)
                .hasCategory(GccParser.LINKER_ERROR)
                .hasPriority(Priority.HIGH);
        softly.assertAll();
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

        IssuesAssert.assertThat(warnings)
                .hasSize(4);

        Iterator<Issue> iterator = warnings.iterator();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(iterator.next())
                .hasLineStart(638)
                .hasLineEnd(638)
                .hasMessage("local declaration of &quot;command&quot; hides instance variable")
                .hasFileName("folder1/file1.m")
                .hasType(TYPE)
                .hasCategory(GCC_WARNING)
                .hasPriority(Priority.NORMAL);
        softly.assertAll();

        SoftAssertions softly1 = new SoftAssertions();
        softly1.assertThat(iterator.next())
                .hasLineStart(640)
                .hasLineEnd(640)
                .hasMessage("instance variable &quot;command&quot; accessed in class method")
                .hasFileName("folder1/file1.m")
                .hasType(TYPE)
                .hasCategory(GCC_WARNING)
                .hasPriority(Priority.NORMAL);
        softly1.assertAll();

        SoftAssertions softly2 = new SoftAssertions();
        softly2.assertThat(iterator.next())
                .hasLineStart(47)
                .hasLineEnd(47)
                .hasMessage("&quot;oldGeb&quot; might be used uninitialized in this function")
                .hasFileName("file1.m")
                .hasType(TYPE)
                .hasCategory(GCC_WARNING)
                .hasPriority(Priority.NORMAL);
        softly2.assertAll();

        SoftAssertions softly3 = new SoftAssertions();
        softly3.assertThat(iterator.next())
                .hasLineStart(640)
                .hasLineEnd(640)
                .hasMessage("local declaration of &quot;command&quot; hides instance variable")
                .hasFileName("file1.m")
                .hasType(TYPE)
                .hasCategory(GCC_WARNING)
                .hasPriority(Priority.NORMAL);
        softly3.assertAll();
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

        IssuesAssert.assertThat(warnings)
                .hasSize(1);
    }

    @Override
    protected String getWarningsFile() {
        return "gcc.txt";
    }
}

