package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Testclass for basic Arraylist operations.
 */
class ArrayListTest extends ListTest {
    @Override
    List<Integer> create(final int numberOfInitialElements) {
        List<Integer> sut = new ArrayList<>();
        IntStream.range(0, numberOfInitialElements)
                .forEach(sut::add);
        return sut;
    }
}
