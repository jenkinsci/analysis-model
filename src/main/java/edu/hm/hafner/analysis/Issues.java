package edu.hm.hafner.analysis;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import edu.hm.hafner.util.Ensure;
import edu.hm.hafner.util.NoSuchElementException;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

/**
 * Container for {@link Issue issues}. Finds and filters issues based on different properties. In order to create issues
 * use the provided {@link IssueBuilder builder} class.
 *
 * @author Ullrich Hafner
 */
// TODO: findByProperty with hardcoded properties?
public class Issues implements Iterable<Issue>, Serializable {
    private final List<Issue> elements = new ArrayList<>();
    private final StringBuilder logMessages = new StringBuilder();
    private final int[] sizeOfPriority = new int[Priority.values().length];
    private String path;
    private String moduleName;

    /**
     * Returns a new issues container. Appends all of the issues in the specified array to the end of this container.
     * The order of the issues in the individual containers is preserved.
     *
     * @param issues the issues to merge
     */
    public static Issues merge(final Issues... issues) {
        Issues merged = new Issues();
        merged.addAll(issues);
        return merged;
    }

    /**
     * Returns a new issues container. Appends all of the issues in the specified array to the end of this container.
     * The order of the issues in the individual containers is preserved.
     *
     * @param issues the issues to merge
     */
    public static Issues merge(final Collection<Issues> issues) {
        return merge(issues.toArray(new Issues[0]));
    }

    public Issues(final String path) {
        this.path = path;
    }

    public Issues() {
        this(StringUtils.EMPTY);
    }

    public Issues(final Collection<? extends Issue> issues) {
        addAll(issues);
    }

    /**
     * Appends the specified element to the end of this container.
     *
     * @param issue the issue to append
     * @return returns the appended issue
     */
    public Issue add(final Issue issue) {
        elements.add(issue);
        sizeOfPriority[issue.getPriority().ordinal()]++;

        return issue;
    }

    /**
     * Appends all of the elements in the specified collection to the end of this container, in the order that they are
     * returned by the specified collection's iterator.
     *
     * @param issues the issues to append
     * @return returns the appended issues
     */
    public Collection<? extends Issue> addAll(final Collection<? extends Issue> issues) {
        for (Issue issue : issues) {
            add(issue);
        }
        return issues;
    }

    /**
     * Appends all of the elements in the specified array of issues to the end of this container, in the order that they
     * are returned by the specified collection's iterator.
     *
     * @param issues the issues to append
     */
    public void addAll(final Issues... issues) {
        Ensure.that(issues).isNotEmpty();

        for (Issues container : issues) {
            addAll(container.elements);
        }
    }

    /**
     * Removes the the issue with the specified ID.
     *
     * @param id the ID of the issue
     * @return the removed issue
     * @throws NoSuchElementException if there is no such issue found
     */
    public Issue remove(final UUID id) {
        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i).getId().equals(id)) {
                Issue issue = elements.get(i);
                sizeOfPriority[issue.getPriority().ordinal()]--;
                return elements.remove(i);
            }
        }
        throw new NoSuchElementException("No issue found with id %s.", id);
    }

    /**
     * Returns all issues of this container.
     *
     * @return all issues
     */
    public ImmutableSet<Issue> all() {
        return ImmutableSet.copyOf(elements);
    }

    /**
     * Returns the issue with the specified ID.
     *
     * @param id the ID of the issue
     * @return the found issue
     * @throws NoSuchElementException if there is no such issue found
     */
    public Issue findById(final UUID id) {
        for (Issue issue : elements) {
            if (issue.getId().equals(id)) {
                return issue;
            }
        }
        throw new NoSuchElementException("No issue found with id %s.", id);
    }

    /**
     * Finds all issues that match the specified criterion.
     *
     * @param criterion the filter criterion
     * @return the found issues
     */
    public ImmutableList<Issue> findByProperty(final Predicate<? super Issue> criterion) {
        return filterElements(criterion).collect(collectingAndThen(toList(), ImmutableList::copyOf));
    }

    /**
     * Finds all issues that match the specified criterion.
     *
     * @param criterion the filter criterion
     * @return the found issues
     */
    public Issues filter(final Predicate<? super Issue> criterion) {
        return new Issues(filterElements(criterion).collect(toList()));
    }

    private Stream<Issue> filterElements(final Predicate<? super Issue> criterion) {
        return elements.stream().filter(criterion);
    }

    @Override
    public Iterator<Issue> iterator() {
        return elements.iterator();
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
     * Returns the number of issues of the specified priority.
     *
     * @param priority the priority of the issues
     * @return total number of issues
     */
    public int getSizeOf(final Priority priority) {
        return sizeOfPriority[priority.ordinal()];
    }

    /**
     * Returns the number of issues of the specified priority.
     *
     * @param priority the priority of the issues
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
     * @param index the index
     * @return the issue at the specified index
     */
    public Issue get(final int index) {
        return elements.get(index);
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
    public SortedSet<String> getModules() {
        return getProperties(issue -> issue.getModuleName());
    }

    /**
     * Returns the affected packages for all issues of this container.
     *
     * @return the affected packages
     */
    public SortedSet<String> getPackages() {
        return getProperties(issue -> issue.getPackageName());
    }

    /**
     * Returns the affected files for all issues of this container.
     *
     * @return the affected files
     */
    public SortedSet<String> getFiles() {
        return getProperties(issue -> issue.getFileName());
    }

    /**
     * Returns the used categories for all issues of this container.
     *
     * @return the used categories
     */
    public SortedSet<String> getCategories() {
        return getProperties(issue -> issue.getCategory());
    }

    /**
     * Returns the used types for all issues of this container.
     *
     * @return the used types
     */
    public SortedSet<String> getTypes() {
        return getProperties(issue -> issue.getType());
    }

    /**
     * Returns the names of the tools that did report the issues of this container.
     *
     * @return the tools
     */
    public SortedSet<String> getToolNames() {
        return getProperties(issue -> issue.getOrigin());
    }

    // TODO: paging for values?
    // getFiles(int start, int end)

    /**
     * Returns the different values for a given property for all issues of this container.
     *
     * @param propertiesMapper the properties mapper
     * @param <R>              the type of the returned values
     * @return the set of different values
     * @see #getFiles()
     */
    public <R> SortedSet<R> getProperties(final Function<? super Issue, ? extends R> propertiesMapper) {
        return elements.stream().map(propertiesMapper)
                .collect(collectingAndThen(toSet(), ImmutableSortedSet::copyOf));
    }

    public Map<String, Integer> getPropertyCount(final Function<? super Issue, ? extends String> propertiesMapper) {
        return elements.stream().collect(groupingBy(propertiesMapper, reducing(0, e -> 1, Integer::sum)));
    }

    /**
     * Returns a shallow copy of this issue container.
     *
     * @return a new issue container that contains the same elements in the same order
     */
    public Issues copy() {
        Issues copied = new Issues();
        copied.addAll(all());
        return copied;
    }

    /**
     * Sets the absolute path for all affected files to the specified value.
     *
     * @param path the path
     */
    public void setPath(final String path) {
        this.path = path;
        // TODO: issue property? Or is this better suited in the AbsoluteFileNamesMapper
    }

    public void setModuleName(final String moduleName) {
        this.moduleName = moduleName;
        // TODO: issue property?
    }

    /**
     * Logs the specified message.
     *
     * @param format A <a href="../util/Formatter.html#syntax">format string</a>
     * @param args   Arguments referenced by the format specifiers in the format string.  If there are more arguments
     *               than format specifiers, the extra arguments are ignored.  The number of arguments is variable and
     *               may be zero.
     */
    public void log(final String format, final Object... args) {
        logMessages.append(String.format(format, args));
        logMessages.append('\n');
    }

    public String getLogMessages() {
        return logMessages.toString();
    }

    public Issues withOrigin(final String id) {
        IssueBuilder builder = new IssueBuilder();
        Issues copy = new Issues();
        for (Issue element : elements) {
            copy.add(builder.copy(element).setOrigin(id).build());
        }
        return copy;
    }
}
