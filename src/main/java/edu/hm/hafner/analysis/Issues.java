package edu.hm.hafner.analysis;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.api.set.sorted.ImmutableSortedSet;
import org.eclipse.collections.impl.collector.Collectors2;
import org.eclipse.collections.impl.factory.Lists;

import edu.hm.hafner.util.NoSuchElementException;
import static java.util.stream.Collectors.*;

/**
 * A set of {@link Issue issues}: it contains no duplicate elements, i.e. it models the mathematical <i>set</i>
 * abstraction. Furthermore, this set of issues provides a <i>total ordering</i> on its elements. I.e., the issues in
 * this set are ordered by their index in this set: the first added issue is at position 0, the second added issues is
 * at position 1, and so on. <p> Additionally, this set of issues provides methods to find and filter issues based on
 * different properties. In order to create issues use the provided {@link IssueBuilder builder} class. </p>
 *
 * @param <T>
 *         type of the issues
 *
 * @author Ullrich Hafner
 */
public class Issues<T extends Issue> implements Iterable<T>, Serializable {
    private final Set<T> elements = new LinkedHashSet<>();
    private final int[] sizeOfPriority = new int[Priority.values().length];
    private final List<String> logMessages = new ArrayList<>();

    private int sizeOfDuplicates = 0;

    /**
     * Creates and returns a new set of issues that contains all issues of the specified {@link Issues} instances. The
     * issues of the specified array element are appended to the end of this container. The order of the issues in the
     * individual containers is preserved.
     *
     * @param issues
     *         the issues to merge
     * @param <T>
     *         type of the issues
     *
     * @return all issues
     */
    @SafeVarargs
    public static <T extends Issue> Issues<T> merge(final Issues<T>... issues) {
        Issues<T> merged = new Issues<>();
        for (Issues<T> issue : issues) {
            merged.addAll(issue);
        }
        return merged;
    }

    /**
     * Returns a predicate that checks if the package name of an issue is equal to the specified package name.
     *
     * @param packageName
     *         the package name to match
     *
     * @return the predicate
     */
    public static Predicate<Issue> byPackageName(final String packageName) {
        return issue -> issue.getPackageName().equals(packageName);
    }

    /**
     * Returns a predicate that checks if the file name of an issue is equal to the specified file name.
     *
     * @param fileName
     *         the package name to match
     *
     * @return the predicate
     */
    public static Predicate<Issue> byFileName(final String fileName) {
        return issue -> issue.getFileName().equals(fileName);
    }

    /**
     * Creates a new empty instance of {@link Issues}.
     */
    public Issues() {
        // no elements to add
    }

    /**
     * Creates a new instance of {@link Issues} that will be initialized with the specified collection of {@link Issue}
     * instances.
     *
     * @param issues
     *         the initial set of issues for this instance
     */
    public Issues(final Collection<? extends T> issues) {
        for (T issue : issues) {
            add(issue);
        }
    }

    /**
     * Creates a new instance of {@link Issues} that will be initialized with the specified collection of {@link Issue}
     * instances.
     *
     * @param issues
     *         the initial set of issues for this instance
     */
    public Issues(final Stream<? extends T> issues) {
        issues.forEach(issue -> add(issue));
    }

    /**
     * Appends the specified element to the end of this container.
     *
     * @param issue
     *         the issue to append
     * @param additionalIssues
     *         the additional issue to append
     *
     * @return {@code true} if this set did not already contain one of the specified issues, {@code false} if a
     *         duplicate has been dropped
     */
    @SafeVarargs
    public final boolean add(final T issue, final T... additionalIssues) {
        boolean hasNoDuplicate = add(issue);
        for (T additional : additionalIssues) {
            hasNoDuplicate &= add(additional);
        }
        return hasNoDuplicate;
    }

    private boolean add(final T issue) {
        if (elements.contains(issue)) {
            sizeOfDuplicates++;
            return false;
        }
        elements.add(issue);
        sizeOfPriority[issue.getPriority().ordinal()]++;

        return true;
    }

    /**
     * Appends all of the elements in the specified collection to the end of this container, in the order that they are
     * returned by the specified collection's iterator.
     *
     * @param issues
     *         the issues to append
     *
     * @return {@code true} if this set did not already contain one of the specified issues, {@code false} if a
     *         duplicate has been dropped
     */
    public boolean addAll(final Collection<? extends T> issues) {
        boolean hasNoDuplicate = true;
        for (T issue : issues) {
            hasNoDuplicate &= add(issue);
        }
        return hasNoDuplicate;
    }

    /**
     * Appends all of the elements in the specified array of issues to the end of this container, in the order that they
     * are returned by the specified collection's iterator.
     *
     * @param issues
     *         the issues to append
     * @param additionalIssues
     *         the additional issue to append
     *
     * @return {@code true} if this set did not already contain one of the specified issues, {@code false} if a
     *         duplicate has been dropped
     */
    @SafeVarargs
    public final boolean addAll(final Issues<T> issues, final Issues<T>... additionalIssues) {
        boolean hasNoDuplicate = addAll(issues.elements);
        for (Issues<T> other : additionalIssues) {
            hasNoDuplicate &= addAll(other.elements);
        }
        return hasNoDuplicate;
    }

    /**
     * Removes the issue with the specified ID. Note that the number of reported duplicates is not affected by calling
     * this method.
     *
     * @param id
     *         the ID of the issue
     *
     * @throws NoSuchElementException
     *         if there is no such issue found
     */
    public void remove(final UUID id) {
        for (T element : elements) {
            if (element.getId().equals(id)) {
                elements.remove(element);
                return;
            }
        }
        throw new NoSuchElementException("No issue found with id %s.", id);
    }

    /**
     * Returns the issue with the specified ID.
     *
     * @param id
     *         the ID of the issue
     *
     * @return the found issue
     * @throws NoSuchElementException
     *         if there is no such issue found
     */
    public T findById(final UUID id) {
        for (T issue : elements) {
            if (issue.getId().equals(id)) {
                return issue;
            }
        }
        throw new NoSuchElementException("No issue found with id %s.", id);
    }

    /**
     * Finds all issues that match the specified criterion.
     *
     * @param criterion
     *         the filter criterion
     *
     * @return the found issues
     */
    public ImmutableSet<T> findByProperty(final Predicate<? super T> criterion) {
        return filterElements(criterion).collect(Collectors2.toImmutableSet());
    }

    /**
     * Finds all issues that match the specified criterion.
     *
     * @param criterion
     *         the filter criterion
     *
     * @return the found issues
     */
    public Issues<T> filter(final Predicate<? super T> criterion) {
        return new Issues<>(filterElements(criterion).collect(toList()));
    }

    private Stream<T> filterElements(final Predicate<? super T> criterion) {
        return elements.stream().filter(criterion);
    }

    @Nonnull
    @Override
    public Iterator<T> iterator() {
        return elements.iterator();
    }

    public Stream<Issue> stream() {
        return StreamSupport.stream(Spliterators.spliterator(iterator(), 0L, Spliterator.NONNULL),false);
    }

    /**
     * Returns the number of issues in this container.
     *
     * @return total number of issues
     */
    public int size() {
        return elements.size();
    }

    /**
     * Returns whether this container is empty.
     *
     * @return {@code true} if this container is empty, {@code false} otherwise
     * @see #isNotEmpty()
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns whether this container is not empty.
     *
     * @return {@code true} if this container is not empty, {@code false} otherwise
     * @see #isEmpty()
     */
    public boolean isNotEmpty() {
        return !isEmpty();
    }

    /**
     * Returns the number of issues in this container.
     *
     * @return total number of issues
     */
    public int getSize() {
        return size();
    }

    /**
     * Returns the number of duplicates. Every issue that has been added to this container, but already is part of this
     * container (based on {@link #equals(Object)}) is counted as a duplicate. Duplicates are not stored in this
     * container.
     *
     * @return total number of duplicates
     */
    public int getDuplicatesSize() {
        return sizeOfDuplicates;
    }

    /**
     * Returns the number of issues of the specified priority.
     *
     * @param priority
     *         the priority of the issues
     *
     * @return total number of issues
     */
    public int getSizeOf(final Priority priority) {
        return sizeOfPriority[priority.ordinal()];
    }

    /**
     * Returns the number of issues of the specified priority.
     *
     * @param priority
     *         the priority of the issues
     *
     * @return total number of issues
     */
    public int sizeOf(final Priority priority) {
        return getSizeOf(priority);
    }

    /**
     * Returns the number of high priority issues in this container.
     *
     * @return total number of high priority issues
     */
    public int getHighPrioritySize() {
        return getSizeOf(Priority.HIGH);
    }

    /**
     * Returns the number of normal priority issues in this container.
     *
     * @return total number of normal priority issues
     */
    public int getNormalPrioritySize() {
        return getSizeOf(Priority.NORMAL);
    }

    /**
     * Returns the number of low priority issues in this container.
     *
     * @return total number of low priority of issues
     */
    public int getLowPrioritySize() {
        return getSizeOf(Priority.LOW);
    }

    /**
     * Returns the issue with the specified index.
     *
     * @param index
     *         the index
     *
     * @return the issue at the specified index
     * @throws IndexOutOfBoundsException
     *         if there is no element for the given index
     */
    public T get(final int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("No such index " + index + " in " + toString());
        }
        Iterator<T> all = elements.iterator();
        for (int i = 0; i < index; i++) {
            all.next(); // skip this element
        }
        return all.next();
    }

    @Override
    public String toString() {
        return String.format("%d issues", size());
    }

    /**
     * Returns the affected modules for all issues of this container.
     *
     * @return the affected modules
     */
    public ImmutableSortedSet<String> getModules() {
        return getProperties(issue -> issue.getModuleName());
    }

    /**
     * Returns the affected packages for all issues of this container.
     *
     * @return the affected packages
     */
    public ImmutableSortedSet<String> getPackages() {
        return getProperties(issue -> issue.getPackageName());
    }

    /**
     * Returns the affected files for all issues of this container.
     *
     * @return the affected files
     */
    public ImmutableSortedSet<String> getFiles() {
        return getProperties(issue -> issue.getFileName());
    }

    /**
     * Returns the used categories for all issues of this container.
     *
     * @return the used categories
     */
    public ImmutableSortedSet<String> getCategories() {
        return getProperties(issue -> issue.getCategory());
    }

    /**
     * Returns the used types for all issues of this container.
     *
     * @return the used types
     */
    public ImmutableSortedSet<String> getTypes() {
        return getProperties(issue -> issue.getType());
    }

    /**
     * Returns the names of the tools that did report the issues of this container.
     *
     * @return the tools
     */
    public ImmutableSortedSet<String> getToolNames() {
        return getProperties(issue -> issue.getOrigin());
    }

    /**
     * Returns the different values for a given property for all issues of this container.
     *
     * @param propertiesMapper
     *         the properties mapper that selects the property
     *
     * @return the set of different values
     * @see #getFiles()
     */
    public ImmutableSortedSet<String> getProperties(final Function<? super T, String> propertiesMapper) {
        return elements.stream().map(propertiesMapper).collect(Collectors2.toImmutableSortedSet());
    }

    /**
     * Returns the number of occurrences for every existing value of a given property for all issues of this container.
     *
     * @param propertiesMapper
     *         the properties mapper that selects the property to evaluate
     *
     * @return a mapping of: property value -> number of issues for that value
     * @see #getProperties(Function)
     */
    public Map<String, Integer> getPropertyCount(final Function<? super T, String> propertiesMapper) {
        return elements.stream().collect(groupingBy(propertiesMapper, reducing(0, e -> 1, Integer::sum)));
    }

    /**
     * Returns a shallow copy of this issue container.
     *
     * @return a new issue container that contains the same elements in the same order
     */
    public Issues<T> copy() {
        Issues<T> copied = new Issues<>();
        copied.addAll(elements);
        return copied;
    }

    /**
     * Logs the specified message. Use this method to log any useful information when composing this set of issues.
     *
     * @param format
     *         A <a href="../util/Formatter.html#syntax">format string</a>
     * @param args
     *         Arguments referenced by the format specifiers in the format string.  If there are more arguments than
     *         format specifiers, the extra arguments are ignored.  The number of arguments is variable and may be
     *         zero.
     *
     * @see #getLogMessages()
     */
    public void log(final String format, final Object... args) {
        logMessages.add(String.format(format, args));
    }

    /**
     * Returns the log messages that have been reported since the creation of this set of issues.
     *
     * @return the log messages
     */
    public ImmutableList<String> getLogMessages() {
        return Lists.immutable.ofAll(logMessages);
    }
}
