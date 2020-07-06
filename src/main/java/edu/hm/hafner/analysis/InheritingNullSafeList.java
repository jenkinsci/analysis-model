package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Eine Decorator-Klasse fuer die Klasse ArrayList, eine konkrete Implementierung des Interfaces List. Erlaubt den Wert
 * null nicht als Element.
 *
 * @param <E>
 *         Ein Typ.
 *
 * @author Andreas Kienle, akienle@hm.edu
 * @version Last modified on 06/07/2020
 * @see java.util.ArrayList
 */
public class InheritingNullSafeList<E> extends ArrayList<E> {
    /**
     * Serialisierungsversion.
     */
    private static final long serialVersionUID = -9116766197983747040L;

    @Override
    public boolean contains(final Object object) {
        Objects.requireNonNull(object);

        return super.contains(object);
    }

    @Override
    public int indexOf(final Object object) {
        Objects.requireNonNull(object);

        return super.indexOf(object);
    }

    @Override
    public int lastIndexOf(final Object object) {
        Objects.requireNonNull(object);

        return super.lastIndexOf(object);
    }

    @Override
    public E set(final int index, final E element) {
        Objects.requireNonNull(element);

        return super.set(index, element);
    }

    @Override
    public boolean add(final E element) {
        Objects.requireNonNull(element);

        return super.add(element);
    }

    @Override
    public void add(final int index, final E element) {
        Objects.requireNonNull(element);

        super.add(index, element);
    }

    @Override
    public boolean remove(final Object object) {
        Objects.requireNonNull(object);

        return super.remove(object);
    }

    @Override
    public boolean addAll(final Collection<? extends E> collection) {
        nullCheck(collection);

        return super.addAll(collection);
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends E> collection) {
        nullCheck(collection);

        return super.addAll(index, collection);
    }

    @Override
    public boolean removeAll(final Collection<?> collection) {
        nullCheck(collection);

        return super.removeAll(collection);
    }

    @Override
    public boolean retainAll(final Collection<?> collection) {
        nullCheck(collection);

        return super.retainAll(collection);
    }

    @Override
    public boolean containsAll(final Collection<?> collection) {
        nullCheck(collection);

        return super.containsAll(collection);
    }

    /**
     * Prueft eine uebergebene Collection auf null Elemente. Wirft eine Exception, falls die Collection null enthaelt.
     *
     * @param collection
     *         Collection, die ueberprueft werden soll.
     *
     * @throws NullPointerException
     *         falls ein Element der Collection null ist oder die Collection selbst null ist.
     */
    private void nullCheck(final Collection<?> collection) {
        Objects.requireNonNull(collection);

        if (collection.contains(null)) {
            throw new NullPointerException();
        }
    }
}
