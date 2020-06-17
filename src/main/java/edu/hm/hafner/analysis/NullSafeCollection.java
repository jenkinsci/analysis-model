package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory for NullSafe Lists.
 * @author DJCoding
 */
class NullSafeCollection {

    public static <E> List<E> nullSafeList() {
        return new NullSafeList<>();
    }
    public static <E> List<E> nullSafeList(final List<E> list) {
        return new NullSafeList<>(list);
    }
    public static <E> ArrayList<E> nullSafeArrayList() {
        return new NullSafeArrayList<>();
    }
    public static <E> List<E> nullSafeList(final int i) {
        return new NullSafeList<>(i);
    }
}
