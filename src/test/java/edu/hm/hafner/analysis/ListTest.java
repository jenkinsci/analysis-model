package edu.hm.hafner.analysis;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Test for Lists
 *
 * @author Viet Phuoc Ho (v.ho@hm.edu)
 */
abstract class ListTest {
    /**
     * Creates a list with a chosen number of initial elements.
     * @param numberOfInitialElements
     *                              represents the number of Elements the list is initialized
     * @return new List with numberOfInitialElements elements.
     */
    public abstract List<Integer> create(int numberOfInitialElements);

    @Test
    public void isEmpty() {
        List<Integer> list = create(0);

        assertThat(list.isEmpty());

        list.add(5);

        assertThat(list.contains(5));
        assertThat(!list.isEmpty());

        list.remove(0);

        assertThat(list.isEmpty());
    }

    @Test
    public void size(){
        assertThat(5 == create(5).size());
        assertThat(15 == create(15).size());
    }

    @Test
    public void remove(){
        List<Integer> list = create(5);
        list.add(99);

        assertThat(list.contains(99));

        list.remove(5);

        assertThat(!list.contains(99));
    }

    @Test
    public void add(){
        List<Integer> list = create(3);

        list.add(9);
        list.add(7);
        list.add(3);

        assertThat(list.contains(9));
        assertThat(list.contains(7));
        assertThat(list.contains(3));
    }

    @Test
    public void get(){
        List<Integer> list = create(3);

        list.add(9);
        list.add(7);
        list.add(3);

        assertThat(list.contains(9));
        assertThat(list.contains(7));
        assertThat(list.contains(3));

        list.set(0,72);
        list.set(1,50);
        list.set(2,47);

        assertThat(list.get(0) == 72);
        assertThat(list.get(1) == 50);
        assertThat(list.get(2) == 47);
    }

    @Test
    public void set(){
        List<Integer> list = create(5);

        list.add(9);
        list.add(7);
        list.add(3);
        list.add(5);
        list.add(6);

        assertThat(list.contains(9));
        assertThat(list.contains(7));
        assertThat(list.contains(3));
        assertThat(list.contains(5));
        assertThat(list.contains(6));

        list.set(0,72);
        list.set(1,50);
        list.set(2,47);

        assertThat(list.contains(72));
        assertThat(list.contains(50));
        assertThat(list.contains(47));
    }

    @Test
    public void contains(){
        List<Integer> list = create(3);

        list.add(6543);
        list.add(548);
        list.add(53);

        assertThat(list.contains(6543));
        assertThat(list.contains(548));
        assertThat(list.contains(53));
    }

}
