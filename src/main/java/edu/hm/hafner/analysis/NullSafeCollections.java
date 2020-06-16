package edu.hm.hafner.analysis;

import java.util.List;

public class NullSafeCollections {
    
    public static <E> List<E> nullSafeList() {
        return new NullSafeList<>();
    }

    public static <E> List<E> nullSafeList(final List<E> list) {
        return new NullSafeList<>(list);
    }

    public static <E> List<E> nullSafeList(final int initialCapacity) {
        return new NullSafeArrayList<>(initialCapacity);
    }
}