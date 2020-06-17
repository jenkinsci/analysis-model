package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

class ArrayListTest extends ListTest {
    @Override
    List<Integer> create(final int numberOfInitialElements) {
        List<Integer> result = new ArrayList<>(numberOfInitialElements);
        for(int index = 0; index < numberOfInitialElements; index++)
            result.add((index + 5) % 10);
        return result;
    }
}
