package edu.hm.ahager.list;

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
    private final List<T> nullSafeList;

    public NullSafeList(final List<T> list){
        checkForNull(list);
        this.nullSafeList = list;
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
    public boolean contains(final Object that) {
        return nullSafeList.contains(that);
    }

    @Override
    public Iterator<T> iterator() {
        return nullSafeList.iterator();
    }

    @Override
    public Object[] toArray() {
        return nullSafeList.toArray();
    }

    @Override
    public <T1> T1[] toArray(final T1[] thatArray) {
        return nullSafeList.toArray(thatArray);
    }

    @Override
    public boolean add(final T that) {
        checkElemForNull(that, "The element to add should not be null.");
        return nullSafeList.add(that);
    }

    @Override
    public boolean remove(final Object that) {
        return nullSafeList.remove(that);
    }

    @Override
    public boolean containsAll(final Collection<?> c) {
        return nullSafeList.containsAll(c);
    }

    @Override
    public boolean addAll(final Collection<? extends T> that) {
        checkForNull(that);
        return nullSafeList.addAll(that);
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends T> that) {
        checkForNull(that);
        return nullSafeList.addAll(index, that);
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
    public void replaceAll(final UnaryOperator<T> operator) {
        for (T elem: nullSafeList) {
            checkElemForNull(operator.apply(elem), "The unaryOperator should not map a element of the list to null.");
        }
        nullSafeList.replaceAll(operator);
    }

    @Override
    public void sort(final Comparator<? super T> c) {
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
    public T get(final int index) {
        return nullSafeList.get(index);
    }

    @Override
    public T set(final int index, final T element) {
        checkElemForNull(element, "The element to add should not be null.");
        return nullSafeList.set(index, element);
    }

    @Override
    public void add(final int index, final T element) {
        checkElemForNull(element, "The element to add should not be null.");
        nullSafeList.add(index, element);
    }

    @Override
    public T remove(final int index) {
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
    public ListIterator<T> listIterator() {
        return nullSafeList.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(final int index) {
        return nullSafeList.listIterator(index);
    }

    @Override
    public List<T> subList(final int fromIndex, final int toIndex) {
        return nullSafeList.subList(fromIndex, toIndex);
    }

    @Override
    public Spliterator<T> spliterator() {
        return nullSafeList.spliterator();
    }

    @Override
    public boolean removeIf(final Predicate<? super T> filter) {
        return nullSafeList.removeIf(filter);
    }

    @Override
    public Stream<T> stream() {
        return nullSafeList.stream();
    }

    @Override
    public Stream<T> parallelStream() {
        return nullSafeList.parallelStream();
    }

    @Override
    public void forEach(final Consumer<? super T> action) {
        nullSafeList.forEach(action);
    }

    private void checkForNull(final Collection<? extends T> listToCheck){
        if (listToCheck == null) {
            throw new NullPointerException("The list should not be null");
        }
        for (T elem: listToCheck){
            checkElemForNull(elem, "All elements should not be null");
        }
    }

    private void checkElemForNull(final T apply, final String s) {
        if (apply == null) {
            throw new NullPointerException(s);
        }
    }
}
