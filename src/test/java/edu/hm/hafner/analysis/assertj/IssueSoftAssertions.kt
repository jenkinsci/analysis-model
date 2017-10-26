package edu.hm.hafner.analysis.assertj

import edu.hm.hafner.analysis.Issue

open class IssueSoftAssertions : SoftAssertions() {
    fun assertThat(actual: Issue): IssueAssert {
        return proxy(IssueAssert::class.java, Issue::class.java, actual)
    }
}
