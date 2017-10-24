package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.util.NoSuchElementException;
import static edu.hm.hafner.analysis.Assertions.IssueAssert.assertThat;
import static edu.hm.hafner.analysis.Assertions.IssuesAssert.assertThat;
import static org.assertj.core.api.Java6Assertions.*;

class IssuesTest {

    @Test
    void addBasicIssueTest() {

        Issues issues = new Issues();
        IssueBuilder builder = new IssueBuilder();
        Issue firstIssue = builder.build();
        issues.add(firstIssue);

        assertThat(issues)
                .hasSize(1)
                .hasHighPrioritySize(0)
                .hasLowPrioritySize(0)
                .hasNormalPrioritySize(1)
                .hasIssueOnIndex(firstIssue, 0)
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
    void removeNotExistingIssueTest() {

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

        assertThatThrownBy(() -> issues.remove(secondIssue.getId()))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void removeBasicIssueTest() {

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
    }

    @Test
    void getElementFromEmptyIssuesTest() {
        Issues issues = new Issues();

        assertThatThrownBy(() -> issues.get(0))
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    void findByIdTest() {

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
    void NotFoundByIdTest() {

        Issues issues = new Issues();
        IssueBuilder builder = new IssueBuilder();
        Issue firstIssue = builder.build();
        issues.add(firstIssue);
        builder.setMessage("second issue");
        builder.setLineStart(2);
        Issue secondIssue = builder.build();


        assertThatThrownBy(() -> issues.findById(secondIssue.getId()))
                .isInstanceOf(NoSuchElementException.class);


    }

    @Test
    void copyBasicIssuesTest() {
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
                .hasIssueOnIndex(firstIssue, 0)
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

    @Test
    void addAllTest() {
        Issues firstIssues = new Issues();
        Issues secondIssues = new Issues();

        IssueBuilder builder = new IssueBuilder();
        Issue firstIssue = builder.build();
        Issue secondIssue = builder
                .setLineStart(2)
                .setPriority(Priority.LOW)
                .setFileName("second issue")
                .build();
        Issue thirdIssue = builder
                .setLineStart(3)
                .setPriority(Priority.HIGH)
                .setFileName("third issue")
                .build();

        firstIssues.add(firstIssue);
        firstIssues.add(secondIssue);
        secondIssues.add(thirdIssue);

        firstIssues.addAll(secondIssues.all());

        assertThat(firstIssues)
                .hasSize(3)
                .hasHighPrioritySize(1)
                .hasLowPrioritySize(1)
                .hasNormalPrioritySize(1)
                .hasIssueOnIndex(firstIssue, 0)
                .hasNumberOfFiles(3)
                .hasToString(String.format("%d issues", firstIssues.size()));

    }

    @Test
    void findByPropertyTest() {
        Issues firstIssues = new Issues();
        Issues filteredIssues = new Issues();

        IssueBuilder builder = new IssueBuilder();

        Issue firstIssue = builder
                .setLineStart(1)
                .setPriority(Priority.LOW)
                .setFileName("first issue")
                .build();
        Issue secondIssue = builder
                .setLineStart(2)
                .setPriority(Priority.LOW)
                .setFileName("second issue")
                .build();
        Issue thirdIssue = builder
                .setLineStart(3)
                .setPriority(Priority.HIGH)
                .setFileName("third issue")
                .build();


        firstIssues.add(firstIssue);
        firstIssues.add(secondIssue);
        firstIssues.add(thirdIssue);

        filteredIssues.addAll(firstIssues.findByProperty((n) -> n.getPriority() == Priority.LOW));

        assertThat(filteredIssues)
                .hasSize(2)
                .hasHighPrioritySize(0)
                .hasLowPrioritySize(2)
                .hasNormalPrioritySize(0)
                .hasIssueOnIndex(firstIssue, 0)
                .hasNumberOfFiles(2)
                .hasToString(String.format("%d issues", filteredIssues.size()));
    }

}