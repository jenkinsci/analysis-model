package edu.hm.hafner.analysis;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class BaseListDecorator implements List {
    private List list;

    BaseListDecorator(final List list) {
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
        return list.contains(o);
    }

    @Override
    public Iterator iterator() {
        return listIterator();
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
    public boolean addAll(final Collection c) {
        return list.addAll(c);
    }

    @Override
    public boolean addAll(final int index, final Collection c) {
        return list.addAll(index, c);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public Object get(final int index) {
        return list.get(index);
    }

    @Override
    public Object set(final int index, final Object element) {
        return list.set(index, element);
    }

    @Override
    public void add(final int index, final Object element) {
        list.add(index, element);
    }

    @Override
    public Object remove(final int index) {
        return list.remove(index);
    }

    @Override
    public int indexOf(final Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(final Object o) {
        return list.lastIndexOf(o);
    }

    @Override
    public ListIterator listIterator() {
        return list.listIterator();
    }

    @Override
    public ListIterator listIterator(final int index) {
        return list.listIterator(index);
    }

    @Override
    public List subList(final int fromIndex, final int toIndex) {
        return list.subList(fromIndex, toIndex);
    }

    @Override
    public boolean retainAll(final Collection c) {
        return list.retainAll(c);
    }

    @Override
    public boolean removeAll(final Collection c) {
        return list.removeAll(c);
    }

    @Override
    public boolean containsAll(final Collection c) {
        return list.containsAll(c);
    }

    @Override
    public Object[] toArray(final Object[] a) {
        return list.toArray(a);
    }
}
