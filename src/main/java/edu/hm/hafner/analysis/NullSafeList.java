package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

import edu.umd.cs.findbugs.annotations.NonNull;

public class NullSafeList<E> implements List<E> {
    private final List<E> list;

    public NullSafeList(final List<E> list) {
        checkIfContainsNull(list);
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
    @NonNull
    public Iterator<E> iterator() {
        return list.iterator();
    }

    @Override
    @NonNull
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    @NonNull
    public <T> T[] toArray(@NonNull final T[] a) {
        return list.toArray(a);
    }

    @Override
    public boolean add(final E e) {
        Objects.requireNonNull(e);
        return list.add(e);
    }

    @Override
    public boolean remove(final Object o) {
        return list.remove(o);
    }

    @Override
    public boolean containsAll(@NonNull final Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public boolean addAll(@NonNull final Collection<? extends E> c) {
        checkIfContainsNull(c);
        return list.addAll(c);
    }

    @Override
    public boolean addAll(final int index, @NonNull final Collection<? extends E> c) {
        checkIfContainsNull(c);
        return list.addAll(index, c);
    }

    @Override
    public boolean removeAll(@NonNull final Collection<?> c) {
        return list.removeAll(c);
    }

    @Override
    public boolean retainAll(@NonNull final Collection<?> c) {
        return list.retainAll(c);
    }

    @Override
    public void clear() {
        list.clear();
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
    @NonNull
    public ListIterator<E> listIterator() {
        return list.listIterator();
    }

    @Override
    @NonNull
    public ListIterator<E> listIterator(final int index) {
        return list.listIterator(index);
    }

    @Override
    @NonNull
    public List<E> subList(final int fromIndex, final int toIndex) {
        return list.subList(fromIndex, toIndex);
    }


    public void checkIfContainsNull(final Collection<? extends E> c) {

        if (c.stream().anyMatch(Objects::isNull)) {
            throw new NullPointerException();
        }
    }

}
