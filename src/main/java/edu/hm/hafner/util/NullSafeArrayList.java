package edu.hm.hafner.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

public class NullSafeArrayList<E> extends ArrayList<E> {
    private static final long serialVersionUID = 4109481474923412532L;

    public NullSafeArrayList() {
        super();
    }

    public NullSafeArrayList(final int initialCapacity) {
        super(initialCapacity);
    }

    public NullSafeArrayList(final List<E> list) {
        super(list);

        list.forEach(Objects::requireNonNull);
    }

    @Override
    public E set(final int index, final E element) {
        Objects.requireNonNull(element);

        return super.set(index, element);
    }

    @Override
    public boolean add(final E e) {
        Objects.requireNonNull(e);

        return super.add(e);
    }

    @Override
    public void add(final int index, final E element) {
        Objects.requireNonNull(element);

        super.add(index, element);
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
        forEach((o) -> Objects.requireNonNull(operator.apply(o)));

        super.replaceAll(operator);
    }
}
