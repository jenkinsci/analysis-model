package edu.hm.hafner.analysis;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NullSafeList extends AbstractList implements Serializable {

    private List<Integer> nullSafe;

    public NullSafeList(Integer number) {
        Objects.requireNonNull(number);
        nullSafe = new ArrayList<>();
        nullSafe.add(number);
    }

    public NullSafeList() {
        nullSafe = new ArrayList<>();
    }

    @Override
    public Object get(final int index) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }
}
