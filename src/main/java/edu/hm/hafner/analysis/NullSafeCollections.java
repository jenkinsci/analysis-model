package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Utility class for the creation of null safe collections.
 *
 * @author Daniel Soukup
 */
public abstract class NullSafeCollections {

    /**
     * Returns a new NullSafeList.
     *
     * @param list
     *         the delegate list
     * @param <T>
     *         type of element the list stores
     * @return the created NullSafeList
     */
    public static <T> List<T> nullSafeList(final List<T> list) {
        return new NullSafeList<>(list);
    }

    /**
     * Returns a new empty NullSafeList with given initialCapacity.
     *
     * @param initialCapacity
     *         the initial Capacity of the delegate ArrayList
     * @param <T>
     *         type of element the list stores
     * @return the created NullSafeList
     */
    public static <T> List<T> nullSafeList(final int initialCapacity) {
        return new NullSafeList<>(new ArrayList<>(initialCapacity));
    }

    /**
     * Copies a collection into a newly created NullSafeList.
     *
     * @param list
     *         the delegate list
     * @param that
     *         the collection the values are copied from
     * @param <T>
     *         type of element the list stores
     * @return the created NullSafeList
     */
    public static <T> List<T> nullSafeList(final List<T> list, final Collection<? extends T> that) {
        NullSafeList<T> resultList = new NullSafeList<>(list);
        resultList.addAll(that);
        return resultList;
    }

}
