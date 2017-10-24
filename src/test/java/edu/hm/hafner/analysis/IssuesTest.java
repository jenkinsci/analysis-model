package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.util.NoSuchElementException;
import static edu.hm.hafner.analysis.Assertions.IssueAssert.assertThat;
import static edu.hm.hafner.analysis.Assertions.IssuesAssert.assertThat;
import static org.assertj.core.api.Java6Assertions.*;

class IssuesTest {
    @Test
    void addBasicIssuesTest(){

        Issues issues = new Issues();
        IssueBuilder builder = new IssueBuilder();
        Issue firstIssue = builder.build();
        issues.add(firstIssue);

        assertThat(issues)
                .hasSize(1)
                .hasHighPrioritySize(0)
                .hasLowPrioritySize(0)
                .hasNormalPrioritySize(1)
                .hasIssueOnIndex(firstIssue,0)
                .hasNumberOfFiles(1)
                .hasToString(String.format("%d issues", issues.size()));
                //files ?

        assertThat(issues.get(0))
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasPriority(Priority.NORMAL)
                .hasMessage("")
                .hasCategory("")
                .hasFileName("-")
                .hasType("-")
                .hasColumnStart(0)
                .hasColumnEnd(0)
                .hasPackageName("-")
                .hasDescription("")
                .hasToString(String.format("%s(%d,%d): %s: %s: %s", "-", 0, 0, "-", "", ""));


    }

    @Test
    void removeBasicIssueIssuesShoudBeEmpty(){

        Issues issues = new Issues();
        IssueBuilder builder = new IssueBuilder();
        Issue firstIssue = builder.build();
        issues.add(firstIssue);
        issues.remove(firstIssue.getId());

        assertThat(issues)
                .hasSize(0)
                .hasHighPrioritySize(0)
                .hasLowPrioritySize(0)
                .hasNormalPrioritySize(0)
                .hasNumberOfFiles(0);
        //files ?
        assertThatThrownBy(()-> issues.get(0))
        .isInstanceOf(IndexOutOfBoundsException.class)
                .hasMessageContaining("0")
                .hasNoCause();

    }

    @Test
    void removeNotExistingIssue(){

        Issues issues = new Issues();
        IssueBuilder builder = new IssueBuilder();
        Issue firstIssue = builder.build();
        issues.add(firstIssue);
        issues.remove(firstIssue.getId());
        issues.add(firstIssue);
        builder.setMessage("second issue");
        builder.setLineStart(2);
        Issue secondIssue = builder.build();

        issues.add(firstIssue);

        //files ?
        assertThatThrownBy(()-> issues.remove(secondIssue.getId()))
                .isInstanceOf(NoSuchElementException.class);

    }

    @Test
    void findByIdTest(){

        Issues issues = new Issues();
        IssueBuilder builder = new IssueBuilder();
        Issue firstIssue = builder.build();
        issues.add(firstIssue);


        assertThat(issues.findById(firstIssue.getId()))
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasPriority(Priority.NORMAL)
                .hasMessage("")
                .hasCategory("")
                .hasFileName("-")
                .hasType("-")
                .hasColumnStart(0)
                .hasColumnEnd(0)
                .hasPackageName("-")
                .hasDescription("")
                .hasToString(String.format("%s(%d,%d): %s: %s: %s", "-", 0, 0, "-", "", ""));


    }

    @Test
    void NotFoundByIdTest(){

        Issues issues = new Issues();
        IssueBuilder builder = new IssueBuilder();
        Issue firstIssue = builder.build();
        issues.add(firstIssue);
        builder.setMessage("second issue");
        builder.setLineStart(2);
        Issue secondIssue = builder.build();


        assertThatThrownBy(()->issues.findById(secondIssue.getId()))
                .isInstanceOf(NoSuchElementException.class);



    }

    @Test
    void copyBasicIssues(){
        Issues issues = new Issues();
        IssueBuilder builder = new IssueBuilder();
        Issue firstIssue = builder.build();
        issues.add(firstIssue);

        Issues copy = issues.copy();

        assertThat(copy)
                .hasSize(1)
                .hasHighPrioritySize(0)
                .hasLowPrioritySize(0)
                .hasNormalPrioritySize(1)
                .hasIssueOnIndex(firstIssue,0)
                .hasNumberOfFiles(1)
                .hasToString(String.format("%d issues", issues.size()));
        //files ?

        assertThat(copy.get(0))
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasPriority(Priority.NORMAL)
                .hasMessage("")
                .hasCategory("")
                .hasFileName("-")
                .hasType("-")
                .hasColumnStart(0)
                .hasColumnEnd(0)
                .hasPackageName("-")
                .hasDescription("")
                .hasToString(String.format("%s(%d,%d): %s: %s: %s", "-", 0, 0, "-", "", ""));




    }



}