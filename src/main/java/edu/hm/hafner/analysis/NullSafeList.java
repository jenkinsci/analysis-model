package edu.hm.hafner.analysis;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

public class NullSafeList<T> implements List<T> {
    private final List<T> list;

    public NullSafeList(final List<T> list) {
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
    public Iterator<T> iterator() {
        return list.iterator();
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T1> T1[] toArray(final T1[] a) {
        return list.toArray(a);
    }

    @Override
    public boolean add(final T t) {
        Objects.requireNonNull(t);
        return list.add(t);
    }

    @Override
    public boolean remove(final Object o) {
        return list.remove(o);
    }

    @Override
    public boolean containsAll(final Collection<?> c) {
        return list.contains(c);
    }

    @Override
    public boolean addAll(final Collection<? extends T> c) {
        Objects.requireNonNull(c);
        if (c.stream().anyMatch(Objects::isNull))   {
            throw new NullPointerException();
        }
        return list.addAll(c);
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends T> c) {
        Objects.requireNonNull(c);
        if (c.stream().anyMatch(Objects::isNull))   {
            throw new NullPointerException();
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
    public void clear() {
        list.clear();
    }

    @Override
    public T get(final int index) {
        return list.get(index);
    }

    @Override
    public T set(final int index, final T element) {
        Objects.requireNonNull(element);
        return list.set(index, element);
    }

    @Override
    public void add(final int index, final T element) {
        Objects.requireNonNull(element);
        list.add(index, element);
    }

    @Override
    public T remove(final int index) {
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
    public ListIterator<T> listIterator() {
        return listIterator();
    }

    @Override
    public ListIterator<T> listIterator(final int index) {
        return listIterator(index);
    }

    @Override
    public List<T> subList(final int fromIndex, final int toIndex) {
        return list.subList(fromIndex, toIndex);
    }
}
