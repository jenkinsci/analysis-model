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
        softly.assertThat(report).hasSize(3).hasDuplicatesSize(0);
        softly.assertThat(report.get(0))
                .hasFileName("file1.adoc")
                .hasDescription("RedHat.SentenceLength")
                .hasMessage("Try to keep sentences to an average of 32 words or fewer.")
                .hasLineStart(10)
                .hasColumnStart(1)
                .hasColumnEnd(5)
                .hasSeverity(Severity.WARNING_LOW);
        softly.assertThat(report.get(1))
                .hasFileName("file2.adoc")
                .hasDescription("RedHat.TermsWarnings")
                .hasMessage("Consider using 'might' or 'can' rather than 'may' unless updating existing content that uses the term.")
                .hasLineStart(39)
                .hasColumnStart(143)
                .hasColumnEnd(145)
                .hasSeverity(Severity.WARNING_NORMAL);
        softly.assertThat(report.get(2))
                .hasFileName("file2.adoc")
                .hasDescription("RedHat.Using")
                .hasMessage("Use 'by using' instead of 'using' when it follows a noun for clarity and grammatical correctness.")
                .hasLineStart(51)
                .hasColumnStart(44)
                .hasColumnEnd(64)
                .hasSeverity(Severity.ERROR);
    }

    @Override
    protected IssueParser createParser() {
        return new ValeParser();
    }
}
