package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

/**
 * Testklasse fuer die Companionklasse NullSafeCollections.
 *
 * @author Andreas Kienle, akienle@hm.edu
 * @version Last modified on 06/07/2020
 * @see edu.hm.hafner.analysis.NullSafeCollections
 */
class NullSafeCollectionsTest {
    @Test
    public void testEmptyNullSafeListReturnsNotNull() {
        // arrange, act
        final NullSafeList<Integer> have = NullSafeCollections.emptyNullSafeList();

        // assert
        Assertions.assertThat(have)
                .as("Check if emptyNullSafeList() does not return the null value.")
                .isNotNull();
    }

    @Test
    public void testEmptyNullSafeListReturnsEmptyList() {
        // arrange
        final int wantSize = 0;

        // act
        final NullSafeList<Integer> have = NullSafeCollections.emptyNullSafeList();

        // assert
        Assertions.assertThat(have)
                .as("Check if emptyNullSafeList() does not return the null value.")
                .isNotNull();
        Assertions.assertThat(have)
                .as("Check if emptyNullSafeList() returns an empty list.")
                .isEmpty();
        Assertions.assertThat(have)
                .as("Check if emptyNullSafeList() returns a list with the size zero.")
                .hasSize(wantSize);
    }

    @Test
    public void testNullSafeListCollectionMethodGivenNullBombs() {
        // arrange, act, assert
        Assertions.assertThatNullPointerException()
                .as("Check if nullSafeList() with collection parameter given the null value bombs.")
                .isThrownBy(() -> NullSafeCollections.nullSafeList(null));
    }

    @Test
    public void testNullSafeListCollectionMethodGivenEmptyCollectionReturnsEmpty() {
        // arrange
        final int wantSize = 0;
        final Collection<Integer> collection = new ArrayList<>();

        // act
        final NullSafeList<Integer> have = NullSafeCollections.nullSafeList(collection);

        // assert
        Assertions.assertThat(have)
                .as("Check if nullSafeList() with collection parameter does not return the null value when given an empty collection.")
                .isNotNull();
        Assertions.assertThat(have)
                .as("Check if nullSafeList() with collection parameter returns an empty list when given an empty collection.")
                .isEmpty();
        Assertions.assertThat(have)
                .as("Check if nullSafeList() with collection parameter returns a list with size zero when given an empty collection.")
                .hasSize(wantSize);
    }

    @Test
    public void testNullSafeListCollectionMethodGivenCollectionReturnsFilledList() {
        // arrange
        final int wantSize = 4;
        final Collection<Integer> collection = new ArrayList<>();
        collection.add(6);
        collection.add(10);
        collection.add(1);
        collection.add(25);

        // act
        final NullSafeList<Integer> have = NullSafeCollections.nullSafeList(collection);

        // assert
        Assertions.assertThat(have)
                .as("Check if nullSafeList() with collection parameter does not return the null value when given a filled collection.")
                .isNotNull();
        Assertions.assertThat(have)
                .as("Check if nullSafeList() with collection parameter returns a correctly filled list when given an empty collection.")
                .containsExactly(6, 10, 1, 25);
        Assertions.assertThat(have)
                .as("Check if nullSafeList() with collection parameter returns a list with the correct size when given a filled collection.")
                .hasSize(wantSize);
    }

    @Test
    public void testNullSafeListCapacityMethodGivenNegativeBombs() {
        // arrange, act, assert
        Assertions.assertThatIllegalArgumentException()
                .as("Check if nullSafeList() with capacity parameter given a negative value bombs.")
                .isThrownBy(() -> NullSafeCollections.nullSafeList(-1));
    }

    @Test
    public void testNullSafeListCapacityMethodGivenZeroReturnsEmpty() {
        // arrange
        final int wantSize = 0;

        // act
        final NullSafeList<Integer> have = NullSafeCollections.nullSafeList(wantSize);

        // assert
        Assertions.assertThat(have)
                .as("Check if nullSafeList() with capacity parameter does not return the null value when given zero.")
                .isNotNull();
        Assertions.assertThat(have)
                .as("Check if nullSafeList() with capacity parameter returns an empty list when given zero.")
                .isEmpty();
        Assertions.assertThat(have)
                .as("Check if nullSafeList() with capacity parameter returns a list with size zero when given zero.")
                .hasSize(wantSize);
    }

    @Test
    public void testNullSafeListCapacityMethodGivenAmountReturnsEmptyList() {
        // arrange
        final int wantSize = 0;

        // act
        final NullSafeList<Integer> have = NullSafeCollections.nullSafeList(8);

        // assert
        Assertions.assertThat(have)
                .as("Check if nullSafeList() with capacity parameter does not return the null value when given an amount.")
                .isNotNull();
        Assertions.assertThat(have)
                .as("Check if nullSafeList() with capacity parameter returns an empty list when given an amount.")
                .isEmpty();
        Assertions.assertThat(have)
                .as("Check if nullSafeList() with capacity parameter returns a list with the size zero when given an amount.")
                .hasSize(wantSize);
    }
}
