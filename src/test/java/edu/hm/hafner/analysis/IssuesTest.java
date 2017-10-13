package edu.hm.hafner.analysis;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.SortedSet;

import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableList;

import edu.hm.hafner.analysis.assertj.AnalysisSoftAssertions;
import static edu.hm.hafner.analysis.assertj.IssuesAssert.assertThat;
import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

/**
 * Unit test for {@link Issues}.
 */
class IssuesTest {
    private static final Issue ISSUE_1 = new IssueBuilder().setFileName("issue-1").setPriority(Priority.HIGH).build();
    private static final Issue ISSUE_2 = new IssueBuilder().setFileName("issue-2").build();
    private static final Issue ISSUE_3 = new IssueBuilder().setFileName("issue-3").build();
    private static final Issue ISSUE_4 = new IssueBuilder().setFileName("issue-4").setPriority(Priority.LOW).build();
    private static final Issue ISSUE_5 = new IssueBuilder().setFileName("issue-5").setPriority(Priority.LOW).build();
    private static final Issue ISSUE_6 = new IssueBuilder().setFileName("issue-6").setPriority(Priority.LOW).build();

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

    private void assertAllIssuesAdded(Issues issues) {
        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        softly.assertThat(issues).hasSize(6);
        softly.assertThat(issues).hasNumberOfFiles(6);
        softly.assertThat(issues).hasHighPrioritySize(1);
        softly.assertThat(issues).hasNormalPrioritySize(2);
        softly.assertThat(issues).hasLowPrioritySize(3);
        softly.assertThat(issues.all()).contains(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6);
        softly.assertAll();
    }

    @Test
    void testAddDuplicate() {
        Issues issues = new Issues();
        issues.addAll(asList(ISSUE_1, ISSUE_1));

        assertThat(issues.all()).contains(ISSUE_1, ISSUE_1);
        assertThat(issues).hasSize(2);
        assertThat(issues).hasNumberOfFiles(2);
        assertThat(issues).hasHighPrioritySize(2);
    }

    @Test
    void testRemove() {
        Issues issues = new Issues();
        issues.addAll(asList(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6));

        Issue removed = issues.remove(ISSUE_1.getId());

        assertThat(removed).isEqualTo(ISSUE_1);
        assertThat(issues.all()).containsOnly(ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6);
        assertThat(issues).hasSize(2);
        assertThat(issues).hasNumberOfFiles(2);
        assertThat(issues).hasHighPrioritySize(0);
        assertThat(issues).hasNormalPrioritySize(2);
        assertThat(issues).hasLowPrioritySize(3);
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
        assertThat(issues).hasNumberOfFiles(1);
        assertThat(issues).hasHighPrioritySize(1);
    }

    @Test
    void testFindById() {
        Issues issues = new Issues();
        issues.addAll(asList(ISSUE_1, ISSUE_2, ISSUE_3));

        Issue found = issues.findById(ISSUE_1.getId());

        assertThat(found).isEqualTo(ISSUE_1);
    }

    @Test
    void testFindByIdNotExisting() {
        Issues issues = new Issues();

        assertThatThrownBy(() -> issues.findById(ISSUE_1.getId()))
                .isInstanceOf(NoSuchElementException.class)
                .hasNoCause()
                .hasMessageContaining(ISSUE_1.getId().toString());
    }

    @Test
    void testFindSingleByProperty() {
        Issues issues = new Issues();
        issues.addAll(asList(ISSUE_1, ISSUE_2, ISSUE_3));

        ImmutableList<Issue> found = issues.findByProperty(issue -> Objects.equals(issue.getPriority(), Priority.HIGH));

        assertThat(found).contains(ISSUE_1);
    }

    @Test
    void testFindMultipleByProperty() {
        Issues issues = new Issues();
        issues.addAll(asList(ISSUE_1, ISSUE_2, ISSUE_3));

        ImmutableList<Issue> found = issues.findByProperty(issue -> Objects.equals(issue.getPriority(), Priority.NORMAL));

        assertThat(found).contains(ISSUE_2, ISSUE_3);
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

        assertThatThrownBy(() -> found.add(ISSUE_4))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasNoCause();
    }

    @Test
    void testIterator() {
        Issues issues = new Issues();
        issues.addAll(asList(ISSUE_1, ISSUE_2, ISSUE_3));

        assertThat(issues.iterator()).contains(ISSUE_1, ISSUE_2, ISSUE_3);
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
        issues.addAll(asList(ISSUE_1, ISSUE_2, ISSUE_3));

        assertThat(issues.getFiles()).contains("issue-1", "issue-2", "issue-3");
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
        issues.addAll(asList(ISSUE_1, ISSUE_2, ISSUE_3));

        SortedSet<String> properties = issues.getProperties(issue -> issue.getPriority().name() + ": " + issue.getFileName());

        assertThat(properties)
                .contains("HIGH: issue-1")
                .contains("NORMAL: issue-2")
                .contains("NORMAL: issue-3");
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
}