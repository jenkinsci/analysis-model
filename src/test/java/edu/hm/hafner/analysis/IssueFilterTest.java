package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;


import edu.hm.hafner.analysis.IssueFilter.IssueFilterBuilder;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * Tests for the class {@link IssueFilter IssueFilter}. These Tests are generated while developing.
 *
 * @author Michael Schmid
 */
class IssueFilterTest {
    private static final List<Issue> ISSUES_LIST = new ArrayList<>();
    static {
        ISSUES_LIST.add(new IssueBuilder()
                .setFileName("file_name")
                .setPackageName("package_name")
                .setModuleName("module_name")
                .setCategory("category")
                .setType("type").build());
        ISSUES_LIST.add(new IssueBuilder()
                .setFileName("eman_elif")
                .setPackageName("eman_egakcap")
                .setModuleName("eman_eludom")
                .setCategory("yrogetac")
                .setType("epyt").build());
    }

    @Test
    void anyEmptyFilterShouldNotFilterAnything() {
        List<Issue> issueList = new ArrayList<>();
        issueList.addAll(ISSUES_LIST);
        IssueFilterBuilder builder = new IssueFilterBuilder();
        testFilter(builder, issueList, issueList);
    }

    @Test
    void includeTypeFilterShouldReturnOnlyTheSpecifiedTypes() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("type");

        List<Issue> expected = new ArrayList<>();
        expected.add(ISSUES_LIST.get(0));

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.addIncludeTypeFilter(patterns);
        testFilter(builder, ISSUES_LIST, expected);
    }

    @Test
    void includeTypeFilterShouldReturnAllInputIfSpecified() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("type");
        patterns.add("epyt");

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.addIncludeTypeFilter(patterns);
        testFilter(builder, ISSUES_LIST, ISSUES_LIST);
    }

    @Test
    void excludeTypeFilterShouldReturnAnythingExceptTheSpecifiedTypes() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("type");

        List<Issue> expected = new ArrayList<>();
        expected.add(ISSUES_LIST.get(1));

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.addExcludeTypeFilter(patterns);
        testFilter(builder, ISSUES_LIST, expected);
    }

    @Test
    void excludeTypeFilterShouldReturnEmptyListIfAllSpecified() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("type");
        patterns.add("epyt");

        List<Issue> expected = new ArrayList<>();

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.addExcludeTypeFilter(patterns);
        testFilter(builder, ISSUES_LIST, expected);
    }

    @Test
    void includeCategoryFilterShouldReturnOnlyTheSpecifiedTypes() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("category");

        List<Issue> expected = new ArrayList<>();
        expected.add(ISSUES_LIST.get(0));

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.addIncludeCategoryFilter(patterns);
        testFilter(builder, ISSUES_LIST, expected);
    }

    @Test
    void includeCategoryFilterShouldReturnAllInputIfSpecified() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("category");
        patterns.add("yrogetac");

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.addIncludeCategoryFilter(patterns);
        testFilter(builder, ISSUES_LIST, ISSUES_LIST);
    }

    @Test
    void excludeCategoryFilterShouldReturnAnythingExceptTheSpecifiedTypes() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("category");

        List<Issue> expected = new ArrayList<>();
        expected.add(ISSUES_LIST.get(1));

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.addExcludeCategoryFilter(patterns);
        testFilter(builder, ISSUES_LIST, expected);
    }

    @Test
    void excludeCategoryFilterShouldReturnEmptyListIfAllSpecified() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("category");
        patterns.add("yrogetac");

        List<Issue> expected = new ArrayList<>();

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.addExcludeCategoryFilter(patterns);
        testFilter(builder, ISSUES_LIST, expected);
    }

    @Test
    void includeFileNameFilterShouldReturnOnlyTheSpecifiedTypes() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("file_name");

        List<Issue> expected = new ArrayList<>();
        expected.add(ISSUES_LIST.get(0));

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.addIncludeFileNameFilter(patterns);
        testFilter(builder, ISSUES_LIST, expected);
    }

    @Test
    void includeFileNameFilterShouldReturnAllInputIfSpecified() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("file_name");
        patterns.add("eman_elif");

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.addIncludeFileNameFilter(patterns);
        testFilter(builder, ISSUES_LIST, ISSUES_LIST);
    }

    @Test
    void excludeFileNameFilterShouldReturnAnythingExceptTheSpecifiedTypes() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("file_name");

        List<Issue> expected = new ArrayList<>();
        expected.add(ISSUES_LIST.get(1));

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.addExcludeFileNameFilter(patterns);
        testFilter(builder, ISSUES_LIST, expected);
    }

    @Test
    void excludeFileNameFilterShouldReturnEmptyListIfAllSpecified() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("file_name");
        patterns.add("eman_elif");

        List<Issue> expected = new ArrayList<>();

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.addExcludeFileNameFilter(patterns);
        testFilter(builder, ISSUES_LIST, expected);
    }

    @Test
    void includePackageNameFilterShouldReturnOnlyTheSpecifiedTypes() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("package_name");

        List<Issue> expected = new ArrayList<>();
        expected.add(ISSUES_LIST.get(0));

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.addIncludePackageNameFilter(patterns);
        testFilter(builder, ISSUES_LIST, expected);
    }

    @Test
    void includePackageNameFilterShouldReturnAllInputIfSpecified() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("package_name");
        patterns.add("eman_egakcap");

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.addIncludePackageNameFilter(patterns);
        testFilter(builder, ISSUES_LIST, ISSUES_LIST);
    }

    @Test
    void excludePackageNameFilterShouldReturnAnythingExceptTheSpecifiedTypes() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("package_name");

        List<Issue> expected = new ArrayList<>();
        expected.add(ISSUES_LIST.get(1));

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.addExcludePackageNameFilter(patterns);
        testFilter(builder, ISSUES_LIST, expected);
    }

    @Test
    void excludePackageNameFilterShouldReturnEmptyListIfAllSpecified() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("package_name");
        patterns.add("eman_egakcap");

        List<Issue> expected = new ArrayList<>();

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.addExcludePackageNameFilter(patterns);
        testFilter(builder, ISSUES_LIST, expected);
    }

    @Test
    void includeModuleNameFilterShouldReturnOnlyTheSpecifiedTypes() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("module_name");

        List<Issue> expected = new ArrayList<>();
        expected.add(ISSUES_LIST.get(0));

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.addIncludeModuleNameFilter(patterns);
        testFilter(builder, ISSUES_LIST, expected);
    }

    @Test
    void includeModuleNameFilterShouldReturnAllInputIfSpecified() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("module_name");
        patterns.add("eman_eludom");

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.addIncludeModuleNameFilter(patterns);
        testFilter(builder, ISSUES_LIST, ISSUES_LIST);
    }

    @Test
    void excludeModuleNameFilterShouldReturnAnythingExceptTheSpecifiedTypes() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("module_name");

        List<Issue> expected = new ArrayList<>();
        expected.add(ISSUES_LIST.get(1));

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.addExcludeModuleNameFilter(patterns);
        testFilter(builder, ISSUES_LIST, expected);
    }

    @Test
    void excludeModuleNameFilterShouldReturnEmptyListIfAllSpecified() {
        Collection<String> patterns = new ArrayList<>();
        patterns.add("module_name");
        patterns.add("eman_eludom");

        List<Issue> expected = new ArrayList<>();

        IssueFilterBuilder builder = new IssueFilterBuilder();
        builder = builder.addExcludeModuleNameFilter(patterns);
        testFilter(builder, ISSUES_LIST, expected);
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
