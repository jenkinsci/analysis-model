package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.SortedSet;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


import com.google.common.collect.ImmutableList;

import edu.hm.hafner.analysis.assertj.AnalysisSoftAssertions;
import edu.hm.hafner.analysis.assertj.IssueAssert;
import edu.hm.hafner.analysis.assertj.IssuesAssert;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

/**
 * Created by Sarah on 19.10.2017.
 */

public class IssuesTest {

    private static final Issue ISSUE_1 = new IssueBuilder().setMessage("issue-1").setFileName("file-1").setPriority(Priority.HIGH).build();
    private static final Issue ISSUE_2 = new IssueBuilder().setMessage("issue-2").setFileName("file-1").build();
    private static final Issue ISSUE_3 = new IssueBuilder().setMessage("issue-3").setFileName("file-1").build();
    private static final Issue ISSUE_4 = new IssueBuilder().setMessage("issue-4").setFileName("file-2").setPriority(Priority.LOW).build();
    private static final Issue ISSUE_5 = new IssueBuilder().setMessage("issue-5").setFileName("file-2").setPriority(Priority.LOW).build();
    private static final Issue ISSUE_6 = new IssueBuilder().setMessage("issue-6").setFileName("file-3").setPriority(Priority.LOW).build();

    @Test
    void testCreateEmptyIssues() {
        Issues issues = new Issues();
        IssuesAssert.assertThat(issues).hasSize(0);
    }

    @Test
    void testAddSixIssuesToIssuesOneByOne(){
        Issues issues = new Issues();

        IssueAssert.assertThat(issues.add(ISSUE_1)).isEqualTo(ISSUE_1);
        IssueAssert.assertThat(issues.add(ISSUE_2)).isEqualTo(ISSUE_2);
        IssueAssert.assertThat(issues.add(ISSUE_3)).isEqualTo(ISSUE_3);
        IssueAssert.assertThat(issues.add(ISSUE_4)).isEqualTo(ISSUE_4);
        IssueAssert.assertThat(issues.add(ISSUE_5)).isEqualTo(ISSUE_5);
        IssueAssert.assertThat(issues.add(ISSUE_6)).isEqualTo(ISSUE_6);

        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        softly.assertThat(issues)
                .hasSize(6)
                .hasNumberOfFiles(3)
                .hasHighPrioritySize(1)
                .hasNormalPrioritySize(2)
                .hasLowPrioritySize(3);
        softly.assertThat(issues.getFiles()).containsExactly("file-1", "file-2", "file-3");
        softly.assertThat((Iterable<Issue>) issues)
                .containsExactly(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6);
        softly.assertThat(issues.all())
                .containsExactly(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6);
    }

    @Test
    void testAddCollectionToIsses(){
        Issues issues = new Issues();
        List<Issue> issueList = new ArrayList<Issue>();
        issueList.add(ISSUE_1);
        issueList.add(ISSUE_2);
        issueList.add(ISSUE_3);
        issueList.add(ISSUE_4);
        issueList.add(ISSUE_5);
        issueList.add(ISSUE_6);

        issues.addAll(issueList);

        IssuesAssert.assertThat(issues).isEqualTo(issueList);

        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        softly.assertThat(issues)
                .hasSize(6)
                .hasNumberOfFiles(3)
                .hasHighPrioritySize(1)
                .hasNormalPrioritySize(2)
                .hasLowPrioritySize(3);
        softly.assertThat(issues.getFiles()).containsExactly("file-1", "file-2", "file-3");
        softly.assertThat((Iterable<Issue>) issues)
                .containsExactly(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6);
        softly.assertThat(issues.all())
                .containsExactly(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6);
    }

    @Test
    void testAddCollectionToNonEmptyIssues(){
        Issues issues = new Issues();
        List<Issue> issueList1 = new ArrayList<Issue>();
        issueList1.add(ISSUE_2);
        issueList1.add(ISSUE_3);
        List<Issue> issueList2 = new ArrayList<Issue>();
        issueList2.add(ISSUE_4);
        issueList2.add(ISSUE_5);
        issueList2.add(ISSUE_6);

        issues.add(ISSUE_1);

        issues.addAll(issueList1);
        issues.addAll(issueList2);

        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        softly.assertThat(issues)
                .hasSize(6)
                .hasNumberOfFiles(3)
                .hasHighPrioritySize(1)
                .hasNormalPrioritySize(2)
                .hasLowPrioritySize(3);
        softly.assertThat(issues.getFiles()).containsExactly("file-1", "file-2", "file-3");
        softly.assertThat((Iterable<Issue>) issues)
                .containsExactly(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6);
        softly.assertThat(issues.all())
                .containsExactly(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6);
    }

    @Test
    void testAddDuplicateIssue(){
        Issues issues = new Issues();
        List<Issue> issueList = new ArrayList<>();

        issueList.add(ISSUE_1);
        issueList.add(ISSUE_1);

        issues.addAll(issueList);

        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        Assertions.assertThat(issues).containsOnly(ISSUE_1, ISSUE_1);
        IssuesAssert.assertThat(issues)
                .hasSize(2)
                .hasNumberOfFiles(1)
                .hasLowPrioritySize(0)
                .hasNormalPrioritySize(0)
                .hasHighPrioritySize(2);
    }

    @Test
    void testRemoveElementFromListWithOneElement(){
        Issues issues = new Issues();
        issues.add(ISSUE_1);
        int expectedSize = 0;
        UUID id = ISSUE_1.getId();

        Issue removedIssue = issues.remove(id);

        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        softly.assertThat(issues).hasSize(expectedSize);
        softly.assertThat(removedIssue).hasUuid(id);
        softly.assertThat(removedIssue).isEqualTo(ISSUE_1);
        softly.assertAll();
    }

    @Test
    void testRemoveElementFromListWithTwoEments(){
        Issues issues = new Issues();
        issues.add(ISSUE_1);
        issues.add(ISSUE_2);
        int expectedSize = 1;
        UUID id = ISSUE_1.getId();

        Issue removedIssue = issues.remove(id);

        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        softly.assertThat(issues).hasSize(expectedSize);
        softly.assertThat(removedIssue).hasUuid(id);
        softly.assertAll();
    }

    @Test
    void testRemoveElementNotExisting(){
        Issues issues = new Issues();

        assertThatThrownBy(() -> issues.remove(ISSUE_1.getId()))
                .isInstanceOf(NoSuchElementException.class)
                .hasNoCause()
                .hasMessageContaining(ISSUE_1.getId().toString());
    }

    @Test
    void testRemoveDuplicate(){
        Issues issues = new Issues();
        List<Issue> issuesList = new ArrayList<>();
        issuesList.add(ISSUE_1);
        issuesList.add(ISSUE_1);
        issues.addAll(issuesList);

        issues.remove(ISSUE_1.getId());

        Assertions.assertThat(issues).containsOnly(ISSUE_1);
        IssuesAssert.assertThat(issues).hasSize(1);
        IssuesAssert.assertThat(issues).hasNumberOfFiles(1);
        IssuesAssert.assertThat(issues).hasHighPrioritySize(1);
    }

    @Test
    void testFindElementById(){
        Issues issues = new Issues();
        issues.add(ISSUE_1);
        issues.add(ISSUE_2);
        int expectedSize = 2;
        UUID id = ISSUE_1.getId();

        Issue foundIssue = issues.findById(id);

        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        softly.assertThat(issues).hasSize(expectedSize);
        softly.assertThat(foundIssue).hasUuid(id);
        softly.assertAll();
    }

    @Test
    void testFindSingleIssueByIdNotExisting() {
        Issues issues = new Issues();
        issues.add(ISSUE_1);

        assertThatThrownBy(() -> issues.findById(ISSUE_2.getId()))
                .isInstanceOf(NoSuchElementException.class)
                .hasNoCause()
                .hasMessageContaining(ISSUE_2.getId().toString());
    }

    @Test
    void testFindNoIssueByProperty() {
        Issues issues = new Issues();
        List<Issue> issuesList = new ArrayList<>();
        issuesList.add(ISSUE_1);
        issuesList.add(ISSUE_2);
        issuesList.add(ISSUE_3);
        issues.addAll(issuesList);

        ImmutableList<Issue> found = issues.findByProperty(issue -> Objects.equals(issue.getPriority(), Priority.LOW));
        Issues foundIssues = new Issues();
        foundIssues.addAll(found.asList());
        Assertions.assertThat(foundIssues).isEmpty();
    }

    @Test
    void testFindByPropertyResultImmutable() {
        Issues issues = new Issues();
        List<Issue> issuesList = new ArrayList<>();
        issuesList.add(ISSUE_1);
        issuesList.add(ISSUE_2);
        issuesList.add(ISSUE_3);
        issues.addAll(issuesList);
        ImmutableList<Issue> found = issues.findByProperty(issue -> Objects.equals(issue.getPriority(), Priority.HIGH));

        //noinspection deprecation
        assertThatThrownBy(() -> found.add(ISSUE_4))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasNoCause();
    }

    @Test
    void testIterator() {
        Issues issues = new Issues();
        List<Issue> issuesList = new ArrayList<>();
        issuesList.add(ISSUE_1);
        issuesList.add(ISSUE_2);
        issuesList.add(ISSUE_3);
        issues.addAll(issuesList);

        Assertions.assertThat((Iterable<Issue>)issues).containsExactly(ISSUE_1, ISSUE_2, ISSUE_3);
    }

    @Test
    void testGet() {
        Issues issues = new Issues();
        List<Issue> issuesList = new ArrayList<>();
        issuesList.add(ISSUE_1);
        issuesList.add(ISSUE_2);
        issuesList.add(ISSUE_3);
        issues.addAll(issuesList);

        IssueAssert.assertThat(issues.get(0)).isEqualTo(ISSUE_1);
        IssueAssert.assertThat(issues.get(1)).isEqualTo(ISSUE_2);
        IssueAssert.assertThat(issues.get(2)).isEqualTo(ISSUE_3);
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
        List<Issue> issuesList = new ArrayList<>();
        issuesList.add(ISSUE_1);
        issuesList.add(ISSUE_2);
        issuesList.add(ISSUE_3);
        issuesList.add(ISSUE_4);
        issuesList.add(ISSUE_5);
        issuesList.add(ISSUE_6);
        issues.addAll(issuesList);

        Assertions.assertThat(issues.getFiles()).contains("file-1", "file-1", "file-3");
    }

    @Test
    void testToString() {
        Issues issues = new Issues();
        List<Issue> issuesList = new ArrayList<>();
        issuesList.add(ISSUE_1);
        issuesList.add(ISSUE_2);
        issuesList.add(ISSUE_3);
        issues.addAll(issuesList);

        Assertions.assertThat(issues.toString()).contains("3");
    }

    @Test
    void testGetProperties() {
        Issues issues = new Issues();
        List<Issue> issuesList = new ArrayList<>();
        issuesList.add(ISSUE_1);
        issuesList.add(ISSUE_2);
        issuesList.add(ISSUE_3);
        issuesList.add(ISSUE_4);
        issuesList.add(ISSUE_5);
        issuesList.add(ISSUE_6);
        issues.addAll(issuesList);

        SortedSet<String> properties = issues.getProperties(issue -> issue.getMessage());

        Assertions.assertThat(properties)
                .contains(ISSUE_1.getMessage())
                .contains(ISSUE_2.getMessage())
                .contains(ISSUE_3.getMessage());
    }

    @Test
    void testCopy() {
        Issues original = new Issues();
        List<Issue> issuesList = new ArrayList<>();
        issuesList.add(ISSUE_1);
        issuesList.add(ISSUE_2);
        issuesList.add(ISSUE_3);
        original.addAll(issuesList);

        Issues copy = original.copy();

        Assertions.assertThat(copy).isNotSameAs(original);
        Assertions.assertThat(copy.all()).contains(ISSUE_1, ISSUE_2, ISSUE_3);

        copy.add(ISSUE_4);
        Assertions.assertThat(original.all()).contains(ISSUE_1, ISSUE_2, ISSUE_3);
        Assertions.assertThat(copy.all()).contains(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4);
    }
}
