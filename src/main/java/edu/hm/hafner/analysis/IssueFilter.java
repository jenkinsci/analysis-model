package edu.hm.hafner.analysis;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
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

    private final Function<Stream<Issue>, Stream<Issue>> filter;

    private IssueFilter(Function<Stream<Issue>, Stream<Issue>> filter) {
        this.filter = filter;
    }

    private Function<Stream<Issue>, Stream<Issue>> getFilter() {
        return filter;
    }

    /**
     * Apply the filter to a issues object.
     * @param issues to filter
     * @return filtered issues, which only contains matching issues
     */
    public Issues filter(final Issues issues) {
        Iterable<Issue> iterable = () -> issues.iterator();
        Stream<Issue> issueStream = StreamSupport.stream(iterable.spliterator(), false);

        List<Issue> filtered = getFilter().apply(issueStream).collect(Collectors.toList());

        Issues result = new Issues();
        result.addAll(filtered);
        return result;
    }


    static class IssueFilterBuilder {

        /**
         * Filter function which should be applied to the issue stream.
         */
        private Function<Stream<Issue>, Stream<Issue>> filter = (Stream<Issue> s) -> s;

        private Function<Stream<Issue>, Stream<Issue>> getFilter() {
            return filter;
        }

        private void setFilter(Function<Stream<Issue>, Stream<Issue>> filter) {
            this.filter = filter;
        }

        /**
         * Append a new filter criterion at the end of the filer.
         * @param filter intermediate function which returns a stream of the wanted issues.
         */
        private void appendFilter(Function<Stream<Issue>, Stream<Issue>> filter) {
            setFilter(getFilter().andThen(filter));
        }

        public IssueFilter build() {
            return new IssueFilter(getFilter());
        }

        /**
         * Create new criterion and append it at the end of the filter pipeline. All patterns will be or associated.
         * @param patterns collection of regex which will be or associated
         * @param issueVariableGetter getter of Issue which delivers a String and the regex is applied to
         * @param includeFilter true if it is a include filter, false if it is a exclude filter
         * @return this as reference (fluent interface)
         */
        private IssueFilterBuilder addFilterCriterion(final Collection<String> patterns, final Function<Issue, String> issueVariableGetter ,final boolean includeFilter) {
            String patternString = patterns.stream().collect(Collectors.joining("|"));
            final Pattern pattern = Pattern.compile(patternString);
            appendFilter(stream -> stream.filter(issue -> pattern.matcher(issueVariableGetter.apply(issue)).matches() == includeFilter));
            return this;
        }

        public IssueFilterBuilder addIncludeTypeFilter(final Collection<String> patterns) {
            return addFilterCriterion(patterns, issue -> issue.getType(), true);
        }

        public IssueFilterBuilder addExcludeTypeFilter(final Collection<String> patterns) {
            return addFilterCriterion(patterns, issue -> issue.getType(), false);
        }

        public IssueFilterBuilder addIncludeCategoryFilter(final Collection<String> patterns) {
            return addFilterCriterion(patterns, issue -> issue.getCategory(), true);
        }

        public IssueFilterBuilder addExcludeCategoryFilter(final Collection<String> patterns) {
            return addFilterCriterion(patterns, issue -> issue.getCategory(), false);
        }

        public IssueFilterBuilder addIncludeFileNameFilter(final Collection<String> patterns) {
            return addFilterCriterion(patterns, issue -> issue.getFileName(), true);
        }

        public IssueFilterBuilder addExcludeFileNameFilter(final Collection<String> patterns) {
            return addFilterCriterion(patterns, issue -> issue.getFileName(), false);
        }

        public IssueFilterBuilder addIncludePackageNameFilter(final Collection<String> patterns) {
            return addFilterCriterion(patterns, issue -> issue.getPackageName(), true);
        }

        public IssueFilterBuilder addExcludePackageNameFilter(final Collection<String> patterns) {
            return addFilterCriterion(patterns, issue -> issue.getPackageName(), false);
        }

        public IssueFilterBuilder addIncludeModuleNameFilter(final Collection<String> patterns) {
            return addFilterCriterion(patterns, issue -> issue.getModuleName(), true);
        }

        public IssueFilterBuilder addExcludeModuleNameFilter(final Collection<String> patterns) {
            return addFilterCriterion(patterns, issue -> issue.getModuleName(), false);
        }
    }
}
