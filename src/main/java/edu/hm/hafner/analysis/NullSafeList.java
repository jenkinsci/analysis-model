package edu.hm.hafner.analysis;

import java.io.Serializable;
import java.util.AbstractList;
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

import edu.hm.hafner.util.VisibleForTesting;

public class NullSafeList<T> extends AbstractList<T> implements List<T>, Serializable {

    private List<T> nullSafe;

    @VisibleForTesting
    public NullSafeList(T number) {
        Objects.requireNonNull(number);
        nullSafe = new ArrayList<>();
        nullSafe.add(number);
    }
    /* Default List für NullSafeList ist ArrayList */
    public NullSafeList() {
        nullSafe = new ArrayList<>();
    }

    public NullSafeList(int capacity) {
        nullSafe = new ArrayList<>(capacity);
    }

    public NullSafeList(List<T> list) {
        containsNull(list);
        nullSafe = list;
    }

    @Override
    public int size() {
        return nullSafe.size();
    }

    @Override
    public boolean isEmpty() {
        return nullSafe.isEmpty();
    }

    @Override
    public boolean contains(final Object o) {
        return nullSafe.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return nullSafe.iterator();
    }

    @Override
    public Object[] toArray() {
        return nullSafe.toArray();
    }

    @Override
    public <T> T[] toArray(final T[] a) {
        return nullSafe.toArray(a);
    }

    public boolean add(final T element) {
        Objects.requireNonNull(element);
        return nullSafe.add(element);
    }

    @Override
    public boolean remove(final Object o) {
        return nullSafe.remove(o);
    }

    public boolean containsAll(final Collection<?> c) {
        return nullSafe.containsAll(c);
    }

    @Override
    public boolean addAll(final Collection<? extends T> c) {
        containsNull(c);
        return nullSafe.addAll(c);
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends T> c) {
        containsNull(c);
        return nullSafe.addAll(index, c);
    }

    public boolean removeAll(final Collection<?> c) {
        return nullSafe.removeAll(c);
    }

    public boolean retainAll(final Collection<?> c) {
        return nullSafe.retainAll(c); // retainAll wirft npe falls null in c enthalten ist
    }

    public void replaceAll(final UnaryOperator<T> operator) {
        nullSafe.replaceAll(operator);
    }

    public void sort(final Comparator<? super T> c) {
        nullSafe.sort(c);
    }

    @Override
    public void clear() {
        nullSafe.clear();
    }

    @Override
    public boolean equals(final Object o) {
        return nullSafe.equals(o);
    }

    @Override
    public int hashCode() {
        return nullSafe.hashCode();
    }

    @Override
    public T get(final int index) {
        return nullSafe.get(index);
    }

    public T set(final int index, final T element) {
        Objects.requireNonNull(element);
        return nullSafe.set(index, element);
    }

    public void add(final int index, final T element) {
        Objects.requireNonNull(element);
        nullSafe.add(index, element);
    }

    @Override
    public T remove(final int index) {
        return nullSafe.remove(index);
    }

    @Override
    public int indexOf(final Object o) {
        return nullSafe.indexOf(o);
    }

    @Override
    public int lastIndexOf(final Object o) {
        return nullSafe.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return nullSafe.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(final int index) {
        return nullSafe.listIterator(index);
    }

    @Override
    public List<T> subList(final int fromIndex, final int toIndex) {
        return nullSafe.subList(fromIndex, toIndex);
    }

    @Override
    public Spliterator<T> spliterator() {
        return nullSafe.spliterator();
    }

    public boolean removeIf(final Predicate<? super T> filter) {
        return nullSafe.removeIf(filter);
    }

    @Override
    public Stream<T> stream() {
        return nullSafe.stream();
    }

    @Override
    public Stream<T> parallelStream() {
        return nullSafe.parallelStream();
    }

    public void forEach(final Consumer<? super T> action) {
        nullSafe.forEach(action);
    }

    private void containsNull(Collection<? extends T> collection){
        collection.stream().peek(Objects::requireNonNull).count();
    }
}
