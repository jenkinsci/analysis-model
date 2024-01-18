package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

/**
 * Tests the class {@link MetrowerksCwCompilerParser}.
 */
class MetrowerksCwCompilerParserTest extends AbstractParserTest {
    private static final String INFO_CATEGORY = "Info";
    private static final String WARNING_CATEGORY = "Warning";
    private static final String ERROR_CATEGORY = "ERROR";

    /**
     * Creates a new instance of {@link MetrowerksCwCompilerParserTest}.
     */
    protected MetrowerksCwCompilerParserTest() {
        super("MetrowerksCWCompiler.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(5);
        softly.assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(570)
                .hasLineEnd(570)
                .hasMessage(
                        "Warning-directive found: EEPROM_QUEUE_BUFFER_SIZE instead of MONITOR_ERROR_DATA_LENGTH is used here. This must be fixed sooner or later")
                .hasFileName("E:/work/PATH/PATH/PATH/PATH/Test1.c");
        softly.assertThat(report.get(1))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(305)
                .hasLineEnd(305)
                .hasMessage("Possible loss of data")
                .hasFileName("E:/work/PATH/PATH/PATH/Test2.c");
        softly.assertThat(report.get(2))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory(ERROR_CATEGORY)
                .hasLineStart(1501)
                .hasLineEnd(1501)
                .hasMessage("bla not declared (or typename)")
                .hasFileName("E:/work/PATH/PATH/Test3.c");
        softly.assertThat(report.get(3))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory(ERROR_CATEGORY)
                .hasLineStart(1502)
                .hasLineEnd(1502)
                .hasMessage("';' missing")
                .hasFileName("E:/work/PATH/Test4.c");
        softly.assertThat(report.get(4))
                .hasSeverity(Severity.WARNING_LOW)
                .hasCategory(INFO_CATEGORY)
                .hasLineStart(480)
                .hasLineEnd(480)
                .hasMessage("Inline expansion done for function call")
                .hasFileName("E:/work/PATH/PATH/PATH/PATH/PATH/PATH/PATH/Test5.c");
    }

    @Override
    protected MetrowerksCwCompilerParser createParser() {
        return new MetrowerksCwCompilerParser();
    }
}

