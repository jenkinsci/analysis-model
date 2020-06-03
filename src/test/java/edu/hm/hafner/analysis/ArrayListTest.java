package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Test for ArrayList.
 *
 * @author Maier Leonhard
 *
 */
class ArrayListTest extends ListTest {

    @Override
    public List<Integer> create(final int numberOfInitalElements) {
        List<Integer> test = new ArrayList<>();
        for (int index = 0; index < numberOfInitalElements; index++){
            test.add(index);
        }
        return test;
    }
}
