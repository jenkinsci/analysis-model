package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class NullSafeListTest extends ListTest {
    @Override
    List<Integer> create(final int numberOfInitialElements) {
        List<Integer> inner = new ArrayList<>(numberOfInitialElements);
        for(int index = 0; index < numberOfInitialElements; index++)
            inner.add((index + 5) % 10);
        return new NullSafeList<>(inner);
    }

    @Test
    void setTest() {
        List<Integer> sut = create(1);

        assertThat(sut.set(0, 4)).isEqualTo(5);
        assertThatThrownBy(() -> sut.set(0, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Null is not an accepted value for an element.");
    }

    @Test
    void addTest() {
        List<Integer> sut = create(1);

        assertThat(sut.add(64)).isTrue();
        assertThatThrownBy(() -> sut.add(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Null is not an accepted value for an element.");
    }

    @Test
    void addAtTest() {
        List<Integer> sut = create(15);

        sut.add(10, 7);
        assertThatThrownBy(() -> sut.add(9, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Null is not an accepted value for an element.");
    }

    @Test
    void addAllTest() {
        List<Integer> sut = create(1);
        List<Integer> nonNullList = new ArrayList<>();
        List<Integer> nullList = new ArrayList<>();

        nonNullList.add(4);
        nullList.add(1);
        nullList.add(null);

        assertThat(sut.addAll(nonNullList)).isTrue();
        assertThatThrownBy(() -> sut.addAll(nullList))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Null is not an accepted value for an element.");
    }

    @Test
    void addAllAtTest() {
        List<Integer> sut = create(6);
        List<Integer> nonNullList = new ArrayList<>();
        List<Integer> nullList = new ArrayList<>();

        nonNullList.add(4);
        nullList.add(1);
        nullList.add(null);

        assertThat(sut.addAll(4, nonNullList)).isTrue();
        assertThatThrownBy(() -> sut.addAll(4, nullList))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Null is not an accepted value for an element.");
    }
}
