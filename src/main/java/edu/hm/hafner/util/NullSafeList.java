package edu.hm.hafner.util;

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

import edu.umd.cs.findbugs.annotations.NonNull;

public class NullSafeList<E> implements List<E> {
    private final List<E> delegate;

    public NullSafeList() {
        delegate = new ArrayList<>();
    }

    public NullSafeList(final List<E> list) {
        list.forEach(Objects::requireNonNull);

        delegate = list;
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
    @NonNull
    public Iterator<E> iterator() {
        return delegate.iterator();
    }

    @Override
    @NonNull
    public Object[] toArray() {
        return delegate.toArray();
    }

    @Override
    @NonNull
    public <T> T[] toArray(@NonNull final T[] a) {
        return delegate.toArray(a);
    }

    @Override
    public boolean add(final E e) {
        Objects.requireNonNull(e);

        return delegate.add(e);
    }

    @Override
    public void add(final int index, final E element) {
        Objects.requireNonNull(element);

        delegate.add(index, element);
    }

    @Override
    public boolean remove(final Object o) {
        return delegate.remove(o);
    }

    @Override
    public E remove(final int index) {
        return delegate.remove(index);
    }

    @Override
    public boolean containsAll(@NonNull final Collection<?> c) {
        return delegate.containsAll(c);
    }

    @Override
    public boolean addAll(@NonNull final Collection<? extends E> c) {
        c.forEach(Objects::requireNonNull);

        return delegate.addAll(c);
    }

    @Override
    public boolean addAll(final int index, @NonNull final Collection<? extends E> c) {
        Objects.requireNonNull(c);

        c.forEach(Objects::requireNonNull);

        return delegate.addAll(index, c);
    }

    @Override
    public boolean removeAll(@NonNull final Collection<?> c) {
        return delegate.removeAll(c);
    }

    @Override
    public boolean retainAll(@NonNull final Collection<?> c) {
        return delegate.retainAll(c);
    }

    @Override
    public void replaceAll(final UnaryOperator<E> operator) {
        delegate.forEach(o -> Objects.requireNonNull(operator.apply(o)));

        delegate.replaceAll(operator);
    }

    @Override
    public void sort(final Comparator<? super E> c) {
        delegate.sort(c);
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NullSafeList<?> that = (NullSafeList<?>) o;
        return delegate.equals(that.delegate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(delegate);
    }

    @Override
    public E get(final int index) {
        return delegate.get(index);
    }

    @Override
    public E set(final int index, final E element) {
        Objects.requireNonNull(element);

        return delegate.set(index, element);
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
    @NonNull
    public ListIterator<E> listIterator() {
        return delegate.listIterator();
    }

    @Override
    @NonNull
    public ListIterator<E> listIterator(final int index) {
        return delegate.listIterator(index);
    }

    @Override
    @NonNull
    public List<E> subList(final int fromIndex, final int toIndex) {
        return delegate.subList(fromIndex, toIndex);
    }

    @Override
    public Spliterator<E> spliterator() {
        return delegate.spliterator();
    }

    @Override
    public boolean removeIf(final Predicate<? super E> filter) {
        return delegate.removeIf(filter);
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
    public void forEach(final Consumer<? super E> action) {
        delegate.forEach(action);
    }
}
