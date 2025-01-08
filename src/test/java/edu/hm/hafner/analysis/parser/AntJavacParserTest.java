package edu.hm.hafner.analysis.parser;

import java.nio.charset.Charset;
import java.time.Duration;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Categories;
import edu.hm.hafner.analysis.FileReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the class {@link AntJavacParser}.
 */
class AntJavacParserTest extends AbstractParserTest {
    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    AntJavacParserTest() {
        super("ant-javac.txt");
    }

    @Test
    void issue67521IgnoreJavaDocWarnings() {
        var warnings = parse("javadoc-in-java.log");

        assertThat(warnings).isEmpty();
    }

    /**
     * Parses a warning log with a very long line that will take several seconds to parse.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-55805">Issue 55805</a>
     */
    @Test
    void issue55805() {
        assertTimeoutPreemptively(TIMEOUT, () -> parse("issue55805.txt"));
    }

    /**
     * Parses a warning log with Javadoc warnings.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-63346">Issue 63346</a>
     */
    @Test
    void issue63346() {
        var report = parse("issue63346.txt");

        assertThat(report).isEmpty();
    }

    /**
     * Parses a warning log with two warnings.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-24611">Issue 24611</a>
     */
    @Test
    void testIssue24611() {
        var warnings = parse("issue24611.txt");

        assertThat(warnings).hasSize(2);
    }

    /**
     * Parses a warning log with one warning that refers to a missing class file.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-21240">Issue 21240</a>
     */
    @Test
    void issue21240() {
        var warnings = parse("issue21240.txt");

        assertThat(warnings).hasSize(1);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage(
                            "Cannot find annotation method 'xxx()' in type 'yyyy': class file for fully.qualified.ClassName not found")
                    .hasFileName("aaa.class");
        }
    }

    /**
     * Parses a warning log with 2 ANT warnings.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-2133">Issue 2133</a>
     */
    @Test
    void issue2133() {
        var warnings = parse("issue2133.txt");

        assertThat(warnings).hasSize(2);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasCategory(DEFAULT_CATEGORY)
                    .hasLineStart(86)
                    .hasLineEnd(86)
                    .hasMessage("non-varargs call of varargs method with inexact argument type for last parameter;")
                    .hasFileName(
                            "/home/hudson/hudson/data/jobs/Mockito/workspace/trunk/test/org/mockitousage/misuse/DescriptiveMessagesOnMisuseTest.java");
            softly.assertThat(warnings.get(1))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasCategory(Categories.DEPRECATION)
                    .hasLineStart(51)
                    .hasLineEnd(51)
                    .hasMessage("<T>stubVoid(T) in org.mockito.Mockito has been deprecated")
                    .hasFileName(
                            "/home/hudson/hudson/data/jobs/Mockito/workspace/trunk/test/org/mockitousage/stubbing/StubbingWithThrowablesTest.java");
            softly.assertAll();
        }
    }

    /**
     * Parses a warning log with 1 warnings that has no associated file.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-4098">Issue 4098</a>
     */
    @Test
    void issue4098() {
        var warnings = parse("issue4098.txt");

        assertThat(warnings).hasSize(1);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasCategory("Path")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage(
                            "bad path element \"C:\\...\\.hudson\\jobs\\...\\log4j.jar\": no such file or directory")
                    .hasFileName("C:/.../.hudson/jobs/.../log4j.jar");
        }
    }

    /**
     * Parses a warning log with 20 ANT warnings. 2 of them are duplicate, all are of priority Normal.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-2316">Issue 2316</a>
     */
    @Test
    void issue2316() {
        var warnings = parse("issue2316.txt");

        assertThat(warnings)
                .hasSize(18)
                .hasDuplicatesSize(2).hasOnlySeverities(Severity.WARNING_NORMAL);
    }

    /**
     * Parses a warning log with 3 ANT warnings. They all use different tasks.
     */
    @Test
    void parseDifferentTaskNames() {
        var warnings = parse("taskname.txt");

        assertThat(warnings).hasSize(1).hasDuplicatesSize(2);
    }

    /**
     * Verifies that arrays in deprecated methods are correctly handled.
     */
    @Test
    void parseArrayInDeprecatedMethod() {
        var warnings = parse("issue5868.txt");

        assertThat(warnings).hasSize(1);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0)).hasSeverity(Severity.WARNING_NORMAL)
                    .hasCategory("Deprecation")
                    .hasLineStart(225)
                    .hasLineEnd(225)
                    .hasMessage(
                            "loadAvailable(java.lang.String,int,int,java.lang.String[]) in my.OtherClass has been deprecated")
                    .hasFileName("D:/path/to/my/Class.java");
        }
    }

    @Test
    void shouldReadErrors() {
        var warnings = parse("gradle.java.log");

        assertThat(warnings).hasSize(1);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0)).hasSeverity(Severity.ERROR)
                    .hasLineStart(59)
                    .hasMessage("';' expected")
                    .hasFileName("/var/lib/jenkins/workspace/webhooks/src/main/java/File.java");
        }
    }

    /**
     * Parses a warning log with 1 warnings that are generated on Japanese environment.
     *
     * @see <a href="http://fisheye.jenkins-ci.org/changelog/Hudson?cs=16376">Commit log on changeset 16376</a>
     */
    @Test
    void parseJapaneseWarnings() {
        // force to use windows-31j - the default encoding on Windows Japanese.
        var warnings = createParser().parseReport(
                new FileReaderFactory(getResourceAsFile("ant-javac-japanese.txt"),
                        Charset.forName("windows-31j")));

        assertThat(warnings).hasSize(1);
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(1);
        softly.assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("Deprecation")
                .hasLineStart(28)
                .hasLineEnd(28)
                .hasMessage("begrussen() in ths.types.IGruss has been deprecated")
                .hasFileName(
                        "C:/Users/tiliven/.hudson/jobs/Hello THS Trunk - compile/workspace/HelloTHSTest/src/ths/Hallo.java");
    }

    @Override
    protected AntJavacParser createParser() {
        return new AntJavacParser();
    }

    @Test
    void shouldNotParseJavaCompilerAndJavaDocWarnings() {
        var warnings = parse("issue63346.log");
        assertThat(warnings).hasSize(0);
    }
}
