package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Factory Class for nullsafe collections.
 */
public class NullSafeCollections {

    /**
     * Wrap an existing list to be a null safe list.
     *
     * @param list
     *         List to be decorated.
     * @param <T>
     *         Type of the list's elements.
     *
     * @return a new {@link NullSafeList} of passed list.
     */
    public static <T> List<T> nullSafeList(final List<T> list) {
        return new NullSafeList<>(list);
    }

    /**
     * Create empty NullSafeList.
     * @param <T> type of null safe lists elements.
     * @return new empty NullSafeList.
     */
    public static <T> List<T> nullSafeList() {
        return new NullSafeList<>(new ArrayList<>());
    }

    /**
     * Create NullSafeList from existing Collections.
     * @param c Collection to decorate
     * @param <T> type of collections elements
     * @return new nullsafelist of existing collection.
     */
    public static <T> List<T> nullSafeList(final Collection<T> c) {
        return new NullSafeList<>(new ArrayList<>(c));
    }

    /**
     * Create NullSafeList from explicitly defined elements.
     * @param args data
     * @param <T> datatype
     * @return new list pre filled
     */
    public static <T> List<T> nullSafeListOf(final T... args) {
        List<T> l =  NullSafeCollections.nullSafeList(
                Arrays.stream(args).collect(Collectors.toList()));
        return l;
    }
}