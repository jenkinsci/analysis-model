package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.parser.gendarme.GendarmeParser;
import static org.junit.Assert.*;

/**
 * Tests the class {@link GendarmeParser}.
 *
 * @author Ullrich Hafner
 */
public class GendarmeParserTest extends ParserTester {
    /**
     * Tests the Gendarme parser with a file of 3 warnings.
     *
     * @throws IOException in case of an exception
     */
    @Test
    public void testParseViolationData() throws IOException {
        Issues results = new GendarmeParser().parse(openFile());
        assertEquals(3, results.size());

        Iterator<Issue> iterator = results.iterator();

        checkWarning(iterator.next(), 0, "This assembly is not decorated with the [CLSCompliant] attribute.",
                "-", "MarkAssemblyWithCLSCompliantRule", Priority.HIGH);
        checkWarning(iterator.next(), 10, "This method does not use any instance fields, properties or methods and can be made static.",
                "c:/Dev/src/hudson/Hudson.Domain/Dog.cs", "MethodCanBeMadeStaticRule", Priority.LOW);
        checkWarning(iterator.next(), 22, "This method does not use any instance fields, properties or methods and can be made static.",
                "c:/Dev/src/hudson/Hudson.Domain/Dog.cs", "MethodCanBeMadeStaticRule", Priority.LOW);
    }

    @Override
    protected String getWarningsFile() {
        return "gendarme/Gendarme.xml";
    }
}
