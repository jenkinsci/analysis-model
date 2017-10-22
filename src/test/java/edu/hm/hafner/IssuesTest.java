package edu.hm.hafner;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class IssuesTest {
    private Issue i1;
    private Issue i2;
    private Issue i3;

    /**
     * init three Issue's befor eacht test.
     */
    @BeforeEach
    void initIssues() {
        i1 = new IssueBuilder().setPriority(Priority.HIGH).setType("t1").setPackageName("p1").setMessage("m1").setFileName("f1").build();
        i2 = new IssueBuilder().setPriority(Priority.LOW).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f2").build();
        i3 = new IssueBuilder().setPriority(Priority.NORMAL).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f3").build();
    }

    /**
     * generate a default Issues collection with three issues.
     */
    private Issues getDefaultIssues() {
        Issues i = new Issues();
        ArrayList<Issue> l = new ArrayList<>();
        l.add(i1);
        l.add(i2);
        l.add(i3);
        i.addAll(l);
        return i;
    }

    /**
     * add two issue's with Normal Prio.
     */
    @Test
    void addTwoNormalPrioIsues() {
        i1 = new IssueBuilder().setPriority(Priority.NORMAL).setType("t1").setPackageName("p1").setMessage("m1").setFileName("f1").build();
        i2 = new IssueBuilder().setPriority(Priority.NORMAL).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f2").build();

        Issues i = new Issues();
        IssuesAssert.assertThat(i).hasSize(0).hasHighPrioritySize(0).hasLowPrioritySize(0).hasNormalPrioritySize(0);
        i.add(i1);
        IssuesAssert.assertThat(i).hasSize(1).hasHighPrioritySize(0).hasLowPrioritySize(0).hasNormalPrioritySize(1);
        i.add(i2);
        IssuesAssert.assertThat(i).hasSize(2).hasHighPrioritySize(0).hasLowPrioritySize(0).hasNormalPrioritySize(2);
    }

    /**
     * add two issue's with Low Prio.
     */
    @Test
    void addTwoLowPrioIsues() {
        Issues i = new Issues();
        i1 = new IssueBuilder().setPriority(Priority.LOW).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f2").build();
        i2 = new IssueBuilder().setPriority(Priority.LOW).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f2").build();

        IssuesAssert.assertThat(i).hasSize(0).hasHighPrioritySize(0).hasLowPrioritySize(0).hasNormalPrioritySize(0);
        i.add(i1);
        IssuesAssert.assertThat(i).hasSize(1).hasHighPrioritySize(0).hasLowPrioritySize(1).hasNormalPrioritySize(0);
        i.add(i2);
        IssuesAssert.assertThat(i).hasSize(2).hasHighPrioritySize(0).hasLowPrioritySize(2).hasNormalPrioritySize(0);
    }

    /**
     * add two issue's with High Prio.
     */
    @Test
    void addTwoHighrioIsues() {
        i1 = new IssueBuilder().setPriority(Priority.HIGH).setType("t1").setPackageName("p1").setMessage("m1").setFileName("f1").build();
        i2 = new IssueBuilder().setPriority(Priority.HIGH).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f2").build();
        Issues i = new Issues();
        IssuesAssert.assertThat(i).hasSize(0).hasHighPrioritySize(0).hasLowPrioritySize(0).hasNormalPrioritySize(0);
        i.add(i1);
        IssuesAssert.assertThat(i).hasSize(1).hasHighPrioritySize(1).hasLowPrioritySize(0).hasNormalPrioritySize(0);
        i.add(i2);
        IssuesAssert.assertThat(i).hasSize(2).hasHighPrioritySize(2).hasLowPrioritySize(0).hasNormalPrioritySize(0);
    }

    /**
     * add two issue's with Mixed Prio's.
     */
    @Test
    void addMixedPrioIsues() {
        Issues i = new Issues();
        IssuesAssert.assertThat(i).hasSize(0).hasHighPrioritySize(0).hasLowPrioritySize(0).hasNormalPrioritySize(0);
        i.add(i1);
        IssuesAssert.assertThat(i).hasSize(1).hasHighPrioritySize(1).hasLowPrioritySize(0).hasNormalPrioritySize(0);
        i.add(i2);
        IssuesAssert.assertThat(i).hasSize(2).hasHighPrioritySize(1).hasLowPrioritySize(1).hasNormalPrioritySize(0);
        i.add(i3);
        IssuesAssert.assertThat(i).hasSize(3).hasHighPrioritySize(1).hasLowPrioritySize(1).hasNormalPrioritySize(1);
    }

    /**
     * Test AddAll Method.
     */
    @Test
    void addAllIssues() {
        Issues ix = new Issues();
        Issues iy = new Issues();
        ix.add(i1);
        iy.add(i2);
        ix.addAll(iy.all());
        IssuesAssert.assertThat(ix).contains(i1).contains(i2);
        IssuesAssert.assertThat(iy).containsNot(i1).contains(i2);
    }

    /**
     * Test findByUuid Method.
     */
    @Test
    void findByUUID() {
        Issues i = getDefaultIssues();
        assertThat(i.findById(i1.getId())).isEqualTo(i1);
        assertThat(i.findById(i2.getId())).isEqualTo(i2);
        assertThat(i.findById(i3.getId())).isEqualTo(i3);
    }

    /**
     * Test remove Method.
     */
    @Test
    void removeByUUID() {
        Issues i = new Issues();
        assertThatThrownBy(() -> i.remove(i3.getId())).isInstanceOf(NoSuchElementException.class).hasMessage("No issue found with id " + i3.getId() + ".");
        i.add(i1);
        i.add(i2);
        assertThat(i.findById(i1.getId())).isEqualTo(i1);
        assertThat(i.findById(i2.getId())).isEqualTo(i2);
        assertThat(i.remove(i2.getId())).isEqualTo(i2);
        assertThatThrownBy(() -> i.findById(i2.getId())).isInstanceOf(NoSuchElementException.class).hasMessage("No issue found with id " + i2.getId() + ".");
        assertThat(i.getLowPrioritySize()).isEqualTo(0);
    }

    /**
     * Test findByID Method.
     */
    @Test
    void findIssueByUUID() {
        Issues ix = new Issues();
        Issues i = getDefaultIssues();
        assertThatThrownBy(() -> ix.findById(i1.getId())).hasMessage("No issue found with id " + i1.getId() + ".");

        IssueAssert.assertThat(i.findById(i1.getId())).isEqualTo(i1);
        IssueAssert.assertThat(i.findById(i2.getId())).isEqualTo(i2);
        IssueAssert.assertThat(i.findById(i3.getId())).isEqualTo(i3);
    }

    /**
     * Test addAll Method.
     */
    @Test
    void addAllFromCollection() {
        Issues iy = new Issues();
        Issues ix = getDefaultIssues();
        ix.add(i3);
        iy.addAll(ix.all());
        assertThat(ix.all()).isEqualTo(iy.all());
    }

    /**
     * Test all Method.
     */
    @Test
    void getAllIssues() {
        Issues ix = getDefaultIssues();

        ImmutableList<Issue> l = ix.all().asList();
        assertThat(l.size()).isEqualTo(3);
        IssueAssert.assertThat(l.get(0)).isEqualTo(i1);
        IssueAssert.assertThat(l.get(1)).isEqualTo(i2);
        IssueAssert.assertThat(l.get(2)).isEqualTo(i3);
        assertThatThrownBy(() -> l.get(3)).isInstanceOf(ArrayIndexOutOfBoundsException.class).hasMessage("3");
    }

    /**
     * Test all Method and change the values.
     */
    @Test
    void getAllIssuesAndChangeThem() {
        Issues issues = new Issues();
        issues.add(i1);
        ImmutableList<Issue> list = issues.all().asList();
        list.get(0).setFingerprint("ixxx1");
        assertThat(list.get(0).getFingerprint()).isEqualTo("ixxx1");
        Issue x = issues.findById(i1.getId());
        assertThat(x.getFingerprint()).isEqualTo("ixxx1");
    }

    /**
     * Test remove Method with not existing element.
     */
    @Test
    void removeElementWichDoesNotExist() {
        Issues issues = new Issues();
        assertThatThrownBy(() -> issues.findById(i1.getId())).isInstanceOf(NoSuchElementException.class).hasMessage("No issue found with id " + i1.getId() + ".");
    }

    /**
     * Test integrator Method.
     */
    @Test
    void getItterator() {
        Issues ix = getDefaultIssues();
        Iterator<Issue> i = ix.iterator();
        assertThat(i.hasNext()).isTrue();
        assertThat(i.next()).isEqualTo(i1);
        assertThat(i.hasNext()).isTrue();
        assertThat(i.next()).isEqualTo(i2);
        assertThat(i.hasNext()).isTrue();
        assertThat(i.next()).isEqualTo(i3);
        assertThat(i.hasNext()).isFalse();
        assertThatThrownBy(() -> i.next()).isInstanceOf(java.util.NoSuchElementException.class);
    }

    /**
     * Test get Method.
     */
    @Test
    void getIndex() {
        Issues ix = getDefaultIssues();
        Issues iy = new Issues();
        assertThatThrownBy(() -> iy.get(0)).isInstanceOf(IndexOutOfBoundsException.class).hasMessage("Index: 0, Size: 0");
        IssueAssert.assertThat(ix.get(0)).isEqualTo(i1);
        IssueAssert.assertThat(ix.get(1)).isEqualTo(i2);
        IssueAssert.assertThat(ix.get(2)).isEqualTo(i3);
        assertThatThrownBy(() -> ix.get(3)).isInstanceOf(IndexOutOfBoundsException.class).hasMessage("Index: 3, Size: 3");
        assertThatThrownBy(() -> ix.get(-1)).isInstanceOf(IndexOutOfBoundsException.class).hasMessage("-1");
        ix.remove(i3.getId());
        assertThatThrownBy(() -> ix.get(2)).isInstanceOf(IndexOutOfBoundsException.class).hasMessage("Index: 2, Size: 2");
    }

    /**
     * Test toString Method.
     */
    @Test
    void checkToString() {
        Issues ix = new Issues();
        assertThat(ix.toString()).isEqualTo("0 issues");
        ix.add(i1);
        assertThat(ix.toString()).isEqualTo("1 issues");
        ix.add(i2);
        ix.add(i3);
        assertThat(ix.toString()).isEqualTo("3 issues");
    }

    /**
     * Test numberOfFiles Method.
     */
    @Test
    void numberOfFiles() {
        Issues ix = new Issues();
        assertThat(ix.getNumberOfFiles()).isEqualTo(0);
        ix.add(i1);
        assertThat(ix.getNumberOfFiles()).isEqualTo(1);
        ix.add(i2);
        assertThat(ix.getNumberOfFiles()).isEqualTo(2);
        ix.add(i2);
        assertThat(ix.getNumberOfFiles()).isEqualTo(2);
    }

    /**
     * Test copy Method.
     */
    @Test
    void copyIssues() {
        Issues ix = new Issues();
        ix.add(i1);
        ix.add(i2);
        Issues iy = ix.copy();
        IssuesAssert.assertThat(iy).isEqualTo(ix);
        ix.add(i3);
        IssuesAssert.assertThat(iy).isNotEqualTo(ix);
    }

    /**
     * Test findByProperty Method.
     */
    @Test
    void propertyFinder() {
        Issues ix = getDefaultIssues();
        ImmutableList<Issue> s = ix.findByProperty((issue) -> issue.getPackageName().equals("p1"));
        assertThat(s.size()).isEqualTo(1);
        s = ix.findByProperty((issue) -> issue.getPackageName().contains("p"));
        assertThat(s.size()).isEqualTo(3);
        s = ix.findByProperty((issue) -> issue.getPriority().equals(Priority.HIGH));
        assertThat(s.size()).isEqualTo(1);
        IssueAssert.assertThat(s.get(0)).isEqualTo(i1);
    }

    /**
     * Test getFileName.
     */
    @Test
    void getFileNames() {
        Issues i = getDefaultIssues();
        assertThat(i.getFiles().size()).isEqualTo(3);
        Issue i4 = new IssueBuilder().copy(i3).setMessage("m4").setLineEnd(99).build();
        i.add(i4);
        assertThat(i.getFiles().size()).isEqualTo(3);
        assertThat(i.all().size()).isEqualTo(4);
    }

    /**
     * Test add the same issue
     * What happepen if i add the same Issue twice...
     */
    @Test
    void addTheSameIssue() {
        Issues i = getDefaultIssues();
        Issue coppy = i.add(new IssueBuilder().copy(i1).build());
        assertThatThrownBy(() -> i.add(coppy)).isInstanceOf(Exception.class);
    }

}