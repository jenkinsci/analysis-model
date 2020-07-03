package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory for creating nullSafe lists
 *
 * @author Matthias König
 */

class NullSafeCollections {

    public <E> List<E> nullSafeList() {
        return new NullSafeList<>(new ArrayList<>());
    }

    public <E> List<E> nullSafeListWithInput(final List<E> inputList) {
        return new NullSafeList<>(inputList);
    }

    public <E> List<E> nullSafeListWithCapacity(final int initialCapacity) {
        return new NullSafeList<>(new ArrayList<>(initialCapacity));
    }
}