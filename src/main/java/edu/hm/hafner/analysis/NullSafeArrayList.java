package edu.hm.hafner.analysis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class NullSafeArrayList<T> extends ArrayList<T> implements Serializable {
    // Vorteil: Klasse übersichtlicher, da man nur Methoden in denen man Code anpassen muss überschreibt
    // Nachteil: man kann von keiner weiteren Klasse mehr erben

    public NullSafeArrayList(final Collection<? extends T> c) {
        super(containsNull(c));
    }

    @Override
    public T set(final int index, final T element) {
        Objects.requireNonNull(element);
        return super.set(index, element);
    }

    @Override
    public boolean add(final T t) {
        Objects.requireNonNull(t);
        return super.add(t);
    }

    @Override
    public void add(final int index, final T element) {
        Objects.requireNonNull(element);
        super.add(index, element);
    }

    @Override
    public boolean addAll(final Collection<? extends T> c) {
        containsNull(c);
        return super.addAll(c);
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends T> c) {
        containsNull(c);
        return super.addAll(index, c);
    }

    private static <T> Collection<T> containsNull(Collection<T> collection){
        collection.stream().peek(Objects::requireNonNull).count();
        return collection;
    }
}
