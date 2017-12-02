package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.SortedSet;

import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableList;

import static edu.hm.hafner.analysis.assertj.Assertions.assertThat;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;
import static java.util.Arrays.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

/**
 * Unit tests for {@link Issues}.
 *
 * @author Marcel Binder
 */
class IssuesTest {
    private static final Issue ISSUE_1 = new IssueBuilder()
            .setMessage("issue-1")
            .setFileName("file-1")
            .setCategory("cat-1")
            .setPackageName("package-1")
            .setType("type-1")
            .setPriority(Priority.HIGH)
            .build();
    private static final Issue ISSUE_2 = new IssueBuilder().setMessage("issue-2").setCategory("cat-1").setPackageName("package-1").setFileName("file-1").build();
    private static final Issue ISSUE_3 = new IssueBuilder().setMessage("issue-3").setCategory("cat-1").setFileName("file-1").setPackageName("package-2").build();
    private static final Issue ISSUE_4 = new IssueBuilder()
            .setMessage("issue-4")
            .setCategory("cat-2")
            .setFileName("file-2")
            .setType("type-2")
            .setModuleName("module-5")
            .setPriority(Priority.LOW)
            .build();
    private static final Issue ISSUE_5 = new IssueBuilder()
            .setMessage("issue-5")
            .setCategory("cat-2")
            .setFileName("file-2")
            .setModuleName("module-5")
            .setPriority(Priority.LOW)
            .build();
    private static final Issue ISSUE_6 = new IssueBuilder()
            .setMessage("issue-6")
            .setCategory("cat-3")
            .setFileName("file-3")
            .setModuleName("module-3")
            .setType("type-3")
            .setPriority(Priority.LOW)
            .build();

    @Test
    void testEmptyIssues() {
        Issues issues = new Issues();

        assertThat(issues).hasSize(0);
    }

    @Test
    void testAddMultipleIssuesOneByOne() {
        Issues issues = new Issues();

        assertThat(issues.add(ISSUE_1)).isEqualTo(ISSUE_1);
        assertThat(issues.add(ISSUE_2)).isEqualTo(ISSUE_2);
        assertThat(issues.add(ISSUE_3)).isEqualTo(ISSUE_3);
        assertThat(issues.add(ISSUE_4)).isEqualTo(ISSUE_4);
        assertThat(issues.add(ISSUE_5)).isEqualTo(ISSUE_5);
        assertThat(issues.add(ISSUE_6)).isEqualTo(ISSUE_6);

        assertAllIssuesAdded(issues);
    }

    @Test
    void testAddMultipleIssuesToEmpty() {
        Issues issues = new Issues();
        List<Issue> issueList = asList(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6);

        assertThat(issues.addAll(issueList)).isEqualTo(issueList);

        assertAllIssuesAdded(issues);
    }

    @Test
    void testAddMultipleIssuesToNonEmpty() {
        Issues issues = new Issues();
        issues.add(ISSUE_1);

        issues.addAll(asList(ISSUE_2, ISSUE_3));
        issues.addAll(asList(ISSUE_4, ISSUE_5, ISSUE_6));

        assertAllIssuesAdded(issues);
    }

    private void assertAllIssuesAdded(final Issues issues) {
        assertSoftly(softly -> {
            softly.assertThat(issues)
                    .hasSize(6)
                    .hasHighPrioritySize(1)
                    .hasNormalPrioritySize(2)
                    .hasLowPrioritySize(3);
            softly.assertThat(issues.getFiles()).containsExactly("file-1", "file-2", "file-3");
            softly.assertThat((Iterable<Issue>) issues)
                    .containsExactly(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6);
            softly.assertThat(issues.all())
                    .containsExactly(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6);
        });
    }

    @Test
    void testAddDuplicate() {
        Issues issues = new Issues();
        issues.addAll(asList(ISSUE_1, ISSUE_1));

        assertThat(issues.all()).contains(ISSUE_1, ISSUE_1);
        assertThat(issues)
                .hasSize(2)
                .hasLowPrioritySize(0)
                .hasNormalPrioritySize(0)
                .hasHighPrioritySize(2);
        assertThat(issues.getFiles()).containsExactly("file-1");
    }

    @Test
    void testRemove() {
        Issues issues = new Issues();
        issues.addAll(asList(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6));

        Issue removed = issues.remove(ISSUE_1.getId());

        assertThat(removed).isEqualTo(ISSUE_1);
        assertThat(issues.all()).containsOnly(ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6);
        assertThat((Iterable<Issue>) issues).containsOnly(ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6);
        assertThat(issues).hasSize(5);
        assertThat(issues).hasHighPrioritySize(0);
        assertThat(issues).hasNormalPrioritySize(2);
        assertThat(issues).hasLowPrioritySize(3);
        assertThat(issues.getFiles()).containsExactly("file-1", "file-2", "file-3");
    }

    @Test
    void testRemoveNotExisting() {
        Issues issues = new Issues();

        assertThatThrownBy(() -> issues.remove(ISSUE_1.getId()))
                .isInstanceOf(NoSuchElementException.class)
                .hasNoCause()
                .hasMessageContaining(ISSUE_1.getId().toString());
    }

    @Test
    void testRemoveDuplicate() {
        Issues issues = new Issues();
        issues.addAll(asList(ISSUE_1, ISSUE_1));

        issues.remove(ISSUE_1.getId());

        assertThat(issues.all()).containsOnly(ISSUE_1);
        assertThat(issues).hasSize(1);
        assertThat(issues).hasHighPrioritySize(1);
        assertThat(issues.getFiles()).containsExactly("file-1");
    }

    @Test
    void testFindByIdSingleIssue() {
        Issues issues = new Issues();
        issues.addAll(ImmutableList.of(ISSUE_1));

        Issue found = issues.findById(ISSUE_1.getId());

        assertThat(found).isEqualTo(ISSUE_1);
    }

    @Test
    void testFindByIdSingleIssueNotExisting() {
        Issues issues = new Issues();
        issues.addAll(ImmutableList.of(ISSUE_1));

        assertThatThrownBy(() -> issues.findById(ISSUE_2.getId()))
                .isInstanceOf(NoSuchElementException.class)
                .hasNoCause()
                .hasMessageContaining(ISSUE_2.getId().toString());
    }

    @Test
    void testFindByIdMultipleIssues() {
        Issues issues = new Issues();
        issues.addAll(ImmutableList.of(ISSUE_1, ISSUE_2));

        Issue found = issues.findById(ISSUE_2.getId());

        assertThat(found).isEqualTo(ISSUE_2);
    }

    @Test
    void testFindByIdMultipleIssuesNotExisting() {
        Issues issues = new Issues();
        issues.addAll(ImmutableList.of(ISSUE_1, ISSUE_2));

        assertThatThrownBy(() -> issues.findById(ISSUE_3.getId()))
                .isInstanceOf(NoSuchElementException.class)
                .hasNoCause()
                .hasMessageContaining(ISSUE_3.getId().toString());
    }

    @Test
    void testFindNoneByProperty() {
        Issues issues = new Issues();
        issues.addAll(asList(ISSUE_1, ISSUE_2, ISSUE_3));

        ImmutableList<Issue> found = issues.findByProperty(issue -> Objects.equals(issue.getPriority(), Priority.LOW));

        assertThat(found).isEmpty();
    }

    @Test
    void testFindByPropertyResultImmutable() {
        Issues issues = new Issues();
        issues.addAll(asList(ISSUE_1, ISSUE_2, ISSUE_3));
        ImmutableList<Issue> found = issues.findByProperty(issue -> Objects.equals(issue.getPriority(), Priority.HIGH));

        //noinspection deprecation
        assertThatThrownBy(() -> found.add(ISSUE_4))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasNoCause();
    }

    @Test
    void testIterator() {
        Issues issues = new Issues();
        issues.addAll(asList(ISSUE_1, ISSUE_2, ISSUE_3));

        assertThat((Iterable<Issue>) issues).containsExactly(ISSUE_1, ISSUE_2, ISSUE_3);
    }

    @Test
    void testGet() {
        Issues issues = new Issues();
        issues.addAll(asList(ISSUE_1, ISSUE_2, ISSUE_3));

        assertThat(issues.get(0)).isEqualTo(ISSUE_1);
        assertThat(issues.get(1)).isEqualTo(ISSUE_2);
        assertThat(issues.get(2)).isEqualTo(ISSUE_3);
        assertThatThrownBy(() -> issues.get(-1))
                .isInstanceOf(IndexOutOfBoundsException.class)
                .hasNoCause()
                .hasMessageContaining("-1");
        assertThatThrownBy(() -> issues.get(3))
                .isInstanceOf(IndexOutOfBoundsException.class)
                .hasNoCause()
                .hasMessageContaining("3");
    }

    @Test
    void testGetFiles() {
        Issues issues = new Issues();
        issues.addAll(asList(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6));

        assertThat(issues.getFiles()).contains("file-1", "file-1", "file-3");
    }

    @Test
    void testToString() {
        Issues issues = new Issues();
        issues.addAll(asList(ISSUE_1, ISSUE_2, ISSUE_3));

        assertThat(issues.toString()).contains("3");
    }

    @Test
    void testGetProperties() {
        Issues issues = new Issues();
        issues.addAll(asList(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6));

        SortedSet<String> properties = issues.getProperties(issue -> issue.getMessage());

        assertThat(properties)
                .contains(ISSUE_1.getMessage())
                .contains(ISSUE_2.getMessage())
                .contains(ISSUE_3.getMessage());
    }

    @Test
    void testCopy() {
        Issues original = new Issues();
        original.addAll(asList(ISSUE_1, ISSUE_2, ISSUE_3));

        Issues copy = original.copy();

        assertThat(copy).isNotSameAs(original);
        assertThat(copy.all()).contains(ISSUE_1, ISSUE_2, ISSUE_3);

        copy.add(ISSUE_4);
        assertThat(original.all()).contains(ISSUE_1, ISSUE_2, ISSUE_3);
        assertThat(copy.all()).contains(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4);
    }

    @Test
    void shouldReturnAllIssuesWhenNoFilterIsSet() {
        Issues original = new Issues();
        original.addAll(asList(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6));

        IssuesFilter includeFilter = new IssuesFilterBuilder().build();
        IssuesFilter emptyExcludeFilter = new IssuesFilterBuilder().build();
        Issues filteredIssues = original.filter(includeFilter, emptyExcludeFilter);

        assertThat(filteredIssues.all()).containsExactly(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6);
    }

    @Test
    void shouldReturnNoIssuesWhenIncludeFilterAndExcludeFilterAreTheSameAndNotEmpty() {
        Issues original = new Issues();
        original.addAll(asList(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6));
        List<String> categories = new ArrayList<>();

        IssuesFilterBuilder builder = new IssuesFilterBuilder();
        categories.add("cat-1");
        builder.setCategories(categories);

        IssuesFilter includeFilter = builder.build();
        IssuesFilter emptyExcludeFilter = builder.build();
        Issues filteredIssues = original.filter(includeFilter, emptyExcludeFilter);
        assertThat(filteredIssues.all()).containsExactly();
    }

    @Test
    void shouldReturnNoIssuesWhenIncludeIssueWithFileNameAndExcludeWhemWithCategorie() {
        Issues original = new Issues();
        original.addAll(asList(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6));
        List<String> categories = new ArrayList<>();
        List<String> fileNames = new ArrayList<>();

        IssuesFilterBuilder excludeBuilder = new IssuesFilterBuilder();
        categories.add("cat-1");
        excludeBuilder.setCategories(categories);

        IssuesFilterBuilder includeBuilder = new IssuesFilterBuilder();
        fileNames.add("cat-1");
        includeBuilder.setCategories(categories);

        IssuesFilter includeFilter = includeBuilder.build();
        IssuesFilter emptyExcludeFilter = excludeBuilder.build();
        Issues filteredIssues = original.filter(includeFilter, emptyExcludeFilter);
        assertThat(filteredIssues.all()).containsExactly();
    }


    @Test
    void shouldOnlyReturnOnlyIssueWithCat1() {
        Issues original = new Issues();
        original.addAll(asList(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6));
        IssuesFilterBuilder builder = new IssuesFilterBuilder();
        List<String> categories = new ArrayList<>();
        categories.add("cat-1");
        builder.setCategories(categories);
        IssuesFilter includeFilter = builder.build();
        IssuesFilter emptyExcludeFilter = new IssuesFilterBuilder().build();
        Issues filteredIssues = original.filter(includeFilter, emptyExcludeFilter);
        assertThat(filteredIssues.all()).containsExactly(ISSUE_1, ISSUE_2, ISSUE_3);
    }

    @Test
    void shouldOnlyReturnOnlyIssueWithFileNameFile2andFile3() {
        Issues original = new Issues();
        original.addAll(asList(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6));
        IssuesFilterBuilder builder = new IssuesFilterBuilder();
        List<String> filenames = new ArrayList<>();
        filenames.add("file-2");
        filenames.add("file-3");
        builder.setFileNames(filenames);
        IssuesFilter includeFilter = builder.build();
        IssuesFilter emptyExcludeFilter = new IssuesFilterBuilder().build();
        Issues filteredIssues = original.filter(includeFilter, emptyExcludeFilter);
        assertThat(filteredIssues.all()).containsExactly(ISSUE_4, ISSUE_5, ISSUE_6);
    }

    @Test
    void shouldOnlyReturnOnlyIssueWithType3() {
        Issues original = new Issues();
        original.addAll(asList(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6));
        IssuesFilterBuilder builder = new IssuesFilterBuilder();
        List<String> types = new ArrayList<>();
        types.add("type-3");
        builder.setTypes(types);
        IssuesFilter includeFilter = builder.build();
        IssuesFilter emptyExcludeFilter = new IssuesFilterBuilder().build();
        Issues filteredIssues = original.filter(includeFilter, emptyExcludeFilter);
        assertThat(filteredIssues.all()).containsExactly(ISSUE_6);
    }

    @Test
    void shouldOnlyReturnOnlyIssueWithPackageName1() {
        Issues original = new Issues();
        original.addAll(asList(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6));
        IssuesFilterBuilder builder = new IssuesFilterBuilder();
        List<String> packageNames = new ArrayList<>();
        packageNames.add("package-1");
        builder.setPackageNames(packageNames);
        IssuesFilter includeFilter = builder.build();
        IssuesFilter emptyExcludeFilter = new IssuesFilterBuilder().build();
        Issues filteredIssues = original.filter(includeFilter, emptyExcludeFilter);
        assertThat(filteredIssues.all()).containsExactly(ISSUE_1, ISSUE_2);
    }

    @Test
    void shouldOnlyReturnOnlyIssueWithModuleName1() {
        Issues original = new Issues();
        original.addAll(asList(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6));
        IssuesFilterBuilder builder = new IssuesFilterBuilder();
        List<String> moduleNames = new ArrayList<>();
        moduleNames.add("module-5");
        builder.setModuleNames(moduleNames);
        IssuesFilter includeFilter = builder.build();
        IssuesFilter emptyExcludeFilter = new IssuesFilterBuilder().build();
        Issues filteredIssues = original.filter(includeFilter, emptyExcludeFilter);
        assertThat(filteredIssues.all()).containsExactly(ISSUE_4, ISSUE_5);
    }

    @Test
    void shouldOnlyReturnIssueWithoutCat1OrCat2() {
        Issues original = new Issues();
        original.addAll(asList(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6));
        IssuesFilterBuilder builder = new IssuesFilterBuilder();
        List<String> categories = new ArrayList<>();
        categories.add("cat-1");
        categories.add("cat-2");
        builder.setCategories(categories);
        IssuesFilter excludeFilter = builder.build();
        IssuesFilter emptyIncludeFilter = new IssuesFilterBuilder().build();
        Issues filteredIssues = original.filter(emptyIncludeFilter, excludeFilter);
        assertThat(filteredIssues.all()).containsExactly(ISSUE_6);
    }

    @Test
    void shouldOnlyReturnIssueWithoutFileOrFile2() {
        Issues original = new Issues();
        original.addAll(asList(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6));
        IssuesFilterBuilder builder = new IssuesFilterBuilder();
        List<String> fileNames = new ArrayList<>();
        fileNames.add("file-1");
        fileNames.add("file-2");
        builder.setFileNames(fileNames);
        IssuesFilter excludeFilter = builder.build();
        IssuesFilter emptyIncludeFilter = new IssuesFilterBuilder().build();
        Issues filteredIssues = original.filter(emptyIncludeFilter, excludeFilter);
        assertThat(filteredIssues.all()).containsExactly(ISSUE_6);
    }

    @Test
    void shouldOnlyReturnIssueWithoutTyp2orTyp3() {
        Issues original = new Issues();
        original.addAll(asList(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6));
        IssuesFilterBuilder builder = new IssuesFilterBuilder();
        List<String> types = new ArrayList<>();
        types.add("type-1");
        types.add("type-2");
        builder.setTypes(types);
        IssuesFilter excludeFilter = builder.build();
        IssuesFilter emptyIncludeFilter = new IssuesFilterBuilder().build();
        Issues filteredIssues = original.filter(emptyIncludeFilter, excludeFilter);
        assertThat(filteredIssues.size()).isEqualTo(4);
        assertThat(filteredIssues.all()).containsExactly(ISSUE_2, ISSUE_3, ISSUE_5, ISSUE_6);
    }

    @Test
    void shouldOnlyReturnIssueWithoutPackageName1orPackageName3() {
        Issues original = new Issues();
        original.addAll(asList(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6));
        IssuesFilterBuilder builder = new IssuesFilterBuilder();
        List<String> packageNames = new ArrayList<>();
        packageNames.add("package-1");
        packageNames.add("package-3");
        builder.setPackageNames(packageNames);
        IssuesFilter excludeFilter = builder.build();
        IssuesFilter emptyIncludeFilter = new IssuesFilterBuilder().build();
        Issues filteredIssues = original.filter(emptyIncludeFilter, excludeFilter);

        assertThat(filteredIssues.all()).containsExactly(ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6);
    }

    @Test
    void shouldOnlyReturnIssueWithoutModuleName5orModuleName3() {
        Issues original = new Issues();
        original.addAll(asList(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6));
        IssuesFilterBuilder builder = new IssuesFilterBuilder();
        List<String> moduleNames = new ArrayList<>();
        moduleNames.add("module-5");
        moduleNames.add("module-3");
        builder.setModuleNames(moduleNames);
        IssuesFilter excludeFilter = builder.build();
        IssuesFilter emptyIncludeFilter = new IssuesFilterBuilder().build();
        Issues filteredIssues = original.filter(emptyIncludeFilter, excludeFilter);
        assertThat(filteredIssues.all()).containsExactly(ISSUE_1, ISSUE_2, ISSUE_3);
    }

    @Test
    void shouldReturnIssuesWithModule1andWithoutCat1andCat3() {
        Issues original = new Issues();
        original.addAll(asList(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6));
        IssuesFilterBuilder builder = new IssuesFilterBuilder();
        List<String> moduleNames = new ArrayList<>();
        moduleNames.add("module-5");

        List<String> categories = new ArrayList<>();
        categories.add("cat-1");
        categories.add("cat-2");

        builder.setModuleNames(moduleNames);
        IssuesFilter excludeFilter = builder.build();
        IssuesFilter includeFilter = new IssuesFilterBuilder().setCategories(categories).build();
        Issues filteredIssues = original.filter(includeFilter, excludeFilter);
        assertThat(filteredIssues.all()).containsExactly(ISSUE_1, ISSUE_2, ISSUE_3);

    }
    @Test
    void shouldReturnIssuesWithFile1andFile3WithoutPackage1andPackage3() {
        Issues original = new Issues();
        original.addAll(asList(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6));
        IssuesFilterBuilder builder = new IssuesFilterBuilder();
        List<String> fileNames = new ArrayList<>();
        fileNames.add("file-1");
        fileNames.add("file-3");

        List<String> packageNames = new ArrayList<>();
        packageNames.add("package-1");
        packageNames.add("package-3");

        builder.setPackageNames(packageNames);
        IssuesFilter excludeFilter = builder.build();
        IssuesFilter includeFilter = new IssuesFilterBuilder().setFileNames(fileNames).build();

        Issues filteredIssues = original.filter(includeFilter, excludeFilter);
        assertThat(filteredIssues.all()).containsExactly(ISSUE_3, ISSUE_6);

    }




}