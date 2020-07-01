package edu.hm.hafner.analysis;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class NullSafeList<T> implements List<T> {
    private List<T> innerList;

    public NullSafeList(final List<T> list){
        innerList = list;
    }

    @Override
    public int size() {
        return innerList.size();
    }

    @Override
    public boolean isEmpty() {
        return innerList.isEmpty();
    }

    @Override
    public boolean contains(final Object o) {
        return innerList.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return innerList.iterator();
    }

    @Override
    public Object[] toArray() {
        return innerList.toArray();
    }

    @Override
    public <T1> T1[] toArray(final T1[] t1s) {
        return innerList.toArray(t1s);
    }

    @Override
    public boolean add(final T t) {
        if (t == null)
            throw new NullPointerException("Null is not an accepted value for an element.");
        return innerList.add(t);
    }

    @Override
    public boolean remove(final Object o) {
        return innerList.remove(o);
    }

    @Override
    public boolean containsAll(final Collection<?> collection) {
        return innerList.containsAll(collection);
    }

    @Override
    public boolean addAll(final Collection<? extends T> collection) {
        for (T t : collection)
            if (t == null)
                throw new NullPointerException("Null is not an accepted value for an element.");
        return innerList.addAll(collection);
    }

    @Override
    public boolean addAll(final int i, final Collection<? extends T> collection) {
        for (T t : collection)
            if (t == null)
                throw new NullPointerException("Null is not an accepted value for an element.");
        return innerList.addAll(i, collection);
    }

    @Override
    public boolean removeAll(final Collection<?> collection) {
        return innerList.removeAll(collection);
    }

    @Override
    public boolean retainAll(final Collection<?> collection) {
        return innerList.retainAll(collection);
    }

    @Override
    public void replaceAll(final UnaryOperator<T> unaryOperator) {
        innerList.replaceAll(unaryOperator);
    }

    @Override
    public void sort(final Comparator<? super T> comparator) {
        innerList.sort(comparator);
    }

    @Override
    public void clear() {
        innerList.clear();
    }

    @Override
    public boolean equals(final Object o) {
        return innerList.equals(o);
    }

    @Override
    public int hashCode() {
        return innerList.hashCode();
    }

    @Override
    public T get(final int i) {
        return innerList.get(i);
    }

    @Override
    public T set(final int i, final T t) {
        if (t == null)
            throw new NullPointerException("Null is not an accepted value for an element.");
        return innerList.set(i, t);
    }

    @Override
    public void add(final int i, final T t) {
        if (t == null)
            throw new NullPointerException("Null is not an accepted value for an element.");
        innerList.add(i, t);
    }

    @Override
    public T remove(final int i) {
        return innerList.remove(i);
    }

    @Override
    public int indexOf(final Object o) {
        return innerList.indexOf(o);
    }

    @Override
    public int lastIndexOf(final Object o) {
        return innerList.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return innerList.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(final int i) {
        return innerList.listIterator(i);
    }

    @Override
    public List<T> subList(final int i, final int i1) {
        return innerList.subList(i, i1);
    }

    @Override
    public Spliterator<T> spliterator() {
        return innerList.spliterator();
    }

    @Override
    public boolean removeIf(final Predicate<? super T> predicate) {
        return innerList.removeIf(predicate);
    }

    @Override
    public Stream<T> stream() {
        return innerList.stream();
    }

    @Override
    public Stream<T> parallelStream() {
        return innerList.parallelStream();
    }

    @Override
    public void forEach(final Consumer<? super T> consumer) {
        innerList.forEach(consumer);
    }
}
