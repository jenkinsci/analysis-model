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
 * Controls the life cycle of issues. Finds issues based on different properties. In order to create issues use the
 * provided {@link IssueBuilder builder} class.
 *
 * @author Ullrich Hafner
 */
// TODO: paging for values?
//       findByProperty hardcoded properties?
public class Issues implements Iterable<Issue> {
    private final List<Issue> elements = new ArrayList<>();
    private int[] sizeOfPriority = new int[Priority.values().length];

    public Issue add(final Issue issue) {
        elements.add(issue);
        sizeOfPriority[issue.getPriority().ordinal()]++;

        return issue;
    }

    public ImmutableSet<Issue> all() {
        return ImmutableSet.copyOf(elements);
    }

    public Issue findById(final UUID id) {
        for (Issue issue : elements) {
            if (issue.getUuid().equals(id)) {
                return issue;
            }
        }
        throw new NoSuchElementException("No issue found with id %s.", id);
    }

    public ImmutableList<Issue> findByProperty(final Predicate<? super Issue> selector) {
        return elements.stream().filter(selector)
                .collect(collectingAndThen(toList(), ImmutableList::copyOf));
    }

    public int size() {
        return elements.size();
    }

    public int getSize() {
        return size();
    }

    @Override
    public Iterator<Issue> iterator() {
        return elements.iterator();
    }

    public int getHighPrioritySize() {
        return sizeOfPriority[Priority.HIGH.ordinal()];
    }

    public int getNormalPrioritySize() {
        return sizeOfPriority[Priority.NORMAL.ordinal()];
    }

    public int getLowPrioritySize() {
        return sizeOfPriority[Priority.LOW.ordinal()];
    }

    public Issue get(final int index) {
        return elements.get(index);
    }

    @Override
    public String toString() {
        return String.format("%d issues", size());
    }

    public Set<String> getFiles() {
        return getProperties(issue -> issue.getFileName());
    }

    public <R> Set<R> getProperties(Function<? super Issue, ? extends R> mapper) {
        return elements.stream().map(mapper)
                .collect(collectingAndThen(toSet(), ImmutableSet::copyOf));
    }
}
