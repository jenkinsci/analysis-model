package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import java.util.Iterator;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link MsBuildParser}.
 */
class MsBuildParserTest extends AbstractParserTest {
    MsBuildParserTest() {
        super("msbuild.txt");
    }

    /**
     * Parses a file with ANSI colors.
     *
     * @see <a href="https://github.com/jenkinsci/analysis-model/pull/118">PR #118</a>
     */
    @Test
    void shouldRemoveAnsiColors() {
        var warnings = parse("MSBuildANSIColor.txt");

        assertThat(warnings)
                .hasSize(1);

        assertThatReportHasSeverities(warnings, 0, 0, 1, 0);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasFileName("C:/j/6aa722/src/CodeRunner/GenericCodeRunner/CompositeCode.cs")
                    .hasCategory("CS1591")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage("Missing XML comment for publicly visible type or member 'CompositeCode.this[int]'")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(137)
                    .hasLineEnd(137)
                    .hasColumnStart(32)
                    .hasColumnEnd(32);
        }
    }

    /**
     * Parse a file with false positive message.
     *
     * @see <a href="https://issues.jenkins.io/browse/JENKINS-56613">Issue 56613</a>
     */
    @Test
    void issue56613() {
        assertThat(parse("issue56613.txt")).isEmpty();
    }

    /**
     * Extended test for issue 56613 - ensures tool names are properly ignored.
     * Tests that executables (.exe) and tool names without extensions (NMAKE, rs, cl) are skipped.
     *
     * @see <a href="https://issues.jenkins.io/browse/JENKINS-56613">Issue 56613</a>
     */
    @Test
    void issue56613Extended() {
        assertThat(parse("issue56613-extended.txt")).isEmpty();
    }

    /**
     * Test for issue 56613 - ensures that valid source file warnings are still parsed correctly.
     * This validates that the fix for tool names doesn't break normal warning detection.
     *
     * @see <a href="https://issues.jenkins.io/browse/JENKINS-56613">Issue 56613</a>
     */
    @Test
    void issue56613MixedToolNamesAndSourceFiles() {
        var warnings = parse("issue56613-mixed.txt");

        // Should have warnings from source files
        assertThat(warnings).hasSize(3);
        assertThatReportHasSeverities(warnings, 1, 0, 2, 0);
    }

    /**
     * Test for issue 56613 - ensures valid source file warnings are still parsed correctly.
     * This validates that the fix for tool names doesn't break normal warning detection.
     *
     * @see <a href="https://issues.jenkins.io/browse/JENKINS-56613">Issue 56613</a>
     */
    @Test
    void issue56613ValidWarningsStillParsed() {
        var warnings = parse("issue56613-valid-warnings.txt");

        // Valid warnings should be parsed
        assertThat(warnings).hasSize(6);
        assertThatReportHasSeverities(warnings, 3, 0, 3, 0);
    }

    /**
     * Parses a file with false positive message.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-42823">Issue 42823</a>
     */
    @Test
    void issue42823() {
        var warnings = parse("issue42823.txt");
        assertThat(warnings).isEmpty();
    }

    /**
     * MSBuildParser should make relative paths absolute if cmake is used.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-56193">Issue 56193</a>
     */
    @Test
    void issue56193() {
        var warnings = parse("issue56193.log");

        assertThat(warnings).hasSize(1);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasFileName("G:/Jenkins-Alserver-Slave/workspace/ninjamgs/Lesson1/bubble.cpp")
                    .hasCategory("C4101")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage("'a': unreferenced local variable")
                    .hasLineStart(17);
        }
    }

    @Test
    void shouldFix70855() {
        var report = parseStringContent("""
                195>c:\\jenkins\\workspace\\develop\\foo.h(76): warning C4100: 'Id': unreferenced formal parameter (compiling source file bar.cpp) [C:\\jenkins\\workspace\\develop\\project.vcxproj]
                195>c:\\jenkins\\workspace\\develop\\foo.h(76): warning C4100: 'Id': unreferenced formal parameter (compiling source file foo.cpp) [C:\\jenkins\\workspace\\develop\\project.vcxproj]
                """);

        assertThat(report)
                .as("Duplicate warnings from .h files should be skipped")
                .hasSize(1)
                .hasDuplicatesSize(1);
        assertThat(report.get(0)).hasMessage("'Id': unreferenced formal parameter");
    }

    /**
     * MSBuildParser should make relative paths absolute, based on the project name listed in the message.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-56030">Issue 56030</a>
     */
    @Test
    void issue56030() {
        var warnings = parse("issue56030.log");

        assertThat(warnings).hasSize(2);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasFileName("C:/DVR/workspace/_Branch_build_updates-SDSYGOEWO53Z6ASKV6W4GSRWQU4DXCNNDGKGTWMQJ4O7LTMGYQVQ/live555/transport/include/TransportRTCP.h")
                    .hasCategory("C4275")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage("non dll-interface class 'transport::RtcpSpec' used as base for dll-interface class 'transport::TransportRTCPInstance'")
                    .hasLineStart(39);
            softly.assertThat(warnings.get(1))
                    .hasFileName(
                            "C:/DVR/workspace/_Branch_build_updates-SDSYGOEWO53Z6ASKV6W4GSRWQU4DXCNNDGKGTWMQJ4O7LTMGYQVQ/live555/transport/include/TransportRTCP.h")
                    .hasCategory("C4251")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage(
                            "'transport::TransportRTCPInstance::m_CNAME': class 'transport::SDESItem' needs to have dll-interface to be used by clients of class 'transport::TransportRTCPInstance'")
                    .hasLineStart(155);
        }
    }

    /**
     * MSBuildParser should make relative paths absolute, based on the project name listed in the message.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-38215">Issue 38215</a>
     */
    @Test
    void issue38215() {
        var warnings = parse("issue38215.txt");

        assertThat(warnings).hasSize(1);
        assertThatReportHasSeverities(warnings, 0, 0, 1, 0);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasFileName("C:/J/workspace/ci_windows/ws/build/rmw/test/test_error_handling.vcxproj")
                    .hasCategory("D9002")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage("ignoring unknown option '-std=c++11'")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);
        }
    }

    /**
     * MSBuildParser should make relative paths absolute, based on the project name listed in the message.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-22386">Issue 22386</a>
     */
    @Test
    void issue22386() {
        var warnings = parse("issue22386.txt");

        assertThat(warnings).hasSize(2);
        assertThatReportHasSeverities(warnings, 0, 0, 2, 0);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasFileName("C:/solutiondir/projectdir/src/main.cpp")
                    .hasCategory("C4996")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage(
                            "'_splitpath': This function or variable may be unsafe. Consider using _splitpath_s instead. To disable deprecation, use _CRT_SECURE_NO_WARNINGS. See online help for details.")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(97)
                    .hasLineEnd(97)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);

            softly.assertThat(warnings.get(1))
                    .hasFileName("C:/solutiondir/projectdir/src/main.cpp")
                    .hasCategory("C4996")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage(
                            "'strdup': The POSIX name for this item is deprecated. Instead, use the ISO C++ conformant name: _strdup. See online help for details.")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(99)
                    .hasLineEnd(99)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);
        }
    }

    /**
     * MSBuildParser should support SASS messages with '-' and '_' in category.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-51485">Issue 51485</a>
     */
    @Test
    void issue51485() {
        var warnings = parse("issue51485.txt");

        assertThat(warnings).hasSize(2);
        assertThatReportHasSeverities(warnings, 0, 0, 2, 0);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasFileName("src/search-list.component.scss")
                    .hasCategory("shorthand-values")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage(
                            "Property `margin` should be written more concisely as `5px 0 0` instead of `5px 0 0 0`")
                    .hasLineStart(41).hasColumnStart(17);
            softly.assertThat(warnings.get(1))
                    .hasFileName("src/search-list.component.scss")
                    .hasCategory("shorthand_values")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage(
                            "Property `margin` should be written more concisely as `5px 0 0` instead of `5px 0 0 0`")
                    .hasLineStart(42).hasColumnStart(18);
        }
    }

    /**
     * MSBuildParser should also detect subcategories as described at <a href="http://blogs.msdn.com/b/msbuild/archive/2006/11/03/msbuild-visual-studio-aware-error-messages-and-message-formats.aspx">
     * MSBuild / Visual Studio aware error messages and message formats</a>.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-27914">Issue 27914</a>
     */
    @Test
    void issue27914() {
        var warnings = parse("issue27914.txt");

        assertThat(warnings).hasSize(3);
        assertThatReportHasSeverities(warnings, 0, 0, 3, 0);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasFileName("E:/workspace/TreeSize release nightly/DelphiLib/Jam.UI.Dialogs.pas")
                    .hasCategory("H2164")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage("Variable 'lTaskDialog' is declared but never used in 'TDialog.ShowTaskDialog'")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(4522)
                    .hasLineEnd(4522)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);

            softly.assertThat(warnings.get(1))
                    .hasFileName("E:/workspace/TreeSize release nightly/DelphiLib/Jam.UI.Dialogs.pas")
                    .hasCategory("H2164")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage("Variable 'lButton' is declared but never used in 'TDialog.ShowTaskDialog'")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(4523)
                    .hasLineEnd(4523)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);

            softly.assertThat(warnings.get(2))
                    .hasFileName("E:/workspace/TreeSize release nightly/DelphiLib/Jam.UI.Dialogs.pas")
                    .hasCategory("H2164")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage("Variable 'lIndex' is declared but never used in 'TDialog.ShowTaskDialog'")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(4524)
                    .hasLineEnd(4524)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);
        }
    }

    /**
     * Parses a file with a google-test failure that should not be shown as a warning.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-26441">Issue 26441</a>
     */
    @Test
    void issue26441() {
        var warnings = parse("issue26441.txt");

        assertThat(warnings).isEmpty();
    }

    /**
     * Parses a file with gcc warnings that should be skipped.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-20544">Issue 20544</a>
     */
    @Test
    void issue20544() {
        var warnings = parse("issue20544.txt");

        assertThat(warnings).isEmpty();
    }

    /**
     * Parses a file with  warnings of a Visual Studio analysis.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-20154">Issue 20154</a>
     */
    @Test
    void issue20154() {
        var warnings = parse("issue20154.txt");

        assertThat(warnings).hasSize(2).hasDuplicatesSize(2);
        assertThatReportHasSeverities(warnings, 0, 0, 2, 0);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasFileName("I:/devel/projects/SampleCodeAnalysis/SampleCodeAnalysis/Program.cs")
                    .hasCategory("CA1801")
                    .hasType("Microsoft.Usage")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage(
                            "Parameter 'args' of 'Program.Main(string[])' is never used. Remove the parameter or use it in the method body.")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(12)
                    .hasLineEnd(12)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);

            softly.assertThat(warnings.get(1))
                    .hasFileName("I:/devel/projects/SampleCodeAnalysis/SampleCodeAnalysis/Program.cs")
                    .hasCategory("CA1801")
                    .hasType("Microsoft.Usage")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage(
                            "Parameter 'args' of 'Program.Main(string[])' is never used. Remove the parameter or use it in the method body.")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(13)
                    .hasLineEnd(13)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);
        }
    }

    /**
     * Parses a file with 4 warnings of PCLint tools.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-14888">Issue 14888</a>
     */
    @Test
    void issue14888() {
        var warnings = parse("issue14888.txt");

        assertThat(warnings).hasSize(4);
        assertThatReportHasSeverities(warnings, 4, 0, 0, 0);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasFileName("F:/AC_working/new-wlanac/ac/np/capwap/source/capwap_data.c")
                    .hasCategory("40")
                    .hasSeverity(Severity.ERROR)
                    .hasMessage("Undeclared identifier 'TRUE'")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(974)
                    .hasLineEnd(974)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);

            softly.assertThat(warnings.get(1))
                    .hasFileName("F:/AC_working/new-wlanac/ac/np/capwap/source/capwap_data.c")
                    .hasCategory("63")
                    .hasSeverity(Severity.ERROR)
                    .hasMessage("Expected an lvalue")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(974)
                    .hasLineEnd(974)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);

            softly.assertThat(warnings.get(2))
                    .hasFileName("F:/AC_working/new-wlanac/ac/np/capwap/source/capwap_data.c")
                    .hasCategory("40")
                    .hasSeverity(Severity.ERROR)
                    .hasMessage("Undeclared identifier 'pDstCwData'")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(975)
                    .hasLineEnd(975)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);

            softly.assertThat(warnings.get(3))
                    .hasFileName("F:/AC_working/new-wlanac/ac/np/capwap/source/capwap_data.c")
                    .hasCategory("10")
                    .hasSeverity(Severity.ERROR)
                    .hasMessage("Expecting a structure or union")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(975)
                    .hasLineEnd(975)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);
        }
    }

    /**
     * Parses a file with warnings of the MS Build tools.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-10566">Issue 10566</a>
     */
    @Test
    void issue10566() {
        var warnings = parse("issue10566.txt");

        assertThat(warnings).hasSize(1);
        assertThatReportHasSeverities(warnings, 1, 0, 0, 0);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasFileName("..//..//..//xx_Source//file.c")
                    .hasCategory("c1083")
                    .hasSeverity(Severity.ERROR)
                    .hasMessage("cannot open include file: 'Header.h': No such file or directory")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(54)
                    .hasLineEnd(54)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);
        }
    }

    /**
     * Parses a file with errors of the MS Build tools.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-3582">Issue 3582</a>
     */
    @Test
    void issue3582() {
        var errors = parse("issue3582.txt");

        assertThat(errors).hasSize(1);
        assertThatReportHasSeverities(errors, 1, 0, 0, 0);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(errors.get(0))
                    .hasFileName("TestLib.lib")
                    .hasCategory("LNK1181")
                    .hasSeverity(Severity.ERROR)
                    .hasMessage("cannot open input file 'TestLib.lib'")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);
        }
    }

    /**
     * Parses a file with warnings of the MS Build tools.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-63580">Issue 63580</a>
     */
    @Test
    void issue63580() {
        var warnings = parse("issue63580.txt");

        assertThat(warnings).hasSize(1);
        assertThatReportHasSeverities(warnings, 0, 0, 1, 0);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasFileName("def.obj")
                    .hasCategory("LNK4217")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage("symbol 'XYZ' defined in 'abc.obj' is imported by 'def.obj' in function 'FGH'")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);
        }
    }

    /**
     * Parses a file with warnings of Stylecop.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-8347">Issue 8347</a>
     */
    @Test
    void issue8347() {
        var warnings = parse("issue8347.txt");

        assertThat(warnings).hasSize(5);
        assertThatReportHasSeverities(warnings, 0, 0, 5, 0);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasFileName("C:/hudsonSlave/workspace/MyProject/Source/MoqExtensions.cs")
                    .hasCategory("SA1210")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage("Using directives must be sorted alphabetically by the namespaces.")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(2)
                    .hasLineEnd(2)
                    .hasColumnStart(1)
                    .hasColumnEnd(1);

            softly.assertThat(warnings.get(1))
                    .hasFileName("C:/hudsonSlave/workspace/MyProject/Source/MoqExtensions.cs")
                    .hasCategory("SA1210")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage("Using directives must be sorted alphabetically by the namespaces.")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(3)
                    .hasLineEnd(3)
                    .hasColumnStart(1)
                    .hasColumnEnd(1);

            softly.assertThat(warnings.get(2))
                    .hasFileName("C:/hudsonSlave/workspace/MyProject/Source/MoqExtensions.cs")
                    .hasCategory("SA1210")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage("Using directives must be sorted alphabetically by the namespaces.")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(4)
                    .hasLineEnd(4)
                    .hasColumnStart(1)
                    .hasColumnEnd(1);

            softly.assertThat(warnings.get(3))
                    .hasFileName("C:/hudsonSlave/workspace/MyProject/Source/MoqExtensions.cs")
                    .hasCategory("SA1208")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage("System using directives must be placed before all other using directives.")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(6)
                    .hasLineEnd(6)
                    .hasColumnStart(1)
                    .hasColumnEnd(1);

            softly.assertThat(warnings.get(4))
                    .hasFileName("C:/hudsonSlave/workspace/MyProject/Source/MoqExtensions.cs")
                    .hasCategory("SA1402")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage(
                            "A C# document may only contain a single class at the root level unless all of the classes are partial and are of the same type.")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(70)
                    .hasLineEnd(70)
                    .hasColumnStart(1)
                    .hasColumnEnd(1);
        }
    }

    /**
     * Parses a file with one warning of the MS Build tools (parallel build).
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-3582">Issue 3582</a>
     */
    @Test
    void issue6709() {
        var warnings = parse("issue6709.txt");

        assertThat(warnings).hasSize(1);
        assertThatReportHasSeverities(warnings, 0, 0, 1, 0);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasFileName("Rules/TaskRules.cs")
                    .hasCategory("CS0168")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage("The variable 'ex' is declared but never used")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(1145)
                    .hasLineEnd(1145)
                    .hasColumnStart(49)
                    .hasColumnEnd(49);
        }
    }

    /**
     * Parses a file with one warning of the MS Build tools that are started by ant.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-9926">Issue 9926</a>
     */
    @Test
    void issue9926() {
        var warnings = parse("issue9926.txt");

        assertThat(warnings).hasSize(1);
        assertThatReportHasSeverities(warnings, 0, 0, 1, 0);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasFileName(
                            "C:/jci/jobs/external_nvtristrip/workspace/compiler/cl/config/debug/platform/win32/tfields/live/external/nvtristrip/nvtristrip.cpp")
                    .hasCategory("C4706")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage("assignment within conditional expression")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(125)
                    .hasLineEnd(125)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);
        }
    }

    /**
     * Parses a file with warnings of the MS Build linker.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-4932">Issue 4932</a>
     */
    @Test
    void issue4932() {
        var warnings = parse("issue4932.txt");

        assertThat(warnings).hasSize(1);
        assertThatReportHasSeverities(warnings, 1, 0, 0, 0);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasFileName("SynchronisationHeure.obj")
                    .hasCategory("LNK2001")
                    .hasSeverity(Severity.ERROR)
                    .hasMessage("unresolved external symbol \"public:")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);
        }
    }

    /**
     * Parses a file with warnings of MS sharepoint.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-4731">Issue 4731</a>
     */
    @Test
    void issue4731() {
        var warnings = parse("issue4731.txt");

        assertThat(warnings).hasSize(11).hasDuplicatesSize(1);
        assertThatReportHasSeverities(warnings, 0, 0, 11, 0);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasFileName("C:/playpens/Catalyst/Platform/src/Ptc.Platform.Web/Package/Package.package")
                    .hasCategory("SPT6")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage(
                            "The Project Item \"StructureLibrary\" is included in the following Features: TypesAndLists, StructureBrowser")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);

            softly.assertThat(warnings.get(1))
                    .hasFileName("C:/playpens/Catalyst/Platform/src/Ptc.Platform.Web/Package/Package.package")
                    .hasCategory("SPT6")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage(
                            "The Project Item \"StructureViewWebPart\" is included in the following Features: PlatformWebParts, StructureBrowser")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);

            softly.assertThat(warnings.get(2))
                    .hasFileName(
                            "C:/playpens/Catalyst/Platform/src/Ptc.Platform.ShowcaseSiteTemplate/Package/Package.package")
                    .hasCategory("SPT6")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage(
                            "The Project Item \"TestPages\" is included in the following Features: SiteLibraryAndPages, DemoSite")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);

            softly.assertThat(warnings.get(3))
                    .hasFileName(
                            "C:/playpens/Catalyst/Platform/src/Ptc.Platform.ShowcaseSiteTemplate/Package/Package.package")
                    .hasCategory("SPT6")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage(
                            "The Project Item \"Test Items\" is included in the following Features: SiteLibraryAndPages, DemoSite")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);
        }
    }

    /**
     * MSBuildParser should also detect keywords 'Warning' and 'Error', as they are produced by the .NET-2.0 compiler of
     * VS2005.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-2383">Issue 2383</a>
     */
    @Test
    void shouldDetectKeywordsInRegexCaseInsensitive() {
        var warnings = parse("issue2383.txt");

        assertThat(warnings).hasSize(2);
        assertThatReportHasSeverities(warnings, 1, 0, 1, 0);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasFileName("Src/Parser/CSharp/cs.ATG")
                    .hasCategory("CS0168")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage("The variable 'type' is declared but never used")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(2242)
                    .hasLineEnd(2242)
                    .hasColumnStart(17)
                    .hasColumnEnd(17);

            softly.assertThat(warnings.get(1))
                    .hasFileName("C:/Src/Parser/CSharp/file.cs")
                    .hasCategory("XXX")
                    .hasSeverity(Severity.ERROR)
                    .hasMessage("An error occurred")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(10)
                    .hasLineEnd(10)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);
        }
    }

    /**
     * Update regular expression to detect logging prefixes like <pre>{@code 17:4>}</pre>.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-48647">Issue 48647</a>
     */
    @Test
    void issue48647() {
        var warnings = parse("issue48647.txt");

        assertThat(warnings).hasSize(1);
        assertThatReportHasSeverities(warnings, 0, 0, 1, 0);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasFileName("Filters/FilterBuilder.cs")
                    .hasCategory("CS0168")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage("There is maybe a mistake.")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(229)
                    .hasLineEnd(229)
                    .hasColumnStart(34)
                    .hasColumnEnd(34);
        }
    }

    /**
     * MSBuildParser should support messages when compiling with /MP.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-56450">Issue 56450</a>
     */
    @Test
    void issue56450() {
        var report = parse("issue56450.txt");

        try (var softly = new SoftAssertions()) {
            Iterator<Issue> iterator = report.iterator();
            softly.assertThat(report).hasSize(2);
            assertThatReportHasSeverities(report, 0, 0, 2, 0);
            softly.assertThat(iterator.next())
                    .hasFileName("C:/Jenkins/workspace/windows-kicad-msvc-tom/build/release/cpu/amd64/label/msvc/src/include/footprint_info.h")
                    .hasCategory("C4251")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage("'FOOTPRINT_ASYNC_LOADER::m_last_table': class 'std::basic_string<char,std::char_traits<char>,std::allocator<char>>' needs to have dll-interface to be used by clients of class 'FOOTPRINT_ASYNC_LOADER'")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(320)
                    .hasLineEnd(320);
            softly.assertThat(iterator.next())
                    .hasFileName(
                            "C:/Program Files (x86)/Microsoft Visual Studio/2017/BuildTools/VC/Tools/MSVC/14.16.27023/include/memory")
                    .hasCategory("C4244")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage("'argument': conversion from '_Ty' to 'int', possible loss of data")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(1801)
                    .hasLineEnd(1801);
        }
    }

    /**
     * Parses a file with false positives if a build project name contains info.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-57365">Issue 57365</a>
     */
    @Test
    void issue57365() {
        var warnings = parse("issue57365.txt");

        assertThat(warnings).isEmpty();
    }

    /**
     * Parser should not stop processing when parsing CMake output that contains text before
     * '-- Build files have been written to'.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse//JENKINS-66950">Issue 66950</a>
     */
    @Test
    void issue66950() {
        var report = parse("issue66950.txt");

        assertThat(report).isEmpty().doesNotHaveErrors();
    }

    /**
     * Parser should handle invalid filenames like &lt;command line option&gt; from PC-Lint.
     * Issues with invalid filenames should be filtered out, only valid file warnings should be parsed.
     *
     * @see <a href="https://issues.jenkins.io/browse/JENKINS-64541">Issue 64541</a>
     */
    @Test
    void issue64541() {
        var report = parse("issue64541-pclint.txt");

        // Should parse 1 issue (the valid one), and ignore the <command line option> issue
        assertThat(report).hasSize(1);

        assertThat(report.get(0))
                .hasFileName("D:/Project/source.c")
                .hasCategory("456")
                .hasSeverity(Severity.ERROR)
                .hasMessage("Actual error in file")
                .hasLineStart(42);
    }

    /**
     * Parse MSBuild-compatible diagnostic with (line,col,line,col).
     *
     * @see <a href="https://issues.jenkins.io/browse/JENKINS-67291">Issue 67291</a>
     */
    @Test
    void issue67291() {
        var report = parse("issue67291.txt");

        assertThat(report).hasSize(6);

        assertThat(report.get(0))
                .hasFileName("C:/Projects/EndLocation/Program.cs")
                .hasCategory("CS1584")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(5)
                .hasLineEnd(5)
                .hasColumnStart(0)
                .hasColumnEnd(0)
                .hasDescription("")
                .hasPackageName("-");

        assertThat(report.get(1))
                .hasFileName("C:/Projects/EndLocation/Program.cs")
                .hasCategory("CS1585")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(5)
                .hasLineEnd(5)
                .hasColumnStart(24)
                .hasColumnEnd(24)
                .hasDescription("")
                .hasPackageName("-");

        assertThat(report.get(2))
                .hasFileName("C:/Projects/EndLocation/Program.cs")
                .hasCategory("CS1586")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(5)
                .hasLineEnd(5)
                .hasColumnStart(0)
                .hasColumnEnd(0)
                .hasDescription("")
                .hasPackageName("-");

        assertThat(report.get(3))
                .hasFileName("C:/Projects/EndLocation/Program.cs")
                .hasCategory("CS1587")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(5)
                .hasLineEnd(5)
                .hasColumnStart(24)
                .hasColumnEnd(26)
                .hasDescription("")
                .hasPackageName("-");

        assertThat(report.get(4))
                .hasFileName("C:/Projects/EndLocation/Program.cs")
                .hasCategory("CS1588")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(5)
                .hasLineEnd(5)
                .hasColumnStart(24)
                .hasColumnEnd(24)
                .hasDescription("")
                .hasPackageName("-");

        assertThat(report.get(5))
                .hasFileName("C:/Projects/EndLocation/Program.cs")
                .hasCategory("CS1589")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(5)
                .hasLineEnd(5)
                .hasColumnStart(24)
                .hasColumnEnd(26)
                .hasDescription("")
                .hasPackageName("-");
    }

    /**
     * Do not ignore long lines while parsing MSBuild warning.
     *
     * @see <a href="https://issues.jenkins.io/browse/JENKINS-68744">Issue 68744</a>
     */
    @Test
    void issue68744() {
        var report = parse("issue68744.txt");

        assertThat(report).hasSize(1);

        assertThat(report.get(0))
                .hasFileName("C:/Program Files (x86)/Microsoft Visual Studio/2019/Professional/VC/Tools/MSVC/14.29.30133/include/type_traits")
                .hasCategory("C4267")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(1534)
                .hasLineEnd(1534)
                .hasColumnStart(1)
                .hasColumnEnd(1)
                .hasDescription("")
                .hasPackageName("-");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(8);
        assertThatReportHasSeverities(report, 4, 0, 3, 1);

        softly.assertThat(report.get(0))
                .hasFileName("Src/Parser/CSharp/cs.ATG")
                .hasCategory("CS0168")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("The variable 'type' is declared but never used")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(2242)
                .hasLineEnd(2242)
                .hasColumnStart(17)
                .hasColumnEnd(17);

        softly.assertThat(report.get(1))
                .hasFileName("C:/Src/Parser/CSharp/file.cs")
                .hasCategory("XXX")
                .hasSeverity(Severity.ERROR)
                .hasMessage("An error occurred")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(10)
                .hasLineEnd(10)
                .hasColumnStart(0)
                .hasColumnEnd(0);

        softly.assertThat(report.get(2))
                .hasFileName("Controls/MozItem.cs")
                .hasCategory("CS0618")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage(
                        "System.ComponentModel.Design.ComponentDesigner.OnSetComponentDefaults() : This method has been deprecated. Use InitializeNewComponent instead. http://go.microsoft.com/fwlink/?linkid=14202")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(1338)
                .hasLineEnd(1338)
                .hasColumnStart(4)
                .hasColumnEnd(4);

        softly.assertThat(report.get(3))
                .hasFileName("MediaPortal.cs")
                .hasCategory("CS0162")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("Hier kommt der Warnings Text")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(3001)
                .hasLineEnd(3001)
                .hasColumnStart(5)
                .hasColumnEnd(5);

        softly.assertThat(report.get(4))
                .hasFileName("x/a/b/include/abc.h")
                .hasCategory("C1083")
                .hasSeverity(Severity.ERROR)
                .hasMessage("Cannot open include file: xyz.h:...")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(18)
                .hasLineEnd(18)
                .hasColumnStart(0)
                .hasColumnEnd(0);

        softly.assertThat(report.get(5))
                .hasFileName("foo.h")
                .hasCategory("701")
                .hasSeverity(Severity.WARNING_LOW)
                .hasMessage("This is an info message from PcLint")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(5)
                .hasLineEnd(5)
                .hasColumnStart(0)
                .hasColumnEnd(0);

        softly.assertThat(report.get(6))
                .hasFileName("x/msbuild/normal/abc.h")
                .hasCategory("X3004")
                .hasSeverity(Severity.ERROR)
                .hasMessage("undeclared identifier")
                .hasPackageName("-")
                .hasLineStart(29)
                .hasLineEnd(29)
                .hasColumnStart(53)
                .hasColumnEnd(53);

        softly.assertThat(report.get(7))
                .hasFileName("x/msbuild/no/category/abc.h")
                .hasCategory("")
                .hasSeverity(Severity.ERROR)
                .hasMessage("use of undeclared identifier")
                .hasPackageName("-")
                .hasLineStart(334)
                .hasLineEnd(334)
                .hasColumnStart(3)
                .hasColumnEnd(3);
    }

    /**
     * MSBuildParser should capture Delphi hints and correctly identify the source file.
     *
     * @see <a href="https://issues.jenkins.io/browse/JENKINS-57789">Issue 57789</a>
     */
    @Test
    void issue57789() {
        var warnings = parse("issue57789.txt");

        assertThat(warnings).hasSize(4);
        assertThatReportHasSeverities(warnings, 0, 0, 3, 1);

        try (var softly = new SoftAssertions()) {
            // First warning - should report the actual Delphi file, not CodeGear.Delphi.Targets
            softly.assertThat(warnings.get(0))
                    .hasFileName("/Delphi Projects/DCU 2010/indy10/Lib/System/IdWship6.pas")
                    .hasCategory("W1000")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage("Symbol 'GIdIPv6FuncsAvailable' is deprecated")
                    .hasLineStart(469);

            // Second warning - same file, different line
            softly.assertThat(warnings.get(1))
                    .hasFileName("/Delphi Projects/DCU 2010/indy10/Lib/System/IdWship6.pas")
                    .hasCategory("W1000")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage("Symbol 'GIdIPv6FuncsAvailable' is deprecated")
                    .hasLineStart(1221);

            // Hint - should be captured and parsed correctly
            softly.assertThat(warnings.get(2))
                    .hasFileName("C:/Delphi Projects/DCU 2010/Externals/Lib/Delphi/IBSQL.pas")
                    .hasCategory("H2219")
                    .hasSeverity(Severity.WARNING_LOW)
                    .hasMessage("Private symbol 'GetCodePage' declared but never used")
                    .hasLineStart(205);

            // Fourth warning - different file
            softly.assertThat(warnings.get(3))
                    .hasFileName("/Program Files (x86)/Jenkins/workspace/POS_Manager_develop/Externals/Lib/Common/FBHStrUtils.pas")
                    .hasCategory("W1024")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage("Combining signed and unsigned types - widened both operands")
                    .hasLineStart(349);
        }
    }

    @Override
    protected MsBuildParser createParser() {
        return new MsBuildParser();
    }
}