package edu.hm.hafner.analysis;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.UnaryOperator;

/**
 *
 * Decorator for a null-safe list
 *
 * @author Matthias König
 */

public class NullSafeList<Integer> implements List<Integer> {

    private final List<Integer> nullSafeList;

    public NullSafeList(final List<Integer> inputList) {
        if (inputList.contains(null)) {
            throw new NullPointerException("not allowed: the list contains or is null");
        }
        this.nullSafeList = inputList;
    }

    @Override
    public int size() {
        return nullSafeList.size();
    }

    @Override
    public boolean isEmpty() {
        return nullSafeList.isEmpty();
    }

    @Override
    public boolean contains(final Object o) {
        return nullSafeList.contains(o);
    }

    @Override
    public Iterator<Integer> iterator() {
        return nullSafeList.iterator();
    }

    @Override
    public Object[] toArray() {
        return nullSafeList.toArray();
    }

    @Override
    public <T> T[] toArray(final T[] a) {
        return nullSafeList.toArray(a);
    }

    @Override
    public boolean add(final Integer integer) {
        if(integer == null) {
            throw new NullPointerException("not allowed: you want to add null to this list");
        }
        return nullSafeList.add(integer);
    }

    @Override
    public boolean remove(final Object o) {
        return nullSafeList.remove(o);
    }

    @Override
    public boolean containsAll(final Collection<?> c) {
        return nullSafeList.containsAll(c);
    }

    @Override
    public boolean addAll(final Collection<? extends Integer> c) {
        if (c.contains(null)) {
            throw new NullPointerException("not allowed: your collection you want to add contains null");
        }
        return nullSafeList.addAll(c);
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends Integer> c) {
        if (c.contains(null)) {
            throw new NullPointerException("not allowed: your collection you want to add contains null");
        }
        return nullSafeList.addAll(index, c);
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        return nullSafeList.removeAll(c);
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        return nullSafeList.retainAll(c);
    }

    @Override
    public void replaceAll(final UnaryOperator<Integer> operator) {
        nullSafeList.replaceAll(operator);
    }

    @Override
    public void sort(final Comparator<? super Integer> c) {
        nullSafeList.sort(c);
    }

    @Override
    public void clear() {
        nullSafeList.clear();
    }

    @Override
    public boolean equals(final Object o) {
        return nullSafeList.equals(o);
    }

    @Override
    public int hashCode() {
        return nullSafeList.hashCode();
    }

    @Override
    public Integer get(final int index) {
        return nullSafeList.get(index);
    }

    @Override
    public Integer set(final int index, final Integer element) {
        if (element == null) {
            throw new NullPointerException("not allowed: your element you want to add is null");
        }
        return nullSafeList.set(index, element);
    }

    @Override
    public void add(final int index, final Integer element) {
        if (element == null) {
            throw new NullPointerException("not allowed: you want to add null to this list");
        }
        nullSafeList.add(index, element);
    }

    @Override
    public Integer remove(final int index) {
        return nullSafeList.remove(index);
    }

    @Override
    public int indexOf(final Object o) {
        return nullSafeList.indexOf(o);
    }

    @Override
    public int lastIndexOf(final Object o) {
        return nullSafeList.lastIndexOf(o);
    }

    @Override
    public ListIterator<Integer> listIterator() {
        return nullSafeList.listIterator();
    }

    @Override
    public ListIterator<Integer> listIterator(final int index) {
        return nullSafeList.listIterator(index);
    }

    @Override
    public List<Integer> subList(final int fromIndex, final int toIndex) {
        return nullSafeList.subList(fromIndex, toIndex);
    }

    @Override
    public Spliterator<Integer> spliterator() {
        return nullSafeList.spliterator();
    }
}