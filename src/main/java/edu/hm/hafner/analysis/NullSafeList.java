package edu.hm.hafner.analysis;


import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class NullSafeList<Object> implements List<Object> {

    private final List<Object> listToBeDecorated;


    public NullSafeList(final List<Object> list) {
        listToBeDecorated = list;
    }

    @Override
    public int size() {
        return listToBeDecorated.size();
    }

    @Override
    public boolean isEmpty() {
        return listToBeDecorated.isEmpty();
    }

    @Override
    public boolean contains(final java.lang.Object o) {
        return listToBeDecorated.contains(o);
    }

    @Override
    public Iterator<Object> iterator() {
        return listToBeDecorated.iterator();
    }

    @Override
    public java.lang.Object[] toArray() {
        return listToBeDecorated.toArray();
    }

    @Override
    public <T> T[] toArray(final T[] ts) {
        return listToBeDecorated.toArray(ts);
    }

    @Override
    public boolean add(final Object object) {
        if(object == null){
            throw new NullPointerException();
        }
        return listToBeDecorated.add(object);
    }

    @Override
    public boolean remove(final java.lang.Object o) {
        return listToBeDecorated.remove(o);
    }

    @Override
    public boolean containsAll(final Collection<?> collection) {
        return listToBeDecorated.containsAll(collection);
    }

    @Override
    public boolean addAll(final Collection<? extends Object> collection) {
        return addAll(0, collection);
    }

    @Override
    public boolean addAll(final int i, final Collection<? extends Object> collection) {
        for(Object o: collection){
            if(o == null){
                throw new NullPointerException();
            }
        }
        return listToBeDecorated.addAll(i, collection);
    }

    @Override
    public boolean removeAll(final Collection<?>  collection) {
        return listToBeDecorated.removeAll(collection);
    }

    @Override
    public boolean retainAll(final Collection<?> collection) {
        return listToBeDecorated.retainAll(collection);
    }

    @Override
    public void clear() {
        listToBeDecorated.clear();
    }

    @Override
    public Object get(final int i) {
        return listToBeDecorated.get(i);
    }

    @Override
    public Object set(final int i, final Object object) {
        return listToBeDecorated.set(i, object);
    }

    @Override
    public void add(final int i, final Object object) {
        if(object == null){
            throw new NullPointerException();
        }
        listToBeDecorated.add(i, object);
    }

    @Override
    public Object remove(final int i) {
        return listToBeDecorated.remove(i);
    }

    @Override
    public int indexOf(final java.lang.Object o) {
        return listToBeDecorated.indexOf(o);
    }

    @Override
    public int lastIndexOf(final java.lang.Object o) {
        return listToBeDecorated.lastIndexOf(o);
    }

    @Override
    public ListIterator<Object> listIterator() {
        return listToBeDecorated.listIterator();
    }

    @Override
    public ListIterator<Object> listIterator(final int i) {
        return listToBeDecorated.listIterator(i);
    }

    @Override
    public List<Object> subList(final int i, final int i1) {
        return listToBeDecorated.subList(i, i1);
    }
}
