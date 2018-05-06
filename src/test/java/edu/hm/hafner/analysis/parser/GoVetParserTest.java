package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractIssueParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.IssuesAssert.*;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link GoVetParser}.
 */
class GoVetParserTest extends AbstractIssueParserTest {
    GoVetParserTest() {
        super("govet.txt");
    }

    @Override
    protected GoVetParser createParser() {
        return new GoVetParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(2);

        softly.assertThat(report.get(0))
                .hasPriority(Priority.NORMAL)
                .hasLineStart(46)
                .hasLineEnd(46)
                .hasMessage("missing argument for Fatalf(\"%#v\"): format reads arg 2, have only 1 args")
                .hasFileName("ui_colored_test.go");
        softly.assertThat(report.get(1))
                .hasPriority(Priority.NORMAL)
                .hasLineStart(59)
                .hasLineEnd(59)
                .hasMessage("missing argument for Fatalf(\"%#v\"): format reads arg 2, have only 1 args")
                .hasFileName("ui_colored_test.go");
    }
}