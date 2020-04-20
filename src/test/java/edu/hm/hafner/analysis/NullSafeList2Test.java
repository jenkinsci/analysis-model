package edu.hm.hafner.analysis;

import java.util.List;
import java.util.stream.Stream;

/**
 * Test class for NullSafeList2.
 */
public class NullSafeList2Test extends NullSafeListTest {

    @Override
    public List<Integer> create(final int numberOfElements) {
        List<Integer> list = new NullSafeList2<>();
        Stream.iterate(0, x -> x + 1).limit(numberOfElements).forEach(list::add);
        return list;
    }
}
