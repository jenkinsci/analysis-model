package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Creates some null safe collections
 * @author Johannes Jäger
 */
public final class NullSafeCollections {

    private NullSafeCollections() {
    }

    public static <E> NullSafeList<E> nullSafeList() {
        return new NullSafeList(new ArrayList<>());
    }

    public static <E> NullSafeList<E> nullSafeList(List<E> list) {
        list.forEach(Objects::requireNonNull);
        return new NullSafeList(list);
    }

    public static <E> NullSafeList<E> nullSafeList(int initialCapacity) {
        return new NullSafeList(new ArrayList<>(initialCapacity));
    }

}
