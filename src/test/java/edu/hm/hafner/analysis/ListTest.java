package edu.hm.hafner.analysis;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

abstract class ListTest {

    abstract List<Integer> create(int numberOfInitialElements);

    @Test
    void IsEmptyTest() {
        List<Integer> sut = create(0);

        assertThat(sut.isEmpty()).isTrue();
    }

    @Test
    void IsNotEmptyTest() {
        List<Integer> sut = create(1);

        assertThat(sut.isEmpty()).isFalse();
    }

    @Test
    void correctSizeTest() {
        List<Integer> emptySut = create(0);
        List<Integer> oneElemSut = create(1);
        List<Integer> multiSut = create(500);

        assertThat(emptySut.size()).isEqualTo(0);
        assertThat(oneElemSut.size()).isEqualTo(1);
        assertThat(multiSut.size()).isEqualTo(500);
    }

    @Test
    void getElementTest() {
        List<Integer> emptySut = create(0);
        List<Integer> oneElemSut = create(1);
        List<Integer> multiSut = create(500);

        assertThatThrownBy(() -> {
            int result = emptySut.get(0);
        }).isInstanceOf(IndexOutOfBoundsException.class);

        assertThatThrownBy(() -> {
            int result = emptySut.get(1);
        }).isInstanceOf(IndexOutOfBoundsException.class);

        assertThatThrownBy(() -> {
            int result = oneElemSut.get(1);
        }).isInstanceOf(IndexOutOfBoundsException.class);

        assertThatThrownBy(() -> {
            int result = multiSut.get(500);
        }).isInstanceOf(IndexOutOfBoundsException.class);

        assertThat(oneElemSut.get(0)).isEqualTo(5);
        assertThat(multiSut.get(0)).isEqualTo(5);
        assertThat(multiSut.get(1)).isEqualTo(6);
        assertThat(multiSut.get(499)).isEqualTo(4);
    }

    @Test
    void addTest(){
        List<Integer> emptySut = create(0);
        List<Integer> oneElemSut = create(1);

        emptySut.add(7);
        oneElemSut.add(-50);
        oneElemSut.add(Integer.MAX_VALUE);

        assertThat(emptySut.size()).isEqualTo(1);
        assertThat(emptySut.get(0)).isEqualTo(7);
        assertThat(oneElemSut.size()).isEqualTo(3);
        assertThat(oneElemSut.get(1)).isEqualTo(-50);
        assertThat(oneElemSut.get(2)).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    void removeRangeTest(){
        List<Integer> oneElemSut = create(1);
        List<Integer> multiSut = create(20);

        assertThatThrownBy(() -> oneElemSut.remove(1)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> oneElemSut.remove(-1)).isInstanceOf(IndexOutOfBoundsException.class);

        oneElemSut.remove(0);
        assertThat(oneElemSut.isEmpty()).isTrue();

        assertThatThrownBy(() -> oneElemSut.remove(0)).isInstanceOf(IndexOutOfBoundsException.class);

        oneElemSut.add(Integer.MAX_VALUE);
        assertThat(oneElemSut.get(0)).isEqualTo(Integer.MAX_VALUE);

        assertThatThrownBy(() -> multiSut.remove(500)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> multiSut.remove(-1)).isInstanceOf(IndexOutOfBoundsException.class);

        int result = multiSut.remove(1);
        assertThat(result).isEqualTo(6);
        assertThat(multiSut.isEmpty()).isFalse();

        assertThatThrownBy(() -> multiSut.remove(499)).isInstanceOf(IndexOutOfBoundsException.class);

        multiSut.add(12);
        assertThat(multiSut.get(19)).isEqualTo(12);
    }

    @Test
    void RemoveObjectTest() {
        List<Integer> oneElemSut = create(1);
        List<Integer> multiSut = create(20);

        assertThat(oneElemSut.remove(Integer.valueOf(6))).isFalse();
        assertThat(oneElemSut.remove(Integer.valueOf(5))).isTrue();
        assertThat(oneElemSut.size()).isEqualTo(0);

        assertThat(multiSut.remove(Integer.valueOf(6))).isTrue();
        assertThat(multiSut.size()).isEqualTo(19);
        assertThat(multiSut.get(1)).isEqualTo(7);
        assertThat(multiSut.get(10)).isEqualTo(6);
    }

    @Test
    void containsTest(){
        List<Integer> oneElemSut = create(1);
        List<Integer> multiSut = create(20);

        assertThat(oneElemSut.contains(5)).isTrue();
        assertThat(oneElemSut.contains(6)).isFalse();
        assertThat(multiSut.contains(6)).isTrue();
        assertThat(multiSut.contains(-6)).isFalse();
        assertThat(multiSut.contains(7)).isTrue();
        assertThat(multiSut.contains(0)).isTrue();
    }
}
