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

class NullSafeListTest extends ListTest {

    @Override
    List<Integer> create(int numberOfInitialElements) {
        List<Integer> nullSafeList = new NullSafeList<>(new ArrayList<>());
        for (int numToAdd = 0; numToAdd < numberOfInitialElements; numToAdd++) {
            nullSafeList.add(numToAdd);
        }
        return nullSafeList;
    }

}