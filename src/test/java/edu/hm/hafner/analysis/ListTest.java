package edu.hm.hafner.analysis;

import java.util.List;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Abstrakte Testklasse ListTest mit Containertyp Integer.
 */
abstract class ListTest {
    abstract List<Integer> create(int numberOfInitialElements);

    @Test
    void shouldBeEmptyWhenZeroElements() {
        List<Integer> sut = create(0);

        assertThat(sut).hasSize(0);
        assertThat(sut).isEmpty();
    }

    @Test
    void shouldContainAnElementThatsAdded() {
        List<Integer> sut = create(0);

        assertThat(sut).isEmpty();

        sut.add(42);
        assertThat(sut).isNotEmpty();
        assertThat(sut).contains(42);
    }

    @Test
    void shouldHaveSizeBiggerOneAfterAdding() {
        List<Integer> sut = create(0);

        assertThat(sut).isEmpty();
        assertThat(sut).hasSize(0);

        sut.add(101);
        assertThat(sut).hasSize(1);
    }

    @Test
    void shouldHaveSizeXafterInitialisationWithX() {
        List<Integer> sut = create(5);

        assertThat(sut).hasSize(5);
    }

    @Test
    void shouldRemoveElements() {
        List<Integer> sut = create(0);

        sut.add(42);
        assertThat(sut).contains(42);

        sut.remove(Integer.valueOf(42));
        assertThat(sut).doesNotContain(42);
        assertThat(sut).isEmpty();
    }

}