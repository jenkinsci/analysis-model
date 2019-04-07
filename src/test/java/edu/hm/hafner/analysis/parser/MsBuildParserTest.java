package edu.hm.hafner.analysis.parser;

import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

import static edu.hm.hafner.analysis.assertj.Assertions.*;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link MsBuildParser}.
 */
class MsBuildParserTest extends AbstractParserTest {
    MsBuildParserTest() {
        super("msbuild.txt");
    }

    /**
     * Parses a file with false positive message.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-42823">Issue 42823</a>
     */
    @Test
    void issue42823() {
        Report warnings = parse("issue42823.txt");
        assertThat(warnings).isEmpty();
    }

    /**
     * MSBuildParser should make relative paths absolute if cmake is used.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-56193">Issue 56193</a>
     */
    @Test
    void issue56193() {
        Report warnings = parse("issue56193.log");

        assertThat(warnings).hasSize(1);

        assertSoftly(softly -> softly.assertThat(warnings.get(0))
                .hasFileName("G:/Jenkins-Alserver-Slave/workspace/ninjamgs/Lesson1/bubble.cpp")
                .hasCategory("C4101")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("'a': unreferenced local variable")
                .hasLineStart(17));
    }

    /**
     * MSBuildParser should make relative paths absolute, based on the project name listed in the message.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-56030">Issue 56030</a>
     */
    @Test
    void issue56030() {
        Report warnings = parse("issue56030.log");

        assertThat(warnings).hasSize(2);

        assertSoftly(softly -> softly.assertThat(warnings.get(0))
                .hasFileName("C:/DVR/workspace/_Branch_build_updates-SDSYGOEWO53Z6ASKV6W4GSRWQU4DXCNNDGKGTWMQJ4O7LTMGYQVQ/live555/transport/include/TransportRTCP.h")
                .hasCategory("C4275")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("non dll-interface class 'transport::RtcpSpec' used as base for dll-interface class 'transport::TransportRTCPInstance' (compiling source file transport\\source\\TransportH265VideoRTPSource.cpp)")
                .hasLineStart(39));
        assertSoftly(softly -> softly.assertThat(warnings.get(1))
                .hasFileName("C:/DVR/workspace/_Branch_build_updates-SDSYGOEWO53Z6ASKV6W4GSRWQU4DXCNNDGKGTWMQJ4O7LTMGYQVQ/live555/transport/include/TransportRTCP.h")
                .hasCategory("C4251")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("'transport::TransportRTCPInstance::m_CNAME': class 'transport::SDESItem' needs to have dll-interface to be used by clients of class 'transport::TransportRTCPInstance' (compiling source file transport\\source\\TransportH265VideoRTPSource.cpp)")
                .hasLineStart(155));
    }

    /**
     * MSBuildParser should make relative paths absolute, based on the project name listed in the message.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-38215">Issue 38215</a>
     */
    @Test
    void issue38215() {
        Report warnings = parse("issue38215.txt");

        assertThat(warnings)
                .hasSize(1)
                .hasSeverities(0, 0, 1, 0);

        assertSoftly(softly -> softly.assertThat(warnings.get(0))
                .hasFileName("C:/J/workspace/ci_windows/ws/build/rmw/test/test_error_handling.vcxproj")
                .hasCategory("D9002")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("ignoring unknown option '-std=c++11'")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasColumnStart(0)
                .hasColumnEnd(0));
    }

    /**
     * MSBuildParser should make relative paths absolute, based on the project name listed in the message.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-22386">Issue 22386</a>
     */
    @Test
    void issue22386() {
        Report warnings = parse("issue22386.txt");

        assertThat(warnings)
                .hasSize(2)
                .hasSeverities(0, 0, 2, 0);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasFileName("c:/solutiondir/projectdir/src/main.cpp")
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
                    .hasFileName("c:/solutiondir/projectdir/src/main.cpp")
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
        });
    }

    /**
     * MSBuildParser should support SASS messages with '-' and '_' in category.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-51485">Issue 51485</a>
     */
    @Test
    void issue51485() {
        Report warnings = parse("issue51485.txt");

        assertThat(warnings)
                .hasSize(2)
                .hasSeverities(0, 0, 2, 0);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasFileName("src/search-list.component.scss")
                    .hasCategory("shorthand-values")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage("Property `margin` should be written more concisely as `5px 0 0` instead of `5px 0 0 0`")
                    .hasLineStart(41).hasColumnStart(17);
            softly.assertThat(warnings.get(1))
                    .hasFileName("src/search-list.component.scss")
                    .hasCategory("shorthand_values")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage("Property `margin` should be written more concisely as `5px 0 0` instead of `5px 0 0 0`")
                    .hasLineStart(42).hasColumnStart(18);
        });
    }

    /**
     * MSBuildParser should also detect subcategories as described at <a href="http://blogs.msdn.com/b/msbuild/archive/2006/11/03/msbuild-visual-studio-aware-error-messages-and-message-formats.aspx">
     * MSBuild / Visual Studio aware error messages and message formats</a>.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-27914">Issue 27914</a>
     */
    @Test
    void issue27914() {
        Report warnings = parse("issue27914.txt");

        assertThat(warnings)
                .hasSize(3)
                .hasSeverities(0, 0, 3, 0);

        assertSoftly(softly -> {
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
        });
    }

    /**
     * Parses a file with a google-test failure that should not be shown as a warning.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-26441">Issue 26441</a>
     */
    @Test
    void issue26441() {
        Report warnings = parse("issue26441.txt");

        assertThat(warnings).isEmpty();
    }

    /**
     * Parses a file with gcc warnings that should be skipped.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-20544">Issue 20544</a>
     */
    @Test
    void issue20544() {
        Report warnings = parse("issue20544.txt");

        assertThat(warnings).isEmpty();
    }

    /**
     * Parses a file with  warnings of a Visual Studio analysis.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-20154">Issue 20154</a>
     */
    @Test
    void issue20154() {
        Report warnings = parse("issue20154.txt");

        assertThat(warnings)
                .hasSize(3)
                .hasDuplicatesSize(5)
                .hasSeverities(0, 0, 3, 0);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasCategory("CA2210")
                    .hasType("Microsoft.Design")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage("Sign 'SampleCodeAnalysis.exe' with a strong name key.")
                    .hasDescription("")
                    .hasFileName("-")
                    .hasPackageName("-")
                    .hasLineStart(0)
                    .hasLineStart(0)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);

            softly.assertThat(warnings.get(1))
                    .hasFileName("i:/devel/projects/SampleCodeAnalysis/SampleCodeAnalysis/Program.cs")
                    .hasCategory("CA1801")
                    .hasType("Microsoft.Usage")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage("Parameter 'args' of 'Program.Main(string[])' is never used. Remove the parameter or use it in the method body.")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(12)
                    .hasLineEnd(12)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);

            softly.assertThat(warnings.get(2))
                    .hasFileName("i:/devel/projects/SampleCodeAnalysis/SampleCodeAnalysis/Program.cs")
                    .hasCategory("CA1801")
                    .hasType("Microsoft.Usage")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage("Parameter 'args' of 'Program.Main(string[])' is never used. Remove the parameter or use it in the method body.")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(13)
                    .hasLineEnd(13)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);
        });
    }

    /**
     * Parses a file with 4 warnings of PCLint tools.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-14888">Issue 14888</a>
     */
    @Test
    void issue14888() {
        Report warnings = parse("issue14888.txt");

        assertThat(warnings)
                .hasSize(4)
                .hasSeverities(4, 0, 0, 0);

        assertSoftly(softly -> {
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
        });
    }

    /**
     * Parses a file with warnings of the MS Build tools.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-10566">Issue 10566</a>
     */
    @Test
    void issue10566() {
        Report warnings = parse("issue10566.txt");

        assertThat(warnings)
                .hasSize(1)
                .hasSeverities(1, 0, 0, 0);

        assertSoftly(softly -> softly.assertThat(warnings.get(0))
                .hasFileName("..//..//..//xx_Source//file.c")
                .hasCategory("c1083")
                .hasSeverity(Severity.ERROR)
                .hasMessage("cannot open include file: 'Header.h': No such file or directory")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(54)
                .hasLineEnd(54)
                .hasColumnStart(0)
                .hasColumnEnd(0));
    }

    /**
     * Parses a file with warnings of the MS Build tools.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-3582">Issue 3582</a>
     */
    @Test
    void issue3582() {
        Report warnings = parse("issue3582.txt");

        assertThat(warnings)
                .hasSize(1)
                .hasSeverities(1, 0, 0, 0);

        assertSoftly(softly -> softly.assertThat(warnings.get(0))
                .hasFileName("TestLib.lib")
                .hasCategory("LNK1181")
                .hasSeverity(Severity.ERROR)
                .hasMessage("cannot open input file 'TestLib.lib'")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasColumnStart(0)
                .hasColumnEnd(0));
    }

    /**
     * Parses a file with warnings of Stylecop.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-8347">Issue 8347</a>
     */
    @Test
    void issue8347() {
        Report warnings = parse("issue8347.txt");

        assertThat(warnings)
                .hasSize(5)
                .hasSeverities(0, 0, 5, 0);

        assertSoftly(softly -> {
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
        });
    }

    /**
     * Parses a file with one warning of the MS Build tools (parallel build).
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-3582">Issue 3582</a>
     */
    @Test
    void issue6709() {
        Report warnings = parse("issue6709.txt");

        assertThat(warnings)
                .hasSize(1)
                .hasSeverities(0, 0, 1, 0);

        assertSoftly(softly -> softly.assertThat(warnings.get(0))
                .hasFileName("Rules/TaskRules.cs")
                .hasCategory("CS0168")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("The variable 'ex' is declared but never used")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(1145)
                .hasLineEnd(1145)
                .hasColumnStart(49)
                .hasColumnEnd(49));
    }

    /**
     * Parses a file with one warning of the MS Build tools that are started by ant.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-9926">Issue 9926</a>
     */
    @Test
    void issue9926() {
        Report warnings = parse("issue9926.txt");

        assertThat(warnings)
                .hasSize(1)
                .hasSeverities(0, 0, 1, 0);

        assertSoftly(softly -> softly.assertThat(warnings.get(0))
                .hasFileName(
                        "c:/jci/jobs/external_nvtristrip/workspace/compiler/cl/config/debug/platform/win32/tfields/live/external/nvtristrip/nvtristrip.cpp")
                .hasCategory("C4706")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("assignment within conditional expression")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(125)
                .hasLineEnd(125)
                .hasColumnStart(0)
                .hasColumnEnd(0));
    }

    /**
     * Parses a file with warnings of the MS Build linker.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-4932">Issue 4932</a>
     */
    @Test
    void issue4932() {
        Report warnings = parse("issue4932.txt");

        assertThat(warnings)
                .hasSize(2)
                .hasSeverities(2, 0, 0, 0);

        assertSoftly(softly -> {
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

            softly.assertThat(warnings.get(1))
                    .hasFileName("Release/Navineo.exe")
                    .hasCategory("LNK1120")
                    .hasSeverity(Severity.ERROR)
                    .hasMessage("1 unresolved externals")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);
        });
    }

    /**
     * Parses a file with warnings of MS sharepoint.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-4731">Issue 4731</a>
     */
    @Test
    void issue4731() {
        Report warnings = parse("issue4731.txt");

        assertThat(warnings)
                .hasSize(11)
                .hasDuplicatesSize(1)
                .hasSeverities(0, 0, 11, 0);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasFileName("c:/playpens/Catalyst/Platform/src/Ptc.Platform.Web/Package/Package.package")
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
                    .hasFileName("c:/playpens/Catalyst/Platform/src/Ptc.Platform.Web/Package/Package.package")
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
                            "c:/playpens/Catalyst/Platform/src/Ptc.Platform.ShowcaseSiteTemplate/Package/Package.package")
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
                            "c:/playpens/Catalyst/Platform/src/Ptc.Platform.ShowcaseSiteTemplate/Package/Package.package")
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
        });
    }

    /**
     * MSBuildParser should also detect keywords 'Warning' and 'Error', as they are produced by the .NET-2.0 compiler of
     * VS2005.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-2383">Issue 2383</a>
     */
    @Test
    void shouldDetectKeywordsInRegexCaseInsensitive() {
        Report warnings = createParser().parse(createIssue2383File());

        assertThat(warnings)
                .hasSize(2)
                .hasSeverities(1, 0, 1, 0);

        assertSoftly(softly -> {
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
        });
    }

    private ReaderFactory createIssue2383File() {
        return createReaderFactory("file.txt", IOUtils.toInputStream("Src\\Parser\\CSharp\\cs.ATG (2242,17):  Warning"
                + " CS0168: The variable 'type' is declared but never used\r\nC:\\Src\\Parser\\CSharp\\file.cs"
                + " (10): Error XXX: An error occurred", StandardCharsets.UTF_8));
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report)
                .hasSize(8)
                .hasSeverities(4, 0, 3, 1);

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
                .hasDescription("")
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
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(334)
                .hasLineEnd(334)
                .hasColumnStart(3)
                .hasColumnEnd(3);
    }

    @Override
    protected MsBuildParser createParser() {
        return new MsBuildParser();
    }
}

