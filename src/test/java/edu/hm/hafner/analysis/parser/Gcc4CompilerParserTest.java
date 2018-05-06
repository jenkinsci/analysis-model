package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractIssueParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link Gcc4CompilerParser}.
 *
 * @author Frederic Chateau
 * @author Raphael Furch
 */
class Gcc4CompilerParserTest extends AbstractIssueParserTest {
    private static final String WARNING_CATEGORY = "Warning";
    private static final String ERROR_CATEGORY = "Error";

    protected Gcc4CompilerParserTest() {
        super("gcc4.txt");
    }

    @Override
    protected Gcc4CompilerParser createParser() {
        return new Gcc4CompilerParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(14);

        Iterator<Issue> iterator = report.iterator();

        softly.assertThat(iterator.next())
                .hasLineStart(451)
                .hasLineEnd(451)
                .hasMessage("'void yyunput(int, char*)' defined but not used")
                .hasFileName("testhist.l")
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(73)
                .hasLineEnd(73)
                .hasMessage("implicit typename is deprecated, please see the documentation for details")
                .hasFileName("/u1/drjohn/bfdist/packages/RegrTest/V00-03-01/RgtAddressLineScan.cc")
                .hasCategory(ERROR_CATEGORY)
                .hasPriority(Priority.HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(4)
                .hasLineEnd(4)
                .hasColumnStart(39)
                .hasColumnEnd(39)
                .hasMessage("foo.h: No such file or directory")
                .hasFileName("foo.cc")
                .hasCategory(ERROR_CATEGORY)
                .hasPriority(Priority.HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(678)
                .hasLineEnd(678)
                .hasMessage("missing initializer for member sigaltstack::ss_sp")
                .hasFileName("../../lib/linux-i686/include/boost/test/impl/execution_monitor.ipp")
                .hasCategory(WARNING_CATEGORY);

        softly.assertThat(iterator.next())
                .hasLineStart(678)
                .hasLineEnd(678)
                .hasMessage("missing initializer for member sigaltstack::ss_flags")
                .hasFileName("../../lib/linux-i686/include/boost/test/impl/execution_monitor.ipp")
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(678)
                .hasLineEnd(678)
                .hasMessage("missing initializer for member sigaltstack::ss_size")
                .hasFileName("../../lib/linux-i686/include/boost/test/impl/execution_monitor.ipp")
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(52)
                .hasLineEnd(52)
                .hasMessage("large integer implicitly truncated to unsigned type")
                .hasFileName("src/test_simple_sgs_message.cxx")
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(352)
                .hasLineEnd(352)
                .hasMessage("'s2.mepSector2::lubrications' may be used uninitialized in this function")
                .hasFileName("main/mep.cpp")
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(6)
                .hasLineEnd(6)
                .hasMessage("passing 'Test' chooses 'int' over 'unsigned int'")
                .hasFileName("warnings.cc")
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(6)
                .hasLineEnd(6)
                .hasMessage("in call to 'std::basic_ostream<_CharT, _Traits>& std::basic_ostream<_CharT, _Traits>::operator<<(int) [with _CharT = char, _Traits = std::char_traits<char>]'")
                .hasFileName("warnings.cc")
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(33)
                .hasLineEnd(33)
                .hasMessage("#warning This file includes at least one deprecated or antiquated header which may be removed without further notice at a future date. Please use a non-deprecated interface with equivalent functionality instead. For a listing of replacement headers and interfaces, consult the file backward_warning.h. To disable this warning use -Wno-deprecated.")
                .hasFileName("/usr/include/c++/4.3/backward/backward_warning.h")
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(8)
                .hasLineEnd(8)
                .hasMessage("'bar' was not declared in this scope")
                .hasFileName("fo:oo.cpp")
                .hasCategory(ERROR_CATEGORY)
                .hasPriority(Priority.HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(12)
                .hasLineEnd(12)
                .hasMessage("expected ';' before 'return'")
                .hasFileName("fo:oo.cpp")
                .hasCategory(ERROR_CATEGORY)
                .hasPriority(Priority.HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(23)
                .hasLineEnd(23)
                .hasMessage("unused variable 'j' [-Wunused-variable]")
                .hasFileName("warner.cpp")
                .hasCategory("Warning:unused-variable")
                .hasPriority(Priority.NORMAL);
    }

    /** Should not report warnings already detected by {@link Gcc4LinkerParser}. */
    @Test
    void shouldNotReportGccWarnings() {
        Report warnings = parse("gcc4ld.txt");

        assertThat(warnings).isEmpty();
    }

    /**
     * Parses a file with one fatal error.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-18081">Issue 18081</a>
     */
    @Test
    void issue18081() {
        Report warnings = parse("issue18081.txt");

        assertThat(warnings).hasSize(1);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasLineStart(10)
                    .hasLineEnd(10)
                    .hasMessage("'test.h' file not found")
                    .hasFileName("./test.h")
                    .hasCategory(ERROR_CATEGORY)
                    .hasPriority(Priority.HIGH);
        });
    }

    /**
     * Parses a file with one warning that are started by ant.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-9926">Issue 9926</a>
     */
    @Test
    void issue9926() {
        Report warnings = parse("issue9926.txt");

        assertThat(warnings).hasSize(1);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasLineStart(52)
                    .hasLineEnd(52)
                    .hasMessage("large integer implicitly truncated to unsigned type")
                    .hasFileName("src/test_simple_sgs_message.cxx")
                    .hasCategory(WARNING_CATEGORY)
                    .hasPriority(Priority.NORMAL);
        });
    }

    /**
     * Parses a warning log with 1 warning.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-6563">Issue 6563</a>
     */
    @Test
    void issue6563() {
        Report warnings = parse("issue6563.txt");

        assertThat(warnings).hasSize(10);
    }

    /**
     * Parses a warning log with 10 template warnings.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-5606">Issue 5606</a>
     */
    @Test
    void issue5606() {
        Report warnings = parse("issue5606.txt");

        assertThat(warnings).hasSize(5).hasDuplicatesSize(5);
    }

    /**
     * Parses a warning log with multi line warnings.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-5605">Issue 5605</a>
     */
    @Test
    void issue5605() {
        Report warnings = parse("issue5605.txt");

        assertThat(warnings).hasSize(2).hasDuplicatesSize(4);
    }

    /**
     * Parses a warning log with multi line warnings.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-5445">Issue 5445</a>
     */
    @Test
    void issue5445() {
        Report warnings = parse("issue5445.txt");

        assertThat(warnings).isEmpty();
    }

    /**
     * Parses a warning log with autoconf messages. There should be no warning.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-5870">Issue 5870</a>
     */
    @Test
    void issue5870() {
        Report warnings = parse("issue5870.txt");

        assertThat(warnings)
                .isEmpty();
    }

    /**
     * Classify warnings by gcc 4.6 or later.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-11799">Issue 11799</a>
     */
    @Test
    void issue11799() {
        Report warnings = parse("issue11799.txt");

        assertThat(warnings)
                .hasSize(4);

        Iterator<? extends Issue> iterator = warnings.iterator();

        assertSoftly(softly -> {
            softly.assertThat(iterator.next())
                    .hasLineStart(4)
                    .hasLineEnd(4)
                    .hasMessage("implicit declaration of function 'undeclared_function' [-Wimplicit-function-declaration]")
                    .hasFileName("gcc4warning.c")
                    .hasCategory(WARNING_CATEGORY + ":implicit-function-declaration")
                    .hasPriority(Priority.NORMAL);

            softly.assertThat(iterator.next())
                    .hasLineStart(3)
                    .hasLineEnd(3)
                    .hasMessage("unused variable 'unused_local' [-Wunused-variable]")
                    .hasFileName("gcc4warning.c")
                    .hasCategory(WARNING_CATEGORY + ":unused-variable")
                    .hasPriority(Priority.NORMAL);

            softly.assertThat(iterator.next())
                    .hasLineStart(1)
                    .hasLineEnd(1)
                    .hasMessage("unused parameter 'unused_parameter' [-Wunused-parameter]")
                    .hasFileName("gcc4warning.c")
                    .hasCategory(WARNING_CATEGORY + ":unused-parameter")
                    .hasPriority(Priority.NORMAL);

            softly.assertThat(iterator.next())
                    .hasLineStart(5)
                    .hasLineEnd(5)
                    .hasMessage("control reaches end of non-void function [-Wreturn-type]")
                    .hasFileName("gcc4warning.c")
                    .hasCategory(WARNING_CATEGORY + ":return-type")
                    .hasPriority(Priority.NORMAL);
        });
    }
}

