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
 * A list that cannot store null values.
 * <p>
 * This implementation uses delegation/decorating.
 * <p>
 * Vorteile Delegation/Decorator: - keine Kopplung an konkrete Klassen, nur an Interface - Erhalten der Encapsulation,
 * da nur public Methoden verdendet werden Nachteile: - alle Methoden müssen Delegiert werden, keine Compiler
 * Unterstützung (zumindest bei Java) dadurch bei großen Interfaces wie List viel Boilerplate Code.
 * </p>
 *
 * @param <T>
 *         Type of the list's elements.
 */
public class NullSafeList<T> implements List<T> {
    private final List<T> list;

    /**
     * Creates a new {@link NullSafeList} that wraps an existing list.
     *
     * @param list
     *         A list that is wrapped in order to provide null safety.
     */
    public NullSafeList(final List<T> list) {
        Objects.requireNonNull(list);
        checkContainsNullElements(list);
        this.list = list;
    }

    // Customized delegated method calls

    @Override
    public T set(final int index, final T value) {
        return list.set(index, Objects.requireNonNull(value));
    }

    @Override
    public void add(final int index, final T value) {
        list.add(index, Objects.requireNonNull(value));
    }

    @Override
    public boolean add(final T value) {
        return list.add(Objects.requireNonNull(value));
    }

    @Override
    public boolean addAll(final Collection<? extends T> collection) {
        checkContainsNullElements(collection);
        return list.addAll(collection);
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends T> collection) {
        checkContainsNullElements(collection);
        return list.addAll(index, collection);
    }

    // Delegated Methods

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
    public Iterator<T> iterator() {
        return list.iterator();
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <I> I[] toArray(final I[] t1s) {
        return list.toArray(t1s);
    }

    @Override
    public boolean remove(final Object o) {
        return list.remove(o);
    }

    @Override
    public boolean containsAll(final Collection<?> collection) {
        return list.containsAll(collection);
    }

    @Override
    public boolean removeAll(final Collection<?> collection) {
        return list.removeAll(collection);
    }

    @Override
    public boolean retainAll(final Collection<?> collection) {
        return list.retainAll(collection);
    }

    @Override
    public void replaceAll(final UnaryOperator<T> unaryOperator) {
        list.replaceAll(unaryOperator);
    }

    @Override
    public void sort(final Comparator<? super T> comparator) {
        list.sort(comparator);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public boolean equals(final Object o) {
        return list.equals(o);
    }

    @Override
    public int hashCode() {
        return list.hashCode();
    }

    @Override
    public T get(final int i) {
        return list.get(i);
    }

    @Override
    public T remove(final int i) {
        return list.remove(i);
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
    public ListIterator<T> listIterator() {
        return list.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(final int i) {
        return list.listIterator(i);
    }

    @Override
    public List<T> subList(final int i, final int i1) {
        return list.subList(i, i1);
    }

    @Override
    public Spliterator<T> spliterator() {
        return list.spliterator();
    }

    @Override
    public boolean removeIf(final Predicate<? super T> predicate) {
        return list.removeIf(predicate);
    }

    @Override
    public Stream<T> stream() {
        return list.stream();
    }

    @Override
    public Stream<T> parallelStream() {
        return list.parallelStream();
    }

    @Override
    public void forEach(final Consumer<? super T> consumer) {
        list.forEach(consumer);
    }

    private void checkContainsNullElements(Collection<? extends T> collection) {
        if (collection.stream().anyMatch(Objects::isNull)) {
            throw new NullPointerException("Can not add collection containing null values.");
        }
    }
}
