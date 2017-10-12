package edu.hm.hafner.analysis;

import java.util.List;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.assertj.AnalysisSoftAssertions;
import static edu.hm.hafner.analysis.assertj.IssuesAssert.assertThat;
import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.assertThat;

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
        softly.assertThat(issues.get(0)).isEqualTo(ISSUE_1);
        softly.assertThat(issues.get(1)).isEqualTo(ISSUE_2);
        softly.assertThat(issues.get(2)).isEqualTo(ISSUE_3);
        softly.assertThat(issues.get(3)).isEqualTo(ISSUE_4);
        softly.assertThat(issues.get(4)).isEqualTo(ISSUE_5);
        softly.assertThat(issues.get(5)).isEqualTo(ISSUE_6);
        softly.assertAll();
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
}