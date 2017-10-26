package edu.hm.hafner.analysis.parser

import edu.hm.hafner.analysis.assertj.IssuesSoftAssertions
import edu.hm.hafner.analysis.assertj.assertSoftly
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.IOException

/**
 * Tests the class [MavenConsoleParser].
 *
 * @author Ullrich Hafner
 */
open class MavenConsoleParserTest : ParserTester() {
    /**
     * Verifies that errors and warnings are correctly picked up.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    fun testParsing() {
        assertSoftly<IssuesSoftAssertions> {
            val warnings = MavenConsoleParser().parse(openFile())
            assertThat(warnings)
                    .hasSize(4)
                    .hasHighPrioritySize(2)
                    .hasNormalPrioritySize(2)
        }
    }

    /**
     * Parses a file with three warnings, two of them will be ignored beacuse they are blank.
     *
     * @throws IOException if the file could not be read
     * @see [Issue 16826](http://issues.jenkins-ci.org/browse/JENKINS-16826)
     */
    @Test
    fun issue16826() {
        assertSoftly<IssuesSoftAssertions> {
            val warnings = MavenConsoleParser().parse(openFile("issue16826.txt"))
            assertThat(warnings)
                    .hasSize(1)
        }
    }

    /**
     * Parses a file with three warnings, two of them will be ignored because they are blank.
     *
     * @throws IOException if the file could not be read
     * @see [Issue 25278](http://issues.jenkins-ci.org/browse/JENKINS-25278)
     */
    @Test
    @Disabled("Until JENKINS-25278 is fixed")
    fun largeFile() {
        assertSoftly<IssuesSoftAssertions> {
            val warnings = MavenConsoleParser().parse(openFile("maven-large.log"))

            assertThat(warnings)
                    .hasSize(1)
        }
    }

    override fun getWarningsFile(): String {
        return "maven-console.txt"
    }
}

