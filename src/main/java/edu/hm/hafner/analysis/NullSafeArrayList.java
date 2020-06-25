package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * A NullSafeArrayList. No element in this ArrayList can be null.
 * @param <E>
 *
 * @author Thorsten Schartel
 */
public class NullSafeArrayList<E> extends ArrayList<E> {

    @Override
    public E set(final int index, final E element) {
        Objects.requireNonNull(element);
        return super.set(index, element);
    }

    @Override
    public boolean add(final E e) {
        Objects.requireNonNull(e);
        return super.add(e);
    }

    @Override
    public void add(final int index, final E element) {
        Objects.requireNonNull(element);
        super.add(index, element);
    }

    @Override
    public boolean addAll(final Collection<? extends E> c) {
        Objects.requireNonNull(c);
        for (E e : c) {
            Objects.requireNonNull(e);
        }
        return super.addAll(c);
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends E> c) {
        Objects.requireNonNull(c);
        for (E e : c) {
            Objects.requireNonNull(e);
        }
        return super.addAll(index, c);
    }
}
