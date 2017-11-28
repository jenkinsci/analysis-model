package edu.hm.hafner.analysis.filter;


import java.util.Iterator;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Streams;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;

public class IssueFilter {

    private final Predicate<Issue> filterFunctions;
    public IssueFilter(Predicate<Issue> filter){
        this.filterFunctions = filter;
    }



    public Issues apply(final Issues issues) {
        Iterator<Issue> iterator = issues.iterator();
        return new Issues(apply(Streams.stream(iterator)).collect(Collectors.toList()));
    }

    public Stream<Issue> apply(Stream<Issue> issues) {
        return issues.filter(filterFunctions::test);
    }
}
