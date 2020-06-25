package edu.hm.hafner.analysis;

import java.util.List;
import java.util.stream.IntStream;

public class NullSafeArrayListTest extends NullSafeListTest {
    List<Integer> sut = new NullSafeArrayList<>();

    @Override
    List<Integer> create(final int numberOfInitialElements) {
        IntStream.range(0, numberOfInitialElements)
                .forEach(sut::add);
        return sut;
    }
}
