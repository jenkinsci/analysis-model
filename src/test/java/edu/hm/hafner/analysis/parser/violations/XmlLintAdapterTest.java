package edu.hm.hafner.analysis.parser.violations;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link XmlLintAdapter}.
 *
 * @author Ullrich Hafner
 */
class XmlLintAdapterTest extends AbstractParserTest<Issue> {
    XmlLintAdapterTest() {
        super("xmllint.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Issues<Issue> issues, final SoftAssertions softly) {
        softly.assertThat(issues).hasSize(3);
        softly.assertThat(issues.get(0))
                .hasMessage("Opening and ending tag mismatch: font line 4 and body")
                .hasFileName("xml/other.xml")
                .hasLineStart(5)
                .hasType("parser error")
                .hasPriority(Priority.HIGH);
    }

    @Override
    protected AbstractParser<Issue> createParser() {
        return new XmlLintAdapter();
    }
}