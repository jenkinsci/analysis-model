package edu.hm.hafner.analysis.registry;

import java.util.Arrays;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.FileReaderFactory;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.util.ResourceTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Integration tests of all parsers of the warnings plug-in in pipelines.
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings({"PMD.CouplingBetweenObjects", "PMD.ExcessivePublicCount", "PMD.CyclomaticComplexity", "PMD.GodClass", "ClassDataAbstractionCoupling", "ClassFanOutComplexity"})
public class ParsersTest extends ResourceTest {
    private static final String CODE_FRAGMENT = "<pre><code>#\n"
            + "\n"
            + "    ERROR HANDLING: N/A\n"
            + "    #\n"
            + "    REMARKS: N/A\n"
            + "    #\n"
            + "    ****************************** END HEADER *************************************\n"
            + "    #\n"
            + "\n"
            + "    ***************************** BEGIN PDL ***************************************\n"
            + "    #\n"
            + "    ****************************** END PDL ****************************************\n"
            + "    #\n"
            + "\n"
            + "    ***************************** BEGIN CODE **************************************\n"
            + "    **\n"
            + "    *******************************************************************************\n"
            + "\n"
            + "    *******************************************************************************\n"
            + "    *******************************************************************************\n"
            + "\n"
            + "if [ $# -lt 3 ]\n"
            + "then\n"
            + "exit 1\n"
            + "fi\n"
            + "\n"
            + "    *******************************************************************************\n"
            + "    initialize local variables\n"
            + "    shift input parameter (twice) to leave only files to copy\n"
            + "    *******************************************************************************\n"
            + "\n"
            + "files&#61;&#34;&#34;\n"
            + "shift\n"
            + "shift\n"
            + "\n"
            + "    *******************************************************************************\n"
            + "    *******************************************************************************\n"
            + "\n"
            + "for i in $*\n"
            + "do\n"
            + "files&#61;&#34;$files $directory/$i&#34;\n"
            + "done</code></pre>";

    /** Verifies that a broken file does not fail. */
    @Test
    public void shouldSilentlyIgnoreWrongFile() {
        assertThatExceptionOfType(ParsingException.class).isThrownBy(() ->
                shouldFindIssuesOfTool(0, "checkstyle", "CargoCheck.json"));
    }

    /**
     * Runs with several tools that internally delegate to CheckStyle's  parser on an output file that contains 6
     * issues.
     */
    @Test
    public void shouldFindAllIssuesForCheckStyleAlias() {
        for (String tool : Arrays.asList("detekt", "eslint", "ktlint", "php-code-sniffer",
                "swiftlint", "tslint")) {
            shouldFindIssuesOfTool(4, tool, "checkstyle.xml");
        }
    }

    /** Runs the Iar parser on an output file that contains 8 issues. */
    @Test
    public void shouldFindAllCmakeIssues() {
        shouldFindIssuesOfTool(8, "cmake", "cmake.txt");
    }

    /** Runs the Iar parser on an output file that contains 2 issues. */
    @Test
    public void shouldFindAllCargoIssues() {
        shouldFindIssuesOfTool(2, "cargo", "CargoCheck.json");
    }

    /** Runs the Iar parser on an output file that contains 262 issues. */
    @Test
    public void shouldFindAllIssuesForPmdAlias() {
        shouldFindIssuesOfTool(262, "infer", "pmd-6.xml");
    }

    /** Runs the Iar parser on an output file that contains 262 issues. */
    @Test
    public void shouldFindAllIssuesForMsBuildAlias() {
        shouldFindIssuesOfTool(8, "pclint", "msbuild.txt");
    }

    /** Runs the Iar parser on an output file that contains 4 issues. */
    @Test
    public void shouldFindAllYamlLintIssues() {
        shouldFindIssuesOfTool(4, "yamllint", "yamllint.txt");
    }

    /** Runs the Iar parser on an output file that contains 6 issues. */
    @Test
    public void shouldFindAllIarIssues() {
        shouldFindIssuesOfTool(6, "iar", "iar.txt");
    }

    /** Runs the IbLinter parser on an output file that contains 1 issue. */
    @Test
    public void shouldFindAllIbLinterIssues() {
        shouldFindIssuesOfTool(1, "iblinter", "iblinter.xml");
    }

    /** Runs the IarCStat parser on an output file that contains 6 issues. */
    @Test
    public void shouldFindAllIarCStatIssues() {
        shouldFindIssuesOfTool(6, "iar-cstat", "iar-cstat.txt");
    }

    /** Runs the SonarQube parsers on two files that contains 6 and 31 issues. */
    @Test
    public void shouldFindAllSonarQubeIssues() {
        shouldFindIssuesOfTool(32, "sonar", "sonarqube-api.json");
        shouldFindIssuesOfTool(6, "sonar-diff", "sonarqube-differential.json");
    }

    /** Runs the TagList parser on an output file that contains 6 issues. */
    @Test
    public void shouldFindAllTagListIssues() {
        shouldFindIssuesOfTool(4, "taglist", "taglist.xml");
    }

    /** Runs the Ccm parser on an output file that contains 6 issues. */
    @Test
    public void shouldFindAllCcmIssues() {
        shouldFindIssuesOfTool(6, "ccm", "ccm.xml");
    }

    /** Runs the ruboCop parser on an output file that contains 2 issues. */
    @Test
    public void shouldFindAllRuboCopIssues() {
        shouldFindIssuesOfTool(3, "rubocop", "rubocop.log");
    }

    /** Runs the flawfinder parser on an output file that contains 3 issues. */
    @Test
    public void shouldFindAllFlawfinderIssues() {
        shouldFindIssuesOfTool(3, "flawfinder", "flawfinder.log");
    }

    /** Runs the Android Lint parser on an output file that contains 2 issues. */
    @Test
    public void shouldFindAllAndroidLintIssues() {
        shouldFindIssuesOfTool(2, "android-lint", "android-lint.xml");
    }

    /** Runs the CodeNarc parser on an output file that contains 11 issues. */
    @Test
    public void shouldFindAllCodeNArcIssues() {
        shouldFindIssuesOfTool(11, "codenarc", "codeNarc.xml");
    }

    /** Runs the Cppcheck parser on an output file that contains 3 issues. */
    @Test
    public void shouldFindAllCppCheckIssues() {
        shouldFindIssuesOfTool(3, "cppcheck", "cppcheck.xml");
    }

    /** Runs the DocFx parser on an output file that contains 3 issues. */
    @Test
    public void shouldFindAllDocFXIssues() {
        shouldFindIssuesOfTool(3, "docfx", "docfx.json");
    }

    /** Runs the ErrorProne parser on output files that contain 9 + 2 issues. */
    @Test
    public void shouldFindAllErrorProneIssues() {
        shouldFindIssuesOfTool(9, "error-prone", "errorprone-maven.log");
        shouldFindIssuesOfTool(2, "gradle-error-prone", "gradle-error-prone.log");
        // FIXME: multiple
        shouldFindIssuesOfTool(9 + 2, "error-prone", "errorprone-maven.log", "gradle-error-prone.log");
    }

    /** Runs the Flake8 parser on an output file that contains 12 issues. */
    @Test
    public void shouldFindAllFlake8Issues() {
        shouldFindIssuesOfTool(12, "flake8", "flake8.txt");
    }

    /** Runs the JSHint parser on an output file that contains 6 issues. */
    @Test
    public void shouldFindAllJsHintIssues() {
        shouldFindIssuesOfTool(6, "js-hint", "jshint.xml");
    }

    /**
     * Runs the JUnit parser on an output file that contains 2 and 1 issues.
     */
    @Test
    public void shouldFindAllJUnitIssues() {
        shouldFindIssuesOfTool(2, "junit", "junit.xml");

        shouldFindIssuesOfTool(1, "junit", "TEST-org.jenkinsci.plugins.jvctb.perform.JvctbPerformerTest.xml");
    }

    /** Runs the Klocwork parser on an output file that contains 2 issues. */
    @Test
    public void shouldFindAllKlocWorkIssues() {
        shouldFindIssuesOfTool(2, "klocwork", "klocwork.xml");
    }

    /** Runs the MyPy parser on an output file that contains 5 issues. */
    @Test
    public void shouldFindAllMyPyIssues() {
        shouldFindIssuesOfTool(5, "mypy", "mypy.txt");
    }

    /** Runs the PIT parser on an output file that contains 25 issues. */
    @Test
    public void shouldFindAllPitIssues() {
        shouldFindIssuesOfTool(2, "pit", "pit.xml");
    }

    /** Runs the PyDocStyle parser on an output file that contains 33 issues. */
    @Test
    public void shouldFindAllPyDocStyleIssues() {
        shouldFindIssuesOfTool(33, "pydocstyle", "pydocstyle.txt");
    }

    /** Runs the XML Lint parser on an output file that contains 3 issues. */
    @Test
    public void shouldFindAllXmlLintStyleIssues() {
        shouldFindIssuesOfTool(3, "xmllint", "xmllint.txt");
    }

    /** Runs the zptlint parser on an output file that contains 2 issues. */
    @Test
    public void shouldFindAllZptLintStyleIssues() {
        shouldFindIssuesOfTool(2, "zptlint", "zptlint.log");
    }

    /** Runs the CPD parser on an output file that contains 2 issues. */
    @Test
    public void shouldFindAllCpdIssues() {
        String cpd = "cpd";
        Report report = shouldFindIssuesOfTool(2, cpd, "cpd.xml");
        assertThatDescriptionOfIssueIsSet(cpd, report.get(0), CODE_FRAGMENT);
    }

    /** Runs the Simian parser on an output file that contains 4 issues. */
    @Test
    public void shouldFindAllSimianIssues() {
        shouldFindIssuesOfTool(4, "simian", "simian.xml");
    }

    /** Runs the DupFinder parser on an output file that contains 2 issues. */
    @Test
    public void shouldFindAllDupFinderIssues() {
        String dupfinder = "dupfinder";
        Report report = shouldFindIssuesOfTool(2, dupfinder, "dupfinder.xml");
        assertThatDescriptionOfIssueIsSet(dupfinder, report.get(0),
                "<pre><code>if (items &#61;&#61; null) throw new ArgumentNullException(&#34;items&#34;);</code></pre>");
    }

    /** Runs the Armcc parser on output files that contain 3 + 3 issues. */
    @Test
    public void shouldFindAllArmccIssues() {
        shouldFindIssuesOfTool(3, "armcc", "armcc.txt");
        shouldFindIssuesOfTool(3, "armcc5", "armcc5.txt");
    }

    /** Runs the Buckminster parser on an output file that contains 3 issues. */
    @Test
    public void shouldFindAllBuckminsterIssues() {
        shouldFindIssuesOfTool(3, "buckminster", "buckminster.txt");
    }

    /** Runs the Cadence parser on an output file that contains 3 issues. */
    @Test
    public void shouldFindAllCadenceIssues() {
        shouldFindIssuesOfTool(3, "cadence", "CadenceIncisive.txt");
    }

    /** Runs the Mentor parser on an output file that contains 12 issues. */
    @Test
    public void shouldFindAllMentorGraphicsIssues() {
        shouldFindIssuesOfTool(12, "modelsim", "MentorGraphics.log");
    }

    /** Runs the PMD parser on an output file that contains 262 issues (PMD 6.1.0). */
    @Test
    public void shouldFindAllPmdIssues() {
        String pmd = "pmd";
        Report report = shouldFindIssuesOfTool(262, pmd, "pmd-6.xml");
        assertThatDescriptionOfIssueIsSet(pmd, report.get(0),
                "A high number of imports can indicate a high degree of coupling within an object.");
    }

    /** Runs the CheckStyle parser on an output file that contains 6 issues. */
    @Test
    public void shouldFindAllCheckStyleIssues() {
        String checkstyle = "checkstyle";
        Report report = shouldFindIssuesOfTool(4, checkstyle, "checkstyle.xml");

        assertThatDescriptionOfIssueIsSet(checkstyle, report.get(2),
                "<p>Since Checkstyle 3.1</p><p>");
        assertThatDescriptionOfIssueIsSet(checkstyle, report.get(2),
                "The check finds classes that are designed for extension (subclass creation).");
    }

    private void assertThatDescriptionOfIssueIsSet(final String tool, final Issue issue,
            final String expectedDescription) {
        ParserRegistry parserRegistry = new ParserRegistry();
        ParserDescriptor descriptor = parserRegistry.get(tool);

        assertThat(issue).hasDescription("");
        Assertions.assertThat(descriptor.getDescription(issue)).contains(expectedDescription);
    }

    /** Runs the FindBugs parser on an output file that contains 2 issues. */
    @Test
    public void shouldFindAllFindBugsIssues() {
        String findbugs = "findbugs";
        Report report = shouldFindIssuesOfTool(2, findbugs, "findbugs-native.xml");

        assertThatDescriptionOfIssueIsSet(findbugs, report.get(0),
                "<p> The fields of this class appear to be accessed inconsistently with respect\n"
                        + "  to synchronization.&nbsp; This bug report indicates that the bug pattern detector\n"
                        + "  judged that\n"
                        + "  </p>\n"
                        + "  <ul>\n"
                        + "  <li> The class contains a mix of locked and unlocked accesses,</li>\n"
                        + "  <li> The class is <b>not</b> annotated as javax.annotation.concurrent.NotThreadSafe,</li>\n"
                        + "  <li> At least one locked access was performed by one of the class's own methods, and</li>\n"
                        + "  <li> The number of unsynchronized field accesses (reads and writes) was no more than\n"
                        + "       one third of all accesses, with writes being weighed twice as high as reads</li>\n"
                        + "  </ul>\n"
                        + "\n"
                        + "  <p> A typical bug matching this bug pattern is forgetting to synchronize\n"
                        + "  one of the methods in a class that is intended to be thread-safe.</p>\n"
                        + "\n"
                        + "  <p> You can select the nodes labeled \"Unsynchronized access\" to show the\n"
                        + "  code locations where the detector believed that a field was accessed\n"
                        + "  without synchronization.</p>\n"
                        + "\n"
                        + "  <p> Note that there are various sources of inaccuracy in this detector;\n"
                        + "  for example, the detector cannot statically detect all situations in which\n"
                        + "  a lock is held.&nbsp; Also, even when the detector is accurate in\n"
                        + "  distinguishing locked vs. unlocked accesses, the code in question may still\n"
                        + "  be correct.</p>");
    }

    /** Runs the SpotBugs parser on an output file that contains 2 issues. */
    @Test
    public void shouldFindAllSpotBugsIssues() {
        String expectedDescription =
                "<p>This code calls a method and ignores the return value. However our analysis shows that\n"
                        + "the method (including its implementations in subclasses if any) does not produce any effect\n"
                        + "other than return value. Thus this call can be removed.\n"
                        + "</p>\n"
                        + "<p>We are trying to reduce the false positives as much as possible, but in some cases this warning might be wrong.\n"
                        + "Common false-positive cases include:</p>\n"
                        + "<p>- The method is designed to be overridden and produce a side effect in other projects which are out of the scope of the analysis.</p>\n"
                        + "<p>- The method is called to trigger the class loading which may have a side effect.</p>\n"
                        + "<p>- The method is called just to get some exception.</p>\n"
                        + "<p>If you feel that our assumption is incorrect, you can use a @CheckReturnValue annotation\n"
                        + "to instruct SpotBugs that ignoring the return value of this method is acceptable.\n"
                        + "</p>";

        String spotBugs = "spotbugs";
        Report report = shouldFindIssuesOfTool(2, spotBugs, "spotbugsXml.xml");
        assertThatDescriptionOfIssueIsSet(spotBugs, report.get(0), expectedDescription);
    }

    /** Runs the SpotBugs parser on an output file that contains 2 issues. */
    @Test
    public void shouldProvideMessagesAndDescriptionForSecurityIssuesWithSpotBugs() {
        String expectedDescription =
                "<p>A file is opened to read its content. The filename comes from an <b>input</b> parameter.\n"
                        + "If an unfiltered parameter is passed to this file API, files from an arbitrary filesystem location could be read.</p>\n"
                        + "<p>This rule identifies <b>potential</b> path traversal vulnerabilities. In many cases, the constructed file path cannot be controlled\n"
                        + "by the user. If that is the case, the reported instance is a false positive.</p>";

        String spotBugs = "spotbugs";
        Report report = shouldFindIssuesOfTool(1, spotBugs, "issue55707.xml");
        Issue issue = report.get(0);
        assertThatDescriptionOfIssueIsSet(spotBugs, issue, expectedDescription);
        assertThat(issue).hasMessage(
                "java/nio/file/Paths.get(Ljava/lang/String;[Ljava/lang/String;)Ljava/nio/file/Path; reads a file whose location might be specified by user input");
    }

    /** Runs the Clang-Analyzer parser on an output file that contains 3 issues. */
    @Test
    public void shouldFindAllClangAnalyzerIssues() {
        shouldFindIssuesOfTool(3, "clang-analyzer", "ClangAnalyzer.txt");
    }

    /** Runs the Clang-Tidy parser on an output file that contains 6 issues. */
    @Test
    public void shouldFindAllClangTidyIssues() {
        shouldFindIssuesOfTool(7, "clang-tidy", "ClangTidy.txt");
    }

    /** Runs the Clang parser on an output file that contains 9 issues. */
    @Test
    public void shouldFindAllClangIssues() {
        shouldFindIssuesOfTool(9, "clang", "apple-llvm-clang.txt");
    }

    /** Runs the Coolflux parser on an output file that contains 1 issues. */
    @Test
    public void shouldFindAllCoolfluxIssues() {
        shouldFindIssuesOfTool(1, "coolflux", "coolfluxchesscc.txt");
    }

    /** Runs the CppLint parser on an output file that contains 1031 issues. */
    @Test
    public void shouldFindAllCppLintIssues() {
        shouldFindIssuesOfTool(1031, "cpplint", "cpplint.txt");
    }

    /** Runs the CodeAnalysis parser on an output file that contains 3 issues. */
    @Test
    public void shouldFindAllCodeAnalysisIssues() {
        shouldFindIssuesOfTool(3, "code-analysis", "codeanalysis.txt");
    }

    /** Runs the DScanner parser on an output file that contains 4 issues. */
    @Test
    public void shouldFindAllDScannerIssues() {
        shouldFindIssuesOfTool(4, "dscanner", "dscanner-report.json");
    }

    /** Runs the GoLint parser on an output file that contains 7 issues. */
    @Test
    public void shouldFindAllGoLintIssues() {
        shouldFindIssuesOfTool(7, "golint", "golint.txt");
    }

    /** Runs the GoVet parser on an output file that contains 2 issues. */
    @Test
    public void shouldFindAllGoVetIssues() {
        shouldFindIssuesOfTool(2, "go-vet", "govet.txt");
    }

    /** Runs the SunC parser on an output file that contains 8 issues. */
    @Test
    public void shouldFindAllSunCIssues() {
        shouldFindIssuesOfTool(8, "sunc", "sunc.txt");
    }

    /** Runs the JcReport parser on an output file that contains 6 issues. */
    @Test
    public void shouldFindAllJcReportIssues() {
        shouldFindIssuesOfTool(6, "jc-report", "jcreport.xml");
    }

    /** Runs the StyleCop parser on an output file that contains 5 issues. */
    @Test
    public void shouldFindAllStyleCopIssues() {
        shouldFindIssuesOfTool(5, "stylecop", "stylecop.xml");
    }

    /** Runs the Tasking VX parser on an output file that contains 8 issues. */
    @Test
    public void shouldFindAllTaskingVxIssues() {
        shouldFindIssuesOfTool(8, "tasking-vx", "tasking-vx.txt");
    }

    /** Runs the tnsdl translator parser on an output file that contains 4 issues. */
    @Test
    public void shouldFindAllTnsdlIssues() {
        shouldFindIssuesOfTool(4, "tnsdl", "tnsdl.txt");
    }

    /** Runs the Texas Instruments Code Composer Studio parser on an output file that contains 10 issues. */
    @Test
    public void shouldFindAllTiCssIssues() {
        shouldFindIssuesOfTool(10, "code-composer", "ticcs.txt");
    }

    /** Runs the IBM XLC compiler and linker parser on an output file that contains 1 + 1 issues. */
    @Test
    public void shouldFindAllXlcIssues() {
        shouldFindIssuesOfTool(1, "xlc", "xlc.txt");
    }

    /** Runs the YIU compressor parser on an output file that contains 3 issues. */
    @Test
    public void shouldFindAllYuiCompressorIssues() {
        shouldFindIssuesOfTool(4, "yui", "yui.txt");
    }

    /** Runs the Erlc parser on an output file that contains 2 issues. */
    @Test
    public void shouldFindAllErlcIssues() {
        shouldFindIssuesOfTool(2, "erlc", "erlc.txt");
    }

    /** Runs the FlexSdk parser on an output file that contains 5 issues. */
    @Test
    public void shouldFindAllFlexSDKIssues() {
        shouldFindIssuesOfTool(5, "flex", "flexsdk.txt");
    }

    /** Runs the FxCop parser on an output file that contains 2 issues. */
    @Test
    public void shouldFindAllFxcopSDKIssues() {
        shouldFindIssuesOfTool(2, "fxcop", "fxcop.xml");
    }

    /** Runs the Gendarme parser on an output file that contains 3 issues. */
    @Test
    public void shouldFindAllGendarmeIssues() {
        shouldFindIssuesOfTool(3, "gendarme", "Gendarme.xml");
    }

    /** Runs the GhsMulti parser on an output file that contains 3 issues. */
    @Test
    public void shouldFindAllGhsMultiIssues() {
        shouldFindIssuesOfTool(6, "ghs-multi", "ghsmulti.txt");
    }

    /**
     * Runs the Gnat parser on an output file that contains 9 issues.
     */
    @Test
    public void shouldFindAllGnatIssues() {
        shouldFindIssuesOfTool(9, "gnat", "gnat.txt");
    }

    /** Runs the GnuFortran parser on an output file that contains 4 issues. */
    @Test
    public void shouldFindAllGnuFortranIssues() {
        shouldFindIssuesOfTool(4, "fortran", "GnuFortran.txt");
    }

    /** Runs the MsBuild parser on an output file that contains 6 issues. */
    @Test
    public void shouldFindAllMsBuildIssues() {
        shouldFindIssuesOfTool(8, "msbuild", "msbuild.txt");
    }

    /** Runs the NagFortran parser on an output file that contains 10 issues. */
    @Test
    public void shouldFindAllNagFortranIssues() {
        shouldFindIssuesOfTool(14, "nag-fortran", "NagFortran.txt");
    }

    /** Runs the Perforce parser on an output file that contains 4 issues. */
    @Test
    public void shouldFindAllP4Issues() {
        shouldFindIssuesOfTool(4, "p4", "perforce.txt");
    }

    /** Runs the Pep8 parser on an output file: the build should report 8 issues. */
    @Test
    public void shouldFindAllPep8Issues() {
        shouldFindIssuesOfTool(8, "pep8", "pep8Test.txt");
    }

    /** Runs the Gcc3Compiler parser on an output file that contains 8 issues. */
    @Test
    public void shouldFindAllGcc3CompilerIssues() {
        shouldFindIssuesOfTool(8, "gcc3", "gcc.txt");
    }

    /** Runs the Gcc4Compiler and Gcc4Linker parsers on separate output file that contains 14 + 7 issues. */
    @Test
    public void shouldFindAllGcc4Issues() {
        shouldFindIssuesOfTool(14, "gcc", "gcc4.txt");
        shouldFindIssuesOfTool(7, "gcc-linker", "gcc4ld.txt");
    }

    /** Runs the Maven console parser on output files that contain 4 + 3 issues. */
    @Test
    public void shouldFindAllMavenConsoleIssues() {
        shouldFindIssuesOfTool(5 + 3, "maven-warnings", "maven-console.txt", "issue13969.txt");
    }

    /** Runs the MetrowerksCWCompiler parser on two output files that contains 5 + 3 issues. */
    @Test
    public void shouldFindAllMetrowerksCWCompilerIssues() {
        shouldFindIssuesOfTool(5, "metrowerks", "MetrowerksCWCompiler.txt");
        shouldFindIssuesOfTool(3, "metrowerks-linker", "MetrowerksCWLinker.txt");
        // FIXME:
    }

    /** Runs the AcuCobol parser on an output file that contains 4 issues. */
    @Test
    public void shouldFindAllAcuCobolIssues() {
        shouldFindIssuesOfTool(4, "acu-cobol", "acu.txt");
    }

    /** Runs the Ajc parser on an output file that contains 9 issues. */
    @Test
    public void shouldFindAllAjcIssues() {
        shouldFindIssuesOfTool(9, "aspectj", "ajc.txt");
    }

    /** Runs the AnsibleLint parser on an output file that contains 4 issues. */
    @Test
    public void shouldFindAllAnsibleLintIssues() {
        shouldFindIssuesOfTool(5, "ansiblelint", "ansibleLint.txt");
    }

    /**
     * Runs the Perl::Critic parser on an output file that contains 105 issues.
     */
    @Test
    public void shouldFindAllPerlCriticIssues() {
        shouldFindIssuesOfTool(105, "perl-critic", "perlcritic.txt");
    }

    /** Runs the Php parser on an output file that contains 5 issues. */
    @Test
    public void shouldFindAllPhpIssues() {
        shouldFindIssuesOfTool(5, "php", "php.txt");
    }

    /**
     * Runs the PHPStan scanner on an output file that contains 14 issues.
     */
    @Test
    public void shouldFindAllPhpStanIssues() {
        shouldFindIssuesOfTool(11, "phpstan", "phpstan.xml");
    }

    /** Runs the Microsoft PreFast parser on an output file that contains 11 issues. */
    @Test
    public void shouldFindAllPREfastIssues() {
        shouldFindIssuesOfTool(11, "prefast", "PREfast.xml");
    }

    /** Runs the Puppet Lint parser on an output file that contains 5 issues. */
    @Test
    public void shouldFindAllPuppetLintIssues() {
        shouldFindIssuesOfTool(5, "puppetlint", "puppet-lint.txt");
    }

    /** Runs the Eclipse parser on an output file that contains 8 issues. */
    @Test
    public void shouldFindAllEclipseIssues() {
        shouldFindIssuesOfTool(8, "eclipse", "eclipse.txt");
        shouldFindIssuesOfTool(6, "eclipse-xml", "eclipse-withinfo.xml");
        // FIXME: multiple
        shouldFindIssuesOfTool(8 + 6, "eclipse-xml", "eclipse-withinfo.xml", "eclipse.txt");
    }

    /** Runs the PyLint parser on output files that contains 6 + 19 issues. */
    @Test
    public void shouldFindAllPyLintParserIssues() {
        String pylint = "pylint";
        Report report = shouldFindIssuesOfTool(9 + 22, pylint, "pyLint.txt", "pylint_parseable.txt");

        assertThatDescriptionOfIssueIsSet(pylint, report.get(1),
                "Used when the name doesn't match the regular expression associated to its type(constant, variable, class...).");
        assertThatDescriptionOfIssueIsSet(pylint, report.get(7),
                "Used when a wrong number of spaces is used around an operator, bracket orblock opener.");
    }

    /**
     * Runs the QacSourceCodeAnalyser parser on an output file that contains 9 issues.
     */
    @Test
    public void shouldFindAllQACSourceCodeAnalyserIssues() {
        shouldFindIssuesOfTool(9, "qac", "QACSourceCodeAnalyser.txt");
    }

    /** Runs the Resharper parser on an output file that contains 3 issues. */
    @Test
    public void shouldFindAllResharperInspectCodeIssues() {
        shouldFindIssuesOfTool(3, "resharper", "ResharperInspectCode.xml");
    }

    /** Runs the RfLint parser on an output file that contains 6 issues. */
    @Test
    public void shouldFindAllRfLintIssues() {
        shouldFindIssuesOfTool(25, "rflint", "rflint.txt");
    }

    /** Runs the Robocopy parser on an output file: the build should report 3 issues. */
    @Test
    public void shouldFindAllRobocopyIssues() {
        shouldFindIssuesOfTool(3, "robocopy", "robocopy.txt");
    }

    /** Runs the Scala and SbtScala parser on separate output files: the build should report 2+3 issues. */
    @Test
    public void shouldFindAllScalaIssues() {
        shouldFindIssuesOfTool(2, "scala", "scalac.txt");
        shouldFindIssuesOfTool(3, "scala-sbt", "sbtScalac.txt");
    }

    /** Runs the Sphinx build parser on an output file: the build should report 6 issues. */
    @Test
    public void shouldFindAllSphinxIssues() {
        shouldFindIssuesOfTool(7, "sphinx", "sphinxbuild.txt");
    }

    /** Runs the Idea Inspection parser on an output file that contains 1 issues. */
    @Test
    public void shouldFindAllIdeaInspectionIssues() {
        shouldFindIssuesOfTool(1, "idea", "IdeaInspectionExample.xml");
    }

    /** Runs the Intel parser on an output file that contains 7 issues. */
    @Test
    public void shouldFindAllIntelIssues() {
        shouldFindIssuesOfTool(9, "intel", "intelc.txt");
    }

    /** Runs the Oracle Invalids parser on an output file that contains 3 issues. */
    @Test
    public void shouldFindAllInvalidsIssues() {
        shouldFindIssuesOfTool(3, "invalids", "invalids.txt");
    }

    /** Runs the Java parser on several output files that contain 2 + 1 + 1 + 1 + 2 issues. */
    @Test
    public void shouldFindAllJavaIssues() {
        shouldFindIssuesOfTool(2 + 1 + 1 + 1 + 2, "java", "javac.txt", "gradle.java.log",
                "gradle.another.java.log",
                "ant-javac.txt", "hpi.txt");
    }

    /**
     * Runs the Kotlin parser on several output files that contain 1 issues.
     */
    @Test
    public void shouldFindAllKotlinIssues() {
        shouldFindIssuesOfTool(1, "kotlin", "kotlin.txt");
    }

    /**
     * Runs the CssLint parser on an output file that contains 51 issues.
     */
    @Test
    public void shouldFindAllCssLintIssues() {
        shouldFindIssuesOfTool(51, "csslint", "csslint.xml");
    }

    /** Runs the DiabC parser on an output file that contains 12 issues. */
    @Test
    public void shouldFindAllDiabCIssues() {
        shouldFindIssuesOfTool(12, "diabc", "diabc.txt");
    }

    /** Runs the Doxygen parser on an output file that contains 18 issues. */
    @Test
    public void shouldFindAllDoxygenIssues() {
        shouldFindIssuesOfTool(19, "doxygen", "doxygen.txt");
    }

    /** Runs the Dr. Memory parser on an output file that contains 8 issues. */
    @Test
    public void shouldFindAllDrMemoryIssues() {
        shouldFindIssuesOfTool(8, "dr-memory", "drmemory.txt");
    }

    /** Runs the PVS-Studio parser on an output file that contains 33 issues. */
    @Test
    public void shouldFindAllPVSStudioIssues() {
        shouldFindIssuesOfTool(33, "pvs-studio", "TestReport.plog");
    }

    /** Runs the JavaC parser on an output file of the Eclipse compiler: the build should report no issues. */
    @Test
    public void shouldFindNoJavacIssuesInEclipseOutput() {
        shouldFindIssuesOfTool(0, "java", "eclipse.txt");
    }

    /** Runs the ProtoLint parser on an output file that contains 10 issues. */
    @Test
    public void shouldFindAllProtoLintIssues() {
        shouldFindIssuesOfTool(2591, "protolint", "protolint.txt");
    }

    /** Runs the HadoLint parser on an output file that contains 5 issues. */
    @Test
    public void shouldFindAllHadoLintIssues() {
        shouldFindIssuesOfTool(5, "hadolint", "hadolint.json");
    }

    /** Runs the DockerLint parser on an output file that contains 3 issues. */
    @Test
    public void shouldFindAllDockerLintIssues() {
        shouldFindIssuesOfTool(7, "dockerlint", "dockerlint.json");
    }

    /** Runs the Clair parser on an output file that contains 112 issues. */
    @Test
    public void shouldFindAllClairIssues() {
        shouldFindIssuesOfTool(112, "clair", "clair.json");
    }

    /** Runs the OTDockerLint parser on an output file that contains 5 issues. */
    @Test
    public void shouldFindAllOTDockerLintIssues() {
        shouldFindIssuesOfTool(3, "ot-docker-linter", "ot-docker-linter.json");
    }

    /** Runs the Brakeman parser on an output file that contains 32 issues. */
    @Test
    public void shouldFindAllBrakemanIssues() {
        shouldFindIssuesOfTool(32, "brakeman", "brakeman.json");
    }

    /** Runs the trivy parser on an output file that contains 4 issues. */
    @Test
    public void shouldFindAllTrivyIssues() {
        shouldFindIssuesOfTool(4, "trivy", "trivy_result.json");
    }

    /** Runs the qt translation parser on an output file that contains 5 issues. */
    @Test
    public void shouldFindAllQtTranslationIssues() {
        shouldFindIssuesOfTool(4, "qt-translation", "qttranslation.ts");
    }

    private Report shouldFindIssuesOfTool(final int expectedSizeOfIssues, final String tool,
            final String... fileNames) {
        ParserRegistry registry = new ParserRegistry();
        ParserDescriptor descriptor = registry.get(tool);

        Report allIssues = new Report();
        for (String fileName : fileNames) {
            IssueParser parser = descriptor.createParser();
            Report report = parser.parse(new FileReaderFactory(getResourceAsFile("../parser/").resolve(fileName)));
            allIssues.addAll(report);
        }

        assertThat(allIssues).as("Parser %s", tool).hasSize(expectedSizeOfIssues);
        return allIssues;
    }
}
