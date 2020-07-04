package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for creating nullsafe collections.
 *
 * @author mbauerness
 */
public abstract class NullSafeCollections {

    private NullSafeCollections() {
        // implicit ctor to hide the public one
    }

    /**
     * Creates a new {@code NullSafeList} that wraps an empty {@code ArrayList}.
     *
     * @param <E>
     *              the type of the elements in the list
     *
     * @return
     *              an {@code ArrayList} wrapped in a {@code NullSafeList}
     */
    public static <E> List<E> nullSafeList() {
        return new NullSafeList<>(new ArrayList<>());
    }

    /**
     * Creates a new {@code NullSafeList} that wraps a {@code List}.
     *
     * @param list
     *              the list to wrap
     * @param <E>
     *              the type of the elements in the list
     * @return
     *              a {@code List} wrapped in a {@code NullSafeList}
     */
    public static <E> List<E> nullSafeList(final List<E> list) {
        return new NullSafeList<>(list);
    }

    /**
     * Creates a new {@code NullSafeList} that wraps an {@code ArrayList}.
     *
     * @param initialCapacity
     *                          the initial capacity of the list at the beginning
     * @param <E>
     *                          the type of the elements in the list
     * @return
     *                          an {@code ArrayList} wrapped in a {@code NullSafeList}
     */
    public static <E> List<E> nullSafeList(final int initialCapacity) {
        return new NullSafeList<>(new ArrayList<>(initialCapacity));
    }
}
