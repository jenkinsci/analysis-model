package edu.hm.hafner.analysis;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.*;


/**
 * Unit tests for {@link IssuesFilter}.
 *
 * @author Andreas Moser
 */
class IssuesFilterTest {
    private static final Issue TEST_ISSUE_1 = new IssueBuilder().setFileName("test1/Name")
            .setPackageName("test1Package")
            .build();
    private static final Issue TEST_ISSUE_2 = new IssueBuilder().setFileName("test2/Name")
            .setPackageName("test2Package")
            .build();
    private static final Issue TEST_ISSUE_3 = new IssueBuilder().setFileName("test3/Name")
            .setPackageName("test3Package")
            .build();

    @Test
    void shouldNotExcludeIssueIfNoFilterIsSet() {
        Issues issues = new Issues();
        issues.addAll(asList(TEST_ISSUE_1, TEST_ISSUE_2, TEST_ISSUE_3));
        IssuesFilter issuesFilter = new IssuesFilter();

        Issues filteredIssues = issuesFilter.filter(issues);

        checkFilteredIssues(filteredIssues, TEST_ISSUE_1, TEST_ISSUE_2, TEST_ISSUE_3);
    }


    @Test
    void shouldExcludeIssueDependingOnFileNameWithOneFilterIsSet() {
        Issues issues = new Issues();
        issues.addAll(asList(TEST_ISSUE_1, TEST_ISSUE_2, TEST_ISSUE_3));
        IssuesFilter issuesFilter = new IssuesFilter();
        issuesFilter.setFileNameExcludes(asList("test1/.*"));

        Issues filteredIssues = issuesFilter.filter(issues);

        checkFilteredIssues(filteredIssues, TEST_ISSUE_2, TEST_ISSUE_3);
    }

    @Test
    void shouldExcludeIssuesDependingOnFileNameWithTwoFiltersAreSet() {
        Issues issues = new Issues();
        issues.addAll(asList(TEST_ISSUE_1, TEST_ISSUE_2, TEST_ISSUE_3));
        IssuesFilter issuesFilter = new IssuesFilter();
        issuesFilter.setFileNameExcludes(asList("test1/.*", "test2/.*"));

        Issues filteredIssues = issuesFilter.filter(issues);

        checkFilteredIssues(filteredIssues, TEST_ISSUE_3);
    }


    @Test
    void shouldExcludeIssueDependingOnPackageNameWithOneFilterIsSet() {
        Issues issues = new Issues();
        issues.addAll(asList(TEST_ISSUE_1, TEST_ISSUE_2, TEST_ISSUE_3));
        IssuesFilter issuesFilter = new IssuesFilter();
        issuesFilter.setPackageNameExcludes(asList("test1.*"));

        Issues filteredIssues = issuesFilter.filter(issues);

        checkFilteredIssues(filteredIssues, TEST_ISSUE_2, TEST_ISSUE_3);
    }


    @Test
    void shouldExcludeIssuesDependingOnPackageNameWithTwoFiltersAreSet() {
        Issues issues = new Issues();
        issues.addAll(asList(TEST_ISSUE_1, TEST_ISSUE_2, TEST_ISSUE_3));
        IssuesFilter issuesFilter = new IssuesFilter();
        issuesFilter.setPackageNameExcludes(asList("test1.*", "test2.*"));

        Issues filteredIssues = issuesFilter.filter(issues);

        checkFilteredIssues(filteredIssues, TEST_ISSUE_3);
    }

    @Disabled
    @Test
    void shouldExcludeIssueDependingOnModuleNameWithOneFilterIsSet() {

    }

    @Disabled
    @Test
    void shouldExcludeIssuesDependingOnModuleNameWithTwoFiltersAreSet() {

    }

    @Disabled
    @Test
    void shouldExcludeIssueDependingOnCategoryWithOneFilterIsSet() {

    }

    @Disabled
    @Test
    void shouldExcludeIssuesDependingOnCategoryWithTwoFiltersAreSet() {

    }

    @Disabled
    @Test
    void shouldExcludeIssueDependingOnTypeWithOneFilterIsSet() {

    }

    @Disabled
    @Test
    void shouldExcludeIssuesDependingOnTypeWithTwoFiltersAreSet() {

    }


    private Issue createIssueWithFileName(final String fileName) {
        IssueBuilder issueBuilder = new IssueBuilder();
        issueBuilder.setFileName(fileName);
        return issueBuilder.build();
    }

    private void checkFilteredIssues(final Issues issuesToCheck, final Issue... containedIssues) {
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(issuesToCheck).hasSize(containedIssues.length);
        softly.assertThat(issuesToCheck.all()).containsExactly(containedIssues);
        softly.assertAll();
    }


}