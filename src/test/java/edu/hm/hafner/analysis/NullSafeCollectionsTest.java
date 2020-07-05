package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class NullSafeCollectionsTest {

    void initLists(final List<Integer> list) {
        list.add(2);
        list.add(3);
        list.add(5);
        list.add(null);
        list.add(7);
        list.add(11);

    }

    @Test
    void shouldWorkWithLinkedList() {
        LinkedList<Integer> linkedList = new LinkedList<>();
        initLists(linkedList);

        Collection<Integer> checked;

        checked = Collections.checkedCollection(
                Collections.synchronizedList(
                        NullSafeCollections.nullSafeList(linkedList)), Integer.class);

        assertThat(checked.contains(null)).isFalse();
    }

    @Test
    void shouldWorkWithArrayList() {

        ArrayList<Integer> arrayList = new ArrayList<>();
        initLists(arrayList);

        Collection<Integer> checked;

        checked = Collections.checkedCollection(
                Collections.synchronizedList(
                        NullSafeCollections.nullSafeList(arrayList)), Integer.class);

        assertThat(checked.contains(null)).isFalse();
    }

    @Test
    void awesomeTest() {
        List<Integer> list = NullSafeCollections.emptyNullSafeList();
        assertThat(list)
                .isInstanceOf(NullSafeList.class)
                .hasSize(0);
    }

    @Test
    void shouldFilterOutNullElements() {
        List<Integer> list = NullSafeCollections.listOf(1, 2, 3, null);
        assertThat(list)
                .isInstanceOf(NullSafeList.class)
                .hasSize(3)
                .containsOnly(1, 2, 3);
    }

}
