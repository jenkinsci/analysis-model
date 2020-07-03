package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Test class for {@link NullSafeList}
 * only for overriding create()
 * test-cases in {@link ListTest}
 *
 * @author Matthias König
 */

class NullSafeArrayListTest extends ListTest{

    @Override
    List<Integer> create(int numberOfInitialElements) {
        ArrayList<Integer> nullSafeArrayList = new NullSafeArrayList<>(new ArrayList<>());
        for (int numToAdd = 0; numToAdd < numberOfInitialElements; numToAdd++) {
            nullSafeArrayList.add(numToAdd);
        }
        return nullSafeArrayList;
    }
}