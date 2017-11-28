package edu.hm.hafner.analysis.parser

import edu.hm.hafner.analysis.Priority
import edu.hm.hafner.analysis.assertj.assertSoftly
import org.junit.jupiter.api.Test
import java.io.IOException

/**
 * Tests the class [MetrowerksCWCompilerParser].
 */
class MetrowerksCWCompilerParserTest : ParserTester() {

    /**
     * Parses a file with CodeWarrior warnings.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    fun testWarningsParser() {
        assertSoftly{
            val warnings = MetrowerksCWCompilerParser().parse(openFile())

            assertThat(warnings)
                    .hasSize(5)

            var index = 0

            assertThat(warnings[index++])
                    .hasLineStart(570)
                    .hasLineEnd(570)
                    .hasMessage("Warning-directive found: EEPROM_QUEUE_BUFFER_SIZE instead of MONITOR_ERROR_DATA_LENGTH is used here. This must be fixed sooner or later")
                    .hasFileName("E:/work/PATH/PATH/PATH/PATH/Test1.c")
                    .hasType(WARNING_TYPE)
                    .hasCategory(WARNING_CATEGORY)
                    .hasPriority(Priority.NORMAL)

            assertThat(warnings[index++])
                    .hasLineStart(305)
                    .hasLineEnd(305)
                    .hasMessage("Possible loss of data")
                    .hasFileName("E:/work/PATH/PATH/PATH/Test2.c")
                    .hasType(WARNING_TYPE)
                    .hasCategory(WARNING_CATEGORY)
                    .hasPriority(Priority.NORMAL)

            assertThat(warnings[index++])
                    .hasLineStart(1501)
                    .hasLineEnd(1501)
                    .hasMessage("bla not declared (or typename)")
                    .hasFileName("E:/work/PATH/PATH/Test3.c")
                    .hasType(WARNING_TYPE)
                    .hasCategory(ERROR_CATEGORY)
                    .hasPriority(Priority.HIGH)

            assertThat(warnings[index++])
                    .hasLineStart(1502)
                    .hasLineEnd(1502)
                    .hasMessage("';' missing")
                    .hasFileName("E:/work/PATH/Test4.c")
                    .hasType(WARNING_TYPE)
                    .hasCategory(ERROR_CATEGORY)
                    .hasPriority(Priority.HIGH)

            assertThat(warnings[index])
                    .hasLineStart(480)
                    .hasLineEnd(480)
                    .hasMessage("Inline expansion done for function call")
                    .hasFileName("E:/work/PATH/PATH/PATH/PATH/PATH/PATH/PATH/Test5.c")
                    .hasType(WARNING_TYPE)
                    .hasCategory(INFO_CATEGORY)
                    .hasPriority(Priority.LOW)
        }
    }

    /** {@inheritDoc}  */
    override fun getWarningsFile(): String {
        return "MetrowerksCWCompiler.txt"
    }

    companion object {
        private val INFO_CATEGORY = "Info"
        private val WARNING_CATEGORY = "Warning"
        private val ERROR_CATEGORY = "ERROR"
        private val WARNING_TYPE = MetrowerksCWCompilerParser().id
    }
}

