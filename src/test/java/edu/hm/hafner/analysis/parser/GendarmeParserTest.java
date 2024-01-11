package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.parser.gendarme.GendarmeParser;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

/**
 * Tests the class {@link GendarmeParser}.
 *
 * @author Ullrich Hafner
 * @author Raphael Furch
 */
class GendarmeParserTest extends AbstractParserTest {
    protected GendarmeParserTest() {
        super("gendarme/Gendarme.xml");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(3);

        Iterator<Issue> iterator = report.iterator();

        softly.assertThat(iterator.next())
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("This assembly is not decorated with the [CLSCompliant] attribute.")
                .hasFileName("-")
                .hasCategory("MarkAssemblyWithCLSCompliantRule")
                .hasSeverity(Severity.WARNING_HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(10)
                .hasLineEnd(10)
                .hasMessage(
                        "This method does not use any instance fields, properties or methods and can be made static.")
                .hasFileName("C:/Dev/src/hudson/Hudson.Domain/Dog.cs")
                .hasCategory("MethodCanBeMadeStaticRule")
                .hasSeverity(Severity.WARNING_LOW);

        softly.assertThat(iterator.next())
                .hasLineStart(22)
                .hasLineEnd(22)
                .hasMessage(
                        "This method does not use any instance fields, properties or methods and can be made static.")
                .hasFileName("C:/Dev/src/hudson/Hudson.Domain/Dog.cs")
                .hasCategory("MethodCanBeMadeStaticRule")
                .hasSeverity(Severity.WARNING_LOW);
    }

    @Override
    protected GendarmeParser createParser() {
        return new GendarmeParser();
    }
}
