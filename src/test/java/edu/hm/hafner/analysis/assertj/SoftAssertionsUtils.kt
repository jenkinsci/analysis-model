package edu.hm.hafner.analysis.assertj

import edu.hm.hafner.analysis.Issue
import edu.hm.hafner.analysis.Issues

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
@JvmName("assertSoftlyGeneric")
inline fun <reified T : SoftAssertions> assertSoftly(softly: T.() -> Unit) =
        with(Class.forName(T::class.java.canonicalName).newInstance() as T) {
            softly()
            assertAll()
            Unit
        }

/** Creates an instance of SoftAssertion and executes the tests that are in the lambda.
 *
 * @param softly test code
 */
fun assertSoftly(softly: SoftAssertions.() -> Unit) : Unit =
        with(SoftAssertions()) {
            softly()
            assertAll()
        }

/** Extending class SoftAssertions with assertThat for Issues.
 * @param actual the test subject
 */
fun SoftAssertions.assertThat(actual: Issue): IssueAssert {
    return proxy(IssueAssert::class.java, Issue::class.java, actual)
}

fun SoftAssertions.assertThat(actual: Issues): IssuesAssert {
    return proxy(IssuesAssert::class.java, Issues::class.java, actual)
}