package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link GoVetParser}.
 */
class GoVetParserTest extends AbstractParserTest {
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
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(46)
                .hasLineEnd(46)
                .hasMessage("missing argument for Fatalf(\"%#v\"): format reads arg 2, have only 1 args")
                .hasFileName("ui_colored_test.go");
        softly.assertThat(report.get(1))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(59)
                .hasLineEnd(59)
                .hasMessage("missing argument for Fatalf(\"%#v\"): format reads arg 2, have only 1 args")
                .hasFileName("ui_colored_test.go");
    }
}
