package edu.hm.hafner;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Base class for unit tests testing {@link List} implementations.
 * TODO: Provide more tests
 */
public abstract class ListTest {

    /**
     * Factory method to create a new {@link List} instance.
     *
     * @param numberOfInitialElements
     *         Size of list after creation.
     *
     * @return A list of integers containing the specified amount of elements.
     */
    protected abstract List<Integer> create(int numberOfInitialElements);

    @Test
    void shouldBeEmptyWithZeroElements() {
        final List<Integer> sut = create(0);

        assertThat(sut.isEmpty()).isTrue();
        assertThat(sut.size()).isEqualTo(0);
    }

    @Test
    void shouldAddElement() {
        final List<Integer> sut = create(0);

        sut.add(1);

        assertThat(sut.isEmpty()).isFalse();
        assertThat(sut.size()).isEqualTo(1);
        assertThat(sut.contains(1)).isTrue();
        assertThat(sut.get(0)).isEqualTo(1);
    }

    @Test
    void shouldCreateWithFiveElements() {
        final List<Integer> sut = create(5);

        assertThat(sut.isEmpty()).isFalse();
        assertThat(sut.size()).isEqualTo(5);
    }

    @Test
    void shouldRemoveFirstElement() {
        final List<Integer> sut = create(6);
        final int nextElement = sut.get(1);

        sut.remove(0);

        assertThat(sut.isEmpty()).isFalse();
        assertThat(sut.size()).isEqualTo(5);
        assertThat(sut.get(0)).isEqualTo(nextElement);
    }
}
