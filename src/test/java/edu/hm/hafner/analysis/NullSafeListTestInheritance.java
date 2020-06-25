package edu.hm.hafner.analysis;

import java.util.List;

public class NullSafeListTestInheritance extends NullSafeListTest{
    @Override
    List<Integer> create(final int numberOfInitialElements) {
        List<Integer> list = new NullSafeListInherited<>();
        for (int i = 0; i < numberOfInitialElements; i++) {
            list.add(i);
        }
        return list;
    }
}
