package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.Assert.*;

/**
 * Tests {@link IdeaInspectionParser } parser class.
 *
 * @author Alex Lopashev, alexlopashev@gmail.com
 */
public class IdeaInspectionParserTest extends ParserTester {
    /**
     * Parses an example file with single inspection.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void parse() throws IOException {
        Issues inspections = new IdeaInspectionParser().parse(openFile());

        assertEquals(1, inspections.size());

        Iterator<Issue> iterator = inspections.iterator();
        Issue annotation = iterator.next();
        checkWarning(annotation,
                42,
                "Parameter <code>intentionallyUnusedString</code> is not used  in either this method or any of its derived methods",
                "file://$PROJECT_DIR$/src/main/java/org/lopashev/Test.java",
                "Unused method parameters",
                Priority.NORMAL);
    }

    @Override
    protected String getWarningsFile() {
        return "IdeaInspectionExample.xml";
    }
}

