package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class NullSafeArrayListTest extends NullSafeListTest{
    @Override
    List<Integer> create(final int numberOfInitialElements) {
        List<Integer> list = new NullSafeArrayList<>();
        Stream.iterate(0, i -> i+1)
                .limit(numberOfInitialElements)
                .forEach(number -> list.add(number));
        return list;
    }
}
