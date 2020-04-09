package edu.hm.hafner.analysis;

import java.util.Collection;
import java.util.List;

/**
 * concret decorator for a null safe list
 * @param <E>
 *              amy class which extends from {@link List}
 *
 * @author Simon sYmhoven
 */
class NullSafeList<E extends List> extends BaseListDecorator {

    /**
     *
     * @param list
     *              any object which implements the interface {@link List}
     */
    NullSafeList(final List list) {
        super(list);
    }

    @Override
    public boolean add(Object element) {
        if (element != null) {
            return super.add(element);
        } else {
            throw new NullPointerException();
        }
    }

    @Override
    public void add(final int index, final Object element) {
        if (element != null) {
            super.add(index, element);
        } else {
            throw new NullPointerException();
        }
    }

    @Override
    public boolean addAll(final Collection c) {
        c.forEach(this::add);
        return true;
    }

    @Override
    public boolean addAll(final int index, final Collection c) {
        c.forEach(element -> this.add(index, element));
        return true;
    }
}