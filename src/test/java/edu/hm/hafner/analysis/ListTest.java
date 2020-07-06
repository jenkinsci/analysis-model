package edu.hm.hafner.analysis;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Abstract Tests for the implementation of the interface {@link List}.
 * @author Johannes Jäger
 */
abstract class ListTest {

    abstract List<Integer> create(int numberOfInitialElements);

    // General list test START
    @Test
    void testEmpty() {
        final List<Integer> list = create(0);
        assertThat(list.isEmpty()).isTrue();
    }

    @Test
    void testNotEmpty() {
        final List<Integer> list = create(1);
        assertThat(list.isEmpty()).isFalse();
    }

    @Test
    void testSize() {
        final List<Integer> list = create(10);
        assertThat(list.size()).isEqualTo(10);
    }

    @Test
    void testAdd() {
        final List<Integer> list = create(0);
        assertThat(list.size()).isEqualTo(0);
        list.add(2);
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0)).isEqualTo(2);
    }

    @Test
    void testRemove() {
        final List<Integer> list = create(0);
        list.add(2);
        assertThat(list.get(0)).isEqualTo(2);
        assertThat(list.remove(0)).isEqualTo(2);
        assertThat(list.size()).isEqualTo(0);
        assertThat(list.isEmpty()).isTrue();
    }

    @Test
    void testContainsTrue() {
        final List<Integer> list = create(0);
        list.add(100);
        assertThat(list.contains(100)).isTrue();
    }

    @Test
    void testContainsFalse() {
        final List<Integer> list = create(0);
        list.add(100);
        assertThat(list.contains(0)).isFalse();
    }

    // General list test END

    // Null safe tests

    @Test
    void testAddNonNull() {
        final List<Integer> list = create(0);
        list.add(1);
        assertThat(list).containsExactly(1);
    }

    @Test
    void testAddNull() {
        final List<Integer> list = create(0);
        assertThatThrownBy(() -> list.add(null)).isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void testAddAllNonNull() {
        final List<Integer> list = create(0);
        final List<Integer> otherList = Arrays.asList(1, 2, 3, 4);
        list.addAll(otherList);
        assertThat(list).containsExactly(1, 2, 3, 4);
    }

    @Test
    void testAddAllNull() {
        final List<Integer> list = create(0);
        final List<Integer> otherList = Arrays.asList(1, 2, 3, null);
        assertThatThrownBy(() -> list.addAll(otherList)).isExactlyInstanceOf(NullPointerException.class);
    }
    @Test
    void testAddAllIndexNonNull() {
        final List<Integer> list = create(0);
        list.add(0);
        list.add(1);
        list.add(2);
        final List<Integer> otherList = Arrays.asList(3, 4, 5, 6);
        list.addAll(3, otherList);
        assertThat(list).containsExactly(0, 1, 2, 3, 4, 5, 6);
    }

    @Test
    void testAddAllIndexNull() {
        final List<Integer> list = create(5);
        final List<Integer> otherList = Arrays.asList(1, 2, 3, null);
        assertThatThrownBy(() -> list.addAll(2, otherList)).isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void testReplaceAllIndexNonNull() {
        final List<Integer> list = create(0);
        list.add(0);
        list.add(1);
        list.add(2);
        list.replaceAll(element -> element+5);
        assertThat(list).containsExactly(5, 6, 7);
    }

    @Test
    void testReplaceAllIndexNull() {
        final List<Integer> list = create(0);
        list.add(0);
        list.add(1);
        list.add(2);
        assertThatThrownBy(() -> list.replaceAll(element -> null)).isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void testSetNonNull() {
        final List<Integer> list = create(0);
        list.add(1);
        list.add(2);
        list.add(3);
        list.set(2, 4);
        assertThat(list).containsExactly(1, 2, 4);
    }

    @Test
    void testSetNull() {
        final List<Integer> list = create(0);
        list.add(1);
        list.add(2);
        list.add(3);
        assertThatThrownBy(() -> list.set(2, null)).isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void testAddIndexNonNull() {
        final List<Integer> list = create(0);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(2, 4);
        assertThat(list).containsExactly(1, 2, 4, 3);
    }

    @Test
    void testAddIndexNull() {
        final List<Integer> list = create(0);
        list.add(1);
        list.add(2);
        list.add(3);
        assertThatThrownBy(() -> list.add(2, null)).isExactlyInstanceOf(NullPointerException.class);
    }

}
