package edu.hm.hafner.analysis.parser;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Concrete Decorator for a List witch excludes the "null" element.
 *
 * @author Elena Lilova
 */

public class NullSafeList<E> extends ListDecorator {

    /**
     * Constructor for Null Safe List
     * @param list
     */
    public NullSafeList(List<E> list) {
        super(list);
    }

    @Override
    public boolean add(final Object element) {
        Objects.requireNonNull(element);
        return super.add(element);
    }

    @Override
    public void add(final int index, final Object element) {
        Objects.requireNonNull(element);
        super.add(index, element);
    }

    @Override
    public boolean addAll(final Collection collection) {
        for (Object element : collection) {
            this.add(element);
        }
        return true;
    }

    @Override
    public boolean addAll(final int index, final Collection collection) {
        int countTheIndex = index;
        for (Object element : collection) {
            this.add(countTheIndex, element);
            countTheIndex++;
        }
        return true;
    }

}

