package edu.hm.hafner.analysis;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.assertj.IssuesAssert.assertThat;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import static java.util.Arrays.asList;

/**
 * Tests for {@link IssueFilter}.
 *
 * @author Joscha Behrmann
 */
public class IssueFilterTest {

    private static final String REGEX_NO_MATCH = ".*#-";
    private static final String REGEX_MATCH = ".*#";

    private static final Issue ISSUE_1 = new IssueBuilder()
            .setFileName("filter_me#1")
            .setPackageName("filter_me#1")
            .setModuleName("filter_me#1")
            .setCategory("filter_me#1")
            .setType("filter_me#1")
            .build();

    private static final Issue ISSUE_2 = new IssueBuilder()
            .setFileName("filter_me#2")
            .setPackageName("filter_me#2")
            .setModuleName("filter_me#2")
            .setCategory("filter_me#2")
            .setType("filter_me#2")
            .build();

    private static final Issue ISSUE_3 = new IssueBuilder()
            .setFileName("filter_me#3")
            .setPackageName("filter_me#3")
            .setModuleName("filter_me#3")
            .setCategory("filter_me#3")
            .setType("filter_me#3")
            .build();

    private static final Issue ISSUE_4 = new IssueBuilder()
            .setFileName("filter_me#4")
            .setPackageName("filter_me#4")
            .setModuleName("filter_me#4")
            .setCategory("filter_me#4")
            .setType("filter_me#4")
            .build();

    private static final Issue ISSUE_5 = new IssueBuilder()
            .setFileName("filter_me#5")
            .setPackageName("filter_me#5")
            .setModuleName("filter_me#5")
            .setCategory("filter_me#5")
            .setType("filter_me#5")
            .build();

    private static final Issues ISSUES = new Issues(asList(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5));

    @Test
    void emptyListWithEmptyFiltersShouldRemainEmpty() {
        final IssueFilter issueFilter = new IssueFilter();

        assertThat(issueFilter.applyFilters(new Issues()))
                .as("Empty list should remain empty after applying no filters.")
                .hasSize(0);
    }

    @Test
    void emptyListWhenFiltersSetShouldRemainEmpty() {
        final IssueFilter issueFilter =
                new IssueFilter()
                        .addIncludedFiles(match(1, 3))
                        .addExcludedFiles(match(1, 2));

        assertThat(issueFilter.applyFilters(new Issues()))
                .as("Empty list should remain empty after applying filters.")
                .hasSize(0);
    }

    @Test
    void issuesShouldRemainEqualWhenNoFiltersSet() {
        final IssueFilter issueFilter = new IssueFilter();

        assertThat(issueFilter.applyFilters(ISSUES))
                .as("No issues should be filtered out when no filters are set.")
                .hasSize(ISSUES.getSize());
    }

    @Test
    void noIssuesShouldBeFilteredWhenNoFiltersMatch() {
        final IssueFilter issueFilter =
                new IssueFilter()
                        .addExcludedFiles(match(-7, 0, 200))
                        .addExcludedPackages(match(0))
                        .addExcludedModules(match(Integer.MIN_VALUE))
                        .addExcludedTypes(match(Integer.MAX_VALUE));

        assertThat(issueFilter.applyFilters(ISSUES))
                .as("No issues should be filtered out when no filters match.")
                .hasSize(ISSUES.getSize());
    }

    /*
     * Adding broken regex to the filter have no effect on the result.
     */
    @Test
    void brokenRegexShouldNotBeConsidered() {
        final IssueFilter issueFilter =
                new IssueFilter()
                        .addIncludedFiles(asList("*__*"))
                        .addExcludedFiles(asList("*_*"));

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(issueFilter.applyFilters(ISSUES))
                    .as("Adding invalid regex as filters should not apply any filtering to the issues.")
                    .hasSize(5);

            // Add include by filenames
            issueFilter.addIncludedFiles(match(3, 4));
            assertThat(issueFilter.applyFilters(ISSUES))
                    .as("Adding invalid regex as filters should not add or alter any filters.")
                    .containsExactly(ISSUE_3, ISSUE_4);

            // Add exclude by category
            issueFilter
                    .addExcludedCategories(match(3))
                    .addExcludedCategories(noMatch(3));
            assertThat(issueFilter.applyFilters(ISSUES))
                    .as("Adding invalid regex as filters should not add or alter any filters.")
                    .containsExactly(ISSUE_4);
        });
    }

    /*
     * Excluded files should not appear in the result even when they were included by a filter.
     */
    @Test
    void includedFilesShouldBeExcludable() {
        final IssueFilter issueFilter =
                new IssueFilter()
                        .addIncludedFiles(match(2, 4, 5, Integer.MAX_VALUE))
                        .addExcludedFiles(match(4));

        assertThat(issueFilter.applyFilters(ISSUES))
                .as("Included files should be excluded when they are excluded by a filter.")
                .containsExactly(ISSUE_2, ISSUE_5);
    }

    @Test
    void onlyIncludedFilesShouldBeIncluded() {
        final IssueFilter issueFilter =
                new IssueFilter().addIncludedFiles(match(1, 3, Integer.MAX_VALUE));

        assertThat(issueFilter.applyFilters(ISSUES))
                .containsExactly(ISSUE_1, ISSUE_3);
    }

    @Test
    void excludedFilesShouldBeExcluded() {
        final IssueFilter issueFilter =
                new IssueFilter().addExcludedFiles(match(1, 3, Integer.MAX_VALUE));

        assertThat(issueFilter.applyFilters(ISSUES))
                .containsExactly(ISSUE_2, ISSUE_4, ISSUE_5);
    }

    @Test
    void onlyIncludedPackagesShouldBeIncluded() {
        final IssueFilter issueFilter =
                new IssueFilter().addIncludedPackages(match(1, 3, Integer.MAX_VALUE));

        assertThat(issueFilter.applyFilters(ISSUES))
                .containsExactly(ISSUE_1, ISSUE_3);
    }

    @Test
    void excludedPackagesShouldBeExcluded() {
        final IssueFilter issueFilter =
                new IssueFilter().addExcludedPackages(match(1, 3, Integer.MAX_VALUE));

        assertThat(issueFilter.applyFilters(ISSUES))
                .containsExactly(ISSUE_2, ISSUE_4, ISSUE_5);
    }

    @Test
    void onlyIncludedModulesShouldBeIncluded() {
        final IssueFilter issueFilter =
                new IssueFilter().addIncludedModules(match(1, 3, Integer.MAX_VALUE));

        assertThat(issueFilter.applyFilters(ISSUES))
                .containsExactly(ISSUE_1, ISSUE_3);
    }

    @Test
    void excludedModulesShouldBeExcluded() {
        final IssueFilter issueFilter =
                new IssueFilter().addExcludedModules(match(1, 3, Integer.MAX_VALUE));

        assertThat(issueFilter.applyFilters(ISSUES))
                .containsExactly(ISSUE_2, ISSUE_4, ISSUE_5);
    }

    @Test
    void onlyIncludedCategoriesShouldBeIncluded() {
        final IssueFilter issueFilter =
                new IssueFilter().addIncludedCategories(match(1, 3, Integer.MAX_VALUE));

        assertThat(issueFilter.applyFilters(ISSUES))
                .containsExactly(ISSUE_1, ISSUE_3);
    }

    @Test
    void excludedCategoriesShouldBeExcluded() {
        final IssueFilter issueFilter =
                new IssueFilter().addExcludedCategories(match(1, 3, Integer.MAX_VALUE));

        assertThat(issueFilter.applyFilters(ISSUES))
                .containsExactly(ISSUE_2, ISSUE_4, ISSUE_5);
    }

    @Test
    void onlyIncludedTypesShouldBeIncluded() {
        final IssueFilter issueFilter =
                new IssueFilter().addIncludedTypes(match(1, 3, Integer.MAX_VALUE));

        assertThat(issueFilter.applyFilters(ISSUES))
                .containsExactly(ISSUE_1, ISSUE_3);
    }

    @Test
    void excludedTypesShouldBeExcluded() {
        final IssueFilter issueFilter =
                new IssueFilter().addExcludedTypes(match(1, 3, Integer.MAX_VALUE));

        assertThat(issueFilter.applyFilters(ISSUES))
                .containsExactly(ISSUE_2, ISSUE_4, ISSUE_5);
    }

    @Test
    void mixingFiltersShouldReturnCorrectResult() {
        final IssueFilter issueFilter = new IssueFilter();
        final Issues issues = issuesGenerator(100);

        SoftAssertions.assertSoftly(softly -> {
            // Include multiples of 2 by fileName (adds 51 elements)
            int[] two = IntStream.range(0, 100).map(i -> i + i).toArray();
            issueFilter
                    .addIncludedFiles(match(two));
            Issues includeTwo = issueFilter.applyFilters(issues);
            softly.assertThat(includeTwo).hasSize(51);

            // Include multiples of 3 by packageName, exclude multiples of 2
            int[] three = IntStream.range(0, 100).map(i -> i * 3).toArray();
            issueFilter
                    .addIncludedPackages(match(three))
                    .addExcludedFiles(match(two));
            Issues includeThree = issueFilter.applyFilters(issues);
            softly.assertThat(includeThree).hasSize(17);

            // Include multiples of 7 by module-name, exclude multiples of 3
            // by package-name, exclude multiples of 2 by filename
            int[] seven = IntStream.range(0, 100).map(i -> i * 7).toArray();
            issueFilter
                    .addIncludedModules(match(seven))
                    .addExcludedPackages(match(three))
                    .addExcludedFiles(match(two));
            Issues includeSeven = issueFilter.applyFilters(issues);
            softly.assertThat(includeSeven).hasSize(5);
        });
    }

    /*
     * When an issue matches multiple include-filters it should still only be added once.
     */
    @Test
    void multipleIncludesShouldNotIncludeSameIssueMultipleTimes() {
        List<String> sameIssues = match(1, 2, 3);
        final IssueFilter issueFilter = new IssueFilter()
                .addIncludedFiles(sameIssues)
                .addIncludedPackages(sameIssues)
                .addIncludedModules(sameIssues)
                .addIncludedCategories(sameIssues)
                .addIncludedTypes(sameIssues);

        assertThat(issueFilter.applyFilters(ISSUES))
                .as("Multiple includes for the same issue should only include it once.")
                .containsExactly(ISSUE_1, ISSUE_2, ISSUE_3);
    }

    @Test
    void multipleIncludesShouldAccumulate() {
        final IssueFilter issueFilter = new IssueFilter();
        final Issues issues = issuesGenerator(100);
        int[] two = IntStream.range(0, 100).map(i -> i + i).toArray();
        int[] three = IntStream.range(0, 100).map(i -> i * 3).toArray();

        SoftAssertions.assertSoftly(softly -> {
            issueFilter
                    .addIncludedFiles(match(two))
                    .addIncludedPackages(match(three));
            Issues includeTwoThree = issueFilter.applyFilters(issues);
            softly.assertThat(includeTwoThree).hasSize(68);

            // Exclude multiples of 2 by type
            issueFilter.addExcludedTypes(match(two));
            Issues includeTwoThreeExcludeTwo = issueFilter.applyFilters(issues);
            softly.assertThat(includeTwoThreeExcludeTwo).hasSize(17);

            // Include twos, threes, exclude twos, threes
            issueFilter.addExcludedModules(match(three));
            Issues includeTwoThreeExcludeTwoThree = issueFilter.applyFilters(issues);
            softly.assertThat(includeTwoThreeExcludeTwoThree).hasSize(0);
        });
    }

    private List<String> match(int... issues) {
        return Arrays.stream(issues).mapToObj(i -> REGEX_MATCH + i).collect(Collectors.toList());
    }

    private List<String> noMatch(int... issues) {
        return Arrays.stream(issues).mapToObj(i -> REGEX_NO_MATCH + i).collect(Collectors.toList());
    }

    private Issues issuesGenerator(int n) {
        final Issues issues = new Issues();

        for (int i = 0; i <= n; i++) {
            Issue issue = new IssueBuilder()
                    .setFileName("filter_me#" + i)
                    .setPackageName("filter_me#" + i)
                    .setModuleName("filter_me#" + i)
                    .setCategory("filter_me#" + i)
                    .setType("filter_me#" + i)
                    .build();
            issues.add(issue);
        }

        return issues;
    }
}