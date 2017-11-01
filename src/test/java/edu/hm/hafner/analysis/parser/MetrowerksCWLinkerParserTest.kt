package edu.hm.hafner.analysis.parser

import edu.hm.hafner.analysis.Priority
import edu.hm.hafner.analysis.assertj.assertSoftly
import org.junit.jupiter.api.Test
import java.io.IOException

/**
 * Tests the class [MetrowerksCWLinkerParser].
 */
class MetrowerksCWLinkerParserTest : ParserTester() {

    /**
     * Parses a file with two GCC warnings.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    fun testWarningsParser() {

        assertSoftly {
            val warnings = MetrowerksCWLinkerParser().parse(openFile())

            assertThat(warnings)
                    .hasSize(3)

            var index = 0

            assertThat(warnings[index++])
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("L1822: Symbol TestFunction in file e:/work/PATH/PATH/PATH/PATH/appl_src.lib is undefined")
                    .hasFileName("See Warning message")
                    .hasType(TYPE)
                    .hasCategory(ERROR_CATEGORY)
                    .hasPriority(Priority.HIGH)

            assertThat(warnings[index++])
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("L1916: Section name TEST_SECTION is too long. Name is cut to 90 characters length")
                    .hasFileName("See Warning message")
                    .hasType(TYPE)
                    .hasCategory(WARNING_CATEGORY)
                    .hasPriority(Priority.NORMAL)

            assertThat(warnings[index])
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("L2: Message overflow, skipping WARNING messages")
                    .hasFileName("See Warning message")
                    .hasType(TYPE)
                    .hasCategory(INFO_CATEGORY)
                    .hasPriority(Priority.LOW)

        }
    }


    /** {@inheritDoc}  */
    override fun getWarningsFile(): String {
        return "MetrowerksCWLinker.txt"
    }

    companion object {
        private val INFO_CATEGORY = "Info"
        private val WARNING_CATEGORY = "Warning"
        private val ERROR_CATEGORY = "ERROR"
        private val TYPE = MetrowerksCWLinkerParser().id
    }
}

