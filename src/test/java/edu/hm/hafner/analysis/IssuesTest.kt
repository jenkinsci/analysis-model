package edu.hm.hafner.analysis

import edu.hm.hafner.util.NoSuchElementException
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test

import java.util.ArrayList

import edu.hm.hafner.analysis.IssuesAssert.Companion.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy

internal class IssuesTest {

    @Test
    fun emptyIssues() {
        assertThat(Issues()).hasSize(0)
    }

    @Test
    fun addIssueToEmptyIssues() {
        val sut = Issues()

        sut.add(IssueBuilder().setFileName("asdf").build())
        assertThat(sut)
                .hasSize(1)
                .hasHighPrioritySize(0)
                .hasNormalPrioritySize(1)
                .hasLowPrioritySize(0)
        IssueAssert.assertThat(sut.get(0))
                .hasFileName("asdf")
    }

    @Test
    fun addNullToEmptyIssuesAndThrowNPE() {
        val sut = Issues()

        assertThatThrownBy { sut.add(null) }
                .isInstanceOf(NullPointerException::class.java)
    }

    @Test
    fun addAllWithNullCollection() {
        assertThatThrownBy { Issues().addAll(null) }
                .isInstanceOf(NullPointerException::class.java)
    }

    @Test
    fun addAllWithEmptyCollection() {
        val sut = Issues()

        sut.addAll(ArrayList())
        assertThat(sut).hasSize(0)
    }

    @Test
    fun addAllWithSingleElementCollection() {

        val sut = Issues()
        val issue = IssueBuilder().setFileName("asdf").build()
        val collection = ArrayList<Issue>()
        collection.add(issue)
        sut.addAll(collection)

        assertThat(sut).hasSize(1)
        IssueAssert.assertThat(sut.get(0)).hasFileName("asdf")
    }

    @Test
    fun removeWithNull() {
        val sut = Issues()

        assertThatThrownBy { sut.remove(null) }
                .isInstanceOf(NoSuchElementException::class.java)
    }

    @Test
    fun removeNotContainingIssue() {
        val sut = Issues()
        assertThat(sut).hasSize(0)
        assertThatThrownBy { sut.remove(IssueBuilder().build().id) }
                .isInstanceOf(NoSuchElementException::class.java)
        assertThat(sut).hasSize(0)
    }

    @Test
    fun removeIssueAfterAddingIt() {
        val sut = Issues()
        val issue = IssueBuilder().setFileName("asdf").build()

        sut.add(issue)
        assertThat(sut).hasSize(1)
        val removedIssue = sut.remove(issue.id)
        assertThat(sut).hasSize(0)
        IssueAssert.assertThat(removedIssue).isEqualTo(issue)
    }

    @Test
    fun removeTenthIssue() {
        val sut = Issues()
        for (i in 0..8) {
            sut.add(IssueBuilder().build())
        }

        val tenth = IssueBuilder().setFileName("tenthFile").build()

        sut.add(tenth)
        assertThat(sut).hasSize(10)

        val removedIssue = sut.remove(tenth.id)

        IssueAssert.assertThat(removedIssue).isEqualTo(tenth)
        assertThat(sut).hasSize(9)
    }

    @Test
    fun findIdInEmptyIssues() {
        val sut = Issues()

        assertThatThrownBy { sut.remove(IssueBuilder().build().id) }
                .isInstanceOf(NoSuchElementException::class.java)

    }

    @Test
    fun findNullIdInEmptyIssues() {
        val sut = Issues()

        SoftAssertions.assertSoftly { softly ->
            softly.assertThatThrownBy { sut.remove(null) }
                    .isInstanceOf(NoSuchElementException::class.java)
        }

    }

    @Test
    fun findIdInIssuesContainingASingleElement() {
        val sut = Issues()
        val issue = IssueBuilder().setFileName("asdf").build()

        sut.add(issue)
        assertThat(sut).hasSize(1)
        val foundIssue = sut.findById(issue.id)
        IssueAssert.assertThat(foundIssue).isEqualTo(issue)
    }

    @Test
    fun findTenthId() {
        val sut = Issues()
        for (i in 0..8) {
            sut.add(IssueBuilder().build())
        }

        val tenth = IssueBuilder().setFileName("tenthFile").build()

        sut.add(tenth)
        assertThat(sut).hasSize(10)

        val foundIssue = sut.findById(tenth.id)

        IssueAssert.assertThat(foundIssue).isEqualTo(tenth)
    }

    @Test
    fun containsNoHighNormalLowPriorities() {
        val sut = Issues()
        assertThat(sut)
                .hasHighPrioritySize(0)
                .hasNormalPrioritySize(0)
                .hasLowPrioritySize(0)
    }

    @Test
    fun containsOneOfAllPriorities() {
        val sut = Issues()
        val builder = IssueBuilder()
        sut.add(builder.setPriority(Priority.HIGH).build())
        sut.add(builder.setPriority(Priority.NORMAL).build())
        sut.add(builder.setPriority(Priority.LOW).build())

        assertThat(sut)
                .hasHighPrioritySize(1)
                .hasNormalPrioritySize(1)
                .hasLowPrioritySize(1)
    }

    @Test
    fun getTenthIssue() {
        val sut = Issues()
        for (i in 0..8) {
            sut.add(IssueBuilder().build())
        }

        val tenth = IssueBuilder().setFileName("tenthFile").build()
        sut.add(tenth)

        for (i in 0..8) {
            sut.add(IssueBuilder().build())
        }

        IssueAssert.assertThat(sut.get(9)).isEqualTo(tenth)
    }

    @Test
    fun stringRepresentationOfEmptyIssues() {
        val sut = Issues()

        assertThat(sut).hasToString("0 issues")
    }

    @Test
    fun stringRepresentationOfIssuesWithTenElements() {
        val sut = Issues()
        for (i in 0..9) {
            sut.add(IssueBuilder().build())
        }

        assertThat(sut).hasToString("10 issues")
    }
}