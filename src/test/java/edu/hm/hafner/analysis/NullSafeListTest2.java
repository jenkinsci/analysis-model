package edu.hm.hafner.analysis;

import java.util.List;

/**
 * Tests for the {@link NullSafeList2}.
 */
class NullSafeListTest2 extends NullSafeListTest {
    @Override
    protected List<Integer> create(final int numberOfInitialElements) {
        final List<Integer> list = new NullSafeList2<>();
        for (int i = 0; i < numberOfInitialElements; i++) {
            list.add(i);
        }
        return list;
    }
}
