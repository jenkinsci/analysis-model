package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

public class NullSafeCollections {

    public static <T> List<T> nullSaveList(){
        return new NullSafeList<>(new ArrayList<>());
    }

    public static <T> List<T> nullSaveList(List<T> list){
        if(! (list instanceof ArrayList<?>))
            throw new IllegalArgumentException();
        return new NullSafeList<>(list);
    }

    public static <T> List<T> nullSaveList(int initCapacity){
        return new NullSafeList<>(new ArrayList<>(initCapacity));
    }
}
