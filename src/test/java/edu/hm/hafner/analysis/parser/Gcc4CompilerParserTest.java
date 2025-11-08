package edu.hm.hafner.analysis.parser;

import java.util.Iterator;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Report.IssueFilterBuilder;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;
import static org.assertj.core.api.Assumptions.*;

/**
 * Tests the class {@link Gcc4CompilerParser}.
 *
 * @author Frederic Chateau
 * @author Raphael Furch
 * @author Ullrich Hafner
 */
class Gcc4CompilerParserTest extends AbstractParserTest {
    Gcc4CompilerParserTest() {
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
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(73)
                .hasLineEnd(73)
                .hasMessage("implicit typename is deprecated, please see the documentation for details")
                .hasFileName("/u1/drjohn/bfdist/packages/RegrTest/V00-03-01/RgtAddressLineScan.cc")
                .hasSeverity(Severity.ERROR);

        softly.assertThat(iterator.next())
                .hasLineStart(4)
                .hasLineEnd(4)
                .hasColumnStart(39)
                .hasColumnEnd(39)
                .hasMessage("foo.h: No such file or directory")
                .hasFileName("foo.cc")
                .hasSeverity(Severity.ERROR);

        softly.assertThat(iterator.next())
                .hasLineStart(678)
                .hasLineEnd(678)
                .hasMessage("missing initializer for member sigaltstack::ss_sp")
                .hasFileName("../../lib/linux-i686/include/boost/test/impl/execution_monitor.ipp");

        softly.assertThat(iterator.next())
                .hasLineStart(678)
                .hasLineEnd(678)
                .hasMessage("missing initializer for member sigaltstack::ss_flags")
                .hasFileName("../../lib/linux-i686/include/boost/test/impl/execution_monitor.ipp")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(678)
                .hasLineEnd(678)
                .hasMessage("missing initializer for member sigaltstack::ss_size")
                .hasFileName("../../lib/linux-i686/include/boost/test/impl/execution_monitor.ipp")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(52)
                .hasLineEnd(52)
                .hasMessage("large integer implicitly truncated to unsigned type")
                .hasFileName("src/test_simple_sgs_message.cxx")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(352)
                .hasLineEnd(352)
                .hasMessage("""
                        's2.mepSector2::lubrications' may be used uninitialized in this function
                        main/mep.cpp:1477: note: 's2.mepSector2::lubrications' was declared here""")
                .hasFileName("main/mep.cpp")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(6)
                .hasLineEnd(6)
                .hasMessage("passing 'Test' chooses 'int' over 'unsigned int'")
                .hasFileName("warnings.cc")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(6)
                .hasLineEnd(6)
                .hasMessage(
                        "in call to 'std::basic_ostream<_CharT, _Traits>& std::basic_ostream<_CharT, _Traits>::operator<<(int) [with _CharT = char, _Traits = std::char_traits<char>]'")
                .hasFileName("warnings.cc")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(33)
                .hasLineEnd(33)
                .hasMessage(
                        "#warning This file includes at least one deprecated or antiquated header which may be removed without further notice at a future date. Please use a non-deprecated interface with equivalent functionality instead. For a listing of replacement headers and interfaces, consult the file backward_warning.h. To disable this warning use -Wno-deprecated.")
                .hasFileName("/usr/include/c++/4.3/backward/backward_warning.h")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(8)
                .hasLineEnd(8)
                .hasMessage("'bar' was not declared in this scope")
                .hasFileName("fo:oo.cpp")
                .hasSeverity(Severity.ERROR);

        softly.assertThat(iterator.next())
                .hasLineStart(12)
                .hasLineEnd(12)
                .hasMessage("""
                        expected ';' before 'return'
                        foo bar.hello*world:12:""")
                .hasFileName("fo:oo.cpp")
                .hasSeverity(Severity.ERROR);

        softly.assertThat(iterator.next())
                .hasLineStart(23)
                .hasLineEnd(23)
                .hasMessage("unused variable 'j' [-Wunused-variable]")
                .hasFileName("warner.cpp")
                .hasCategory("unused-variable")
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Test
    void shouldAssignAbsolutePath() {
        var report = parse("gnuMakeGcc.txt");
        try (var softly = new SoftAssertions()) {
            Iterator<Issue> iterator = report.iterator();
            softly.assertThat(report).hasSize(12);
            softly.assertThat(iterator.next())
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(451)
                    .hasLineEnd(451)
                    .hasMessage("'void yyunput(int, char*)' defined but not used")
                    .hasFileName("/dir1/testhist.l");

            softly.assertThat(iterator.next())
                    .hasSeverity(Severity.ERROR)
                    .hasLineStart(4)
                    .hasLineEnd(4)
                    .hasMessage("foo.h: No such file or directory")
                    .hasFileName("/dir1/foo.cc");

            softly.assertThat(iterator.next())
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(678)
                    .hasLineEnd(678)
                    .hasMessage("missing initializer for member sigaltstack::ss_sp")
                    .hasFileName("../../lib/linux-i686/include/boost/test/impl/execution_monitor.ipp");

            softly.assertThat(iterator.next())
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(678)
                    .hasLineEnd(678)
                    .hasMessage("missing initializer for member sigaltstack::ss_flags")
                    .hasFileName("../../lib/linux-i686/include/boost/test/impl/execution_monitor.ipp");

            softly.assertThat(iterator.next())
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(678)
                    .hasLineEnd(678)
                    .hasMessage("missing initializer for member sigaltstack::ss_size")
                    .hasFileName("../../lib/linux-i686/include/boost/test/impl/execution_monitor.ipp");

            softly.assertThat(iterator.next())
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(52)
                    .hasLineEnd(52)
                    .hasMessage("large integer implicitly truncated to unsigned type")
                    .hasFileName("/dir1/src/test_simple_sgs_message.cxx");

            softly.assertThat(iterator.next())
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(352)
                    .hasLineEnd(352)
                    .hasMessage("""
                            's2.mepSector2::lubrications' may be used uninitialized in this function
                            main/mep.cpp:1477: note: 's2.mepSector2::lubrications' was declared here""")
                    .hasFileName("/dir1/dir2/main/mep.cpp");

            softly.assertThat(iterator.next())
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(6)
                    .hasLineEnd(6)
                    .hasMessage("passing 'Test' chooses 'int' over 'unsigned int'")
                    .hasFileName("/dir1/dir2/warnings.cc");

            softly.assertThat(iterator.next())
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(6)
                    .hasLineEnd(6)
                    .hasMessage(
                            "in call to 'std::basic_ostream<_CharT, _Traits>& std::basic_ostream<_CharT, _Traits>::operator<<(int) [with _CharT = char, _Traits = std::char_traits<char>]'")
                    .hasFileName("/dir1/dir2/warnings.cc");

            softly.assertThat(iterator.next())
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(5)
                    .hasLineEnd(5)
                    .hasMessage("Your code is bad, and you should feel bad!")
                    .hasFileName("/dir4/zoidberg.c");

            assumeThat(isWindows()).isFalse();

            softly.assertThat(iterator.next())
                    .hasSeverity(Severity.ERROR)
                    .hasLineStart(73)
                    .hasLineEnd(73)
                    .hasMessage("implicit typename is deprecated, please see the documentation for details")
                    .hasFileName("/u1/drjohn/bfdist/packages/RegrTest/V00-03-01/RgtAddressLineScan.cc");

            softly.assertThat(iterator.next())
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(33)
                    .hasLineEnd(33)
                    .hasMessage(
                            "#warning This file includes at least one deprecated or antiquated header which may be removed without further notice at a future date. Please use a non-deprecated interface with equivalent functionality instead. For a listing of replacement headers and interfaces, consult the file backward_warning.h. To disable this warning use -Wno-deprecated.")
                    .hasFileName("/usr/include/c++/4.3/backward/backward_warning.h");
        }
    }

    /** Should not report warnings already detected by {@link Gcc4LinkerParser}. */
    @Test
    void shouldNotReportGccWarnings() {
        var warnings = parse("gcc4ld.txt");

        assertThat(warnings).isEmpty();
    }

    @Test
    void shouldResolveAbsolutePaths() {
        var warnings = createParser().parseReport(createReaderFactory("absolute-paths.txt"));

        assertThat(warnings).hasSize(188);

        assertThat(warnings.get(0))
                .hasFileName(
                        "/var/lib/jenkins/workspace/daos-stack-org_daos_PR-13-centos7/_build.external/pmix/src/util/keyval/keyval_lex.c");
    }

    /**
     * Parser should make relative paths absolute correctly if make/cmake is used recursively.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-66835">Issue 66835</a>
     */
    @Test
    void issue66835() {
        var warnings = createParser().parseReport(createReaderFactory("issue66835.makefile.log"));

        assertThat(warnings).hasSize(3);

        assertThat(warnings.get(0))
                .hasFileName(
                        "/path/to/workspace/libraries/this-library-workspace/sublibrary/src/FirstProblemFile.cpp");

        assertThat(warnings.get(1))
                .hasFileName(
                        "/path/to/workspace/libraries/this-library-workspace/sublibrary/inc/Library/ProblemFile.h");

        assertThat(warnings.get(2))
                .hasFileName(
                        "/path/to/workspace/libraries/this-library-workspace/sublibrary/src/OtherProblemFile.cpp");
    }

    /**
     * Parser should not throw errors when Entering directory and Leaving directory if not from make.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-66923">Issue 66923</a>
     */
    @Test
    void issue66923() {
        var warnings = createParser().parseReport(createReaderFactory("issue66923.txt"));

        assertThat(warnings).hasSize(0);
        assertThat(warnings).doesNotHaveErrors();
    }

    /**
     * Parser should not throw errors when path does not start and end with hyphen.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-69242">Issue 69242</a>
     */
    @Test
    void issue69242() {
        var warnings = createParser().parseReport(createReaderFactory("issue69242.txt"));

        assertThat(warnings).hasSize(2);
        assertThat(warnings).doesNotHaveErrors();
        assertThat(warnings.get(0))
                .hasFileName(
                        "/path/to/workspace/daos-stack-org_daos_PR-13-centos7/_build.external/pmix/src/util/keyval/keyval_lex.c");
    }

    /**
     * Parser should make relative paths absolute if cmake/ninja is used.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-56020">Issue 56020</a>
     */
    @Test
    void issue56020() {
        var makefileReport = parse("issue56020.makefile.log");

        assertThat(makefileReport).hasSize(1);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(makefileReport.get(0))
                    .hasLineStart(1)
                    .hasColumnStart(26)
                    .hasMessage("""
                            unused variable ‘a’ [-Wunused-variable]
                             void doSomething() { int a; }
                                                      ^""")
                    .hasFileName("/shd/CTC/TOOLS/Jenkins/workspace/ChrisTest/main.cpp")
                    .hasCategory("unused-variable")
                    .hasSeverity(Severity.WARNING_NORMAL);
        }

        var ninjaReport = parse("issue56020.ninja.log");

        assertThat(ninjaReport).hasSize(1);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(ninjaReport.get(0))
                    .hasLineStart(1)
                    .hasColumnStart(26)
                    .hasMessage("""
                            unused variable ‘a’ [-Wunused-variable]
                             void doSomething() { int a; }
                                                      ^""")
                    .hasFileName("/shd/CTC/TOOLS/Jenkins/workspace/ChrisTest/main.cpp")
                    .hasCategory("unused-variable")
                    .hasSeverity(Severity.WARNING_NORMAL);
        }
    }

    /**
     * Parses a file with one fatal error.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-18081">Issue 18081</a>
     */
    @Test
    void issue18081() {
        var warnings = parse("issue18081.txt");

        assertThat(warnings).hasSize(1);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasLineStart(10)
                    .hasLineEnd(10)
                    .hasMessage("""
                            'test.h' file not found
                            #include "test.h"
                                     ^""")
                    .hasFileName("./test.h")
                    .hasSeverity(Severity.ERROR);
        }
    }

    /**
     * Parses a file with one warning that are started by ant.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-9926">Issue 9926</a>
     */
    @Test
    void issue9926() {
        var warnings = parse("issue9926.txt");

        assertThat(warnings).hasSize(1);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasLineStart(52)
                    .hasLineEnd(52)
                    .hasMessage("large integer implicitly truncated to unsigned type")
                    .hasFileName("src/test_simple_sgs_message.cxx")
                    .hasSeverity(Severity.WARNING_NORMAL);
        }
    }

    /**
     * Parses a warning log with 1 warning.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-6563">Issue 6563</a>
     */
    @Test
    void issue6563() {
        var warnings = parse("issue6563.txt");

        assertThat(warnings).hasSize(10);
    }

    /**
     * Parses a warning log with 10 template warnings.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-5606">Issue 5606</a>
     */
    @Test
    void issue5606() {
        var warnings = parse("issue5606.txt");

        assertThat(warnings).hasSize(5).hasDuplicatesSize(5);
    }

    /**
     * Parses a warning log with multi line warnings.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-5605">Issue 5605</a>
     */
    @Test
    void issue5605() {
        var warnings = parse("issue5605.txt");

        assertThat(warnings).hasSize(2).hasDuplicatesSize(4);
    }

    /**
     * Parses a warning log with multi line warnings.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-5445">Issue 5445</a>
     */
    @Test
    void issue5445() {
        var warnings = parse("issue5445.txt");

        assertThat(warnings).isEmpty();
    }

    /**
     * Parses a warning log with autoconf messages. There should be no warning.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-5870">Issue 5870</a>
     */
    @Test
    void issue5870() {
        var warnings = parse("issue5870.txt");

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
        var warnings = parse("issue11799.txt");

        assertThat(warnings)
                .hasSize(4);

        Iterator<? extends Issue> iterator = warnings.iterator();

        try (var softly = new SoftAssertions()) {
            softly.assertThat(iterator.next())
                    .hasLineStart(4)
                    .hasLineEnd(4)
                    .hasMessage(
                            "implicit declaration of function 'undeclared_function' [-Wimplicit-function-declaration]")
                    .hasFileName("gcc4warning.c")
                    .hasCategory("implicit-function-declaration")
                    .hasSeverity(Severity.WARNING_NORMAL);

            softly.assertThat(iterator.next())
                    .hasLineStart(3)
                    .hasLineEnd(3)
                    .hasMessage("unused variable 'unused_local' [-Wunused-variable]")
                    .hasFileName("gcc4warning.c")
                    .hasCategory("unused-variable")
                    .hasSeverity(Severity.WARNING_NORMAL);

            softly.assertThat(iterator.next())
                    .hasLineStart(1)
                    .hasLineEnd(1)
                    .hasMessage("unused parameter 'unused_parameter' [-Wunused-parameter]")
                    .hasFileName("gcc4warning.c")
                    .hasCategory("unused-parameter")
                    .hasSeverity(Severity.WARNING_NORMAL);

            softly.assertThat(iterator.next())
                    .hasLineStart(5)
                    .hasLineEnd(5)
                    .hasMessage("control reaches end of non-void function [-Wreturn-type]")
                    .hasFileName("gcc4warning.c")
                    .hasCategory("return-type")
                    .hasSeverity(Severity.WARNING_NORMAL);
        }
    }

    /**
     * Detects several multi line messages.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-56612">Issue 56612</a>
     */
    @Test
    void issue56612() {
        var warnings = parse("issue56612.log");

        assertThat(warnings).hasSize(6);

        assertThat(warnings.get(0))
                .hasFileName("Applications/DataExplorer/VtkVis/VtkVis_autogen/include/ui_VisualizationWidgetBase.h")
                .hasLineStart(263);
        assertThat(warnings.get(0).getMessage())
                .startsWith(
                        "'QVTKWidget::QVTKWidget(QWidget*, Qt::WindowFlags)' is deprecated [-Wdeprecated-declarations]\n");

        Predicate<Issue> predicate = new IssueFilterBuilder()
                .setExcludeMessageFilter(".*QVTKWidget.*", ".*tmpnam.*")
                .setExcludeFileNameFilter(".*qrc_icons\\.cpp.*").build();
        var filtered = warnings.filter(predicate);
        assertThat(filtered).hasSize(0);
    }

    /**
     * Detect make paths using different types of apostrophe's.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-55221">Issue 55221</a>
     */
    @Test
    void issue55221() {
        var warnings = parse("issue55221.txt");

        assertThat(warnings).hasSize(6).hasDuplicatesSize(2);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasLineStart(204)
                    .hasColumnStart(26)
                    .hasMessage(
                            """
                            ‘StarLibs::Camelot::ScBitTrue::StarUlPhyRxCommonCamelot::SectorDLCAL’ will be initialized after [-Wreorder]
                            ParamNumeric<unsigned> SectorDLCAL;
                            ^~~~~~~~~~~""")
                    .hasFileName(
                            "/data/hudsonuser/workspace/Regression_test_SystemC_gcc@2/StarLibs/Camelot/ScBitTrue/StarUlPhyRxCommonCamelot.h")
                    .hasCategory("reorder")
                    .hasSeverity(Severity.WARNING_NORMAL);

            softly.assertThat(warnings.get(1))
                    .hasLineStart(179)
                    .hasColumnStart(32)
                    .hasMessage(
                            """
                            ‘ParamNumeric<unsigned int> StarLibs::Camelot::ScBitTrue::StarUlPhyRxCommonCamelot::UseDSPBuilderFFT’ [-Wreorder]
                            ParamNumeric<unsigned> UseDSPBuilderFFT;
                            ^~~~~~~~~~~~~~~~""")
                    .hasFileName(
                            "/data/hudsonuser/workspace/Regression_test_SystemC_gcc@2/StarLibs/Camelot/ScBitTrue/StarUlPhyRxCommonCamelot.h")
                    .hasCategory("reorder")
                    .hasSeverity(Severity.WARNING_NORMAL);

            softly.assertThat(warnings.get(2))
                    .hasLineStart(168)
                    .hasColumnStart(21)
                    .hasMessage(
                            "dereferencing type-punned pointer will break strict-aliasing rules [-Wstrict-aliasing]\n             GetUInt((uint32_t&)result);\n                     ^~~~~~~~~~~~~~~~~")
                    .hasFileName(
                            "/data/hudsonuser/workspace/Regression_test_SystemC_gcc/StarLibs/Camelot/ScBitTrue/AlteraDspBuilderFFT/csl/stimulus_file.h")
                    .hasCategory("strict-aliasing")
                    .hasSeverity(Severity.WARNING_NORMAL);
            softly.assertThat(warnings.get(3))
                    .hasLineStart(168)
                    .hasColumnStart(21)
                    .hasMessage(
                            "dereferencing type-punned pointer will break strict-aliasing rules [-Wstrict-aliasing]")
                    .hasFileName(
                            "/data/hudsonuser/workspace/Regression_test_SystemC_gcc/StarLibs/Camelot/ScBitTrue/AlteraDspBuilderFFT/csl/stimulus_file.h")
                    .hasCategory("strict-aliasing")
                    .hasSeverity(Severity.WARNING_NORMAL);
            softly.assertThat(warnings.get(4))
                    .hasLineStart(168)
                    .hasColumnStart(21)
                    .hasMessage(
                            """
                            dereferencing type-punned pointer will break strict-aliasing rules [-Wstrict-aliasing]
                            In file included from ../../../StarLibs/Camelot/ScBitTrue/AlteraDspBuilderFFT/csl/steps.h:4:0,
                                             from ../../../StarLibs/Camelot/ScBitTrue/AlteraDspBuilderFFT/csl/csl.h:4,
                                             from vfft_fft_core_VFFT1_CModel.h:2,
                                             from vfft_fft_core_VFFT1_CModel.cpp:2:""")
                    .hasFileName(
                            "/data/hudsonuser/workspace/Regression_test_SystemC_gcc/StarLibs/Camelot/ScBitTrue/AlteraDspBuilderFFT/csl/stimulus_file.h")
                    .hasCategory("strict-aliasing")
                    .hasSeverity(Severity.WARNING_NORMAL);
            softly.assertThat(warnings.get(5))
                    .hasLineStart(105)
                    .hasColumnStart(39)
                    .hasMessage(
                            "returning reference to temporary [-Wreturn-local-addr]\n   return P::Execute(std::forward<T>(v));")
                    .hasFileName(
                            "/data/hudsonuser/workspace/Regression_test_SystemC_gcc/StarLibs/Camelot/ScBitTrue/AlteraDspBuilderFFT/csl/post_steps.h")
                    .hasCategory("return-local-addr")
                    .hasSeverity(Severity.WARNING_NORMAL);
        }
    }

    /**
     * Parses a javac warning log.
     *
     * @see <a href="https://issues.jenkins.io/browse/JENKINS-68396">Issue 68396</a>
     */
    @Test
    void issue68396() {
        var warnings = parse("issue68396.txt");

        assertThat(warnings).isEmpty();
    }

    /**
     * Verifies that GCC warnings with [-W...] are matched but clang-tidy warnings with [check-name] are not.
     *
     * @see <a href="https://issues.jenkins.io/browse/JENKINS-64612">Issue 64612</a>
     */
    @Test
    void issue64612() {
        var report = parse("gcc-clang-tidy-mixed.txt");

        // Should only match GCC warnings with [-W...] pattern, not clang-tidy warnings
        assertThat(report).hasSize(4);

        try (var softly = new SoftAssertions()) {
            Iterator<Issue> iterator = report.iterator();

            softly.assertThat(iterator.next())
                    .hasLineStart(10)
                    .hasColumnStart(5)
                    .hasMessage("unused variable 'x' [-Wunused-variable]")
                    .hasFileName("main.cpp")
                    .hasCategory("unused-variable")
                    .hasSeverity(Severity.WARNING_NORMAL);

            softly.assertThat(iterator.next())
                    .hasLineStart(20)
                    .hasColumnStart(10)
                    .hasMessage("comparison between signed and unsigned [-Wsign-compare]")
                    .hasFileName("test.cpp")
                    .hasCategory("sign-compare")
                    .hasSeverity(Severity.WARNING_NORMAL);

            softly.assertThat(iterator.next())
                    .hasLineStart(30)
                    .hasColumnStart(12)
                    .hasMessage("undefined reference to 'bar' [-Wundefined]")
                    .hasFileName("foo.cpp")
                    .hasCategory("undefined")
                    .hasSeverity(Severity.ERROR);

            softly.assertThat(iterator.next())
                    .hasLineStart(15)
                    .hasColumnStart(1)
                    .hasMessage("control reaches end of non-void function [-Wreturn-type]")
                    .hasFileName("bar.cpp")
                    .hasCategory("return-type")
                    .hasSeverity(Severity.WARNING_NORMAL);
        }
    }
}