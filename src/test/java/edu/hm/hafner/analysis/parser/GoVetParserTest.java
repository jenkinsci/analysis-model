package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.IssuesAssert.*;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link GoLintParser}.
 */
public class GoVetParserTest extends ParserTester {
    /**
     * Parses a file with multiple go vet warnings.
     */
    @Test
    public void testWarningsParser() {
        Issues<Issue> warnings = new GoVetParser().parse(openFile());

        assertThat(warnings).hasSize(2);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(DEFAULT_CATEGORY)
                    .hasLineStart(46)
                    .hasLineEnd(46)
                    .hasMessage("missing argument for Fatalf(\"%#v\"): format reads arg 2, have only 1 args")
                    .hasFileName("ui_colored_test.go");
            softly.assertThat(warnings.get(1))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(DEFAULT_CATEGORY)
                    .hasLineStart(59)
                    .hasLineEnd(59)
                    .hasMessage("missing argument for Fatalf(\"%#v\"): format reads arg 2, have only 1 args")
                    .hasFileName("ui_colored_test.go");
        });
    }

    @Override
    protected String getWarningsFile() {
        return "govet.txt";
    }
}