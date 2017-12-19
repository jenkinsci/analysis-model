package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

/**
 * Used to apply a user-defined set of include and exclude filters to an {@link Issues} instance in
 * a fluent-style manner. Filters are added as lists of regex and are applied to given Issues.
 *
 * Example:
 *  <pre>
 *      {@code
 *      Issues issues = new Issues();
 *      IssueFilter issueFilter = new IssueFilter()
 *                                      .addIncludeFiles(listOfRegex1)
 *                                      .addIncludeCategories(listOfRegex2)
 *                                      .addIncludeModules(listOfRegex3)
 *                                      .addExcludeFiles(listOfRegex4)
 *                                      .addExcludeTypes(listOfRegex5);
 *      Issues result = issueFilter.applyFilters(issues);
 *      }
 *      
 *      The result will contain all issues with a fileName that matches any regex in listOfRegex1,
 *                              all issues with a category that matches any regex in listOfRegex2,
 *                              all issues with a moduleName that matches any regex in listOfRegex3,
 *                              no issues with a fileName that matches any regex in listOfRegex4,
 *                              no issues with a type that matches any regex in listOfRegex5
 *                              
 *      Excluded issues will be filtered out even when they were included previously.
 *  </pre>
 *
 *  @author Joscha Behrmann
 */
public class IssueFilter {
    // The include-filters
    private final List<Pattern> includeFiles = new ArrayList<>();
    private final List<Pattern> includePackages = new ArrayList<>();
    private final List<Pattern> includeModules = new ArrayList<>();
    private final List<Pattern> includeCategories = new ArrayList<>();
    private final List<Pattern> includeTypes = new ArrayList<>();

    // The exclude-filters
    private final List<Pattern> excludeFiles = new ArrayList<>();
    private final List<Pattern> excludePackages = new ArrayList<>();
    private final List<Pattern> excludeModules = new ArrayList<>();
    private final List<Pattern> excludeCategories = new ArrayList<>();
    private final List<Pattern> excludeTypes = new ArrayList<>();

    /**
     * Applies the current set of filters to an instance of {@link Issues}.
     * 
     * @param issuesToFilter the Issues to filter.
     * @return a new instance of Issues that includes all issues from issuesToFilter that match an include-filter
     *         but no issues from issuesToFilter that match an exclude-filter
     */
    public Issues applyFilters(final Issues issuesToFilter) {
        if (issuesToFilter.isEmpty() || !hasFilters()) {
            return issuesToFilter.copy();
        }

        Set<Issue> includes = new HashSet<>();
        issuesToFilter.all().stream()
                .filter(this::isIncluded).forEach(includes::add);

        return new Issues(includes);
    }

    /*
     * Tests if the issue should be included in the final result or not.
     */
    private boolean isIncluded(final Issue issue) {
        return !isExcluded(issue)
                && (!hasIncludeFilters()
                || isIncludedByFile(issue)
                || isIncludedByPackage(issue)
                || isIncludedByModule(issue)
                || isIncludedByCategory(issue)
                || isIncludedByType(issue));
    }

    /*
     * Tests if the issue is included by any fileName-Filter.
     */
    private boolean isIncludedByFile(final Issue issue) {
        return includeFiles.stream().anyMatch(p -> p.matcher(issue.getFileName()).matches());
    }

    /*
     * Tests if the issue is included by any packageName-Filter.
     */
    private boolean isIncludedByPackage(final Issue issue) {
        return includePackages.stream().anyMatch(p -> p.matcher(issue.getPackageName()).matches());
    }

    /*
     * Tests if the issue is included by any moduleName-Filter.
     */
    private boolean isIncludedByModule(final Issue issue) {
        return includeModules.stream().anyMatch(p -> p.matcher(issue.getModuleName()).matches());
    }

    /*
     * Tests if the issue is included by any categoryName-Filter.
     */
    private boolean isIncludedByCategory(final Issue issue) {
        return includeCategories.stream().anyMatch(p -> p.matcher(issue.getCategory()).matches());
    }

    /*
     * Tests if the issue is included by any typeName-Filter.
     */
    private boolean isIncludedByType(final Issue issue) {
        return includeTypes.stream().anyMatch(p -> p.matcher(issue.getType()).matches());
    }

    /*
     * Tests if the issue is excluded by any exclude-filter.
     */
    private boolean isExcluded(final Issue issue) {
        return excludeFiles.stream().anyMatch(p -> p.matcher(issue.getFileName()).matches())
                || excludePackages.stream().anyMatch(p -> p.matcher(issue.getPackageName()).matches())
                || excludeModules.stream().anyMatch(p -> p.matcher(issue.getModuleName()).matches())
                || excludeCategories.stream().anyMatch(p -> p.matcher(issue.getCategory()).matches())
                || excludeTypes.stream().anyMatch(p -> p.matcher(issue.getType()).matches());
    }

    /**
     * Adds filters that include issues by fileName.
     * 
     * @param regex the regex to match
     * @return this
     */
    public IssueFilter addIncludedFiles(final List<String> regex) {
        addRegexToList(regex, includeFiles);
        return this;
    }

    /**
     * Adds filters that include issues by packageName.
     *
     * @param regex the regex to match
     * @return this
     */
    public IssueFilter addIncludedPackages(final List<String> regex) {
        addRegexToList(regex, includePackages);
        return this;
    }

    /**
     * Adds filters that include issues by moduleName.
     *
     * @param regex the regex to match
     * @return this
     */
    public IssueFilter addIncludedModules(final List<String> regex) {
        addRegexToList(regex, includeModules);
        return this;
    }

    /**
     * Adds filters that include issues by category.
     *
     * @param regex the regex to match
     * @return this
     */
    public IssueFilter addIncludedCategories(final List<String> regex) {
        addRegexToList(regex, includeCategories);
        return this;
    }

    /**
     * Adds filters that include issues by type.
     *
     * @param regex the regex to match
     * @return this
     */
    public IssueFilter addIncludedTypes(final List<String> regex) {
        addRegexToList(regex, includeTypes);
        return this;
    }

    /**
     * Adds filters that exclude issues by fileName.
     *
     * @param regex the regex to match
     * @return this
     */
    public IssueFilter addExcludedFiles(final List<String> regex) {
        addRegexToList(regex, excludeFiles);
        return this;
    }

    /**
     * Adds filters that exclude issues by packageName.
     *
     * @param regex the regex to match
     * @return this
     */
    public IssueFilter addExcludedPackages(final List<String> regex) {
        addRegexToList(regex, excludePackages);
        return this;
    }

    /**
     * Adds filters that exclude issues by moduleName.
     *
     * @param regex the regex to match
     * @return this
     */
    public IssueFilter addExcludedModules(final List<String> regex) {
        addRegexToList(regex, excludeModules);
        return this;
    }

    /**
     * Adds filters that exclude issues by category.
     *
     * @param regex the regex to match
     * @return this
     */
    public IssueFilter addExcludedCategories(final List<String> regex) {
        addRegexToList(regex, excludeCategories);
        return this;
    }

    /**
     * Adds filters that exclude issues by type.
     *
     * @param regex the regex to match
     * @return this
     */
    public IssueFilter addExcludedTypes(final List<String> regex) {
        addRegexToList(regex, excludeTypes);
        return this;
    }

    /*
     * Adds all valid regex contained in regex into destination.
     */
    private void addRegexToList(final List<String> regex, final List<Pattern> destination) {
        regex.stream()
                .map(this::getPatternFromString)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(() -> destination));
    }

    /*
     * Returns a Pattern from a regex-string or returns null if given regex is invalid.
     */
    private Pattern getPatternFromString(final String pattern) {
        try {
            return Pattern.compile(pattern);
        }
        catch (PatternSyntaxException ex) {
            return null;
        }
    }

    /*
     * Checks if any filters are set.
     */
    private boolean hasFilters() {
        return hasIncludeFilters() || hasExcludeFilters();
    }

    /*
     * Checks if any include-filters are set.
     */
    private boolean hasIncludeFilters() {
        return includeFiles.size() > 0
                || includePackages.size() > 0
                || includeModules.size() > 0
                || includeCategories.size() > 0
                || includeTypes.size() > 0;
    }

    /*
     * Checks if any exclude-filters are set.
     */
    private boolean hasExcludeFilters() {
        return excludeFiles.size() > 0
                || excludePackages.size() > 0
                || excludeModules.size() > 0
                || excludeCategories.size() > 0
                || excludeTypes.size() > 0;
    }
}
