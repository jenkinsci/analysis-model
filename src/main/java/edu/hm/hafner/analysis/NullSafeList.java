package edu.hm.hafner.analysis;

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

public class NullSafeList<T> implements List<T> {
    private final List<T> list;

    public NullSafeList(final List<T> list) {
        this.list = list;
    }

    private void checkForNull(final Collection<? extends T> c){
        if(c.parallelStream().anyMatch(e -> e==null))
            throw new NullPointerException("Null in Collection");
    }

    @Override
    public boolean addAll(final Collection<? extends T> c) {
        checkForNull(c);
        return list.addAll(c);
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends T> c) {
        checkForNull(c);
        return list.addAll(index, c);
    }

    @Override
    public boolean add(final T t) {
        Objects.requireNonNull(t);
        return list.add(t);
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
    public boolean remove(final Object o) {
        return list.remove(o);
    }

    @Override
    public boolean containsAll(final Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        return list.retainAll(c);
    }

    @Override
    public boolean removeIf(final Predicate<? super T> filter) {
        return list.removeIf(filter);
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
        return list.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(final int index) {
        return list.listIterator(index);
    }

    @Override
    public List<T> subList(final int fromIndex, final int toIndex) {
        return list.subList(fromIndex, toIndex);
    }

    @Override
    public Spliterator<T> spliterator() {
        return list.spliterator();
    }

    @Override
    public Stream<T> stream() {
        return list.stream();
    }

    @Override
    public Stream<T> parallelStream() {
        return list.parallelStream();
    }
}
