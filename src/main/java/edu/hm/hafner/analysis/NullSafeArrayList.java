package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Decorator for a List witch excludes the "null" element
 * and extends directly from  {@link ArrayList}.
 *
 * @author Elena Lilova
 */

public class NullSafeArrayList<E>  extends ArrayList<E> {

    @Override
    public boolean add(final E element) {
        Objects.requireNonNull(element);
        return super.add(element);
    }

    @Override
    public void add(final int index, final E element) {
        Objects.requireNonNull(element);
        super.add(index, element);
    }

    @Override
    public boolean addAll(final Collection<? extends E> collection) {
        if (collection.contains(null)) {
            throw new NullPointerException();
        }
        else {
            return super.addAll(collection);
        }

    }

    @Override
    public boolean addAll(final int i, final Collection<? extends E> collection) {
        if (collection.contains(null)) {
            throw new NullPointerException();
        }
        else {
            return super.addAll(i, collection);
        }
    }
}
