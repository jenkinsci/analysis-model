package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.Assert.*;

/**
 * Tests the class {@link SbtScalacParser}. Author: <a href="mailto:hochak@gmail.com">Hochak Hung</a>
 */
public class SbtScalacParserTest extends ParserTester {

    @Test
    public void basicFunctionality() throws IOException {
        Issues warnings = new SbtScalacParser().parse(openFile());
        assertEquals(2, warnings.size());
        Iterator<Issue> iter = warnings.iterator();
        checkWarning(iter.next(), 111, "method stop in class Thread is deprecated: see corresponding Javadoc for more information.",
                "/home/user/.jenkins/jobs/job/workspace/path/SomeFile.scala", DEFAULT_CATEGORY, Priority.NORMAL);
        checkWarning(iter.next(), 9, "';' expected but identifier found.",
                "/home/user/.jenkins/jobs/job/workspace/another/path/SomeFile.scala", DEFAULT_CATEGORY, Priority.HIGH);
    }

    @Override
    protected String getWarningsFile() {
        return "sbtScalac.txt";
    }
}
