package edu.hm.hafner.analysis;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.*;

class IssuesFilterTest {

    private static final Issue ISSUE_FILENAME_INCLUDED = new IssueBuilder()
            .setFileName("Name")
            .build();

    private static final Issue ISSUE_PACKAGE_INCLUDED = new IssueBuilder()
            .setModuleName("Name")
            .build();

    private static final List<Issue> ALL = asList(ISSUE_FILENAME_INCLUDED, ISSUE_PACKAGE_INCLUDED);

    @Test
    void shouldCopyIssuesWhenNoFiltersSupplied() {
        IssuesFilter sut = new IssuesFilter(Lists.emptyList());

        Issues issues = new Issues(ALL);
        Issues sameContent = sut.filter(issues);

        assertThat(sameContent).isNotSameAs(issues);

        assertThat(sameContent.all())
                .containsExactlyElementsOf(issues.copy());
    }

    @Test
    void shouldIncludeAllWhenAllFiltersTrue() {
        List<IssueFilter> filters = asList(i -> true, i -> true);

        IssuesFilter sut = new IssuesFilter(filters);

        Issues issues = new Issues(ALL);
        Issues sameContent = sut.filter(issues);

        assertThat(sameContent.all())
                .containsExactlyElementsOf(ALL);
    }

    @Test
    void shouldIncludeWhenFilterIncludes() {
        List<IssueFilter> filters = singletonList(ISSUE_FILENAME_INCLUDED::equals);

        IssuesFilter sut = new IssuesFilter(filters);

        Issues issues = new Issues(ALL);
        Issues sameContent = sut.filter(issues);

        assertThat(sameContent.all())
                .containsExactly(ISSUE_FILENAME_INCLUDED);
    }

    @Test
    void shouldIncludeNoneWhenAllFiltered() {
        List<IssueFilter> filters = singletonList(issue -> false);

        IssuesFilter sut = new IssuesFilter(filters);

        Issues issues = new Issues(ALL);
        Issues empty = sut.filter(issues);

        assertThat(empty.all())
                .hasSize(0);
    }

    @Test
    void shouldTreatFiltersAsAndCombination() {
        List<IssueFilter> filters = asList(
                issue -> ISSUE_FILENAME_INCLUDED.equals(issue) || ISSUE_PACKAGE_INCLUDED.equals(issue),
                ISSUE_PACKAGE_INCLUDED::equals);
        IssuesFilter sut = new IssuesFilter(filters);

        Issues issues = new Issues(ALL);
        Issues sameContent = sut.filter(issues);

        assertThat(sameContent.all())
                .containsExactly(ISSUE_PACKAGE_INCLUDED);
    }
}