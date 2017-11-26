package edu.hm.hafner.analysis.filter;


import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Streams;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;

public class IssueFilter {

    private final Collection<IssuePropertyFilter> filterFunctions;
    public IssueFilter(Collection<IssuePropertyFilter> filter){
        this.filterFunctions = filter;
    }



    public Issues apply(final Issues issues) {
        Iterator<Issue> iterator = issues.iterator();
        return new Issues(apply(Streams.stream(iterator)).collect(Collectors.toList()));
    }

    public Stream<Issue> apply(Stream<Issue> issues) {
        if(filterFunctions.size() == 0)
            return issues;
        return issues
                .filter(f -> filterFunctions.stream()
                .anyMatch(ff -> ff.apply(f)));
    }
}
