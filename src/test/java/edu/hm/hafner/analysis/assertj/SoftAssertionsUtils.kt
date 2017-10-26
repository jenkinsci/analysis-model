package edu.hm.hafner.analysis.assertj

import edu.hm.hafner.analysis.Issue
import edu.hm.hafner.analysis.IssueAssert
import edu.hm.hafner.analysis.IssuesSoftAssertions

/** This function is a extension function for any class derived from SoftAssertions.
 *  The lambda should contain tests for a test subject.
 *  These tests will be executed softly.
 *
 *  @param softly A lambda that is extending any class derived from SoftAssertions.
 */
inline fun <T : SoftAssertions> T.assertSoftly(softly: T.() -> Unit) {
    softly()
    assertAll()
}

/** Creates an instance of the class that is passed as the generic parameter
 * (default constructor required) and executes the test that are given in the lambda softly.
 *
 *  @param T A class derived from SoftAssertions.
 *  @param softly A lambda that is extending any class derived from SoftAssertions.
 */
inline fun <reified T : SoftAssertions> assertSoftly(softly: T.() -> Unit) =
        with(Class.forName(T::class.java.canonicalName).newInstance() as T) {
            softly()
            assertAll()
            Unit
        }

fun IssuesSoftAssertions.assertThat(actual: Issue): IssueAssert {
    return proxy(IssueAssert::class.java, Issue::class.java, actual)
}