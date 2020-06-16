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
 * A list that doesn't accept null.
 *
 * @author Viet Phuoc Ho - (v.ho@hm.edu)
 */
public class NullSafeList<E> implements List<E> {
    private final List<E> nullSafeList;

    /**
     * Constructor for NullSafeList.
     */
    public NullSafeList(){
        this.nullSafeList = new ArrayList<>();
    }

    /**
     * Constructor for NullSafeList.
     * @param list
     *        represents a list of elements that the NullSafeList will be filled with
     */
    public NullSafeList(final List<E> list){
        for(E e : list){
            Objects.requireNonNull(e);
        }

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
    public boolean contains(final Object o) {
        return nullSafeList.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return nullSafeList.iterator();
    }

    @Override
    public void forEach(final Consumer<? super E> consumer) {
        nullSafeList.forEach(consumer);
    }

    @Override
    public Object[] toArray() {
        return nullSafeList.toArray();
    }

    @Override
    public <T> T[] toArray(final T[] ts) {
        return nullSafeList.toArray(ts);
    }

    @Override
    public boolean add(final E e) {
        if(e.equals(null)) throw new IllegalArgumentException();
        return nullSafeList.add(e);
    }

    @Override
    public boolean remove(final Object o) {
        return nullSafeList.remove(o);
    }

    @Override
    public boolean containsAll(final Collection<?> collection) {
        return nullSafeList.containsAll(collection);
    }

    @Override
    public boolean addAll(final Collection<? extends E> collection) {
        Objects.requireNonNull(collection);
        for(E e :collection){
            if(e.equals(null)) throw new IllegalArgumentException();
        }
        return nullSafeList.addAll(collection);
    }

    @Override
    public boolean addAll(final int i, final Collection<? extends E> collection) {
        Objects.requireNonNull(collection);
        for(E e :collection){
            if(e.equals(null)) throw new IllegalArgumentException();
        }
        return nullSafeList.addAll(i,collection);
    }

    @Override
    public boolean removeAll(final Collection<?> collection) {
        return nullSafeList.removeAll(collection);
    }

    @Override
    public boolean removeIf(final Predicate<? super E> predicate) {
        return nullSafeList.removeIf(predicate);
    }

    @Override
    public boolean retainAll(final Collection<?> collection) {
        return nullSafeList.retainAll(collection);
    }

    @Override
    public void replaceAll(final UnaryOperator<E> unaryOperator) {
        for(E e: nullSafeList){
            if(unaryOperator.apply(e).equals(null)) throw new IllegalArgumentException();
        }
        nullSafeList.replaceAll(unaryOperator);
    }

    @Override
    public void sort(final Comparator<? super E> comparator) {
        nullSafeList.sort(comparator);
    }

    @Override
    public void clear() {
        nullSafeList.clear();
    }

    @Override
    public E get(final int i) {
        return nullSafeList.get(i);
    }

    @Override
    public E set(final int i, final E e) {
        if(e.equals(null)) throw new IllegalArgumentException();
        return nullSafeList.set(i, e);
    }

    @Override
    public void add(final int i, final E e) {
        if(e.equals(null)) throw new IllegalArgumentException();
        nullSafeList.add(i, e);
    }

    @Override
    public E remove(final int i) {
        return nullSafeList.remove(i);
    }

    @Override
    public int indexOf(final Object o) {
        return nullSafeList.indexOf(o);
    }

    @Override
    public int lastIndexOf(final Object o) {
        return nullSafeList.indexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        return nullSafeList.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(final int i) {
        return nullSafeList.listIterator(i);
    }

    @Override
    public List<E> subList(final int i, final int i1) {
        return nullSafeList.subList(i,i1);
    }

    @Override
    public Spliterator<E> spliterator() {
        return nullSafeList.spliterator();
    }

    @Override
    public Stream<E> stream() {
        return nullSafeList.stream();
    }

    @Override
    public Stream<E> parallelStream() {
        return nullSafeList.parallelStream();
    }
}
