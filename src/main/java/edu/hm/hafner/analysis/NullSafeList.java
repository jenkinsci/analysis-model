package edu.hm.hafner.analysis;


import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * A list which trows a NPE when attempted to store a null value.
 *
 * This implementation is done through a decorator pattern.
 * A NullSafeList internally stores a List but at the same time implements List aswell
 *
 * Decorator:
 *
 * Vorteile: - Bindung an Inferface - konkrete Klasse wird einem beim Aufruf erst übergeben
 *           - Unabhängiger, man "dekoriert" sich einzelne Features dazu wie man will. Reihenfolge auch unbedeutend
 *
 * Nachteile: - Boilerplate Code - auch wenn er leicht generiert werden kann
 *
 * @param <T>
 *     type of element the list stores
 *
 * @author Daniel Soukup
 */
public class NullSafeList<T> implements List<T> {

    private final List<T> listToBeDecorated;

    /**
     * Creates a new {@link NullSafeList} which wraps an existing list object.
     *
     * @param list
     *          the concrete list type one wants to use internally to store the elements
     */
    public NullSafeList(final List<T> list) {
        Objects.requireNonNull(list);
        checkForNull(list);
        listToBeDecorated = list;
    }

    // Newly (over-)written method calls

    @Override
    public boolean add(final T t) {
        Objects.requireNonNull(t);
        return listToBeDecorated.add(t);
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
    public T set(final int i, final T t) {
        Objects.requireNonNull(t);
        return listToBeDecorated.set(i, t);
    }

    @Override
    public void add(final int i, final T t) {
        Objects.requireNonNull(t);
        listToBeDecorated.add(i, t);
    }

    private void checkForNull(final Collection<? extends T> collection) {
        for (T element : collection) {
            Objects.requireNonNull(element);
        }
    }

    // Purely delegated method calls

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
    public <L> L[] toArray(final L[] t1s) {
        return listToBeDecorated.toArray(t1s);
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
    public boolean removeAll(final Collection<?> collection) {
        return listToBeDecorated.removeAll(collection);
    }

    @Override
    public boolean retainAll(final Collection<?> collection) {
        return listToBeDecorated.retainAll(collection);
    }

    @Override
    public void replaceAll(final UnaryOperator<T> unaryOperator) {
        listToBeDecorated.replaceAll(unaryOperator);
    }

    @Override
    public void sort(final Comparator<? super T> comparator) {
        listToBeDecorated.sort(comparator);
    }

    @Override
    public void clear() {
        listToBeDecorated.clear();
    }

    @Override
    public boolean equals(final Object o) {
        return listToBeDecorated.equals(o);
    }

    @Override
    public int hashCode() {
        return listToBeDecorated.hashCode();
    }

    @Override
    public T get(final int i) {
        return listToBeDecorated.get(i);
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

    @Override
    public Spliterator<T> spliterator() {
        return listToBeDecorated.spliterator();
    }

    @Override
    public boolean removeIf(final Predicate<? super T> predicate) {
        return listToBeDecorated.removeIf(predicate);
    }

    @Override
    public Stream<T> stream() {
        return listToBeDecorated.stream();
    }

    @Override
    public Stream<T> parallelStream() {
        return listToBeDecorated.parallelStream();
    }

    @Override
    public void forEach(final Consumer<? super T> consumer) {
        listToBeDecorated.forEach(consumer);
    }

}
