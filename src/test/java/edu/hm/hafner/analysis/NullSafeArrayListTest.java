package edu.hm.hafner.analysis;

import java.util.List;
import java.util.stream.Stream;

/**
 * The test class for the {@link NullSafeArrayList}.
 * extends the {@link NullSafeListTest}
 * @author Tobias Karius
 */
public class NullSafeArrayListTest extends NullSafeListTest {
    @Override
    List<Integer> create(final int numberOfInitialElements) {
        List<Integer> list = new NullSafeArrayList<>();
        Stream.iterate(0, i -> i + 1)
                .limit(numberOfInitialElements)
                .forEach(list::add);
        return list;
    }
}
