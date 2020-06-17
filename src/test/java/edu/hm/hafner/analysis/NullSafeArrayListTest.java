package edu.hm.hafner.analysis;

import java.util.List;

class NullSafeArrayListTest extends NullSafeListTest {

    @Override
    public List<Integer> create(final int numberOfInitialElements) {
        List<Integer> list = new NullSafeArrayList<>();
        for (int index = 0; index < numberOfInitialElements; index++) {
            list.add(index);
        }
        return list;
    }
}
