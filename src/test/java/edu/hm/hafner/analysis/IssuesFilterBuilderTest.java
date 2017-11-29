package edu.hm.hafner.analysis;

import java.util.List;

import org.junit.jupiter.api.Test;

import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class IssuesFilterBuilderTest {

    private static final Issue ISSUE_ONE = new IssueBuilder().setFileName("A").build();
    private static final Issue ISSUE_TWO = new IssueBuilder().setFileName("B").build();
    private static final Issue ISSUE_THREE = new IssueBuilder().setFileName("C").build();

    private static final List<Issue> ALL = asList(ISSUE_ONE, ISSUE_TWO, ISSUE_THREE);

    private static final IssueFilter FIRST_FILTER = createInclusionFilter(ISSUE_TWO, ISSUE_THREE);
    private static final IssueFilter SECOND_FILTER = createInclusionFilter(ISSUE_THREE);

    @Test
    public void shouldCreateFilterWithTwo() {
        IssuesFilter filter = new IssuesFilterBuilder()
                .add(FIRST_FILTER)
                .add(SECOND_FILTER)
                .build();

        Issues filtered = filter.filter(new Issues(ALL));

        assertThat(filtered.all())
                .containsExactly(ISSUE_THREE);
    }

    @Test
    public void shouldCreateFilterContainingOneFilter() {

        IssuesFilter filter = new IssuesFilterBuilder()
                .add(FIRST_FILTER)
                .build();

        Issues filtered = filter.filter(new Issues(ALL));

        assertThat(filtered.all())
                .containsExactly(ISSUE_TWO, ISSUE_THREE);
    }

    @Test
    public void shouldCreateFilterContainingNoFilters() {
        IssuesFilter filter = new IssuesFilterBuilder().build();

        Issues filtered = filter.filter(new Issues(ALL));

        assertThat(filtered.all())
                .containsExactlyElementsOf(ALL);
    }

    private static IssueFilter createInclusionFilter(final Issue... issue) {
        IssueFilter filter = mock(IssueFilter.class);
        for (Issue i : issue) {
            when(filter.isIncluded(i)).thenReturn(true);
        }
        return filter;
    }
}