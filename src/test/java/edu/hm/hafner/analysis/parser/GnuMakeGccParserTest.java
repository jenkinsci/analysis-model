package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link GnuMakeGccParser}.
 *
 * @author vichak
 */
@SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
public class GnuMakeGccParserTest extends AbstractParserTest {
    private static final String WARNING_CATEGORY = "Warning";
    private static final String ERROR_CATEGORY = "Error";

    GnuMakeGccParserTest() {
        super("gnuMakeGcc.txt");
    }

    /**
     * Checks that the root of the path is not changed on non-windows systems.
     */
    @Test
    public void checkCorrectPathNonWindows() {
        checkOsSpecificPath("Ubuntu", "/c");
    }

    /**
     * Checks that the root of the path is fixed if it is unix-type while running on windows.
     */
    @Test
    public void checkCorrectPathWindows() {
        checkOsSpecificPath("Windows NT", "c:");
    }

    /**
     * Checks that paths of the type "/c/anything" are changed to "c:/anything" on windows but no other OS.
     */
    private void checkOsSpecificPath(final String os, final String rootDir) {
        Issues<Issue> warnings = new GnuMakeGccParser(os).parse(openFile());

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(14))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(WARNING_CATEGORY)
                    .hasLineStart(20)
                    .hasLineEnd(20)
                    .hasMessage("I'm warning you! He's got huge, sharp... er... He can leap about.")
                    .hasFileName(rootDir + "/dir5/grail.cpp");
        });
    }

    @Override
    protected void assertThatIssuesArePresent(final Issues<Issue> issues, final SoftAssertions softly) {
        Iterator<Issue> iterator = issues.iterator();
        softly.assertThat(issues).hasSize(15);
        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(451)
                .hasLineEnd(451)
                .hasMessage("'void yyunput(int, char*)' defined but not used")
                .hasFileName("/dir1/testhist.l");

        softly.assertThat(iterator.next())
                .hasPriority(Priority.HIGH)
                .hasCategory(ERROR_CATEGORY)
                .hasLineStart(73)
                .hasLineEnd(73)
                .hasMessage("implicit typename is deprecated, please see the documentation for details")
                .hasFileName("/u1/drjohn/bfdist/packages/RegrTest/V00-03-01/RgtAddressLineScan.cc");

        softly.assertThat(iterator.next())
                .hasPriority(Priority.HIGH)
                .hasCategory(ERROR_CATEGORY)
                .hasLineStart(4)
                .hasLineEnd(4)
                .hasMessage("foo.h: No such file or directory")
                .hasFileName("/dir1/foo.cc");

        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(678)
                .hasLineEnd(678)
                .hasMessage("missing initializer for member sigaltstack::ss_sp")
                .hasFileName("/dir1/../../lib/linux-i686/include/boost/test/impl/execution_monitor.ipp");

        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(678)
                .hasLineEnd(678)
                .hasMessage("missing initializer for member sigaltstack::ss_flags")
                .hasFileName("/dir1/../../lib/linux-i686/include/boost/test/impl/execution_monitor.ipp");

        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(678)
                .hasLineEnd(678)
                .hasMessage("missing initializer for member sigaltstack::ss_size")
                .hasFileName("/dir1/../../lib/linux-i686/include/boost/test/impl/execution_monitor.ipp");

        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(52)
                .hasLineEnd(52)
                .hasMessage("large integer implicitly truncated to unsigned type")
                .hasFileName("/dir1/src/test_simple_sgs_message.cxx");

        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(352)
                .hasLineEnd(352)
                .hasMessage("'s2.mepSector2::lubrications' may be used uninitialized in this function")
                .hasFileName("/dir1/dir2/main/mep.cpp");

        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(6)
                .hasLineEnd(6)
                .hasMessage("passing 'Test' chooses 'int' over 'unsigned int'")
                .hasFileName("/dir1/dir2/warnings.cc");

        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(6)
                .hasLineEnd(6)
                .hasMessage("in call to 'std::basic_ostream<_CharT, _Traits>& std::basic_ostream<_CharT, _Traits>::operator<<(int) [with _CharT = char, _Traits = std::char_traits<char>]'")
                .hasFileName("/dir1/dir2/warnings.cc");

        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(33)
                .hasLineEnd(33)
                .hasMessage("#warning This file includes at least one deprecated or antiquated header which may be removed without further notice at a future date. Please use a non-deprecated interface with equivalent functionality instead. For a listing of replacement headers and interfaces, consult the file backward_warning.h. To disable this warning use -Wno-deprecated.")
                .hasFileName("/usr/include/c++/4.3/backward/backward_warning.h");

        softly.assertThat(iterator.next())
                .hasPriority(Priority.HIGH)
                .hasCategory(ERROR_CATEGORY)
                .hasLineStart(8)
                .hasLineEnd(8)
                .hasMessage("'bar' was not declared in this scope")
                .hasFileName("/dir1/dir2/dir3/fo:oo.cpp");

        softly.assertThat(iterator.next())
                .hasPriority(Priority.HIGH)
                .hasCategory(ERROR_CATEGORY)
                .hasLineStart(12)
                .hasLineEnd(12)
                .hasMessage("expected ';' before 'return'")
                .hasFileName("/dir1/dir2/dir3/fo:oo.cpp");

        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(5)
                .hasLineEnd(5)
                .hasMessage("Your code is bad, and you should feel bad!")
                .hasFileName("/dir4/zoidberg.c");
    }

    @Override
    protected AbstractParser createParser() {
        return new GnuMakeGccParser();
    }
}
