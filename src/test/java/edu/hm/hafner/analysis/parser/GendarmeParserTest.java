package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.IssuesAssert.*;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import edu.hm.hafner.analysis.parser.gendarme.GendarmeParser;

/**
 * Tests the class {@link GendarmeParser}.
 *
 * @author Raphael Furch
 */
public class GendarmeParserTest extends AbstractParserTest {


    /**
     * Creates a new instance of {@link AbstractParserTest}.
     */
    protected GendarmeParserTest() {
        super("gendarme/Gendarme.xml");
    }

    /**
     * Tests the Gendarme parser with a file of 3 warnings.
     */
    @Override
    protected void assertThatIssuesArePresent(final Issues<Issue> issues, final SoftAssertions softly) {

        softly.assertThat(issues).hasSize(3);

        Iterator<Issue> iterator = issues.iterator();

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
                .hasMessage(
                        "This method does not use any instance fields, properties or methods and can be made static.")
                .hasFileName("c:/Dev/src/hudson/Hudson.Domain/Dog.cs")
                .hasCategory("MethodCanBeMadeStaticRule")
                .hasPriority(Priority.LOW);
    }

    @Override
    protected AbstractParser createParser() {
        return new GendarmeParser();
    }


}
