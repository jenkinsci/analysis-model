package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.Assert.*;

/**
 * Tests the class {@link ScalacParser}. Author: <a href="mailto:alexey.kislin@gmail.com">Alexey Kislin</a>
 */
public class ScalacParserTest extends ParserTester {

    @Test
    public void basicFunctionality() throws IOException {
        Issues warnings = parse("scalac.txt");
        assertEquals(3, warnings.size());
        Iterator<Issue> iter = warnings.iterator();
        checkWarning(iter.next(), 29, "implicit conversion method toLab2OI should be enabled",
                "/home/user/.jenkins/jobs/job/workspace/some/path/SomeFile.scala", "warning", Priority.NORMAL);
        checkWarning(iter.next(), 408, "method asJavaMap in object JavaConversions is deprecated: use mapAsJavaMap instead",
                "/home/user/.jenkins/jobs/job/workspace/another/path/SomeFile.scala", "warning", Priority.NORMAL);
        checkWarning(iter.next(), 59, "method error in object Predef is deprecated: Use `sys.error(message)` instead",
                "/home/user/.jenkins/jobs/job/workspace/yet/another/path/SomeFile.scala", "warning", Priority.HIGH);
    }

    private Issues parse(final String fileName) throws IOException {
        return new ScalacParser().parse(openFile(fileName));
    }

    @Override
    protected String getWarningsFile() {
        return "scalac.txt";
    }
}
