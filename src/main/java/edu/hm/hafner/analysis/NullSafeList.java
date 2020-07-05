package edu.hm.hafner.analysis;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.UnaryOperator;

public class NullSafeList<E> implements List<E> {

    private final List<E> list;

    public NullSafeList(final List<E> list) {
        this.list = list;
    }

    @Override
    public E remove(final int i) {
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
    public ListIterator<E> listIterator() {
        return list.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(final int i) {
        return list.listIterator(i);
    }

    @Override
    public List<E> subList(final int i, final int i1) {
        return list.subList(i, i1);
    }

    @Override
    public Spliterator<E> spliterator() {
        return list.spliterator();
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
    public <T> T[] toArray(final T[] ts) {
        return list.toArray(ts);
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
    public boolean containsAll(final Collection<?> collection) {
        return list.containsAll(collection);
    }

    @Override
    public boolean addAll(final Collection<? extends E> collection) {
        checkObjectsRequireNonNull(collection);
        return list.addAll(collection);
    }

    @Override
    public boolean addAll(final int i, final Collection<? extends E> collection) {
        checkObjectsRequireNonNull(collection);
        return list.addAll(i, collection);
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
    public void replaceAll(final UnaryOperator<E> unaryOperator) {
        list.replaceAll(unaryOperator);
    }

    @Override
    public void sort(final Comparator<? super E> comparator) {
        list.sort(comparator);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public E get(final int i) {
        return list.get(i);
    }

    @Override
    public E set(final int i, final E e) {
        Objects.requireNonNull(e);
        return list.set(i, e);
    }

    @Override
    public void add(final int i, final E e) {
        Objects.requireNonNull(e);
        list.add(i, e);
    }

    private void checkObjectsRequireNonNull(final Collection<? extends E> that) {
        that.forEach(Objects::requireNonNull);
    }
}
