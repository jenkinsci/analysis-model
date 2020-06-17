package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * a Non Null arrayList.
 * @author DJCoding
 * @param <E> different types allowed.
 */
class NullSafeArrayList<E> extends ArrayList<E> {

    private static final long serialVersionUID = -5441395044719494577L;

    @Override
    public boolean add(final E e) {
        Objects.requireNonNull(e);
        return super.add(e);
    }

    @Override
    public void add(final int i, final E e) {
        Objects.requireNonNull(e);
        super.add(i, e);
    }

    @Override
    public E set(final int i, final E e) {
        Objects.requireNonNull(e);
        return super.set(i, e);
    }

    @Override
    public boolean addAll(final Collection<? extends E> collection) {
        collection.forEach(Objects::requireNonNull);
        return super.addAll(collection);
    }

    @Override
    public boolean addAll(final int i, final Collection<? extends E> collection) {
        collection.forEach(Objects::requireNonNull);
        return super.addAll(i, collection);
    }
}
