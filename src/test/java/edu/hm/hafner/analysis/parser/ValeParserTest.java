package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

class ValeParserTest extends AbstractParserTest {
    ValeParserTest() {
        super("vale-report.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(12).hasDuplicatesSize(0);
        softly.assertThat(report.get(0))
                .hasFileName("file3.adoc")
                .hasDescription("RedHat.SimpleWords")
                .hasSeverity(Severity.WARNING_LOW);
    }

    @Override
    protected IssueParser createParser() {
        return new ValeParser();
    }
}
