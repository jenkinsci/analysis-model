package edu.hm.hafner.analysis.parser.violations;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

/**
 * Tests the class {@link XmlLintAdapter}.
 *
 * @author Ullrich Hafner
 */
class XmlLintAdapterTest extends AbstractParserTest {
    XmlLintAdapterTest() {
        super("xmllint.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(3);
        softly.assertThat(report.get(2))
                .hasMessage("Opening and ending tag mismatch: font line 4 and body")
                .hasFileName("xml/other.xml")
                .hasLineStart(5)
                .hasType("parser error")
                .hasSeverity(Severity.WARNING_HIGH);
    }

    @Override
    protected XmlLintAdapter createParser() {
        return new XmlLintAdapter();
    }
}
