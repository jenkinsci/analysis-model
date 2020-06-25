package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

public class NullSafeCollections {

    public static <T> List<T> nullSafeList(final List<T> list) {
        return new NullSafeList<T>(list);
    }

    public static <T> List<T> nullSafeList(final int initialCapacity) {
        return new NullSafeList<T>(new ArrayList<T>(initialCapacity));
    }

    public static <T> List<T> nullSafeList() {
        return new NullSafeListInherited<T>();
    }
}