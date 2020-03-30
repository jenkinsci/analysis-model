package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation for the ArrayList of the ListTest class.
 */
public class ArrayListTest extends ListTest {
    @Override
    List<Integer> create(final int numberOfInitialElements) {
        List<Integer> list = new ArrayList<>();
        for (int index = 0; index < numberOfInitialElements; index++) {
            list.add(index);
        }
        return list;
    }
}
