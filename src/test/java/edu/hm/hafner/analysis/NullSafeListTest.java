package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link NullSafeList}.
 *
 * @author Elena Lilova
 */

 class NullSafeListTest {

    @Test
    public void testShouldThrowNullPointerException() {
        // arange
        List<Integer> list = new NullSafeList<>(new ArrayList<>());
        // act, assert
        Assertions.assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> list.add(null));
    }

    @Test
    public void testShouldThrowNullPointerExceptionAddOnIndex() {
        // arange
        List<Integer> list = new NullSafeList<>(new ArrayList<>());
        // act
        list.add(4);
        list.add(3);
        // act, assert
        Assertions.assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> list.add(0, null));
    }

    @Test
    public void testShouldThrowNullPointerExceptionAddCollectionOnIndex() {
        // arange
        List<Integer> list = new NullSafeList<>(new ArrayList<>());
        List<Integer> collection = new ArrayList<>();
        //act
        list.add(1);
        list.add(2);
        collection.add(4);
        collection.add(3);
        collection.add(null);
        // act, assert
        Assertions.assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> list.addAll(1, collection));

    }

    @Test
    public void testShouldThrowNullPointerExceptionAddCollection() {
        // arange
        List<Integer> list = new NullSafeList<>(new ArrayList<>());
        List<Integer> collection = new ArrayList<>();
        // act
        list.add(1);
        list.add(2);
        collection.add(4);
        collection.add(3);
        collection.add(null);
        // act, assert
        Assertions.assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> list.addAll(collection));
    }

    @Test
    public void testSize() {
        // arange
        List<Integer> list = new NullSafeList<>(new ArrayList<>());
        int want = 0;
        // act
        int have = list.size();
        assertThat(want).isEqualTo(have);
        // act
        list.add(3);
        want = 1;
        have = list.size();
        // assert
        assertThat(want).isEqualTo(have);
        // act
        list.add(4);
        list.add(5);
        list.add(6);
        want = 4;
        have = list.size();
        // assert
        assertThat(want).isEqualTo(have);
        // act
        list.remove(0);
        list.remove(0);
        list.remove(0);
        list.remove(0);
        want = 0;
        have = list.size();
        // assert
        assertThat(want).isEqualTo(have);
    }

    @Test
    public void testRemoveGetAddContains() {
        // arange
        List<Integer> list = new NullSafeList<>(new ArrayList<>());
        // act
        list.add(0, 22);
        list.add(1, 23);
        list.add(2, 24);
        list.add(11);
        // assert
        assertThat(list.size()).isEqualTo(4);
        assertThat(list.get(1)).isEqualTo(23);
        assertThat(list.get(3)).isEqualTo(11);
        assertThat(list.contains(23));
        // act
        list.remove(1);
        // assert
        assertThat(list.size()).isEqualTo(3);
        assertThat(!list.contains(23));
    }

    @Test
    public void testAddCollectionOnIndex() {
        // arange
        List<Integer> list = new NullSafeList<>(new ArrayList<>());
        List<Integer> collection = new ArrayList<>();
        // act
        list.add(22);
        list.add(23);
        list.add(24);
        list.add(25);
        collection.add(99);
        collection.add(98);
        collection.add(97);
        collection.add(96);
        list.addAll(1, collection);
        // assert
        assertThat(list.size()).isEqualTo(8);
        assertThat(list.get(0)).isEqualTo(22);
        assertThat(list.get(1)).isEqualTo(99);
        assertThat(list.get(2)).isEqualTo(98);
        assertThat(list.get(3)).isEqualTo(97);
        assertThat(list.get(4)).isEqualTo(96);
        assertThat(list.get(5)).isEqualTo(23);
        assertThat(list.get(6)).isEqualTo(24);
        assertThat(list.get(7)).isEqualTo(25);
    }

    @Test
    public void testAddCollection() {
        // arange
        List<Integer> list = new NullSafeList<>(new ArrayList<>());
        List<Integer> collection = new ArrayList<>();
        // act
        list.add(22);
        list.add(23);
        list.add(24);
        list.add(25);
        collection.add(99);
        collection.add(98);
        collection.add(97);
        collection.add(96);
        list.addAll(collection);
        // assert
        assertThat(list.size()).isEqualTo(8);
        assertThat(list.get(0)).isEqualTo(22);
        assertThat(list.get(1)).isEqualTo(23);
        assertThat(list.get(2)).isEqualTo(24);
        assertThat(list.get(3)).isEqualTo(25);
        assertThat(list.get(4)).isEqualTo(99);
        assertThat(list.get(5)).isEqualTo(98);
        assertThat(list.get(6)).isEqualTo(97);
        assertThat(list.get(7)).isEqualTo(96);
    }

    @Test
    public void testNullSafeCollectionList() {
        // arange
        List<Integer> list = new ArrayList<>();
        //act
        list.add(4);
        list.add(3);
        Collection collection = Collections.checkedCollection(
                Collections.synchronizedList(NullSafeCollection.nullSafeList(list)), Integer.class);

        // act, assert
        Assertions.assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> collection.addAll(null));

    }
    @Test
    public void testNullSafeCollectionInitialCapacity() {
        // arange
        List<Integer> list = new ArrayList<>();
        //act
        list.add(4);
        list.add(3);
        Collection collection = Collections.checkedCollection(
                Collections.synchronizedList(NullSafeCollection.nullSafeList(10)), Integer.class);

        // act, assert
        Assertions.assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> collection.addAll(null));

    }
    @Test
    public void testNullSafeCollectionEmptyList() {
        // arange
        List<Integer> list = new ArrayList<>();
        //act
        list.add(4);
        list.add(3);
        Collection collection = Collections.checkedCollection(
                Collections.synchronizedList(NullSafeCollection.nullSafeList()), Integer.class);

        // act, assert
        Assertions.assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> collection.addAll(null));

    }
}

