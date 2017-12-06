package edu.hm.hafner.analysis;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;


import static java.util.Arrays.*;


/**
 * Unit tests for {@link IssuesFilter}.
 *
 * @author Andreas Moser
 */
public class IssuesFilterTest {
    private static final Issue TEST_ISSUE_1 = new IssueBuilder()
            .setFileName("test1/Name")
            .setPackageName("test1Package")
            .setModuleName("test1Module")
            .setCategory("test1Category")
            .setType("test1Type")
            .build();
    private static final Issue TEST_ISSUE_2 = new IssueBuilder()
            .setFileName("test2/Name")
            .setPackageName("test2Package")
            .setModuleName("test2Module")
            .setCategory("test2Category")
            .setType("test2Type")
            .build();
    private static final Issue TEST_ISSUE_3 = new IssueBuilder()
            .setFileName("test3/Name")
            .setPackageName("test3Package")
            .setModuleName("test3Module")
            .setCategory("test3Category")
            .setType("test3Type")
            .build();
    private static final Issue TEST_ISSUE_4 = new IssueBuilder()
            .setFileName("test4/Name")
            .setPackageName("test4Package")
            .setModuleName("test4Module")
            .setCategory("test4Category")
            .setType("test4Type")
            .build();
    private static final Issue TEST_ISSUE_5 = new IssueBuilder()
            .setFileName("test5/Name")
            .setPackageName("test5Package")
            .setModuleName("test5Module")
            .setCategory("test5Category")
            .setType("test5Type")
            .build();
    private static final Issue TEST_ISSUE_6 = new IssueBuilder()
            .setFileName("test6/Name")
            .setPackageName("test6Package")
            .setModuleName("test6Module")
            .setCategory("test6Category")
            .setType("test6Type")
            .build();

    /** Verifies that no issue is excluded if no filter is set. */
    @Test
    public void shouldNotExcludeIssueIfNoFilterIsSet() {
        Issues issues = createIssuesObject();
        IssuesFilter issuesFilter = new IssuesFilter();

        Issues filteredIssues = issuesFilter.filter(issues);

        checkFilteredIssues(filteredIssues, TEST_ISSUE_1, TEST_ISSUE_2, TEST_ISSUE_3, TEST_ISSUE_4, TEST_ISSUE_5, TEST_ISSUE_6);
    }

    /** Verifies that a issue is excluded if exclude filter on file name is set. */
    @Test
    public void shouldExcludeIssueDependingOnFileNameWithOneFilterIsSet() {
        Issues issues = createIssuesObject();
        IssuesFilter issuesFilter = new IssuesFilter();
        issuesFilter.setFileNameExcludes(asList("test1/.*"));

        Issues filteredIssues = issuesFilter.filter(issues);

        checkFilteredIssues(filteredIssues, TEST_ISSUE_2, TEST_ISSUE_3, TEST_ISSUE_4, TEST_ISSUE_5, TEST_ISSUE_6);
    }

    /** Verifies that file name exclude filters are associated with OR. */
    @Test
    public void shouldExcludeIssuesDependingOnFileNameWithTwoFiltersAreSet() {
        Issues issues = createIssuesObject();
        IssuesFilter issuesFilter = new IssuesFilter();
        issuesFilter.setFileNameExcludes(asList("test1/.*", "test2/.*"));

        Issues filteredIssues = issuesFilter.filter(issues);

        checkFilteredIssues(filteredIssues, TEST_ISSUE_3, TEST_ISSUE_4, TEST_ISSUE_5, TEST_ISSUE_6);
    }

    /** Verifies that a issue is excluded if exclude filter on package name is set. */
    @Test
    public void shouldExcludeIssueDependingOnPackageNameWithOneFilterIsSet() {
        Issues issues = createIssuesObject();
        IssuesFilter issuesFilter = new IssuesFilter();
        issuesFilter.setPackageNameExcludes(asList("test1.*"));

        Issues filteredIssues = issuesFilter.filter(issues);

        checkFilteredIssues(filteredIssues, TEST_ISSUE_2, TEST_ISSUE_3, TEST_ISSUE_4, TEST_ISSUE_5, TEST_ISSUE_6);
    }

    /** Verifies that package name exclude filters are associated with OR. */
    @Test
    public void shouldExcludeIssuesDependingOnPackageNameWithTwoFiltersAreSet() {
        Issues issues = createIssuesObject();
        IssuesFilter issuesFilter = new IssuesFilter();
        issuesFilter.setPackageNameExcludes(asList("test1.*", "test2.*"));

        Issues filteredIssues = issuesFilter.filter(issues);

        checkFilteredIssues(filteredIssues, TEST_ISSUE_3, TEST_ISSUE_4, TEST_ISSUE_5, TEST_ISSUE_6);
    }

    /** Verifies that a issue is excluded if exclude filter on module name is set. */
    @Test
    public void shouldExcludeIssueDependingOnModuleNameWithOneFilterIsSet() {
        Issues issues = createIssuesObject();
        IssuesFilter issuesFilter = new IssuesFilter();
        issuesFilter.setModuleNameExcludes(asList("test1.*"));

        Issues filteredIssues = issuesFilter.filter(issues);

        checkFilteredIssues(filteredIssues, TEST_ISSUE_2, TEST_ISSUE_3, TEST_ISSUE_4, TEST_ISSUE_5, TEST_ISSUE_6);
    }

    /** Verifies that module name exclude filters are associated with OR. */
    @Test
    public void shouldExcludeIssuesDependingOnModuleNameWithTwoFiltersAreSet() {
        Issues issues = createIssuesObject();
        IssuesFilter issuesFilter = new IssuesFilter();
        issuesFilter.setModuleNameExcludes(asList("test1.*", "test2.*"));

        Issues filteredIssues = issuesFilter.filter(issues);

        checkFilteredIssues(filteredIssues, TEST_ISSUE_3, TEST_ISSUE_4, TEST_ISSUE_5, TEST_ISSUE_6);
    }

    /** Verifies that a issue is excluded if exclude filter on category is set. */
    @Test
    public void shouldExcludeIssueDependingOnCategoryWithOneFilterIsSet() {
        Issues issues = createIssuesObject();
        IssuesFilter issuesFilter = new IssuesFilter();
        issuesFilter.setCategoryExcludes(asList("test1.*"));

        Issues filteredIssues = issuesFilter.filter(issues);

        checkFilteredIssues(filteredIssues, TEST_ISSUE_2, TEST_ISSUE_3, TEST_ISSUE_4, TEST_ISSUE_5, TEST_ISSUE_6);
    }

    /** Verifies that category exclude filters are associated with OR. */
    @Test
    public void shouldExcludeIssuesDependingOnCategoryWithTwoFiltersAreSet() {
        Issues issues = createIssuesObject();
        IssuesFilter issuesFilter = new IssuesFilter();
        issuesFilter.setCategoryExcludes(asList("test1.*", "test2.*"));

        Issues filteredIssues = issuesFilter.filter(issues);

        checkFilteredIssues(filteredIssues, TEST_ISSUE_3, TEST_ISSUE_4, TEST_ISSUE_5, TEST_ISSUE_6);
    }

    /** Verifies that a issue is excluded if exclude filter on type is set. */
    @Test
    public void shouldExcludeIssueDependingOnTypeWithOneFilterIsSet() {
        Issues issues = createIssuesObject();
        IssuesFilter issuesFilter = new IssuesFilter();
        issuesFilter.setTypeExcludes(asList("test1.*"));

        Issues filteredIssues = issuesFilter.filter(issues);

        checkFilteredIssues(filteredIssues, TEST_ISSUE_2, TEST_ISSUE_3, TEST_ISSUE_4, TEST_ISSUE_5, TEST_ISSUE_6);
    }

    /** Verifies that type exclude filters are associated with OR. */
    @Test
    public void shouldExcludeIssuesDependingOnTypeWithTwoFiltersAreSet() {
        Issues issues = createIssuesObject();
        IssuesFilter issuesFilter = new IssuesFilter();
        issuesFilter.setTypeExcludes(asList("test1.*", "test2.*"));

        Issues filteredIssues = issuesFilter.filter(issues);

        checkFilteredIssues(filteredIssues, TEST_ISSUE_3, TEST_ISSUE_4, TEST_ISSUE_5, TEST_ISSUE_6);
    }

    /** Verifies that a issue is included if include filter on file name is set. */
    @Test
    public void shouldIncludeIssueDependingOnFileNameWithOneFilterIsSet() {
        Issues issues = createIssuesObject();
        IssuesFilter issuesFilter = new IssuesFilter();
        issuesFilter.setFileNameIncludes(asList("test1/.*"));

        Issues filteredIssues = issuesFilter.filter(issues);

        checkFilteredIssues(filteredIssues, TEST_ISSUE_1);
    }

    /** Verifies that file name include filters are associated with OR. */
    @Test
    public void shouldIncludeIssuesDependingOnFileNameWithTwoFiltersAreSet() {
        Issues issues = createIssuesObject();
        IssuesFilter issuesFilter = new IssuesFilter();
        issuesFilter.setFileNameIncludes(asList("test1/.*", "test2/.*"));

        Issues filteredIssues = issuesFilter.filter(issues);

        checkFilteredIssues(filteredIssues, TEST_ISSUE_1, TEST_ISSUE_2);
    }

    /** Verifies that a issue is included if include filter on package name is set. */
    @Test
    public void shouldIncludeIssueDependingOnPackageNameWithOneFilterIsSet() {
        Issues issues = createIssuesObject();
        IssuesFilter issuesFilter = new IssuesFilter();
        issuesFilter.setPackageNameIncludes(asList("test1.*"));

        Issues filteredIssues = issuesFilter.filter(issues);

        checkFilteredIssues(filteredIssues, TEST_ISSUE_1);
    }

    /** Verifies that package name include filters are associated with OR. */
    @Test
    public void shouldIncludeIssuesDependingOnPackageNameWithTwoFiltersAreSet() {
        Issues issues = createIssuesObject();
        IssuesFilter issuesFilter = new IssuesFilter();
        issuesFilter.setPackageNameIncludes(asList("test1.*", "test2.*"));

        Issues filteredIssues = issuesFilter.filter(issues);

        checkFilteredIssues(filteredIssues, TEST_ISSUE_1, TEST_ISSUE_2);
    }

    /** Verifies that a issue is included if include filter on module name is set. */
    @Test
    public void shouldIncludeIssueDependingOnModuleNameWithOneFilterIsSet() {
        Issues issues = createIssuesObject();
        IssuesFilter issuesFilter = new IssuesFilter();
        issuesFilter.setModuleNameIncludes(asList("test1.*"));

        Issues filteredIssues = issuesFilter.filter(issues);

        checkFilteredIssues(filteredIssues, TEST_ISSUE_1);
    }

    /** Verifies that module name include filters are associated with OR. */
    @Test
    public void shouldIncludeIssuesDependingOnModuleNameWithTwoFiltersAreSet() {
        Issues issues = createIssuesObject();
        IssuesFilter issuesFilter = new IssuesFilter();
        issuesFilter.setModuleNameIncludes(asList("test1.*", "test2.*"));

        Issues filteredIssues = issuesFilter.filter(issues);

        checkFilteredIssues(filteredIssues, TEST_ISSUE_1, TEST_ISSUE_2);
    }

    /** Verifies that a issue is included if include filter on category is set. */
    @Test
    public void shouldIncludeIssueDependingOnCategoryWithOneFilterIsSet() {
        Issues issues = createIssuesObject();
        IssuesFilter issuesFilter = new IssuesFilter();
        issuesFilter.setCategoryIncludes(asList("test1.*"));

        Issues filteredIssues = issuesFilter.filter(issues);

        checkFilteredIssues(filteredIssues, TEST_ISSUE_1);
    }

    /** Verifies that category include filters are associated with OR. */
    @Test
    public void shouldIncludeIssuesDependingOnCategoryWithTwoFiltersAreSet() {
        Issues issues = createIssuesObject();
        IssuesFilter issuesFilter = new IssuesFilter();
        issuesFilter.setCategoryIncludes(asList("test1.*", "test2.*"));

        Issues filteredIssues = issuesFilter.filter(issues);

        checkFilteredIssues(filteredIssues, TEST_ISSUE_1, TEST_ISSUE_2);
    }

    /** Verifies that a issue is included if include filter on type is set. */
    @Test
    public void shouldIncludeIssueDependingOnTypeWithOneFilterIsSet() {
        Issues issues = createIssuesObject();
        IssuesFilter issuesFilter = new IssuesFilter();
        issuesFilter.setTypeIncludes(asList("test1.*"));

        Issues filteredIssues = issuesFilter.filter(issues);

        checkFilteredIssues(filteredIssues, TEST_ISSUE_1);

    }

    /** Verifies that type include filters are associated with OR. */
    @Test
    public void shouldIncludeIssuesDependingOnTypeWithTwoFiltersAreSet() {
        Issues issues = createIssuesObject();
        IssuesFilter issuesFilter = new IssuesFilter();
        issuesFilter.setTypeIncludes(asList("test1.*", "test2.*"));

        Issues filteredIssues = issuesFilter.filter(issues);

        checkFilteredIssues(filteredIssues, TEST_ISSUE_1, TEST_ISSUE_2);
    }

    /** Verifies that issues can be included using all available criteria at once. */
    @Test
    public void shouldIncludeIssuesDependingOnAllAvailableFilterCriteria() {
        Issues issues = createIssuesObject();
        IssuesFilter issuesFilter = new IssuesFilter()
                .setFileNameIncludes(asList("test1.*"))
                .setPackageNameIncludes(asList("test2.*"))
                .setModuleNameIncludes(asList("test3.*"))
                .setCategoryIncludes(asList("test4.*"))
                .setTypeIncludes(asList("test5.*"));

        Issues filteredIssues = issuesFilter.filter(issues);

        checkFilteredIssues(filteredIssues, TEST_ISSUE_1, TEST_ISSUE_2, TEST_ISSUE_3, TEST_ISSUE_4, TEST_ISSUE_5);
    }

    /** Verifies that issues can be excluded using all available criteria at once. */
    @Test
    public void shouldExcludeIssuesDependingOnAllAvailableFilterCriteria() {
        Issues issues = createIssuesObject();
        IssuesFilter issuesFilter = new IssuesFilter()
                .setFileNameExcludes(asList("test2.*"))
                .setPackageNameExcludes(asList("test3.*"))
                .setModuleNameExcludes(asList("test4.*"))
                .setCategoryExcludes(asList("test5.*"))
                .setTypeExcludes(asList("test6.*"));

        Issues filteredIssues = issuesFilter.filter(issues);

        checkFilteredIssues(filteredIssues, TEST_ISSUE_1);
    }

    /** Verifies that included issue can be excluded by another filter. */
    @Test
    public void shouldExcludeIssueFromIncludedIssues() {
        Issues issues = createIssuesObject();
        IssuesFilter issuesFilter = new IssuesFilter()
                .setTypeExcludes(asList("test1.*"))
                .setModuleNameIncludes(asList("test1.*"))
                .setTypeIncludes(asList("test2.*"))
                .setPackageNameIncludes(asList("test3.*"));

        Issues filteredIssues = issuesFilter.filter(issues);

        checkFilteredIssues(filteredIssues, TEST_ISSUE_2, TEST_ISSUE_3);
    }

    private void checkFilteredIssues(final Issues issuesToCheck, final Issue... containedIssues) {
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(issuesToCheck).hasSize(containedIssues.length);
        softly.assertThat(issuesToCheck.all()).containsExactly(containedIssues);
        softly.assertAll();
    }

    private Issues createIssuesObject() {
        Issues issues = new Issues();
        issues.addAll(asList(TEST_ISSUE_1, TEST_ISSUE_2, TEST_ISSUE_3, TEST_ISSUE_4, TEST_ISSUE_5, TEST_ISSUE_6));

        return issues;
    }
}