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
 */
public class IssuesFilter {
    private final Issues issues;
    private final Collection<String> includeFileNames;
    private final Collection<String> includePackageNames;
    private final Collection<String> includeModuleNames;
    private final Collection<String> includeCategories;
    private final Collection<String> includeTypes;

    private final Collection<String> excludeFilenames;
    private final Collection<String> excludePackageNames;
    private final Collection<String> excludeModuleNames;
    private final Collection<String> excludeCategories;
    private final Collection<String> excludeTypes;


    /**
     * Constructor for IssuesFilter.
     *
     * @param issues that shall be filtered.
     */
    public IssuesFilter(Issues issues) {
        this.issues = issues;

        this.includeFileNames = new HashSet<>();
        this.includePackageNames = new HashSet<>();
        this.includeModuleNames = new HashSet<>();
        this.includeCategories = new HashSet<>();
        this.includeTypes = new HashSet<>();

        this.excludeFilenames = new HashSet<>();
        this.excludePackageNames = new HashSet<>();
        this.excludeModuleNames = new HashSet<>();
        this.excludeCategories = new HashSet<>();
        this.excludeTypes = new HashSet<>();
    }

    /**
     * Filters the issues according to the given filters.
     *
     * @return if the issues were empty or no filters were given, a copy of the issues will be returned,
     * otherwise a filtered issues will be returned.
     */
    public Issues filter() {
        if (this.issues.isEmpty() || !hasFilters()) return issues.copy();

        ensureDisjointRegexps();

        final Collection<Issues> includeWarnings = hasIncludeFilters() ? includeWarnings() : singletonList(this.issues);
        final Issues includeWarningsMerged = mergeIssuesDistinct(includeWarnings);
        if (!hasExcludeFilters()) return includeWarningsMerged;

        mergeIssuesDistinct(excludeWarnings()).forEach(issue -> includeWarningsMerged.remove(issue.getId()));

        return includeWarningsMerged;
    }

    private void ensureDisjointRegexps() {
        final String filterConflict = "cannot include and exclude same ";
        Ensure.that(Collections.disjoint(includeFileNames, excludeFilenames))
                .isTrue(filterConflict + "fileName");

        Ensure.that(Collections.disjoint(includePackageNames, excludePackageNames))
                .isTrue(filterConflict + "packageName");

        Ensure.that(Collections.disjoint(includeModuleNames, excludeModuleNames))
                .isTrue(filterConflict + "moduleName");

        Ensure.that(Collections.disjoint(includeCategories, excludeCategories))
                .isTrue(filterConflict + "category");

        Ensure.that(Collections.disjoint(includeTypes, excludeTypes))
                .isTrue(filterConflict + "type");
    }

    private Issues mergeIssuesDistinct(Collection<Issues> warnings) {
        return new Issues(stream(Issues.merge(warnings)).distinct().collect(Collectors.toList()));
    }

    private Collection<Issues> filterWarnings(Collection<String> fileNamesCollection,
                                              Collection<String> packageNamesCollection,
                                              Collection<String> moduleNamesCollection,
                                              Collection<String> categoriesCollection,
                                              Collection<String> typesCollection) {
        Collection<Issues> warnings = new ArrayList<>();

        final String fileNames = mergeRegex(fileNamesCollection);
        final String packageNames = mergeRegex(packageNamesCollection);
        final String moduleNames = mergeRegex(moduleNamesCollection);
        final String categories = mergeRegex(categoriesCollection);
        final String types = mergeRegex(typesCollection);

        if (!fileNames.isEmpty())
            warnings.add(this.issues.filter(issue -> issue.getFileName().matches(fileNames)));
        if (!packageNames.isEmpty())
            warnings.add(this.issues.filter(issue -> issue.getPackageName().matches(packageNames)));
        if (!moduleNames.isEmpty())
            warnings.add(this.issues.filter(issue -> issue.getModuleName().matches(moduleNames)));
        if (!categories.isEmpty())
            warnings.add(this.issues.filter(issue -> issue.getCategory().matches(categories)));
        if (!types.isEmpty())
            warnings.add(this.issues.filter(issue -> issue.getType().matches(types)));
        return warnings;
    }

    private Collection<Issues> includeWarnings() {
        return filterWarnings(
                this.includeFileNames,
                this.includePackageNames,
                this.includeModuleNames,
                this.includeCategories,
                this.includeTypes);
    }

    private Collection<Issues> excludeWarnings() {
        return filterWarnings(
                this.excludeFilenames,
                this.excludePackageNames,
                this.excludeModuleNames,
                this.excludeCategories,
                this.excludeTypes);
    }

    private String mergeRegex(Collection<String> regexps) {
        if (regexps.isEmpty()) return "";

        StringBuilder builder = new StringBuilder();

        regexps.forEach(regex -> builder.append(regex).append('|'));
        builder.deleteCharAt(builder.length() - 1);

        return builder.toString();
    }

    private boolean hasFilters() {
        return hasIncludeFilters() || hasExcludeFilters();
    }

    private boolean hasIncludeFilters() {
        return !(includeFileNames.isEmpty()
                && includePackageNames.isEmpty()
                && includeModuleNames.isEmpty()
                && includeCategories.isEmpty()
                && includeTypes.isEmpty());
    }

    private boolean hasExcludeFilters() {
        return !(excludeFilenames.isEmpty()
                && excludePackageNames.isEmpty()
                && excludeModuleNames.isEmpty()
                && excludeCategories.isEmpty()
                && excludeTypes.isEmpty());
    }

    /**
     * Specify files by the filename that will be included additionally.
     * The regex will be added to a collection.
     *
     * @param regex specifies the filenames.
     */
    public void includeFileNames(String regex) {
        this.includeFileNames.add(regex);
    }

    /**
     * Specify files by the package name that will be included additionally.
     * The regex will be added to a collection.
     *
     * @param regex specifies the package names.
     */
    public void includePackageNames(String regex) {
        this.includePackageNames.add(regex);
    }

    /**
     * Specify files by the module name that will be included additionally.
     * The regex will be added to a collection.
     *
     * @param regex specifies the module names.
     */
    public void includeModuleNames(String regex) {
        this.includeModuleNames.add(regex);
    }

    /**
     * Specify files by the category that will be included additionally.
     * The regex will be added to a collection.
     *
     * @param regex specifies the categories.
     */
    public void includeCategories(String regex) {
        this.includeCategories.add(regex);
    }

    /**
     * Specify files by the type that will be included additionally.
     * The regex will be added to a collection.
     *
     * @param regex specifies the types.
     */
    public void includeType(String regex) {
        this.includeTypes.add(regex);
    }

    /**
     * Specify files by the filename that will be excluded.
     * The regex will be added to a collection.
     *
     * @param regex specifies the filenames.
     */
    public void excludeFileNames(String regex) {
        this.excludeFilenames.add(regex);
    }

    /**
     * Specify files by the package name that will be excluded.
     * The regex will be added to a collection.
     *
     * @param regex specifies the package names.
     */
    public void excludePackageNames(String regex) {
        this.excludePackageNames.add(regex);
    }

    /**
     * Specify files by the module name that will be excluded.
     * The regex will be added to a collection.
     *
     * @param regex specifies the module names.
     */
    public void excludeModuleNames(String regex) {
        this.excludeModuleNames.add(regex);
    }

    /**
     * Specify files by the category that will be excluded.
     * The regex will be added to a collection.
     *
     * @param regex specifies the categories.
     */
    public void excludeCategories(String regex) {
        this.excludeCategories.add(regex);
    }

    /**
     * Specify files by the type that will be excluded.
     * The regex will be added to a collection.
     *
     * @param regex specifies the types.
     */
    public void excludeTypes(String regex) {
        this.excludeTypes.add(regex);
    }
}
