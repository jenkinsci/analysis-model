package edu.hm.hafner.analysis.assertj

import org.assertj.core.api.AbstractAssert

inline fun <T : SoftAssertions> T.assertSoftly(softly: T.() -> Unit) {
    softly()
    assertAll()
}

inline fun <reified T : SoftAssertions> assertSoftly(softly: T.() -> Unit) =
        with(Class.forName(T::class.java.canonicalName).newInstance() as T) {
            softly()
            assertAll()
            Unit
        }


inline fun <reified T, reified R : AbstractAssert<R, T>> softAssertion(): Assertable<T, R> {
    return object : SoftAssertions(), Assertable<T, R> {
        override fun assertThat(actual: T): R {
            return proxy(R::class.java, T::class.java, actual)
        }
    }
}

interface Assertable<T, R : AbstractAssert<R, T>> {
    fun assertThat(actual: T): R
}