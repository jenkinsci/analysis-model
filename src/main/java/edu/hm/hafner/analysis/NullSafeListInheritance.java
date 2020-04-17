package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.UnaryOperator;

/**
 * Other than {@link NullSafeList} this List version uses inheritance to implement a decorator pattern.
 * @param <T> Type of the Container.
 */
public class NullSafeListInheritance<T> extends ArrayList<T> {

    private static final long serialVersionUID = -3330498997754972315L;

    /**
     * Creates a new instance.
     */
    public NullSafeListInheritance() {
        super();
    }

    @Override
    public T set(final int index, final T element) {
        if (element == null) {
            throw new NullPointerException();
        }
        return super.set(index, element);
    }

    @Override
    public boolean add(final T t) {
        if (t == null) {
            throw new NullPointerException();
        }
        return super.add(t);
    }

    @Override
    public void add(final int index, final T element) {
        if (element == null) {
            throw new NullPointerException();
        }
        super.add(index, element);
    }

    @Override
    public boolean addAll(final Collection<? extends T> c) {
        if (c.contains(null)) {
            throw new NullPointerException();
        }
        return super.addAll(c);
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends T> c) {
        if (c.contains(null)) {
            throw new NullPointerException();
        }
        return super.addAll(index, c);
    }
}
