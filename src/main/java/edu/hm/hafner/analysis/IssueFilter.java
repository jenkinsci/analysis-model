package edu.hm.hafner.analysis;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.sun.istack.internal.NotNull;

/**
 * Issue filter only returns issues which match at least one filter criterion.
 * This class is test driven developed.
 *
 * @author Michael Schmid
 */
public class IssueFilter {

    private final Predicate<Issue> filter;

    private IssueFilter(Predicate<Issue> filter) {
        this.filter = filter;
    }

    private Predicate<Issue> getFilter() {
        return filter;
    }

    /**
     * Apply the filter to a issues object.
     * @param issues to filter
     * @return filtered issues, which only contains matching issues
     */
    @NotNull
    public Issues filter(final Issues issues) {
        Stream<Issue> issueStream = StreamSupport.stream(issues.spliterator(), false);

        List<Issue> filtered = issueStream.filter(getFilter()).collect(Collectors.toList());

        Issues result = new Issues();
        result.addAll(filtered);
        return result;
    }

    enum FilterCriterionScope {
        TYPE(Issue::getType),
        CATEGORY(Issue::getCategory),
        FILE_NAME(Issue::getFileName),
        PACKAGE_NAME(Issue::getPackageName),
        MODULE_NAME(Issue::getModuleName);

        /**
         * Getter of Issue which delivers a String the regex is applied to
         */
        private final Function<Issue, String> issueVariableGetter;

        FilterCriterionScope(Function<Issue, String> issueVariableGetter) {
            this.issueVariableGetter = issueVariableGetter;
        }

        public Function<Issue, String> getIssueVariableGetter() {
            return issueVariableGetter;
        }
    }


    static class IssueFilterBuilder {

        private final Map<FilterCriterionScope, Map<Boolean, Predicate<Issue>>> filterCriteria = new HashMap<>();

        private Map<FilterCriterionScope, Map<Boolean, Predicate<Issue>>> getFilterCriteria() {
            return filterCriteria;
        }

        /**
         * Generate a IssueFilter with the set filter criteria,
         * Put include and exclude of each scope with an and together. All this connect all these predicates with or.
         * @return a IssueFilter with the set criteria
         */
        public IssueFilter build() {
            Predicate<Issue> filter = getFilterCriteria().values().stream()
                    .map(criterionContainer -> criterionContainer.values().stream()
                            .reduce(issue -> true, Predicate::and))
                    .reduce(issue -> false, Predicate::or);

            if(getFilterCriteria().size() == 0) {
                filter = issue -> true;
            }

            return new IssueFilter(filter);
        }

        /**
         * Create new criterion and set it as to the criteria map.
         * @param criterionScope scope which variable should be filtered
         * @param includeFilter true if it is a include filter, false if it is a exclude filter
         * @param patterns collection of regex which will be or associated
         * @return this as reference (fluent interface)
         */
        private IssueFilterBuilder setFilterCriterion(final FilterCriterionScope criterionScope, final boolean includeFilter, final Collection<String> patterns) {
            String patternString = patterns.stream().collect(Collectors.joining("|"));
            final Pattern pattern = Pattern.compile(patternString);

            if(!getFilterCriteria().containsKey(criterionScope)) {
                getFilterCriteria().put(criterionScope, new HashMap<>());
            }

            getFilterCriteria().get(criterionScope)
                    .put(includeFilter, issue -> pattern.matcher(criterionScope.getIssueVariableGetter().apply(issue)).matches() == includeFilter);
            return this;
        }

        public IssueFilterBuilder setIncludeTypeFilter(final Collection<String> patterns) {
            return setFilterCriterion(FilterCriterionScope.TYPE, true, patterns);
        }

        public IssueFilterBuilder setExcludeTypeFilter(final Collection<String> patterns) {
            return setFilterCriterion(FilterCriterionScope.TYPE,false, patterns);
        }

        public IssueFilterBuilder setIncludeCategoryFilter(final Collection<String> patterns) {
            return setFilterCriterion(FilterCriterionScope.CATEGORY, true, patterns);
        }

        public IssueFilterBuilder setExcludeCategoryFilter(final Collection<String> patterns) {
            return setFilterCriterion(FilterCriterionScope.CATEGORY,false, patterns);
        }

        public IssueFilterBuilder setIncludeFileNameFilter(final Collection<String> patterns) {
            return setFilterCriterion(FilterCriterionScope.FILE_NAME,true, patterns);
        }

        public IssueFilterBuilder setExcludeFileNameFilter(final Collection<String> patterns) {
            return setFilterCriterion(FilterCriterionScope.FILE_NAME,false, patterns);
        }

        public IssueFilterBuilder setIncludePackageNameFilter(final Collection<String> patterns) {
            return setFilterCriterion(FilterCriterionScope.PACKAGE_NAME,true, patterns);
        }

        public IssueFilterBuilder setExcludePackageNameFilter(final Collection<String> patterns) {
            return setFilterCriterion(FilterCriterionScope.PACKAGE_NAME,false, patterns);
        }

        public IssueFilterBuilder setIncludeModuleNameFilter(final Collection<String> patterns) {
            return setFilterCriterion(FilterCriterionScope.MODULE_NAME,true, patterns);
        }

        public IssueFilterBuilder setExcludeModuleNameFilter(final Collection<String> patterns) {
            return setFilterCriterion(FilterCriterionScope.MODULE_NAME,false, patterns);
        }
    }
}
