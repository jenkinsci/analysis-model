package edu.hm.hafner.util;

import java.util.Collections;
import java.util.Iterator;

/**
 * Provides a simple access to the element of a singleton collection.
 *
 * @see Collections#singleton(Object)
 * @see Collections#singletonList(Object)
 * @author Ullrich Hafner
 */
public final class Singleton {
    /**
     * Returns the element of the singleton collection.
     *
     * @param collection
     *            the singleton collection to get the element from
     * @return the element of the singleton collection
     * @param <T> the type of the element
     * @throws AssertionError
     *             if the collection does not contain exactly one element
     */
    public static <T> T get(final Iterable<T> collection) {
        Ensure.that(collection).isNotNull();
        Iterator<T> iterator = collection.iterator();
        Ensure.that(iterator.hasNext()).isTrue("Collection is empty.");

        T element = iterator.next();

        Ensure.that(iterator.hasNext()).isFalse("Collection contains more than one element.");
        Ensure.that(element).isNotNull("Element in collection must be not NULL.");

        return element;
    }

    private Singleton() {
        // prevents instantiation
    }
}

