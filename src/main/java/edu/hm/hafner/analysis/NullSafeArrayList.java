package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class NullSafeArrayList<T> extends ArrayList<T> {

    @Override
    public boolean add(final T value)   {
        Objects.requireNonNull(value);
        return super.add(value);
    }

    @Override
    public void add(final int index, final T value)   {
        Objects.requireNonNull(value);
        super.add(index, value);
    }

    @Override
    public boolean addAll(final Collection<? extends T> collection) {
        Objects.requireNonNull(collection);
        if (collection.stream().anyMatch(Objects::isNull))   {
            throw new NullPointerException();
        }
        return super.addAll(collection);
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends T> collection) {
        Objects.requireNonNull(collection);
        if (collection.stream().anyMatch(Objects::isNull))   {
            throw new NullPointerException();
        }
        return super.addAll(index, collection);
    }

    @Override
    public T set(final int index, final T value)   {
        Objects.requireNonNull(value);
        return super.set(index, value);
    }
}
