package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.hm.hafner.analysis.parser.NullSafeList;

/**
 * Collection Factory for NullSafeList.
 * @author Elena Lilova
 */
 class NullSafeCollection {

    /**
     * @param list to be added in the {@link ArrayList}.
     * @param <E> Collection
     * @return  {@link NullSafeList}
     */
     static <E> List<E> nullSafeList(Collection<? extends E> list) {
        return new NullSafeList<E>((new ArrayList<>(list)));
    }

    /**
     * @param initialCapacity Number of elements that should be added in the List.
     * @param <E> Collection
     * @return {@link NullSafeList}
     */
     static <E> List<E> nullSafeList(int initialCapacity) {
       return new NullSafeList<>(new ArrayList<>(initialCapacity));
    }

    /**
     * Default Constructor.
     * @param <E> Collection
     * @return {@link NullSafeList}
     */
     static <E> List<E> nullSafeList() {
      return new NullSafeList<>(new ArrayList<>());
    }
}
