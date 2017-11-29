package edu.hm.hafner.analysis;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

/**
 * Base class for filter builders. Supplies a consistent interface for adding filters.
 */
public abstract class IssueFilterBuilder {


    private final List<IssueFilter> filters = new LinkedList<>();

    private final Function<Collection<IssueFilter>, IssueFilter> aggregator;

    /**
     * Base ctor provided with a func aggregating the filters.
     *
     * @param aggregator
     *         aggregates filters.
     */
    public IssueFilterBuilder(final Function<Collection<IssueFilter>, IssueFilter> aggregator) {
        this.aggregator = aggregator;
    }

    /**
     * Adds filters for filter names.
     *
     * @param patterns
     *         to filter filenames for.
     *
     * @return this
     */
    public IssueFilterBuilder addFileFilters(final Collection<String> patterns) {
        patterns.forEach(pattern -> addRegexFilter(Issue::getFileName, pattern));

        return this;
    }

    /**
     * Adds filters for package names.
     *
     * @param patterns
     *         to filter packages names for.
     *
     * @return this
     */
    public IssueFilterBuilder addPackageFilters(final Collection<String> patterns) {
        patterns.forEach(pattern -> addRegexFilter(Issue::getPackageName, pattern));

        return this;
    }

    /**
     * Adds filters for module names.
     *
     * @param patterns
     *         to filter module names for.
     *
     * @return this
     */
    public IssueFilterBuilder addModuleFilters(final Collection<String> patterns) {
        patterns.forEach(pattern -> addRegexFilter(Issue::getModuleName, pattern));

        return this;
    }

    /**
     * Adds filters for categories.
     *
     * @param patterns
     *         to filter categories for.
     *
     * @return this
     */
    public IssueFilterBuilder addCategoryFilters(final Collection<String> patterns) {
        patterns.forEach(pattern -> addRegexFilter(Issue::getCategory, pattern));

        return this;
    }

    /**
     * Adds filters to types.
     *
     * @param patterns
     *         to filter types for.
     *
     * @return this
     */
    public IssueFilterBuilder addTypeFilters(final Collection<String> patterns) {
        patterns.forEach(pattern -> addRegexFilter(Issue::getType, pattern));

        return this;
    }

    /**
     * Build a filter instance from all added filters.
     *
     * @return filter instance containing all filters
     */
    public IssueFilter build() {
        return filters.isEmpty() ? IssueFilter.NO_OP : aggregator.apply(filters);
    }

    private void addRegexFilter(Function<Issue, String> propertySelector, final String pattern) {
        filters.add(new GenericIssueFilter<>(propertySelector, (toMatch) -> toMatch.matches(pattern)));
    }

}

