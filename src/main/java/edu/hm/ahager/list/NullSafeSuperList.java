package edu.hm.ahager.list;

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

/**
 * I do not like this solution, because I extend the class ArrayList
 * and change the behaviour of it's implemented methods.
 * Furthermore I can, due to the inheritance, replace an ArrayList 
 * with the NullSafeSuperList and pass it for example in a function 
 * as a argument. But as the autor of such an function I have to know 
 * about this SubArrayList and I have to add further complexity to
 * decide if I can store null values in the ArrayList.
 *
 * I cannot see any advantage of this solution
 * (I think I took the last advantages by setting the NullSafeList to
 * implement the interface List\<T\>).
 *
 * @param <T>
 */
public class NullSafeSuperList<T> extends ArrayList<T> {
    private static final long serialVersionUID = -4587333826140264772L;

    public NullSafeSuperList() {
        super();
    }

    @Override
    public void trimToSize() {
        super.trimToSize();
    }

    @Override
    public void ensureCapacity(final int minCapacity) {
        super.ensureCapacity(minCapacity);
    }

    @Override
    public int size() {
        return super.size();
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public boolean contains(final Object o) {
        return super.contains(o);
    }

    @Override
    public int indexOf(final Object o) {
        return super.indexOf(o);
    }

    @Override
    public int lastIndexOf(final Object o) {
        return super.lastIndexOf(o);
    }

    @Override
    public Object clone() {
        return super.clone();
    }

    @Override
    public Object[] toArray() {
        return super.toArray();
    }

    @Override
    public <T1> T1[] toArray(final T1[] a) {
        return super.toArray(a);
    }

    @Override
    public T get(final int index) {
        return super.get(index);
    }

    @Override
    public T set(final int index, final T element) {
        checkElementForNull(element, "The element to add should not be null");
        return super.set(index, element);
    }

    @Override
    public boolean add(final T that) {
        checkElementForNull(that, "The element to add should not be null");
        return super.add(that);
    }

    @Override
    public void add(final int index, final T element) {
        checkElementForNull(element, "The element to add should not be null");
        super.add(index, element);
    }

    @Override
    public T remove(final int index) {
        return super.remove(index);
    }

    @Override
    public boolean remove(final Object o) {
        return super.remove(o);
    }

    @Override
    public boolean containsAll(final Collection<?> c) {
        return super.containsAll(c);
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean addAll(final Collection<? extends T> c) {
        checkForNull(c);
        return super.addAll(c);
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends T> c) {
        checkForNull(c);
        return super.addAll(index, c);
    }

    @Override
    protected void removeRange(final int fromIndex, final int toIndex) {
        super.removeRange(fromIndex, toIndex);
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        return super.removeAll(c);
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        return super.retainAll(c);
    }

    @Override
    public ListIterator<T> listIterator(final int index) {
        return super.listIterator(index);
    }

    @Override
    public ListIterator<T> listIterator() {
        return super.listIterator();
    }

    @Override
    public Iterator<T> iterator() {
        return super.iterator();
    }

    @Override
    public List<T> subList(final int fromIndex, final int toIndex) {
        return super.subList(fromIndex, toIndex);
    }

    @Override
    public boolean equals(final Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public void forEach(final Consumer<? super T> action) {
        super.forEach(action);
    }

    @Override
    public Spliterator<T> spliterator() {
        return super.spliterator();
    }

    @Override
    public Stream<T> stream() {
        return null;
    }

    @Override
    public Stream<T> parallelStream() {
        return null;
    }

    @Override
    public boolean removeIf(final Predicate<? super T> filter) {
        return super.removeIf(filter);
    }

    @Override
    public void replaceAll(final UnaryOperator<T> operator) {
        for (T elem: this) {
            if (operator.apply(elem) == null) {
                throw new NullPointerException("The unaryOperator should not map a element of the list to null.");
            }
        }
        super.replaceAll(operator);
    }

    @Override
    public void sort(final Comparator<? super T> c) {
        super.sort(c);
    }

    private void checkForNull(final Collection<? extends T> listToCheck){
        if (listToCheck == null) {
            throw  new NullPointerException("The list should not be null");
        }
        for (T elem: listToCheck){
            checkElementForNull(elem, "All elements should not be null");
        }
    }

    private void checkElementForNull(final T element, final String s) {
        if (element == null) {
            throw new NullPointerException(s);
        }
    }
}
