package edu.hm.hafner.analysis;

import java.util.regex.PatternSyntaxException;

import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.IssueFilter.Builder.*;
import static edu.hm.hafner.analysis.IssueFilter.IssueProperty.*;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;

/**
 * Unit test for {@link IssueFilter}.
 *
 * @author Marcel Binder
 */
class IssueFilterTest {
    private static final Issue ISSUE_A1 = new IssueBuilder()
            .setFileName("A1")
            .setPackageName("a.1")
            .setModuleName("module-a-1")
            .setCategory("category-a-1")
            .setType("type-a-1")
            .build();
    private static final Issue ISSUE_A2 = new IssueBuilder()
            .setFileName("A2")
            .setPackageName("a.2")
            .setModuleName("module-a-2")
            .setCategory("category-a-2")
            .setType("type-a-2")
            .build();
    private static final Issue ISSUE_A3 = new IssueBuilder()
            .setFileName("A3")
            .setPackageName("a.3")
            .setModuleName("module-a-3")
            .setCategory("category-a-3")
            .setType("type-a-3")
            .build();
    private static final Issue ISSUE_B1 = new IssueBuilder()
            .setFileName("B1")
            .setPackageName("b.1")
            .setModuleName("module-b-1")
            .setCategory("category-b-1")
            .setType("type-b-1")
            .build();
    private static final Issue ISSUE_B2 = new IssueBuilder()
            .setFileName("B2")
            .setPackageName("b.2")
            .setModuleName("module-b-2")
            .setCategory("category-b-2")
            .setType("type-b-2")
            .build();
    private static final Issue ISSUE_B3 = new IssueBuilder()
            .setFileName("B3")
            .setPackageName("b.3")
            .setModuleName("module-b-3")
            .setCategory("category-b-3")
            .setType("type-b-3")
            .build();
    private static final Issues ALL = new Issues(asList(ISSUE_A1, ISSUE_A2, ISSUE_A3, ISSUE_B1, ISSUE_B2, ISSUE_B3));
    private static final String INVALID_REGEX = "(";

    @Test
    void nonePassIfNoFilterNoIssues() {
        IssueFilter filter = issueFilter().build();

        Issues output = filter.filter(new Issues());

        assertThat(output).hasSize(0);
    }

    @Test
    void allPassIfNoFilterAnyIssues() {
        IssueFilter filter = issueFilter().build();

        Issues output = filter.filter(ALL);

        assertThat(output.all()).containsExactlyInAnyOrder(ISSUE_A1, ISSUE_A2, ISSUE_A3, ISSUE_B1, ISSUE_B2, ISSUE_B3);
    }

    @Test
    void onlyIncludedBySinglePatternPass() {
        IssueFilter filter = issueFilter()
                .include(FILE_NAME, singletonList("A."))
                .build();

        Issues output = filter.filter(ALL);

        assertThat(output.all()).containsExactlyInAnyOrder(ISSUE_A1, ISSUE_A2, ISSUE_A3);
    }

    @Test
    void onlyIncludedByAnyPatternPass() {
        IssueFilter filter = issueFilter()
                .include(FILE_NAME, asList("A.", ".1"))
                .build();

        Issues output = filter.filter(ALL);

        assertThat(output.all()).containsExactlyInAnyOrder(ISSUE_A1, ISSUE_A2, ISSUE_A3, ISSUE_B1);
    }

    @Test
    void noneIncluded() {
        IssueFilter filter = issueFilter()
                .include(FILE_NAME, singletonList("C."))
                .build();

        Issues output = filter.filter(ALL);

        assertThat(output.all()).isEmpty();
    }

    @Test
    void onlyNotExcludedBySinglePatternPass() {
        IssueFilter filter = issueFilter()
                .exclude(FILE_NAME, singletonList("A."))
                .build();

        Issues output = filter.filter(ALL);

        assertThat(output.all()).containsExactlyInAnyOrder(ISSUE_B1, ISSUE_B2, ISSUE_B3);
    }

    @Test
    void onlyNotExcludedByAnyPatternPass() {
        IssueFilter filter = issueFilter()
                .exclude(FILE_NAME, asList("A.", ".1"))
                .build();

        Issues output = filter.filter(ALL);

        assertThat(output.all()).containsExactlyInAnyOrder(ISSUE_B2, ISSUE_B3);
    }

    @Test
    void allExcluded() {
        IssueFilter filter = issueFilter()
                .exclude(FILE_NAME, asList("A.", "B."))
                .build();

        Issues output = filter.filter(ALL);

        assertThat(output.all()).isEmpty();
    }

    @Test
    void onlyIncludedAndNotExcludedPass() {
        IssueFilter filter = issueFilter()
                .include(FILE_NAME, asList(".1", "B."))
                .exclude(FILE_NAME, singletonList(".3"))
                .build();

        Issues output = filter.filter(ALL);

        assertThat(output.all()).containsExactlyInAnyOrder(ISSUE_A1, ISSUE_B1, ISSUE_B2);
    }

    @Test
    void filterByPackageName() {
        IssueFilter filter = issueFilter()
                .include(PACKAGE_NAME, singletonList("a.*"))
                .exclude(PACKAGE_NAME, singletonList(".*\\.1"))
                .build();

        Issues output = filter.filter(ALL);

        assertThat(output.all()).containsExactlyInAnyOrder(ISSUE_A2, ISSUE_A3);
    }

    @Test
    void filterByModuleName() {
        IssueFilter filter = issueFilter()
                .include(MODULE_NAME, singletonList("module-a-."))
                .exclude(MODULE_NAME, singletonList("module-.-1"))
                .build();

        Issues output = filter.filter(ALL);

        assertThat(output.all()).containsExactlyInAnyOrder(ISSUE_A2, ISSUE_A3);
    }

    @Test
    void filterByCategory() {
        IssueFilter filter = issueFilter()
                .include(CATEGORY, singletonList("category-a-."))
                .exclude(CATEGORY, singletonList("category-.-1"))
                .build();

        Issues output = filter.filter(ALL);

        assertThat(output.all()).containsExactlyInAnyOrder(ISSUE_A2, ISSUE_A3);
    }

    @Test
    void filterByType() {
        IssueFilter filter = issueFilter()
                .include(TYPE, singletonList("type-a-."))
                .exclude(TYPE, singletonList("type-.-1"))
                .build();

        Issues output = filter.filter(ALL);

        assertThat(output.all()).containsExactlyInAnyOrder(ISSUE_A2, ISSUE_A3);
    }

    @Test
    void filterByMultipleProperties() {
        IssueFilter filter = issueFilter()
                .include(PACKAGE_NAME, singletonList("a.*"))
                .include(TYPE, singletonList("type-b-."))
                .exclude(PACKAGE_NAME, singletonList(".*1"))
                .build();

        Issues output = filter.filter(ALL);

        assertThat(output.all()).containsExactlyInAnyOrder(ISSUE_A2, ISSUE_A3, ISSUE_B2, ISSUE_B3);
    }

    @Test
    void invalidRegex() {
        assertThatThrownBy(() -> issueFilter().include(FILE_NAME, singletonList(INVALID_REGEX)))
                .isInstanceOf(InvalidRegexException.class)
                .hasMessageContaining(FILE_NAME.name())
                .hasMessageContaining(INVALID_REGEX)
                .hasCauseInstanceOf(PatternSyntaxException.class);
    }
}
