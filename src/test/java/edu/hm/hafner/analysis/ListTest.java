package edu.hm.hafner.analysis;

import java.util.List;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Abstract Tests for the implementation of the interface {@link List}.
 * @author Johannes Jäger 
 */
abstract class ListTest {

    abstract List<Integer> create(int numberOfInitialElements);

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

}
