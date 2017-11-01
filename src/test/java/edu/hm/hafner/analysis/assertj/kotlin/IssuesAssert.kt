package edu.hm.hafner.analysis.assertj

import edu.hm.hafner.analysis.Issue
import edu.hm.hafner.analysis.Issues
import org.assertj.core.api.AbstractAssert

import java.util.SortedSet

open class IssuesAssert(actual: Issues) : AbstractAssert<IssuesAssert, Issues>(actual, IssuesAssert::class.java) {

    fun hasFiles(files: SortedSet<String>): IssuesAssert {
        isNotNull

        if (actual.files != files) {
            failWithMessage("Expected issues's files to be <%s> but was <%s>", files, actual.files)
        }

        return this
    }

    fun hasSize(size: Int): IssuesAssert {
        isNotNull

        if (actual.size != size) {
            failWithMessage("Expected issues's size to be <%s> but was <%s>", size, actual.size())
        }

        return this
    }

    fun hasHighPrioritySize(size: Int): IssuesAssert {
        isNotNull

        if (actual.highPrioritySize != size) {
            failWithMessage("Expected issues's highPrioritySize to be <%s> but was <%s>", size, actual.highPrioritySize)
        }

        return this
    }

    fun hasNormalPrioritySize(size: Int): IssuesAssert {
        isNotNull

        if (actual.normalPrioritySize != size) {
            failWithMessage("Expected issues's normalPrioritySize to be <%s> but was <%s>", size, actual.normalPrioritySize)
        }

        return this
    }

    fun hasLowPrioritySize(size: Int): IssuesAssert {
        isNotNull

        if (actual.lowPrioritySize != size) {
            failWithMessage("Expected issues's lowPrioritySize to be <%s> but was <%s>", size, actual.lowPrioritySize)
        }

        return this
    }

    fun hasNumberOfFiles(number: Int) : IssuesAssert {
        isNotNull

        if(actual.numberOfFiles != number) {
            failWithMessage("Expected issues's numberOfFiles to be <%s> but was <%s>", number, actual.numberOfFiles)
        }

        return this
    }

    fun hasIssueAt(index: Int, issue: Issue) : IssuesAssert {
        isNotNull

        if(actual[index] != issue) {
            failWithMessage("Expected issues's element at <%s> to be <%s> but was <%s>", index, issue, actual[index])
        }

        return this
    }

    companion object {

        fun assertThat(actual: Issues): IssuesAssert {
            return IssuesAssert(actual)
        }
    }
}
