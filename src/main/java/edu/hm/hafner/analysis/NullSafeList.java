package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.UnaryOperator;

/**
 * A NullSafeList. Elements in this List can't be null.
 *
 * @author Thorsten Schartel
 */
public class NullSafeList<E> implements List<E> {
    /**
     * A Delegate List.
     */
    private final List<E> list;

    /**
     * Constructor for a new NullSafeList.
     */
    NullSafeList() {
        list = new ArrayList<>();
    }

    /**
     * Constroctur for a new NullSafeList from another list.
     */
    NullSafeList(final List<E> list) {
        Objects.requireNonNull(list);
        for (E e : list) {
            Objects.requireNonNull(e);
        }
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
    public boolean contains(final Object o) {
        return list.contains(o);
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
    public <T> T[] toArray(final T[] a) {
        return list.toArray(a);
    }

    @Override
    public boolean add(final E E) {
        Objects.requireNonNull(E);
        return list.add(E);
    }

    @Override
    public boolean remove(final Object o) {
        return list.remove(o);
    }

    @Override
    public boolean containsAll(final Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public boolean addAll(final Collection<? extends E> c) {
        Objects.requireNonNull(c);
        for (E E : c) {
            Objects.requireNonNull(E);
        }
        return list.addAll(c);
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends E> c) {
        Objects.requireNonNull(c);
        for (E E : c) {
            Objects.requireNonNull(E);
        }
        return list.addAll(index, c);
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        return list.removeAll(c);
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        return list.retainAll(c);
    }

    @Override
    public void replaceAll(final UnaryOperator<E> operator) {
        list.replaceAll(operator);
    }

    @Override
    public void sort(final Comparator<? super E> c) {
        list.sort(c);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public boolean equals(final Object o) {
        return list.equals(o);
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
    public int indexOf(final Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(final Object o) {
        return list.lastIndexOf(o);
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

    @Override
    public Spliterator<E> spliterator() {
        return list.spliterator();
    }
}
