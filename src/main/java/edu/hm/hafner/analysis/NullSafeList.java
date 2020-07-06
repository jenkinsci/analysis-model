package edu.hm.hafner.analysis;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

/**
 * Ein Decorator fuer konkrete Implementierungen des Interfaces List. Erlaubt den Wert null nicht als Element.
 *
 * @param <E>
 *         Ein Typ.
 *
 * @author Andreas Kienle, akienle@hm.edu
 * @version Last modified on 06/07/2020
 * @see java.util.List
 */
public class NullSafeList<E> implements List<E> {
    /**
     * Liste, an welche alle Methodenaufrufe delegiert werden.
     */
    private final List<E> list;

    /**
     * Konstruktor der Klasse NullSafeList.
     *
     * @param list
     *         Liste, an die alle Methodenaufrufe delegiert werden.
     *
     * @throws NullPointerException
     *         falls die uebergebene Liste null ist oder den Wert null als Element enthaelt.
     */
    public NullSafeList(final List<E> list) {
        nullCheck(list);

        this.list = list;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(final Object object) {
        Objects.requireNonNull(object);

        return list.contains(object);
    }

    @Override
    public Iterator<E> iterator() {
        return list.iterator();
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T> T[] toArray(final T[] array) {
        return list.toArray(array);
    }

    @Override
    public boolean add(final E element) {
        Objects.requireNonNull(element);

        return list.add(element);
    }

    @Override
    public boolean remove(final Object object) {
        Objects.requireNonNull(object);

        return list.remove(object);
    }

    @Override
    public boolean containsAll(final Collection<?> collection) {
        nullCheck(collection);

        return list.containsAll(collection);
    }

    @Override
    public boolean addAll(final Collection<? extends E> collection) {
        nullCheck(collection);

        return list.addAll(collection);
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends E> collection) {
        nullCheck(collection);

        return list.addAll(index, collection);
    }

    @Override
    public boolean removeAll(final Collection<?> collection) {
        nullCheck(collection);

        return list.removeAll(collection);
    }

    @Override
    public boolean retainAll(final Collection<?> collection) {
        nullCheck(collection);

        return list.retainAll(collection);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public boolean equals(final Object object) {
        return list.equals(object);
    }

    @Override
    public int hashCode() {
        return list.hashCode();
    }

    @Override
    public E get(final int index) {
        return list.get(index);
    }

    @Override
    public E set(final int index, final E element) {
        Objects.requireNonNull(element);

        return list.set(index, element);
    }

    @Override
    public void add(final int index, final E element) {
        Objects.requireNonNull(element);

        list.add(index, element);
    }

    @Override
    public E remove(final int index) {
        return list.remove(index);
    }

    @Override
    public int indexOf(final Object object) {
        Objects.requireNonNull(object);

        return list.indexOf(object);
    }

    @Override
    public int lastIndexOf(final Object object) {
        Objects.requireNonNull(object);

        return list.lastIndexOf(object);
    }

    @Override
    public ListIterator<E> listIterator() {
        return list.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(final int index) {
        return list.listIterator(index);
    }

    @Override
    public List<E> subList(final int fromIndex, final int toIndex) {
        return list.subList(fromIndex, toIndex);
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
