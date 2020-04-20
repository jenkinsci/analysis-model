package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

// Vorteile von Vererbung
// Wenig Implementierungsarbeit bzw. wenig Overhead
// Nachteile
// Gebunden an konkrete Implementierung der geerbten Klasse

/**
 * Implementation of NullSafe ArrayList.
 * @param <T> DataType of list.
 */
public class NullSafeList2<T> extends ArrayList<T> {
    private static final long serialVersionUID = 8988831990261565931L;

    @Override
    public T set(final int index, final T element) {
        return super.set(index, Objects.requireNonNull(element));
    }

    @Override
    public boolean add(final T t) {
        return super.add(Objects.requireNonNull(t));
    }

    @Override
    public void add(final int index, final T element) {
        super.add(index, Objects.requireNonNull(element));
    }

    @Override
    public boolean addAll(final Collection<? extends T> c) {
        checkList(c);
        return super.addAll(c);
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends T> c) {
        checkList(c);
        return super.addAll(index, c);
    }

    //Help Methods
    private void checkList(final Collection<? extends T> toCheck) {
        toCheck.forEach(Objects::requireNonNull);
    }
}
