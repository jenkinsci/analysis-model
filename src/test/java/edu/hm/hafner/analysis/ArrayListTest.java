package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

/**
 * Testklasse fuer Array-Listen von Integer-Werten.
 *
 * @author Andreas Kienle, akienle@hm.edu
 * @version Last modified on 22/06/2020
 */
class ArrayListTest extends ListTest {
    @Override
    protected List<Integer> create(final int numberOfInitialElements) {
        if (numberOfInitialElements < 0) {
            throw new IllegalArgumentException("The number of initial elements can not be negative.");
        }

        final List<Integer> toReturn = new ArrayList<>();

        for (int index = 0; index < numberOfInitialElements; index++) {
            toReturn.add(index);
        }

        return toReturn;
    }
}
