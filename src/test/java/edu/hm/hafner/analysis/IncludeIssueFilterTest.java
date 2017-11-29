package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.*;

class IncludeIssueFilterTest {

    private static final String FILE_A = "file.a";
    private static final String FILE_B = "file.b";

    private static final String CAT_A = "Cat a";
    private static final String CAT_B = "Cat b";

    private static final Issue ISSUE_A = new IssueBuilder()
            .setCategory(CAT_A)
            .setFileName(FILE_A)
            .build();

    private static final Issue ISSUE_B = new IssueBuilder()
            .setCategory(CAT_B)
            .setFileName(FILE_B)
            .build();

    @Test
    void shouldFilterUsingOneFilterOnePattern() {
        IssueFilter filter = new IncludeIssueFilterBuilder()
                .addCategoryFilters(singletonList(CAT_A))
                .build();

        assertThat(filter.isIncluded(ISSUE_A))
                .isTrue();

        assertThat(filter.isIncluded(ISSUE_B))
                .isFalse();
    }

    @Test
    void shouldUsePatternsAsOrCombination() {
        IssueFilter filter = new IncludeIssueFilterBuilder()
                .addCategoryFilters(asList(CAT_A, CAT_B))
                .build();

        assertThat(filter.isIncluded(ISSUE_A))
                .isTrue();

        assertThat(filter.isIncluded(ISSUE_B))
                .isTrue();
    }

    @Test
    void shouldUseFiltersAsOrCombination() {
        IssueFilter filter = new IncludeIssueFilterBuilder()
                .addFileFilters(singletonList(FILE_A))
                .addCategoryFilters(singletonList(CAT_B))
                .build();

        assertThat(filter.isIncluded(ISSUE_A))
                .isTrue();

        assertThat(filter.isIncluded(ISSUE_B))
                .isTrue();
    }

    @Test
    void shouldNotIncludeWhenAllFiltersFail() {
        IssueFilter filter = new IncludeIssueFilterBuilder()
                .addFileFilters(singletonList("NoneSense"))
                .addCategoryFilters(singletonList("NoneSense"))
                .build();

        assertThat(filter.isIncluded(ISSUE_A))
                .isFalse();

        assertThat(filter.isIncluded(ISSUE_B))
                .isFalse();
    }

    @Test
    void shouldIncludeWhenNoFiltersConfigure() {
        IssueFilter filter = new IncludeIssueFilterBuilder()
                .build();

        assertThat(filter.isIncluded(ISSUE_A))
                .isTrue();

        assertThat(filter.isIncluded(ISSUE_B))
                .isTrue();
    }
}