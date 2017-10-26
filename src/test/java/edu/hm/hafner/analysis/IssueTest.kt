package edu.hm.hafner.analysis

import edu.hm.hafner.analysis.assertj.IssueSoftAssertions
import edu.hm.hafner.analysis.assertj.assertSoftly
import org.junit.jupiter.api.Test


internal class IssueTest {

    @Test
    fun createDefaultIssue() {
        val sut = IssueBuilder().build()

        assertSoftly<IssueSoftAssertions> {
            assertThat(sut)
                    .hasFileName("-")
                    .hasCategory("")
                    .hasType("-")
                    .hasPriority(Priority.NORMAL)
                    .hasMessage("")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasColumnStart(0)
                    .hasColumnEnd(0)
                    .hasFingerPrint("-")
        }
    }

    @Test
    fun createSimpleIssue() {
        val sut = IssueBuilder()
                .setFileName("/random/folder/fileName")
                .setLineStart(0)
                .setLineEnd(10)
                .setColumnStart(0)
                .setColumnEnd(10)
                .setCategory("category")
                .setType("type")
                .setPriority(Priority.NORMAL)
                .setMessage("Error in File \"fileName\"")
                .setDescription("description")
                .setPackageName("package")
                .build().apply { fingerprint = "fingerprint" }

        assertSoftly<IssueSoftAssertions> {
            assertThat(sut)
                    .hasFileName("/random/folder/fileName")
                    .hasLineStart(0)
                    .hasLineEnd(10)
                    .hasColumnStart(0)
                    .hasColumnEnd(10)
                    .hasCategory("category")
                    .hasType("type")
                    .hasPriority(Priority.NORMAL)
                    .hasMessage("Error in File \"fileName\"")
                    .hasDescription("description")
                    .hasPackageName("package")
                    .hasFingerPrint("fingerprint")
                    .hasId(sut.id)
        }
    }

    @Test
    fun lineEndAndColumnEndAreZero() {
        val sut = IssueBuilder()
                .setLineEnd(0)
                .setLineStart(10)
                .setColumnEnd(0)
                .setColumnStart(10)
                .build()

        assertSoftly<IssueSoftAssertions> {
            assertThat(sut)
                    .hasLineEnd(10)
                    .hasLineStart(10)
                    .hasColumnEnd(10)
                    .hasColumnStart(10)
        }
    }

    @Test
    fun negativeLineAndColumnParams() {
        val sut = IssueBuilder()
                .setLineStart(-1)
                .setLineEnd(-1)
                .setColumnStart(-1)
                .setColumnEnd(-1)
                .build()

        assertSoftly<IssueSoftAssertions> {
            assertThat(sut)
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasColumnStart(0)
                    .hasColumnEnd(0)
        }
    }

    @Test
    fun issueWithWindowsFileName() {
        val sut = IssueBuilder()
                .setFileName("\\random\\folder\\filename")
                .build()

        assertSoftly<IssueSoftAssertions> {
            assertThat(sut).hasFileName("/random/folder/filename")
        }
    }

    @Test
    fun setFingerPrintToNullAndReplaceWithDefault() {
        val sut = IssueBuilder().build().apply { fingerprint = null }

        assertSoftly<IssueSoftAssertions> {
            assertThat(sut).hasFingerPrint("-")
        }
    }

    @Test
    fun setFingerPrintToEmptyStringAndReplaceWithDefault() {
        val sut = IssueBuilder().build().apply { fingerprint = "" }
        assertSoftly<IssueSoftAssertions> {
            assertThat(sut).hasFingerPrint("-")
        }
    }

    @Test
    fun stringRepresentationOfDefaultIssue() {
        val sut = IssueBuilder().build()
        assertSoftly<IssueSoftAssertions> {
            assertThat(sut).hasToString("-(0,0): -: : ")
        }
    }

    @Test
    fun stringRepresentationOfSimpleIssue() {
        val sut = IssueBuilder()
                .setFileName("/random/folder/fileName")
                .setLineStart(0)
                .setLineEnd(10)
                .setColumnStart(0)
                .setColumnEnd(10)
                .setCategory("category")
                .setType("type")
                .setPriority(Priority.NORMAL)
                .setMessage("Error in File \"fileName\"")
                .setDescription("description")
                .setPackageName("package")
                .build().apply { fingerprint = "fingerprint" }

        assertSoftly<IssueSoftAssertions> {
            assertThat(sut).hasToString(with(sut) {
                "$fileName($lineStart,$columnStart): $type: $category: $message"
            })
        }
    }
}