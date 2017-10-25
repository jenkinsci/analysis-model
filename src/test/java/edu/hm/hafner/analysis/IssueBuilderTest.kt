package edu.hm.hafner.analysis

import org.junit.jupiter.api.Test

import edu.hm.hafner.analysis.IssueAssert.Companion.assertThat

internal class IssueBuilderTest {

    @Test
    fun createIssueWithDefaultValues() {
        val want = Issue(
                "",
                0,
                0,
                0,
                0,
                "",
                "",
                "",
                Priority.NORMAL,
                "",
                ""
        )

        val sut = IssueBuilder()
                .setLineStart(0)
                .setLineEnd(0)
                .setColumnStart(0)
                .setColumnEnd(0)
                .setFileName("")
                .setCategory("")
                .setType("")
                .setPackageName("")
                .setPriority(Priority.NORMAL)
                .setMessage("")
                .setDescription("").build()

        assertThat(sut).isEqualTo(want)
    }

    @Test
    fun createIssueWithNullValues() {
        val want = Issue(null,
                0,
                0,
                0,
                0, null, null, null, null, null, null
        )

        val sut = IssueBuilder().build()

        assertThat(sut).isEqualTo(want)
    }

    @Test
    fun copyFromIssueEqualsTheSameIssue() {
        val want = Issue(
                "asdf",
                0,
                0,
                0,
                0,
                "qweqr",
                "qwer",
                "asdf",
                Priority.NORMAL,
                "jkl",
                "zuio"
        )

        val sut = IssueBuilder().copy(want).build()


        assertThat(sut).isEqualTo(want)
    }

    @Test
    fun copyFromIssueNotEqualToOtherIssue() {
        val want = Issue(
                "asdf",
                0,
                0,
                0,
                0,
                "qweqr",
                "qwer",
                "asdf",
                Priority.NORMAL,
                "jkl",
                "zuio"
        )

        val sut = IssueBuilder().copy(want).build()


        assertThat(sut).isNotEqualTo(IssueBuilder().build())
    }

    @Test
    fun fillWithUnAllowedValues() {
        val sut = IssueBuilder().apply {
            setLineStart(-1)
            setLineEnd(-1)
            setColumnStart(-1)
            setColumnEnd(-1)
            setPriority(null)
        }

        assertThat(sut.build())
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