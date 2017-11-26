package edu.hm.hafner.analysis.filter;

import java.util.function.Function;
import java.util.regex.Pattern;

import edu.hm.hafner.analysis.Issue;

public class IssuePropertyFilter implements Function<Issue, Boolean> {

    private final boolean include;
    private final Pattern pattern;
    private final Function<Issue, String> propertyToFilter;
    public IssuePropertyFilter(String pattern, Function<Issue, String> propertyToFilter, boolean include){
        this.pattern = Pattern.compile(pattern);;
        this.include = include;
        this.propertyToFilter = propertyToFilter;
    }

    public boolean isInclude(){
        return include;
    }

    @Override
    public Boolean apply(final Issue issue) {

        boolean b =  this.pattern.matcher(propertyToFilter.apply(issue)).matches() == include;
        System.out.println(propertyToFilter.apply(issue) + " vs " + pattern.pattern() + " = "  + b);
        return b;
    }
}
