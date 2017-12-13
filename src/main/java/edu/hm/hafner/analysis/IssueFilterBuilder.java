package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import edu.hm.hafner.analysis.Issue;

/**
 * Builds a IssueFilter 
 * @author Raphael Furch
 */
public class IssueFilterBuilder {

    /**
     * List of include filters.
     */
    private Collection<Predicate<Issue>> filterInclude = new ArrayList<>();

    /**
     * List of exclude filters.
     */
    private Collection<Predicate<Issue>> filterExclude = new ArrayList<>();

    /**
     * Include combine behavior.
     * true = or.
     * false = and.
     */
    private boolean includeCombineWithOr = true;


    /**
     * Add a new filter for each pattern string.
     * Add filter to include or exclude list.
     * @param pattern = filter pattern.
     * @param propertyToFilter = Function to get a string from Issue for pattern
     * @param include = include or exclude filter.
     */
    private void addNewFilter(final Collection<String> pattern, Function<Issue, String> propertyToFilter, boolean include ){

        Collection<Predicate<Issue>> filters = new ArrayList<>();
        for (String patter : pattern) {
            filters.add((issueToFilter) ->  Pattern.compile(patter)
                    .matcher(propertyToFilter.apply(issueToFilter)).matches() == include);
        }

        if(include){
            this.filterInclude.addAll(filters);
        }
        else{
            this.filterExclude.addAll(filters);
        }
    }

    /**
     * Create a IssueFilter.
     * Combine by default all includes with or and all excludes with and.
     * @return a IssueFilter which has all added filter as filter criteria.
     */
    public IssueFilter createIssueFilter(){
        return new IssueFilter(filterInclude.stream().reduce(includeCombineWithOr?Predicate::or:Predicate::and)
                .orElse((issue)-> true).and(
        filterExclude.stream().reduce(Predicate::and).orElse((issue)-> true)));
    }

    /**
     * Sets include combine behavior.
     * @param withOr = or or and.
     * @return this.
     */
    public IssueFilterBuilder setIncludeCombineFilterWithOr(boolean withOr){
        this.includeCombineWithOr = withOr;
        return this;
    }


    //<editor-fold desc="File name">

    /**
     * Add a new filter.
     * @param pattern = pattern
     * @return this.
     */
    public IssueFilterBuilder setIncludeFilenameFilter(final Collection<String> pattern){
        addNewFilter(pattern, Issue::getFileName, true);
        return this;
    }
    /**
     * Add a new filter.
     * @param pattern = pattern
     * @return this.
     */
    public IssueFilterBuilder setIncludeFilenameFilter(final String... pattern){
        return setIncludeFilenameFilter(Arrays.asList(pattern));
    }
    /**
     * Add a new filter.
     * @param pattern = pattern
     * @return this.
     */
    public IssueFilterBuilder setExcludeFilenameFilter(final Collection<String> pattern){
        addNewFilter(pattern, Issue::getFileName, false);
        return this;
    }
    /**
     * Add a new filter.
     * @param pattern = pattern
     * @return this.
     */
    public IssueFilterBuilder setExcludeFilenameFilter(final String... pattern){
        return setExcludeFilenameFilter(Arrays.asList(pattern));
    }
    //</editor-fold>

    //<editor-fold desc="Package name">
    /**
     * Add a new filter.
     * @param pattern = pattern
     * @return this.
     */
    public IssueFilterBuilder setIncludePackageNameFilter(final Collection<String> pattern){
        addNewFilter(pattern, Issue::getPackageName, true);
        return this;
    }
    /**
     * Add a new filter.
     * @param pattern = pattern
     * @return this.
     */
    public IssueFilterBuilder setIncludePackageNameFilter(final String... pattern){
        return setIncludePackageNameFilter(Arrays.asList(pattern));
    }
    /**
     * Add a new filter.
     * @param pattern = pattern
     * @return this.
     */
    public IssueFilterBuilder setExcludePackageNameFilter(final Collection<String> pattern){
        addNewFilter(pattern, Issue::getPackageName, false);
        return this;
    }
    /**
     * Add a new filter.
     * @param pattern = pattern
     * @return this.
     */
    public IssueFilterBuilder setExcludePackageNameFilter(final String... pattern){
        return setExcludePackageNameFilter(Arrays.asList(pattern));
    }
    //</editor-fold>

    //<editor-fold desc="Module name">
    /**
     * Add a new filter.
     * @param pattern = pattern
     * @return this.
     */
    public IssueFilterBuilder setIncludeModuleNameFilter(final Collection<String> pattern){
        addNewFilter(pattern, Issue::getModuleName, true);
        return this;
    }
    /**
     * Add a new filter.
     * @param pattern = pattern
     * @return this.
     */
    public IssueFilterBuilder setIncludeModuleNameFilter(final String... pattern){
        return setIncludeModuleNameFilter(Arrays.asList(pattern));
    }
    /**
     * Add a new filter.
     * @param pattern = pattern
     * @return this.
     */
    public IssueFilterBuilder setExcludeModuleNameFilter(final Collection<String> pattern){
        addNewFilter(pattern, Issue::getModuleName, false);
        return this;
    }
    /**
     * Add a new filter.
     * @param pattern = pattern
     * @return this.
     */
    public IssueFilterBuilder setExcludeModuleNameFilter(final String... pattern){
        return setExcludeModuleNameFilter(Arrays.asList(pattern));
    }
    //</editor-fold>

    //<editor-fold desc="Category">
    /**
     * Add a new filter.
     * @param pattern = pattern
     * @return this.
     */
    public IssueFilterBuilder setIncludeCategoryFilter(final Collection<String> pattern){
        addNewFilter(pattern, Issue::getCategory, true);
        return this;
    }
    /**
     * Add a new filter.
     * @param pattern = pattern
     * @return this.
     */
    public IssueFilterBuilder setIncludeCategoryFilter(final String... pattern){
        return setIncludeCategoryFilter(Arrays.asList(pattern));
    }
    /**
     * Add a new filter.
     * @param pattern = pattern
     * @return this.
     */
    public IssueFilterBuilder setExcludeCategoryFilter(final Collection<String> pattern){
        addNewFilter(pattern, Issue::getCategory, false);
        return this;
    }
    /**
     * Add a new filter.
     * @param pattern = pattern
     * @return this.
     */
    public IssueFilterBuilder setExcludeCategoryFilter(final String... pattern){
        return setExcludeCategoryFilter(Arrays.asList(pattern));
    }
    //</editor-fold>

    //<editor-fold desc="Type">
    /**
     * Add a new filter.
     * @param pattern = pattern
     * @return this.
     */
    public IssueFilterBuilder setIncludeTypeFilter(final Collection<String> pattern){
        addNewFilter(pattern, Issue::getType, true);
        return this;
    }
    /**
     * Add a new filter.
     * @param pattern = pattern
     * @return this.
     */
    public IssueFilterBuilder setIncludeTypeFilter(final String... pattern){
        return setIncludeTypeFilter(Arrays.asList(pattern));
    }
    /**
     * Add a new filter.
     * @param pattern = pattern
     * @return this.
     */
    public IssueFilterBuilder setExcludeTypeFilter(final Collection<String> pattern){
        addNewFilter(pattern, Issue::getType, false);
        return this;
    }
    /**
     * Add a new filter.
     * @param pattern = pattern
     * @return this.
     */
    public IssueFilterBuilder setExcludeTypeFilter(final String... pattern){
        return setExcludeTypeFilter(Arrays.asList(pattern));
    }
    //</editor-fold>
}
