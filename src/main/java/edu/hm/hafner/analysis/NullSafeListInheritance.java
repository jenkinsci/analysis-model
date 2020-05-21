package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * A list which trows a NPE when attempted to store a null value.
 *
 * This implementation is done through inheritance.
 *
 * Vererbung:
 *
 * Vorteile: - Nicht jede Methode muss delegiert werden. Änderungen sind deshalb übersichtlicher
 *
 * Nachteile: - Bindung an konkrete Klasse ArrayList
 *
 * @param <T>
 *      type of element the list stores
 *
 * @author Daniel Soukup
 */
public class NullSafeListInheritance<T> extends ArrayList<T> {
    private static final long serialVersionUID = 8836356389637173921L;

    /**
     * Creates a new {@link NullSafeListInheritance} object and initializes it with a free of null values collection.
     *
     * The initializing collection isn't allowed to store null values else a NPE will be thrown.
     *
     * @param collection
     *          the concrete list type one wants to use internally to store the elements
     */
    public NullSafeListInheritance(final Collection<? extends T> collection) {
        super(collection);
        for (T element : collection) {
            Objects.requireNonNull(element);
        }
    }

    @Override
    public boolean add(final T t) {
        Objects.requireNonNull(t);
        return super.add(t);
    }

    @Override
    public void add(final int i, final T t) {
        Objects.requireNonNull(t);
        super.add(i, t);
    }

    @Override
    public boolean addAll(final Collection<? extends T> collection) {
        for (T t : collection) {
            Objects.requireNonNull(t);
        }
        return super.addAll(collection);
    }

    @Override
    public boolean addAll(final int i, final Collection<? extends T> collection) {
        for (T t : collection) {
            Objects.requireNonNull(t);
        }
        return super.addAll(i, collection);
    }

    @Override
    public T set(final int i, final T t) {
        Objects.requireNonNull(t);
        return super.set(i, t);
    }
}
