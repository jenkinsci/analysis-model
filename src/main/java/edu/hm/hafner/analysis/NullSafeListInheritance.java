package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static java.util.Objects.*;

/**
 * Implementierung einer Null-Sage List mit Vererbung.
 *
 * Vorteil:
 * - es muessen nicht alle Methoden neu definiert werden
 *
 * Nachteil:
 * -  Verletzung des Liskovschen Substitutionsprinzips
 *
 * @param <T>
 *         Typparameter
 *
 * @author budelmann
 */
public class NullSafeListInheritance <T> extends ArrayList<T> {

    @Override
    public T set(final int i, final T e) {
        requireNonNull(e);
        return super.set(i, e);
    }

    @Override
    public boolean add(final T e) {
        requireNonNull(e);
        return super.add(e);
    }

    @Override
    public void add(final int i, final T e) {
        requireNonNull(e);
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

    /**
     * Ueberprueft eine Collection auf Null-Werte.
     *
     * @param collection
     *         Werte, welche in die Liste eingefuegt werden sollen.
     */
    private void checkForNull(final Collection<? extends T> collection) {
        for(T element : collection) {
            requireNonNull(element);
        }
    }
}
