package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * The Inheritance of the NullSafeList with extending the ArrayList.
 * @param <E> the element type of the list
 * @author Tobias Karius
 */
public class NullSafeArrayList<E> extends ArrayList<E> {

    private static final long serialVersionUID = -6970452305270898333L;

    @Override
    public E set(final int i, final E e) {
        Objects.requireNonNull(e);
        return super.set(i, e);
    }

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
    public boolean addAll(final Collection<? extends E> collection) {
        checkObjectsRequireNonNull(collection);
        return super.addAll(collection);
    }

    @Override
    public boolean addAll(final int i, final Collection<? extends E> collection) {
        checkObjectsRequireNonNull(collection);
        return super.addAll(i, collection);
    }

    private void checkObjectsRequireNonNull(final Collection<? extends E> that) {
        that.forEach(Objects::requireNonNull);
    }
}
