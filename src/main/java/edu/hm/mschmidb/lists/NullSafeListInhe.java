package edu.hm.mschmidb.lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class NullSafeListInhe<T> extends ArrayList<T> {

    @Override
    public boolean add(final T t) {
        Objects.requireNonNull(t);
        return super.add(t);
    }

    @Override
    public boolean remove(final Object o) {
        Objects.requireNonNull(o);
        return super.remove(o);
    }

    @Override
    public boolean addAll(final Collection<? extends T> collection) {
        for(T item: collection) {
            Objects.requireNonNull(item);
        }
        return super.addAll(collection);
    }
    @Override

    public boolean addAll(final int i, final Collection<? extends T> collection) {
        for(T item: collection) {
            Objects.requireNonNull(item);
        }
        return super.addAll(i, collection);
    }

    @Override
    public void add(final int i, final T t) {
        Objects.requireNonNull(t);
        super.add(i, t);
    }

}
