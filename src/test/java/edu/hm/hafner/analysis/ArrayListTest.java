package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Tests the class {@link ArrayList}.
 * @author Johannes Jäger
 */
class ArrayListTest extends ListTest {

    @Override
    List<Integer> create(int numberOfInitialElements) {
        final List<Integer> list = new ArrayList<>(numberOfInitialElements);
        if (numberOfInitialElements > 0) {
            IntStream.range(0, numberOfInitialElements).forEach(value -> list.add(value));
        }
        return list;
    }

}
