package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import static edu.hm.hafner.analysis.assertj.IssuesAssert.*;

/**
 * Tests the class {@link MsBuildParser}.
 */
@SuppressWarnings("ReuseOfLocalVariable")
public class MsBuildParserTest extends ParserTester {
    private static final String TYPE = new MsBuildParser().getId();

    /**
     * MSBuildParser should make relative paths absolute, based on the project name listed in the message.
     *
     * @throws IOException if the stream could not be read
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-38215">Issue 38215</a>
     */
    @Test
    public void issue38215() throws IOException {
        Issues warnings = new MsBuildParser().parse(openFile("issue38215.txt"));

        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(warnings).hasSize(1);
            softly.assertThat(warnings).hasNormalPrioritySize(1);

            Issue warning = warnings.get(0);
            softly.assertThat(warning)
                    .hasFileName("C:/J/workspace/ci_windows/ws/build/rmw/test/test_error_handling.vcxproj")
                    .hasCategory("D9002")
                    .hasType(TYPE)
                    .hasPriority(Priority.NORMAL)
                    .hasMessage("ignoring unknown option '-std=c++11'")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);
        });
    }

    /**
     * MSBuildParser should make relative paths absolute, based on the project name listed in the message.
     *
     * @throws IOException if the stream could not be read
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-22386">Issue 22386</a>
     */
    @Test
    public void issue22386() throws IOException {
        Issues warnings = new MsBuildParser().parse(openFile("issue22386.txt"));

        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(warnings).hasSize(2);
            softly.assertThat(warnings).hasNormalPrioritySize(2);

            Iterator<Issue> iterator = warnings.iterator();
            Issue warning = iterator.next();

            softly.assertThat(warning)
                    .hasFileName("c:/solutiondir/projectdir/src/main.cpp")
                    .hasCategory("C4996")
                    .hasType(TYPE)
                    .hasPriority(Priority.NORMAL)
                    .hasMessage("'_splitpath': This function or variable may be unsafe. Consider using _splitpath_s " +
                            "instead. To disable deprecation, use _CRT_SECURE_NO_WARNINGS. See online help for details.")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(97)
                    .hasLineEnd(97)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);

            warning = iterator.next();
            softly.assertThat(warning)
                    .hasFileName("c:/solutiondir/projectdir/src/main.cpp")
                    .hasCategory("C4996")
                    .hasType(TYPE)
                    .hasPriority(Priority.NORMAL)
                    .hasMessage("'strdup': The POSIX name for this item is deprecated. Instead, use the ISO C++ conformant name: _strdup. See online help for details.")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(99)
                    .hasLineEnd(99)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);
        });
    }

    /**
     * MSBuildParser should also detect subcategories as described at <a href="http://blogs.msdn.com/b/msbuild/archive/2006/11/03/msbuild-visual-studio-aware-error-messages-and-message-formats.aspx">
     * MSBuild / Visual Studio aware error messages and message formats</a>.
     *
     * @throws IOException if the stream could not be read
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-27914">Issue 27914</a>
     */
    @Test
    public void issue27914() throws IOException {
        Issues warnings = new MsBuildParser().parse(openFile("issue27914.txt"));

        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(warnings).hasSize(3);
            softly.assertThat(warnings).hasNormalPrioritySize(3);

            Iterator<Issue> iterator = warnings.iterator();
            Issue warning = iterator.next();

            softly.assertThat(warning)
                    .hasFileName("E:/workspace/TreeSize release nightly/DelphiLib/Jam.UI.Dialogs.pas")
                    .hasCategory("H2164")
                    .hasType(TYPE)
                    .hasPriority(Priority.NORMAL)
                    .hasMessage("Variable 'lTaskDialog' is declared but never used in 'TDialog.ShowTaskDialog'")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(4522)
                    .hasLineEnd(4522)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);

            warning = iterator.next();
            softly.assertThat(warning)
                    .hasFileName("E:/workspace/TreeSize release nightly/DelphiLib/Jam.UI.Dialogs.pas")
                    .hasCategory("H2164")
                    .hasType(TYPE)
                    .hasPriority(Priority.NORMAL)
                    .hasMessage("Variable 'lButton' is declared but never used in 'TDialog.ShowTaskDialog'")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(4523)
                    .hasLineEnd(4523)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);

            warning = iterator.next();
            softly.assertThat(warning)
                    .hasFileName("E:/workspace/TreeSize release nightly/DelphiLib/Jam.UI.Dialogs.pas")
                    .hasCategory("H2164")
                    .hasType(TYPE)
                    .hasPriority(Priority.NORMAL)
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
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-26441">Issue 26441</a>
     */
    @Test
    public void issue26441() throws IOException {
        Issues warnings = new MsBuildParser().parse(openFile("issue26441.txt"));

        assertThat(warnings).hasSize(0);
    }

    /**
     * Parses a file with gcc warnings that should be skipped.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-20544">Issue 20544</a>
     */
    @Test
    public void issue20544() throws IOException {
        Issues warnings = new MsBuildParser().parse(openFile("issue20544.txt"));

        assertThat(warnings).hasSize(0);
    }

    /**
     * Parses a file with  warnings of a Visual Studio analysis.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-20154">Issue 20154</a>
     */
    @Test
    public void issue20154() throws IOException {
        Issues warnings = new MsBuildParser().parse(openFile("issue20154.txt"));

        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(warnings).hasSize(8);
            softly.assertThat(warnings).hasNormalPrioritySize(8);

            Iterator<Issue> iterator = warnings.iterator();
            Issue warning = iterator.next();

            softly.assertThat(warning)
                    .hasFileName("MSBUILD")
                    .hasCategory("CA2210")
                    .hasType("Microsoft.Design")
                    .hasPriority(Priority.NORMAL)
                    .hasMessage("Sign 'SampleCodeAnalysis.exe' with a strong name key.")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(0)
                    .hasLineStart(0)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);

            warning = iterator.next();
            softly.assertThat(warning)
                    .hasFileName("i:/devel/projects/SampleCodeAnalysis/SampleCodeAnalysis/Program.cs")
                    .hasCategory("CA1801")
                    .hasType("Microsoft.Usage")
                    .hasPriority(Priority.NORMAL)
                    .hasMessage("Parameter 'args' of 'Program.Main(string[])' is never used. Remove the parameter or use it in the method body.")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(12)
                    .hasLineEnd(12)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);
        });
    }

    /**
     * Parses a file with 4 warnings of PCLint tools.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-14888">Issue 14888</a>
     */
    @Test
    public void issue14888() throws IOException {
        Issues warnings = new MsBuildParser().parse(openFile("issue14888.txt"));

        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(warnings).hasSize(4);
            softly.assertThat(warnings).hasHighPrioritySize(4);

            Iterator<Issue> iterator = warnings.iterator();
            Issue warning = iterator.next();

            softly.assertThat(warning)
                    .hasFileName("F:/AC_working/new-wlanac/ac/np/capwap/source/capwap_data.c")
                    .hasCategory("40")
                    .hasType(TYPE)
                    .hasPriority(Priority.HIGH)
                    .hasMessage("Undeclared identifier 'TRUE'")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(974)
                    .hasLineEnd(974)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);

            warning = iterator.next();
            softly.assertThat(warning)
                    .hasFileName("F:/AC_working/new-wlanac/ac/np/capwap/source/capwap_data.c")
                    .hasCategory("63")
                    .hasType(TYPE)
                    .hasPriority(Priority.HIGH)
                    .hasMessage("Expected an lvalue")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(974)
                    .hasLineEnd(974)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);

            warning = iterator.next();
            softly.assertThat(warning)
                    .hasFileName("F:/AC_working/new-wlanac/ac/np/capwap/source/capwap_data.c")
                    .hasCategory("40")
                    .hasType(TYPE)
                    .hasPriority(Priority.HIGH)
                    .hasMessage("Undeclared identifier 'pDstCwData'")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(975)
                    .hasLineEnd(975)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);

            warning = iterator.next();
            softly.assertThat(warning)
                    .hasFileName("F:/AC_working/new-wlanac/ac/np/capwap/source/capwap_data.c")
                    .hasCategory("10")
                    .hasType(TYPE)
                    .hasPriority(Priority.HIGH)
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
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-10566">Issue 10566</a>
     */
    @Test
    public void issue10566() throws IOException {
        Issues warnings = new MsBuildParser().parse(openFile("issue10566.txt"));

        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(warnings).hasSize(1);
            softly.assertThat(warnings).hasHighPrioritySize(1);

            Iterator<Issue> iterator = warnings.iterator();
            Issue warning = iterator.next();

            softly.assertThat(warning)
                    .hasFileName("..//..//..//xx_Source//file.c")
                    .hasCategory("c1083")
                    .hasType(TYPE)
                    .hasPriority(Priority.HIGH)
                    .hasMessage("cannot open include file: 'Header.h': No such file or directory")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(54)
                    .hasLineEnd(54)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);

            softly.assertThat(warning)
                    .hasFileName("..//..//..//xx_Source//file.c")
                    .hasCategory("c1083")
                    .hasType(TYPE)
                    .hasPriority(Priority.HIGH)
                    .hasMessage("cannot open include file: 'Header.h': No such file or directory")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(54)
                    .hasLineEnd(54)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);

            softly.assertThat(warning)
                    .hasFileName("..//..//..//xx_Source//file.c")
                    .hasCategory("c1083")
                    .hasType(TYPE)
                    .hasPriority(Priority.HIGH)
                    .hasMessage("cannot open include file: 'Header.h': No such file or directory")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(54)
                    .hasLineEnd(54)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);
        });
    }

    /**
     * Parses a file with warnings of the MS Build tools.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-3582">Issue 3582</a>
     */
    @Test
    public void issue3582() throws IOException {
        Issues warnings = new MsBuildParser().parse(openFile("issue3582.txt"));

        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(warnings).hasSize(1);
            softly.assertThat(warnings).hasHighPrioritySize(1);

            Issue warning = warnings.get(0);
            softly.assertThat(warning)
                    .hasFileName("TestLib.lib")
                    .hasCategory("LNK1181")
                    .hasType(TYPE)
                    .hasPriority(Priority.HIGH)
                    .hasMessage("cannot open input file 'TestLib.lib'")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);
        });
    }

    /**
     * Parses a file with warnings of Stylecop.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-8347">Issue 8347</a>
     */
    @Test
    public void issue8347() throws IOException {
        Issues warnings = new MsBuildParser().parse(openFile("issue8347.txt"));

        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(warnings).hasSize(5);
            softly.assertThat(warnings).hasNormalPrioritySize(5);

            Iterator<Issue> iterator = warnings.iterator();
            Issue warning = iterator.next();

            softly.assertThat(warning)
                    .hasFileName("C:/hudsonSlave/workspace/MyProject/Source/MoqExtensions.cs")
                    .hasCategory("SA1210")
                    .hasType(TYPE)
                    .hasPriority(Priority.NORMAL)
                    .hasMessage("Using directives must be sorted alphabetically by the namespaces.")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(2)
                    .hasLineEnd(2)
                    .hasColumnStart(1)
                    .hasColumnEnd(1);

            warning = iterator.next();
            softly.assertThat(warning)
                    .hasFileName("C:/hudsonSlave/workspace/MyProject/Source/MoqExtensions.cs")
                    .hasCategory("SA1210")
                    .hasType(TYPE)
                    .hasPriority(Priority.NORMAL)
                    .hasMessage("Using directives must be sorted alphabetically by the namespaces.")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(3)
                    .hasLineEnd(3)
                    .hasColumnStart(1)
                    .hasColumnEnd(1);

            warning = iterator.next();
            softly.assertThat(warning)
                    .hasFileName("C:/hudsonSlave/workspace/MyProject/Source/MoqExtensions.cs")
                    .hasCategory("SA1210")
                    .hasType(TYPE)
                    .hasPriority(Priority.NORMAL)
                    .hasMessage("Using directives must be sorted alphabetically by the namespaces.")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(4)
                    .hasLineEnd(4)
                    .hasColumnStart(1)
                    .hasColumnEnd(1);

            warning = iterator.next();
            softly.assertThat(warning)
                    .hasFileName("C:/hudsonSlave/workspace/MyProject/Source/MoqExtensions.cs")
                    .hasCategory("SA1208")
                    .hasType(TYPE)
                    .hasPriority(Priority.NORMAL)
                    .hasMessage("System using directives must be placed before all other using directives.")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(6)
                    .hasLineEnd(6)
                    .hasColumnStart(1)
                    .hasColumnEnd(1);

            warning = iterator.next();
            softly.assertThat(warning)
                    .hasFileName("C:/hudsonSlave/workspace/MyProject/Source/MoqExtensions.cs")
                    .hasCategory("SA1402")
                    .hasType(TYPE)
                    .hasPriority(Priority.NORMAL)
                    .hasMessage("A C# document may only contain a single class at the root level unless all of the classes are partial and are of the same type.")
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
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-3582">Issue 3582</a>
     */
    @Test
    public void issue6709() throws IOException {
        Issues warnings = new MsBuildParser().parse(openFile("issue6709.txt"));

        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(warnings).hasSize(1);
            softly.assertThat(warnings).hasNormalPrioritySize(1);

            Issue warning = warnings.get(0);
            softly.assertThat(warning)
                    .hasFileName("Rules/TaskRules.cs")
                    .hasCategory("CS0168")
                    .hasType(TYPE)
                    .hasPriority(Priority.NORMAL)
                    .hasMessage("The variable 'ex' is declared but never used")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(1145)
                    .hasLineEnd(1145)
                    .hasColumnStart(49)
                    .hasColumnEnd(49);
        });
    }

    /**
     * Parses a file with one warning of the MS Build tools that are started by ant.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-9926">Issue 9926</a>
     */
    @Test
    public void issue9926() throws IOException {
        Issues warnings = new MsBuildParser().parse(openFile("issue9926.txt"));

        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(warnings).hasSize(1);
            softly.assertThat(warnings).hasNormalPrioritySize(1);

            Issue warning = warnings.get(0);
            softly.assertThat(warning)
                    .hasFileName("c:/jci/jobs/external_nvtristrip/workspace/compiler/cl/config/debug/platform/win32/tfields/live/external/nvtristrip/nvtristrip.cpp")
                    .hasCategory("C4706")
                    .hasType(TYPE)
                    .hasPriority(Priority.NORMAL)
                    .hasMessage("assignment within conditional expression")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(125)
                    .hasLineEnd(125)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);
        });
    }

    /**
     * Parses a file with warnings of the MS Build linker.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-4932">Issue 4932</a>
     */
    @Test
    public void issue4932() throws IOException {
        Issues warnings = new MsBuildParser().parse(openFile("issue4932.txt"));

        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(warnings).hasSize(2);
            softly.assertThat(warnings).hasHighPrioritySize(2);

            Iterator<Issue> iterator = warnings.iterator();
            Issue warning = iterator.next();
            softly.assertThat(warning)
                    .hasFileName("SynchronisationHeure.obj")
                    .hasCategory("LNK2001")
                    .hasType(TYPE)
                    .hasPriority(Priority.HIGH)
                    .hasMessage("unresolved external symbol \"public:")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);

            warning = iterator.next();
            softly.assertThat(warning)
                    .hasFileName("Release/Navineo.exe")
                    .hasCategory("LNK1120")
                    .hasType(TYPE)
                    .hasPriority(Priority.HIGH)
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
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-4731">Issue 4731</a>
     */
    @Test
    public void issue4731() throws IOException {
        Issues warnings = new MsBuildParser().parse(openFile("issue4731.txt"));

        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(warnings).hasSize(12);
            softly.assertThat(warnings).hasNormalPrioritySize(12);

            Iterator<Issue> iterator = warnings.iterator();
            Issue warning = iterator.next();

            softly.assertThat(warning)
                    .hasFileName("c:/playpens/Catalyst/Platform/src/Ptc.Platform.Web/Package/Package.package")
                    .hasCategory("SPT6")
                    .hasType(TYPE)
                    .hasPriority(Priority.NORMAL)
                    .hasMessage("The Project Item \"StructureLibrary\" is included in the following Features: TypesAndLists, StructureBrowser")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);

            warning = iterator.next();
            softly.assertThat(warning)
                    .hasFileName("c:/playpens/Catalyst/Platform/src/Ptc.Platform.Web/Package/Package.package")
                    .hasCategory("SPT6")
                    .hasType(TYPE)
                    .hasPriority(Priority.NORMAL)
                    .hasMessage("The Project Item \"StructureViewWebPart\" is included in the following Features: PlatformWebParts, StructureBrowser")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);

            warning = iterator.next();
            softly.assertThat(warning)
                    .hasFileName("c:/playpens/Catalyst/Platform/src/Ptc.Platform.ShowcaseSiteTemplate/Package/Package.package")
                    .hasCategory("SPT6")
                    .hasType(TYPE)
                    .hasPriority(Priority.NORMAL)
                    .hasMessage("The Project Item \"TestPages\" is included in the following Features: SiteLibraryAndPages, DemoSite")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);

            warning = iterator.next();
            softly.assertThat(warning)
                    .hasFileName("c:/playpens/Catalyst/Platform/src/Ptc.Platform.ShowcaseSiteTemplate/Package/Package.package")
                    .hasCategory("SPT6")
                    .hasType(TYPE)
                    .hasPriority(Priority.NORMAL)
                    .hasMessage("The Project Item \"Test Items\" is included in the following Features: SiteLibraryAndPages, DemoSite")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);
        });
    }

    /**
     * Parses a file with warnings of the MS Build tools.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void parseWarnings() throws IOException {
        Issues warnings = new MsBuildParser().parse(openFile());

        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(warnings).hasSize(6);
            softly.assertThat(warnings).hasHighPrioritySize(2);
            softly.assertThat(warnings).hasNormalPrioritySize(3);
            softly.assertThat(warnings).hasLowPrioritySize(1);

            Iterator<Issue> iterator = warnings.iterator();
            Issue warning = iterator.next();

            softly.assertThat(warning)
                    .hasFileName("Src/Parser/CSharp/cs.ATG")
                    .hasCategory("CS0168")
                    .hasType(TYPE)
                    .hasPriority(Priority.NORMAL)
                    .hasMessage("The variable 'type' is declared but never used")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(2242)
                    .hasLineEnd(2242)
                    .hasColumnStart(17)
                    .hasColumnEnd(17);

            warning = iterator.next();
            softly.assertThat(warning)
                    .hasFileName("C:/Src/Parser/CSharp/file.cs")
                    .hasCategory("XXX")
                    .hasType(TYPE)
                    .hasPriority(Priority.HIGH)
                    .hasMessage("An error occurred")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(10)
                    .hasLineEnd(10)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);

            warning = iterator.next();
            softly.assertThat(warning)
                    .hasFileName("Controls/MozItem.cs")
                    .hasCategory("CS0618")
                    .hasType(TYPE)
                    .hasPriority(Priority.NORMAL)
                    .hasMessage("System.ComponentModel.Design.ComponentDesigner.OnSetComponentDefaults() : " +
                            "This method has been deprecated. Use InitializeNewComponent instead. " +
                            "http://go.microsoft.com/fwlink/?linkid=14202")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(1338)
                    .hasLineEnd(1338)
                    .hasColumnStart(4)
                    .hasColumnEnd(4);

            warning = iterator.next();
            softly.assertThat(warning)
                    .hasFileName("MediaPortal.cs")
                    .hasCategory("CS0162")
                    .hasType(TYPE)
                    .hasPriority(Priority.NORMAL)
                    .hasMessage("Hier kommt der Warnings Text")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(3001)
                    .hasLineEnd(3001)
                    .hasColumnStart(5)
                    .hasColumnEnd(5);

            warning = iterator.next();
            softly.assertThat(warning)
                    .hasFileName("x/a/b/include/abc.h")
                    .hasCategory("C1083")
                    .hasType(TYPE)
                    .hasPriority(Priority.HIGH)
                    .hasMessage("Cannot open include file: xyz.h:...")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(18)
                    .hasLineEnd(18)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);

            warning = iterator.next();
            softly.assertThat(warning)
                    .hasFileName("foo.h")
                    .hasCategory("701")
                    .hasType(TYPE)
                    .hasPriority(Priority.LOW)
                    .hasMessage("This is an info message from PcLint")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(5)
                    .hasLineEnd(5)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);
        });
    }

    /**
     * MSBuildParser should also detect keywords 'Warning' and 'Error', as they are produced by the .NET-2.0 compiler of
     * VS2005.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-2383">Issue 2383</a>
     */
    @Test
    public void shouldDetectKeywordsInRegexCaseInsensitive() throws IOException {
        String containsErrorAndWarningKeywords =
                "Src\\Parser\\CSharp\\cs.ATG (2242,17):  Warning CS0168: The variable 'type' is declared but never used" +
                "\r\n" +
                "C:\\Src\\Parser\\CSharp\\file.cs (10): Error XXX: An error occurred";

        Issues warnings = new MsBuildParser().parse(new InputStreamReader(
                IOUtils.toInputStream(containsErrorAndWarningKeywords, "UTF-8")
        ));

        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(warnings).hasSize(2);
            softly.assertThat(warnings).hasHighPrioritySize(1);
            softly.assertThat(warnings).hasNormalPrioritySize(1);

            Iterator<Issue> iterator = warnings.iterator();
            Issue warning = iterator.next();

            softly.assertThat(warning)
                    .hasFileName("Src/Parser/CSharp/cs.ATG")
                    .hasCategory("CS0168")
                    .hasType(TYPE)
                    .hasPriority(Priority.NORMAL)
                    .hasMessage("The variable 'type' is declared but never used")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(2242)
                    .hasLineEnd(2242)
                    .hasColumnStart(17)
                    .hasColumnEnd(17);

            warning = iterator.next();
            softly.assertThat(warning)
                    .hasFileName("C:/Src/Parser/CSharp/file.cs")
                    .hasCategory("XXX")
                    .hasType(TYPE)
                    .hasPriority(Priority.HIGH)
                    .hasMessage("An error occurred")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(10)
                    .hasLineEnd(10)
                    .hasColumnStart(0)
                    .hasColumnEnd(0);
        });
    }

    @Override
    protected String getWarningsFile() {
        return "msbuild.txt";
    }
}

