package edu.hm.hafner.analysis;

import java.util.List;

/**
 * Testklasse fuer die Klasse InheritingNullSafeList. Erbt alle Tests von der Testklasse NullSafeListTest.
 *
 * @author Andreas Kienle
 * @version Last modified on 06/07/2020
 * @see edu.hm.hafner.analysis.InheritingNullSafeList
 * @see edu.hm.hafner.analysis.NullSafeListTest
 */
class InheritingNullSafeListTest extends NullSafeListTest {
    @Override
    protected List<Integer> create(final int numberOfInitialElements) {
        if (numberOfInitialElements < 0) {
            throw new IllegalArgumentException("The number of initial elements may not be negative.");
        }

        final List<Integer> toReturn = new InheritingNullSafeList<>();

        for (int index = 0; index < numberOfInitialElements; index++) {
            toReturn.add(index);
        }

        return toReturn;
    }
}
