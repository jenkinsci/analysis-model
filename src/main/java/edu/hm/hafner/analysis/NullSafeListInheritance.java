package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;

public class NullSafeListInheritance <T> extends ArrayList<T> {

    @Override
    public T set(final int i, final T e) {
        if(e == null)
            throw new NullPointerException();
        return super.set(i, e);
    }

    @Override
    public boolean add(final T e) {
        if(e == null)
            throw new NullPointerException();
        return super.add(e);
    }

    @Override
    public void add(final int i, final T e) {
        if(e == null)
            throw new NullPointerException();
        super.add(i, e);
    }

    @Override
    public boolean addAll(final Collection<? extends T> collection) {
        checkForNull(collection);
        return super.addAll(collection);
    }

    @Override
    public boolean addAll(final int i, final Collection<? extends T> collection) {
        checkForNull(collection);
        return super.addAll(i, collection);
    }

    private void checkForNull(final Collection<? extends T> collection) {
        for(T element : collection) {
            if (element == null) {
                throw new NullPointerException();
            }
        }
    }
}
