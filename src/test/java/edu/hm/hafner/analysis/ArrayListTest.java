package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

class ArrayListTest extends ListTest {
    @Override
    List<Integer> create(final int numberOfInitialElements) {
        List<Integer> list = new ArrayList<>(numberOfInitialElements);
        // generate some "random" positiv and negative values
        for(int element = -numberOfInitialElements; element < numberOfInitialElements; element += 2){
            list.add(element * (int)(Math.random() * 10));
        }
        return list;
    }
}
