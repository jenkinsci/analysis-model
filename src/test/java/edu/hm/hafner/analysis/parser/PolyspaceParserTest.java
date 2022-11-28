package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

/**
 * Tests the class {@link PolyspaceParser}.
 *
 *  @author Eva Habeeb
 */
class PolyspaceParserTest extends AbstractParserTest {
    /**
     * Creates a new instance of {@link PolyspaceParserTest}.
     */
    protected PolyspaceParserTest() {
        super("polyspace.csv");
    }

    @Override
    protected PolyspaceParser createParser() {
        return new PolyspaceParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(9);

        softly.assertThat(report.get(0))
                .hasLineStart(222)
                .hasMessage(
                        "Check: Null pointer Impact: High")
                .hasModuleName("calculate()")
                .hasCategory("memory")
                .hasDescription("Defect")
                .hasSeverity(Severity.WARNING_HIGH);
        softly.assertThat(report.get(1))
                .hasLineStart(276)
                .hasDescription("Defect")
                .hasMessage(
                        "Check: Qualifier removed in conversion Impact: Low")
                .hasCategory("Programming")
                .hasSeverity(Severity.WARNING_HIGH);
        softly.assertThat(report.get(5))
                .hasLineStart(512)
                .hasDescription("MISRA C:2012")
                .hasMessage(
                        "Check: 11.1 Conversions shall not be performed between a pointer to a function and any other type. Category: Required")
                .hasModuleName("tester()")
                .hasSeverity(Severity.WARNING_NORMAL);
    }
}

