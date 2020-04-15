package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Base class for unit tests testing {@link List} implementations. TODO: Provide more tests
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
    void shouldAddElementOnIndex0() {
        final List<Integer> sut = create(0);

        sut.add(0, 10);

        assertThat(sut.isEmpty()).isFalse();
        assertThat(sut.size()).isEqualTo(1);
        assertThat(sut.contains(10)).isTrue();
        assertThat(sut.get(0)).isEqualTo(10);
    }

    @Test
    void shouldAddElementOnIndex5() {
        final List<Integer> sut = create(5);

        sut.add(5, 10);

        assertThat(sut.isEmpty()).isFalse();
        assertThat(sut.size()).isEqualTo(6);
        assertThat(sut.contains(10)).isTrue();
        assertThat(sut.get(5)).isEqualTo(10);
    }

    @Test
    void shouldAddAll() {
        final List<Integer> sut = create(0);
        final List<Integer> all = new ArrayList<>();
        all.add(3);
        all.add(1);
        all.add(5);

        sut.addAll(all);

        assertThat(sut).containsExactly(3, 1, 5);
    }

    @Test
    void shouldAddAllAtIndex1() {
        final List<Integer> sut = create(3);
        final List<Integer> all = new ArrayList<>();
        all.add(5);
        all.add(7);
        all.add(10);

        sut.addAll(1, all);

        assertThat(sut).containsExactly(0, 5, 7, 10, 1, 2);
    }

    @Test
    void shouldSet() {
        final List<Integer> sut = create(3);

        sut.set(1, 10);

        assertThat(sut).containsExactly(0, 10, 2);
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