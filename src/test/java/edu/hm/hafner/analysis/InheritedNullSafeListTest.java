package edu.hm.hafner.analysis;

import java.util.List;


class InheritedNullSafeListTest extends NullSafeListTest {
    @Override
    List<Integer> create(final int numberOfInitialElements) {
        List<Integer> list = new InheritedNullSafeList<>(numberOfInitialElements);
        for(int index = 0; index < numberOfInitialElements; index++)
            list.add((index + 5) % 10);
        return list;
    }
}