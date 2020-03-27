package edu.hm.ahager.listtest;

import java.util.ArrayList;
import java.util.List;

public class ArrayListTest extends ListTest {
    @Override
    List<Integer> create(final int numberOfInitialElements) {
        if(numberOfInitialElements < 0) {
            throw new IllegalArgumentException();
        }
        final List<Integer> arrayList = new ArrayList<>();
        for(int index = 0; index < numberOfInitialElements; index++){
            arrayList.add(index);
        }
        return arrayList;
    }
}
