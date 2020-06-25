package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

public class NullSafeListTestComposition extends NullSafeListTest{
    @Override
    List<Integer> create(final int numberOfInitialElements) {
        List<Integer> list = new NullSafeList<>(new ArrayList<>());
        for (int i = 0; i < numberOfInitialElements; i++) {
            list.add(i);
        }
        return list;
    }
}
