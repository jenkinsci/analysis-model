package edu.hm.hafner.analysis;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.assertions.Assertions;

import sun.jvm.hotspot.utilities.Assert;

/**
 * abstract class for list tests, e.g. {@link ArrayListTest}.
 *
 * @author Simon Symhoven
 */

abstract class ListTest {
    abstract List<Integer> create(int numberOfInitialElements);
    private Random r = new Random();

    @Test
    @DisplayName("list should be empty")
    void isEmpty() {
        Assertions.assertThat(this.create(0)).isEmpty();
    }

    @Test
    @DisplayName("get element of list should be not null")
    void getElementShouldBeNotNull() {
        Assertions.assertThat(this.create(5).get(1)).isNotNull();
    }

    @Test
    @DisplayName("get element of list should be null (IndexOutOfBounds)")
    void getElementOutOfIndex() {
        try {
            Assertions.assertThat(this.create(5).get(6)).isNull();
        }
        catch (IndexOutOfBoundsException e) {
            System.out.println("IndexOutOfBoundException catched - Test successfull: " + e.getMessage());
            Assertions.assertThat(true).isTrue();
        }
    }

    @Test
    @DisplayName("add element should increase size of 1")
    void addElement() {
        List<Integer> list = this.create(5);

        int lastElem = list.get(list.size() - 1);
        System.out.println("Last element: " + lastElem);

        list.add(r.nextInt());
        System.out.println("add new element to list.");

        int lastElemAfterAdding = list.get(list.size() - 1);
        System.out.println("Last element after adding one: " + lastElem);

        Assertions.assertThat(list).isNotEmpty().size().isEqualTo(6);
        System.out.println("Size should be 6");
        Assertions.assertThat(lastElem == lastElemAfterAdding).isFalse();
        System.out.println("last element after adding one should be another one as before.");
    }

    @Test
    @DisplayName("remove element should decrease size of 1")
    void removeElement() {
        List<Integer> list = this.create(5);

        int lastElem = list.get(list.size() - 1);
        System.out.println("Last element: " + lastElem);

        list.remove(list.size() - 2);
        System.out.println("Remove fourth element of list.");

        int lastElemAfterRemove = list.get(list.size() - 1);
        System.out.println("Last element after removing one: " + lastElem);

        Assertions.assertThat(list).isNotEmpty().size().isEqualTo(4);
        System.out.println("Size should be 4");
        Assertions.assertThat(lastElem == lastElemAfterRemove).isTrue();
        System.out.println("Both elements should be the same");
    }
}
