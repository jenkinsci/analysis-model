package edu.hm.hafner.analysis.parser.violations;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

/**
 * Tests the class {@link YamlLintAdapter}.
 *
 * @author Ullrich Hafner
 */
class YamlLintAdapterTest extends AbstractParserTest {
    YamlLintAdapterTest() {
        super("yamllint.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(4);
        softly.assertThat(report.get(0))
                .hasMessage("missing starting space in comment")
                .hasFileName("file.yml")
                .hasLineStart(6)
                .hasColumnStart(2)
                .hasType("comments")
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Override
    protected YamlLintAdapter createParser() {
        return new YamlLintAdapter();
    }
}
