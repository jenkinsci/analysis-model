package edu.hm.hafner.analysis;

import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Abstract Tests for the implementation of the interface {@link List}.
 */
abstract class ListTest {

    abstract List<Integer> create(int numberOfInitialElements);

    @Test
    void testEmpty() {
        final List<Integer> list = create(0);
        assertTrue(list.isEmpty());
    }

    @Test
    void testNotEmpty() {
        final List<Integer> list = create(1);
        assertFalse(list.isEmpty());
    }

    @Test
    void testSize() {
        final List<Integer> list = create(10);
        assertEquals(list.size(), 10);
    }

    @Test
    void testAdd() {
        final List<Integer> list = create(0);
        assertEquals(list.size(), 0);
        list.add(2);
        assertEquals(list.size(), 1);
        assertEquals(list.get(0), 2);
    }

    @Test
    void testRemove() {
        final List<Integer> list = create(0);
        list.add(2);
        assertEquals(list.get(0), 2);
        assertEquals(list.remove(0), 2);
        assertEquals(list.size(), 0);
        assertTrue(list.isEmpty());
    }
    
    @Test
    void testContainsTrue() {
        final List<Integer> list = create(0);
        list.add(100);
        assertTrue(list.contains(100));
    }
    
    @Test
    void testContainsFalse() {
        final List<Integer> list = create(0);
        list.add(100);
        assertFalse(list.contains(0));
    }

}
