package edu.hm.hafner.util;

import java.util.List;

import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

abstract class ListTest {

    @Test
    void shouldContainNoElementsAfterClear() {
        List<Integer> list = create(13);

        assertThat(list).isNotEmpty();

        list.clear();

        assertThat(list).isEmpty();
    }

    @Test
    void shouldContainElementAfterAdd() {
        List<Integer> list = create(0);

        list.add(17);

        assertThat(list).isNotEmpty(); //And
        assertThat(list).containsExactly(17);
    }

    @Test
    void shouldNotContainElementAfterRemove() {
        List<Integer> list = create(0);

        list.add(17);
        list.add(18);

        list.remove(0);

        assertThat(list).containsExactly(18);
    }

    @Test
    void shouldReturnCorrectElementWithGet() {
        List<Integer> list = create(0);

        list.add(5);
        list.add(17);
        list.add(23);

        assertThat(list.get(1)).isEqualTo(17);
    }

    protected abstract List<Integer> create(int numberOfInitialElements);
}
