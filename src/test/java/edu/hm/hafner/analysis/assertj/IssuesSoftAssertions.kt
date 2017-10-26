package edu.hm.hafner.analysis.assertj

import edu.hm.hafner.analysis.Issues

open class IssuesSoftAssertions : SoftAssertions() {
    fun assertThat(actual: Issues) : IssuesAssert {
        return proxy(IssuesAssert::class.java, Issues::class.java, actual)
    }
}