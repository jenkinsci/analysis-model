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

// Vorteile von Delegation/Decorating
// Nicht gebunden an konkrete Implementierung eines Interfaces
// Nachteile
// Viel Boilerplate Code -> viel Overhead


/**
 * List which is nullsafe.
 * @param <T> datatype
 */
public class NullSafeList<T> implements List<T> {

    private final List<T> base;

    /**
     * Ctor for the base decorating list.
     *
     * @param toDecorate
     *         list to decorate.
     */
    public NullSafeList(final List<T> toDecorate) {
        Objects.requireNonNull(toDecorate);
        checkList(toDecorate);
        this.base = toDecorate;
    }

    //Overridden Methods

    @Override
    public boolean add(final T t) {
        return base.add(Objects.requireNonNull(t));
    }

    @Override
    public void add(final int index, final T element) {
        base.add(index, Objects.requireNonNull(element));
    }
    @Override
    public boolean addAll(final Collection<? extends T> c) {
        checkList(c);
        return base.addAll(c);
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends T> c) {
        checkList(c);
        return base.addAll(index, c);
    }

    @Override
    public T set(final int index, final T element) {
        return base.set(index, Objects.requireNonNull(element));
    }

    //Help Methods
    private void checkList(final Collection<? extends T> toCheck) {
        toCheck.forEach(Objects::requireNonNull);
    }

    //Delegate
    @Override
    public int size() {
        return base.size();
    }

    @Override
    public boolean isEmpty() {
        return base.isEmpty();
    }

    @Override
    public boolean contains(final Object o) {
        return contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return base.iterator();
    }

    @Override
    public Object[] toArray() {
        return base.toArray();
    }

    @Override
    public <I> I[] toArray(final I[] a) {
        return base.toArray(a);
    }

    @Override
    public boolean remove(final Object o) {
        return base.remove(o);
    }

    @Override
    public boolean containsAll(final Collection<?> c) {
        return base.containsAll(c);
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        return base.removeAll(c);
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        return base.retainAll(c);
    }

    @Override
    public void clear() {
        base.clear();
    }

    @Override
    public T get(final int index) {
        return base.get(index);
    }

    @Override
    public T remove(final int index) {
        return base.remove(index);
    }

    @Override
    public int indexOf(final Object o) {
        return base.indexOf(o);
    }

    @Override
    public int lastIndexOf(final Object o) {
        return base.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return base.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(final int index) {
        return base.listIterator(index);
    }

    @Override
    public List<T> subList(final int fromIndex, final int toIndex) {
        return base.subList(fromIndex, toIndex);
    }

    @Override
    public void replaceAll(final UnaryOperator<T> operator) {
        base.replaceAll(operator);
    }

    @Override
    public void sort(final Comparator<? super T> c) {
        base.sort(c);
    }

    @Override
    public Spliterator<T> spliterator() {
        return base.spliterator();
    }

    @Override
    public boolean removeIf(final Predicate<? super T> filter) {
        return base.removeIf(filter);
    }

    @Override
    public Stream<T> stream() {
        return base.stream();
    }

    @Override
    public Stream<T> parallelStream() {
        return base.parallelStream();
    }

    @Override
    public void forEach(final Consumer<? super T> action) {
        base.forEach(action);
    }
}
