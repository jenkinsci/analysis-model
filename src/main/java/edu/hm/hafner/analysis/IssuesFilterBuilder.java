package edu.hm.hafner.analysis;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Builds an issues filter instance.
 */
public class IssuesFilterBuilder {

    private List<IssueFilter> filters = new LinkedList<>();

    /**
     * Adds an inclusion filter to the resulting filter.
     *
     * @return this
     */
    public IssuesFilterBuilder add(final IssueFilter filter) {
        filters.add(filter);

        return this;
    }


    /**
     * Build the filter instance.
     *
     * @return Filter built from config. No-op, when none configured.
     */
    public IssuesFilter build() {
        return new IssuesFilter(Collections.unmodifiableCollection(filters));
    }
}
