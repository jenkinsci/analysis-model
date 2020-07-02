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
 * Eine Liste, die keine null Elemente nimmt via delegation.
 * @author Michael Schober
 */
class NullSafeList<Integer> implements List<Integer> {

    /** Liste, an die Methoden delegiert werden. */
    private final List<Integer> list;

    NullSafeList(List<Integer> list){
        Objects.requireNonNull(list);
        this.list = list;
    }

    @Override
    public boolean add(final Integer integer) {
        Objects.requireNonNull(integer);
        return list.add(integer);
    }

    @Override
    public void add(final int index, final Integer integer) {
        Objects.requireNonNull(integer);
        list.add(index, integer);
    }

    @Override
    public boolean addAll(final Collection<? extends Integer> collection) {
        Objects.requireNonNull(collection);
        collection.forEach(Objects::requireNonNull);
        return list.addAll(collection);
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends Integer> collection) {
        Objects.requireNonNull(collection);
        collection.forEach(Objects::requireNonNull);
        return list.addAll(index, collection);
    }

    //===================Ab hier delgation ohne weitere Aenderung=========================
    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean contains(final Object o) {
        return list.contains(o);
    }

    @Override
    public Iterator<Integer> iterator() {
        return list.iterator();
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T> T[] toArray(final T[] ts) {
        return list.toArray(ts);
    }

    @Override
    public boolean remove(final Object o) {
        return list.remove(o);
    }

    @Override
    public boolean containsAll(final Collection<?> collection) {
        return list.containsAll(collection);
    }

    @Override
    public boolean removeAll(final Collection<?> collection) {
        return list.removeAll(collection);
    }

    @Override
    public boolean retainAll(final Collection<?> collection) {
        return list.retainAll(collection);
    }

    @Override
    public void replaceAll(final UnaryOperator<Integer> unaryOperator) {
        list.replaceAll(unaryOperator);
    }

    @Override
    public void sort(final Comparator<? super Integer> comparator) {
        list.sort(comparator);
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
    public Integer get(final int i) {
        return list.get(i);
    }

    @Override
    public Integer set(final int i, final Integer integer) {
        return list.set(i, integer);
    }

    @Override
    public Integer remove(final int i) {
        return list.remove(i);
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
    public ListIterator<Integer> listIterator() {
        return list.listIterator();
    }

    @Override
    public ListIterator<Integer> listIterator(final int i) {
        return list.listIterator(i);
    }

    @Override
    public List<Integer> subList(final int i, final int i1) {
        return list.subList(i, i1);
    }

    @Override
    public Spliterator<Integer> spliterator() {
        return list.spliterator();
    }
}
