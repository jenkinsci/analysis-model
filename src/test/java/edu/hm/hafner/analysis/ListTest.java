package edu.hm.hafner.analysis;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

/**
 * Abstrakte Testklasse, welche Tests fuer das Interface List mit dem generischen Typ Integer bereitstellt.
 *
 * @author Andreas Kienle, akienle@hm.edu
 * @version Last modified on 22/06/2020
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
        Assertions.assertThat(have).as("Check if get() returns an element correctly when called for a list without initial elements.").isEqualTo(want);
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
        Assertions.assertThat(have).as("Check if get() returns an element correctly when called for a list with initial elements.").isEqualTo(want);
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
        Assertions.assertThat(have).as("Check if remove(index) removes and returns an element correctly when called for a list without initial elements.").isEqualTo(want);
        Assertions.assertThat(have2).as("Check if contains() returns false for an element that was removed when called for a list without initial elements.").isFalse();
        Assertions.assertThat(have3).as("Check if size() returns the correct number when called after an element has been removed from a list without initial elements.").isEqualTo(1);
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
        Assertions.assertThat(have).as("Check if remove(index) removes and returns an element correctly when called for a list with initial elements.").isEqualTo(want);
        Assertions.assertThat(have2).as("Check if contains() returns false for an element that was removed when called for a list with initial elements.").isFalse();
        Assertions.assertThat(have3).as("Check if size() returns the correct number when called after an element has been removed from a list with initial elements.").isEqualTo(7);
    }
}
