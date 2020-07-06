package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Creates null safe collections with the help of the simple factory method pattern.
 *
 * @author Tobias Karius
 */
public class NullSafeCollections {

    /**
     * Private constructor, so there cant be an object instantiated with the default contructor.
     */
    private NullSafeCollections() {

    }

    /**
     * The nullSafeList without any arguments returns a new NullSafeList of an empty ArrayList.
     *
     * @param <E> the element type of the list
     * @return a new NullSafeList of an empty ArrayList
     */
    public static <E> NullSafeList<E> nullSafeList() {
        return new NullSafeList<>(new ArrayList<>());
    }

    /**
     * The nullSafeList with a list as an argument.
     * the method transers the list to a {@link NullSafeList}
     *
     * @throws NullPointerException if the list is null or has any null values in it
     * @param list the original list transfered into a NullSafeList
     * @param <E> the element type of the list
     * @return a new NullSafeList
     */
    public static <E> NullSafeList<E> nullSafeList(final List<E> list) {
        Objects.requireNonNull(list);
        //this is needed, since the NullSafeList does not test for already existing elements in the list
        list.forEach(Objects::requireNonNull);
        return new NullSafeList<>(list);
    }

    /**
     * creates a new NullSafeList of an ArrayList with the specific number of initial elements.
     * @param numberOfInitialElements the size of the ArrayList/NullSafeList
     * @param <E> the element type of the list
     * @return a new NullSafeList
     */
    public static <E> NullSafeList<E> nullSafeList(final int numberOfInitialElements) {
        return new NullSafeList<>(new ArrayList<>(numberOfInitialElements));
    }
}
