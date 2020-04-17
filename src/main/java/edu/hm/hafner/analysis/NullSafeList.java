package edu.hm.hafner.analysis;


import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

public class NullSafeList<T> implements List<T> {

    private final List<T> listToBeDecorated;


    public NullSafeList(final List<T> list) {
        checkForNull(list);
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
    public boolean contains(final Object o) {
        return listToBeDecorated.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return listToBeDecorated.iterator();
    }

    @Override
    public Object[] toArray() {
        return listToBeDecorated.toArray();
    }

    @Override
    public <T> T[] toArray(final T[] ts) {
        return listToBeDecorated.toArray(ts);
    }

    @Override
    public boolean add(final T t) {
        Objects.requireNonNull(t);
        return listToBeDecorated.add(t);
    }

    @Override
    public boolean remove(final Object o) {
        return listToBeDecorated.remove(o);
    }

    @Override
    public boolean containsAll(final Collection<?> collection) {
        return listToBeDecorated.containsAll(collection);
    }

    @Override
    public boolean addAll(final Collection<? extends T> collection) {
        checkForNull(collection);
        return addAll(0, collection);
    }

    @Override
    public boolean addAll(final int i, final Collection<? extends T> collection) {
        checkForNull(collection);
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
    public T get(final int i) {
        return listToBeDecorated.get(i);
    }

    @Override
    public T set(final int i, final T object) {
        return listToBeDecorated.set(i, object);
    }

    @Override
    public void add(final int i, final T object) {
        if(object == null){
            throw new NullPointerException();
        }
        listToBeDecorated.add(i, object);
    }

    @Override
    public T remove(final int i) {
        return listToBeDecorated.remove(i);
    }

    @Override
    public int indexOf(final Object o) {
        return listToBeDecorated.indexOf(o);
    }

    @Override
    public int lastIndexOf(final Object o) {
        return listToBeDecorated.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return listToBeDecorated.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(final int i) {
        return listToBeDecorated.listIterator(i);
    }

    @Override
    public List<T> subList(final int i, final int i1) {
        return listToBeDecorated.subList(i, i1);
    }


    private void checkForNull(final Collection<? extends T> collection){
        for(T element: collection){
            Objects.requireNonNull(element);
        }
    }
}
