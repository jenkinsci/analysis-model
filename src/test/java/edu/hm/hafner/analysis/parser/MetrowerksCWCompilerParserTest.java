package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link MetrowerksCWCompilerParser}.
 */
public class MetrowerksCWCompilerParserTest extends ParserTester {

    private static final String INFO_CATEGORY = "Info";
    private static final String WARNING_CATEGORY = "Warning";
    private static final String ERROR_CATEGORY = "ERROR";
    private static final String WARNING_TYPE = new MetrowerksCWCompilerParser().getId();

    /**
     * Parses a file with CodeWarrior warnings.
     */
    @Test
    public void testWarningsParser() {
        Issues<Issue> warnings = new MetrowerksCWCompilerParser().parse(openFile());

        assertThat(warnings).hasSize(5);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(WARNING_CATEGORY)
                    .hasLineStart(570)
                    .hasLineEnd(570)
                    .hasMessage(
                            "Warning-directive found: EEPROM_QUEUE_BUFFER_SIZE instead of MONITOR_ERROR_DATA_LENGTH is used here. This must be fixed sooner or later")
                    .hasFileName("E:/work/PATH/PATH/PATH/PATH/Test1.c");
            softly.assertThat(warnings.get(1))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(WARNING_CATEGORY)
                    .hasLineStart(305)
                    .hasLineEnd(305)
                    .hasMessage("Possible loss of data")
                    .hasFileName("E:/work/PATH/PATH/PATH/Test2.c");
            softly.assertThat(warnings.get(2))
                    .hasPriority(Priority.HIGH)
                    .hasCategory(ERROR_CATEGORY)
                    .hasLineStart(1501)
                    .hasLineEnd(1501)
                    .hasMessage("bla not declared (or typename)")
                    .hasFileName("E:/work/PATH/PATH/Test3.c");
            softly.assertThat(warnings.get(3))
                    .hasPriority(Priority.HIGH)
                    .hasCategory(ERROR_CATEGORY)
                    .hasLineStart(1502)
                    .hasLineEnd(1502)
                    .hasMessage("';' missing")
                    .hasFileName("E:/work/PATH/Test4.c");
            softly.assertThat(warnings.get(4))
                    .hasPriority(Priority.LOW)
                    .hasCategory(INFO_CATEGORY)
                    .hasLineStart(480)
                    .hasLineEnd(480)
                    .hasMessage("Inline expansion done for function call")
                    .hasFileName("E:/work/PATH/PATH/PATH/PATH/PATH/PATH/PATH/Test5.c");
        });
    }

    @Override
    protected String getWarningsFile() {
        return "MetrowerksCWCompiler.txt";
    }
}

