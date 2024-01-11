package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

/**
 * Tests for {@link TaglistParser}.
 *
 * @author Jason Faust
 */
class TaglistParserTest extends AbstractParserTest {
    TaglistParserTest() {
        super("taglist.xml");
    }

    @Override
    protected IssueParser createParser() {
        return new TaglistParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(4);

        softly.assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("@todo")
                .hasLineStart(6)
                .hasLineEnd(6)
                .hasMessage("main")
                .hasFileName("y/Z.java")
                .hasPackageName("y")
                .hasAdditionalProperties("y.Z");

        softly.assertThat(report.get(1))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("TODO")
                .hasLineStart(3)
                .hasLineEnd(3)
                .hasMessage("todo")
                .hasFileName("y/Z.java")
                .hasPackageName("y")
                .hasAdditionalProperties("y.Z");

        softly.assertThat(report.get(2))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("TODO")
                .hasLineStart(8)
                .hasLineEnd(8)
                .hasMessage("main method")
                .hasFileName("y/Z.java")
                .hasPackageName("y")
                .hasAdditionalProperties("y.Z");

        softly.assertThat(report.get(3))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("TODO")
                .hasLineStart(3)
                .hasLineEnd(3)
                .hasMessage("todo")
                .hasFileName("y/Z2.java")
                .hasPackageName("y")
                .hasAdditionalProperties("y.Z2");
    }
}
