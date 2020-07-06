package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

/**
 * Abstrakte Testklasse, welche Tests fuer das Interface List mit dem generischen Typ Integer bereitstellt.
 *
 * @author Andreas Kienle, akienle@hm.edu
 * @version Last modified on 06/07/2020
 * @see java.util.List
 */
abstract class ListTest {
    /**
     * Factory-Methode, welche eine Instanz einer Liste von Integer-Werten erstellt und zurueckgibt. Diese Instanz wird
     * fuer die Tests verwendet.
     *
     * @param numberOfInitialElements
     *         Die Anzahl an Elementen, welche die zurueckgegebene Liste enthalten soll. Nicht negativ.
     *
     * @return Eine, fuer die Tests erstellte, Instanz einer Liste von Integer-Werten.
     */
    protected abstract List<Integer> create(int numberOfInitialElements);

    @Test
    public void testIsEmptyAfterInitNoInitElementsReturnsTrue() {
        // arrange
        final List<Integer> sut = create(0);

        // act
        final boolean have = sut.isEmpty();

        // assert
        Assertions.assertThat(have)
                .isTrue()
                .as("Check if isEmpty() returns true when called after initialization of the list with no initial elements.");
    }

    @Test
    public void testSizeAfterInitNoInitElementsReturnsZero() {
        // arrange
        final List<Integer> sut = create(0);

        // act
        final int have = sut.size();

        // assert
        Assertions.assertThat(have)
                .as("Check if size() returns 0 when called after initialization of the list with no initial elements.")
                .isZero();
    }

    @Test
    public void testInitialElementsNegativeBombs() {
        // arrange, act, assert
        Assertions.assertThatIllegalArgumentException().isThrownBy(() -> create(-1));
    }

    @Test
    public void testIsEmptyAfterInitWithInitElementsReturnsFalse() {
        // arrange
        final List<Integer> sut = create(5);

        // act
        final boolean have = sut.isEmpty();

        // assert
        Assertions.assertThat(have)
                .isFalse()
                .as("Check if isEmpty() returns false when called after initialization of the list with initial elements.");
    }

    @Test
    public void testSizeAfterInitWithInitElementsEqualsNumberElements() {
        // arrange
        final List<Integer> sut = create(5);
        final int want = 5;

        // act
        final int have = sut.size();

        // assert
        Assertions.assertThat(have)
                .as("Check if size() returns the number of initial elements when called after initialization of the list with initial elements.")
                .isEqualTo(want);
    }

    @Test
    public void testContainsWithoutElementsReturnsFalse() {
        // arrange
        final List<Integer> sut = create(0);

        // act
        final boolean have = sut.contains(4);
        final boolean have2 = sut.contains(-3);
        final boolean have3 = sut.contains(500);

        // assert
        Assertions.assertThat(have)
                .as("Check if contains() returns false when called after initialization of the list without initial elements.")
                .isFalse();
        Assertions.assertThat(have2)
                .as("Check if contains() returns false when called after initialization of the list without initial elements.")
                .isFalse();
        Assertions.assertThat(have3)
                .as("Check if contains() returns false when called after initialization of the list without initial elements.")
                .isFalse();
    }

    @Test
    public void testNormalAddWithoutInitElementsWorksCorrectly() {
        // arrange
        final List<Integer> sut = create(0);
        final int want = 1;

        sut.add(3);

        // act
        final int have = sut.size();
        final boolean have2 = sut.contains(3);
        final boolean have3 = sut.isEmpty();

        // assert
        Assertions.assertThat(have)
                .as("Check if size() returns 1 when called after adding one element to a list without initial elements.")
                .isEqualTo(want);
        Assertions.assertThat(have2)
                .as("Check if contains() returns true for the added element when called after adding one element to a list without initial elements.")
                .isTrue();
        Assertions.assertThat(have3)
                .as("Check if isEmpty() returns false when called after adding one element to a list without initial elements.")
                .isFalse();
    }

    @Test
    public void testNormalAddWithInitElementsWorksCorrectly() {
        // arrange
        final List<Integer> sut = create(4);
        final int want = 5;

        sut.add(10);

        // act
        final int have = sut.size();
        final boolean have2 = sut.contains(10);
        final boolean have3 = sut.isEmpty();

        // assert
        Assertions.assertThat(have)
                .as("Check if size() returns 1 when called after adding one element to a list with initial elements.")
                .isEqualTo(want);
        Assertions.assertThat(have2)
                .as("Check if contains() returns true for the added element when called after adding one element to a list with initial elements.")
                .isTrue();
        Assertions.assertThat(have3)
                .as("Check if isEmpty() returns false when called after adding one element to a list with initial elements.")
                .isFalse();
    }

    @Test
    public void testContainsForAddedElementWithoutInitElementsReturnsTrue() {
        // arrange
        final List<Integer> sut = create(0);
        sut.add(17);

        // act
        final boolean have = sut.contains(17);

        // assert
        Assertions.assertThat(have)
                .as("Check if contains() returns true for the added element when called after adding one element to a list without initial elements.")
                .isTrue();
    }

    @Test
    public void testContainsForAddedElementWithInitElementsReturnsTrue() {
        // arrange
        final List<Integer> sut = create(20);
        sut.add(57);

        // act
        final boolean have = sut.contains(57);

        // assert
        Assertions.assertThat(have)
                .as("Check if contains() returns true for the added element when called after adding one element to a list with initial elements.")
                .isTrue();
    }

    @Test
    public void testContainsForNotAddedElementWithoutInitElementsReturnsFalse() {
        // arrange
        final List<Integer> sut = create(0);
        sut.add(4);

        // act
        final boolean have = sut.contains(10);

        // assert
        Assertions.assertThat(have)
                .as("Check if contains() returns false for a not added element when called after adding one element to a list without initial elements.")
                .isFalse();
    }

    @Test
    public void testContainsForNotAddedElementWithInitElementsReturnsFalse() {
        // arrange
        final List<Integer> sut = create(9);
        sut.add(13);

        // act
        final boolean have = sut.contains(21);

        // assert
        Assertions.assertThat(have)
                .as("Check if contains() returns false for a not added element when called after adding one element to a list with initial elements.")
                .isFalse();
    }

    @Test
    public void testGetForNegativeNumberBombs() {
        // arrange
        final List<Integer> sut = create(2);

        // act, assert
        Assertions.assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> sut.get(-1));
    }

    @Test
    public void testGetForNumberBiggerSizeBombs() {
        // arrange
        final List<Integer> sut = create(10);

        // act, assert
        Assertions.assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> sut.get(12));
    }

    @Test
    public void testGetWithoutInitElementsWorksCorrectly() {
        // arrange
        final List<Integer> sut = create(0);
        sut.add(5);
        sut.add(11);
        sut.add(29);

        final int want = 11;

        // act
        final int have = sut.get(1);

        // assert
        Assertions.assertThat(have)
                .as("Check if get() returns an element correctly when called for a list without initial elements.")
                .isEqualTo(want);
    }

    @Test
    public void testGetWithInitElementsWorksCorrectly() {
        // arrange
        final List<Integer> sut = create(8);
        sut.add(30);
        sut.add(125);
        sut.add(55);
        sut.add(75);
        sut.add(3);

        final int want = 30;

        // act
        final int have = sut.get(8);

        // assert
        Assertions.assertThat(have)
                .as("Check if get() returns an element correctly when called for a list with initial elements.")
                .isEqualTo(want);
    }

    @Test
    public void testIndexRemoveWithoutInitElementsWorksCorrectly() {
        // arrange
        final List<Integer> sut = create(0);
        sut.add(5);
        sut.add(7);

        final Integer want = 5;

        // act
        final int have = sut.remove(0);
        final boolean have2 = sut.contains(5);
        final int have3 = sut.size();

        // assert
        Assertions.assertThat(have)
                .as("Check if remove(index) removes and returns an element correctly when called for a list without initial elements.")
                .isEqualTo(want);
        Assertions.assertThat(have2)
                .as("Check if contains() returns false for an element that was removed when called for a list without initial elements.")
                .isFalse();
        Assertions.assertThat(have3)
                .as("Check if size() returns the correct number when called after an element has been removed from a list without initial elements.")
                .isEqualTo(1);
    }

    @Test
    public void testIndexRemoveWithInitElementsWorksCorrectly() {
        // arrange
        final List<Integer> sut = create(6);
        sut.add(14);
        sut.add(23);

        final Integer want = 23;

        // act
        final int have = sut.remove(7);
        final boolean have2 = sut.contains(23);
        final int have3 = sut.size();

        // assert
        Assertions.assertThat(have)
                .as("Check if remove(index) removes and returns an element correctly when called for a list with initial elements.")
                .isEqualTo(want);
        Assertions.assertThat(have2)
                .as("Check if contains() returns false for an element that was removed when called for a list with initial elements.")
                .isFalse();
        Assertions.assertThat(have3)
                .as("Check if size() returns the correct number when called after an element has been removed from a list with initial elements.")
                .isEqualTo(7);
    }

    @Test
    public void testIndexAddWithoutInitElementsWorksCorrectly() {
        // arrange
        final List<Integer> sut = create(0);
        sut.add(4);
        sut.add(7);
        sut.add(5);

        final int wantSize = 4;

        // act
        sut.add(2, 10);

        // assert
        Assertions.assertThat(sut)
                .as("Check if a list without initial elements is not empty after adding an element with the add() method with index parameter.")
                .isNotEmpty();
        Assertions.assertThat(sut)
                .as("Check if add() with index parameter adds an element at a certain index in a list without initial elements correctly.")
                .containsExactly(4, 7, 10, 5);
        Assertions.assertThat(sut)
                .as("Check if a list without initial elements has the correct size after adding an element with the add() method with index parameter.")
                .hasSize(wantSize);
    }

    @Test
    public void testIndexAddWithInitElementsWorksCorrectly() {
        // arrange
        final List<Integer> sut = create(3);
        sut.add(5);
        sut.add(10);
        sut.add(2);

        final int wantSize = 7;
        final int want = 15;

        // act
        sut.add(5, want);
        final int have = sut.get(5);

        // assert
        Assertions.assertThat(sut)
                .as("Check if a list with initial elements is not empty after adding an element with the add() method with index parameter.")
                .isNotEmpty();
        Assertions.assertThat(sut)
                .as("Check if add() with index parameter adds an element at a certain index in a list with initial elements correctly.")
                .containsSequence(5, 10, 15, 2);
        Assertions.assertThat(have)
                .as("Check if add() with index parameter adds an element at a certain index in a list with initial elements correctly.")
                .isEqualTo(want);
        Assertions.assertThat(sut)
                .as("Check if a list with initial elements has the correct size after adding an element with the add() method with index parameter.")
                .hasSize(wantSize);
    }

    @Test
    public void testNormalAddAllWithoutInitElementsGivenNullBombs() {
        // arrange
        final List<Integer> sut = create(0);

        // act, assert
        Assertions.assertThatNullPointerException()
                .as("Check if addAll() throws a NullPointerException when called for a list without initial elements and given null.")
                .isThrownBy(() -> sut.addAll(null));
    }

    @Test
    public void testNormalAddAllWithInitElementsGivenNullBombs() {
        // arrange
        final List<Integer> sut = create(6);

        // act, assert
        Assertions.assertThatNullPointerException()
                .as("Check if addAll() parameter throws a NullPointerException when called for a list with initial elements and given null.")
                .isThrownBy(() -> sut.addAll(null));
    }

    @Test
    public void testNormalAddAllWithoutInitElementsWorksCorrectly() {
        // arrange
        final List<Integer> sut = create(0);
        sut.add(10);
        sut.add(2);

        final Collection<Integer> collection = new ArrayList<>();
        collection.add(3);
        collection.add(5);
        collection.add(1);

        final int wantSize = 5;

        // act
        sut.addAll(collection);

        // assert
        Assertions.assertThat(sut)
                .as("Check if a list without initial elements is not empty after adding a collection with the addAll() method.")
                .isNotEmpty();
        Assertions.assertThat(sut)
                .as("Check if addAll() adds a collection to a list without initial elements correctly.")
                .containsExactly(10, 2, 3, 5, 1);
        Assertions.assertThat(sut)
                .as("Check if a list without initial elements has the correct size after adding a collection with the addAll() method.")
                .hasSize(wantSize);
    }

    @Test
    public void testNormalAddAllWithInitElementsWorksCorrectly() {
        // arrange
        final List<Integer> sut = create(3);
        sut.add(10);
        sut.add(2);

        final Collection<Integer> collection = new ArrayList<>();
        collection.add(8);
        collection.add(12);
        collection.add(25);

        final int wantSize = 8;

        // act
        sut.addAll(collection);

        // assert
        Assertions.assertThat(sut)
                .as("Check if a list with initial elements is not empty after adding a collection with the addAll() method.")
                .isNotEmpty();
        Assertions.assertThat(sut)
                .as("Check if addAll() adds a collection to a list with initial elements correctly.")
                .containsSequence(10, 2, 8, 12, 25);
        Assertions.assertThat(sut)
                .as("Check if a list with initial elements has the correct size after adding a collection with the addAll() method.")
                .hasSize(wantSize);
    }

    @Test
    public void testIndexAddAllWithoutInitElementsGivenNullBombs() {
        // arrange
        final List<Integer> sut = create(0);

        // act, assert
        Assertions.assertThatNullPointerException()
                .as("Check if addAll() with index parameter throws a NullPointerException when called for a list without initial elements and given null.")
                .isThrownBy(() -> sut.addAll(0, null));
    }

    @Test
    public void testIndexAddAllWithInitElementsGivenNullBombs() {
        // arrange
        final List<Integer> sut = create(6);

        // act, assert
        Assertions.assertThatNullPointerException()
                .as("Check if addAll() with index parameter throws a NullPointerException when called for a list with initial elements and given null.")
                .isThrownBy(() -> sut.addAll(4, null));
    }

    @Test
    public void testIndexAddAllWithoutInitElementsWorksCorrectly() {
        // arrange
        final List<Integer> sut = create(0);
        sut.add(10);
        sut.add(2);
        sut.add(34);
        sut.add(1);

        final Collection<Integer> collection = new ArrayList<>();
        collection.add(9);
        collection.add(16);
        collection.add(22);

        final int wantSize = 7;

        // act
        sut.addAll(1, collection);

        // assert
        Assertions.assertThat(sut)
                .as("Check if a list without initial elements is not empty after adding a collection with the addAll() method with index parameter.")
                .isNotEmpty();
        Assertions.assertThat(sut)
                .as("Check if addAll() with index parameter adds a collection to a list without initial elements correctly.")
                .containsExactly(10, 9, 16, 22, 2, 34, 1);
        Assertions.assertThat(sut)
                .as("Check if a list without initial elements has the correct size after adding a collection with the addAll() method with index parameter.")
                .hasSize(wantSize);
    }

    @Test
    public void testIndexAddAllWithInitElementsWorksCorrectly() {
        // arrange
        final List<Integer> sut = create(7);
        sut.add(10);
        sut.add(14);
        sut.add(34);
        sut.add(50);

        final Collection<Integer> collection = new ArrayList<>();
        collection.add(22);
        collection.add(16);
        collection.add(27);

        final int wantSize = 14;

        // act
        sut.addAll(9, collection);

        // assert
        Assertions.assertThat(sut)
                .as("Check if a list with initial elements is not empty after adding a collection with the addAll() method with index parameter.")
                .isNotEmpty();
        Assertions.assertThat(sut)
                .as("Check if addAll() with index parameter adds a collection to a list with initial elements correctly.")
                .containsSequence(10, 14, 22, 16, 27, 34, 50);
        Assertions.assertThat(sut)
                .as("Check if a list with initial elements has the correct size after adding a collection with the addAll() method with index parameter.")
                .hasSize(wantSize);
    }

    @Test
    public void testSetWithoutInitElementsWorksCorrectly() {
        // arrange
        final List<Integer> sut = create(0);
        sut.add(4);
        sut.add(7);
        sut.add(5);

        final int wantSize = 3;

        // act
        sut.set(1, 10);

        // assert
        Assertions.assertThat(sut)
                .as("Check if a list without initial elements is not empty after replacing an element with the set() method.")
                .isNotEmpty();
        Assertions.assertThat(sut)
                .as("Check if set() replaces an element at a certain index in a list without initial elements correctly.")
                .containsExactly(4, 10, 5);
        Assertions.assertThat(sut)
                .as("Check if a list without initial elements has the correct size after replacing an element with the set() method.")
                .hasSize(wantSize);
    }

    @Test
    public void testSetWithInitElementsWorksCorrectly() {
        // arrange
        final List<Integer> sut = create(3);
        sut.add(5);
        sut.add(10);
        sut.add(19);

        final int wantSize = 6;

        // act
        sut.set(3, 26);

        // assert
        Assertions.assertThat(sut)
                .as("Check if a list with initial elements is not empty after replacing an element with the set() method.")
                .isNotEmpty();
        Assertions.assertThat(sut)
                .as("Check if set() replaces an element at a certain index in a list with initial elements correctly.")
                .containsSequence(26, 10, 19);
        Assertions.assertThat(sut)
                .as("Check if a list with initial elements has the correct size after replacing an element with the set() method.")
                .hasSize(wantSize);
    }

    @Test
    public void testClearWithoutInitElementsWorksCorrectly() {
        // arrange
        final List<Integer> sut = create(0);
        sut.add(5);
        sut.add(10);
        sut.add(555);

        final int wantSize = 0;

        // act
        sut.clear();

        // assert
        Assertions.assertThat(sut)
                .as("Check if a list without initial elements is empty after calling the clear() method.")
                .isEmpty();
        Assertions.assertThat(sut)
                .as("Check if a list without initial elements has the correct size after calling the clear() method.")
                .hasSize(wantSize);
    }

    @Test
    public void testClearWithInitElementsWorksCorrectly() {
        // arrange
        final List<Integer> sut = create(5);
        sut.add(5);
        sut.add(10);
        sut.add(555);

        final int wantSize = 0;

        // act
        sut.clear();

        // assert
        Assertions.assertThat(sut)
                .as("Check if a list with initial elements is empty after calling the clear() method.")
                .isEmpty();
        Assertions.assertThat(sut)
                .as("Check if a list with initial elements has the correct size after calling the clear() method.")
                .hasSize(wantSize);
    }

    @Test
    public void testContainsAllWithoutInitElementsGivenNullBombs() {
        // arrange
        final List<Integer> sut = create(0);

        // act, assert
        Assertions.assertThatNullPointerException()
                .as("Check if containsAll() throws a NullPointerException when called for a list without initial elements and given null.")
                .isThrownBy(() -> sut.containsAll(null));
    }

    @Test
    public void testContainsAllWithInitElementsGivenNullBombs() {
        // arrange
        final List<Integer> sut = create(10);

        // act, assert
        Assertions.assertThatNullPointerException()
                .as("Check if containsAll() throws a NullPointerException when called for a list with initial elements and given null.")
                .isThrownBy(() -> sut.containsAll(null));
    }

    @Test
    public void testContainsAllWithoutInitElementsContainsNotAllElementsReturnsFalse() {
        // arrange
        final List<Integer> sut = create(0);
        sut.add(10);
        sut.add(14);
        sut.add(34);
        sut.add(50);

        final Collection<Integer> collection = new ArrayList<>();
        collection.add(10);
        collection.add(34);
        collection.add(999);

        final int wantSize = 4;

        // act
        final boolean have = sut.containsAll(collection);

        // assert
        Assertions.assertThat(sut)
                .as("Check if containsAll() does not change the size when called for a list without initial elements.")
                .hasSize(wantSize);
        Assertions.assertThat(have)
                .as("Check if containsAll() returns false when called for a list without initial elements that does not contain all elements of the given collection.")
                .isFalse();
    }

    @Test
    public void testContainsAllWithInitElementsContainsNotAllElementsReturnsFalse() {
        // arrange
        final List<Integer> sut = create(5);
        sut.add(10);
        sut.add(14);
        sut.add(34);
        sut.add(50);

        final Collection<Integer> collection = new ArrayList<>();
        collection.add(10);
        collection.add(34);
        collection.add(999);

        final int wantSize = 9;

        // act
        final boolean have = sut.containsAll(collection);

        // assert
        Assertions.assertThat(sut)
                .as("Check if containsAll() does not change the size when called for a list with initial elements.")
                .hasSize(wantSize);
        Assertions.assertThat(have)
                .as("Check if containsAll() returns false when called for a list with initial elements that does not contain all elements of the given collection.")
                .isFalse();
    }

    @Test
    public void testContainsAllWithoutInitElementsContainsAllElementsReturnsTrue() {
        // arrange
        final List<Integer> sut = create(0);
        sut.add(10);
        sut.add(14);
        sut.add(34);
        sut.add(50);

        final Collection<Integer> collection = new ArrayList<>();
        collection.add(10);
        collection.add(14);
        collection.add(34);

        final int wantSize = 4;

        // act
        final boolean have = sut.containsAll(collection);

        // assert
        Assertions.assertThat(sut)
                .as("Check if containsAll() does not change the size when called for a list without initial elements.")
                .hasSize(wantSize);
        Assertions.assertThat(have)
                .as("Check if containsAll() returns true when called for a list without initial elements that does contain all elements of the given collection.")
                .isTrue();
    }

    @Test
    public void testContainsAllWithInitElementsContainsAllElementsReturnsTrue() {
        // arrange
        final List<Integer> sut = create(4);
        sut.add(10);
        sut.add(14);
        sut.add(34);
        sut.add(50);

        final Collection<Integer> collection = new ArrayList<>();
        collection.add(10);
        collection.add(14);
        collection.add(34);

        final int wantSize = 8;

        // act
        final boolean have = sut.containsAll(collection);

        // assert
        Assertions.assertThat(sut)
                .as("Check if containsAll() does not change the size when called for a list with initial elements.")
                .hasSize(wantSize);
        Assertions.assertThat(have)
                .as("Check if containsAll() returns true when called for a list with initial elements that does contain all elements of the given collection.")
                .isTrue();
    }
}
