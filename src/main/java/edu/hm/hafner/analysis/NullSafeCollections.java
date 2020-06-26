package edu.hm.hafner.analysis;

import java.util.Collection;
import java.util.List;

/**
 * Collection factory for NullSafeList
 * @author Fabian Diener
 */

public class NullSafeCollections {
    /**
     * creates an empty NullSafeList with initial capayity of 10.
     * @param <E>
     * @return a new nullSafeList
     */
    public static <E> List<E> emptyNullSafeList(){
        return new NullSafeList<>();
    }

    /**
     * creates an empty NullSafeList with a given capacity.
     * @param capacity
     * @param <E>
     * @return a new NullSafeList
     */
    public static <E> List<E> sizedNullSafeList(int capacity){
        return new NullSafeList<>(capacity);
    }

    /**
     * creates a NullSafeList with all Elements from the given collection.
     * @param collection
     * @param <E>
     * @return a new NullSafeList
     */
    public static <E> List<E> copyNullSafeList(Collection<? extends E> collection){
        return new NullSafeList<>(collection);
    }

}
