package edu.hm.hafner.util;


import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Verifies that comparable objects comply with the contract in {@link Comparable#compareTo(Object)}.
 *
 * @author Ullrich Hafner
 * @param <T> the type of the subject under test
 */
public abstract class AbstractComparableTest<T extends Comparable<T>> {
    /**
     * Verifies that a negative integer, zero, or a positive integer as this object is less than, equal to, or greater
     * than the specified object.
     */
    @SuppressWarnings("EqualsWithItself")
    @Test
    public void shouldBeNegativeIfThisIsSmaller() {
        T smaller = createSmallerSut();
        T greater = createGreaterSut();

        assertThat(smaller.compareTo(greater)).isNegative();
        assertThat(greater.compareTo(smaller)).isPositive();

        assertThat(smaller.compareTo(smaller)).isZero();
        assertThat(greater.compareTo(greater)).isZero();
    }

    /**
     * Verifies that {@code sgn(x.compareTo(y)) == -sgn(y.compareTo(x))} for all {@code x} and {@code y}.
     */
    @Test
    public void shouldBeSymmetric() {
        T left = createSmallerSut();
        T right = createGreaterSut();

        int leftCompareToResult = left.compareTo(right);
        int rightCompareToResult = right.compareTo(left);

        assertThat(Integer.signum(leftCompareToResult)).isEqualTo(-Integer.signum(rightCompareToResult));
    }

    /**
     * Creates a subject under test. The SUT must be smaller than the SUT of the opposite method {@link
     * #createGreaterSut()}.
     *
     * @return the SUT
     */
    protected abstract T createSmallerSut();

    /**
     * Creates a subject under test. The SUT must be greater than the SUT of the opposite method {@link
     * #createSmallerSut()}.
     *
     * @return the SUT
     */
    protected abstract T createGreaterSut();
}
