package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;


/**
 * Decorator for {@link ArrayList}
 * This Decorator extends {@link ArrayList}.
 *
 * @author S. A. D.
 */
public class NullSafeListInherit<E> extends ArrayList<E> {
    private static final long serialVersionUID = 3729652377905730737L;

    public NullSafeListInherit() {
        super();
    }

    public NullSafeListInherit(final int size) {
        super(size);
    }

    public NullSafeListInherit(final List<E> list) {
        super(list);
        checkIfContainsNull(list);
    }

    /**
     * Replaces the element at the specified position in this list with the specified element.
     *
     * @param index
     *         index of the element to replace
     * @param element
     *         element to be stored at the specified position
     *
     * @return the element previously at the specified position
     * @throws IndexOutOfBoundsException
     *         {@inheritDoc}
     */
    @Override
    public E set(final int index, final E element) {
        Objects.requireNonNull(element);
        return super.set(index, element);
    }

    /**
     * Appends the specified element to the end of this list.
     *
     * @param e
     *         element to be appended to this list
     *
     * @return <tt>true</tt> (as specified by {@link Collection#add})
     */
    @Override
    public boolean add(final E e) {
        Objects.requireNonNull(e);
        return super.add(e);
    }

    /**
     * Inserts the specified element at the specified position in this list. Shifts the element currently at that
     * position (if any) and any subsequent elements to the right (adds one to their indices).
     *
     * @param index
     *         index at which the specified element is to be inserted
     * @param element
     *         element to be inserted
     *
     * @throws IndexOutOfBoundsException
     *         {@inheritDoc}
     */
    @Override
    public void add(final int index, final E element) {
        Objects.requireNonNull(element);
        super.add(index, element);
    }

    /**
     * Appends all of the elements in the specified collection to the end of this list, in the order that they are
     * returned by the specified collection's Iterator.  The behavior of this operation is undefined if the specified
     * collection is modified while the operation is in progress.  (This implies that the behavior of this call is
     * undefined if the specified collection is this list, and this list is nonempty.) This method only allows to add
     * collection which don't contain null elements.
     *
     * @param c
     *         collection containing elements to be added to this list
     *
     * @return <tt>true</tt> if this list changed as a result of the call
     * @throws NullPointerException
     *         if the specified collection is null
     */
    @Override
    public boolean addAll(final Collection<? extends E> c) {
        checkIfContainsNull(c);
        return super.addAll(c);
    }

    /**
     * Inserts all of the elements in the specified collection into this list, starting at the specified position.
     * Shifts the element currently at that position (if any) and any subsequent elements to the right (increases their
     * indices).  The new elements will appear in the list in the order that they are returned by the specified
     * collection's iterator.
     *
     * @param index
     *         index at which to insert the first element from the specified collection
     * @param c
     *         collection containing elements to be added to this list
     *
     * @return <tt>true</tt> if this list changed as a result of the call
     *
     * @throws IndexOutOfBoundsException
     *         {@inheritDoc}
     * @throws NullPointerException
     *         if the specified collection is null
     */
    @Override
    public boolean addAll(final int index, final Collection<? extends E> c) {
        checkIfContainsNull(c);
        return super.addAll(index, c);
    }

    /**
     * Checks if an element of a collection is null. If there is a instance of null within the collection it will throw
     * a NullPointerException
     *
     * @param c
     *         a specified collection
     *
     * @throws NullPointerException
     *         if the specified collection is null
     */
    private void checkIfContainsNull(final Collection<? extends E> c) {
        if (c.stream().anyMatch(Objects::isNull)) {
            throw new NullPointerException();
        }
    }

}
