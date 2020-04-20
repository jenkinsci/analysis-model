package edu.hm.hafner.analysis;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Abstract class for testing implementations of the list.
 */
public abstract class ListTest {

    /**
     * Abstract Factory class to run the tests for the different implementations of list.
     *
     * @param numberOfElements starting size of the list.
     * @return instance of a list.
     */
    public abstract List<Integer> create(int numberOfElements);

    @Test
    void shouldBeEmpty() {
        List<Integer> sut = create(0);
        assertThat(sut).isEmpty();
    }

    @Test
    void addShouldWork() {
        List<Integer> sut = create(0);

        assertThat(sut).doesNotContain(1000);
        sut.add(1000);
        assertThat(sut).size().isEqualTo(1);
        assertThat(sut).contains(1000);
    }

    @Test
    void removeShouldWork() {
        List<Integer> sut = create(5);

        assertThat(sut).contains(4);
        sut.remove(4);
        assertThat(sut).size().isEqualTo(4);
        assertThat(sut).doesNotContain(4);
    }
}
