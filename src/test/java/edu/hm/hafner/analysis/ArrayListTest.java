package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to call tests from {@Link ListTest}
 * @author Matthias König
 *
 */

class ArrayListTest extends ListTest {
    @Override
    List<Integer> create(int numberOfInitialElements) {
        ArrayList<Integer> arrayListTest = new ArrayList<Integer>(numberOfInitialElements);
        for (int numToAdd = 0; numToAdd < numberOfInitialElements; numToAdd++) {
            arrayListTest.add(numToAdd);
        }
        return arrayListTest;
    }
}