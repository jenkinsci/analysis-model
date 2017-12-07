package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import org.apache.commons.io.input.BOMInputStream;
import org.junit.Test;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.Assert.*;

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

        assertEquals(2, warnings.size());
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

        assertEquals(1, warnings.size());
        checkWarning(warnings.iterator().next(),
                0,
                "Cannot find annotation method 'xxx()' in type 'yyyy': class file for fully.qualified.ClassName not found",
                "aaa.class", WARNING_TYPE, Priority.NORMAL);
    }

    /**
     * Parses a file with two deprecation warnings.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void parseDeprecation() throws IOException {
        Issues warnings = new AntJavacParser().parse(openFile());

        assertEquals(1, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation = iterator.next();
        checkWarning(annotation,
                28,
                "begrussen() in ths.types.IGruss has been deprecated",
                "C:/Users/tiliven/.hudson/jobs/Hello THS Trunk - compile/workspace/HelloTHSTest/src/ths/Hallo.java",
                WARNING_TYPE, "Deprecation", Priority.NORMAL);
    }

    /**
     * Parses a warning log with 2 ANT warnings.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-2133">Issue 2133</a>
     */
    @Test
    public void issue2133() throws IOException {
        Issues warnings = new AntJavacParser().parse(openFile("issue2133.txt"));

        assertEquals(2, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        checkWarning(iterator.next(),
                86,
                "non-varargs call of varargs method with inexact argument type for last parameter;",
                "/home/hudson/hudson/data/jobs/Mockito/workspace/trunk/test/org/mockitousage/misuse/DescriptiveMessagesOnMisuseTest.java",
                WARNING_TYPE, DEFAULT_CATEGORY, Priority.NORMAL);
        checkWarning(iterator.next(),
                51,
                "<T>stubVoid(T) in org.mockito.Mockito has been deprecated",
                "/home/hudson/hudson/data/jobs/Mockito/workspace/trunk/test/org/mockitousage/stubbing/StubbingWithThrowablesTest.java",
                WARNING_TYPE, AbstractParser.DEPRECATION, Priority.NORMAL);
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

        assertEquals(1, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        checkWarning(iterator.next(),
                0,
                "bad path element \"C:\\...\\.hudson\\jobs\\...\\log4j.jar\": no such file or directory",
                "C:/.../.hudson/jobs/.../log4j.jar",
                WARNING_TYPE, "Path", Priority.NORMAL);
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

        assertEquals(20, warnings.size());

        assertEquals(0, warnings.getHighPrioritySize());
        assertEquals(20, warnings.getNormalPrioritySize());
        assertEquals(0, warnings.getLowPrioritySize());
    }

    /**
     * Parses a warning log with 3 ANT warnings. They all use different tasks.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void parseDifferentTaskNames() throws IOException {
        Issues warnings = new AntJavacParser().parse(openFile("taskname.txt"));

        assertEquals(3, warnings.size());
    }

    /**
     * Verifies that arrays in deprecated methods are correctly handled.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void parseArrayInDeprecatedMethod() throws IOException {
        Issues warnings = new AntJavacParser().parse(openFile("issue5868.txt"));

        assertEquals(1, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        checkWarning(iterator.next(),
                225,
                "loadAvailable(java.lang.String,int,int,java.lang.String[]) in my.OtherClass has been deprecated",
                "D:/path/to/my/Class.java",
                WARNING_TYPE, "Deprecation", Priority.NORMAL);
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
        assertEquals(1, warnings.size());
    }

    @Override
    protected String getWarningsFile() {
        return "ant-javac.txt";
    }
}

