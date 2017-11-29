package edu.hm.hafner.analysis;

import java.util.Collection;

/**
 * Builds for exclusion filters.
 */
public class ExcludeIssueFilterBuilder extends IssueFilterBuilder {

    /**
     * Supplies filter constructor to base ctor.
     */
    ExcludeIssueFilterBuilder() {
        super(ExcludeFilter::new);
    }

    /**
     * Includes Issues when none of the passed filters matches.
     */
    private static class ExcludeFilter implements IssueFilter {

        private final Collection<IssueFilter> filters;

        ExcludeFilter(final Collection<IssueFilter> filters) {
            this.filters = filters;
        }

        @Override
        public boolean isIncluded(final Issue issue) {
            return filters.stream().noneMatch(f -> f.isIncluded(issue));
        }
    }
}
