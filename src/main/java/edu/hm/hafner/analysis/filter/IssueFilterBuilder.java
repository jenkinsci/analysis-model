package edu.hm.hafner.analysis.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import edu.hm.hafner.analysis.Issue;

public class IssueFilterBuilder {

    private Collection<Predicate<Issue>> filterInclude = new ArrayList<>();
    private Collection<Predicate<Issue>> filterExclude = new ArrayList<>();
    private boolean includeCombineWithOr = true;
    private boolean excludeCombineWithOr = true;

    private void addNewFilter(final Collection<String> pattern, Function<Issue, String> propertyToFilter, boolean include ){
        if(include){
            String allPatternInOneString = "("+pattern.stream().collect(Collectors.joining(includeCombineWithOr?")|(":")&&("))+")";
            this.filterInclude.add((issueToFilter) ->  Pattern.compile(allPatternInOneString)
                    .matcher(propertyToFilter.apply(issueToFilter)).matches() == include);
        }
        else{
            String allPatternInOneString = "("+pattern.stream().collect(Collectors.joining(excludeCombineWithOr?")|(":")&&("))+")";
            this.filterExclude.add((issueToFilter) ->  Pattern.compile(allPatternInOneString)
                    .matcher(propertyToFilter.apply(issueToFilter)).matches() == include);
        }
    }

    public IssueFilter createIssueFilter(){
        return new IssueFilter(filterInclude.stream().reduce(includeCombineWithOr?Predicate::or:Predicate::and)
                .orElse((issue)-> true).and(
        filterExclude.stream().reduce(excludeCombineWithOr?Predicate::or:Predicate::and).orElse((issue)-> true)));
    }

    public IssueFilterBuilder IncludeCombineFilterWithOr(boolean withOr){
        this.includeCombineWithOr = withOr;
        return this;
    }
    public IssueFilterBuilder ExcludeCombineFilterWithOr(boolean withOr){
        this.excludeCombineWithOr = withOr;
        return this;
    }

    //<editor-fold desc="File name">
    public IssueFilterBuilder setIncludeFilenameFilter(final Collection<String> pattern){
        addNewFilter(pattern, Issue::getFileName, true);
        return this;
    }
    public IssueFilterBuilder setIncludeFilenameFilter(final String... pattern){
        return setIncludeFilenameFilter(Arrays.asList(pattern));
    }
    public IssueFilterBuilder setExcludeFilenameFilter(final Collection<String> pattern){
        addNewFilter(pattern, Issue::getFileName, false);
        return this;
    }
    public IssueFilterBuilder setExcludeFilenameFilter(final String... pattern){
        return setExcludeFilenameFilter(Arrays.asList(pattern));
    }
    //</editor-fold>

    //<editor-fold desc="Package name">
    public IssueFilterBuilder setIncludePackageNameFilter(final Collection<String> pattern){
        addNewFilter(pattern, Issue::getPackageName, true);
        return this;
    }
    public IssueFilterBuilder setIncludePackageNameFilter(final String... pattern){
        return setIncludePackageNameFilter(Arrays.asList(pattern));
    }
    public IssueFilterBuilder setExcludePackageNameFilter(final Collection<String> pattern){
        addNewFilter(pattern, Issue::getPackageName, false);
        return this;
    }
    public IssueFilterBuilder setExcludePackageNameFilter(final String... pattern){
        return setExcludePackageNameFilter(Arrays.asList(pattern));
    }
    //</editor-fold>

    //<editor-fold desc="Module name">
    public IssueFilterBuilder setIncludeModuleNameFilter(final Collection<String> pattern){
        addNewFilter(pattern, Issue::getModuleName, true);
        return this;
    }
    public IssueFilterBuilder setIncludeModuleNameFilter(final String... pattern){
        return setIncludeModuleNameFilter(Arrays.asList(pattern));
    }
    public IssueFilterBuilder setExcludeModuleNameFilter(final Collection<String> pattern){
        addNewFilter(pattern, Issue::getModuleName, false);
        return this;
    }
    public IssueFilterBuilder setExcludeModuleNameFilter(final String... pattern){
        return setExcludeModuleNameFilter(Arrays.asList(pattern));
    }
    //</editor-fold>

    //<editor-fold desc="Category">
    public IssueFilterBuilder setIncludeCategoryFilter(final Collection<String> pattern){
        addNewFilter(pattern, Issue::getCategory, true);
        return this;
    }
    public IssueFilterBuilder setIncludeCategoryFilter(final String... pattern){
        return setIncludeCategoryFilter(Arrays.asList(pattern));
    }
    public IssueFilterBuilder setExcludeCategoryFilter(final Collection<String> pattern){
        addNewFilter(pattern, Issue::getCategory, false);
        return this;
    }
    public IssueFilterBuilder setExcludeCategoryFilter(final String... pattern){
        return setExcludeCategoryFilter(Arrays.asList(pattern));
    }
    //</editor-fold>

    //<editor-fold desc="Type">
    public IssueFilterBuilder setIncludeTypeFilter(final Collection<String> pattern){
        addNewFilter(pattern, Issue::getType, true);
        return this;
    }
    public IssueFilterBuilder setIncludeTypeFilter(final String... pattern){
        return setIncludeTypeFilter(Arrays.asList(pattern));
    }
    public IssueFilterBuilder setExcludeTypeFilter(final Collection<String> pattern){
        addNewFilter(pattern, Issue::getType, false);
        return this;
    }
    public IssueFilterBuilder setExcludeTypeFilter(final String... pattern){
        return setExcludeTypeFilter(Arrays.asList(pattern));
    }
    //</editor-fold>
}
