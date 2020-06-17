package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * a delegated List. Cant be null.
 * @author DJCoding
 * @param <E>
 */
class NullSafeList<E> implements List<E> {

    /**
     *the delegate list.
     */
    private final List<E> delegate;

    /**
     * default delegate is ArrayList.
     */
    NullSafeList() {
        this.delegate = new ArrayList<>();
    }

    NullSafeList(final int i) {
        this.delegate = new ArrayList<>(i);
    }

    /**
     * uses the given argument list as delegate.
     * @param list the givenlist.
     */
    NullSafeList(final List<E> list) {
        Objects.requireNonNull(list);
        this.delegate = list;
    }

    @Override
    public int size() {
        return this.delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return this.delegate.isEmpty();
    }

    @Override
    public boolean contains(final Object o) {
        return this.delegate.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return this.delegate.iterator();
    }

    @Override
    public Object[] toArray() {
        return this.delegate.toArray();
    }

    @Override
    public <T> T[] toArray(final T[] ts) {
        return this.delegate.toArray(ts);
    }

    @Override
    public boolean add(final E e) {
        Objects.requireNonNull(e);
        return this.delegate.add(e);
    }

    @Override
    public boolean remove(final Object o) {
        return this.remove(o);
    }

    @Override
    public boolean containsAll(final Collection<?> collection) {
        return this.delegate.containsAll(collection);
    }

    @Override
    public boolean addAll(final Collection<? extends E> collection) {
        collection.forEach(Objects::requireNonNull);
        return this.delegate.addAll(collection);
    }

    @Override
    public boolean addAll(final int i, final Collection<? extends E> collection) {
        collection.forEach(Objects::requireNonNull);
        return this.delegate.addAll(i, collection);
    }

    @Override
    public boolean removeAll(final Collection<?> collection) {
        return this.delegate.removeAll(collection);
    }

    @Override
    public boolean retainAll(final Collection<?> collection) {
        return this.delegate.retainAll(collection);
    }

    @Override
    public void clear() {
        this.delegate.clear();
    }

    @Override
    public E get(final int i) {
        return this.delegate.get(i);
    }

    @Override
    public E set(final int i, final E e) {
        Objects.requireNonNull(e);
        return this.delegate.set(i, e);
    }

    @Override
    public void add(final int i, final E e) {
        Objects.requireNonNull(e);
        this.delegate.add(i, e);
    }

    @Override
    public E remove(final int i) {
        return this.delegate.remove(i);
   }

    @Override
    public int indexOf(final Object o) {
        return this.delegate.indexOf(o);
    }

    @Override
    public int lastIndexOf(final Object o) {
        return this.delegate.lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        return this.delegate.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(final int i) {
        return this.delegate.listIterator(i);
    }

    @Override
    public List<E> subList(final int i, final int i1) {
        return this.delegate.subList(i, i1);
    }

    @Override
    public void replaceAll(final UnaryOperator<E> unaryOperator) {
        delegate.replaceAll(unaryOperator);
    }

    @Override
    public void sort(final Comparator<? super E> comparator) {
        delegate.sort(comparator);
    }

    @Override
    public boolean equals(final Object o) {
        return delegate.equals(o);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public Spliterator<E> spliterator() {
        return delegate.spliterator();
    }

    @Override
    public boolean removeIf(final Predicate<? super E> predicate) {
        return delegate.removeIf(predicate);
    }

    @Override
    public Stream<E> stream() {
        return delegate.stream();
    }

    @Override
    public Stream<E> parallelStream() {
        return delegate.parallelStream();
    }

    @Override
    public void forEach(final Consumer<? super E> consumer) {
        delegate.forEach(consumer);
    }
}