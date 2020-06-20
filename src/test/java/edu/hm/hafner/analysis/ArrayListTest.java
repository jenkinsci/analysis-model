package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ArrayListTest extends ListTest{
    @Override
    List<Integer> create(final int numberOfInitialElements) {
        List<Integer> sut = new ArrayList<>();
        IntStream.range(1, numberOfInitialElements + 1).forEach(sut::add);
        return sut;
    }
}
