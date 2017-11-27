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

/**
 * Issue filter only returns issues which match to all filter criteria.
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
    public Issues filter(final Issues issues) {
        Iterable<Issue> iterable = issues::iterator;
        Stream<Issue> issueStream = StreamSupport.stream(iterable.spliterator(), false);

        List<Issue> filtered = issueStream.filter(getFilter()).collect(Collectors.toList());

        Issues result = new Issues();
        result.addAll(filtered);
        return result;
    }


    static class IssueFilterBuilder {

        private final Map<String, Map<Boolean, Predicate<Issue>>> filterCriteria = new HashMap<>();

        private Map<String, Map<Boolean, Predicate<Issue>>> getFilterCriteria() {
            return filterCriteria;
        }

        public IssueFilter build() {
            Predicate<Issue> filter = null;

            for (Map<Boolean, Predicate<Issue>> criterionContainer : getFilterCriteria().values()) {
                Predicate<Issue> subFilter = null;
                for (Predicate<Issue> filterCriterion : criterionContainer.values()) {
                    if(subFilter == null) {
                        subFilter = filterCriterion;
                    } else {
                        subFilter = subFilter.and(filterCriterion);
                    }
                }
                if (filter == null) {
                    filter = subFilter;
                } else {
                    filter = filter.and(subFilter);
                }

            }

            if (filter == null) {
                filter = issue -> true;
            }

            return new IssueFilter(filter);
        }

        /**
         * Create new criterion and append it at the end of the filter pipeline. All patterns will be or associated.
         * @param patterns collection of regex which will be or associated
         * @param issueVariableGetter getter of Issue which delivers a String and the regex is applied to
         * @param includeFilter true if it is a include filter, false if it is a exclude filter
         * @return this as reference (fluent interface)
         */
        private IssueFilterBuilder setFilterCriterion(final Collection<String> patterns, final Function<Issue, String> issueVariableGetter, final boolean includeFilter, final String criterionName) {
            String patternString = patterns.stream().collect(Collectors.joining("|"));
            final Pattern pattern = Pattern.compile(patternString);

            if(!getFilterCriteria().containsKey(criterionName)) {
                getFilterCriteria().put(criterionName, new HashMap<>());
            }

            getFilterCriteria().get(criterionName).put(includeFilter, issue -> pattern.matcher(issueVariableGetter.apply(issue)).matches() == includeFilter);
            return this;
        }

        public IssueFilterBuilder setIncludeTypeFilter(final Collection<String> patterns) {
            return setFilterCriterion(patterns, Issue::getType, true, "type");
        }

        public IssueFilterBuilder setExcludeTypeFilter(final Collection<String> patterns) {
            return setFilterCriterion(patterns, Issue::getType, false, "type");
        }

        public IssueFilterBuilder setIncludeCategoryFilter(final Collection<String> patterns) {
            return setFilterCriterion(patterns, Issue::getCategory, true, "category");
        }

        public IssueFilterBuilder setExcludeCategoryFilter(final Collection<String> patterns) {
            return setFilterCriterion(patterns, Issue::getCategory, false, "category");
        }

        public IssueFilterBuilder setIncludeFileNameFilter(final Collection<String> patterns) {
            return setFilterCriterion(patterns, Issue::getFileName, true, "fileName");
        }

        public IssueFilterBuilder setExcludeFileNameFilter(final Collection<String> patterns) {
            return setFilterCriterion(patterns, Issue::getFileName, false, "fileName");
        }

        public IssueFilterBuilder setIncludePackageNameFilter(final Collection<String> patterns) {
            return setFilterCriterion(patterns, Issue::getPackageName, true, "packageName");
        }

        public IssueFilterBuilder setExcludePackageNameFilter(final Collection<String> patterns) {
            return setFilterCriterion(patterns, Issue::getPackageName, false, "packageName");
        }

        public IssueFilterBuilder setIncludeModuleNameFilter(final Collection<String> patterns) {
            return setFilterCriterion(patterns, Issue::getModuleName, true, "moduleName");
        }

        public IssueFilterBuilder setExcludeModuleNameFilter(final Collection<String> patterns) {
            return setFilterCriterion(patterns, Issue::getModuleName, false, "moduleName");
        }
    }
}
