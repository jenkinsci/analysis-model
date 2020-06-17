package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

class NullSafeArrayListTest extends ListTest{
    @Override
    List<Integer> create(final int numberOfInitialElements) {
        List<Integer> list = new NullSafeArrayList<Integer>();
        for (int i = 0; i < numberOfInitialElements; i++) {
            list.add(new Integer(i));
        }
        return list;
    }
}
