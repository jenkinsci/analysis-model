package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * Null safe array list
 * @param <E>
 * @author Johannes Jäger
 */
public class NullSafeArrayList<E> extends ArrayList<E> {
    private static final long serialVersionUID = -3201790067882704332L;

    public NullSafeArrayList() {
        super();
    }

    public NullSafeArrayList(int initialCapacity) {
        super(initialCapacity);
    }

    public NullSafeArrayList(Collection<? extends E> c) {
        super(c);
    }

    @Override
    public boolean add(final E e) {
        Objects.requireNonNull(e);
        return super.add(e);
    }

    @Override
    public boolean addAll(final Collection<? extends E> c) {
        c.forEach(Objects::requireNonNull);
        return super.addAll(c);
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends E> c) {
        c.forEach(Objects::requireNonNull);
        return super.addAll(index, c);
    }

    @Override
    public void replaceAll(final UnaryOperator<E> operator) {
        super.replaceAll(e -> Objects.requireNonNull(operator.apply(e)));
    }

    @Override
    public E set(final int index, final E element) {
        Objects.requireNonNull(element);
        return super.set(index, element);
    }

    @Override
    public void add(final int index, final E element) {
        Objects.requireNonNull(element);
        super.add(index, element);
    }
}
