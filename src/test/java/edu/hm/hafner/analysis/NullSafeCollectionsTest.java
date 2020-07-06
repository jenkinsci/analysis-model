package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Test class for {@link NullSafeCollections}.
 * extends {@link NullSafeListTest}
 * @author Tobias Karius
 */
class NullSafeCollectionsTest extends NullSafeListTest {

    @Override
    List<Integer> create(final int numberOfInitialElements) {
        List<Integer> list = NullSafeCollections.nullSafeList(numberOfInitialElements);
        Stream.iterate(0, i -> i + 1)
                .limit(numberOfInitialElements)
                .forEach(list::add);
        return list;
    }

    @Test
    public void combinatedArrayList() {
        final List<Integer> list = new ArrayList<>();
        Collections.checkedCollection(
                Collections.synchronizedList(
                        NullSafeCollections.nullSafeList(list)), Integer.class);
    }

    @Test
    public void listWithoutArguments() {
        List<Integer> list = NullSafeCollections.nullSafeList();
        assertThat(list.isEmpty()).isTrue();
    }

    @Test
    public void listWithArgumentsNotNull() {
        List<Integer> arrayList = new ArrayList<>();
        arrayList.add(2);
        List<Integer> list = NullSafeCollections.nullSafeList(arrayList);
        assertThat(list.isEmpty()).isFalse();
        assertThat(list).containsExactly(2);
    }

    @Test
    public void listWithNull() {
        List<String> arrayList = new ArrayList<>();
        arrayList.add(null);
        assertThatThrownBy(() -> NullSafeCollections.nullSafeList(arrayList)).isInstanceOf(NullPointerException.class);
    }

}