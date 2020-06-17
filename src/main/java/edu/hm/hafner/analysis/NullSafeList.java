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

    private final List<T> list;

    public NullSafeList(List<T> list){
        if(list == null)
            throw new NullPointerException();
        this.list = list;
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
    public <T1> T1[] toArray(final T1[] t1s) {
        return list.toArray(t1s);
    }

    @Override
    public boolean add(final T t) {
        if(t == null)
            throw new NullPointerException();
        return list.add(t);
    }

    @Override
    public boolean remove(final Object o) {
        return list.remove(o);
    }

    @Override
    public boolean containsAll(final Collection<?> collection) {
        return list.containsAll(collection);
    }

    @Override
    public boolean addAll(final Collection<? extends T> collection) {
        for (T t : collection){
            if(t == null)
                throw new NullPointerException();
        }
        return list.addAll(collection);
    }

    @Override
    public boolean addAll(final int i, final Collection<? extends T> collection) {
        for (T t : collection){
            if(t == null)
                throw new NullPointerException();
        }
        return list.addAll(i, collection);
    }

    @Override
    public boolean removeAll(final Collection<?> collection) {
        return list.removeAll(collection);
    }

    @Override
    public boolean retainAll(final Collection<?> collection) {
        return list.retainAll(collection);
    }

    @Override
    public void replaceAll(final UnaryOperator<T> unaryOperator) {
        list.replaceAll(unaryOperator);
    }

    @Override
    public void sort(final Comparator<? super T> comparator) {
        list.sort(comparator);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public boolean equals(final Object o) {
        return list.equals(o);
    }

    @Override
    public int hashCode() {
        return list.hashCode();
    }

    @Override
    public T get(final int i) {
        return list.get(i);
    }

    @Override
    public T set(final int i, final T t) {
        if(t == null)
            throw new NullPointerException();
        return list.set(i, t);
    }

    @Override
    public void add(final int i, final T t) {
        if(t == null)
            throw new NullPointerException();
        list.add(i, t);
    }

    @Override
    public T remove(final int i) {
        return list.remove(i);
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
    public ListIterator<T> listIterator(final int i) {
        return list.listIterator(i);
    }

    @Override
    public List<T> subList(final int i, final int i1) {
        return list.subList(i, i1);
    }

    @Override
    public Spliterator<T> spliterator() {
        return list.spliterator();
    }

    @Override
    public boolean removeIf(final Predicate<? super T> predicate) {
        return list.removeIf(predicate);
    }

    @Override
    public Stream<T> stream() {
        return list.stream();
    }

    @Override
    public Stream<T> parallelStream() {
        return list.parallelStream();
    }

    @Override
    public void forEach(final Consumer<? super T> consumer) {
        list.forEach(consumer);
    }
}
