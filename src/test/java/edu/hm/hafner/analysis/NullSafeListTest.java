package edu.hm.hafner.analysis;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Concrete class for testing {@link NullSafeList}.
 *
 * @author S. A. D.
 */
class NullSafeListTest extends AbstractNullSafeListTest {

    @Override
    NullSafeList<Integer> create(final int numberOfInitialElements) {
        List<Integer> randomNumbers = new Random()
                .ints(numberOfInitialElements, 0, 8)
                .boxed()
                .collect(Collectors.toList());
        return new NullSafeList(randomNumbers);
    }

}
