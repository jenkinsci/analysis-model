package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

/**
 * Nullsafe collections Factory creates NullSafe Lists.
 *
 * @author Thorsten Schartel
 */
class NullSafeCollection {
    /**
     * Creates a new nullSafeList.
     *
     * @param <E>
     *         the type parameter
     *
     * @return the new nullSafeList
     */
    public static <E> List<E> nullSafeList() {
        return new NullSafeList<>();
    }

    /**
     * Creates a new nullSafeList from another list.
     *
     * @param list
     *         the other list
     * @param <E>
     *         the type parameter
     *
     * @return the new nullSafeList
     */
    public static <E> List<E> nullSafeList(final List<E> list) {
        return new NullSafeList<>(list);
    }

    /**
     * Creates a new nullSafeArrayList.
     *
     * @param <E>
     *         the type parameter
     *
     * @return the new nullSafeArrayList
     */
    public static <E> ArrayList<E> nullSafeArrayList() {
        return new NullSafeArrayList<>();
    }
}
