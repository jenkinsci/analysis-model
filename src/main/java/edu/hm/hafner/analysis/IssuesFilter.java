package edu.hm.hafner.analysis;

import edu.hm.hafner.util.Ensure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

import static com.google.common.collect.Streams.stream;
import static java.util.Collections.singletonList;

/**
 * Filters issues by the given filters that are set.
 *
 * @author Aykut Yilmaz
 */
public class IssuesFilter {

    private final Issues issues;

    private final Collection<String> includeFileNames;
    private final Collection<String> includePackageNames;
    private final Collection<String> includeModuleNames;
    private final Collection<String> includeCategories;
    private final Collection<String> includeTypes;

    private final Collection<String> excludeFileNames;
    private final Collection<String> excludePackageNames;
    private final Collection<String> excludeModuleNames;
    private final Collection<String> excludeCategories;
    private final Collection<String> excludeTypes;


    /**
     * Constructor for IssuesFilter.
     *
     * @param issues that shall be filtered.
     */
    public IssuesFilter(final Issues issues) {
        this.issues = issues;

        includeFileNames = new HashSet<>();
        includePackageNames = new HashSet<>();
        includeModuleNames = new HashSet<>();
        includeCategories = new HashSet<>();
        includeTypes = new HashSet<>();

        excludeFileNames = new HashSet<>();
        excludePackageNames = new HashSet<>();
        excludeModuleNames = new HashSet<>();
        excludeCategories = new HashSet<>();
        excludeTypes = new HashSet<>();
    }

    /**
     * Filters the issues according to the given filters.
     *
     * @return if the issues were empty or no filters were given, a copy of the issues will be returned, otherwise a
     * filtered issues will be returned.
     */
    public Issues filter() {
        if (issues.isEmpty() || !hasFilters()) {
            return issues.copy();
        }

        ensureDisjointRegexps();

        Collection<Issues> includeWarnings = hasIncludeFilters() ? includeWarnings() : singletonList(issues);
        Issues includeWarningsMerged = mergeIssuesDistinct(includeWarnings);
        if (!hasExcludeFilters()) {
            return includeWarningsMerged;
        }

        mergeIssuesDistinct(excludeWarnings()).forEach(issue -> includeWarningsMerged.remove(issue.getId()));

        return includeWarningsMerged;
    }

    private void ensureDisjointRegexps() {
        String filterConflict = "cannot include and exclude same";
        Ensure.that(Collections.disjoint(includeFileNames, excludeFileNames))
                .isTrue(filterConflict + " " + "fileName");

        Ensure.that(Collections.disjoint(includePackageNames, excludePackageNames))
                .isTrue(filterConflict + " " + "packageName");

        Ensure.that(Collections.disjoint(includeModuleNames, excludeModuleNames))
                .isTrue(filterConflict + " " + "moduleName");

        Ensure.that(Collections.disjoint(includeCategories, excludeCategories))
                .isTrue(filterConflict + " " + "category");

        Ensure.that(Collections.disjoint(includeTypes, excludeTypes))
                .isTrue(filterConflict + " " + "type");
    }

    private Issues mergeIssuesDistinct(final Collection<Issues> warnings) {
        return new Issues(stream(Issues.merge(warnings)).distinct().collect(Collectors.toList()));
    }

    private Collection<Issues> filterWarnings(final Collection<String> fileNamesCollection,
                                              final Collection<String> packageNamesCollection,
                                              final Collection<String> moduleNamesCollection,
                                              final Collection<String> categoriesCollection,
                                              final Collection<String> typesCollection) {
        Collection<Issues> warnings = new ArrayList<>();

        String fileNames = mergeRegex(fileNamesCollection);
        String packageNames = mergeRegex(packageNamesCollection);
        String moduleNames = mergeRegex(moduleNamesCollection);
        String categories = mergeRegex(categoriesCollection);
        String types = mergeRegex(typesCollection);

        if (!fileNames.isEmpty()) {
            warnings.add(issues.filter(issue -> issue.getFileName().matches(fileNames)));
        }
        if (!packageNames.isEmpty()) {
            warnings.add(issues.filter(issue -> issue.getPackageName().matches(packageNames)));
        }
        if (!moduleNames.isEmpty()) {
            warnings.add(issues.filter(issue -> issue.getModuleName().matches(moduleNames)));
        }
        if (!categories.isEmpty()) {
            warnings.add(issues.filter(issue -> issue.getCategory().matches(categories)));
        }
        if (!types.isEmpty()) {
            warnings.add(issues.filter(issue -> issue.getType().matches(types)));
        }
        return warnings;
    }

    private Collection<Issues> includeWarnings() {
        return filterWarnings(
                includeFileNames,
                includePackageNames,
                includeModuleNames,
                includeCategories,
                includeTypes);
    }

    private Collection<Issues> excludeWarnings() {
        return filterWarnings(
                excludeFileNames,
                excludePackageNames,
                excludeModuleNames,
                excludeCategories,
                excludeTypes);
    }

    private String mergeRegex(final Collection<String> regexps) {
        if (regexps.isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();

        regexps.forEach(regex -> builder.append('(').append(regex).append(')').append('|'));
        builder.deleteCharAt(builder.length() - 1);

        return builder.toString();
    }

    private boolean hasFilters() {
        return hasIncludeFilters() || hasExcludeFilters();
    }

    private boolean hasIncludeFilters() {
        if (hasIncludeFileNames() || hasIncludePackageNames()) {
            return true;
        }

        if (hasIncludeModuleNames() || hasIncludeCategories()) {
            return true;
        }

        return hasIncludeTypes();
    }

    private boolean hasIncludeTypes() {
        return !includeTypes.isEmpty();
    }

    private boolean hasIncludeCategories() {
        return !includeCategories.isEmpty();
    }

    private boolean hasIncludeModuleNames() {
        return !includeModuleNames.isEmpty();
    }

    private boolean hasIncludePackageNames() {
        return !includePackageNames.isEmpty();
    }

    private boolean hasIncludeFileNames() {
        return !includeFileNames.isEmpty();
    }

    private boolean hasExcludeFilters() {
        if (hasExcludeFileNames() || hasExcludePackageNames()) {
            return true;
        }

        if (hasExcludeModuleNames() || hasExcludeCategories()) {
            return true;
        }

        return hasExcludeTypes();
    }

    private boolean hasExcludeTypes() {
        return !excludeTypes.isEmpty();
    }

    private boolean hasExcludeCategories() {
        return !excludeCategories.isEmpty();
    }

    private boolean hasExcludeModuleNames() {
        return !excludeModuleNames.isEmpty();
    }

    private boolean hasExcludePackageNames() {
        return !excludePackageNames.isEmpty();
    }

    private boolean hasExcludeFileNames() {
        return !excludeFileNames.isEmpty();
    }

    /**
     * Specify files by the filename that will be included additionally. The regex will be added to a collection.
     *
     * @param regex specifies the filenames.
     */
    public void includeFileNames(final String regex) {
        includeFileNames.add(regex);
    }

    /**
     * Specify files by the package name that will be included additionally. The regex will be added to a collection.
     *
     * @param regex specifies the package names.
     */
    public void includePackageNames(final String regex) {
        includePackageNames.add(regex);
    }

    /**
     * Specify files by the module name that will be included additionally. The regex will be added to a collection.
     *
     * @param regex specifies the module names.
     */
    public void includeModuleNames(final String regex) {
        includeModuleNames.add(regex);
    }

    /**
     * Specify files by the category that will be included additionally. The regex will be added to a collection.
     *
     * @param regex specifies the categories.
     */
    public void includeCategories(final String regex) {
        includeCategories.add(regex);
    }

    /**
     * Specify files by the type that will be included additionally. The regex will be added to a collection.
     *
     * @param regex specifies the types.
     */
    public void includeType(final String regex) {
        includeTypes.add(regex);
    }

    /**
     * Specify files by the filename that will be excluded. The regex will be added to a collection.
     *
     * @param regex specifies the filenames.
     */
    public void excludeFileNames(final String regex) {
        excludeFileNames.add(regex);
    }

    /**
     * Specify files by the package name that will be excluded. The regex will be added to a collection.
     *
     * @param regex specifies the package names.
     */
    public void excludePackageNames(final String regex) {
        excludePackageNames.add(regex);
    }

    /**
     * Specify files by the module name that will be excluded. The regex will be added to a collection.
     *
     * @param regex specifies the module names.
     */
    public void excludeModuleNames(final String regex) {
        excludeModuleNames.add(regex);
    }

    /**
     * Specify files by the category that will be excluded. The regex will be added to a collection.
     *
     * @param regex specifies the categories.
     */
    public void excludeCategories(final String regex) {
        excludeCategories.add(regex);
    }

    /**
     * Specify files by the type that will be excluded. The regex will be added to a collection.
     *
     * @param regex specifies the types.
     */
    public void excludeTypes(final String regex) {
        excludeTypes.add(regex);
    }
}
