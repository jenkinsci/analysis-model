package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.IssuesAssert.*;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Basic tests for the Eclipse parser.
 *
 * @author Ullrich Hafner
 */
public abstract class AbstractEclipseParserTest extends AbstractParserTest {
    protected static final String CATEGORY = new IssueBuilder().build().getCategory();

    AbstractEclipseParserTest() {
        super("eclipse.txt");
    }

    @Override
    protected AbstractParser createParser() {
        return new EclipseParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Issues<Issue> issues, final SoftAssertions softly) {
        assertThat(issues).hasSize(8);

        Issue annotation = issues.get(0);
        softly.assertThat(annotation)
                .hasPriority(Priority.NORMAL)
                .hasCategory(CATEGORY)
                .hasLineStart(3)
                .hasLineEnd(3)
                .hasMessage(
                        "The serializable class AttributeException does not declare a static final serialVersionUID field of type long")
                .hasFileName("C:/Desenvolvimento/Java/jfg/src/jfg/AttributeException.java");
    }
}
