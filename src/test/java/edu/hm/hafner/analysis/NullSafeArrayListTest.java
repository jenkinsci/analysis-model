package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

public class NullSafeArrayListTest extends NullSafeListTest{
    @Override
    public List<Integer> create(final int numberOfInitialElements) {
        ArrayList<Integer> list = new ArrayList<>(numberOfInitialElements);
        for(int i = 0; i < numberOfInitialElements; i++){
            list.add(0);
        }

        return new NullSafeArrayList<>(list);
    }
}
