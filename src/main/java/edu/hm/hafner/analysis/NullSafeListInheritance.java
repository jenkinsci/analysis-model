package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Class that provides a null safe wrapper for list implementations.
 * Uses inheritance.
 *
 * @param <E> the type of the list's elements
 *
 * @author mbauerness
 *
 * Pro Inheritance: you can just override the methods you want to change (no boilerplate code)
 *
 * Con Inheritance: your implementation is tied to a concrete class (ArrayList).
 *                  if the concrete class changes, your code may not work then
 */
public class NullSafeListInheritance<E> extends ArrayList<E> {

    private static final long serialVersionUID = 8988831990261565931L;

    public NullSafeListInheritance() {
        super();
    }

    @Override
    public E set(final int index, final E element) {
        return super.set(index, Objects.requireNonNull(element));
    }

    @Override
    public boolean add(final E e) {
        return super.add(Objects.requireNonNull(e));
    }

    @Override
    public void add(final int index, final E element) {
        super.add(index, Objects.requireNonNull(element));
    }

    @Override
    public boolean addAll(final Collection<? extends E> c) {
        checkIfListContainsNullElements(c);
        return super.addAll(Objects.requireNonNull(c));
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends E> c) {
        checkIfListContainsNullElements(c);
        return super.addAll(index, Objects.requireNonNull(c));
    }

    // helper methods
    private void checkIfListContainsNullElements(final Collection<? extends E> pList) {
        pList.forEach(Objects::requireNonNull);
    }
}
