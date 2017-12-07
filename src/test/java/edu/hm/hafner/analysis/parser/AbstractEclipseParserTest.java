package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.Assert.*;

/**
 * Basic tests for the Eclipse parser.
 *
 * @author Ullrich Hafner
 */
public abstract class AbstractEclipseParserTest extends ParserTester {
    protected static final String TYPE = new EclipseParser().getId();

    /**
     * Creates the parser under test.
     *
     * @return the created parser
     */
    protected AbstractParser createParser() {
        return new EclipseParser();
    }

    @Override
    protected String getWarningsFile() {
        return "eclipse.txt";
    }

    /**
     * Parses a file with two deprecation warnings.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void parseDeprecation() throws IOException {
        Issues warnings = createParser().parse(openFile());

        assertEquals(8, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation = iterator.next();
        checkWarning(annotation,
                3,
                "The serializable class AttributeException does not declare a static final serialVersionUID field of type long",
                "C:/Desenvolvimento/Java/jfg/src/jfg/AttributeException.java",
                TYPE, DEFAULT_CATEGORY, Priority.NORMAL);
    }
}
