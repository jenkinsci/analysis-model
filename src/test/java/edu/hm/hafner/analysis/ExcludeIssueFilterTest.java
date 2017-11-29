package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.*;

class ExcludeIssueFilterTest {

    private static final String TYPE_A = "TypeA";
    private static final String TYPE_B = "TypeB";

    private static final String MODULE_A = "Module A";
    private static final String MODULE_B = "Module B";

    private static final Issue ISSUE_A = new IssueBuilder()
            .setType(TYPE_A)
            .setModuleName(MODULE_A)
            .build();

    private static final Issue ISSUE_B = new IssueBuilder()
            .setType(TYPE_B)
            .setModuleName(MODULE_B)
            .build();

    @Test
    void shouldExcludeWhenFilterMatches() {
        IssueFilter filter = new ExcludeIssueFilterBuilder()
                .addTypeFilters(singletonList(TYPE_A))
                .build();

        assertThat(filter.isIncluded(ISSUE_A))
                .isFalse();

        assertThat(filter.isIncluded(ISSUE_B))
                .isTrue();
    }

    @Test
    void shouldUsePatternsAsOrCombination() {
        IssueFilter filter = new ExcludeIssueFilterBuilder()
                .addTypeFilters(asList(TYPE_A, TYPE_B))
                .build();

        assertThat(filter.isIncluded(ISSUE_A))
                .isFalse();

        assertThat(filter.isIncluded(ISSUE_B))
                .isFalse();
    }

    @Test
    void shouldUseFiltersAsOrCombination() {
        IssueFilter filter = new ExcludeIssueFilterBuilder()
                .addTypeFilters(singletonList(TYPE_A))
                .addModuleFilters(singletonList(MODULE_B))
                .build();

        assertThat(filter.isIncluded(ISSUE_A))
                .isFalse();

        assertThat(filter.isIncluded(ISSUE_B))
                .isFalse();
    }

    @Test
    void shouldNotExcludeWhenEmpty() {
        IssueFilter filter = new ExcludeIssueFilterBuilder().build();

        assertThat(filter.isIncluded(ISSUE_A))
                .isTrue();

        assertThat(filter.isIncluded(ISSUE_B))
                .isTrue();
    }

}