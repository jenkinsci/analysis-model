package edu.hm.ahager.listtest;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

abstract class ListTest {

    abstract List<Integer> create(int numberOfInitialElements);

    @Test
    public void testCreateNumberOfInitialElementsSmallerZeroBooms(){
        assertThrows(IllegalArgumentException.class, () -> create(-1));
    }

    @Test
    public void testIsEmptyOfEmptyList() {
        // arrange
        final List<Integer> sut = this.create(0);
        final boolean want = true;
        // act
        final boolean have = sut.isEmpty();
        // assert
        assertEquals(want, have);
    }

    @Test
    public void testIsEmptyOfSingularList() {
        // arrange
        final List<Integer> sut = this.create(1);
        final boolean want = false;
        // act
        final boolean have = sut.isEmpty();
        // assert
        assertEquals(want, have);
    }

    @Test
    public void testSizeOfEmptyList() {
        // arrange
        final int want = 0;
        final List<Integer> sut = this.create(want);
        // act
        final int have = sut.size();
        // assert
        assertEquals(want, have);
    }

    @Test
    public void testSizeOfSingularList() {
        // arrange
        final int want = 1;
        final List<Integer> sut = this.create(want);
        // act
        final int have = sut.size();
        // assert
        assertEquals(want, have);
    }

    @Test
    public void testSizeOfList() {
        // arrange
        final int want = 1_000;
        final List<Integer> sut = this.create(want);
        // act
        final int have = sut.size();
        // assert
        assertEquals(want, have);
    }

    @Test
    public void testContainsInEmptyList() {
        // arrange
        final List<Integer> sut = this.create(0);
        for (int number = -1000; number < 1000; number++) {
            // act
            final boolean have = sut.contains(number);
            // assert
            assertFalse(have);
        }
    }

    @Test
    public void testContainsFilledList() {
        // arrange
        final List<Integer> sut = this.create(3);
        sut.add(1);
        sut.add(2);
        sut.add(-126);
        // act
        final boolean have0 = sut.contains(1);
        final boolean have1 = sut.contains(2);
        final boolean have2 = sut.contains(-126);
        final boolean have3 = sut.contains(0);
        final boolean have4 = sut.contains(-1);
        // assert
        assertTrue(have0);
        assertTrue(have1);
        assertTrue(have2);
        assertTrue(have3);
        assertFalse(have4);
    }

    // TODO: further test
}
