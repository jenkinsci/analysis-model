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
        filterShouldReturnTheExpectedResultOnePattern(IssueFilterBuilder::setIncludeTypeFilter, Issue::getType, true);
    }

    @Test
    void includeTypeFilterShouldReturnAllInputIfSpecified() {
        filterShouldReturnTheExpectedResultTwoPattern(IssueFilterBuilder::setIncludeTypeFilter, Issue::getType, true);
    }

    @Test
    void excludeTypeFilterShouldReturnAnythingExceptTheSpecifiedTypes() {
        filterShouldReturnTheExpectedResultOnePattern(IssueFilterBuilder::setExcludeTypeFilter, Issue::getType, false);
    }

    @Test
    void excludeTypeFilterShouldReturnEmptyListIfAllSpecified() {
        filterShouldReturnTheExpectedResultTwoPattern(IssueFilterBuilder::setExcludeTypeFilter, Issue::getType, false);
    }

    @Test
    void includeTypeFilterShouldReturnResultOfLastFilterSet() {
        filterShouldReturnResultOfLastFilterSetTest(IssueFilterBuilder::setIncludeTypeFilter, Issue::getType, true);
    }

    @Test
    void excludeTypeFilterShouldReturnResultOfLastFilterSet() {
        filterShouldReturnResultOfLastFilterSetTest(IssueFilterBuilder::setExcludeTypeFilter, Issue::getType, false);
    }

    @Test
    void includeCategoryFilterShouldReturnOnlyTheSpecifiedTypes() {
        filterShouldReturnTheExpectedResultOnePattern(IssueFilterBuilder::setIncludeCategoryFilter, Issue::getCategory, true);
    }

    @Test
    void includeCategoryFilterShouldReturnAllInputIfSpecified() {
        filterShouldReturnTheExpectedResultTwoPattern(IssueFilterBuilder::setIncludeCategoryFilter, Issue::getCategory, true);
    }

    @Test
    void excludeCategoryFilterShouldReturnAnythingExceptTheSpecifiedTypes() {
        filterShouldReturnTheExpectedResultOnePattern(IssueFilterBuilder::setExcludeCategoryFilter, Issue::getCategory, false);
    }

    @Test
    void excludeCategoryFilterShouldReturnEmptyListIfAllSpecified() {
        filterShouldReturnTheExpectedResultTwoPattern(IssueFilterBuilder::setExcludeCategoryFilter, Issue::getCategory, false);
    }

    @Test
    void includeCategoryFilterShouldReturnResultOfLastFilterSet() {
        filterShouldReturnResultOfLastFilterSetTest(IssueFilterBuilder::setIncludeCategoryFilter, Issue::getCategory, true);
    }

    @Test
    void excludeCategoryFilterShouldReturnResultOfLastFilterSet() {
        filterShouldReturnResultOfLastFilterSetTest(IssueFilterBuilder::setExcludeCategoryFilter, Issue::getCategory, false);
    }

    @Test
    void includeFileNameFilterShouldReturnOnlyTheSpecifiedTypes() {
        filterShouldReturnTheExpectedResultOnePattern(IssueFilterBuilder::setIncludeFileNameFilter, Issue::getFileName, true);
    }

    @Test
    void includeFileNameFilterShouldReturnAllInputIfSpecified() {
        filterShouldReturnTheExpectedResultTwoPattern(IssueFilterBuilder::setIncludeFileNameFilter, Issue::getFileName, true);
    }

    @Test
    void excludeFileNameFilterShouldReturnAnythingExceptTheSpecifiedTypes() {
        filterShouldReturnTheExpectedResultOnePattern(IssueFilterBuilder::setExcludeFileNameFilter, Issue::getFileName, false);
    }

    @Test
    void excludeFileNameFilterShouldReturnEmptyListIfAllSpecified() {
        filterShouldReturnTheExpectedResultTwoPattern(IssueFilterBuilder::setExcludeFileNameFilter, Issue::getFileName, false);
    }

    @Test
    void includeFileNameFilterShouldReturnResultOfLastFilterSet() {
        filterShouldReturnResultOfLastFilterSetTest(IssueFilterBuilder::setIncludeFileNameFilter, Issue::getFileName, true);
    }

    @Test
    void excludeFileNameFilterShouldReturnResultOfLastFilterSet() {
        filterShouldReturnResultOfLastFilterSetTest(IssueFilterBuilder::setExcludeFileNameFilter, Issue::getFileName, false);
    }

    @Test
    void includePackageNameFilterShouldReturnOnlyTheSpecifiedTypes() {
        filterShouldReturnTheExpectedResultOnePattern(IssueFilterBuilder::setIncludePackageNameFilter, Issue::getPackageName, true);
    }

    @Test
    void includePackageNameFilterShouldReturnAllInputIfSpecified() {
        filterShouldReturnTheExpectedResultTwoPattern(IssueFilterBuilder::setIncludePackageNameFilter, Issue::getPackageName, true);
    }

    @Test
    void excludePackageNameFilterShouldReturnAnythingExceptTheSpecifiedTypes() {
        filterShouldReturnTheExpectedResultOnePattern(IssueFilterBuilder::setExcludePackageNameFilter, Issue::getPackageName, false);
    }

    @Test
    void excludePackageNameFilterShouldReturnEmptyListIfAllSpecified() {
        filterShouldReturnTheExpectedResultTwoPattern(IssueFilterBuilder::setExcludePackageNameFilter, Issue::getPackageName, false);
    }

    @Test
    void includePackageNameFilterShouldReturnResultOfLastFilterSet() {
        filterShouldReturnResultOfLastFilterSetTest(IssueFilterBuilder::setIncludePackageNameFilter, Issue::getPackageName, true);
    }

    @Test
    void excludePackageNameFilterShouldReturnResultOfLastFilterSet() {
        filterShouldReturnResultOfLastFilterSetTest(IssueFilterBuilder::setExcludePackageNameFilter, Issue::getPackageName, false);
    }

    @Test
    void includeModuleNameFilterShouldReturnOnlyTheSpecifiedTypes() {
        filterShouldReturnTheExpectedResultOnePattern(IssueFilterBuilder::setIncludeModuleNameFilter, Issue::getModuleName, true);
    }

    @Test
    void includeModuleNameFilterShouldReturnAllInputIfSpecified() {
        filterShouldReturnTheExpectedResultTwoPattern(IssueFilterBuilder::setIncludeModuleNameFilter, Issue::getModuleName, true);
    }

    @Test
    void excludeModuleNameFilterShouldReturnAnythingExceptTheSpecifiedTypes() {
        filterShouldReturnTheExpectedResultOnePattern(IssueFilterBuilder::setExcludeModuleNameFilter, Issue::getModuleName, false);
    }

    @Test
    void excludeModuleNameFilterShouldReturnEmptyListIfAllSpecified() {
        filterShouldReturnTheExpectedResultTwoPattern(IssueFilterBuilder::setExcludeModuleNameFilter, Issue::getModuleName, false);
    }

    @Test
    void includeModuleNameFilterShouldReturnResultOfLastFilterSet() {
        filterShouldReturnResultOfLastFilterSetTest(IssueFilterBuilder::setIncludeModuleNameFilter, Issue::getModuleName, true);
    }

    @Test
    void excludeModuleNameFilterShouldReturnResultOfLastFilterSet() {
        filterShouldReturnResultOfLastFilterSetTest(IssueFilterBuilder::setExcludeModuleNameFilter, Issue::getModuleName, false);
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

    private void filterShouldReturnTheExpectedResultOnePattern(final BiFunction<IssueFilterBuilder, Collection<String>, IssueFilterBuilder> filterSetter, final Function<Issue, String> issueVariableGetter, final boolean includeFilter) {
        List<Issue> expected = new ArrayList<>();
        if(includeFilter) {
            expected.add(SINGLE_FILTER_ISSUES.get(0));
        } else {
            expected.add(SINGLE_FILTER_ISSUES.get(1));
        }

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = filterSetter.apply(builder, Arrays.asList(issueVariableGetter.apply(SINGLE_FILTER_ISSUES.get(0))));
        testFilter(builder, SINGLE_FILTER_ISSUES, expected);
    }

    private void filterShouldReturnTheExpectedResultTwoPattern(final BiFunction<IssueFilterBuilder, Collection<String>, IssueFilterBuilder> filterSetter, final Function<Issue, String> issueVariableGetter, final boolean includeFilter) {
        Collection<String> patterns = new ArrayList<>();
        patterns.add(issueVariableGetter.apply(SINGLE_FILTER_ISSUES.get(0)));
        patterns.add(issueVariableGetter.apply(SINGLE_FILTER_ISSUES.get(1)));

        List<Issue> expected = new ArrayList<>();
        if(includeFilter) {
            expected = SINGLE_FILTER_ISSUES;
        }

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = filterSetter.apply(builder, patterns);
        testFilter(builder, SINGLE_FILTER_ISSUES, expected);
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
