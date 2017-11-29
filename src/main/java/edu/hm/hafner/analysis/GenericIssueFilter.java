package edu.hm.hafner.analysis;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Generic filter for issues.
 *
 * @param <T>
 *         type of the property to filter for
 */
public class GenericIssueFilter<T> implements IssueFilter {

    private final Predicate<T> predicate;
    private final Function<Issue, T> propertySupplier;

    /**
     * Creates a new instance of {@link GenericIssueFilter}
     *
     * @param propertySupplier
     *         supplies the issues property to test
     * @param filter
     *         filter function checking the properties value.
     */
    public GenericIssueFilter(final Function<Issue, T> propertySupplier, final Predicate<T> filter) {
        this.propertySupplier = propertySupplier;
        this.predicate = filter;
    }

    @Override
    public boolean isIncluded(final Issue issue) {
        return this.predicate.test(propertySupplier.apply(issue));
    }
}
