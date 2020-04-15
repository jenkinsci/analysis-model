package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for null safe collections.
 */
public class NullSafeCollections {
    /**
     * Wrap an existing list to be a null safe list.
     *
     * @param list
     *         List to be wrapped.
     * @param <T>
     *         Type of the list's elements.
     *
     * @return a new {@link NullSafeList}.
     */
    public static <T> List<T> nullSafeList(final List<T> list) {
        return new NullSafeList<>(list);
    }

    /**
     * Create a new empty null safe list.
     *
     * @param <T>
     *         Type of the list's elements.
     *
     * @return a new {@link NullSafeList}.
     */
    public static <T> List<T> nullSafeList() {
        return new NullSafeList<>(new ArrayList<>());
    }

    /**
     * Create a new empty null safe list with an initial capacity.
     *
     * @param initialCapacity
     *         Initial capacity of the underlying {@link ArrayList}.
     * @param <T>
     *         Type of the list's elements.
     *
     * @return a new {@link NullSafeList}.
     */
    public static <T> List<T> nullSafeList(final int initialCapacity) {
        return new NullSafeList<>(new ArrayList<>(initialCapacity));
    }
}
