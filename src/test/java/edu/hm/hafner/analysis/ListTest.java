package edu.hm.hafner.analysis;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Abstract Test Factory for List Implementations.
 *
 * @author Daniel Soukup
 *
 */
abstract class ListTest {

    /**
     * Creates the concrete List.
     * @param numberOfInitalElements
     *         The size of the list at creation
     * @return the concrete List
     */
    public abstract List<Integer> create(int numberOfInitalElements);

    @Test
    void shouldBeEmpty() {
        List<Integer> sut = create(0);

        assertThat(sut).hasSize(0);
    }

    @Test
    void shouldBeSizeOf5() {
        List<Integer> sut = create(0);

        sut.add(10);
        sut.add(10);
        sut.add(10);
        sut.add(10);
        sut.add(10);

        assertThat(sut).hasSize(5).contains(10);
    }

    @Test
    void shouldBeSizeOf8() {
        List<Integer> sut = create(8);

        assertThat(sut).hasSize(8);
    }


    @Test
    void shouldBeEmptyAfterAddingAndRemovingTen() {
        List<Integer> sut = create(0);

        sut.add(10);
        sut.remove(0);

        assertThat(sut).isEmpty();
    }

    @Test
    void shouldContain10() {
        List<Integer> sut = create(0);

        sut.add(10);
        sut.add(10);
        sut.remove(0);

        assertThat(sut).contains(10);
    }

    @Test
    void shouldBe5() {
        List<Integer> sut = create(0);

        sut.add(10);
        sut.add(5);
        sut.remove(0);

        assertThat(sut.get(0)).isEqualTo(5);
    }

}
