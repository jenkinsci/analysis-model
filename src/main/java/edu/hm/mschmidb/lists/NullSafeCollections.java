package edu.hm.mschmidb.lists;

import java.util.Collection;
import java.util.List;

public class NullSafeCollections {

    public static <T> List<T> empty() {
        return new NullSafeListComp<>();
    }

    public static <T> List<T> withSize(int size) {
        return new NullSafeListComp<>(size);
    }

    public static <T> List<T> fromCollection(Collection<? extends T> col) {
        return new NullSafeListComp<>(col);
    }
}
