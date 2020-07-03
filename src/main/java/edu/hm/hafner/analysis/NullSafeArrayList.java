package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Decorator for a null-safe arraList
 *
 * @author Matthias König
 */

public class NullSafeArrayList<Integer> extends ArrayList<Integer> {

    private final ArrayList<Integer> nullSafeArrayList;

    public NullSafeArrayList(final ArrayList<Integer> inputList) {
        if (inputList.contains(null)) {
            throw new NullPointerException("not allowed: the list contains or is null");
        }
        this.nullSafeArrayList = inputList;
    }

    @Override
    public boolean add(final Integer integer) {
        if(integer == null) {
            throw new NullPointerException("not allowed: you want to add null to this list");
        }
        return super.add(integer);
    }

    @Override
    public void add(final int index, final Integer element) {
        if (element == null) {
            throw new NullPointerException("not allowed: your element you want to add is null");
        }
        super.add(index, element);
    }

    @Override
    public boolean addAll(final Collection<? extends Integer> c) {
        if (c.contains(null)) {
            throw new NullPointerException("not allowed: your collection you want to add contains null");
        }
        return super.addAll(c);
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends Integer> c) {
        if (c.contains(null)) {
            throw new NullPointerException("not allowed: your collection you want to add contains null");
        }
        return super.addAll(index, c);
    }

    @Override
    public Integer set(final int index, final Integer element) {
        if (element == null) {
            throw new NullPointerException("not allowed: your element you want to add is null");
        }
        return super.set(index, element);
    }
}