//Sarah Hofstätter
package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import org.apache.commons.io.input.BOMInputStream;
import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.AnalysisSoftAssertions;

/**
 * Tests the class {@link AntJavacParser}.
 */
public class AntJavacParserTest extends ParserTester {
    private static final String WARNING_TYPE = new AntJavacParser().getId();

    /**
     * Parses a warning log with two warnings.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-24611">Issue 24611</a>
     */
    @Test
    public void testIssue24611() throws IOException {
        InputStream file = AntJavacParser.class.getResourceAsStream("issue24611.txt");
        InputStreamReader reader = new InputStreamReader(new BOMInputStream(file), "UTF8");
        Issues warnings = new AntJavacParser().parse(reader);
        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();

        softly.assertThat(warnings).hasSize(2);
    }

    /**
     * Parses a warning log with one warning that refers to a missing class file.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-21240">Issue 21240</a>
     */
    @Test
    public void issue21240() throws IOException {
        Issues warnings = new AntJavacParser().parse(openFile("issue21240.txt"));
        final Issue warning = warnings.iterator().next();
        String warningMessage = "Cannot find annotation method 'xxx()' in type 'yyyy': class file for fully.qualified.ClassName not found";
        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();

        softly.assertThat(warnings).hasSize(1);
        softly.assertThat(warning).hasPriority(Priority.NORMAL).
                hasCategory(WARNING_TYPE).
                hasLineStart(0).
                hasLineEnd(0).
                hasMessage(warningMessage).
                hasFileName("aaa.class");
    }

    /**
     * Parses a file with two deprecation warnings.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void parseDeprecation() throws IOException {
        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        Issues warnings = new AntJavacParser().parse(openFile());
        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation = iterator.next();
        String message = "begrussen() in ths.types.IGruss has been deprecated";
        String filename = "C:/Users/tiliven/.hudson/jobs/Hello THS Trunk - compile/workspace/HelloTHSTest/src/ths/Hallo.java";


        softly.assertThat(warnings).hasSize(1);
        softly.assertThat(annotation).hasPriority(Priority.NORMAL).
                hasCategory("Deprecation").
                hasLineStart(28).
                hasLineEnd(28).
                hasMessage(message).
                hasFileName(filename).
                hasType(WARNING_TYPE);
    }

    /**
     * Parses a warning log with 2 ANT warnings.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-2133">Issue 2133</a>
     */
    @Test
    public void issue2133() throws IOException {
        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        Issues warnings = new AntJavacParser().parse(openFile("issue2133.txt"));
        Iterator<Issue> iterator = warnings.iterator();
        final Issue warning1 = iterator.next();
        String message1 = "non-varargs call of varargs method with inexact argument type for last parameter;";
        String filename1 = "/home/hudson/hudson/data/jobs/Mockito/workspace/trunk/test/org/mockitousage/misuse/DescriptiveMessagesOnMisuseTest.java";
        final Issue warning = iterator.next();
        String message = "<T>stubVoid(T) in org.mockito.Mockito has been deprecated";
        String filename = "/home/hudson/hudson/data/jobs/Mockito/workspace/trunk/test/org/mockitousage/stubbing/StubbingWithThrowablesTest.java";

        softly.assertThat(warnings).hasSize(2);
        softly.assertThat(warning1).hasPriority(Priority.NORMAL).
                hasCategory(DEFAULT_CATEGORY).
                hasLineStart(86).
                hasLineEnd(86).
                hasMessage(message1).
                hasFileName(filename1).
                hasType(WARNING_TYPE);
        softly.assertThat(warning).hasPriority(Priority.NORMAL).
                hasCategory(AbstractParser.DEPRECATION).
                hasLineStart(51).
                hasLineEnd(51).
                hasMessage(message).
                hasFileName(filename).
                hasType(WARNING_TYPE);
    }

    /**
     * Parses a warning log with 1 warnings that has no associated file.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-4098">Issue 4098</a>
     */
    @Test
    public void issue4098() throws IOException {
        Issues warnings = new AntJavacParser().parse(openFile("issue4098.txt"));
        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        Iterator<Issue> iterator = warnings.iterator();
        final Issue warning = iterator.next();
        String message = "bad path element \"C:\\...\\.hudson\\jobs\\...\\log4j.jar\": no such file or directory";
        String filename = "C:/.../.hudson/jobs/.../log4j.jar";

        softly.assertThat(warnings).hasSize(1);
        softly.assertThat(warning).hasPriority(Priority.NORMAL)
                .hasCategory("Path")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage(message)
                .hasFileName(filename)
                .hasType(WARNING_TYPE);
    }

    /**
     * Parses a warning log with 20 ANT warnings. 2 of them are duplicate, all are of priority Normal.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-2316">Issue 2316</a>
     */
    @Test
    public void issue2316() throws IOException {
        Issues warnings = new AntJavacParser().parse(openFile("issue2316.txt"));
        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();

        softly.assertThat(warnings).hasSize(20)
                .hasHighPrioritySize(0)
                .hasNormalPrioritySize(20)
                .hasLowPrioritySize(0);
    }

    /**
     * Parses a warning log with 3 ANT warnings. They all use different tasks.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void parseDifferentTaskNames() throws IOException {
        Issues warnings = new AntJavacParser().parse(openFile("taskname.txt"));
        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();

        softly.assertThat(warnings).hasSize(3);
    }

    /**
     * Verifies that arrays in deprecated methods are correctly handled.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void parseArrayInDeprecatedMethod() throws IOException {
        Issues warnings = new AntJavacParser().parse(openFile("issue5868.txt"));
        Iterator<Issue> iterator = warnings.iterator();
        final Issue warning = iterator.next();
        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        String message = "loadAvailable(java.lang.String,int,int,java.lang.String[]) in my.OtherClass has been deprecated";
        String filename = "D:/path/to/my/Class.java";

        softly.assertThat(warnings).hasSize(1);
        softly.assertThat(warning).hasPriority(Priority.NORMAL)
                .hasCategory("Deprecation")
                .hasLineStart(225)
                .hasLineEnd(225)
                .hasMessage(message)
                .hasFileName(filename)
                .hasType(WARNING_TYPE);
    }

    /**
     * Parses a warning log with 1 warnings that are generated on Japanese environment.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://fisheye.jenkins-ci.org/changelog/Hudson?cs=16376">Commit log on changeset 16376</a>
     */
    @Test
    public void parseJapaneseWarnings() throws IOException {
        // force to use windows-31j - the default encoding on Windows Japanese.
        InputStreamReader is = new InputStreamReader(ParserTester.class.getResourceAsStream("ant-javac-japanese.txt"), "windows-31j");
        Issues warnings = new AntJavacParser().parse(is);
        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();

        softly.assertThat(warnings).hasSize(1);
    }

    @Override
    protected String getWarningsFile() {
        return "ant-javac.txt";
    }
}

