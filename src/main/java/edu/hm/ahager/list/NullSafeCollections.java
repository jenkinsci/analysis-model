package edu.hm.ahager.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NullSafeCollections {

    public static <T> List<T> emptyNullSaveList() {
        return new NullSafeList<>(new ArrayList<>());
    }

    public static <T> List<T> nullSaveList(final Collection<? extends T> initialElements) {
        return new NullSafeList<>(new ArrayList<>(initialElements));
    }

    public static <T> List<T> copyNullSaveList(final Collection<? extends T> that) {
        final List<T> newList = emptyNullSaveList();
        if (!newList.addAll(that)) {
            throw new IllegalStateException("Something went wrong");
        }
        return newList;
    }
}
