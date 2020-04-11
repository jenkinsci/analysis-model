package edu.hm.hafner.analysis;

import java.util.Collection;
import java.util.List;

public class NullSafeCollections <T> {

    public static <T> List<T> nullSafeList(List<T> list) {
        return new NullSafeList<>(list);
    }

    public static <T> List<T> nullSafeList(List<T> list, int initialCapacity) {
        return new NullSafeList<>(list);
    }

    public static <T> List<T> nullSafeList(List<T> list, Collection<? extends T> collection) {
        NullSafeList<T> nullSafeList = new NullSafeList<>(list);
        list.addAll(collection);
        return nullSafeList;
    }
}
