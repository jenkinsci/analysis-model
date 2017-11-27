package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

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
        builder = builder.setIncludeTypeFilter(patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, expected);
    }

    @Test
    void includeTypeFilterShouldReturnAllInputIfSpecified() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("type");
        patterns.add("epyt");

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.setIncludeTypeFilter(patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, SINGLE_FILTER_ISSUES);
    }

    @Test
    void excludeTypeFilterShouldReturnAnythingExceptTheSpecifiedTypes() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("type");

        List<Issue> expected = new ArrayList<>();
        expected.add(SINGLE_FILTER_ISSUES.get(1));

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.setExcludeTypeFilter(patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, expected);
    }

    @Test
    void excludeTypeFilterShouldReturnEmptyListIfAllSpecified() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("type");
        patterns.add("epyt");

        List<Issue> expected = new ArrayList<>();

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.setExcludeTypeFilter(patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, expected);
    }

    @Test
    void includeTypeFilterShouldReturnResultOfLastFilterSet() {
        filterShouldReturnResultOfLastFilterSetTest((builder, patterns) -> builder.setIncludeTypeFilter(patterns), issue -> issue.getType(), true);
    }

    @Test
    void excludeTypeFilterShouldReturnResultOfLastFilterSet() {
        filterShouldReturnResultOfLastFilterSetTest((builder, patterns) -> builder.setExcludeTypeFilter(patterns), issue -> issue.getType(), false);
    }

    @Test
    void includeCategoryFilterShouldReturnOnlyTheSpecifiedTypes() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("category");

        List<Issue> expected = new ArrayList<>();
        expected.add(SINGLE_FILTER_ISSUES.get(0));

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.setIncludeCategoryFilter(patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, expected);
    }

    @Test
    void includeCategoryFilterShouldReturnAllInputIfSpecified() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("category");
        patterns.add("yrogetac");

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.setIncludeCategoryFilter(patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, SINGLE_FILTER_ISSUES);
    }

    @Test
    void excludeCategoryFilterShouldReturnAnythingExceptTheSpecifiedTypes() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("category");

        List<Issue> expected = new ArrayList<>();
        expected.add(SINGLE_FILTER_ISSUES.get(1));

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.setExcludeCategoryFilter(patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, expected);
    }

    @Test
    void excludeCategoryFilterShouldReturnEmptyListIfAllSpecified() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("category");
        patterns.add("yrogetac");

        List<Issue> expected = new ArrayList<>();

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.setExcludeCategoryFilter(patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, expected);
    }

    @Test
    void includeCategoryFilterShouldReturnResultOfLastFilterSet() {
        filterShouldReturnResultOfLastFilterSetTest((builder, patterns) -> builder.setIncludeCategoryFilter(patterns), issue -> issue.getCategory(), true);
    }

    @Test
    void excludeCategoryFilterShouldReturnResultOfLastFilterSet() {
        filterShouldReturnResultOfLastFilterSetTest((builder, patterns) -> builder.setExcludeCategoryFilter(patterns), issue -> issue.getCategory(), false);
    }

    @Test
    void includeFileNameFilterShouldReturnOnlyTheSpecifiedTypes() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("file_name");

        List<Issue> expected = new ArrayList<>();
        expected.add(SINGLE_FILTER_ISSUES.get(0));

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.setIncludeFileNameFilter(patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, expected);
    }

    @Test
    void includeFileNameFilterShouldReturnAllInputIfSpecified() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("file_name");
        patterns.add("eman_elif");

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.setIncludeFileNameFilter(patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, SINGLE_FILTER_ISSUES);
    }

    @Test
    void excludeFileNameFilterShouldReturnAnythingExceptTheSpecifiedTypes() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("file_name");

        List<Issue> expected = new ArrayList<>();
        expected.add(SINGLE_FILTER_ISSUES.get(1));

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.setExcludeFileNameFilter(patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, expected);
    }

    @Test
    void excludeFileNameFilterShouldReturnEmptyListIfAllSpecified() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("file_name");
        patterns.add("eman_elif");

        List<Issue> expected = new ArrayList<>();

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.setExcludeFileNameFilter(patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, expected);
    }

    @Test
    void includeFileNameFilterShouldReturnResultOfLastFilterSet() {
        filterShouldReturnResultOfLastFilterSetTest((builder, patterns) -> builder.setIncludeFileNameFilter(patterns), issue -> issue.getFileName(), true);
    }

    @Test
    void excludeFileNameFilterShouldReturnResultOfLastFilterSet() {
        filterShouldReturnResultOfLastFilterSetTest((builder, patterns) -> builder.setExcludeFileNameFilter(patterns), issue -> issue.getFileName(), false);
    }

    @Test
    void includePackageNameFilterShouldReturnOnlyTheSpecifiedTypes() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("package_name");

        List<Issue> expected = new ArrayList<>();
        expected.add(SINGLE_FILTER_ISSUES.get(0));

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.setIncludePackageNameFilter(patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, expected);
    }

    @Test
    void includePackageNameFilterShouldReturnAllInputIfSpecified() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("package_name");
        patterns.add("eman_egakcap");

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.setIncludePackageNameFilter(patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, SINGLE_FILTER_ISSUES);
    }

    @Test
    void excludePackageNameFilterShouldReturnAnythingExceptTheSpecifiedTypes() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("package_name");

        List<Issue> expected = new ArrayList<>();
        expected.add(SINGLE_FILTER_ISSUES.get(1));

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.setExcludePackageNameFilter(patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, expected);
    }

    @Test
    void excludePackageNameFilterShouldReturnEmptyListIfAllSpecified() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("package_name");
        patterns.add("eman_egakcap");

        List<Issue> expected = new ArrayList<>();

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.setExcludePackageNameFilter(patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, expected);
    }

    @Test
    void includePackageNameFilterShouldReturnResultOfLastFilterSet() {
        filterShouldReturnResultOfLastFilterSetTest((builder, patterns) -> builder.setIncludePackageNameFilter(patterns), issue -> issue.getPackageName(), true);
    }

    @Test
    void excludePackageNameFilterShouldReturnResultOfLastFilterSet() {
        filterShouldReturnResultOfLastFilterSetTest((builder, patterns) -> builder.setExcludePackageNameFilter(patterns), issue -> issue.getPackageName(), false);
    }

    @Test
    void includeModuleNameFilterShouldReturnOnlyTheSpecifiedTypes() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("module_name");

        List<Issue> expected = new ArrayList<>();
        expected.add(SINGLE_FILTER_ISSUES.get(0));

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.setIncludeModuleNameFilter(patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, expected);
    }

    @Test
    void includeModuleNameFilterShouldReturnAllInputIfSpecified() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("module_name");
        patterns.add("eman_eludom");

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.setIncludeModuleNameFilter(patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, SINGLE_FILTER_ISSUES);
    }

    @Test
    void excludeModuleNameFilterShouldReturnAnythingExceptTheSpecifiedTypes() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("module_name");

        List<Issue> expected = new ArrayList<>();
        expected.add(SINGLE_FILTER_ISSUES.get(1));

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.setExcludeModuleNameFilter(patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, expected);
    }

    @Test
    void excludeModuleNameFilterShouldReturnEmptyListIfAllSpecified() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("module_name");
        patterns.add("eman_eludom");

        List<Issue> expected = new ArrayList<>();

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.setExcludeModuleNameFilter(patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, expected);
    }

    @Test
    void includeModuleNameFilterShouldReturnResultOfLastFilterSet() {
        filterShouldReturnResultOfLastFilterSetTest((builder, patterns) -> builder.setIncludeModuleNameFilter(patterns), issue -> issue.getModuleName(), true);
    }

    @Test
    void excludeModuleNameFilterShouldReturnResultOfLastFilterSet() {
        filterShouldReturnResultOfLastFilterSetTest((builder, patterns) -> builder.setExcludeModuleNameFilter(patterns), issue -> issue.getModuleName(), false);
    }

    @Test
    void concatenationOfFiltersDeliversTheIssuesWhichMatchesAllFilterCriteriaIncludeFilter() {
        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder.setIncludeFileNameFilter(Arrays.asList("expected file_name"))
                .setIncludePackageNameFilter(Arrays.asList("expected package_name"))
                .setIncludeModuleNameFilter(Arrays.asList("expected module_name"))
                .setIncludeCategoryFilter(Arrays.asList("expected category"))
                .setIncludeTypeFilter(Arrays.asList("expected type"));

        testFilter(builder, FILTER_CONCATENATION_ISSUES, Arrays.asList(FILTER_CONCATENATION_ISSUES.get(0)));
    }

    @Test
    void concatenationOfFiltersDeliversTheIssuesWhichMatchesAllFilterCriteriaExcludeFilter() {
        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder.setExcludeFileNameFilter(Arrays.asList("file_name"))
                .setExcludePackageNameFilter(Arrays.asList("package_name"))
                .setExcludeModuleNameFilter(Arrays.asList("module_name"))
                .setExcludeCategoryFilter(Arrays.asList("category"))
                .setExcludeTypeFilter(Arrays.asList("type"));

        testFilter(builder, FILTER_CONCATENATION_ISSUES, Arrays.asList(FILTER_CONCATENATION_ISSUES.get(0)));
    }

    @Test
    void concatenationOfFiltersDeliversTheIssuesWhichMatchesAllFilterCriteriaIncludeAndExcludeFilters() {
        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder.setIncludeFileNameFilter(Arrays.asList("expected file_name"))
                .setExcludePackageNameFilter(Arrays.asList("package_name"))
                .setIncludeModuleNameFilter(Arrays.asList("expected module_name"))
                .setExcludeCategoryFilter(Arrays.asList("category"))
                .setIncludeTypeFilter(Arrays.asList("expected type"));

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

    private void filterShouldReturnResultOfLastFilterSetTest(final BiFunction<IssueFilterBuilder, Collection<String>, IssueFilterBuilder> filterSetter, final Function<Issue, String> issueVariableGetter, final boolean includeFilter) {
        List<Issue> expected = new ArrayList<>();
        if(includeFilter) {
            expected.add(SINGLE_FILTER_ISSUES.get(1));
        } else {
            expected.add(SINGLE_FILTER_ISSUES.get(0));
        }

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = filterSetter.apply(builder, Arrays.asList(issueVariableGetter.apply(SINGLE_FILTER_ISSUES.get(0))));
        builder = filterSetter.apply(builder, Arrays.asList(issueVariableGetter.apply(SINGLE_FILTER_ISSUES.get(1))));
        testFilter(builder, SINGLE_FILTER_ISSUES, expected);
    }
}
