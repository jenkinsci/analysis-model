package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests for the {@link NullSafeCollections} utility class.
 */
class NullSafeCollectionsTest {
    @Test
    void shouldCreateEmptyNullSafeList() {
        final List<Integer> sut = NullSafeCollections.nullSafeList();

        assertThat(sut).isEmpty();
        assertThat(sut).isInstanceOf(NullSafeList.class);
    }

    @Test
    void shouldCreateNullSafeListFromExistingList() {
        final List<Integer> existingList = new ArrayList<>();
        existingList.add(3);
        existingList.add(1);
        existingList.add(5);

        List<Integer> sut = NullSafeCollections.nullSafeList(existingList);
        assertThat(sut).containsExactly(3, 1, 5);
        assertThat(sut).isInstanceOf(NullSafeList.class);
    }

    @Test
    void shouldCreateNullSafeListWithInitialCapacity() {
        List<Integer> sut = NullSafeCollections.nullSafeList(20);
        assertThat(sut).isEmpty();
        assertThat(sut).isInstanceOf(NullSafeList.class);
    }

    @Test
    void throwsCreatingNullSafeListWithExistingListContainingNull() {
        final List<Integer> existingList = new ArrayList<>();
        existingList.add(3);
        existingList.add(1);
        existingList.add(5);
        existingList.add(null);

        assertThatThrownBy(() -> NullSafeCollections.nullSafeList(existingList))
                .isInstanceOf(NullPointerException.class);
    }
}
