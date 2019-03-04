package edu.hm.hafner.analysis.parser;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.time.Duration;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Categories;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

import static edu.hm.hafner.analysis.assertj.Assertions.*;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests the class {@link AntJavacParser}.
 */
class AntJavacParserTest extends AbstractParserTest {
    AntJavacParserTest() {
        super("ant-javac.txt");
    }

    /**
     * Parses a warning log with a very long line that will take several seconds to parse.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-55805">Issue 55805</a>
     */
    @Test
    void issue55805() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> parse("issue55805.txt"));
    }

    /**
     * Parses a warning log with two warnings.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-24611">Issue 24611</a>
     */
    @Test
    void testIssue24611() {
        Report warnings = parse("issue24611.txt");

        assertThat(warnings).hasSize(2);
    }

    /**
     * Parses a warning log with one warning that refers to a missing class file.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-21240">Issue 21240</a>
     */
    @Test
    void issue21240() {
        Report warnings = parse("issue21240.txt");

        assertThat(warnings).hasSize(1);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage(
                            "Cannot find annotation method 'xxx()' in type 'yyyy': class file for fully.qualified.ClassName not found")
                    .hasFileName("aaa.class");
        });
    }

    /**
     * Parses a warning log with 2 ANT warnings.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-2133">Issue 2133</a>
     */
    @Test
    void issue2133() {
        Report warnings = parse("issue2133.txt");

        assertThat(warnings).hasSize(2);

        assertSoftly(softly -> {
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
        });
    }

    /**
     * Parses a warning log with 1 warnings that has no associated file.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-4098">Issue 4098</a>
     */
    @Test
    void issue4098() {
        Report warnings = parse("issue4098.txt");

        assertThat(warnings).hasSize(1);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasCategory("Path")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage(
                            "bad path element \"C:\\...\\.hudson\\jobs\\...\\log4j.jar\": no such file or directory")
                    .hasFileName("C:/.../.hudson/jobs/.../log4j.jar");
        });
    }

    /**
     * Parses a warning log with 20 ANT warnings. 2 of them are duplicate, all are of priority Normal.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-2316">Issue 2316</a>
     */
    @Test
    void issue2316() {
        Report warnings = parse("issue2316.txt");

        assertThat(warnings)
                .hasSize(18)
                .hasDuplicatesSize(2)
                .hasSeverities(0, 0, 18, 0);
    }

    /**
     * Parses a warning log with 3 ANT warnings. They all use different tasks.
     */
    @Test
    void parseDifferentTaskNames() {
        Report warnings = parse("taskname.txt");

        assertThat(warnings).hasSize(1).hasDuplicatesSize(2);
    }

    /**
     * Verifies that arrays in deprecated methods are correctly handled.
     */
    @Test
    void parseArrayInDeprecatedMethod() {
        Report warnings = parse("issue5868.txt");

        assertThat(warnings).hasSize(1);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0)).hasSeverity(Severity.WARNING_NORMAL)
                    .hasCategory("Deprecation")
                    .hasLineStart(225)
                    .hasLineEnd(225)
                    .hasMessage(
                            "loadAvailable(java.lang.String,int,int,java.lang.String[]) in my.OtherClass has been deprecated")
                    .hasFileName("D:/path/to/my/Class.java");
        });
    }

    @Test
    void shouldReadErrors() {
        Report warnings = parse("gradle.java.log");

        assertThat(warnings).hasSize(1);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0)).hasSeverity(Severity.ERROR)
                    .hasLineStart(59)
                    .hasMessage("';' expected")
                    .hasFileName("/var/lib/jenkins/workspace/webhooks/src/main/java/File.java");
        });
    }

    /**
     * Parses a warning log with 1 warnings that are generated on Japanese environment.
     *
     * @throws UnsupportedEncodingException
     *         if the encoding is not found
     * @see <a href="http://fisheye.jenkins-ci.org/changelog/Hudson?cs=16376">Commit log on changeset 16376</a>
     */
    @Test
    void parseJapaneseWarnings() throws UnsupportedEncodingException {
        // force to use windows-31j - the default encoding on Windows Japanese.
        InputStreamReader is = new InputStreamReader(
                AntJavacParserTest.class.getResourceAsStream("ant-javac-japanese.txt"), "windows-31j");
        ReaderFactory readerFactory = createReaderFactory();
        when(readerFactory.create()).thenReturn(is);
        Report warnings = createParser().parse(readerFactory);

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
                .hasFileName("C:/Users/tiliven/.hudson/jobs/Hello THS Trunk - compile/workspace/HelloTHSTest/src/ths/Hallo.java");
    }

    @Override
    protected AntJavacParser createParser() {
        return new AntJavacParser();
    }
}

