package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import org.apache.commons.lang.ObjectUtils.Null;

/**
 * A List implementation that is null safe. Takes an ArrayList and makes it Null Safe.
 * @param <T> Type of the elements stored in the List.
 */
public class NullSafeList<T> implements List<T> {
    private List<T> delegate;

    /**
     * Creates a new NullSafeList.
     * @param delegate the List that should be null safe.
     */
    public NullSafeList(final List<T> delegate) {
        if (delegate.contains(null)) {
            throw new NullPointerException();
        }
        this.delegate = delegate;
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public boolean contains(final Object o) {
        return delegate.contains(o);
    }

    @Override
    public int indexOf(final Object o) {
        return delegate.indexOf(o);
    }

    @Override
    public int lastIndexOf(final Object o) {
        return delegate.lastIndexOf(o);
    }

    @Override
    public Object[] toArray() {
        return delegate.toArray();
    }

    @Override
    public <B1> B1[] toArray(final B1[] b1s) {
        return delegate.toArray(b1s);
    }

    @Override
    public T get(final int index) {
        return delegate.get(index);
    }

    @Override
    public T set(final int index, final T element) {
        if (element == null) {
            throw new NullPointerException();
        }
        return delegate.set(index, element);
    }

    @Override
    public boolean add(final T t) {
        if (t == null) {
            throw new NullPointerException();
        }
        return delegate.add(t);
    }

    @Override
    public void add(final int index, final T element) {
        if (element == null) {
            throw new NullPointerException();
        }
        delegate.add(index, element);
    }

    @Override
    public T remove(final int index) {
        return delegate.remove(index);
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
    public boolean remove(final Object o) {
        return delegate.remove(o);
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public boolean addAll(final Collection<? extends T> c) {
        if (c.contains(null)) {
            throw new NullPointerException();
        }
        return delegate.addAll(c);
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends T> c) {
        if (c.contains(null)) {
            throw new NullPointerException();
        }
        return delegate.addAll(index, c);
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        return delegate.removeAll(c);
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        return delegate.retainAll(c);
    }

    @Override
    public ListIterator<T> listIterator(final int index) {
        return delegate.listIterator(index);
    }

    @Override
    public ListIterator<T> listIterator() {
        return delegate.listIterator();
    }

    @Override
    public Iterator<T> iterator() {
        return delegate.iterator();
    }

    @Override
    public List<T> subList(final int fromIndex, final int toIndex) {
        return delegate.subList(fromIndex, toIndex);
    }

    @Override
    public void forEach(final Consumer<? super T> action) {
        delegate.forEach(action);
    }

    @Override
    public Spliterator<T> spliterator() {
        return delegate.spliterator();
    }

    @Override
    public boolean removeIf(final Predicate<? super T> filter) {
        return delegate.removeIf(filter);
    }

    @Override
    public void replaceAll(final UnaryOperator<T> operator) {
        delegate.replaceAll(operator);
    }

    @Override
    public void sort(final Comparator<? super T> c) {
        delegate.sort(c);
    }

    @Override
    public boolean containsAll(final Collection<?> c) {
        return delegate.containsAll(c);
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

    @Override
    public Stream<T> stream() {
        return delegate.stream();
    }

    @Override
    public Stream<T> parallelStream() {
        return delegate.parallelStream();
    }
}
