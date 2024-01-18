package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

/**
 * Tests the class {@link ErlcParser}.
 */
class ErlcParserTest extends AbstractParserTest {
    /**
     * Creates a new instance of {@link ErlcParserTest}.
     */
    protected ErlcParserTest() {
        super("erlc.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(2);

        softly.assertThat(report.get(0)).hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("Warning")
                .hasLineStart(125)
                .hasLineEnd(125)
                .hasMessage("variable 'Name' is unused")
                .hasFileName("./test.erl");
        softly.assertThat(report.get(1)).hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("Error")
                .hasLineStart(175)
                .hasLineEnd(175)
                .hasMessage("record 'Extension' undefined")
                .hasFileName("./test2.erl");
    }

    @Override
    protected ErlcParser createParser() {
        return new ErlcParser();
    }
}

