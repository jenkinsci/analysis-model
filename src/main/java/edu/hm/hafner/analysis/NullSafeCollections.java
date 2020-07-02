package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

public class NullSafeCollections {

    public static <T> NullSafeList<T> nullSafeList(final List<T> list){
        return new NullSafeList<>(list);
    }

    public static <T> NullSafeList<T> nullSafeList(final int length){
        return new NullSafeList<>(new ArrayList<>(length));
    }

    public static <T> NullSafeList<T> nullSafeList(){
        return new NullSafeList<>(new ArrayList<>());
    }
}
