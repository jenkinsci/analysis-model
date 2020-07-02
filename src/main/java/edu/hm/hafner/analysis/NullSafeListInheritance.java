package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Eine Liste, die keine null Elemente nimmt via inheritance.
 * @author Michael Schober
 */
class NullSafeListInheritance<Integer> extends ArrayList<Integer> {

    NullSafeListInheritance(final int integer) {
        super(integer);
    }

    @Override
    public boolean add(final Integer integer) {
        Objects.requireNonNull(integer);
        return super.add(integer);
    }

    @Override
    public void add(final int index, final Integer integer) {
        Objects.requireNonNull(integer);
        super.add(index, integer);
    }

    @Override
    public boolean addAll(final Collection<? extends Integer> collection) {
        Objects.requireNonNull(collection);
        collection.forEach(Objects::requireNonNull);
        return super.addAll(collection);
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends Integer> collection) {
        Objects.requireNonNull(collection);
        collection.forEach(Objects::requireNonNull);
        return super.addAll(index, collection);
    }
}
