package edu.hm.hafner.analysis;

import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import static com.google.common.collect.Lists.*;
import static java.util.regex.Pattern.*;
import static java.util.stream.Collectors.*;

/**
 * Filter for {@link Issues issues}.
 * <p>
 * The filtering can be performed based on a combination of the following properties of an {@link Issue issue}: <ul>
 * <li>{@link Issue#getFileName file name}</li> <li>{@link Issue#getPackageName package name}</li> <li>{@link
 * Issue#getModuleName module name}</li> <li>{@link Issue#getCategory category}</li> <li>{@link Issue#getType type}</li>
 * </ul>
 * <p>
 * A list of include and exclude regex can be defined for each property.
 * <p>
 * <strong>An {@link Issue issue} is included in the output if at least one include filter matches and no exclude filter
 * matches.</strong>
 *
 * @author Marcel Binder
 */
public class IssueFilter {
    private final List<IssuePropertyFilter> includeFilters;
    private final List<IssuePropertyFilter> excludeFilters;

    private IssueFilter(final List<IssuePropertyFilter> includeFilters, final List<IssuePropertyFilter> excludeFilters) {
        this.includeFilters = includeFilters;
        this.excludeFilters = excludeFilters;
    }

    public Issues filter(final Issues issues) {
        Collection<Issue> filteredIssues = issues.all()
                .stream()
                .filter(this::isIncluded)
                .filter(this::isExcluded)
                .collect(toList());
        return new Issues(filteredIssues);
    }

    private boolean isIncluded(Issue issue) {
        return areAllIncluded() || includeFilters
                .stream()
                .anyMatch(filter -> filter.filter(issue));
    }

    private boolean areAllIncluded() {
        return includeFilters.isEmpty();
    }

    private boolean isExcluded(Issue issue) {
        return excludeFilters
                .stream()
                .noneMatch(filter -> filter.filter(issue));
    }

    /**
     * A extractor for a {@link String string property} of an {@link Issue issue}.
     */
    @FunctionalInterface
    private interface IssueProperty {
        /**
         * Extract the property from an {@link Issue issue}.
         *
         * @param issue
         *         the {@link Issue issue} which the property should be extracted from
         *
         * @return the extracted {@link String string property}
         */
        String extract(final Issue issue);
    }

    /**
     *
     */
    @FunctionalInterface
    private interface IssuePropertyFilter {
        boolean filter(final Issue issue);
    }

    /**
     * Create a new {@link Builder builder} for an {@link IssueFilter issue filter}.
     *
     * @return the newly created {@link IssueFilter issue filter}
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * A builder for {@link IssueFilter issue filters}.
     */
    public static class Builder {
        private List<IssuePropertyFilter> includeFilters;
        private List<IssuePropertyFilter> excludeFilters;

        private Builder() {
            this.includeFilters = newArrayList();
            this.excludeFilters = newArrayList();
        }

        private IssuePropertyFilter filter(final IssueProperty propertyExtractor, final String regex) {
            Pattern pattern = compile(regex);
            return issue -> pattern.matcher(propertyExtractor.extract(issue)).matches();
        }

        private List<IssuePropertyFilter> filters(final IssueProperty propertyExtractor, final List<String> regex) {
            return regex.stream()
                    .map(r -> filter(propertyExtractor, r))
                    .collect(toList());
        }

        /**
         * Add an include filter for a certain {@link Issue issue} property.
         *
         * @param property
         *         the {@link Issue issue} property to be used for filtering
         * @param regex
         *         the regex that is applied to the specified property during filtering
         *
         * @return this {@link Builder builder}
         */
        private Builder include(final IssueProperty property, final List<String> regex) {
            includeFilters.addAll(filters(property, regex));
            return this;
        }

        /**
         * Add a exclude filter for a certain {@link Issue issue} property.
         *
         * @param property
         *         the {@link Issue issue} property to be used for filtering
         * @param regex
         *         the regex that is applied to the specified property during filtering
         *
         * @return this {@link Builder builder}
         */
        private Builder exclude(final IssueProperty property, final List<String> regex) {
            excludeFilters.addAll(filters(property, regex));
            return this;
        }

        /**
         * Include a list of file names.
         *
         * @param fileNames
         *         the file names to be included
         *
         * @return this {@link Builder builder}
         */
        public Builder includeFileNames(final List<String> fileNames) {
            return include(Issue::getFileName, fileNames);
        }

        /**
         * Exclude a list of file names.
         *
         * @param fileNames
         *         the file names to be included
         *
         * @return this {@link Builder builder}
         */
        public Builder excludeFileNames(final List<String> fileNames) {
            return exclude(Issue::getFileName, fileNames);
        }

        /**
         * Include a list of package names.
         *
         * @param packageNames
         *         the package names to be included
         *
         * @return this {@link Builder builder}
         */
        public Builder includePackageNames(final List<String> packageNames) {
            return include(Issue::getPackageName, packageNames);
        }

        /**
         * Exclude a list of package names.
         *
         * @param packageNames
         *         the package names to be included
         *
         * @return this {@link Builder builder}
         */
        public Builder excludePackageNames(final List<String> packageNames) {
            return exclude(Issue::getPackageName, packageNames);
        }

        /**
         * Include a list of module names.
         *
         * @param moduleNames
         *         the module names to be included
         *
         * @return this {@link Builder builder}
         */
        public Builder includeModuleNames(final List<String> moduleNames) {
            return include(Issue::getModuleName, moduleNames);
        }

        /**
         * Exclude a list of module names.
         *
         * @param moduleNames
         *         the module names to be included
         *
         * @return this {@link Builder builder}
         */
        public Builder excludeModuleNames(final List<String> moduleNames) {
            return exclude(Issue::getModuleName, moduleNames);
        }

        /**
         * Include a list of categories.
         *
         * @param categories
         *         the categories to be included
         *
         * @return this {@link Builder builder}
         */
        public Builder includeCategories(final List<String> categories) {
            return include(Issue::getCategory, categories);
        }

        /**
         * Exclude a list of categories.
         *
         * @param categories
         *         the categories to be included
         *
         * @return this {@link Builder builder}
         */
        public Builder excludeCategories(final List<String> categories) {
            return exclude(Issue::getCategory, categories);
        }

        /**
         * Include a list of types.
         *
         * @param types
         *         the types to be included
         *
         * @return this {@link Builder builder}
         */
        public Builder includeTypes(final List<String> types) {
            return include(Issue::getType, types);
        }

        /**
         * Exclude a list of types.
         *
         * @param types
         *         the types to be included
         *
         * @return this {@link Builder builder}
         */
        public Builder excludeTypes(final List<String> types) {
            return exclude(Issue::getType, types);
        }

        /**
         * Build a new {@link IssueFilter issue filter} with the provided parameters.
         *
         * @return the newly created {@link IssueFilter issue filter}
         */
        public IssueFilter build() {
            return new IssueFilter(includeFilters, excludeFilters);
        }
    }
}
