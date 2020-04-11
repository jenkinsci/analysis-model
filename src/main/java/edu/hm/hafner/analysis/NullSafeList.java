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

public class NullSafeList<T> implements List<T>{

    private final List<T> list;

    public NullSafeList(final List<T> list) {
        checkForNull(list);
        this.list = list;
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public boolean contains(final Object o) {
        return list.contains(o);
    }

    public Iterator<T> iterator() {
        return list.iterator();
    }

    public Object[] toArray() {
        return list.toArray();
    }

    public <T1> T1[] toArray(final T1[] t1s) {
        return list.toArray(t1s);
    }

    public boolean add(final T t) {
        if(t == null)
            throw new NullPointerException();
        return list.add(t);
    }

    public boolean remove(final Object o) {
        return list.remove(o);
    }

    public boolean containsAll(final Collection<?> collection) {
        return list.containsAll(collection);
    }

    public boolean addAll(final Collection<? extends T> collection) {
        checkForNull(collection);
        return list.addAll(collection);
    }

    public boolean addAll(final int i, final Collection<? extends T> collection) {
        checkForNull(collection);
        return list.addAll(i, collection);
    }

    public boolean removeAll(final Collection<?> collection) {
        return list.removeAll(collection);
    }

    public boolean retainAll(final Collection<?> collection) {
        return list.retainAll(collection);
    }

    public void replaceAll(final UnaryOperator<T> unaryOperator) {
        list.replaceAll(unaryOperator);
    }

    public void sort(final Comparator<? super T> comparator) {
        list.sort(comparator);
    }

    public void clear() {
        list.clear();
    }

    public T get(final int i) {
        return list.get(i);
    }

    public T set(final int i, final T t) {
        if(t == null)
            throw new NullPointerException();
        return list.set(i, t);
    }

    public void add(final int i, final T t) {
        if(t == null)
            throw new NullPointerException();
        list.add(i, t);
    }

    public T remove(final int i) {
        return list.remove(i);
    }

    public int indexOf(final Object o) {
        return list.indexOf(o);
    }

    public int lastIndexOf(final Object o) {
        return list.lastIndexOf(o);
    }

    public ListIterator<T> listIterator() {
        return list.listIterator();
    }

    public ListIterator<T> listIterator(final int i) {
        return list.listIterator(i);
    }

    public List<T> subList(final int i, final int i1) {
        return list.subList(i, i1);
    }

    public Spliterator<T> spliterator() {
        return list.spliterator();
    }

    public boolean removeIf(final Predicate<? super T> predicate) {
        return list.removeIf(predicate);
    }

    public Stream<T> stream() {
        return list.stream();
    }

    public Stream<T> parallelStream() {
        return list.parallelStream();
    }

    public void forEach(final Consumer<? super T> consumer) {
        list.forEach(consumer);
    }

    private void checkForNull(final Collection<? extends T> collection) {
        for(T element : collection) {
            if (element == null) {
                throw new NullPointerException();
            }
        }
    }
}
