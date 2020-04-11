package edu.hm.hafner.analysis.parser;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Decorator for {@link List}
 * @author Elena Lilova
 */

public class ListDecorator implements List {
    /**
     * wrapper list.
     */
    private List list;

    /**
     * Constructor for {@link ListDecorator}
     * @param list
     */
    public ListDecorator(List list) {
        this.list = list;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(final Object o) {
        return list.contains(0);
    }

    @Override
    public Iterator iterator() {
        return list.iterator();
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public boolean add(final Object o) {
        return list.add(o);
    }

    @Override
    public boolean remove(final Object o) {
        return list.remove(o);
    }

    @Override
    public boolean addAll(final Collection collection) {
        return list.addAll(collection);
    }

    @Override
    public boolean addAll(final int i, final Collection collection) {
        return list.addAll(i, collection);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public Object get(final int i) {
        return list.get(i);
    }

    @Override
    public Object set(final int i, final Object o) {
        return list.set(i, o);
    }

    @Override
    public void add(final int i, final Object o) {
        list.add(i,o);

    }

    @Override
    public Object remove(final int i) {
        return list.remove(i);
    }

    @Override
    public int indexOf(final Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(final Object o) {
        return list.indexOf(o);
    }

    @Override
    public ListIterator listIterator() {
        return list.listIterator();
    }

    @Override
    public ListIterator listIterator(final int i) {
        return list.listIterator(i);
    }

    @Override
    public List subList(final int i, final int i1) {
        return list.subList(i,i1);
    }

    @Override
    public boolean retainAll(final Collection collection) {
        return list.retainAll(collection);
    }

    @Override
    public boolean removeAll(final Collection collection) {
        return list.removeAll(collection);
    }

    @Override
    public boolean containsAll(final Collection collection) {
        return list.containsAll(collection);
    }

    @Override
    public Object[] toArray(final Object[] objects) {
        return list.toArray(objects);
    }
}
