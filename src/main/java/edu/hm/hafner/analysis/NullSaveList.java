package edu.hm.hafner.analysis;

import java.util.ArrayList;
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



public class NullSaveList<E> implements List<E> {
    private final List<E> candidate;


    /**
     * Creates a new instance of NullSaveList.
     */
    public NullSaveList(){
        candidate = new ArrayList<>();
    }


    /**
     * Creates a new instance of NullSaveList.
     *
     * @param input
     *      a List
     *
     *
     */
    public NullSaveList(final List<E> input){
        if(input.contains(null)){
            throw new NullPointerException();
        }
        candidate = input;
    }

    @Override
    public int size() {
        return candidate.size();
    }

    @Override
    public boolean isEmpty() {
        return candidate.isEmpty();
    }

    @Override
    public boolean contains(final Object o) {
        return candidate.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return candidate.iterator();
    }

    @Override
    public void forEach(final Consumer<? super E> consumer) {
        candidate.forEach(consumer);

    }

    @Override
    public Object[] toArray() {
        return candidate.toArray();
    }

    @Override
    public <T> T[] toArray(final T[] ts) {
        return candidate.toArray(ts);
    }

    @Override
    public boolean add(final E e) {
        if(e.equals(null)){
            throw new NullPointerException();
        }
        return candidate.add(e);
    }

    @Override
    public boolean remove(final Object o) {
        if(o.equals(null)){
            throw new NullPointerException();
        }
        return  candidate.remove(o);
    }

    @Override
    public boolean containsAll(final Collection<?> collection) {
        return candidate.contains(collection);
    }

    @Override
    public boolean addAll(final Collection<? extends E> collection) {
        if(collection.contains(null)){
            throw new NullPointerException();
        }
        return candidate.addAll(collection);
    }

    @Override
    public boolean addAll(final int i, final Collection<? extends E> collection) {
        if(collection.equals(null)){
            throw new NullPointerException();
        }
        if(collection.contains(null)){
            throw new NullPointerException();
        }
        return candidate.addAll(i, collection);
    }

    @Override
    public boolean removeAll(final Collection<?> collection) {
        if(collection.equals(null)){
            throw new NullPointerException();
        }
        return candidate.removeAll(collection);
    }

    @Override
    public boolean removeIf(final Predicate<? super E> predicate) {
        return candidate.removeIf(predicate);
    }

    @Override
    public boolean retainAll(final Collection<?> collection) {
        if(collection.equals(null)){
            throw new NullPointerException();
        }
        return collection.retainAll(collection);
    }

    @Override
    public void replaceAll(final UnaryOperator<E> unaryOperator) {
        if(unaryOperator.equals(null)){
            throw new NullPointerException();
        }
        List<E> test;
        test = candidate;
        test.replaceAll(unaryOperator);
        if(test.contains(null)){
            throw new NullPointerException();
        }
        candidate.replaceAll(unaryOperator);
    }

    @Override
    public void sort(final Comparator<? super E> comparator) {
        candidate.sort(comparator);
    }

    @Override
    public void clear() {
        candidate.clear();
    }

    @Override
    public E get(final int i) {
        if(i<0){
            throw new IllegalArgumentException();
        }
        return candidate.get(i);
    }

    @Override
    public E set(final int i, final E e) {
        if(e.equals(null)){
            throw new NullPointerException();
        }
        return candidate.set(i,e);
    }

    @Override
    public void add(final int i, final E e) {
        if(e.equals(null)){
            throw new NullPointerException();
        }
        candidate.add(e);
    }

    @Override
    public E remove(final int i) {
        if(i<0){
            throw new IllegalArgumentException();
        }
        return candidate.remove(i);
    }

    @Override
    public int indexOf(final Object o) {
        if(o.equals(null)){
            throw new NullPointerException();
        }
        return indexOf(o);
    }

    @Override
    public int lastIndexOf(final Object o) {
        if(o.equals(null)){
            throw new NullPointerException();
        }
        return lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        return candidate.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(final int i) {
        return candidate.listIterator(i);
    }

    @Override
    public List<E> subList(final int i, final int i1) {
        return candidate.subList(i,i1);
    }

    @Override
    public Spliterator<E> spliterator() {
        return candidate.spliterator();
    }

    @Override
    public Stream<E> stream() {
        return candidate.stream();
    }

    @Override
    public Stream<E> parallelStream() {
        return candidate.parallelStream();
    }
}
