package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.assertj.IssueAssert.assertThat;
import static edu.hm.hafner.analysis.assertj.IssuesAssert.assertThat;
import static java.util.Arrays.asList;

public class IssuesFilterTest {

    private static final Issue ISSUE_1 = new IssueBuilder()
            .setMessage("issue-1")
            .setFileName("file-1")
            .setPackageName("package-1")
            .setModuleName("module-1")
            .setCategory("category-1")
            .setPriority(Priority.HIGH)
            .build();
    private static final Issue ISSUE_2 = new IssueBuilder()
            .setMessage("issue-2")
            .setFileName("file-1")
            .setPackageName("package-1")
            .setModuleName("module-1")
            .setType("type-1")
            .build();
    private static final Issue ISSUE_3 = new IssueBuilder()
            .setMessage("issue-3")
            .setFileName("file-1")
            .setPackageName("package-1")
            .setModuleName("module-1")
            .build();
    private static final Issue ISSUE_4 = new IssueBuilder()
            .setMessage("issue-4")
            .setFileName("file-2")
            .setPackageName("package-2")
            .setType("type-2")
            .setPriority(Priority.LOW)
            .build();
    private static final Issue ISSUE_5 = new IssueBuilder()
            .setMessage("issue-5")
            .setFileName("file-2")
            .setPackageName("package-2")
            .setPriority(Priority.LOW)
            .build();
    private static final Issue ISSUE_6 = new IssueBuilder()
            .setMessage("issue-6")
            .setFileName("file-3")
            .setModuleName("module-2")
            .setCategory("category-2")
            .setPriority(Priority.LOW)
            .build();

    private static final Issues ISSUES_1 = new Issues(asList(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6));

    @Test
    void filteringEmptyIssuesShouldReturnEmptyIssues() {
        final IssuesFilter issuesFilter = new IssuesFilter(new Issues());
        assertThat(issuesFilter.filter())
                .hasSize(0);
    }

    @Test
    void noIncludeFilterShouldReturnSameIssues() {
        final IssuesFilter issuesFilter = createIssuesFilter();
        assertThat(issuesFilter.filter())
                .hasSize(ISSUES_1.size());
    }

    @Test
    void shouldReturnEqualIssuesWhereIncludeFilterEffectivelyFiltersNothing() {
        final IssuesFilter issuesFilter = createIssuesFilter();

        issuesFilter.includeFileNames("file-[0-9]");
        assertThat(issuesFilter.filter())
                .hasSize(ISSUES_1.size());
    }

    @Test
    void shouldReturnIssuesWithOnlyThreeRemainingIssue() {
        IssuesFilter issuesFilter = createIssuesFilter();

        issuesFilter.includeFileNames("file-1");
        final Issues filtered = issuesFilter.filter();
        assertThat(filtered)
                .as("Should only contain one Issue")
                .hasSize(3);

        assertThat(filtered.get(0))
                .as("Should be ISSUE_1")
                .isSameAs(ISSUE_1);

        assertThat(filtered.get(1))
                .as("Should be ISSUE_2")
                .isSameAs(ISSUE_2);

        assertThat(filtered.get(2))
                .as("Should be ISSUE_3")
                .isSameAs(ISSUE_3);
    }

    @Test
    void shouldReturnIssuesThatMatchAllFilters() {
        final IssuesFilter issuesFilter = createIssuesFilter();

        issuesFilter.includeFileNames("file-(1|2)");
        final Issues filtered = issuesFilter.filter();

        assertThat(filtered)
                .hasSize(5);

        assertThat(filtered.get(0))
                .as("Should contain only warnings with fileNames file-1 and file-2")
                .isSameAs(ISSUE_1);

        assertThat(filtered.get(1))
                .as("Should contain only warnings with fileNames file-1 and file-2")
                .isSameAs(ISSUE_2);

        assertThat(filtered.get(2))
                .as("Should contain only warnings with fileNames file-1 and file-2")
                .isSameAs(ISSUE_3);

        assertThat(filtered.get(3))
                .as("Should contain only warnings with fileNames file-1 and file-2")
                .isSameAs(ISSUE_4);

        assertThat(filtered.get(4))
                .as("Should contain only warnings with fileNames file-1 and file-2")
                .isSameAs(ISSUE_5);
    }

    @Test
    void shouldOnlyContainIssuesWithSpecificPackageName() {
        final IssuesFilter issuesFilter = createIssuesFilter();

        issuesFilter.includePackageNames("package-(1|2)");

        final Issues filtered = issuesFilter.filter();
        assertThat(filtered)
                .hasSize(5);

        assertThat(filtered.get(0))
                .as("Should contain only warnings with packageNames package-1 and package-2")
                .isSameAs(ISSUE_1);

        assertThat(filtered.get(1))
                .as("Should contain only warnings with packageNames package-1 and package-2")
                .isSameAs(ISSUE_2);

        assertThat(filtered.get(2))
                .as("Should contain only warnings with packageNames package-1 and package-2")
                .isSameAs(ISSUE_3);

        assertThat(filtered.get(3))
                .as("Should contain only warnings with packageNames package-1 and package-2")
                .isSameAs(ISSUE_4);

        assertThat(filtered.get(4))
                .as("Should contain only warnings with packageNames package-1 and package-2")
                .isSameAs(ISSUE_5);
    }

    @Test
    void shouldOnlyContainIssuesWithSpecificModuleName() {
        final IssuesFilter issuesFilter = createIssuesFilter();

        issuesFilter.includeModuleNames("module-(1|2)");

        final Issues filtered = issuesFilter.filter();
        assertThat(filtered)
                .hasSize(4);

        assertThat(filtered.get(0))
                .as("Should contain only warnings with moduleNames module-1 and module-2")
                .isSameAs(ISSUE_1);

        assertThat(filtered.get(1))
                .as("Should contain only warnings with moduleNames module-1 and module-2")
                .isSameAs(ISSUE_2);

        assertThat(filtered.get(2))
                .as("Should contain only warnings with moduleNames module-1 and module-2")
                .isSameAs(ISSUE_3);

        assertThat(filtered.get(3))
                .as("Should contain only warnings with moduleNames module-1 and module-2")
                .isSameAs(ISSUE_6);
    }

    @Test
    void shouldOnlyContainIssuesWithSpecificCategory() {
        final IssuesFilter issuesFilter = createIssuesFilter();

        issuesFilter.includeCategories("category-(1|2)");

        final Issues filtered = issuesFilter.filter();

        assertThat(filtered)
                .hasSize(2);

        assertThat(filtered.get(0))
                .as("Should only contain warnings with category-1 and category-2")
                .isSameAs(ISSUE_1);


        assertThat(filtered.get(1))
                .as("Should only contain warnings with category-1 and category-2")
                .isSameAs(ISSUE_6);
    }

    @Test
    void shouldOnlyContainIssuesWithSpecificType() {
        final IssuesFilter issuesFilter = createIssuesFilter();

        issuesFilter.includeType("type-(1|2)");

        final Issues filtered = issuesFilter.filter();

        assertThat(filtered)
                .hasSize(2);

        assertThat(filtered.get(0))
                .as("Should only contain warnings with type-1 and type-2")
                .isSameAs(ISSUE_2);

        assertThat(filtered.get(1))
                .as("Should only contain warnings with type-1 and type-2")
                .isSameAs(ISSUE_4);
    }

    @Test
    void nonDisjointFiltersShouldStillProduceIssuesWithNoDuplicates() {
        final IssuesFilter issuesFilter = createIssuesFilter();

        issuesFilter.includeFileNames("file-1");
        issuesFilter.includeFileNames("package-1");

        final Issues filtered = issuesFilter.filter();

        assertThat(filtered)
                .hasSize(3);

        assertThat(filtered.get(0))
                .as("Should only contain warnings with fileName file-1 and packageName package-1")
                .isSameAs(ISSUE_1);

        assertThat(filtered.get(1))
                .as("Should only contain warnings with fileName file-1 and packageName package-1")
                .isSameAs(ISSUE_2);

        assertThat(filtered.get(2))
                .as("Should only contain warnings with fileName file-1 and packageName package-1")
                .isSameAs(ISSUE_3);

    }

    @Test
    void shouldOnlyContainIssuesNotWithFileName() {
        final IssuesFilter issuesFilter = createIssuesFilter();

        issuesFilter.excludeFileNames("file-1");

        final Issues filtered = issuesFilter.filter();

        assertThat(filtered)
                .hasSize(3);

        assertThat(filtered.get(0))
                .as("Filename should not be file-1")
                .isSameAs(ISSUE_4);

        assertThat(filtered.get(1))
                .as("Filename should not be file-1")
                .isSameAs(ISSUE_5);

        assertThat(filtered.get(2))
                .as("Filename should not be file-1")
                .isSameAs(ISSUE_6);
    }

    @Test
    void shouldOnlyContainIssuesNotWithPackageName() {
        final IssuesFilter issuesFilter = createIssuesFilter();

        issuesFilter.excludePackageNames("package-1");

        final Issues filtered = issuesFilter.filter();

        assertThat(filtered)
                .hasSize(3);

        assertThat(filtered.get(0))
                .as("PackageName should not be package-1")
                .isSameAs(ISSUE_4);

        assertThat(filtered.get(1))
                .as("PackageName should not be package-1")
                .isSameAs(ISSUE_5);

        assertThat(filtered.get(2))
                .as("PackageName should not be package-1")
                .isSameAs(ISSUE_6);
    }

    @Test
    void shouldOnlyContainIssuesNotWithModuleName() {
        final IssuesFilter issuesFilter = createIssuesFilter();

        issuesFilter.excludeModuleNames("module-1");

        final Issues filtered = issuesFilter.filter();

        assertThat(filtered)
                .hasSize(3);

        assertThat(filtered.get(0))
                .as("ModuleName should not be module-1")
                .isSameAs(ISSUE_4);

        assertThat(filtered.get(1))
                .as("ModuleName should not be module-1")
                .isSameAs(ISSUE_5);

        assertThat(filtered.get(2))
                .as("ModuleName should not be module-1")
                .isSameAs(ISSUE_6);
    }

    @Test
    void shouldOnlyContainIssuesNotWithCategories() {
        final IssuesFilter issuesFilter = createIssuesFilter();

        issuesFilter.excludeCategories("category-(1|2)");

        final Issues filtered = issuesFilter.filter();

        assertThat(filtered)
                .hasSize(4);

        assertThat(filtered.get(0))
                .as("Category should not be category-1 or category-2")
                .isSameAs(ISSUE_2);

        assertThat(filtered.get(1))
                .as("Category should not be category-1 or category-2")
                .isSameAs(ISSUE_3);

        assertThat(filtered.get(2))
                .as("Category should not be category-1 or category-2")
                .isSameAs(ISSUE_4);

        assertThat(filtered.get(3))
                .as("Category should not be category-1 or category-2")
                .isSameAs(ISSUE_5);
    }

    @Test
    void shouldOnlyContainIssuesNotWithTypes() {
        final IssuesFilter issuesFilter = createIssuesFilter();

        issuesFilter.excludeTypes("type-(1|2)");

        final Issues filtered = issuesFilter.filter();

        assertThat(filtered)
                .hasSize(4);

        assertThat(filtered.get(0))
                .as("Type should not be type-1 or type-2")
                .isSameAs(ISSUE_1);

        assertThat(filtered.get(1))
                .as("Type should not be type-1 or type-2")
                .isSameAs(ISSUE_3);

        assertThat(filtered.get(2))
                .as("Type should not be type-1 or type-2")
                .isSameAs(ISSUE_5);

        assertThat(filtered.get(3))
                .as("Type should not be type-1 or type-2")
                .isSameAs(ISSUE_6);
    }

    private IssuesFilter createIssuesFilter() {
        return new IssuesFilter(ISSUES_1);
    }
}
