package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import edu.hm.hafner.analysis.assertj.IssueAssert;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Tests the class {@link ErlcParser}.
 */
public class ErlcParserTest extends ParserTester {
    private static final String TYPE = new ErlcParser().getId();

    /**
     * Parses a file with two Erlc warnings.
     */
    @Test
    public void testWarningsParser() {
        Issues warnings = new ErlcParser().parse(openFile());

        assertThat(warnings.size()).isEqualTo(2);

        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation = iterator.next();
        IssueAssert.assertThat(annotation).hasPriority(Priority.NORMAL)
                .hasCategory("Warning")
                .hasLineStart(125)
                .hasLineEnd(125)
                .hasMessage("variable 'Name' is unused")
                .hasFileName("./test.erl")
                .hasType(TYPE);

        annotation = iterator.next();
        IssueAssert.assertThat(annotation).hasPriority(Priority.HIGH)
                .hasCategory("Error")
                .hasLineStart(175)
                .hasLineEnd(175)
                .hasMessage("record 'Extension' undefined")
                .hasFileName("./test2.erl")
                .hasType(TYPE);

    }

    @Override
    protected String getWarningsFile() {
        return "erlc.txt";
    }
}

