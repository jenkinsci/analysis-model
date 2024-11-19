package edu.hm.hafner.analysis.registry;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.FileReaderFactory;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.util.ResourceTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Integration tests of all parsers.
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings({"PMD.CouplingBetweenObjects", "PMD.ExcessivePublicCount", "PMD.CyclomaticComplexity", "PMD.GodClass", "ClassDataAbstractionCoupling", "ClassFanOutComplexity"})
class ParsersTest extends ResourceTest {
    private static final String CODE_FRAGMENT = """
            <pre><code>
                #
            
                ERROR HANDLING: N/A
                #
                REMARKS: N/A
                #
                ****************************** END HEADER *************************************
                #
            
                ***************************** BEGIN PDL ***************************************
                #
                ****************************** END PDL ****************************************
                #
            
                ***************************** BEGIN CODE **************************************
                **
                *******************************************************************************
            
                *******************************************************************************
                *******************************************************************************
            
            if [ $# -lt 3 ]
            then
            exit 1
            fi
            
                *******************************************************************************
                initialize local variables
                shift input parameter (twice) to leave only files to copy
                *******************************************************************************
            
            files=&quot;&quot;
            shift
            shift
            
                *******************************************************************************
                *******************************************************************************
            
            for i in $*
            do
            files=&quot;$files $directory/$i&quot;
            done
            </code>
            </pre>
            """;

    /** Verifies that a broken file does not fail. */
    @Test
    void shouldSilentlyIgnoreWrongFile() {
        assertThatExceptionOfType(ParsingException.class).isThrownBy(() ->
                findIssuesOfTool(0, "checkstyle", "CargoCheck.json"));
    }

    /**
     * Runs with several tools that internally delegate to CheckStyle's  parser on an output file that contains 6
     * issues.
     */
    @Test
    void shouldFindAllIssuesForCheckStyleAlias() {
        for (String tool : Arrays.asList("detekt", "eslint", "ktlint", "php-code-sniffer",
                "swiftlint", "stylelint", "tslint")) {
            findIssuesOfTool(4, tool, "checkstyle.xml");
        }
    }

    /** Runs the Semgrep analysis parser on an output file that contains 1 issue. */
    @Test
    void shouldFindAllSemgrepIssues() {
        findIssuesOfTool(1, "semgrep", "violations/semgrep-report.json");
    }

    /** Runs the Dart analysis parser on an output file that contains 6 issues. */
    @Test
    void shouldFindAllDartIssues() {
        findIssuesOfTool(6, "dart", "dart.log");
    }

    /** Runs the SARIF parser on an output file that contains 2 issues. */
    @Test
    void shouldFindAllSarifIssues() {
        findIssuesOfTool(2, "sarif", "sarif.json");
    }

    /** Runs the Cmake parser on an output file that contains 8 issues. */
    @Test
    void shouldFindAllCmakeIssues() {
        findIssuesOfTool(8, "cmake", "cmake.txt");
    }

    /** Runs the Cargo parser on an output file that contains 2 issues. */
    @Test
    void shouldFindAllCargoIssues() {
        findIssuesOfTool(2, "cargo", "CargoCheck.json");
    }

    /** Runs the Pmd parser on an output file that contains 262 issues. */
    @Test
    void shouldFindAllIssuesForPmdAlias() {
        findIssuesOfTool(262, "infer", "pmd-6.xml");
    }

    /** Runs the MSBuild parser on an output file that contains 262 issues. */
    @Test
    void shouldFindAllIssuesForMsBuildAlias() {
        findIssuesOfTool(8, "pclint", "msbuild.txt");
    }

    /** Runs the YamlLint parser on an output file that contains 4 issues. */
    @Test
    void shouldFindAllYamlLintIssues() {
        findIssuesOfTool(4, "yamllint", "yamllint.txt");
    }

    /** Runs the Iar parser on an output file that contains 6 issues. */
    @Test
    void shouldFindAllIarIssues() {
        findIssuesOfTool(6, "iar", "iar.txt");
    }

    /** Runs the IbLinter parser on an output file that contains 1 issue. */
    @Test
    void shouldFindAllIbLinterIssues() {
        findIssuesOfTool(1, "iblinter", "iblinter.xml");
    }

    /** Runs the IarCStat parser on an output file that contains 6 issues. */
    @Test
    void shouldFindAllIarCStatIssues() {
        findIssuesOfTool(6, "iar-cstat", "iar-cstat.txt");
    }

    /** Runs the SonarQube parsers on two files that contains 6 and 31 issues. */
    @Test
    void shouldFindAllSonarQubeIssues() {
        findIssuesOfTool(32 + 6, "sonar", "sonarqube-api.json", "sonarqube-differential.json");
    }

    /** Runs the TagList parser on an output file that contains 6 issues. */
    @Test
    void shouldFindAllTagListIssues() {
        findIssuesOfTool(4, "taglist", "taglist.xml");
    }

    /** Runs the Ccm parser on an output file that contains 6 issues. */
    @Test
    void shouldFindAllCcmIssues() {
        findIssuesOfTool(6, "ccm", "ccm.xml");
    }

    /** Runs the ruboCop parser on an output file that contains 2 issues. */
    @Test
    void shouldFindAllRuboCopIssues() {
        findIssuesOfTool(3, "rubocop", "rubocop.log");
    }

    /** Runs the flawfinder parser on an output file that contains 3 issues. */
    @Test
    void shouldFindAllFlawfinderIssues() {
        findIssuesOfTool(3, "flawfinder", "flawfinder.log");
    }

    /** Runs the Android Lint parser on an output file that contains 2 issues. */
    @Test
    void shouldFindAllAndroidLintIssues() {
        findIssuesOfTool(2, "android-lint", "android-lint.xml");
    }

    /** Runs the CodeNarc parser on an output file that contains 11 issues. */
    @Test
    void shouldFindAllCodeNArcIssues() {
        findIssuesOfTool(11, "codenarc", "codeNarc.xml");
    }

    /** Runs the Cppcheck parser on an output file that contains 3 issues. */
    @Test
    void shouldFindAllCppCheckIssues() {
        findIssuesOfTool(3, "cppcheck", "cppcheck.xml");
    }

    /** Runs the DocFx parser on an output file that contains 3 issues. */
    @Test
    void shouldFindAllDocFXIssues() {
        findIssuesOfTool(3, "docfx", "docfx.json");
    }

    /** Runs the ErrorProne parser on output files that contain 9 + 2 issues. */
    @Test
    void shouldFindAllErrorProneIssues() {
        findIssuesOfTool(9 + 2, "error-prone", "errorprone-maven.log", "gradle-error-prone.log");
    }

    /** Runs the Flake8 parser on an output file that contains 12 issues. */
    @Test
    void shouldFindAllFlake8Issues() {
        findIssuesOfTool(12, "flake8", "flake8.txt");
    }

    /** Runs the JSHint parser on an output file that contains 6 issues. */
    @Test
    void shouldFindAllJsHintIssues() {
        findIssuesOfTool(6, "js-hint", "jshint.xml");
    }

    /**
     * Runs the JUnit parser on an output file that contains 2 and 1 issues.
     */
    @Test
    void shouldFindAllJUnitIssues() {
        findIssuesOfTool(2, "junit", "junit.xml");

        findIssuesOfTool(1, "junit", "TEST-org.jenkinsci.plugins.jvctb.perform.JvctbPerformerTest.xml");
    }

    /** Runs the Klocwork parser on an output file that contains 2 issues. */
    @Test
    void shouldFindAllKlocWorkIssues() {
        findIssuesOfTool(2, "klocwork", "klocwork.xml");
    }

    /** Runs the MyPy parser on an output file that contains 5 issues. */
    @Test
    void shouldFindAllMyPyIssues() {
        findIssuesOfTool(5, "mypy", "mypy.txt");
    }

    /** Runs the PIT parser on an output file that contains 25 issues. */
    @Test
    void shouldFindAllPitIssues() {
        findIssuesOfTool(2, "pit", "pit.xml");
    }

    /** Runs the PyDocStyle parser on an output file that contains 33 issues. */
    @Test
    void shouldFindAllPyDocStyleIssues() {
        findIssuesOfTool(33, "pydocstyle", "pydocstyle.txt");
    }

    /** Runs the XML Lint parser on an output file that contains 3 issues. */
    @Test
    void shouldFindAllXmlLintStyleIssues() {
        findIssuesOfTool(3, "xmllint", "xmllint.txt");
    }

    /** Runs the zptlint parser on an output file that contains 2 issues. */
    @Test
    void shouldFindAllZptLintStyleIssues() {
        findIssuesOfTool(2, "zptlint", "zptlint.log");
    }

    /** Runs the CPD parser on an output file that contains 2 issues. */
    @Test
    void shouldFindAllCpdIssues() {
        var cpd = "cpd";
        var report = findIssuesOfTool(2, cpd, "cpd.xml");
        assertThatDescriptionOfIssueIsSet(cpd, report.get(0), CODE_FRAGMENT);
    }

    /** Runs the Simian parser on an output file that contains 4 issues. */
    @Test
    void shouldFindAllSimianIssues() {
        findIssuesOfTool(4, "simian", "simian.xml");
    }

    /** Runs the DupFinder parser on an output file that contains 2 issues. */
    @Test
    void shouldFindAllDupFinderIssues() {
        var dupfinder = "dupfinder";
        var report = findIssuesOfTool(2, dupfinder, "dupfinder.xml");
        assertThatDescriptionOfIssueIsSet(dupfinder, report.get(0),
                """
                <pre><code>
                    if (items == null) throw new ArgumentNullException(&quot;items&quot;);
                </code>
                </pre>
                """);
    }

    /** Runs the Armcc parser on output files that contain 3 + 3 issues. */
    @Test
    void shouldFindAllArmccIssues() {
        findIssuesOfTool(3 + 3, "armcc", "armcc.txt", "armcc5.txt");
    }

    /** Runs the Buckminster parser on an output file that contains 3 issues. */
    @Test
    void shouldFindAllBuckminsterIssues() {
        findIssuesOfTool(3, "buckminster", "buckminster.txt");
    }

    /** Runs the Cadence parser on an output file that contains 3 issues. */
    @Test
    void shouldFindAllCadenceIssues() {
        findIssuesOfTool(3, "cadence", "CadenceIncisive.txt");
    }

    /** Runs the Mentor parser on an output file that contains 12 issues. */
    @Test
    void shouldFindAllMentorGraphicsIssues() {
        findIssuesOfTool(12, "modelsim", "MentorGraphics.log");
    }

    /** Runs the BluePearl an output file that contains 8 issues. */
    @Test
    void shouldFindAllBluePearlIssues() {
        findIssuesOfTool(12, "bluepearl", "bluepearl.log");
    }

    /** Runs the PMD parser on an output file that contains 262 issues (PMD 6.1.0). */
    @Test
    void shouldFindAllPmdIssues() {
        var pmd = "pmd";
        var report = findIssuesOfTool(262, pmd, "pmd-6.xml");
        assertThatDescriptionOfIssueIsSet(pmd, report.get(0),
                "A high number of imports can indicate a high degree of coupling within an object.");
    }

    /** Runs the CheckStyle parser on an output file that contains 6 issues. */
    @Test
    void shouldFindAllCheckStyleIssues() {
        var checkstyle = "checkstyle";
        var report = findIssuesOfTool(4, checkstyle, "checkstyle.xml");

        assertThatDescriptionOfIssueIsSet(checkstyle, report.get(2),
                "<p>Since Checkstyle 3.1</p><p>");
        assertThatDescriptionOfIssueIsSet(checkstyle, report.get(2),
                "The check finds classes that are designed for extension (subclass creation).");
    }

    private void assertThatDescriptionOfIssueIsSet(final String tool, final Issue issue,
            final String expectedDescription) {
        var parserRegistry = new ParserRegistry();
        var descriptor = parserRegistry.get(tool);

        assertThat(issue).hasDescription("");
        assertThat(descriptor.getDescription(issue)).contains(expectedDescription);
    }

    /** Runs the FindBugs parser on an output file that contains 2 issues. */
    @Test
    void shouldFindAllFindBugsIssues() {
        var findbugs = "findbugs";
        var report = findIssuesOfTool(2, findbugs, "findbugs-native.xml");

        assertThatDescriptionOfIssueIsSet(findbugs, report.get(0),
                """
                <p> The fields of this class appear to be accessed inconsistently with respect
                  to synchronization.&nbsp; This bug report indicates that the bug pattern detector
                  judged that
                  </p>
                  <ul>
                  <li> The class contains a mix of locked and unlocked accesses,</li>
                  <li> The class is <b>not</b> annotated as javax.annotation.concurrent.NotThreadSafe,</li>
                  <li> At least one locked access was performed by one of the class's own methods, and</li>
                  <li> The number of unsynchronized field accesses (reads and writes) was no more than
                       one third of all accesses, with writes being weighed twice as high as reads</li>
                  </ul>
                
                  <p> A typical bug matching this bug pattern is forgetting to synchronize
                  one of the methods in a class that is intended to be thread-safe.</p>
                
                  <p> You can select the nodes labeled "Unsynchronized access" to show the
                  code locations where the detector believed that a field was accessed
                  without synchronization.</p>
                
                  <p> Note that there are various sources of inaccuracy in this detector;
                  for example, the detector cannot statically detect all situations in which
                  a lock is held.&nbsp; Also, even when the detector is accurate in
                  distinguishing locked vs. unlocked accesses, the code in question may still
                  be correct.</p>""");
    }

    /** Runs the SpotBugs parser on an output file that contains 2 issues. */
    @Test
    void shouldFindAllSpotBugsIssues() {
        var expectedDescription = """
                <p>This code calls a method and ignores the return value. However our analysis shows that
                the method (including its implementations in subclasses if any) does not produce any effect
                other than return value. Thus this call can be removed.
                </p>
                <p>We are trying to reduce the false positives as much as possible, but in some cases this warning might be wrong.
                Common false-positive cases include:</p>
                <p>- The method is designed to be overridden and produce a side effect in other projects which are out of the scope of the analysis.</p>
                <p>- The method is called to trigger the class loading which may have a side effect.</p>
                <p>- The method is called just to get some exception.</p>
                <p>If you feel that our assumption is incorrect, you can use a @CheckReturnValue annotation
                to instruct SpotBugs that ignoring the return value of this method is acceptable.
                </p>""";

        var spotBugs = "spotbugs";
        var report = findIssuesOfTool(2, spotBugs, "spotbugsXml.xml");
        assertThatDescriptionOfIssueIsSet(spotBugs, report.get(0), expectedDescription);
    }

    /** Runs the SpotBugs parser on an output file that contains 2 issues. */
    @Test
    void shouldProvideMessagesAndDescriptionForSecurityIssuesWithSpotBugs() {
        var expectedDescription = """
                <p>A file is opened to read its content. The filename comes from an <b>input</b> parameter.
                If an unfiltered parameter is passed to this file API, files from an arbitrary filesystem location could be read.</p>
                <p>This rule identifies <b>potential</b> path traversal vulnerabilities. In many cases, the constructed file path cannot be controlled
                by the user. If that is the case, the reported instance is a false positive.</p>""";

        var spotBugs = "spotbugs";
        var report = findIssuesOfTool(1, spotBugs, "issue55707.xml");
        var issue = report.get(0);
        assertThatDescriptionOfIssueIsSet(spotBugs, issue, expectedDescription);
        assertThat(issue).hasMessage(
                "java/nio/file/Paths.get(Ljava/lang/String;[Ljava/lang/String;)Ljava/nio/file/Path; reads a file whose location might be specified by user input");
    }

    /** Runs the Clang-Analyzer parser on an output file that contains 3 issues. */
    @Test
    void shouldFindAllClangAnalyzerIssues() {
        findIssuesOfTool(3, "clang-analyzer", "ClangAnalyzer.txt");
    }

    /** Runs the Clang-Tidy parser on an output file that contains 8 issues. */
    @Test
    void shouldFindAllClangTidyIssues() {
        findIssuesOfTool(9, "clang-tidy", "ClangTidy.txt");
    }

    /** Runs the Clang parser on an output file that contains 9 issues. */
    @Test
    void shouldFindAllClangIssues() {
        findIssuesOfTool(9, "clang", "apple-llvm-clang.txt");
    }

    /** Runs the Coolflux parser on an output file that contains 1 issues. */
    @Test
    void shouldFindAllCoolfluxIssues() {
        findIssuesOfTool(1, "coolflux", "coolfluxchesscc.txt");
    }

    /** Runs the CppLint parser on an output file that contains 1031 issues. */
    @Test
    void shouldFindAllCppLintIssues() {
        findIssuesOfTool(1031, "cpplint", "cpplint.txt");
    }

    /** Runs the CodeAnalysis parser on an output file that contains 3 issues. */
    @Test
    void shouldFindAllCodeAnalysisIssues() {
        findIssuesOfTool(3, "code-analysis", "codeanalysis.txt");
    }

    /** Runs the DScanner parser on an output file that contains 4 issues. */
    @Test
    void shouldFindAllDScannerIssues() {
        findIssuesOfTool(4, "dscanner", "dscanner-report.json");
    }

    /** Runs the GoLint parser on an output file that contains 7 issues. */
    @Test
    void shouldFindAllGoLintIssues() {
        findIssuesOfTool(7, "golint", "golint.txt");
    }

    /** Runs the GoVet parser on an output file that contains 2 issues. */
    @Test
    void shouldFindAllGoVetIssues() {
        findIssuesOfTool(2, "go-vet", "govet.txt");
    }

    /** Runs the SunC parser on an output file that contains 8 issues. */
    @Test
    void shouldFindAllSunCIssues() {
        findIssuesOfTool(8, "sunc", "sunc.txt");
    }

    /** Runs the JcReport parser on an output file that contains 6 issues. */
    @Test
    void shouldFindAllJcReportIssues() {
        findIssuesOfTool(6, "jc-report", "jcreport.xml");
    }

    /** Runs the StyleCop parser on an output file that contains 5 issues. */
    @Test
    void shouldFindAllStyleCopIssues() {
        findIssuesOfTool(5, "stylecop", "stylecop.xml");
    }

    /** Runs the Tasking VX parser on an output file that contains 10 issues. */
    @Test
    void shouldFindAllTaskingVxIssues() {
        findIssuesOfTool(10, "tasking-vx", "tasking-vx.txt");
    }

    /** Runs the tnsdl translator parser on an output file that contains 4 issues. */
    @Test
    void shouldFindAllTnsdlIssues() {
        findIssuesOfTool(4, "tnsdl", "tnsdl.txt");
    }

    /** Runs the Texas Instruments Code Composer Studio parser on an output file that contains 10 issues. */
    @Test
    void shouldFindAllTiCssIssues() {
        findIssuesOfTool(10, "code-composer", "ticcs.txt");
    }

    /** Runs the IBM XLC compiler and linker parser on an output file that contains 1 + 1 issues. */
    @Test
    void shouldFindAllXlcIssues() {
        findIssuesOfTool(1, "xlc", "xlc.txt");
    }

    /** Runs the YIU compressor parser on an output file that contains 3 issues. */
    @Test
    void shouldFindAllYuiCompressorIssues() {
        findIssuesOfTool(4, "yui", "yui.txt");
    }

    /** Runs the Erlc parser on an output file that contains 2 issues. */
    @Test
    void shouldFindAllErlcIssues() {
        findIssuesOfTool(2, "erlc", "erlc.txt");
    }

    /** Runs the FlexSdk parser on an output file that contains 5 issues. */
    @Test
    void shouldFindAllFlexSDKIssues() {
        findIssuesOfTool(5, "flex", "flexsdk.txt");
    }

    /** Runs the FxCop parser on an output file that contains 2 issues. */
    @Test
    void shouldFindAllFxcopSDKIssues() {
        findIssuesOfTool(2, "fxcop", "fxcop.xml");
    }

    /** Runs the Gendarme parser on an output file that contains 3 issues. */
    @Test
    void shouldFindAllGendarmeIssues() {
        findIssuesOfTool(3, "gendarme", "Gendarme.xml");
    }

    /** Runs the GhsMulti parser on an output file that contains 3 issues. */
    @Test
    void shouldFindAllGhsMultiIssues() {
        findIssuesOfTool(7, "ghs-multi", "ghsmulti.txt");
    }

    /**
     * Runs the Gnat parser on an output file that contains 9 issues.
     */
    @Test
    void shouldFindAllGnatIssues() {
        findIssuesOfTool(9, "gnat", "gnat.txt");
    }

    /** Runs the GnuFortran parser on an output file that contains 4 issues. */
    @Test
    void shouldFindAllGnuFortranIssues() {
        findIssuesOfTool(4, "fortran", "GnuFortran.txt");
    }

    /** Runs the MsBuild parser on an output file that contains 6 issues. */
    @Test
    void shouldFindAllMsBuildIssues() {
        findIssuesOfTool(8, "msbuild", "msbuild.txt");
    }

    /** Runs the NagFortran parser on an output file that contains 10 issues. */
    @Test
    void shouldFindAllNagFortranIssues() {
        findIssuesOfTool(14, "nag-fortran", "NagFortran.txt");
    }

    /** Runs the Perforce parser on an output file that contains 4 issues. */
    @Test
    void shouldFindAllP4Issues() {
        findIssuesOfTool(4, "perforce", "perforce.txt");
    }

    /** Runs the Pep8 parser on an output file: the build should report 8 issues. */
    @Test
    void shouldFindAllPep8Issues() {
        findIssuesOfTool(8, "pep8", "pep8Test.txt");
    }

    /** Runs the Gcc3Compiler parser on an output file that contains 8 issues. */
    @Test
    void shouldFindAllGcc3CompilerIssues() {
        findIssuesOfTool(8, "gcc3", "gcc.txt");
    }

    /** Runs the Gcc4Compiler and Gcc4Linker parsers on separate output file that contains 16 + 7 issues. */
    @Test
    void shouldFindAllGcc4Issues() {
        findIssuesOfTool(16 + 7 - 1, "gcc", "gcc4.txt", "gcc4ld.txt"); // one duplicate warning
    }

    /** Runs the Maven console parser on output files that contain 4 + 3 issues. */
    @Test
    void shouldFindAllMavenConsoleIssues() {
        findIssuesOfTool(5 + 3, "maven-warnings", "maven-console.txt", "issue13969.txt");
    }

    /** Runs the MetrowerksCWCompiler parser on two output files that contains 5 + 3 issues. */
    @Test
    void shouldFindAllMetrowerksCWCompilerIssues() {
        findIssuesOfTool(5 + 3, "metrowerks", "MetrowerksCWCompiler.txt", "MetrowerksCWLinker.txt");
    }

    /** Runs the AcuCobol parser on an output file that contains 4 issues. */
    @Test
    void shouldFindAllAcuCobolIssues() {
        findIssuesOfTool(4, "acu-cobol", "acu.txt");
    }

    /** Runs the Ajc parser on an output file that contains 9 issues. */
    @Test
    void shouldFindAllAjcIssues() {
        findIssuesOfTool(9, "aspectj", "ajc.txt");
    }

    /** Runs the AnsibleLint parser on an output file that contains 12 issues. */
    @Test
    void shouldFindAllAnsibleLintIssues() {
        findIssuesOfTool(12, "ansiblelint", "ansibleLint.txt");
    }

    /**
     * Runs the Perl::Critic parser on an output file that contains 105 issues.
     */
    @Test
    void shouldFindAllPerlCriticIssues() {
        findIssuesOfTool(105, "perl-critic", "perlcritic.txt");
    }

    /** Runs the Php parser on an output file that contains 5 issues. */
    @Test
    void shouldFindAllPhpIssues() {
        findIssuesOfTool(5, "php", "php.txt");
    }

    /**
     * Runs the PHPStan scanner on an output file that contains 14 issues.
     */
    @Test
    void shouldFindAllPhpStanIssues() {
        findIssuesOfTool(11, "phpstan", "phpstan.xml");
    }

    /** Runs the Microsoft PreFast parser on an output file that contains 11 issues. */
    @Test
    void shouldFindAllPREfastIssues() {
        findIssuesOfTool(11, "prefast", "PREfast.xml");
    }

    /** Runs the Puppet Lint parser on an output file that contains 5 issues. */
    @Test
    void shouldFindAllPuppetLintIssues() {
        findIssuesOfTool(5, "puppetlint", "puppet-lint.txt");
    }

    /** Runs the Eclipse parser on an output file that contains 8 issues. */
    @Test
    void shouldFindAllEclipseIssues() {
        var eclipse = "eclipse";
        findIssuesOfTool(8, eclipse, "eclipse.txt");

        // FIXME: fails if offline
        findIssuesOfTool(6, eclipse, "eclipse-withinfo.xml");

        findIssuesOfTool(8 + 6, eclipse, "eclipse-withinfo.xml", "eclipse.txt");
    }

    /** Runs the PyLint parser on output files that contains 6 + 19 issues. */
    @Test
    void shouldFindAllPyLintParserIssues() {
        var pylint = "pylint";
        var report = findIssuesOfTool(9 + 22, pylint, "pyLint.txt", "pylint_parseable.txt");

        assertThatDescriptionOfIssueIsSet(pylint, report.get(1),
                "Used when the name doesn't match the regular expression associated to its type(constant, variable, class...).");
        assertThatDescriptionOfIssueIsSet(pylint, report.get(7),
                "Used when an imported module or variable is not used.");
    }

    /**
     * Runs the QacSourceCodeAnalyser parser on an output file that contains 9 issues.
     */
    @Test
    void shouldFindAllQACSourceCodeAnalyserIssues() {
        findIssuesOfTool(9, "qac", "QACSourceCodeAnalyser.txt");
    }

    /** Runs the Resharper parser on an output file that contains 3 issues. */
    @Test
    void shouldFindAllResharperInspectCodeIssues() {
        findIssuesOfTool(3, "resharper", "ResharperInspectCode.xml");
    }

    /** Runs the RfLint parser on an output file that contains 6 issues. */
    @Test
    void shouldFindAllRfLintIssues() {
        findIssuesOfTool(25, "rflint", "rflint.txt");
    }

    /** Runs the Robocopy parser on an output file: the build should report 3 issues. */
    @Test
    void shouldFindAllRobocopyIssues() {
        findIssuesOfTool(3, "robocopy", "robocopy.txt");
    }

    /** Runs the Scala and SbtScala parser on separate output files: the build should report 2+3 issues. */
    @Test
    void shouldFindAllScalaIssues() {
        findIssuesOfTool(2 + 5, "scala", "scalac.txt", "sbtScalac.txt");
    }

    /** Runs the Sphinx build parser on 2 output files: the build should report 7 + 6 issues. */
    @Test
    void shouldFindAllSphinxIssues() {
        findIssuesOfTool(7 + 6, "sphinx", "sphinxbuild.txt", "sphinxbuildlinkcheck.txt");
    }

    /** Runs the Idea Inspection parser on an output file that contains 1 issues. */
    @Test
    void shouldFindAllIdeaInspectionIssues() {
        findIssuesOfTool(1, "idea", "IdeaInspectionExample.xml");
    }

    /** Runs the Intel parser on an output file that contains 7 issues. */
    @Test
    void shouldFindAllIntelIssues() {
        findIssuesOfTool(9, "intel", "intelc.txt");
    }

    /** Runs the Oracle Invalids parser on an output file that contains 3 issues. */
    @Test
    void shouldFindAllInvalidsIssues() {
        findIssuesOfTool(3, "invalids", "invalids.txt");
    }

    /** Runs the Java parser on several output files that contain 2 + 1 + 1 + 1 + 2 issues. */
    @Test
    void shouldFindAllJavaIssues() {
        findIssuesOfTool(2 + 1 + 1 + 1 + 2, "java", "javac.txt", "gradle.java.log",
                "gradle.another.java.log",
                "ant-javac.txt", "hpi.txt");
    }

    /**
     * Runs the Kotlin parser on several output files that contain 1 issues.
     */
    @Test
    void shouldFindAllKotlinIssues() {
        findIssuesOfTool(1, "kotlin", "kotlin.txt");
    }

    /**
     * Runs the CssLint parser on an output file that contains 51 issues.
     */
    @Test
    void shouldFindAllCssLintIssues() {
        findIssuesOfTool(51, "csslint", "csslint.xml");
    }

    /** Runs the DiabC parser on an output file that contains 12 issues. */
    @Test
    void shouldFindAllDiabCIssues() {
        findIssuesOfTool(12, "diabc", "diabc.txt");
    }

    /** Runs the Doxygen parser on an output file that contains 18 issues. */
    @Test
    void shouldFindAllDoxygenIssues() {
        findIssuesOfTool(19, "doxygen", "doxygen.txt");
    }

    /** Runs the Dr. Memory parser on an output file that contains 8 issues. */
    @Test
    void shouldFindAllDrMemoryIssues() {
        findIssuesOfTool(8, "dr-memory", "drmemory.txt");
    }

    /** Runs the PVS-Studio parser on an output file that contains 33 issues. */
    @Test
    void shouldFindAllPVSStudioIssues() {
        findIssuesOfTool(33, "pvs-studio", "TestReport.plog");
    }

    /** Runs the JavaC parser on an output file of the Eclipse compiler: the build should report no issues. */
    @Test
    void shouldFindNoJavacIssuesInEclipseOutput() {
        findIssuesOfTool(0, "java", "eclipse.txt");
    }

    /** Runs the ProtoLint parser on a plaintext output file that contains 2591 issues. */
    @Test
    void shouldFindAllProtoLintIssues() {
        findIssuesOfTool(2591, "protolint", "protolint.txt");
    }

    /** Runs the ProtoLint parser on a json output file that contains 462 issues. */
    @Test
    void shouldFindAllProtoLintIssuesJsonFormat() {
        findIssuesOfTool(462, "protolint", "protolint_0.50.2.json");
    }

    /** Runs the HadoLint parser on an output file that contains 5 issues. */
    @Test
    void shouldFindAllHadoLintIssues() {
        findIssuesOfTool(5, "hadolint", "hadolint.json");
    }

    /** Runs the DockerLint parser on an output file that contains 3 issues. */
    @Test
    void shouldFindAllDockerLintIssues() {
        findIssuesOfTool(7, "dockerlint", "dockerlint.json");
    }

    /** Runs the Clair parser on an output file that contains 112 issues. */
    @Test
    void shouldFindAllClairIssues() {
        findIssuesOfTool(112, "clair", "clair.json");
    }

    /** Runs the OTDockerLint parser on an output file that contains 5 issues. */
    @Test
    void shouldFindAllOTDockerLintIssues() {
        findIssuesOfTool(3, "ot-docker-linter", "ot-docker-linter.json");
    }

    /** Runs the Brakeman parser on an output file that contains 32 issues. */
    @Test
    void shouldFindAllBrakemanIssues() {
        findIssuesOfTool(32, "brakeman", "brakeman.json");
    }

    /** Runs the trivy parser on an output file that contains 4 issues. */
    @Test
    void shouldFindAllTrivyIssues() {
        findIssuesOfTool(4, "trivy", "trivy_result.json");
    }

    /** Runs the qt translation parser on an output file that contains 5 issues. */
    @Test
    void shouldFindAllQtTranslationIssues() {
        findIssuesOfTool(4, "qt-translation", "qttranslation.ts");
    }

    /** Runs the oelint-adv parser on an output file that contains 8 issues. */
    @Test
    void shouldFindAllOELintAdvIssues() {
        findIssuesOfTool(8, "oelint-adv", "oelint-adv.txt");
    }

    private Report findIssuesOfTool(final int expectedSizeOfIssues, final String tool, final String... fileNames) {
        var registry = new ParserRegistry();
        var descriptor = registry.get(tool);

        var allIssues = new Report();
        for (String fileName : fileNames) {
            var parser = descriptor.createParser();
            var report = parser.parse(new FileReaderFactory(getResourceAsFile("../parser/").resolve(fileName)));
            allIssues.addAll(report);
        }

        assertThat(allIssues).as("Parser %s", tool).hasSize(expectedSizeOfIssues);
        return allIssues;
    }
}
