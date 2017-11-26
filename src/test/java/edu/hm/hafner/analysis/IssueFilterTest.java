package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;


import edu.hm.hafner.analysis.IssueFilter.IssueFilterBuilder;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * Tests the class {@link IssueFilter IssueFilter}. These Tests are generated while developing.
 *
 * @author Michael Schmid
 */
class IssueFilterTest {
    private static final List<Issue> SINGLE_FILTER_ISSUES = new ArrayList<>();
    private static final List<Issue> FILTER_CONCATENATION_ISSUES = new ArrayList<>();
    static {
        SINGLE_FILTER_ISSUES.add(new IssueBuilder()
                .setFileName("file_name")
                .setPackageName("package_name")
                .setModuleName("module_name")
                .setCategory("category")
                .setType("type").build());
        SINGLE_FILTER_ISSUES.add(new IssueBuilder()
                .setFileName("eman_elif")
                .setPackageName("eman_egakcap")
                .setModuleName("eman_eludom")
                .setCategory("yrogetac")
                .setType("epyt").build());

        FILTER_CONCATENATION_ISSUES.add(new IssueBuilder()
                .setFileName("expected file_name")
                .setPackageName("expected package_name")
                .setModuleName("expected module_name")
                .setCategory("expected category")
                .setType("expected type").build());
        FILTER_CONCATENATION_ISSUES.add(new IssueBuilder()
                .setFileName("file_name")
                .setPackageName("expected package_name")
                .setModuleName("expected module_name")
                .setCategory("expected category")
                .setType("expected type")
                .build());
        FILTER_CONCATENATION_ISSUES.add(new IssueBuilder()
                .setFileName("expected file_name")
                .setPackageName("package_name")
                .setModuleName("expected module_name")
                .setCategory("expected category")
                .setType("expected type")
                .build());
        FILTER_CONCATENATION_ISSUES.add(new IssueBuilder()
                .setFileName("expected file_name")
                .setPackageName("expected package_name")
                .setModuleName("module_name")
                .setCategory("expected category")
                .setType("expected type")
                .build());
        FILTER_CONCATENATION_ISSUES.add(new IssueBuilder()
                .setFileName("expected file_name")
                .setPackageName("expected package_name")
                .setModuleName("expected module_name")
                .setCategory("category")
                .setType("expected type")
                .build());
        FILTER_CONCATENATION_ISSUES.add(new IssueBuilder()
                .setFileName("expected file_name")
                .setPackageName("expected package_name")
                .setModuleName("expected module_name")
                .setCategory("expected category")
                .setType("type").build());
    }

    @Test
    void anyEmptyFilterShouldNotFilterAnything() {
        List<Issue> issueList = new ArrayList<>();
        issueList.addAll(SINGLE_FILTER_ISSUES);
        IssueFilterBuilder builder = new IssueFilterBuilder();
        testFilter(builder, issueList, issueList);
    }

    @Test
    void includeTypeFilterShouldReturnOnlyTheSpecifiedTypes() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("type");

        List<Issue> expected = new ArrayList<>();
        expected.add(SINGLE_FILTER_ISSUES.get(0));

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.addIncludeTypeFilter(patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, expected);
    }

    @Test
    void includeTypeFilterShouldReturnAllInputIfSpecified() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("type");
        patterns.add("epyt");

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.addIncludeTypeFilter(patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, SINGLE_FILTER_ISSUES);
    }

    @Test
    void excludeTypeFilterShouldReturnAnythingExceptTheSpecifiedTypes() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("type");

        List<Issue> expected = new ArrayList<>();
        expected.add(SINGLE_FILTER_ISSUES.get(1));

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.addExcludeTypeFilter(patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, expected);
    }

    @Test
    void excludeTypeFilterShouldReturnEmptyListIfAllSpecified() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("type");
        patterns.add("epyt");

        List<Issue> expected = new ArrayList<>();

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.addExcludeTypeFilter(patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, expected);
    }

    @Test
    void includeCategoryFilterShouldReturnOnlyTheSpecifiedTypes() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("category");

        List<Issue> expected = new ArrayList<>();
        expected.add(SINGLE_FILTER_ISSUES.get(0));

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.addIncludeCategoryFilter(patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, expected);
    }

    @Test
    void includeCategoryFilterShouldReturnAllInputIfSpecified() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("category");
        patterns.add("yrogetac");

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.addIncludeCategoryFilter(patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, SINGLE_FILTER_ISSUES);
    }

    @Test
    void excludeCategoryFilterShouldReturnAnythingExceptTheSpecifiedTypes() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("category");

        List<Issue> expected = new ArrayList<>();
        expected.add(SINGLE_FILTER_ISSUES.get(1));

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.addExcludeCategoryFilter(patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, expected);
    }

    @Test
    void excludeCategoryFilterShouldReturnEmptyListIfAllSpecified() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("category");
        patterns.add("yrogetac");

        List<Issue> expected = new ArrayList<>();

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.addExcludeCategoryFilter(patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, expected);
    }

    @Test
    void includeFileNameFilterShouldReturnOnlyTheSpecifiedTypes() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("file_name");

        List<Issue> expected = new ArrayList<>();
        expected.add(SINGLE_FILTER_ISSUES.get(0));

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.addIncludeFileNameFilter(patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, expected);
    }

    @Test
    void includeFileNameFilterShouldReturnAllInputIfSpecified() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("file_name");
        patterns.add("eman_elif");

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.addIncludeFileNameFilter(patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, SINGLE_FILTER_ISSUES);
    }

    @Test
    void excludeFileNameFilterShouldReturnAnythingExceptTheSpecifiedTypes() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("file_name");

        List<Issue> expected = new ArrayList<>();
        expected.add(SINGLE_FILTER_ISSUES.get(1));

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.addExcludeFileNameFilter(patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, expected);
    }

    @Test
    void excludeFileNameFilterShouldReturnEmptyListIfAllSpecified() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("file_name");
        patterns.add("eman_elif");

        List<Issue> expected = new ArrayList<>();

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.addExcludeFileNameFilter(patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, expected);
    }

    @Test
    void includePackageNameFilterShouldReturnOnlyTheSpecifiedTypes() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("package_name");

        List<Issue> expected = new ArrayList<>();
        expected.add(SINGLE_FILTER_ISSUES.get(0));

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.addIncludePackageNameFilter(patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, expected);
    }

    @Test
    void includePackageNameFilterShouldReturnAllInputIfSpecified() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("package_name");
        patterns.add("eman_egakcap");

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.addIncludePackageNameFilter(patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, SINGLE_FILTER_ISSUES);
    }

    @Test
    void excludePackageNameFilterShouldReturnAnythingExceptTheSpecifiedTypes() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("package_name");

        List<Issue> expected = new ArrayList<>();
        expected.add(SINGLE_FILTER_ISSUES.get(1));

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.addExcludePackageNameFilter(patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, expected);
    }

    @Test
    void excludePackageNameFilterShouldReturnEmptyListIfAllSpecified() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("package_name");
        patterns.add("eman_egakcap");

        List<Issue> expected = new ArrayList<>();

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.addExcludePackageNameFilter(patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, expected);
    }

    @Test
    void includeModuleNameFilterShouldReturnOnlyTheSpecifiedTypes() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("module_name");

        List<Issue> expected = new ArrayList<>();
        expected.add(SINGLE_FILTER_ISSUES.get(0));

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.addIncludeModuleNameFilter(patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, expected);
    }

    @Test
    void includeModuleNameFilterShouldReturnAllInputIfSpecified() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("module_name");
        patterns.add("eman_eludom");

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.addIncludeModuleNameFilter(patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, SINGLE_FILTER_ISSUES);
    }

    @Test
    void excludeModuleNameFilterShouldReturnAnythingExceptTheSpecifiedTypes() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("module_name");

        List<Issue> expected = new ArrayList<>();
        expected.add(SINGLE_FILTER_ISSUES.get(1));

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.addExcludeModuleNameFilter(patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, expected);
    }

    @Test
    void excludeModuleNameFilterShouldReturnEmptyListIfAllSpecified() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("module_name");
        patterns.add("eman_eludom");

        List<Issue> expected = new ArrayList<>();

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.addExcludeModuleNameFilter(patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, expected);
    }

    @Test
    void concatenationOfFiltersDeliversTheIssuesWhichMatchesAllFilterCriteriaIncludeFilter() {
        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder.addIncludeFileNameFilter(Arrays.asList("expected file_name"))
                .addIncludePackageNameFilter(Arrays.asList("expected package_name"))
                .addIncludeModuleNameFilter(Arrays.asList("expected module_name"))
                .addIncludeCategoryFilter(Arrays.asList("expected category"))
                .addIncludeTypeFilter(Arrays.asList("expected type"));

        testFilter(builder, FILTER_CONCATENATION_ISSUES, Arrays.asList(FILTER_CONCATENATION_ISSUES.get(0)));
    }

    @Test
    void concatenationOfFiltersDeliversTheIssuesWhichMatchesAllFilterCriteriaExcludeFilter() {
        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder.addExcludeFileNameFilter(Arrays.asList("file_name"))
                .addExcludePackageNameFilter(Arrays.asList("package_name"))
                .addExcludeModuleNameFilter(Arrays.asList("module_name"))
                .addExcludeCategoryFilter(Arrays.asList("category"))
                .addExcludeTypeFilter(Arrays.asList("type"));

        testFilter(builder, FILTER_CONCATENATION_ISSUES, Arrays.asList(FILTER_CONCATENATION_ISSUES.get(0)));
    }

    @Test
    void concatenationOfFiltersDeliversTheIssuesWhichMatchesAllFilterCriteriaIncludeAndExcludeFilters() {
        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder.addIncludeFileNameFilter(Arrays.asList("expected file_name"))
                .addExcludePackageNameFilter(Arrays.asList("package_name"))
                .addIncludeModuleNameFilter(Arrays.asList("expected module_name"))
                .addExcludeCategoryFilter(Arrays.asList("category"))
                .addIncludeTypeFilter(Arrays.asList("expected type"));

        testFilter(builder, FILTER_CONCATENATION_ISSUES, Arrays.asList(FILTER_CONCATENATION_ISSUES.get(0)));
    }

    /**
     * Test if a defined IssueFilterBuilder delivers the expected result if applied to a defined input.
     * @param builder which still contains all filter criteria
     * @param input list of issues which should be filtered.
     * @param expected list of issues which match all filter criteria
     */
    private void testFilter(IssueFilterBuilder builder, List<Issue> input, List<Issue> expected) {
        Issues issues = new Issues();
        issues.addAll(input);
        IssueFilter sut = builder.build();
        Issues result = sut.filter(issues);
        Issue[] expectedArray = new Issue[expected.size()];
        assertThat(result.iterator()).containsExactly(expected.toArray(expectedArray));
    }
}
