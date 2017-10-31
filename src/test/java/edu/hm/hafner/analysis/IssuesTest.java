package edu.hm.hafner.analysis;

import com.google.common.collect.ImmutableList;

import edu.hm.hafner.util.NoSuchElementException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Test Cases for Issues.
 *
 * @author Sebastian Balz
 */
class IssuesTest {


    /**
     * add two issue's of each prio to Issues.
     */
    @Test
    void addofEachPrioTwoToIsues() {
        Issue firstNormalPriorityIssue = new IssueBuilder().setPriority(Priority.NORMAL).setType("t1").setPackageName("p1").setMessage("m1").setFileName("f1").build();
        Issue secondNormalPriorityIssue = new IssueBuilder().setPriority(Priority.NORMAL).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f2").build();
        Issue firstLowIssue = new IssueBuilder().setPriority(Priority.LOW).setType("t1").setPackageName("p1").setMessage("m1").setFileName("f1").build();
        Issue secondLowIssue = new IssueBuilder().setPriority(Priority.LOW).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f2").build();
        Issue firstHighIssue = new IssueBuilder().setPriority(Priority.HIGH).setType("t1").setPackageName("p1").setMessage("m1").setFileName("f1").build();
        Issue secondHighIssue = new IssueBuilder().setPriority(Priority.HIGH).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f2").build();
        Issues issues = new Issues();
        IssuesAssert.assertThat(issues).hasSize(0).hasHighPrioritySize(0).hasLowPrioritySize(0).hasNormalPrioritySize(0);
        issues.add(firstNormalPriorityIssue);
        IssuesAssert.assertThat(issues).hasSize(1).hasHighPrioritySize(0).hasLowPrioritySize(0).hasNormalPrioritySize(1);
        issues.add(secondNormalPriorityIssue);
        IssuesAssert.assertThat(issues).hasSize(2).hasHighPrioritySize(0).hasLowPrioritySize(0).hasNormalPrioritySize(2);
        issues.add(firstLowIssue);
        IssuesAssert.assertThat(issues).hasSize(3).hasHighPrioritySize(0).hasLowPrioritySize(1).hasNormalPrioritySize(2);
        issues.add(secondLowIssue);
        IssuesAssert.assertThat(issues).hasSize(4).hasHighPrioritySize(0).hasLowPrioritySize(2).hasNormalPrioritySize(2);
        issues.add(firstHighIssue);
        IssuesAssert.assertThat(issues).hasSize(5).hasHighPrioritySize(1).hasLowPrioritySize(2).hasNormalPrioritySize(2);
        issues.add(secondHighIssue);
        IssuesAssert.assertThat(issues).hasSize(6).hasHighPrioritySize(2).hasLowPrioritySize(2).hasNormalPrioritySize(2);

    }


    /**
     * Test AddAll Method.
     */
    @Test
    void addAllIssues() {
        Issue firstIssue = new IssueBuilder().setPriority(Priority.HIGH).setType("t1").setPackageName("p1").setMessage("m1").setFileName("f1").build();
        Issue secondIssue = new IssueBuilder().setPriority(Priority.LOW).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f2").build();
        Issues issuesX = new Issues();
        Issues issuesY = new Issues();
        issuesX.add(firstIssue);
        issuesY.add(secondIssue);
        issuesX.addAll(issuesY.all());
        IssuesAssert.assertThat(issuesX).contains(firstIssue).contains(secondIssue);
        IssuesAssert.assertThat(issuesY).containsNot(firstIssue).contains(secondIssue);
    }

    /**
     * Test findByUuid Method.
     */
    @Test
    void findByUUID() {
        Issue firstIssue = new IssueBuilder().setPriority(Priority.HIGH).setType("t1").setPackageName("p1").setMessage("m1").setFileName("f1").build();
        Issue secondIssue = new IssueBuilder().setPriority(Priority.LOW).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f2").build();
        Issue thirdIssue = new IssueBuilder().setPriority(Priority.NORMAL).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f3").build();
        Issues issues1 = new Issues();
        ArrayList<Issue> l = new ArrayList<>();
        l.add(firstIssue);
        l.add(secondIssue);
        l.add(thirdIssue);
        issues1.addAll(l);
        assertThat(issues1.findById(firstIssue.getId())).isEqualTo(firstIssue);
        assertThat(issues1.findById(secondIssue.getId())).isEqualTo(secondIssue);
        assertThat(issues1.findById(thirdIssue.getId())).isEqualTo(thirdIssue);
    }

    //TODO redundant

    /**
     * Test remove Method.
     */
    @Test
    void removeByUUID() {
        Issue firstIssue = new IssueBuilder().setPriority(Priority.HIGH).setType("t1").setPackageName("p1").setMessage("m1").setFileName("f1").build();
        Issue secondIssue = new IssueBuilder().setPriority(Priority.LOW).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f2").build();
        Issue thirdIssue = new IssueBuilder().setPriority(Priority.NORMAL).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f3").build();
        Issues issues = new Issues();
        assertThatThrownBy(() -> issues.remove(thirdIssue.getId())).isInstanceOf(NoSuchElementException.class).hasMessage("No issue found with id " + thirdIssue.getId() + ".");
        issues.add(firstIssue);
        issues.add(secondIssue);
        assertThat(issues.findById(firstIssue.getId())).isEqualTo(firstIssue);
        assertThat(issues.findById(secondIssue.getId())).isEqualTo(secondIssue);
        assertThat(issues.remove(secondIssue.getId())).isEqualTo(secondIssue);
        assertThatThrownBy(() -> issues.findById(secondIssue.getId())).isInstanceOf(NoSuchElementException.class).hasMessage("No issue found with id " + secondIssue.getId() + ".");
        assertThat(issues.getLowPrioritySize()).isEqualTo(0);
        assertThatThrownBy(() -> issues.remove(secondIssue.getId())).isInstanceOf(NoSuchElementException.class).hasMessage("No issue found with id " + secondIssue.getId() + ".");
    }
    //TODO redundant

    /**
     * Test findByID Method.
     */
    @Test
    void findIssueByUUID() {
        Issue firstIssue = new IssueBuilder().setPriority(Priority.HIGH).setType("t1").setPackageName("p1").setMessage("m1").setFileName("f1").build();
        Issue secondIssue = new IssueBuilder().setPriority(Priority.LOW).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f2").build();
        Issue thirdIssue = new IssueBuilder().setPriority(Priority.NORMAL).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f3").build();
        Issues issues1 = new Issues();
        ArrayList<Issue> l = new ArrayList<>();
        // l.add(firstIssue);  expectet not to be in Issues
        l.add(secondIssue);
        l.add(thirdIssue);
        issues1.addAll(l);
        assertThatThrownBy(() -> issues1.findById(firstIssue.getId())).hasMessage("No issue found with id " + firstIssue.getId() + ".");
        IssueAssert.assertThat(issues1.findById(secondIssue.getId())).isEqualTo(secondIssue);
        IssueAssert.assertThat(issues1.findById(thirdIssue.getId())).isEqualTo(thirdIssue);
    }

    /**
     * Test addAll Method.
     */
    @Test
    void addAllFromCollection() {
        Issue firstIssue = new IssueBuilder().setPriority(Priority.HIGH).setType("t1").setPackageName("p1").setMessage("m1").setFileName("f1").build();
        Issue secondIssue = new IssueBuilder().setPriority(Priority.LOW).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f2").build();
        Issue thirdIssue = new IssueBuilder().setPriority(Priority.NORMAL).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f3").build();
        Issues issuesY = new Issues();
        Issues issues = new Issues();
        ArrayList<Issue> l = new ArrayList<>();
        l.add(firstIssue);
        l.add(secondIssue);
        l.add(thirdIssue);
        issues.addAll(l);
        issues.add(thirdIssue);
        issuesY.addAll(issues.all());
        assertThat(issues.all()).isEqualTo(issuesY.all());
    }

    /**
     * Test all Method.
     */
    @Test
    void getAllIssues() {
        Issue firstIssue = new IssueBuilder().setPriority(Priority.HIGH).setType("t1").setPackageName("p1").setMessage("m1").setFileName("f1").build();
        Issue secondIssue = new IssueBuilder().setPriority(Priority.LOW).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f2").build();
        Issue thirdIssue = new IssueBuilder().setPriority(Priority.NORMAL).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f3").build();
        Issues issues = new Issues();
        ArrayList<Issue> l1 = new ArrayList<>();
        l1.add(firstIssue);
        l1.add(secondIssue);
        l1.add(thirdIssue);
        issues.addAll(l1);

        ImmutableList<Issue> l = issues.all().asList();
        assertThat(l.size()).isEqualTo(3); //TODO  assertThat(l).hasSize(3)  with ImmutableList
        IssueAssert.assertThat(l.get(0)).isEqualTo(firstIssue);
        IssueAssert.assertThat(l.get(1)).isEqualTo(secondIssue);
        IssueAssert.assertThat(l.get(2)).isEqualTo(thirdIssue);
        assertThatThrownBy(() -> l.get(3)).isInstanceOf(ArrayIndexOutOfBoundsException.class).hasMessage("3");
    }

    //TODO Der Test wäre unnötig, wenn oben statt issue isEqualTo isSameAs verwendet würde.

    /**
     * Test all Method and change the values.
     */
    @Test
    void getAllIssuesAndChangeThem() {
        Issue firstIssue = new IssueBuilder().setPriority(Priority.HIGH).setType("t1").setPackageName("p1").setMessage("m1").setFileName("f1").build();
        Issues issues = new Issues();
        issues.add(firstIssue);
        ImmutableList<Issue> list = issues.all().asList();
        list.get(0).setFingerprint("issuesXxx1");
        assertThat(list.get(0).getFingerprint()).isEqualTo("issuesXxx1");
        Issue x = issues.findById(firstIssue.getId());
        assertThat(x.getFingerprint()).isEqualTo("issuesXxx1");
    }

    /**
     * Test remove Method with not existing element.
     */
    @Test
    void removeElementWhichDoesNotExist() {
        Issues issues = new Issues();
        Issue firstIssue = new IssueBuilder().setPriority(Priority.HIGH).setType("t1").setPackageName("p1").setMessage("m1").setFileName("f1").build();
        assertThatThrownBy(() -> issues.findById(firstIssue.getId())).isInstanceOf(NoSuchElementException.class).hasMessage("No issue found with id " + firstIssue.getId() + ".");
    }

    /**
     * Test integrator Method.
     */
    @Test
    void getIterator() {
        Issue firstIssue = new IssueBuilder().setPriority(Priority.HIGH).setType("t1").setPackageName("p1").setMessage("m1").setFileName("f1").build();
        Issue secondIssue = new IssueBuilder().setPriority(Priority.LOW).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f2").build();
        Issue thirdIssue = new IssueBuilder().setPriority(Priority.NORMAL).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f3").build();
        Issues issues1 = new Issues();
        ArrayList<Issue> l = new ArrayList<>();
        l.add(firstIssue);
        l.add(secondIssue);
        l.add(thirdIssue);
        issues1.addAll(l);
        IssuesAssert.assertThat(issues1).containsExactly(firstIssue, secondIssue, thirdIssue);

    }
    //TODO redundant

    /**
     * Test get Method.
     */
    @Test
    void getIssueAtIndex() {
        Issue firstIssue = new IssueBuilder().setPriority(Priority.HIGH).setType("t1").setPackageName("p1").setMessage("m1").setFileName("f1").build();
        Issue secondIssue = new IssueBuilder().setPriority(Priority.LOW).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f2").build();
        Issue thirdIssue = new IssueBuilder().setPriority(Priority.NORMAL).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f3").build();
        Issues issues = new Issues();
        ArrayList<Issue> l = new ArrayList<>();
        l.add(firstIssue);
        l.add(secondIssue);
        l.add(thirdIssue);
        issues.addAll(l);
        Issues issuesY = new Issues();
        assertThatThrownBy(() -> issuesY.get(0)).isInstanceOf(IndexOutOfBoundsException.class).hasMessage("Index: 0, Size: 0");
        IssueAssert.assertThat(issues.get(0)).isEqualTo(firstIssue);
        IssueAssert.assertThat(issues.get(1)).isEqualTo(secondIssue);
        IssueAssert.assertThat(issues.get(2)).isEqualTo(thirdIssue);
        assertThatThrownBy(() -> issues.get(3)).isInstanceOf(IndexOutOfBoundsException.class).hasMessage("Index: 3, Size: 3");
        assertThatThrownBy(() -> issues.get(-1)).isInstanceOf(IndexOutOfBoundsException.class).hasMessage("-1");
        issues.remove(thirdIssue.getId());
        assertThatThrownBy(() -> issues.get(2)).isInstanceOf(IndexOutOfBoundsException.class).hasMessage("Index: 2, Size: 2");
    }

    /**
     * Test toString Method.
     */
    @Test
    void checkToString() {
        Issue firstIssue = new IssueBuilder().setPriority(Priority.HIGH).setType("t1").setPackageName("p1").setMessage("m1").setFileName("f1").build();
        Issue secondIssue = new IssueBuilder().setPriority(Priority.LOW).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f2").build();
        Issue thirdIssue = new IssueBuilder().setPriority(Priority.NORMAL).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f3").build();
        Issues issues = new Issues();
        assertThat(issues.toString()).isEqualTo("0 issues");
        issues.add(firstIssue);
        assertThat(issues.toString()).isEqualTo("1 issues");
        issues.add(secondIssue);
        issues.add(thirdIssue);
        assertThat(issues.toString()).isEqualTo("3 issues");
    }

    /**
     * Test numberOfFiles Method.
     */
    @Test
    void checkTheNumberOfFiles() {
        Issue firstIssue = new IssueBuilder().setPriority(Priority.HIGH).setType("t1").setPackageName("p1").setMessage("m1").setFileName("f1").build();
        Issue secondIssue = new IssueBuilder().setPriority(Priority.LOW).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f2").build();
        Issue thirdIssue = new IssueBuilder().setPriority(Priority.NORMAL).setType("t3").setPackageName("p3").setMessage("m3").setFileName("f2").build();
        Issues issues = new Issues();
        assertThat(issues.getNumberOfFiles()).isEqualTo(0);
        issues.add(firstIssue);
        assertThat(issues.getNumberOfFiles()).isEqualTo(1);
        issues.add(secondIssue);
        assertThat(issues.getNumberOfFiles()).isEqualTo(2);
        issues.add(thirdIssue);
        assertThat(issues.getNumberOfFiles()).isEqualTo(2);
    }

    /**
     * Test copy Method.
     */
    @Test
    void copyIssuesandCompare() {
        Issue firstIssue = new IssueBuilder().setPriority(Priority.HIGH).setType("t1").setPackageName("p1").setMessage("m1").setFileName("f1").build();
        Issue secondIssue = new IssueBuilder().setPriority(Priority.LOW).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f2").build();
        Issue thirdIssue = new IssueBuilder().setPriority(Priority.NORMAL).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f3").build();
        Issues issuesX = new Issues();
        issuesX.add(firstIssue);
        issuesX.add(secondIssue);
        Issues issuesY = issuesX.copy();
        IssuesAssert.assertThat(issuesY).isEqualTo(issuesX);
        issuesX.add(thirdIssue);
        IssuesAssert.assertThat(issuesY).isNotEqualTo(issuesX);
    }

    /**
     * Test findByProperty Method.
     */
    @Test
    void propertyFinder() {
        Issue firstIssue = new IssueBuilder().setPriority(Priority.HIGH).setType("t1").setPackageName("p1").setMessage("m1").setFileName("f1").build();
        Issue secondIssue = new IssueBuilder().setPriority(Priority.LOW).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f2").build();
        Issue thirdIssue = new IssueBuilder().setPriority(Priority.NORMAL).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f3").build();
        Issues issues = new Issues();
        ArrayList<Issue> l = new ArrayList<>();
        l.add(firstIssue);
        l.add(secondIssue);
        l.add(thirdIssue);
        issues.addAll(l);
        ImmutableList<Issue> s = issues.findByProperty((issue) -> issue.getPackageName().equals("p1"));
        assertThat(s.size()).isEqualTo(1);
        s = issues.findByProperty((issue) -> issue.getPackageName().contains("p"));
        assertThat(s.size()).isEqualTo(3);
        s = issues.findByProperty((issue) -> issue.getPriority().equals(Priority.HIGH));
        assertThat(s.size()).isEqualTo(1);
        IssueAssert.assertThat(s.get(0)).isEqualTo(firstIssue);
    }

    /**
     * Test getFileName.
     */
    @Test
    void getFileNames() {
        Issue firstIssue = new IssueBuilder().setPriority(Priority.HIGH).setType("t1").setPackageName("p1").setMessage("m1").setFileName("f1").build();
        Issue secondIssue = new IssueBuilder().setPriority(Priority.LOW).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f2").build();
        Issue thirdIssue = new IssueBuilder().setPriority(Priority.NORMAL).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f3").build();
        Issues issues1 = new Issues();
        ArrayList<Issue> l = new ArrayList<>();
        l.add(firstIssue);
        l.add(secondIssue);
        l.add(thirdIssue);
        issues1.addAll(l);
        assertThat(issues1.getFiles().size()).isEqualTo(3);
        Issue i4 = new IssueBuilder().copy(thirdIssue).setMessage("m4").setLineEnd(99).build();
        issues1.add(i4);
        assertThat(issues1.getFiles().size()).isEqualTo(3);
        assertThat(issues1.all().size()).isEqualTo(4);
    }


    @Test
    void getPropertyUUID() {
        Issue firstIssue = new IssueBuilder().setPriority(Priority.HIGH).setType("t1").setPackageName("p1").setMessage("m1").setFileName("f1").build();
        Issue secondIssue = new IssueBuilder().setPriority(Priority.LOW).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f2").build();
        Issue thirdIssue = new IssueBuilder().setPriority(Priority.NORMAL).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f3").build();
        Issues issues1 = new Issues();
        ArrayList<Issue> l = new ArrayList<>();
        l.add(firstIssue);
        l.add(secondIssue);
        l.add(thirdIssue);
        issues1.addAll(l);
        SortedSet<UUID> s = issues1.getProperties(Issue::getId);
        assertThat(s.size()).isEqualTo(3);
        assertThat(s.contains(firstIssue.getId())).isTrue();
        assertThat(s.contains(secondIssue.getId())).isTrue();
        assertThat(s.contains(thirdIssue.getId())).isTrue();
    }
}