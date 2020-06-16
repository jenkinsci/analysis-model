package edu.hm.hafner.analysis;

import java.util.List;

import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Abstract Testclass ListTest.
 *
 * @author Thorsten Schartel
 */
abstract class ListTest {
    abstract List<Integer> create(int numberOfInitialElements);

    @Test
    void shouldBeEmpty() {
        //assert
        assertThat(create(0).isEmpty()).isTrue();
    }

    @Test
    void shouldBeEmptyAfterClear() {
        //arrange
        List<Integer> sut = create(5);
        //act
        sut.clear();
        //assert
        assertThat(sut.isEmpty()).isTrue();
    }

    @Test
    void shouldBeEmptyAfterRemove() {
        //arrange
        List<Integer> sut = create(2);
        //act
        sut.remove(0);
        sut.remove(0);
        //assert
        assertThat(sut.isEmpty()).isTrue();
    }

    @Test
    void shouldHaveSize() {
        //arrange
        List<Integer> sut = create(2);

        //assert
        assertThat(sut.size()).isEqualTo(2);
    }

    @Test
    void shouldHaveSizeAfterRemove() {
        //arrange
        List<Integer> sut = create(2);
        //act
        sut.remove(0);
        //assert
        assertThat(sut.size()).isEqualTo(1);
    }

    @Test
    void shouldHaveRightElementGetAfterAdd() {
        //arrange
        List<Integer> sut = create(0);
        //act
        sut.add(52);
        sut.add(25);
        //assert
        assertThat(sut.get(0)).isEqualTo(52);
        assertThat(sut.get(1)).isEqualTo(25);
    }

    @Test
    void shouldContainElement() {
        //arrange
        List<Integer> sut = create(0);
        //act
        sut.add(52);
        sut.add(25);
        //assert
        assertThat(sut.contains(52)).isTrue();
        assertThat(sut.contains(25)).isTrue();
    }

    @Test
    void shouldAddElement() {
        //arrange
        List<Integer> sut = create(0);
        //assert
        assertThat(sut.add(52)).isTrue();
        assertThat(sut.size()).isEqualTo(1);
    }

}
