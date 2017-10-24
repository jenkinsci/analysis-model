package edu.hm.hafner.analysis

import edu.hm.hafner.analysis.assertj.SoftAssertions

class IssuesSoftAssertions : SoftAssertions() {
    fun assertThat(actual: Issues): IssuesAssert {
        return proxy(IssuesAssert::class.java, Issues::class.java, actual)
    }
}
