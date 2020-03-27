package edu.hm.ahager.listtest;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public abstract class ListTest {

    abstract List<Integer> create(int numberOfInitialElements);

    @Test
    public void testIsEmptyOfEmptyList(){
        // arrange
        final List<Integer> sut = this.create(0);
        final boolean want = true;
        // act
        final boolean have = sut.isEmpty();
        // assert
        assertEquals(want, have);
    }

    @Test
    public void testIsEmptyOfSingularList(){
        // arrange
        final List<Integer> sut = this.create(1);
        final boolean want = false;
        // act
        final boolean have = sut.isEmpty();
        // assert
        assertEquals(want, have);
    }

    @Test
    public void testSizeOfEmptyList(){
        // arrange
        final int want = 0;
        final List<Integer> sut = this.create(want);
        // act
        final int have = sut.size();
        // assert
        assertEquals(want, have);
    }

    @Test
    public void testSizeOfSingularList(){
        // arrange
        final int want = 1;
        final List<Integer> sut = this.create(want);
        // act
        final int have = sut.size();
        // assert
        assertEquals(want, have);
    }

    @Test
    public void testSizeOfList(){
        // arrange
        final int want = 1_000;
        final List<Integer> sut = this.create(want);
        // act
        final int have = sut.size();
        // assert
        assertEquals(want, have);
    }

}
