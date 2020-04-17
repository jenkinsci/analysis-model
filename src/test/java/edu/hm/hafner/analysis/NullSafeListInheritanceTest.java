package edu.hm.hafner.analysis;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Test for {@link NullSafeListInheritance}.
 */
class NullSafeListInheritanceTest extends NullSafeListTest {

    @Override
    List<Integer> create(final int numberOfInitialElements) {
        return IntStream
                .generate(() -> new Random(100).nextInt())
                .limit(numberOfInitialElements)
                .boxed()
                .collect(Collectors.toCollection(NullSafeListInheritance::new));
    }
}
