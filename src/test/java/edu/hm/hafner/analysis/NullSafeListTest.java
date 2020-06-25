package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class NullSafeListTest extends ListTest {
    @Override
    List<Integer> create(final int numberOfInitialElements) {
        List<Integer> sut = new NullSafeList<>();
        IntStream.range(0, numberOfInitialElements)
                .forEach(sut::add);
        return sut;
    }

    @Test
    void shouldBombAddNullElement() {
        //arrange
        List<Integer> sut = create(0);
        //assert
        assertThatThrownBy(() -> sut.add(null)).isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldBombAddNullElementAtIndex() {
        //arrange
        List<Integer> sut = create(0);
        //assert
        assertThatThrownBy(() -> sut.add(0, null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldBombAddAllNull() {
        //arrange
        List<Integer> sut = create(0);
        List<Integer> sut2 = new ArrayList<>();
        sut2.add(null);
        //assert
        assertThatThrownBy(() -> sut.addAll(sut2)).isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldBombAddAllNullAtIndex() {
        //arrange
        List<Integer> sut = create(0);
        List<Integer> sut2 = new ArrayList<>();
        sut2.add(null);
        //assert
        assertThatThrownBy(() -> sut.addAll(0, sut2)).isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldBombSetNull() {
        //arrange
        List<Integer> sut = create(5);
        //assert
        assertThatThrownBy(() -> sut.set(3, null)).isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldBombCtorOtherListNull() {
        //arrange
        List<Integer> sut2 = null;
        //assert
        assertThatThrownBy(() -> new NullSafeList<>(sut2)).isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldBombCtorOtherListElementIsNull() {
        //arrange
        List<Integer> sut2 = new ArrayList<>();
        sut2.add(null);
        //assert
        assertThatThrownBy(() -> new NullSafeList<>(sut2)).isExactlyInstanceOf(NullPointerException.class);
    }
}
