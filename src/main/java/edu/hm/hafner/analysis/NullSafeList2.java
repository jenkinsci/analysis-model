package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * A list that cannot store null values.
 * <p>
 * This implementation uses inheritance
 * <p>
 * Vorteile Vererbung:
 *      - Nur geänderte Methoden tauchen im Quellcode auf, dadurch übersichtlicher
 * <p>
 * Nachteile:
 *      - Kopplung an konkreten Type ArrayList
 *      - Aufbrechen von Encapsulation (z.B. Methode ruft andere eigene
 *        Methode auf, die überschrieben wurde)
 * </p>
 *
 * @param <T>
 *         Type of the list's elements.
 */
public class NullSafeList2<T> extends ArrayList<T> {
    private static final long serialVersionUID = -1166656596703491146L;

    @Override
    public T set(final int index, final T value) {
        return super.set(index, Objects.requireNonNull(value));
    }

    @Override
    public void add(final int index, final T value) {
        super.add(index, Objects.requireNonNull(value));
    }

    @Override
    public boolean add(final T value) {
        return super.add(Objects.requireNonNull(value));
    }

    @Override
    public boolean addAll(final Collection<? extends T> collection) {
        if (collection.stream().anyMatch(Objects::isNull)) {
            throw new NullPointerException("Cannot add collection containing null values.");
        }
        return super.addAll(collection);
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends T> collection) {
        if (collection.stream().anyMatch(Objects::isNull)) {
            throw new NullPointerException("Can not add collection containing null values.");
        }
        return super.addAll(index, collection);
    }
}
