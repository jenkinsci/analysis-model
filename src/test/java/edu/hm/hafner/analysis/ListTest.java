package edu.hm.hafner.analysis;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Abstract class for testing.
 *
 * @author S. A. D.
 */
abstract class ListTest {

    abstract List<Integer> create(int numberOfInitialElements);

    @Test
    void shouldBeEmpty() {
        List<Integer> list = create(0);
        assertThat(list).isEmpty();
    }

    @Test
    void shouldBeFilled() {
        List<Integer> list = create(7);
        assertThat(list.isEmpty()).isFalse();
    }

    @Test
    void shouldHaveSizeZero() {
        List<Integer> list = create(0);
        assertThat(list.size()).isEqualTo(0);
    }

    @Test
    void shouldHaveSizeSeven() {
        List<Integer> list = create(7);
        assertThat(list.size()).isEqualTo(7);
    }

    @Test
    void shouldRemoveValue() {
        List<Integer> list = create(7);
        int old = list.get(0);

        list.remove(0);
        assertThat(list.size()).isEqualTo(6);
    }

    @Test
    void shouldAddAndGetValue() {
        List<Integer> list = create(0);
        list.add(42);
        assertThat(list.get(list.size() - 1)).isEqualTo(42);
    }

    @Test
    void shouldContainValue() {
        List<Integer> list = create(0);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);

        assertThat(list.contains(4)).isTrue();
        assertThat(list.contains(6)).isTrue();
        assertThat(list.contains(9)).isFalse();
    }

    @Test
    void shouldAdd() {
        List<Integer> list = create(14);

        list.add(6, 8);
        assertThat(list.get(6)).isEqualTo(8);
    }

    @Test
    void shouldReturnIndexOf() {
        List<Integer> list = create(14);
        list.add(99);
        assertThat(list.indexOf(99)).isEqualTo(14);
    }

    @Test
    void shouldRemove() {
        List<Integer> list = create(0);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(20);
        list.remove(2);
        assertThat(list.size()).isEqualTo(5);
    }

    @Test
    void shouldRemoveAll() {
        List<Integer> list = create(0);
        list.add(1);
        list.add(1);
        list.add(1);
        list.add(1);
        list.add(2);
        list.add(3);
        List<Integer> otherList = create(0);
        otherList.add(1);
        otherList.add(2);
        list.removeAll(otherList);
        assertThat(list.size()).isEqualTo(1);
    }

    @Test
    void shouldCreateArray() {
        List<Integer> list = create(0);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        Object[] result = new Object[2];
        assertThat(list.toArray()).hasSameClassAs(result);

    }

    @Test
    void shouldReturnLastIndexOf() {
        List<Integer> list = create(10);
        list.add(9);
        list.add(9);
        assertThat(list.lastIndexOf(9)).isEqualTo(list.size() - 1);

    }

    @Test
    void shouldClearList() {
        List<Integer> list = create(10);
        list.clear();
        assertThat(list).isEmpty();

    }

    @Test
    void shouldSetValue() {
        List<Integer> list = create(10);
        list.set(2, 8);
        assertThat(list.get(2)).isEqualTo(8);

    }

}