package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class NullSafeCollectionsTest {

    @Test
    void shouldCreateNullSafeList() {
        List<Integer> sut = new ArrayList<>();
        sut.add(42);

        assertThatCode(() -> NullSafeCollections.nullSafeList(sut)).doesNotThrowAnyException();

        sut.add(null);
        assertThatThrownBy(() -> NullSafeCollections.nullSafeList(sut)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldCreateEmptyList() {
        NullSafeList<Integer> sut = NullSafeCollections.nullSafeList();
        assertThat(sut).isEmpty();
    }

    @Test
    void shouldCreateListWithInitialCapacity() {
        NullSafeList<Integer> sut = NullSafeCollections.nullSafeList(5);
        assertThat(sut).isEmpty();
    }

    @Test
    void shouldWorkWithOtherCollections() {
        List<Integer> helper = new ArrayList<>();
        helper.add(42);

        Collection<Integer> sut = Collections.checkedCollection(
                Collections.synchronizedList(
                        NullSafeCollections.nullSafeList(helper)), Integer.class);

        assertThat(sut).contains(42);
    }
}
