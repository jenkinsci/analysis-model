package edu.hm.hafner.analysis;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class IssueFilter {

    private final Function<Stream<Issue>, Stream<Issue>> filter;

    private IssueFilter(Function<Stream<Issue>, Stream<Issue>> filter) {
        this.filter = filter;
    }

    private Function<Stream<Issue>, Stream<Issue>> getFilter() {
        return filter;
    }

    public Issues filter(final Issues issues) {
        Iterable<Issue> iterable = () -> issues.iterator();
        List<Issue> filtered = getFilter().apply(StreamSupport.stream(iterable.spliterator(), false)).collect(Collectors.toList());
        Issues result = new Issues();
        result.addAll(filtered);
        return result;
    }



    static class IssueFilterBuilder {

        private Function<Stream<Issue>, Stream<Issue>> filter = (Stream<Issue> s) -> s;

        private Function<Stream<Issue>, Stream<Issue>> getFilter() {
            return filter;
        }

        private void setFilter(Function<Stream<Issue>, Stream<Issue>> filter) {
            this.filter = filter;
        }

        private void appendFilter(Function<Stream<Issue>, Stream<Issue>> filter) {
            setFilter(stream -> filter.apply(stream));
        }

        public IssueFilter build() {
            return new IssueFilter(getFilter());
        }

        private IssueFilterBuilder setFilter(final Collection<String> patterns, final Function<Issue, String> issueVariable ,final boolean includeFilter) {
            String patternString = patterns.stream().collect(Collectors.joining("|"));
            final Pattern pattern = Pattern.compile(patternString);
            appendFilter(stream -> stream.filter(issue -> pattern.matcher(issueVariable.apply(issue)).matches() == includeFilter));
            return this;
        }

        public IssueFilterBuilder setIncludeTypeFilter(final Collection<String> patterns) {
            return setFilter(patterns, issue -> issue.getType(), true);
        }

        public IssueFilterBuilder setExcludeTypeFilter(final Collection<String> patterns) {
            return setFilter(patterns, issue -> issue.getType(), false);
        }

        public IssueFilterBuilder setIncludeCategoryFilter(final Collection<String> patterns) {
            return setFilter(patterns, issue -> issue.getCategory(), true);
        }

        public IssueFilterBuilder setExcludeCategoryFilter(final Collection<String> patterns) {
            return setFilter(patterns, issue -> issue.getCategory(), false);
        }

        public IssueFilterBuilder setIncludeFileNameFilter(final Collection<String> patterns) {
            return setFilter(patterns, issue -> issue.getFileName(), true);
        }

        public IssueFilterBuilder setExcludeFileNameFilter(final Collection<String> patterns) {
            return setFilter(patterns, issue -> issue.getFileName(), false);
        }

        public IssueFilterBuilder setIncludePackageNameFilter(final Collection<String> patterns) {
            return setFilter(patterns, issue -> issue.getPackageName(), true);
        }

        public IssueFilterBuilder setExcludePackageNameFilter(final Collection<String> patterns) {
            return setFilter(patterns, issue -> issue.getPackageName(), false);
        }

        public IssueFilterBuilder setIncludeModuleNameFilter(final Collection<String> patterns) {
            return setFilter(patterns, issue -> issue.getModuleName(), true);
        }

        public IssueFilterBuilder setExcludeModuleNameFilter(final Collection<String> patterns) {
            return setFilter(patterns, issue -> issue.getModuleName(), false);
        }
    }
}
