package edu.hm.hafner.analysis;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class ListTest {

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
         int want = 0;
         int have = list.size();
        assertEquals(want,have);
        list.add(0);
        list.add(3);
        want = 2;
        assertEquals(want, list.size());
        list.add(4);
        list.add(5);
        list.add(6);
        want = 5;
        assertEquals(want,list.size());
    }
    @Test
    public void testRemoveGetAdd() {
        List list = create(10);
        list.add(0,9);
        list.add(1,10);
        list.add(2,11);
        list.add(14);
        assertEquals(4,list.size());
        assertEquals(14,list.get(3));
        assertEquals(9,list.get(0));
        list.remove(3);
        list.remove(2);
        assertEquals(2,list.size());
        assertFalse(list.contains(11));
        assertTrue(list.contains(9));

    }

}
