package edu.hm.hafner.analysis

import java.util.Objects
import java.util.UUID

import org.assertj.core.api.AbstractAssert

open class IssueAssert(actual: Issue) : AbstractAssert<IssueAssert, Issue>(actual, IssueAssert::class.java) {

    fun hasFileName(fileName: String): IssueAssert {
        isNotNull

        if (actual.fileName != fileName) {
            failWithMessage("Expected issue's fileName to be <%s> but was <%s>", fileName, actual.fileName)
        }

        return this
    }

    fun hasCategory(category: String): IssueAssert {
        isNotNull

        if (actual.category != category) {
            failWithMessage("Expected issue's category to be <%s> but was <%s>", category, actual.category)
        }

        return this
    }

    fun hasType(type: String): IssueAssert {
        isNotNull

        if (actual.type != type) {
            failWithMessage("Expected issue's type to be <%s> but was <%s>", type, actual.type)
        }

        return this
    }

    fun hasPriority(priority: Priority): IssueAssert {
        isNotNull

        if (actual.priority != priority) {
            failWithMessage("Expected issue's priority to be <%s> but was <%s>", priority, actual.priority)
        }

        return this
    }

    fun hasMessage(message: String): IssueAssert {
        isNotNull

        if (actual.message != message) {
            failWithMessage("Expected issue's message to be <%s> but was <%s>", message, actual.message)
        }

        return this
    }

    fun hasDescription(description: String): IssueAssert {
        isNotNull

        if (actual.description != description) {
            failWithMessage("Expected issue's description to be <%s> but was <%s>", description, actual.description)
        }

        return this
    }

    fun hasPackageName(packageName: String): IssueAssert {
        isNotNull

        if (actual.packageName != packageName) {
            failWithMessage("Expected issue's packageName to be <%s> but was <%s>", packageName, actual.packageName)
        }

        return this
    }

    fun hasLineStart(lineStart: Int): IssueAssert {
        isNotNull

        if (actual.lineStart != lineStart) {
            failWithMessage("Expected issue's lineStart to be <%s> but was <%s>", lineStart, actual.lineStart)
        }

        return this
    }

    fun hasLineEnd(lineEnd: Int): IssueAssert {
        isNotNull

        if (actual.lineEnd != lineEnd) {
            failWithMessage("Expected issue's lineEnd to be <%s> but was <%s>", lineEnd, actual.lineEnd)
        }

        return this
    }

    fun hasColumnStart(columnStart: Int): IssueAssert {
        isNotNull

        if (actual.columnStart != columnStart) {
            failWithMessage("Expected issue's columnStart to be <%s> but was <%s>", columnStart, actual.columnStart)
        }

        return this
    }

    fun hasColumnEnd(columnEnd: Int): IssueAssert {
        isNotNull

        if (actual.columnEnd != columnEnd) {
            failWithMessage("Expected issue's columnEnd to be <%s> but was <%s>", columnEnd, actual.columnEnd)
        }

        return this
    }

    fun hasId(uuid: UUID): IssueAssert {
        isNotNull

        if (actual.id != uuid) {
            failWithMessage("Expected issue's id to be <%s> but was <%s>", uuid, actual.id)
        }

        return this
    }

    fun hasFingerPrint(fingerPrint: String): IssueAssert {
        isNotNull

        if (actual.fingerprint != fingerPrint) {
            failWithMessage("Expected issue's fingerPrint to be <%s> but was <%s>", fingerPrint, actual.fingerprint)
        }

        return this
    }

    companion object {

        fun assertThat(actual: Issue): IssueAssert {
            return IssueAssert(actual)
        }
    }
}