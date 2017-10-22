package edu.hm.hafner.analysis

import org.assertj.core.api.SoftAssertions

class IssuesSoftAssertions : SoftAssertions() {

    fun assertThat(actual: Issues): IssuesAssert {
        return proxy(IssuesAssert::class.java, Issues::class.java, actual)
    }
}
