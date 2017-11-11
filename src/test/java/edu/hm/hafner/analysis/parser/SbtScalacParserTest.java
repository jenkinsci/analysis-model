package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the class {@link SbtScalacParser}. Author: <a href="mailto:hochak@gmail.com">Hochak Hung</a>
 */
public class SbtScalacParserTest extends ParserTester {

    @Test
    public void basicFunctionality() {
        Issues<Issue> warnings = new SbtScalacParser().parse(openFile());
        assertEquals(2, warnings.size());
        Iterator<Issue> iter = warnings.iterator();
        final Issue warning1 = iter.next();
        assertEquals(Priority.NORMAL, warning1.getPriority(), "Wrong priority detected: ");
        assertEquals(DEFAULT_CATEGORY, warning1.getCategory(), "Wrong category of warning detected: ");
        assertEquals(111, warning1.getLineStart(), "Wrong line start detected: ");
        assertEquals(111, warning1.getLineEnd(), "Wrong line end detected: ");
        assertEquals("method stop in class Thread is deprecated: see corresponding Javadoc for more information.", warning1
                .getMessage(), "Wrong message detected: ");
        assertEquals("/home/user/.jenkins/jobs/job/workspace/path/SomeFile.scala", warning1.getFileName(), "Wrong filename detected: ");
        final Issue warning = iter.next();
        assertEquals(Priority.HIGH, warning.getPriority(), "Wrong priority detected: ");
        assertEquals(DEFAULT_CATEGORY, warning.getCategory(), "Wrong category of warning detected: ");
        assertEquals(9, warning.getLineStart(), "Wrong line start detected: ");
        assertEquals(9, warning.getLineEnd(), "Wrong line end detected: ");
        assertEquals("';' expected but identifier found.", warning.getMessage(), "Wrong message detected: ");
        assertEquals("/home/user/.jenkins/jobs/job/workspace/another/path/SomeFile.scala", warning.getFileName(), "Wrong filename detected: ");
    }

    @Override
    protected String getWarningsFile() {
        return "sbtScalac.txt";
    }
}
