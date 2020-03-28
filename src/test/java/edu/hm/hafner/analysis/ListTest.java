package edu.hm.hafner.analysis;


import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the class {@link ArrayList}.
 *
 * @author Elena Lilova
 */

public abstract class ListTest {

    /**
     * Creates a new instance of a {@link List}
     * @param numberOfInitialElements the number of initial elements
     * @return a new instance of a {@link List}
     */

    abstract List<Integer> create(int numberOfInitialElements);

    @Test
    public void testIsItEmpty() {
        List list = create(0);
        assertTrue(list.isEmpty());
        list.add(7);
        assertFalse(list.isEmpty());
        list.remove(0);
        assertTrue(list.isEmpty());
        list.add(4);
        assertFalse(list.isEmpty());
        list.remove(0);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testSize() {
        List list = create(15);
         int want = 15;
         int have = list.size();
        assertEquals(want,have);
        list.add(0);
        list.add(3);
        want = 17;
        assertEquals(want, list.size());
        list.add(4);
        list.add(5);
        list.add(6);
        want = 20;
        assertEquals(want,list.size());
        list.remove(0);
        list.remove(0);
        list.remove(0);
        list.remove(0);
        want = 16;
        assertEquals(want,list.size());
    }
    @Test
    public void testRemoveGetAddContains() {
        List list = create(10);
        list.add(0,22);
        list.add(1,23);
        list.add(2,24);
        list.add(11);
        assertEquals(14,list.size());
        assertEquals(23,list.get(1));
        assertEquals(11,list.get(13));
        assertTrue(list.contains(23));
        list.remove(1);
        list.remove(5);
        assertEquals(12,list.size());
    }

}
