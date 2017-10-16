package edu.hm.balz;

import com.google.common.collect.ImmutableList;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class IssuesTest {
    @Test
    void addTwoNormalPrioIsues(){
        Issues i = new Issues();
        Issue i1= new IssueBuilder().setPriority(Priority.NORMAL).setType("t1").setPackageName("p1").setMessage("m1").setFileName("f1").build();
        Issue i2= new IssueBuilder().setPriority(Priority.NORMAL).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f2").build();
        IssuesAssert.assertThat(i).hasSize(0).hasHighPrioritySize(0).hasLowPrioritySize(0).hasNormalPrioritySize(0);
        i.add(i1);
        IssuesAssert.assertThat(i).hasSize(1).hasHighPrioritySize(0).hasLowPrioritySize(0).hasNormalPrioritySize(1);
        i.add(i2);
        IssuesAssert.assertThat(i).hasSize(2).hasHighPrioritySize(0).hasLowPrioritySize(0).hasNormalPrioritySize(2);

    }
    @Test
    void addTwoLowPrioIsues(){
        Issues i = new Issues();
        Issue i1= new IssueBuilder().setPriority(Priority.LOW).setType("t1").setPackageName("p1").setMessage("m1").setFileName("f1").build();
        Issue i2= new IssueBuilder().setPriority(Priority.LOW).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f2").build();
        IssuesAssert.assertThat(i).hasSize(0).hasHighPrioritySize(0).hasLowPrioritySize(0).hasNormalPrioritySize(0);
        i.add(i1);
        IssuesAssert.assertThat(i).hasSize(1).hasHighPrioritySize(0).hasLowPrioritySize(1).hasNormalPrioritySize(0);
        i.add(i2);
        IssuesAssert.assertThat(i).hasSize(2).hasHighPrioritySize(0).hasLowPrioritySize(2).hasNormalPrioritySize(0);

    }
    @Test
    void addTwoHighrioIsues(){
        Issues i = new Issues();
        Issue i1= new IssueBuilder().setPriority(Priority.HIGH).setType("t1").setPackageName("p1").setMessage("m1").setFileName("f1").build();
        Issue i2= new IssueBuilder().setPriority(Priority.HIGH).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f2").build();
        IssuesAssert.assertThat(i).hasSize(0).hasHighPrioritySize(0).hasLowPrioritySize(0).hasNormalPrioritySize(0);
        i.add(i1);
        IssuesAssert.assertThat(i).hasSize(1).hasHighPrioritySize(1).hasLowPrioritySize(0).hasNormalPrioritySize(0);
        i.add(i2);
        IssuesAssert.assertThat(i).hasSize(2).hasHighPrioritySize(2).hasLowPrioritySize(0).hasNormalPrioritySize(0);

    }
    @Test
    void addMixedPrioIsues(){
        Issues i = new Issues();
        Issue i1= new IssueBuilder().setPriority(Priority.HIGH).setType("t1").setPackageName("p1").setMessage("m1").setFileName("f1").build();
        Issue i2= new IssueBuilder().setPriority(Priority.LOW).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f2").build();
        Issue i3= new IssueBuilder().setPriority(Priority.NORMAL).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f2").build();
        IssuesAssert.assertThat(i).hasSize(0).hasHighPrioritySize(0).hasLowPrioritySize(0).hasNormalPrioritySize(0);
        i.add(i1);
        IssuesAssert.assertThat(i).hasSize(1).hasHighPrioritySize(1).hasLowPrioritySize(0).hasNormalPrioritySize(0);
        i.add(i2);
        IssuesAssert.assertThat(i).hasSize(2).hasHighPrioritySize(1).hasLowPrioritySize(1).hasNormalPrioritySize(0);
        i.add(i3);
        IssuesAssert.assertThat(i).hasSize(3).hasHighPrioritySize(1).hasLowPrioritySize(1).hasNormalPrioritySize(1);
    }

    //TODO  Exeptions
    @Test void addNullTOIssues(){
        Issue i1 = null;
        Issues i = new Issues();
       assertThatThrownBy(()->i.add(i1)).isInstanceOf(NullPointerException.class);
    }

    @Test void addAllIssues(){
        Issues ix = new Issues();
        Issues iy = new Issues();
        Issue i1= new IssueBuilder().setPriority(Priority.HIGH).setType("t1").setPackageName("p1").setMessage("m1").setFileName("f1").build();
        Issue i2= new IssueBuilder().setPriority(Priority.LOW).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f2").build();
        ix.add(i1);
        iy.add(i2);
        ix.addAll(iy.all());
        IssuesAssert.assertThat(ix).contains(i1).contains(i2);
        IssuesAssert.assertThat(iy).containsNot(i1).contains(i2);
    }

     @Test void findByUUID(){
         Issues i = new Issues();
         Issue i1= new IssueBuilder().setPriority(Priority.HIGH).setType("t1").setPackageName("p1").setMessage("m1").setFileName("f1").build();
         Issue i2= new IssueBuilder().setPriority(Priority.LOW).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f2").build();
         Issue i3= new IssueBuilder().setPriority(Priority.NORMAL).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f2").build();
         i.add(i1);
         i.add(i2);
         i.add(i3);
         assertThat(i.findById(i1.getId())).isEqualTo(i1);
         assertThat(i.findById(i2.getId())).isEqualTo(i2);
         assertThat(i.findById(i3.getId())).isEqualTo(i3);
     }

    @Test void removeByUUID(){
        Issues i = new Issues();
        Issue i1= new IssueBuilder().setPriority(Priority.HIGH).setType("t1").setPackageName("p1").setMessage("m1").setFileName("f1").build();
        Issue i2= new IssueBuilder().setPriority(Priority.LOW).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f2").build();
        Issue i3= new IssueBuilder().setPriority(Priority.NORMAL).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f2").build();
        i.add(i1);
        i.add(i2);
        assertThat(i.findById(i1.getId())).isEqualTo(i1);
        assertThat(i.findById(i2.getId())).isEqualTo(i2);
        assertThat(i.remove(i1.getId())).isEqualTo(i1);
        assertThatThrownBy(()->i.findById(i1.getId())).isInstanceOf(NoSuchElementException.class).hasMessage("No issue found with id "+i1.getId()+".");

    }

    @Test void findIssueByUUID(){
        Issues i = new Issues();
        Issue i1= new IssueBuilder().setPriority(Priority.HIGH).setType("t1").setPackageName("p1").setMessage("m1").setFileName("f1").build();
        Issue i2= new IssueBuilder().setPriority(Priority.LOW).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f2").build();
        Issue i3= new IssueBuilder().setPriority(Priority.NORMAL).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f2").build();
        assertThatThrownBy(()->i.findById(i1.getId())).hasMessage("No issue found with id "+i1.getId()+".");
        i.add(i1);
        i.add(i2);
        i.add(i3);
        IssueAssert.assertThat(i.findById(i1.getId())).isEqualTo(i1);
        IssueAssert.assertThat(i.findById(i2.getId())).isEqualTo(i2);
        IssueAssert.assertThat(i.findById(i3.getId())).isEqualTo(i3);

    }

    @Test void addAllFromCollection(){
        Issues iy = new Issues();
        Issue i1= new IssueBuilder().setPriority(Priority.HIGH).setType("t1").setPackageName("p1").setMessage("m1").setFileName("f1").build();
        Issue i2= new IssueBuilder().setPriority(Priority.LOW).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f2").build();
        Issue i3= new IssueBuilder().setPriority(Priority.NORMAL).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f2").build();
       Issues ix = new Issues();
        ix.add(i1);
        ix.add(i2);
        ix.add(i3);
        iy.addAll(ix.all());
        assertThat(ix.all()).isEqualTo(iy.all());

    }
    @Test void getAllIssues(){
        Issues iy = new Issues();
        Issue i1= new IssueBuilder().setPriority(Priority.HIGH).setType("t1").setPackageName("p1").setMessage("m1").setFileName("f1").build();
        Issue i2= new IssueBuilder().setPriority(Priority.LOW).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f2").build();
        Issue i3= new IssueBuilder().setPriority(Priority.NORMAL).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f2").build();
        Issues ix = new Issues();
        ix.add(i1);
        ix.add(i2);
        ix.add(i3);
    }

    @Test void getAllIssuesAndChangeThem(){

        Issue i1= new IssueBuilder().setPriority(Priority.HIGH).setType("t1").setPackageName("p1").setMessage("m1").setFileName("f1").build();
        Issue i2= new IssueBuilder().setPriority(Priority.LOW).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f2").build();
        Issue i3= new IssueBuilder().setPriority(Priority.NORMAL).setType("t2").setPackageName("p2").setMessage("m2").setFileName("f2").build();
        Issues ix = new Issues();
        i1.setFingerprint("i1");
        i2.setFingerprint("i2");
        i3.setFingerprint("i3");
        ix.add(i1);
        ix.add(i2);
        ix.add(i3);
        Issues iy = new Issues();
        iy.addAll(ix.all());
        ImmutableList<Issue> list =  ix.all().asList();
        list.get(0).setFingerprint("ixxx1");
        list.get(1).setFingerprint("ixxx2");
        list.get(2).setFingerprint("ixxx3");
        Issue x = ix.findById(i1.getId());
        Issue y = iy.findById(i1.getId());
        assertThat(x.getFingerprint()).isEqualTo("i1");
        assertThat(y.getFingerprint()).isEqualTo("i1");

    }
}