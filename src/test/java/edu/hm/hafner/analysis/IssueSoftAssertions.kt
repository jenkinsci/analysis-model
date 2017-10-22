package edu.hm.hafner.analysis

import edu.hm.hafner.analysis.assertj.SoftAssertions
import org.assertj.core.api.AbstractAssert

class IssueSoftAssertions : SoftAssertions() {
    fun assertThat(actual: Issue): IssueAssert {
        return proxy(IssueAssert::class.java, Issue::class.java, actual)
    }
}

/*
inline fun assertSoftly(softly: IssueSoftAssertions.() -> Unit) = with(IssueSoftAssertions()) {
    softly()
    assertAll()
    Unit
}*/

