package edu.hm.hafner.analysis.parser.violations;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

class CodeClimateAdapterTest extends AbstractParserTest {
    CodeClimateAdapterTest() {
        super("code-climate.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(2);
        softly.assertThat(report.get(0))
                .hasMessage("Avoid parameter lists longer than 5 parameters. [6/5]")
                .hasFileName("argument_count.rb")
                .hasType("Rubocop/Metrics/ParameterLists")
                .hasCategory("Complexity")
                .hasLineStart(2)
                .hasSeverity(Severity.WARNING_NORMAL);
        softly.assertThat(report.get(1))
                .hasMessage("Method `destroy` has 6 arguments (exceeds 4 allowed). Consider refactoring.")
                .hasFileName("argument_count.rb")
                .hasType("argument_count")
                .hasCategory("Complexity")
                .hasLineStart(2)
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Override
    protected CodeClimateAdapter createParser() {
        return new CodeClimateAdapter();
    }
}
