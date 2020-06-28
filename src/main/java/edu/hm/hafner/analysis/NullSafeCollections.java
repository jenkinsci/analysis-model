package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

public class NullSafeCollections {

    public static <T> List<T> nullSafeList()    {
        return new NullSafeList<>(new ArrayList<>());
    }

    public static <T> List<T> nullSafeList(final List<T> list)  {
        return new NullSafeList<>(list);
    }

    public static <T> List<T> nullSafeList(final int capacity)  {
        return new NullSafeList<>(new ArrayList<>(capacity));
    }
}
