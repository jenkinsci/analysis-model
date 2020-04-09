package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NullSafeCollections<E> {
    public static List nullSafeList(final List list) {
        return new NullSafeList<>(list);
    }

    public static List nullSafeList() {
        return new NullSafeList<>(new ArrayList());
    }

    public static List nullSafeList(Collection c) {
        return new NullSafeList<>(new ArrayList(c));
    }
}
