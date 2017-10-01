package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractWarningsParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the class {@link JavacParser} for output log of a maven compile.
 */
public class MavenParserTest extends ParserTester {
    private static final String WARNING_TYPE = new JavacParser().getId();

    /**
     * Parses a file with two deprecation warnings.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void parseMaven() throws IOException {
        Issues warnings = new JavacParser().parse(openFile());

        assertEquals(5, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        checkMavenWarning(iterator.next(), 3);
        checkMavenWarning(iterator.next(), 36);
        checkMavenWarning(iterator.next(), 47);
        checkMavenWarning(iterator.next(), 69);
        checkMavenWarning(iterator.next(), 105);
    }

    /**
     * Verifies the annotation content.
     *
     * @param annotation the annotation to check
     * @param lineNumber the line number of the warning
     */
    private void checkMavenWarning(final Issue annotation, final int lineNumber) {
        checkWarning(annotation, lineNumber,
                "com.sun.org.apache.xerces.internal.impl.dv.util.Base64 is Sun proprietary API and may be removed in a future release",
                "/home/hudson/hudson/data/jobs/Hudson main/workspace/remoting/src/test/java/hudson/remoting/BinarySafeStreamTest.java",
                WARNING_TYPE, AbstractWarningsParser.PROPRIETARY_API, Priority.NORMAL);
    }

    @Override
    protected String getWarningsFile() {
        return "maven.txt";
    }
}

