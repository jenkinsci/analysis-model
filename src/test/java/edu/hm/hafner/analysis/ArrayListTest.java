package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

class ArrayListTest extends ListTest{
    @Override
    List<Integer> create(final int numberOfInitialElements) {
        List<Integer> sut = new ArrayList<>();
        IntStream.range(1, numberOfInitialElements + 1).forEach(sut::add);
        return sut;
    }
}