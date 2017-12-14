package edu.hm.hafner.util;


import org.junit.Test;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import static org.assertj.core.api.Assertions.*;

/**
 * Verifies that objects of any Java class comply with the contract in {@link Object#equals(Object)}.
 *
 * @author Ullrich Hafner
 */
public abstract class AbstractEqualsTest {
    /**
     * Creates the subject under test.
     *
     * @return the SUT
     */
    protected abstract Object createSut();

    /**
     * Verifies that for any non-null reference value {@code x}, {@code x.equals(null)} should return {@code false}.
     */
    @Test
    @SuppressWarnings({"ObjectEqualsNull", "PMD.EqualsNull"})
    @SuppressFBWarnings("EC")
    public void shouldReturnFalseOnEqualsNull() {
        assertThat(createSut().equals(null)).isFalse();
    }

    /**
     * Verifies that equals is <i>reflexive</i>: for any non-null reference value {@code x}, {@code x.equals(x)} should
     * return {@code true}.
     */
    @Test @SuppressWarnings("EqualsWithItself")
    public void shouldReturnTrueOnEqualsThis() {
        Object sut = createSut();

        assertThat(sut.equals(sut)).isTrue();
    }
}
