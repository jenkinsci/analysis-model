package edu.hm.hafner.analysis;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import edu.hm.hafner.analysis.assertions.Assertions;

/**
 * abstract class for list tests, e.g. {@link ArrayListTest}.
 *
 * @author Simon Symhoven
 */

abstract class ListTest {
    abstract List<Integer> create(int numberOfInitialElements);

    @Test
    @DisplayName("list should be empty")
    void isEmpty() {
        Assertions.assertThat(this.create(0)).isEmpty();
    }

    @Test
    @DisplayName("get random element between 0-4 should be not null")
    void getElementShouldBeNotNull() {
        Assertions.assertThat(this.create(5).get((int)(Math.random() * 4))).isNotNull();
    }

    @Test
    @DisplayName("get element of list should be null (IndexOutOfBounds)")
    void getElementOutOfIndex() {
        Assertions.assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> this.create(5).get(6));
    }

    @Test
    @DisplayName("add element should increase size of 1")
    void addElement() {
        List<Integer> list = this.create(5);
        list.add((int) Math.round(Math.random()));
        Assertions.assertThat(list).isNotEmpty().hasSize(6);
    }

    @Test
    @DisplayName("remove element should decrease size of 1")
    void removeElement() {
        List<Integer> list = this.create(5);
        int lastElem = list.get(list.size() - 1);
        list.remove(list.size() - 2);
        int lastElemAfterRemove = list.get(list.size() - 1);
        Assertions.assertThat(list).isNotEmpty().hasSize(4);
        Assertions.assertThat(lastElem == lastElemAfterRemove).isTrue();
    }
}
