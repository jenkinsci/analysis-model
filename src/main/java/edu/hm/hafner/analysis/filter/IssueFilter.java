package edu.hm.hafner.analysis.filter;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;

import edu.hm.hafner.analysis.Issue;

public class IssueFilter implements Function<Stream<Issue>, Stream<Issue>>{

    private final Collection<IssuePropertyFilter> filterFunctions;
    public IssueFilter(Collection<IssuePropertyFilter> filter){
        this.filterFunctions = filter;
    }


    @Override
    public Stream<Issue> apply(final Stream<Issue> issueStream) {
        return issueStream.filter(f ->filterFunctions.stream().anyMatch(ff -> ff.apply(f)));
    }
}
