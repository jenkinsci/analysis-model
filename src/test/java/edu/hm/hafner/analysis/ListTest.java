package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the class {@link ArrayList}.
 *
 * @author Elena Lilova
 */

public abstract class ListTest {

    /**
     * Creates a new instance of a {@link List}
     *
     * @param numberOfInitialElements
     *         the number of initial elements
     *
     * @return a new instance of a {@link List}
     */

    abstract List<Integer> create(int numberOfInitialElements);

    @Test
    public void testIsItEmpty() {
        List list = create(0);
        assertThat(list.isEmpty());

        //      assertTrue(list.isEmpty());
        list.add(7);
        assertThat(!list.isEmpty());
        //  assertFalse(list.isEmpty());
        list.remove(0);
        //    assertTrue(list.isEmpty());
        assertThat(list.isEmpty());
        list.add(4);
        //      assertFalse(list.isEmpty());
        assertThat(!list.isEmpty());
        list.remove(0);
        //    assertTrue(list.isEmpty());
        assertThat(list.isEmpty());
    }

    @Test
    public void testSize() {
        List list = create(15);
        int want = 15;
        int have = list.size();
        assertThat(want).isEqualTo(have);
        list.add(0);
        list.add(3);
        want = 17;
        have = list.size();
        assertThat(want).isEqualTo(have);
        list.add(4);
        list.add(5);
        list.add(6);
        want = 20;
        have = list.size();
        assertThat(want).isEqualTo(have);
        list.remove(0);
        list.remove(0);
        list.remove(0);
        list.remove(0);
        want = 16;
        have = list.size();
        assertThat(want).isEqualTo(have);
    }

    @Test
    public void testRemoveGetAddContains() {
        List list = create(10);
        list.add(0, 22);
        list.add(1, 23);
        list.add(2, 24);
        list.add(11);
        assertThat(list.size()).isEqualTo(14);
        assertThat(list.get(1)).isEqualTo(23);
        assertThat(list.get(13)).isEqualTo(11);
        assertThat(list.contains(23));
        list.remove(1);
        list.remove(5);
        assertThat(list.size()).isEqualTo(12);
        assertThat(list.contains(8));
    }
}
