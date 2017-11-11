package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.IssuesAssert.*;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

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
     */
    @Test
    public void parseDeprecation() {
        Issues<Issue> warnings = createParser().parse(openFile());

        assertThat(warnings).hasSize(8);

        Issue annotation = warnings.get(0);
        assertSoftly(softly -> softly.assertThat(annotation)
                .hasPriority(Priority.NORMAL)
                .hasCategory(DEFAULT_CATEGORY)
                .hasLineStart(3)
                .hasLineEnd(3)
                .hasMessage("The serializable class AttributeException does not declare a static final serialVersionUID field of type long")
                .hasFileName("C:/Desenvolvimento/Java/jfg/src/jfg/AttributeException.java")
                .hasType(TYPE)
        );
    }
}
