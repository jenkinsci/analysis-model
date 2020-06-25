package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import edu.umd.cs.findbugs.annotations.NonNull;

public class NullSafeListInherited<T> extends ArrayList<T> {

    /*
    Forteilt von Vererbung ist ich muss nur die Methoden implementieren / delegieren in denen ich was ändere
    Nachteil ist ich binde mit fest an eine Parantclass, in diesem Falle Arraylist
     */

    private void checkForNull(final Collection<? extends T> c){
        if(c.parallelStream().anyMatch(e -> e==null))
            throw new NullPointerException("Null in Collection");
    }

    @Override
    public boolean addAll(final Collection<? extends T> c) {
        checkForNull(c);
        return super.addAll(c);
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends T> c) {
        checkForNull(c);
        return super.addAll(index, c);
    }

    @Override
    public boolean add(@NonNull final T t) {
        Objects.requireNonNull(t);
        return super.add(t);
    }

    @Override
    public T set(final int index, @NonNull final T element) {
        Objects.requireNonNull(element);
        return super.set(index, element);
    }

    @Override
    public void add(final int index, @NonNull final T element) {
        Objects.requireNonNull(element);
        super.add(index, element);
    }
}
