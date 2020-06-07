package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

class ArrayListTest extends ListTest{
    @Override
    List<Integer> create(final int numberOfInitialElements) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < numberOfInitialElements; i++) {
            list.add(i);
        }
        return list;
    }
}
