package edu.hm.hafner.analysis;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Filters an {@link Issues} instance using the passed filters. The result is contained in a new instance.
 */
public class IssuesFilter {

    private final Collection<IssueFilter> filters;

    /**
     * Creates a new instance of {@link IssuesFilter}
     *
     * @param filters
     *         to apply
     */
    IssuesFilter(final Collection<IssueFilter> filters) {
        this.filters = filters;
    }

    /**
     * Filters the issues instance using the given filters.
     *
     * @param issues
     *         to filter
     *
     * @return new {@link Issues} instance containing filtered result
     */
    public Issues filter(final Issues issues) {
        return filters.isEmpty() ? issues.copy()
                : new Issues(issues.all().stream()
                .filter(i -> filters.stream().allMatch(s -> s.isIncluded(i)))
                .collect(Collectors.toList()));
    }
}
