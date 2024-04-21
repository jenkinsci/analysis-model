package edu.hm.hafner.analysis.parser.violations;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

class CoverityAdapterTest extends AbstractParserTest {
    CoverityAdapterTest() {
        super("coverity.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(1);
        var issue = report.get(0);
        softly.assertThat(issue)
                .hasFileName("C:/Workspace/workspace/Build_jenkins_development/somefile.cs")
                .hasCategory("Integer handling issues")
                .hasType("constant_expression_result/bit_and_with_zero")
                .hasLineStart(79)
                .hasSeverity(Severity.WARNING_NORMAL);
        softly.assertThat(issue.getMessage()).startsWith("Bitwise-and ('&amp;') operation applied to zero always produces zero.");
    }

    @Override
    protected CoverityAdapter createParser() {
        return new CoverityAdapter();
    }
}
