package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

/**
 * Testklasse fuer die spezifische Listen-Implementierung NullSafeList.
 *
 * @author Andreas Kienle, akienle@hm.edu
 * @version Last modified on 06/07/2020
 * @see edu.hm.hafner.analysis.NullSafeList
 * @see edu.hm.hafner.analysis.ListTest
 */
class NullSafeListTest extends ListTest {
    @Override
    protected List<Integer> create(final int numberOfInitialElements) {
        if (numberOfInitialElements < 0) {
            throw new IllegalArgumentException("The number of initial elements may not be negative.");
        }

        final List<Integer> toReturn = new NullSafeList<>(new ArrayList<>());

        for (int index = 0; index < numberOfInitialElements; index++) {
            toReturn.add(index);
        }

        return toReturn;
    }

    @Test
    public void testContainsWithoutInitElementsGivenNullBombs() {
        // arrange
        final List<Integer> sut = create(0);

        // act, assert
        Assertions.assertThatNullPointerException()
                .as("Check if contains() throws a NullPointerException when called with the null value for a list with no elements.")
                .isThrownBy(() -> sut.contains(null));
    }

    @Test
    public void testContainsWithInitElementsGivenNullBombs() {
        // arrange
        final List<Integer> sut = create(5);

        // act, assert
        Assertions.assertThatNullPointerException()
                .as("Check if contains() throws a NullPointerException when called with the null value for a list with initial elements.")
                .isThrownBy(() -> sut.contains(null));
    }

    @Test
    public void testContainsAllWithoutInitElementsGivenCollectionWithNullBombs() {
        // arrange
        final List<Integer> sut = create(0);

        final Collection<Integer> collection = new HashSet<>();
        collection.add(5);
        collection.add(null);
        collection.add(17);

        // act, assert
        Assertions.assertThatNullPointerException()
                .as("Check if containsAll() throws a NullPointerException when called with a collection containing the null value for a list with no elements.")
                .isThrownBy(() -> sut.containsAll(collection));
    }

    @Test
    public void testContainsAllWithInitElementsGivenCollectionWithNullBombs() {
        // arrange
        final List<Integer> sut = create(5);

        final Collection<Integer> collection = new HashSet<>();
        collection.add(5);
        collection.add(null);
        collection.add(17);

        // act, assert
        Assertions.assertThatNullPointerException()
                .as("Check if containsAll() throws a NullPointerException when called with a collection containing the null value for a list with initial elements.")
                .isThrownBy(() -> sut.containsAll(collection));
    }

    @Test
    public void testNormalAddWithoutInitElementsGivenNullBombs() {
        // arrange
        final List<Integer> sut = create(0);

        // act, assert
        Assertions.assertThatNullPointerException()
                .as("Check if add() throws a NullPointerException when called for a list without initial elements and given the null value.")
                .isThrownBy(() -> sut.add(null));
    }

    @Test
    public void testNormalAddWithInitElementsGivenNullBombs() {
        // arrange
        final List<Integer> sut = create(7);

        // act, assert
        Assertions.assertThatNullPointerException()
                .as("Check if add() throws a NullPointerException when called for a list with initial elements and given the null value.")
                .isThrownBy(() -> sut.add(null));
    }

    @Test
    public void testIndexAddWithoutInitElementsGivenNullBombs() {
        // arrange
        final List<Integer> sut = create(0);

        // act, assert
        Assertions.assertThatNullPointerException()
                .as("Check if add() with index parameter throws a NullPointerException when called for a list without initial elements and given the null value.")
                .isThrownBy(() -> sut.add(0, null));
    }

    @Test
    public void testIndexAddWithInitElementsGivenNullBombs() {
        // arrange
        final List<Integer> sut = create(7);

        // act, assert
        Assertions.assertThatNullPointerException()
                .as("Check if add() with index parameter throws a NullPointerException when called for a list with initial elements and given the null value.")
                .isThrownBy(() -> sut.add(5, null));
    }

    @Test
    public void testNormalAddAllWithoutInitElementsGivenCollectionWithNullBombs() {
        // arrange
        final List<Integer> sut = create(0);

        final Collection<Integer> collection = new HashSet<>();
        collection.add(9);
        collection.add(null);
        collection.add(22);
        collection.add(10);
        collection.add(null);
        collection.add(17);

        // act, assert
        Assertions.assertThatNullPointerException()
                .as("Check if addAll() throws a NullPointerException when called for a list without initial elements and given a collection containing the null value.")
                .isThrownBy(() -> sut.addAll(collection));
    }

    @Test
    public void testNormalAddAllWithInitElementsGivenCollectionWithNullBombs() {
        // arrange
        final List<Integer> sut = create(5);

        final Collection<Integer> collection = new HashSet<>();
        collection.add(9);
        collection.add(null);
        collection.add(22);
        collection.add(10);
        collection.add(null);
        collection.add(17);

        // act, assert
        Assertions.assertThatNullPointerException()
                .as("Check if addAll() throws a NullPointerException when called for a list with initial elements and given a collection containing the null value.")
                .isThrownBy(() -> sut.addAll(collection));
    }

    @Test
    public void testIndexAddAllWithoutInitElementsGivenCollectionWithNullBombs() {
        // arrange
        final List<Integer> sut = create(0);

        final Collection<Integer> collection = new HashSet<>();
        collection.add(9);
        collection.add(null);
        collection.add(22);
        collection.add(10);
        collection.add(null);
        collection.add(17);

        // act, assert
        Assertions.assertThatNullPointerException()
                .as("Check if addAll() with index parameter throws a NullPointerException when called for a list without initial elements and given a collection containing the null value.")
                .isThrownBy(() -> sut.addAll(0, collection));
    }

    @Test
    public void testIndexAddAllWithInitElementsGivenCollectionWithNullBombs() {
        // arrange
        final List<Integer> sut = create(5);

        final Collection<Integer> collection = new HashSet<>();
        collection.add(9);
        collection.add(null);
        collection.add(22);
        collection.add(10);
        collection.add(null);
        collection.add(17);

        // act, assert
        Assertions.assertThatNullPointerException()
                .as("Check if addAll() with index parameter throws a NullPointerException when called for a list with initial elements and given a collection containing the null value.")
                .isThrownBy(() -> sut.addAll(3, collection));
    }

    @Test
    public void testSetWithoutInitElementsGivenNullBombs() {
        // arrange
        final List<Integer> sut = create(0);

        // act, assert
        Assertions.assertThatNullPointerException()
                .as("Check if set() throws a NullPointerException when called for a list without initial elements and given the null value.")
                .isThrownBy(() -> sut.set(0, null));
    }

    @Test
    public void testSetWithInitElementsGivenNullBombs() {
        // arrange
        final List<Integer> sut = create(6);

        // act, assert
        Assertions.assertThatNullPointerException()
                .as("Check if set() throws a NullPointerException when called for a list with initial elements and given the null value.")
                .isThrownBy(() -> sut.set(3, null));
    }

    @Test
    public void testRemoveWithoutInitElementsGivenNullBombs() {
        // arrange
        final List<Integer> sut = create(0);
        sut.add(5);
        sut.add(7);

        // act, assert
        Assertions.assertThatNullPointerException()
                .as("Check if remove() throws a NullPointerException when called for a list without initial elements and given the null value.")
                .isThrownBy(() -> sut.remove(null));
    }

    @Test
    public void testRemoveWithInitElementsGivenNullBombs() {
        // arrange
        final List<Integer> sut = create(3);
        sut.add(5);
        sut.add(7);

        // act, assert
        Assertions.assertThatNullPointerException()
                .as("Check if remove() throws a NullPointerException when called for a list with initial elements and given the null value.")
                .isThrownBy(() -> sut.remove(null));
    }
}
