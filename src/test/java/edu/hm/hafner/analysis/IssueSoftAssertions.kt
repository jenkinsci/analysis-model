package edu.hm.hafner.analysis

import edu.hm.hafner.analysis.assertj.SoftAssertions

open class IssueSoftAssertions : SoftAssertions() {
    fun assertThat(actual: Issue): IssueAssert {
        return proxy(IssueAssert::class.java, Issue::class.java, actual)
    }
}
