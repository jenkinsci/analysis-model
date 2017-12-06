package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.IssueFilter.FilterCategory;
import edu.hm.hafner.analysis.assertj.IssuesAssert;
import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Tests {@link IssueFilter}
 */
class IssueFilterTest {

    private static final Issue TEST_ISSUE_1 = new IssueBuilder()
            .setFileName("TestFile_1")
            .setCategory("TestCategory_1")
            .setType("TestType_1")
            .setModuleName("TestModule_1")
            .setPackageName("TestPackage_1")
            .build();

    private static final Issue TEST_ISSUE_2 = new IssueBuilder()
            .setFileName("TestFile_2")
            .setCategory("TestCategory_2")
            .setType("TestType_2")
            .setModuleName("TestModule_2")
            .setPackageName("TestPackage_2")
            .build();

    private static final Issue TEST_ISSUE_3 = new IssueBuilder()
            .setFileName("TestFile_3")
            .setCategory("TestCategory_3")
            .setType("TestType_3")
            .setModuleName("TestModule_3")
            .setPackageName("TestPackage_3")
            .build();

    private Issues buildTestIssues() {
        return new Issues(asList(TEST_ISSUE_1, TEST_ISSUE_2, TEST_ISSUE_3));
    }

    /**
     * Tests that an empty list of Issues and no given filters result in an empty list of Issues.
     */
    @Test
    void noIssuesPassedIfNoIssuesAndNoFilters() {
        IssueFilter filter = new IssueFilter();
        Issues issues = new Issues();

        IssuesAssert.assertThat(filter.filter(issues)).hasSize(0);
    }

    /**
     * Tests that a given list of Issues is untouched by IssueFilter if no filters are defined.
     */
    @Test
    void allIssuesPassedIfNoFilters() {
        IssueFilter filter = new IssueFilter();
        Issues issues = buildTestIssues();

        Issues filteredOutput = filter.filter(issues);

        assertThat(filteredOutput.all()).containsExactly(TEST_ISSUE_1, TEST_ISSUE_2, TEST_ISSUE_3);

    }

    /**
     * Tests correct behaviour of include-filter that only one Issue can pass.
     */
    @Test
    void onlyIncludedPassSingleRegex() {
        IssueFilter filter = new IssueFilter().include(FilterCategory.FILE_NAME, asList(".1"));
        Issues issues = buildTestIssues();

        Issues filteredOutput = filter.filter(issues);

        assertThat(filteredOutput.all()).containsExactly(TEST_ISSUE_1);
    }

    /**
     * Tests correct behaviour of exclude-filter that only one Issue alarms.
     */
    @Test
    void onlyNonExcludedPassSingleRegex() {
        IssueFilter filter = new IssueFilter().exclude(FilterCategory.FILE_NAME, asList(".1"));
        Issues issues = buildTestIssues();

        Issues filteredOutput = filter.filter(issues);
        assertThat(filteredOutput.all()).containsExactly(TEST_ISSUE_2, TEST_ISSUE_3);
    }

    /**
     * Tests whether an include-filter which is impossible to pass results in an empty list of Issues.
     */
    @Test
    void nonePassImpossibleIncludeRegex() {
        IssueFilter filter = new IssueFilter().include(FilterCategory.FILE_NAME, asList("42"));
        Issues issues = buildTestIssues();

        Issues filteredOutput = filter.filter(issues);

        IssuesAssert.assertThat(filteredOutput).hasSize(0);
    }

    /**
     * Tests whether an exclude-filter which is impossible to alarm excludes no Issues.
     */
    @Test
    void allPassImpossibleExcludeRegex() {
        IssueFilter filter = new IssueFilter().exclude(FilterCategory.FILE_NAME, asList("42"));
        Issues issues = buildTestIssues();

        Issues filteredOutput = filter.filter(issues);

        assertThat(filteredOutput.all()).containsExactly(TEST_ISSUE_1, TEST_ISSUE_2, TEST_ISSUE_3);
    }

    /**
     * Tests whether the OR of include-filters works. If there is one include-filter which all issues pass and one
     * include-filter which is impossible to pass, all issues should be included.
     */
    @Test
    void allPassWithOnlyOneImpossibleIncludeRegex() {
        IssueFilter filter = new IssueFilter().include(FilterCategory.FILE_NAME, asList("Test", "42"));
        Issues issues = buildTestIssues();

        Issues filteredOutput = filter.filter(issues);

        assertThat(filteredOutput.all()).containsExactly(TEST_ISSUE_1, TEST_ISSUE_2, TEST_ISSUE_3);
    }

    /**
     * Tests a combination of include- and exclude-filters with different issue-attributes.
     */
    @Test
    void testMultipleExcludeAndIncludeRegex() {
        IssueFilter filter = new IssueFilter()
                .include(FilterCategory.FILE_NAME, asList("i", "l", "le", "2", "_3"))
                .include(FilterCategory.CATEGORY, asList("_1", "Cat"))
                .include(FilterCategory.TYPE, asList("TestType_2"))
                .exclude(FilterCategory.MODULE_NAME, asList("3"))
                .exclude(FilterCategory.PACKAGE_NAME, asList("age.3"));
        Issues issues = buildTestIssues();

        Issues filteredOutput = filter.filter(issues);

        assertThat(filteredOutput.all()).containsExactly(TEST_ISSUE_1, TEST_ISSUE_2);
    }

    /**
     * Tests the include- and exclude-filter for issue attribute "packageName".
     */
    @Test
    void testIncludeAndExcludeOfPackageName() {
        IssueFilter filter = new IssueFilter()
                .exclude(FilterCategory.PACKAGE_NAME, asList(".2"))
                .include(FilterCategory.PACKAGE_NAME, asList(".1", ".2"));
        Issues issues = buildTestIssues();

        Issues filteredOutput = filter.filter(issues);

        assertThat(filteredOutput.all()).containsExactly(TEST_ISSUE_1);
    }

    /**
     * Tests the include- and exclude-filter for issue attribute "moduleName".
     */
    @Test
    void testIncludeAndExcludeOfModuleName() {
        IssueFilter filter = new IssueFilter()
                .exclude(FilterCategory.MODULE_NAME, asList(".2"))
                .include(FilterCategory.MODULE_NAME, asList(".3", ".2"));
        Issues issues = buildTestIssues();

        Issues filteredOutput = filter.filter(issues);

        assertThat(filteredOutput.all()).containsExactly(TEST_ISSUE_3);
    }

    /**
     * Tests the include- and exclude-filter for issue attribute "category".
     */
    @Test
    void testIncludeAndExcludeOfCategory() {
        IssueFilter filter = new IssueFilter()
                .exclude(FilterCategory.CATEGORY, asList(".1"))
                .include(FilterCategory.CATEGORY, asList(".3", ".1"));
        Issues issues = buildTestIssues();

        Issues filteredOutput = filter.filter(issues);

        assertThat(filteredOutput.all()).containsExactly(TEST_ISSUE_3);
    }

    /**
     * Tests the include- and exclude-filter for issue attribute "type".
     */
    @Test
    void testIncludeAndExcludeOfType() {
        IssueFilter filter = new IssueFilter()
                .exclude(FilterCategory.TYPE, asList(".2"))
                .include(FilterCategory.TYPE, asList(".3", ".2"));
        Issues issues = buildTestIssues();

        Issues filteredOutput = filter.filter(issues);

        assertThat(filteredOutput.all()).containsExactly(TEST_ISSUE_3);
    }
}