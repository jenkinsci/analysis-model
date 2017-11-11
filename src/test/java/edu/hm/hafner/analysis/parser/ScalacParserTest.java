package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the class {@link ScalacParser}. Author: <a href="mailto:alexey.kislin@gmail.com">Alexey Kislin</a>
 */
public class ScalacParserTest extends ParserTester {

    @Test
    public void basicFunctionality() {
        Issues<Issue> warnings = parse("scalac.txt");
        assertEquals(3, warnings.size());
        Iterator<Issue> iter = warnings.iterator();
        final Issue warning2 = iter.next();
        assertEquals(Priority.NORMAL, warning2.getPriority(), "Wrong priority detected: ");
        assertEquals("warning", warning2.getCategory(), "Wrong category of warning detected: ");
        assertEquals(29, warning2.getLineStart(), "Wrong line start detected: ");
        assertEquals(29, warning2.getLineEnd(), "Wrong line end detected: ");
        assertEquals("implicit conversion method toLab2OI should be enabled", warning2.getMessage(), "Wrong message detected: ");
        assertEquals("/home/user/.jenkins/jobs/job/workspace/some/path/SomeFile.scala", warning2.getFileName(), "Wrong filename detected: ");
        final Issue warning1 = iter.next();
        assertEquals(Priority.NORMAL, warning1.getPriority(), "Wrong priority detected: ");
        assertEquals("warning", warning1.getCategory(), "Wrong category of warning detected: ");
        assertEquals(408, warning1.getLineStart(), "Wrong line start detected: ");
        assertEquals(408, warning1.getLineEnd(), "Wrong line end detected: ");
        assertEquals("method asJavaMap in object JavaConversions is deprecated: use mapAsJavaMap instead", warning1.getMessage(), "Wrong message detected: ");
        assertEquals("/home/user/.jenkins/jobs/job/workspace/another/path/SomeFile.scala", warning1.getFileName(), "Wrong filename detected: ");
        final Issue warning = iter.next();
        assertEquals(Priority.HIGH, warning.getPriority(), "Wrong priority detected: ");
        assertEquals("warning", warning.getCategory(), "Wrong category of warning detected: ");
        assertEquals(59, warning.getLineStart(), "Wrong line start detected: ");
        assertEquals(59, warning.getLineEnd(), "Wrong line end detected: ");
        assertEquals("method error in object Predef is deprecated: Use `sys.error(message)` instead", warning.getMessage(), "Wrong message detected: ");
        assertEquals("/home/user/.jenkins/jobs/job/workspace/yet/another/path/SomeFile.scala", warning.getFileName(), "Wrong filename detected: ");
    }

    private Issues<Issue> parse(final String fileName) {
        return new ScalacParser().parse(openFile(fileName));
    }

    @Override
    protected String getWarningsFile() {
        return "scalac.txt";
    }
}
