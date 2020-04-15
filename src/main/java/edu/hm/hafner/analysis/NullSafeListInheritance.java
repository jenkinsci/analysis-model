package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;

import static java.util.Objects.requireNonNull;

/**
 * Implementierung einer Null-Safe List mit Vererbung.
 *
 * Vorteil:
 * - es muessen nicht alle Methoden neu definiert werden
 *
 * Nachteil:
 * -  Verletzung des Liskovschen Substitutionsprinzips
 * -  Festsetzung auf ArrayList, keine dynamische Nutzung anderer List Implementierungen moeglich, kein Programming against Interfaces
 *
 * @param <T>
 *         Typparameter
 *
 * @author budelmann
 */
public class NullSafeListInheritance <T> extends ArrayList<T> {

    private static final long serialVersionUID = -3330498997754972315L;

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
