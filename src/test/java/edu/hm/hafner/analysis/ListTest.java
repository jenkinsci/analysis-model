package edu.hm.hafner.analysis;

import java.util.List;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Abstrakte Testklasse ListTest mit Containertyp Integer.
 */
public abstract class ListTest {
    abstract List<Integer> create(int numberOfInitialElements);

    @Test
    void shouldBeEmptyWhenZeroElements() {
        List<Integer> sut = create(0);

        assertThat(sut.size()).isZero();
        assertThat(sut.isEmpty()).isTrue();
    }

    @Test
    void shouldContainAnElementThatsAdded() {
        List<Integer> sut = create(0);

        assertThat(sut.isEmpty()).isTrue();

        sut.add(42);
        assertThat(sut.isEmpty()).isFalse();
        assertThat(sut.contains(42)).isTrue();
    }

    @Test
    void shouldHaveSizeBiggerOneAfterAdding() {
        List<Integer> sut = create(0);

        assertThat(sut.isEmpty()).isTrue();
        assertThat(sut.size()).isZero();

        sut.add(101);
        assertThat(sut.size()).isGreaterThan(0);
        assertThat(sut.size()).isEqualTo(1);
    }

    @Test
    void shouldHaveSizeXafterInitialisationWithX() {
        List<Integer> sut = create(5);

        assertThat(sut.size()).isEqualTo(5);
    }

    @Test
    void shouldRemoveElements() {
        List<Integer> sut = create(0);

        sut.add(42);
        assertThat(sut.contains(42)).isTrue();

        sut.remove(Integer.valueOf(42));
        assertThat(sut.contains(43)).isFalse();
        assertThat(sut.isEmpty()).isTrue();
    }

}
