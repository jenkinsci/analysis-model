package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

/**
 * A Class that provides null safe collections.
 */
public class NullSafeCollections {

    /**
     * Makes a given List null safe.
     * @param list the list that should be null safe.
     * @param <T> Type of the class.
     * @return a {@link NullSafeList}.
     */
    public static <T> NullSafeList<T> nullSafeList(final List<T> list) {
        return new NullSafeList<>(list);
    }

    /**
     * Returns an empty NullSafeList.
     * @param <T> Type of the Container.
     * @return an empty {@link NullSafeList}.
     */
    public static <T> NullSafeList<T> nullSafeList() {
        return new NullSafeList<>(new ArrayList<>());
    }

    /**
     * Returns a {@link NullSafeList} with a special initial capacity.
     * @param initialCapacity the initial capacity.
     * @param <T> The type of the Container.
     * @return a {@link NullSafeList}.
     */
    public static <T> NullSafeList<T> nullSafeList(final int initialCapacity) {
        return new NullSafeList<>(new ArrayList<>(initialCapacity));
    }
}
