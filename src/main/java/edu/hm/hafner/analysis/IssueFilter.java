package edu.hm.hafner.analysis;


import java.util.Iterator;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Streams;

/**
 * Filters a Issues.
 * @author Raphael Furch
 */
public class IssueFilter {

    /**
     * filter criteria.
     */
    private final Predicate<Issue> filterFunctions;

    /**
     * C.
     * @param filter = filter criteria.
     */
    public IssueFilter(Predicate<Issue> filter){
        this.filterFunctions = filter;
    }


    /**
     * Apply filter.
     * @param issues = issues to filter.
     * @return filtered issues.
     */
    public Issues apply(final Issues issues) {
        return new Issues(Streams.stream(issues.iterator()).filter(filterFunctions::test).collect(Collectors.toList()));
    }
}
