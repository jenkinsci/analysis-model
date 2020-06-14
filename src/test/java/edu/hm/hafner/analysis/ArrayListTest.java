package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

public class ArrayListTest extends ListTest {

    @Override
    protected List<Integer> create(final int numberOfInitialElements) {
        ArrayList<Integer> newList = new ArrayList<Integer>();
        for (int i = 0; i < numberOfInitialElements; i++) {
            newList.add(i);
        }
        return newList;
    }
}
