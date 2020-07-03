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

public class NullSafeList extends AbstractList<Integer> implements List<Integer>, Serializable {

    private List<Integer> nullSafe;

    public NullSafeList(Integer number) {
        Objects.requireNonNull(number);
        nullSafe = new ArrayList<>();
        nullSafe.add(number);
    }

    public NullSafeList() {
        nullSafe = new ArrayList<>();
    }

    public NullSafeList(List<Integer> list) {
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
    public Iterator<Integer> iterator() {
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

    public boolean add(final Integer integer) {
        Objects.requireNonNull(integer);
        return nullSafe.add(integer);
    }

    @Override
    public boolean remove(final Object o) {
        return nullSafe.remove(o);
    }

    public boolean containsAll(final Collection<?> c) {
        return nullSafe.containsAll(c);
    }

    @Override
    public boolean addAll(final Collection<? extends Integer> c) {
        containsNull(c);
        return nullSafe.addAll(c);
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends Integer> c) {
        containsNull(c);
        return nullSafe.addAll(index, c);
    }

    public boolean removeAll(final Collection<?> c) {
        return nullSafe.removeAll(c);
    }

    public boolean retainAll(final Collection<?> c) {
        return nullSafe.retainAll(c); // retainAll wirft npe falls null in c enthalten ist
    }

    public void replaceAll(final UnaryOperator<Integer> operator) {
        nullSafe.replaceAll(operator);
    }

    public void sort(final Comparator<? super Integer> c) {
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
    public Integer get(final int index) {
        return nullSafe.get(index);
    }

    public Integer set(final int index, final Integer element) {
        Objects.requireNonNull(element);
        return nullSafe.set(index, element);
    }

    public void add(final int index, final Integer element) {
        Objects.requireNonNull(element);
        nullSafe.add(index, element);
    }

    @Override
    public Integer remove(final int index) {
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
    public ListIterator<Integer> listIterator() {
        return nullSafe.listIterator();
    }

    @Override
    public ListIterator<Integer> listIterator(final int index) {
        return nullSafe.listIterator(index);
    }

    @Override
    public List<Integer> subList(final int fromIndex, final int toIndex) {
        return nullSafe.subList(fromIndex, toIndex);
    }

    @Override
    public Spliterator<Integer> spliterator() {
        return nullSafe.spliterator();
    }

    public boolean removeIf(final Predicate<? super Integer> filter) {
        return nullSafe.removeIf(filter);
    }

    @Override
    public Stream<Integer> stream() {
        return nullSafe.stream();
    }

    @Override
    public Stream<Integer> parallelStream() {
        return nullSafe.parallelStream();
    }

    public void forEach(final Consumer<? super Integer> action) {
        nullSafe.forEach(action);
    }

    private void containsNull(Collection<? extends Integer> collection){
        collection.stream().peek(Objects::requireNonNull).count();
    }
}
