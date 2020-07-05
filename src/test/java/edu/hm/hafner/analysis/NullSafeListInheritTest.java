package edu.hm.hafner.analysis;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Concrete class for testing {@link NullSafeListInherit}.
 *
 * @author S. A. D.
 */
class NullSafeListInheritTest extends AbstractNullSafeListTest {

    @Override
    NullSafeListInherit<Integer> create(final int numberOfInitialElements) {
        int[] s = new Random().ints(numberOfInitialElements, 0, 8).toArray();
        System.out.println(Arrays.stream(s).boxed().collect(Collectors.toList()));
        return new NullSafeListInherit<Integer>(Arrays.stream(s).boxed().collect(Collectors.toList()));

    }
}
