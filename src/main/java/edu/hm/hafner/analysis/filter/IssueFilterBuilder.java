package edu.hm.hafner.analysis.filter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import edu.hm.hafner.analysis.Issue;

public class IssueFilterBuilder {

    private Collection<IssuePropertyFilter> filter = new ArrayList<>();
    private String createOnePatternFromList(final Collection<String> pattern){
        return pattern.stream().collect(Collectors.joining("|")); //  | is or-operator from patter class.
    }
    public IssueFilter createIssueFilter(){
        return new IssueFilter(filter);
    }
    //<editor-fold desc="File name">
    public IssueFilterBuilder setFilenameIncludeFilter(final Collection<String> pattern){
        filter.add(new IssuePropertyFilter(createOnePatternFromList(pattern), Issue::getFileName, true));
        return this;
    }
    public IssueFilterBuilder setFilenameIncludeFilter(final String... pattern){
        return setFilenameIncludeFilter(Arrays.asList(pattern));
    }
    public IssueFilterBuilder setFilenameExcludeFilter(final Collection<String> pattern){
        filter.add(new IssuePropertyFilter(createOnePatternFromList(pattern), Issue::getFileName, false));
        return this;
    }
    public IssueFilterBuilder setFilenameExcludeFilter(final String... pattern){
        return setFilenameExcludeFilter(Arrays.asList(pattern));
    }
    //</editor-fold>

    //<editor-fold desc="Package name">
    public IssueFilterBuilder setPackageNameIncludeFilter(final Collection<String> pattern){
        filter.add(new IssuePropertyFilter(createOnePatternFromList(pattern), Issue::getPackageName, true));
        return this;
    }
    public IssueFilterBuilder setPackageNameIncludeFilter(final String... pattern){
        return setPackageNameIncludeFilter(Arrays.asList(pattern));
    }
    public IssueFilterBuilder setPackageNameExcludeFilter(final Collection<String> pattern){
        filter.add(new IssuePropertyFilter(createOnePatternFromList(pattern), Issue::getPackageName, false));
        return this;
    }
    public IssueFilterBuilder setPackageNameExcludeFilter(final String... pattern){
        return setPackageNameExcludeFilter(Arrays.asList(pattern));
    }
    //</editor-fold>

    //<editor-fold desc="Module name">
    public IssueFilterBuilder setModuleNameIncludeFilter(final Collection<String> pattern){
        filter.add(new IssuePropertyFilter(createOnePatternFromList(pattern), Issue::getModuleName, true));
        return this;
    }
    public IssueFilterBuilder setModuleNameIncludeFilter(final String... pattern){
        return setModuleNameIncludeFilter(Arrays.asList(pattern));
    }
    public IssueFilterBuilder setModuleNameExcludeFilter(final Collection<String> pattern){
        filter.add(new IssuePropertyFilter(createOnePatternFromList(pattern), Issue::getModuleName, false));
        return this;
    }
    public IssueFilterBuilder setModuleNameExcludeFilter(final String... pattern){
        return setModuleNameExcludeFilter(Arrays.asList(pattern));
    }
    //</editor-fold>

    //<editor-fold desc="Category">
    public IssueFilterBuilder setCategoryIncludeFilter(final Collection<String> pattern){
        filter.add(new IssuePropertyFilter(createOnePatternFromList(pattern), Issue::getCategory, true));
        return this;
    }
    public IssueFilterBuilder setCategoryIncludeFilter(final String... pattern){
        return setCategoryIncludeFilter(Arrays.asList(pattern));
    }
    public IssueFilterBuilder setCategoryExcludeFilter(final Collection<String> pattern){
        filter.add(new IssuePropertyFilter(createOnePatternFromList(pattern), Issue::getCategory, false));
        return this;
    }
    public IssueFilterBuilder setCategoryExcludeFilter(final String... pattern){
        return setCategoryExcludeFilter(Arrays.asList(pattern));
    }
    //</editor-fold>

    //<editor-fold desc="Type">
    public IssueFilterBuilder setTypeIncludeFilter(final Collection<String> pattern){
        filter.add(new IssuePropertyFilter(createOnePatternFromList(pattern), Issue::getType, true));
        return this;
    }
    public IssueFilterBuilder setTypeIncludeFilter(final String... pattern){
        return setTypeIncludeFilter(Arrays.asList(pattern));
    }
    public IssueFilterBuilder setTypeExcludeFilter(final Collection<String> pattern){
        filter.add(new IssuePropertyFilter(createOnePatternFromList(pattern), Issue::getType, false));
        return this;
    }
    public IssueFilterBuilder setTypeExcludeFilter(final String... pattern){
        return setTypeExcludeFilter(Arrays.asList(pattern));
    }
    //</editor-fold>
}
