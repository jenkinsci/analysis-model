package edu.hm.hafner.analysis;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.UnaryOperator;

/**
 * Class that provides a null safe wrapper for list implementations.
 * Uses delegation.
 * @param <E>
 *           the type of the list's elements
 *
 * @author mbauerness
 *
 * Pro Delegation: it's not tied to a concrete class but to an interface (flexible)
 *
 * Con Delegation: many methods you don't want to change in general must be implemented (boilerplate code)
 */
public class NullSafeList<E> implements List<E> {

    /**
     * The list to wrap.
     */
    private final List<E> list;

    /**
     * Constructor for a null safe list.
     *
     * @param pList t
     *              he list to wrap
     * @throws NullPointerException
     *          if pList or at least one of it's elements is null
     */
    public NullSafeList(final List<E> pList) {
        Objects.requireNonNull(pList);
        checkIfListContainsNullElements(pList);
        this.list = pList;
    }

    // adding methods
    @Override
    public boolean add(final E e) {
        return list.add(Objects.requireNonNull(e));
    }

    @Override
    public void add(final int index, final E element) {
        list.add(index, Objects.requireNonNull(element));
    }

    @Override
    public boolean addAll(final Collection<? extends E> c) {
        Objects.requireNonNull(c);
        checkIfListContainsNullElements(c);
        return list.addAll(c);
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends E> c) {
        Objects.requireNonNull(c);
        checkIfListContainsNullElements(c);
        return list.addAll(index, c);
    }

    @Override
    public E set(final int index, final E element) {
        return list.set(index, Objects.requireNonNull(element));
    }

    // remaining methods
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
    public <T> T[] toArray(final T[] a) {
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
        return list.removeAll(c);
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        return list.retainAll(c);
    }

    @Override
    public void replaceAll(final UnaryOperator<E> operator) {
        list.replaceAll(operator);
    }

    @Override
    public void sort(final Comparator<? super E> c) {
        list.sort(c);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public E get(final int index) {
        return list.get(index);
    }

    @Override
    public E remove(final int index) {
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
    public ListIterator<E> listIterator() {
        return list.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(final int index) {
        return list.listIterator(index);
    }

    @Override
    public List<E> subList(final int fromIndex, final int toIndex) {
        return list.subList(fromIndex, toIndex);
    }

    @Override
    public Spliterator<E> spliterator() {
        return list.spliterator();
    }

    // helper methods
    private void checkIfListContainsNullElements(final Collection<? extends E> pList) {
        pList.forEach(Objects::requireNonNull);
    }
}
