package edu.hm.hafner.analysis.parser.violations;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link CodeNarcAdapter}.
 *
 * @author Ullrich Hafner
 */
class CodeNarcAdapterTest extends AbstractParserTest  {
    CodeNarcAdapterTest() {
        super("codeNarc.xml");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(11);
        softly.assertThat(report.get(0))
                .hasMessage("In most cases, exceptions should not be caught and ignored (swallowed).")
                .hasFileName("foo/bar/Test.groovy")
                .hasType("EmptyCatchBlock")
                .hasLineStart(192)
                .hasPriority(Priority.NORMAL);
    }

    @Override
    protected AbstractParser createParser() {
        return new CodeNarcAdapter();
    }
}