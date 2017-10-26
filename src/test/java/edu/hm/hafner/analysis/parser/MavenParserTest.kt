package edu.hm.hafner.analysis.parser

import edu.hm.hafner.analysis.*
import edu.hm.hafner.analysis.assertj.assertSoftly
import edu.hm.hafner.analysis.assertj.assertThat
import java.io.IOException

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

/**
 * Tests the class [JavacParser] for output log of a maven compile.
 */
open class MavenParserTest : ParserTester() {

    /**
     * Parses a file with two deprecation warnings.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    @Throws(IOException::class)
    fun parseMaven() {

        assertSoftly<IssuesSoftAssertions> {
            val warnings = JavacParser().parse(openFile())


            assertThat(warnings)
                    .hasSize(5)

            var index = 0

            assertThat(warnings[index++])
                    .hasLineStart(3)
                    .hasLineEnd(3)
                    .hasMessage("com.sun.org.apache.xerces.internal.impl.dv.util.Base64 is Sun proprietary API and may be removed in a future release")
                    .hasFileName("/home/hudson/hudson/data/jobs/Hudson main/workspace/remoting/src/test/java/hudson/remoting/BinarySafeStreamTest.java")
                    .hasType(WARNING_TYPE)
                    .hasCategory(AbstractParser.PROPRIETARY_API)
                    .hasPriority(Priority.NORMAL)

            assertThat(warnings[index++])
                    .hasLineStart(36)
                    .hasLineEnd(36)
                    .hasMessage("com.sun.org.apache.xerces.internal.impl.dv.util.Base64 is Sun proprietary API and may be removed in a future release")
                    .hasFileName("/home/hudson/hudson/data/jobs/Hudson main/workspace/remoting/src/test/java/hudson/remoting/BinarySafeStreamTest.java")
                    .hasType(WARNING_TYPE)
                    .hasCategory(AbstractParser.PROPRIETARY_API)
                    .hasPriority(Priority.NORMAL)

            assertThat(warnings[index++])
                    .hasLineStart(47)
                    .hasLineEnd(47)
                    .hasMessage("com.sun.org.apache.xerces.internal.impl.dv.util.Base64 is Sun proprietary API and may be removed in a future release")
                    .hasFileName("/home/hudson/hudson/data/jobs/Hudson main/workspace/remoting/src/test/java/hudson/remoting/BinarySafeStreamTest.java")
                    .hasType(WARNING_TYPE)
                    .hasCategory(AbstractParser.PROPRIETARY_API)
                    .hasPriority(Priority.NORMAL)


            assertThat(warnings[index++])
                    .hasLineStart(69)
                    .hasLineEnd(69)
                    .hasMessage("com.sun.org.apache.xerces.internal.impl.dv.util.Base64 is Sun proprietary API and may be removed in a future release")
                    .hasFileName("/home/hudson/hudson/data/jobs/Hudson main/workspace/remoting/src/test/java/hudson/remoting/BinarySafeStreamTest.java")
                    .hasType(WARNING_TYPE)
                    .hasCategory(AbstractParser.PROPRIETARY_API)
                    .hasPriority(Priority.NORMAL)


            assertThat(warnings[index])
                    .hasLineStart(105)
                    .hasLineEnd(105)
                    .hasMessage("com.sun.org.apache.xerces.internal.impl.dv.util.Base64 is Sun proprietary API and may be removed in a future release")
                    .hasFileName("/home/hudson/hudson/data/jobs/Hudson main/workspace/remoting/src/test/java/hudson/remoting/BinarySafeStreamTest.java")
                    .hasType(WARNING_TYPE)
                    .hasCategory(AbstractParser.PROPRIETARY_API)
                    .hasPriority(Priority.NORMAL)

        }
    }

    override fun getWarningsFile(): String {
        return "maven.txt"
    }

    companion object {
        private val WARNING_TYPE = JavacParser().id
    }
}

