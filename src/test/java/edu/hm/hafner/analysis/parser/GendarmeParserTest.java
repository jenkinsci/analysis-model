package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertions.IssueSoftAssertions;
import edu.hm.hafner.analysis.assertions.IssuesSoftAssertions;
import edu.hm.hafner.analysis.parser.gendarme.GendarmeParser;

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
        IssuesSoftAssertions softlyWarnings = new IssuesSoftAssertions();
        softlyWarnings.assertThat(results)
                .hasSize(3);
        softlyWarnings.assertAll();

        Iterator<Issue> iterator = results.iterator();

        IssueSoftAssertions softlyIssue1 = new IssueSoftAssertions();
        softlyIssue1.assertThat(iterator.next())
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("This assembly is not decorated with the [CLSCompliant] attribute.")
                .hasFileName("-")
                .hasCategory("MarkAssemblyWithCLSCompliantRule")
                .hasPriority(Priority.HIGH);
        softlyIssue1.assertAll();

        IssueSoftAssertions softlyIssue2 = new IssueSoftAssertions();
        softlyIssue2.assertThat(iterator.next())
                .hasLineStart(10)
                .hasLineEnd(10)
                .hasMessage("This method does not use any instance fields, properties or methods and can be made static.")
                .hasFileName("c:/Dev/src/hudson/Hudson.Domain/Dog.cs")
                .hasCategory("MethodCanBeMadeStaticRule")
                .hasPriority(Priority.LOW);
        softlyIssue2.assertAll();

        IssueSoftAssertions softlyIssue3 = new IssueSoftAssertions();
        softlyIssue3.assertThat(iterator.next())
                .hasLineStart(22)
                .hasLineEnd(22)
                .hasMessage("This method does not use any instance fields, properties or methods and can be made static.")
                .hasFileName("c:/Dev/src/hudson/Hudson.Domain/Dog.cs")
                .hasCategory("MethodCanBeMadeStaticRule")
                .hasPriority(Priority.LOW);
        softlyIssue3.assertAll();
    }

    @Override
    protected String getWarningsFile() {
        return "gendarme/Gendarme.xml";
    }
}
