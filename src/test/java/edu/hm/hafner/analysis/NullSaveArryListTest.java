package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

public class NullSaveArryListTest extends NullSafeListTest{
    @Override
    public List<Integer> create(final int numberOfInitalElements) {
            ArrayList<Integer> test = new ArrayList<>(numberOfInitalElements);
            for (int index = 0; index < numberOfInitalElements; index++){
                test.add(index);
            }
            return new NullSaveArrayList<>(test);
    }
}
