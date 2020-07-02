package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;

public class InheritedNullSafeList<T> extends ArrayList<T> {

    public InheritedNullSafeList() {
        super();
    }

    public InheritedNullSafeList(int i) {
        super(i);
    }

    public InheritedNullSafeList(final Collection<? extends T> collection) {
        addAll(collection);
    }

    @Override
    public T set(final int i, final T t) {
        if (t == null)
            throw new NullPointerException("Null is not an accepted value for an element.");
        return super.set(i, t);
    }

    @Override
    public boolean add(final T t) {
        if (t == null)
            throw new NullPointerException("Null is not an accepted value for an element.");
        return super.add(t);
    }

    @Override
    public void add(final int i, final T t) {
        if (t == null)
            throw new NullPointerException("Null is not an accepted value for an element.");
        super.add(i, t);
    }

    @Override
    public boolean addAll(final Collection<? extends T> collection) {
        for (T t : collection) {
            if (t == null)
                throw new NullPointerException("Null is not an accepted value for an element.");
        }
        return super.addAll(collection);
    }

    @Override
    public boolean addAll(final int i, final Collection<? extends T> collection) {
        for (T t : collection) {
            if (t == null)
                throw new NullPointerException("Null is not an accepted value for an element.");
        }
        return super.addAll(i, collection);
    }
}
