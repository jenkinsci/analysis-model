package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.IssuesAssert.*;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import edu.hm.hafner.analysis.parser.gendarme.GendarmeParser;

/**
 * Tests the class {@link GendarmeParser}.
 *
 * @author Raphael Furch
 */
public class GendarmeParserTest extends ParserTester {
    /**
     * Tests the Gendarme parser with a file of 3 warnings.
     */
    @Test
    public void testParseViolationData() {
        Issues<Issue> results = new GendarmeParser().parse(openFile());

        assertThat(results)
                .hasSize(3);

        Iterator<Issue> iterator = results.iterator();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(iterator.next())
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("This assembly is not decorated with the [CLSCompliant] attribute.")
                .hasFileName("-")
                .hasCategory("MarkAssemblyWithCLSCompliantRule")
                .hasPriority(Priority.HIGH);


        softly.assertThat(iterator.next())
                .hasLineStart(10)
                .hasLineEnd(10)
                .hasMessage("This method does not use any instance fields, properties or methods and can be made static.")
                .hasFileName("c:/Dev/src/hudson/Hudson.Domain/Dog.cs")
                .hasCategory("MethodCanBeMadeStaticRule")
                .hasPriority(Priority.LOW);


        softly.assertThat(iterator.next())
                .hasLineStart(22)
                .hasLineEnd(22)
                .hasMessage("This method does not use any instance fields, properties or methods and can be made static.")
                .hasFileName("c:/Dev/src/hudson/Hudson.Domain/Dog.cs")
                .hasCategory("MethodCanBeMadeStaticRule")
                .hasPriority(Priority.LOW);
        softly.assertAll();
    }

    @Override
    protected String getWarningsFile() {
        return "gendarme/Gendarme.xml";
    }
}
