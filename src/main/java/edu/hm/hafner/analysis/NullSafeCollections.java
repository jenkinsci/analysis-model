package edu.hm.hafner.analysis;

import java.util.Collection;
import java.util.List;

/**
 * Utility Klasse zum Erstellen von NullSafe Objekten.
 *
 * @param <T>
 *         Typparameter
 *
 * @author budelmann
 */
public abstract class NullSafeCollections <T>{

    public static <T> List<T> nullSafeList(final List<T> list) {
        return new NullSafeList<>(list);
    }

    public static <T> List<T> nullSafeList(final List<T> list, final int initialCapacity) {
        return new NullSafeList<>(list);
    }

    public static <T> List<T> nullSafeList(final List<T> list, final Collection<? extends T> collection) {
        NullSafeList<T> nullSafeList = new NullSafeList<>(list);
        list.addAll(collection);
        return nullSafeList;
    }
}
