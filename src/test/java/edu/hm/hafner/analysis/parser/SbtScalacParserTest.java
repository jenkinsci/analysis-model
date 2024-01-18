package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

/**
 * Tests the class {@link SbtScalacParser}.
 *
 * @author Hochak Hung
 */
class SbtScalacParserTest extends AbstractParserTest {
    SbtScalacParserTest() {
        super("sbtScalac.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(4);

        softly.assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(DEFAULT_CATEGORY)
                .hasLineStart(111)
                .hasLineEnd(111)
                .hasMessage(
                        "method stop in class Thread is deprecated: see corresponding Javadoc for more information.")
                .hasFileName("/home/user/.jenkins/jobs/job/workspace/path/SomeFile.scala");
        softly.assertThat(report.get(1))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory(DEFAULT_CATEGORY)
                .hasLineStart(9)
                .hasLineEnd(9)
                .hasMessage("';' expected but identifier found.")
                .hasFileName("/home/user/.jenkins/jobs/job/workspace/another/path/SomeFile.scala");
        softly.assertThat(report.get(2))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(DEFAULT_CATEGORY)
                .hasLineStart(4)
                .hasLineEnd(4)
                .hasMessage("implicit numeric widening")
                .hasFileName("/home/user/.jenkins/jobs/job/workspace/Main.scala");
        softly.assertThat(report.get(3))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory(DEFAULT_CATEGORY)
                .hasLineStart(5)
                .hasLineEnd(5)
                .hasMessage("Invalid literal number")
                .hasFileName("/home/user/.jenkins/jobs/job/workspace/Main.scala");
    }

    @Override
    protected SbtScalacParser createParser() {
        return new SbtScalacParser();
    }
}
