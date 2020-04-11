
package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;


class ArrayListTest extends ListTest {

    @Override
    List<Integer> create(int numberOfInitialElements) {
        List list = new ArrayList();
        for (int element = 0; element < numberOfInitialElements; element++) {
            list.add(element);
        }
        return list;
    }
}