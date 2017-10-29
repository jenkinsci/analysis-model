package edu.hm.hafner.analysis;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.assertions.IssuesAssert.*;
import static edu.hm.hafner.analysis.assertions.IssueAssert.*;
import static org.assertj.core.api.Assertions.*;
import edu.hm.hafner.util.NoSuchElementException;

/**
 * Testing Class for Issues.
 *
 * @author Mark Tripolt
 */
class IssuesTest {

    @Test
    void addIssueToIssues() {
        Issues issues = new Issues();
        Issue issue = new IssueBuilder().build();
        issues.add(issue);

        assertThat(issues)
                .hasSize(1)
                .hasHighPrioritySize(0)
                .hasLowPrioritySize(0)
                .hasNormalPrioritySize(1)
                .hasIssueOnIndex(issue, 0)
                .hasNumberOfFiles(1)
                .hasToString(String.format("%d issues", issues.size()));

        assertThat(issues.get(0))
                .hasFileName("-")
                .hasPriority(Priority.NORMAL)
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasType("-")
                .hasColumnStart(0)
                .hasColumnEnd(0)
                .hasCategory("")
                .hasMessage("")
                .hasPackageName("-")
                .hasDescription("")
                .hasToString(String.format("-(0,0): -: : "));

    }

    @Test
    void testCopy() {
        Issue issueLow = new IssueBuilder()
                .setFileName("a")
                .setPriority(Priority.LOW)
                .build();
        Issue issueNorm = new IssueBuilder()
                .setFileName("b")
                .setPriority(Priority.NORMAL)
                .build();
        Issues issues = new Issues();
        issues.add(issueLow);
        issues.add(issueNorm);
        Issues issues2 = issues.copy();

        assertThat(issues)
                .hasSize(2)
                .hasLowPrioritySize(1)
                .hasNormalPrioritySize(1);
        assertThat(issues2)
                .hasSize(2)
                .hasLowPrioritySize(1)
                .hasNormalPrioritySize(1);


    }

    @Test
    void testNumberOfFiles() {
        Issue issueLow = new IssueBuilder()
                .setFileName("a")
                .setPriority(Priority.LOW)
                .build();
        Issue issueNorm = new IssueBuilder()
                .setFileName("b")
                .setPriority(Priority.NORMAL)
                .build();
        Issues issues = new Issues();
        issues.add(issueLow);
        issues.add(issueNorm);
        assertThat(issues).hasNumberOfFiles(2);
    }

    @Test
    void testFiles() {
        Issue issueLow = new IssueBuilder()
                .setFileName("a")
                .setPriority(Priority.LOW)
                .build();
        Issue issueNorm = new IssueBuilder()
                .setFileName("b")
                .setPriority(Priority.NORMAL)
                .build();
        Issues issues = new Issues();
        SortedSet<String> set = new TreeSet<>();
        set.add("a");
        set.add("b");
        issues.add(issueLow);
        issues.add(issueNorm);
        assertThat(issues).hasFiles(set);
    }

    @Test
    void testFindByIdNotFound() {
        Issue issueLow = new IssueBuilder()
                .setPriority(Priority.LOW)
                .build();
        Issue issueNorm = new IssueBuilder()
                .setPriority(Priority.NORMAL)
                .build();
        Issues issues = new Issues();
        UUID id = UUID.randomUUID();

        issues.add(issueLow);
        issues.add(issueNorm);

        assertThat(issues)
                .hasSize(2)
                .hasLowPrioritySize(1)
                .hasNormalPrioritySize(1);

        assertThatThrownBy(() -> issues.findById(id))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("No issue found with id " + id + ".")
                .hasNoCause();
    }

    @Test
    void testFindById() {
        Issue issueLow = new IssueBuilder()
                .setPriority(Priority.LOW)
                .build();
        Issue issueNorm = new IssueBuilder()
                .setPriority(Priority.NORMAL)
                .build();
        Issues issues = new Issues();
        UUID id = issueLow.getId();

        issues.add(issueLow);
        issues.add(issueNorm);

        assertThat(issues)
                .hasSize(2)
                .hasLowPrioritySize(1)
                .hasNormalPrioritySize(1);

        assertThat(issues.findById(id))
                .hasPriority(Priority.LOW);
    }

    @Test
    void testAddAll() {
        Issue issueLow = new IssueBuilder()
                .setFileName("b")
                .setPriority(Priority.LOW)
                .build();
        Issue issueHigh1 = new IssueBuilder()
                .setFileName("a")
                .setPriority(Priority.HIGH)
                .build();
        Issue issueHigh2 = new IssueBuilder()
                .setFileName("c")
                .setPriority(Priority.HIGH)
                .build();
        Issues issues = new Issues();
        Issues issues2 = new Issues();

        issues.add(issueLow);
        issues.add(issueHigh1);
        issues.add(issueHigh2);

        issues2.addAll(issues.all());

        assertThat(issues)
                .hasSize(3)
                .hasLowPrioritySize(1)
                .hasNormalPrioritySize(0)
                .hasHighPrioritySize(2);

        assertThat(issues2)
                .hasSize(3)
                .hasLowPrioritySize(1)
                .hasNormalPrioritySize(0)
                .hasHighPrioritySize(2);


    }

    @Test
    void testRemoveIssue() {
        Issue issueLow = new IssueBuilder()
                .setPriority(Priority.LOW)
                .build();
        Issue issueNorm = new IssueBuilder()
                .setPriority(Priority.NORMAL)
                .build();
        Issues issues = new Issues();
        UUID id = issueLow.getId();

        issues.add(issueLow);
        issues.add(issueNorm);

        assertThat(issues)
                .hasSize(2)
                .hasLowPrioritySize(1)
                .hasNormalPrioritySize(1);

        issues.remove(id);

        assertThat(issues)
                .hasSize(1)
                .hasLowPrioritySize(0)
                .hasNormalPrioritySize(1);
    }

    @Test
    void testRemoveNonExistingIssue() {
        Issue issueLow = new IssueBuilder()
                .setPriority(Priority.LOW)
                .build();
        Issue issueNorm = new IssueBuilder()
                .setPriority(Priority.NORMAL)
                .build();
        Issues issues = new Issues();
        UUID id = UUID.randomUUID();

        issues.add(issueLow);
        issues.add(issueNorm);

        assertThat(issues)
                .hasSize(2)
                .hasLowPrioritySize(1)
                .hasNormalPrioritySize(1);

        assertThatThrownBy(() -> issues.remove(id))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("No issue found with id " + id + ".")
                .hasNoCause();

        assertThat(issues)
                .hasSize(2)
                .hasLowPrioritySize(1)
                .hasNormalPrioritySize(1);
    }

    @Test
    void testEmptyIssues() {
        Issues issues = new Issues();
        assertThat(issues)
                .hasSize(0)
                .hasLowPrioritySize(0)
                .hasNormalPrioritySize(0)
                .hasHighPrioritySize(0)
                .hasNumberOfFiles(0);
    }

    @Test
    void testIssuesPriority() {
        Issue issueLow = new IssueBuilder()
                .setPriority(Priority.LOW)
                .build();
        Issue issueNorm = new IssueBuilder()
                .setPriority(Priority.NORMAL)
                .build();
        Issue issueHigh = new IssueBuilder()
                .setPriority(Priority.HIGH)
                .build();
        Issues issues = new Issues();

        issues.add(issueLow);
        issues.add(issueNorm);
        issues.add(issueNorm);
        issues.add(issueHigh);
        issues.add(issueHigh);
        issues.add(issueHigh);

        assertThat(issues)
                .hasSize(6)
                .hasLowPrioritySize(1)
                .hasNormalPrioritySize(2)
                .hasHighPrioritySize(3);

    }

    @Test
    void findByPropertyTest() {
        Issues issues1 = new Issues();
        Issues issues2 = new Issues();

        Issue issueLow = new IssueBuilder()
                .setFileName("a")
                .setPriority(Priority.LOW)
                .build();
        Issue issueNorm1 = new IssueBuilder()
                .setFileName("b")
                .setPriority(Priority.NORMAL)
                .build();
        Issue issueNorm2 = new IssueBuilder()
                .setFileName("c")
                .setPriority(Priority.NORMAL)
                .build();

        issues1.add(issueLow);
        issues1.add(issueNorm1);
        issues1.add(issueNorm2);

        issues2.addAll(issues1.findByProperty(n -> n.getPriority() == Priority.NORMAL));
        assertThat(issues2).hasSize(2).hasNormalPrioritySize(2).hasLowPrioritySize(0);


    }
}