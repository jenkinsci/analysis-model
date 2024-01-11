package edu.hm.hafner.analysis.parser.violations;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

/**
 * Tests the class {@link CodeNarcAdapter}.
 *
 * @author Ullrich Hafner
 */
class CodeNarcAdapterTest extends AbstractParserTest {
    CodeNarcAdapterTest() {
        super("codeNarc.xml");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(11);
        softly.assertThat(report.get(6))
                .hasMessage("In most cases, exceptions should not be caught and ignored (swallowed).")
                .hasFileName("foo/bar/Test.groovy")
                .hasType("EmptyCatchBlock")
                .hasLineStart(192)
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Override
    protected CodeNarcAdapter createParser() {
        return new CodeNarcAdapter();
    }
}
