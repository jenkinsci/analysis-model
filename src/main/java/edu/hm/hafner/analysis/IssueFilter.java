package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.*;

/**
 * Filter for {@link Issue}
 * <p>
 * This filter can filter issues by their file-name, package-name, module-name, category and type. You can specify a
 * list of include and exclude patterns which are used to filter the issues.
 *
 * @author Sebastian Lausch
 */
public class IssueFilter {
    /** List of patterns/filters which determine if an issue should be included. */
    private List<Filter> includePattern;
    /** List of patterns/filters which determine if an issue should be excluded. */
    private List<Filter> excludePattern;

    public IssueFilter() {
        includePattern = new ArrayList<>();
        excludePattern = new ArrayList<>();
    }

    private boolean hasIncludeFilter() {
        return !includePattern.isEmpty();
    }

    private boolean hasExcludeFilter() {
        return !excludePattern.isEmpty();
    }

    /**
     * Adds another inlude-filter to the IssueFilter.
     *
     * @param category
     *         - Attribute of issue that shall be checked.
     * @param regexToInclude
     *         - Regular Expression defining the filter behaviour.
     */
    public IssueFilter include(FilterCategory category, List<String> regexToInclude) {
        for (String regex : regexToInclude) {
            Pattern pattern = Pattern.compile(regex);
            includePattern.add(new Filter(category, pattern));
        }
        return this;
    }

    /**
     * Adds another exclude-filter to the IssueFilter.
     *
     * @param category
     *         - Attribute of issue that shall be checked.
     * @param regexToExclude
     *         - Regular Expression defining the filter behaviour.
     */
    public IssueFilter exclude(FilterCategory category, List<String> regexToExclude) {
        for (String regex : regexToExclude) {
            Pattern pattern = Pattern.compile(regex);
            excludePattern.add(new Filter(category, pattern));
        }
        return this;
    }

    /**
     * Filters a collection of Issues. The method therefore applies the given include- and exclude-filters.
     *
     * @param issuesToFilter
     *         - Collection of Issues to filter.
     *
     * @return Collection of filtered issues.
     */
    public Issues filter(Issues issuesToFilter) {
        Issues filteredIssues = new Issues();

        if (hasIncludeFilter()) {
            filteredIssues.addAll(issuesToFilter.all().stream()
                    .filter(issue -> isIncluded(issue))
                    .collect(toList()));
        }
        else {
            filteredIssues = issuesToFilter;
        }

        if (hasExcludeFilter()) {
            filteredIssues = new Issues(filteredIssues.all().stream()
                    .filter(issue -> isExcluded(issue))
                    .collect(toList()));
        }

        return filteredIssues;
    }

    /**
     * Checks if the issue passes minimum of one include-filter.
     *
     * @param issue
     *         - Issue to check.
     *
     * @return true -> issue passed a minimum of one include-filter and should be included. false -> issue passed no
     *         include-filter and should therefore not be included.
     */
    private boolean isIncluded(Issue issue) {
        for (Filter filter : includePattern) {
            if (filter.filter(issue)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the issue alarms a minimum of one exclude-filter.
     *
     * @param issue
     *         - Issue to check.
     *
     * @return true -> issue alarmed a minimum of one exclude-filter and should be excluded. false -> issue didn't alarm
     *         any exclude-filter.
     */
    private boolean isExcluded(Issue issue) {
        for (Filter filter : excludePattern) {
            if (filter.filter(issue)) {
                return false;
            }
        }
        return true;
    }

    public enum FilterCategory {
        FILE_NAME, PACKAGE_NAME, MODULE_NAME, CATEGORY, TYPE
    }

    /**
     * This represents a single filter for a given filter-category.
     */
    private class Filter {
        /** Pattern which checks the issue. */
        private final Pattern pattern;
        /** Attribute of issue that shall be checked. */
        private final FilterCategory category;

        Filter(FilterCategory category, Pattern pattern) {
            this.category = category;
            this.pattern = pattern;
        }

        /**
         * Checks whether a filter matches an issue.
         *
         * @param issue
         *         - Issue to check
         *
         * @return true -> Filter did match the issue false -> Filter didn't match the issue
         */
        public boolean filter(Issue issue) {
            if (category == FilterCategory.FILE_NAME) {
                return pattern.matcher(issue.getFileName()).find();
            }
            else if (category == FilterCategory.CATEGORY) {
                return pattern.matcher(issue.getCategory()).find();
            }
            else if (category == FilterCategory.MODULE_NAME) {
                return pattern.matcher(issue.getModuleName()).find();
            }
            else if (category == FilterCategory.PACKAGE_NAME) {
                return pattern.matcher(issue.getPackageName()).find();
            }
            else {
                return pattern.matcher(issue.getType()).find();
            }
        }
    }
}
