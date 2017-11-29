package edu.hm.hafner.analysis;

import java.util.Collection;

/**
 * Builds a new inclusion filter.
 */
public class IncludeIssueFilterBuilder extends IssueFilterBuilder {

    /**
     * Creates a new instance of @link{IncludeIssueFilterBuilder}.
     */
    IncludeIssueFilterBuilder() {
        super(IncludeIssueFilter::new);
    }

    /**
     * Includes Issues when any of the filters matches.
     */
    private static class IncludeIssueFilter implements IssueFilter {

        private Collection<IssueFilter> filters;

        IncludeIssueFilter(final Collection<IssueFilter> filters) {
            this.filters = filters;
        }

        @Override
        public boolean isIncluded(final Issue issue) {

            return filters.stream().anyMatch(f -> f.isIncluded(issue));
        }
    }
}
