package edu.hm.hafner.analysis;

import java.text.CollationElementIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class NullSafeCollectionTest {

    private final NullSafeCollection nullSafeCollection = new NullSafeCollection();

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
                        NullSafeCollection.nullSafeList(linkedList)), Integer.class);

        assertThat(checked.contains(null)).isFalse();
    }

    @Test
    void shouldWorkWithArrayList() {

        ArrayList<Integer> arrayList = new ArrayList<>();
        initLists(arrayList);

        Collection<Integer> checked;

        checked = Collections.checkedCollection(
                Collections.synchronizedList(
                        NullSafeCollection.nullSafeList(arrayList)), Integer.class);

        assertThat(checked.contains(null)).isFalse();
    }

    @Test
    void aweseomeTest() {
        List<Integer> list = NullSafeCollection.nullSafeList();
        assertThat(list).isInstanceOf(NullSafeList.class);
    }

}
