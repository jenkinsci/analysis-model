package edu.hm.hafner.util;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link ArrayList}. Demonstrates some features of
 * <a href="https://joel-costigliola.github.io/assertj/">AssertJ</a>.
 *
 * @author Ullrich Hafner
 */
class ArrayListAssertJTest {
    /** Verifies that a new list is empty. */
    @Test
    void shouldBeEmptyWhenCreatedShortForm() {
        List<String> strings = new ArrayList<>();

        assertThat(strings).isEmpty();
        assertThat(strings).isEmpty();
    }

    /** Verifies that a new list is empty. */
    @Test
    void shouldBeEmptyWhenCreatedShortFormImplicitSoftly() {
        List<String> strings = new ArrayList<>();

        assertSoftly(softly -> {
            softly.assertThat(strings).isEmpty();
            softly.assertThat(strings).isEmpty();
        });
    }

    /** Verifies that a new list is empty. */
    @Test
    void shouldBeEmptyWhenCreatedShortFormManualSoftly() {
        List<String> strings = new ArrayList<>();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(strings).isEmpty();
        softly.assertThat(strings).isEmpty();
        softly.assertAll();
    }

    /** Verifies that an element can be added and retrieved. */
    @Test
    void shouldReturnStoredElement() {
        List<String> strings = new ArrayList<>();
        String element = "Hello World!";
        strings.add(element);

        assertThat(strings).hasSize(1);
        assertThat(strings).isNotEmpty();
        assertThat(strings.get(0)).isEqualTo(element).isSameAs(element);
    }

    /** Verifies that an {@link IndexOutOfBoundsException} is thrown if the requested index is not in the list. */
    @Test @SuppressWarnings({"ResultOfMethodCallIgnored", "MismatchedQueryAndUpdateOfCollection"})
    void shouldThrowExceptionIfIndexIsTooLarge() {
        List<String> strings = new ArrayList<>();
        assertThatThrownBy(() -> strings.get(0))
                .isInstanceOf(IndexOutOfBoundsException.class)
                .hasMessageContaining("0")
                .hasNoCause();
    }
}
