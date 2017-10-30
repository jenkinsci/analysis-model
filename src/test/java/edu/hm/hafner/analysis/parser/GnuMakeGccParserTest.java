package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueAssertSoft;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.IssuesAssert;
import edu.hm.hafner.analysis.Priority;

/**
 * Tests the class {@link GnuMakeGccParser}.
 *
 * @author vichak
 */
public class GnuMakeGccParserTest extends ParserTester {
    private static final String WARNING_CATEGORY = "Warning";
    private static final String ERROR_CATEGORY = "Error";
    private static final String WARNING_TYPE = new GnuMakeGccParser().getId();
    private static final int NUMBER_OF_TESTS = 15;

    /**
     * Test of createWarning method of class {@link GnuMakeGccParser} No specifc OS is assumed
     *
     * @throws IOException in case of an error
     */
    @Test
    public void testCreateWarning() throws IOException {
        Issues warnings = new GnuMakeGccParser().parse(openFile());
        IssuesAssert.assertThat(warnings).hasSize(NUMBER_OF_TESTS);

        Iterator<Issue> iterator = warnings.iterator();

        IssueAssertSoft softly = new IssueAssertSoft();

        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(451)
                .hasLineEnd(451)
                .hasMessage("'void yyunput(int, char*)' defined but not used")
                .hasFileName("/dir1/testhist.l")
                .hasType(WARNING_TYPE);
        softly.assertAll();

        softly.assertThat(iterator.next())
                .hasPriority(Priority.HIGH)
                .hasCategory(ERROR_CATEGORY)
                .hasLineStart(73)
                .hasLineEnd(73)
                .hasMessage("implicit typename is deprecated, please see the documentation for details")
                .hasFileName("/u1/drjohn/bfdist/packages/RegrTest/V00-03-01/RgtAddressLineScan.cc")
                .hasType(WARNING_TYPE);
        softly.assertAll();

        softly.assertThat(iterator.next())
                .hasPriority(Priority.HIGH)
                .hasCategory(ERROR_CATEGORY)
                .hasLineStart(4)
                .hasLineEnd(4)
                .hasMessage("foo.h: No such file or directory")
                .hasFileName("/dir1/foo.cc")
                .hasType(WARNING_TYPE);
        softly.assertAll();

        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(678)
                .hasLineEnd(678)
                .hasMessage("missing initializer for member sigaltstack::ss_sp")
                .hasFileName("/dir1/../../lib/linux-i686/include/boost/test/impl/execution_monitor.ipp")
                .hasType(WARNING_TYPE);
        softly.assertAll();

        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(678)
                .hasLineEnd(678)
                .hasMessage("missing initializer for member sigaltstack::ss_flags")
                .hasFileName("/dir1/../../lib/linux-i686/include/boost/test/impl/execution_monitor.ipp")
                .hasType(WARNING_TYPE);
        softly.assertAll();

        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(678)
                .hasLineEnd(678)
                .hasMessage("missing initializer for member sigaltstack::ss_size")
                .hasFileName("/dir1/../../lib/linux-i686/include/boost/test/impl/execution_monitor.ipp")
                .hasType(WARNING_TYPE);
        softly.assertAll();

        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(52)
                .hasLineEnd(52)
                .hasMessage("large integer implicitly truncated to unsigned type")
                .hasFileName("/dir1/src/test_simple_sgs_message.cxx")
                .hasType(WARNING_TYPE);
        softly.assertAll();

        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(352)
                .hasLineEnd(352)
                .hasMessage("'s2.mepSector2::lubrications' may be used uninitialized in this function")
                .hasFileName("/dir1/dir2/main/mep.cpp")
                .hasType(WARNING_TYPE);
        softly.assertAll();

        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(6)
                .hasLineEnd(6)
                .hasMessage("passing 'Test' chooses 'int' over 'unsigned int'")
                .hasFileName("/dir1/dir2/warnings.cc")
                .hasType(WARNING_TYPE);
        softly.assertAll();

        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(6)
                .hasLineEnd(6)
                .hasMessage("in call to 'std::basic_ostream<_CharT, _Traits>& std::basic_ostream<_CharT, _Traits>::operator<<(int) [with _CharT = char, _Traits = std::char_traits<char>]'")
                .hasFileName("/dir1/dir2/warnings.cc")
                .hasType(WARNING_TYPE);
        softly.assertAll();

        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(33)
                .hasLineEnd(33)
                .hasMessage("#warning This file includes at least one deprecated or antiquated header which may be removed without further notice at a future date. Please use a non-deprecated interface with equivalent functionality instead. For a listing of replacement headers and interfaces, consult the file backward_warning.h. To disable this warning use -Wno-deprecated.")
                .hasFileName("/usr/include/c++/4.3/backward/backward_warning.h")
                .hasType(WARNING_TYPE);
        softly.assertAll();

        softly.assertThat(iterator.next())
                .hasPriority(Priority.HIGH)
                .hasCategory(ERROR_CATEGORY)
                .hasLineStart(8)
                .hasLineEnd(8)
                .hasMessage("'bar' was not declared in this scope")
                .hasFileName("/dir1/dir2/dir3/fo:oo.cpp")
                .hasType(WARNING_TYPE);
        softly.assertAll();

        softly.assertThat(iterator.next())
                .hasPriority(Priority.HIGH)
                .hasCategory(ERROR_CATEGORY)
                .hasLineStart(12)
                .hasLineEnd(12)
                .hasMessage("expected ';' before 'return'")
                .hasFileName("/dir1/dir2/dir3/fo:oo.cpp")
                .hasType(WARNING_TYPE);
        softly.assertAll();

        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(5)
                .hasLineEnd(5)
                .hasMessage("Your code is bad, and you should feel bad!")
                .hasFileName("/dir4/zoidberg.c")
                .hasType(WARNING_TYPE);
        softly.assertAll();
        //Next test is OS-specific, so just skip it
        iterator.next();
        //Place new tests after this line
    }

    /**
     * Checks that the root of the path is not changed on non-windows systems
     *
     * @throws IOException in case of an error
     */
    @Test
    public void checkCorrectPath_NonWindows() throws IOException {
        checkOsSpecificPath("Ubuntu", "/c");
    }

    /**
     * Checks that the root of the path is fixed if it is unix-type while running on windows.
     *
     * @throws IOException in case of an error
     */
    @Test
    public void checkCorrectPath_Windows() throws IOException {
        checkOsSpecificPath("Windows NT", "c:");
    }

    /**
     * Checks that paths of the type "/c/anything" are changed to "c:/anything" on windows but no other OS
     */
    private void checkOsSpecificPath(final String os, final String rootDir) {

        Issues warnings = new GnuMakeGccParser(os).parse(openFile());
        IssueAssertSoft softly = new IssueAssertSoft();

        softly.assertThat(warnings.get(14))
                .hasPriority(Priority.NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(20)
                .hasLineEnd(20)
                .hasMessage("I'm warning you! He's got huge, sharp... er... He can leap about.")
                .hasFileName(rootDir + "/dir5/grail.cpp")
                .hasType(WARNING_TYPE);
        softly.assertAll();

    }


    @Override
    protected String getWarningsFile() {
        return "gnuMakeGcc.txt";
    }
}
