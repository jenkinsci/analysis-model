package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link RobocopyParser}.
 */
class RobocopyParserTest extends AbstractParserTest {
    RobocopyParserTest() {
        super("robocopy.txt");
    }

    private static final String FILENAME = "a.log";

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(3);

        softly.assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("EXTRA File")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("b")
                .hasFileName("b");
        softly.assertThat(report.get(1))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("New File")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage(FILENAME)
                .hasFileName(FILENAME);
        softly.assertThat(report.get(2))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("same")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage(FILENAME)
                .hasFileName(FILENAME);
    }

    @Override
    protected RobocopyParser createParser() {
        return new RobocopyParser();
    }
}

