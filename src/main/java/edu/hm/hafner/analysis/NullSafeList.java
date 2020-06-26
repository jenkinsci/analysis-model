package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

/**
 * A NullSafeList is a variation of a List but does not accept a null value.
 * @author Fabian Diener
 */

public class NullSafeList<E> implements List<E> {

    /**
     * a List to store the Elements.
     */
    private final List<E> list;

    /**
     * Chained Constructor with initial Capacity of 10.
     * ArrayList std. Capacity.
     */
    public NullSafeList() {
        this(10);
    }

    /**
     * Chained Constructor with initial Capacity of the size and all Elements of the collection.
     * @param collection
     */
    public NullSafeList(final Collection<? extends E> collection){
        this(collection.size());
        addAll(collection);
    }

    /**
     * Constructor for an empty NullSafeList with specified initial capacity.
     * @param size
     */
    public NullSafeList(final int size){
        list = new ArrayList<>(size);
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
    public Iterator<E> iterator() {
        return list.iterator();
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T> T[] toArray(final T[] ts) {
        return list.toArray(ts);
    }

    @Override
    public boolean add(final E e) {
        Objects.requireNonNull(e);
        return list.add(e);
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
    public boolean addAll(final Collection<? extends E> collection) {
        return addAll(0, collection);
    }

    @Override
    public boolean addAll(final int i, final Collection<? extends E> collection) {
        Objects.requireNonNull(collection);
        collection.forEach(Objects::requireNonNull);
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
    public void clear() {
        list.clear();
    }

    @Override
    public E get(final int i) {
        return list.get(i);
    }

    @Override
    public E set(final int i, final E e) {
        Objects.requireNonNull(e);
        return list.set(i, e);
    }

    @Override
    public void add(final int i, final E e) {
        Objects.requireNonNull(e);
        list.add(i, e);
    }

    @Override
    public E remove(final int i) {
        return list.remove(i);
    }

    @Override
    public int indexOf(final Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(final Object o) {
        return lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        return list.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(final int i) {
        return list.listIterator(i);
    }

    @Override
    public List<E> subList(final int i, final int i1) {
        return list.subList(i, i1);
    }
}
