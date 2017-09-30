package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import edu.hm.hafner.util.NoSuchElementException;
import static java.util.stream.Collectors.*;

/**
 * Container for {@link Issue issues}. Finds and filters issues based on different properties.
 * In order to create issues use the provided {@link IssueBuilder builder} class.
 *
 * @author Ullrich Hafner
 */
// TODO: paging for values?
// TODO: findByProperty with hardcoded properties?
public class Issues implements Iterable<Issue> {
    private final List<Issue> elements = new ArrayList<>();
    private final int[] sizeOfPriority = new int[Priority.values().length];

    /**
     * Adds the specified issue at the tail of this container.
     *
     * @param issue the issue to add
     * @return returns the added issue
     */
    public Issue add(final Issue issue) {
        elements.add(issue);
        sizeOfPriority[issue.getPriority().ordinal()]++;

        return issue;
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
        return elements.stream().filter(criterion)
                .collect(collectingAndThen(toList(), ImmutableList::copyOf));
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
     * Returns the number of issues in this container.
     *
     * @return total number of issues
     */
    public int getSize() {
        return size();
    }

    @Override
    public Iterator<Issue> iterator() {
        return elements.iterator();
    }

    /**
     * Returns the number of high priority issues in this container.
     *
     * @return total number of high priority issues
     */
    public int getHighPrioritySize() {
        return sizeOfPriority[Priority.HIGH.ordinal()];
    }

    /**
     * Returns the number of normal priority issues in this container.
     *
     * @return total number of normal priority issues
     */
    public int getNormalPrioritySize() {
        return sizeOfPriority[Priority.NORMAL.ordinal()];
    }

    /**
     * Returns the number of low priority issues in this container.
     *
     * @return total number of low priority of issues
     */
    public int getLowPrioritySize() {
        return sizeOfPriority[Priority.LOW.ordinal()];
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
     * Returns the affected files for all issues of this container.
     *
     * @return the affected files
     */
    public Set<String> getFiles() {
        return getProperties(issue -> issue.getFileName());
    }

    /**
     * Returns the different values for a given property for all issues of this container.
     *
     * @param propertiesMapper the properties mapper
     * @param <R> the type of the returned values
     * @return the set of different values
     * @see #getFiles()
     */
    public <R> Set<R> getProperties(final Function<? super Issue, ? extends R> propertiesMapper) {
        return elements.stream().map(propertiesMapper)
                .collect(collectingAndThen(toSet(), ImmutableSet::copyOf));
    }
}
