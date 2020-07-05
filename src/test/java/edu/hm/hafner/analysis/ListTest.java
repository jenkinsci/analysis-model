package edu.hm.hafner.analysis;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

abstract class ListTest {

    abstract List<Integer> create(int numberOfInitialElements);

    @Test
    public void listIsEmpty() {
        assertThat(create(0).isEmpty());
    }

    @Test
    public void listIsNotEmpty() {
        assertThat(create(1).isEmpty()).isFalse();
    }

    @Test
    public void listSize0() {
        assertThat(create(0).size()).isEqualTo(0);
    }

    @Test
    public void listSize1() {
        assertThat(create(1).size()).isEqualTo(1);
    }

    @Test
    public void listRemove() {
        List<Integer> list = create(1);
        list.remove(0);
        assertThat(list.size()).isEqualTo(0);
    }

    @Test
    public void listDoubleRemove() {
        List<Integer> list = create(3);
        list.remove(0);
        list.remove(0);
        assertThat(list.size()).isEqualTo(1);
    }


}
