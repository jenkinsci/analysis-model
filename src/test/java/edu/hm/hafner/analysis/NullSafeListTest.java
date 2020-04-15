package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests for the {@link NullSafeList}.
 * <p>
 * Uses default list tests from {@link ListTest} and additional tests for adding null values.
 */
class NullSafeListTest extends ListTest {
    @Override
    protected List<Integer> create(final int numberOfInitialElements) {
        final List<Integer> list = new ArrayList<>();
        for (int i = 0; i < numberOfInitialElements; i++) {
            list.add(i);
        }
        return new NullSafeList<>(list);
    }

    @Test
    void shouldCreateWithInitialList() {
        final List<Integer> existing = new ArrayList<>();
        existing.add(3);
        existing.add(1);
        existing.add(5);

        final List<Integer> sut = new NullSafeList<>(existing);
        assertThat(sut).containsExactly(3, 1, 5);
    }

    @Test
    void throwsCreatingWithListContainingNull() {
        final List<Integer> existing = new ArrayList<>();
        existing.add(3);
        existing.add(1);
        existing.add(5);
        existing.add(null);

        assertThatThrownBy(() -> new NullSafeList<>(existing)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void throwsAddingNull() {
        final List<Integer> sut = create(0);

        assertThatThrownBy(() -> sut.add(null)).isInstanceOf(NullPointerException.class);
        assertThat(sut).isEmpty();
    }

    @Test
    void throwsAddingNullOnIndex0() {
        final List<Integer> sut = create(1);

        assertThatThrownBy(() -> sut.add(0, null)).isInstanceOf(NullPointerException.class);

        assertThat(sut).containsExactly(0);
    }

    @Test
    void throwsAddingCollectionContainingNull() {
        final List<Integer> sut = create(0);
        final List<Integer> all = new ArrayList<>();
        all.add(3);
        all.add(1);
        all.add(5);
        all.add(null);

        assertThatThrownBy(() -> sut.addAll(all)).isInstanceOf(NullPointerException.class);

        assertThat(sut).isEmpty();
    }

    @Test
    void throwsAddingCollectionContainingNullAtIndex1() {
        final List<Integer> sut = create(3);
        final List<Integer> all = new ArrayList<>();
        all.add(3);
        all.add(null);
        all.add(1);

        assertThatThrownBy(() -> sut.addAll(1, all)).isInstanceOf(NullPointerException.class);

        assertThat(sut).containsExactly(0, 1, 2);
    }

    @Test
    void throwsSettingNull() {
        final List<Integer> sut = create(3);

        assertThatThrownBy(() -> sut.set(1, null)).isInstanceOf(NullPointerException.class);

        assertThat(sut).containsExactly(0, 1, 2);
    }
}
