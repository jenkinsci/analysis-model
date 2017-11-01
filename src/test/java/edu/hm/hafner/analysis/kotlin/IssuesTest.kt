package edu.hm.hafner.analysis

/*
import edu.hm.hafner.analysis.assertj.kotlin.IssueAssert
import edu.hm.hafner.analysis.assertj.kotlin.IssuesAssert
import edu.hm.hafner.analysis.assertj.kotlin.IssuesSoftAssertions
import edu.hm.hafner.analysis.assertj.assertSoftly
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

internal class IssuesTest {

    @Test
    fun emptyIssues() {
        assertSoftly<IssuesSoftAssertions> {
            assertThat(Issues())
                    .hasSize(0)
                    .hasHighPrioritySize(0)
                    .hasNormalPrioritySize(0)
                    .hasLowPrioritySize(0)
                    .hasNumberOfFiles(0)
        }
    }

    @Test
    fun addIssueTwice() {
        assertSoftly<IssuesSoftAssertions> {
            val sut = Issues().apply {
                val issue = IssueBuilder().setFileName("fileName").build()
                add(issue)
                add(issue)
            }
            assertThat(sut)
                    .hasSize(2)
                    .hasHighPrioritySize(0)
                    .hasNormalPrioritySize(2)
                    .hasLowPrioritySize(0)
                    .hasNumberOfFiles(1)
                    .hasIssueAt(0, IssueBuilder().setFileName("fileName").build())
        }
    }

    @Test
    fun addNullAndThrowNPE() {
        val sut = Issues()

        assertThatThrownBy { sut.add(null) }
                .isInstanceOf(NullPointerException::class.java)
    }

    @Test
    fun addAllWithNullCollectionAndThrowNPE() {
        assertThatThrownBy { Issues().addAll(null) }
                .isInstanceOf(NullPointerException::class.java)
    }

    @Test
    fun addAllWithEmptyCollection() {
        assertSoftly<IssuesSoftAssertions> {
            val sut = Issues().apply { addAll(emptyList()) }
            assertThat(sut)
                    .hasSize(0)
                    .hasHighPrioritySize(0)
                    .hasNormalPrioritySize(0)
                    .hasLowPrioritySize(0)
                    .hasNumberOfFiles(0)
        }
    }

    @Test
    fun addAllWithThreeIssuesWithDifferentPriorities() {
        assertSoftly<IssuesSoftAssertions> {
            val sut = Issues().apply {
                val builder = IssueBuilder()
                val collection = mutableListOf<Issue>().apply {
                    builder.setFileName("fileName1").setPriority(Priority.HIGH)
                    add(builder.build())
                    builder.setFileName("fileName2").setPriority(Priority.NORMAL)
                    add(builder.build())
                    builder.setFileName("fileName3").setPriority(Priority.LOW)
                    add(builder.build())
                }

                addAll(collection)
            }
            assertThat(sut)
                    .hasSize(3)
                    .hasHighPrioritySize(1)
                    .hasNormalPrioritySize(1)
                    .hasLowPrioritySize(1)
                    .hasNumberOfFiles(3)
        }
    }

    @Test
    fun removeNullId() {
        val sut = Issues()

        assertThatThrownBy { sut.remove(null) }
                .isInstanceOf(NoSuchElementException::class.java)
    }

    @Test
    fun removeNotContainingIssue() {
        val sut = Issues()
        assertThatThrownBy { sut.remove(IssueBuilder().build().id) }
                .isInstanceOf(NoSuchElementException::class.java)
    }

    @Test
    fun removingIssueAfterAddingIt() {
        val sut = Issues()
        val issue = IssueBuilder().setFileName("fileName").build()

        assertSoftly<IssuesSoftAssertions> {
            sut.add(issue)
            assertThat(sut)
                    .hasSize(1)
                    .hasNormalPrioritySize(1)
                    .hasNumberOfFiles(1)
            val removedIssue = sut.remove(issue.id)
            assertThat(sut)
                    .hasSize(0)
                    .hasNormalPrioritySize(0)
                    .hasNumberOfFiles(0)

            IssueAssert.assertThat(removedIssue).isEqualTo(issue)
        }
    }

    @Test
    fun removeAll() {
        assertSoftly<IssuesSoftAssertions> {
            val sut = Issues().apply {
                val builder = IssueBuilder()
                val collection = mutableListOf<Issue>().apply {
                    builder.setFileName("fileName1").setPriority(Priority.HIGH)
                    add(builder.build())
                    builder.setFileName("fileName2").setPriority(Priority.NORMAL)
                    add(builder.build())
                    builder.setFileName("fileName3").setPriority(Priority.LOW)
                    add(builder.build())
                }

                addAll(collection)
            }

            repeat(3) {
                sut.remove(sut[0].id)
            }
            assertThat(sut)
                    .hasSize(0)
                    .hasHighPrioritySize(0)
                    .hasNormalPrioritySize(0)
                    .hasLowPrioritySize(0)
                    .hasNumberOfFiles(0)
        }
    }

    @Test
    fun tryToFindIdInEmptyIssuesAndThrowNSEE() {
        assertThatThrownBy { Issues().remove(IssueBuilder().build().id) }
                .isInstanceOf(NoSuchElementException::class.java)

    }

    @Test
    fun tryToFindNullIdInEmptyIssuesAndThrowNSEE() {
        assertThatThrownBy { Issues().remove(null) }
                .isInstanceOf(NoSuchElementException::class.java)
    }

    @Test
    fun findIdInIssuesContainingASingleElement() {
        assertSoftly<IssuesSoftAssertions> {
            val sut = Issues()
            val issue = IssueBuilder().setFileName("fileName").build()
            sut.add(issue)

            assertThat(sut).hasSize(1)

            val foundIssue = sut.findById(issue.id)
            IssueAssert.assertThat(foundIssue).isEqualTo(issue) }
    }

    @Test
    fun findTenthById() {
        val sut = Issues()
        repeat(9) {
            sut.add(IssueBuilder().build())
        }

        val tenth = IssueBuilder().setFileName("tenthFile").build()

        sut.add(tenth)

        IssuesAssert.assertThat(sut).hasSize(10)
        val foundIssue = sut.findById(tenth.id)

        IssueAssert.assertThat(foundIssue).isEqualTo(tenth)

    }

    @Test
    fun getFirstAndSecondElement() {
        val builder = IssueBuilder()
        val first = builder.setFileName("fileName1").build()
        val second = builder.setFileName("fileName2").build()

        val sut = Issues().apply { addAll(listOf(first, second)) }
        IssueAssert.assertThat(sut[0])
                .hasFileName("fileName1")

        IssueAssert.assertThat(sut[1])
                .hasFileName("fileName2")

    }

    @Test
    fun getPropertiesInEmptyIssues() {
        val sut = Issues()

        val list = sut.getProperties {
            it.fileName
        }

        Assertions.assertThat(list).isEmpty()
    }

    @Test
    fun getPropertiesInTwoElementIssues() {
        val sut = Issues().apply {
            add(IssueBuilder().setFileName("fileName1").build())
            add(IssueBuilder().setFileName("fileName2").build())
        }

        val list = sut.getProperties {
            it.fileName
        }

        Assertions.assertThat(list)
                .isNotEmpty
                .contains("fileName1", "fileName2")
    }

    @Test
    fun stringRepresentationOfEmptyIssues() {
        val sut = Issues()

        assertSoftly<IssuesSoftAssertions> {
            assertThat(sut).hasToString("0 issues")
        }
    }

    @Test
    fun stringRepresentationOfIssuesWithTenElements() {
        val sut = Issues()
        repeat(10) {
            sut.add(IssueBuilder().build())
        }

        assertSoftly<IssuesSoftAssertions> {
            assertThat(sut).hasToString("10 issues")
        }

    }

    @Test
    fun copyStringRepresentationEqualsOriginalStringRepresentation() {
        val sut = Issues().apply {
            add(IssueBuilder().setFileName("fileName1").build())
            add(IssueBuilder().setFileName("fileName2").build())
        }

        Assertions.assertThat(sut.toString()).isEqualTo(sut.copy().toString())
    }
}

*/