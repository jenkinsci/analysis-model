package edu.hm.hafner.analysis;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.util.NoSuchElementException;
import static edu.hm.hafner.analysis.Assertions.IssueAssert.assertThat;
import static edu.hm.hafner.analysis.Assertions.IssuesAssert.assertThat;
import static org.assertj.core.api.Java6Assertions.*;

/**
 * Test class for issues.
 *
 * @author Tom Maier
 * @author Johannes Arzt
 */
class IssuesTest {

    @Test
    void addIssueTest() {

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

        assertThat(issues.get(0)).isSameAs(firstIssue);



    }

    @Test
    void removeNotExistingIssueTest() {

        Issues issues = new Issues();
        IssueBuilder builder = new IssueBuilder();
        builder.setMessage("second issue");
        builder.setLineStart(2);
        Issue secondIssue = builder.build();

        assertThatThrownBy(() -> issues.remove(secondIssue.getId()))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining(String.format("No issue found with id %s.",secondIssue.getId()));


    }

    @Test
    void removeExistingIssueTest() {

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
    void limitTest() {
        Issues issues = new Issues();

        IssueBuilder builder = new IssueBuilder();
        Issue firstIssue = builder.build();
        Issue secondIssue = builder
                .setLineStart(1)
                .build();
        Issue thirdIssue = builder
                .setLineStart(2)
                .build();
        Issue fourthIssue = builder
                .setLineStart(3)
                .build();

        issues.add(firstIssue);
        issues.add(secondIssue);
        issues.add(thirdIssue);
        issues.add(fourthIssue);

        assertThatThrownBy(() -> issues.get(-1))
                .isInstanceOf(IndexOutOfBoundsException.class);

        assertThat(issues.get(0)).isSameAs(firstIssue);
        assertThat(issues.get(2)).isSameAs(thirdIssue);
        assertThat(issues.get(3)).isSameAs(fourthIssue);
    }

    @Test
    void findByIdTest() {

        Issues issues = new Issues();
        IssueBuilder builder = new IssueBuilder();
        Issue firstIssue = builder.build();
        issues.add(firstIssue);

        assertThat(issues.findById(firstIssue.getId())).isSameAs(firstIssue);


    }

    @Test
    void notFoundByIdTest() {

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
    void copyIssuesTest() {
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

        assertThat(copy.get(0)).isSameAs(firstIssue);


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

        filteredIssues.addAll(firstIssues.findByProperty(issue -> issue.getPriority() == Priority.LOW));

        assertThat(filteredIssues)
                .hasSize(2)
                .hasHighPrioritySize(0)
                .hasLowPrioritySize(2)
                .hasNormalPrioritySize(0)
                .hasIssueOnIndex(firstIssue, 0)
                .hasNumberOfFiles(2)
                .hasToString(String.format("%d issues", filteredIssues.size()));
    }

    @Test
    void getFilesTest() {
        Issues issues = new Issues();
        IssueBuilder builder = new IssueBuilder();

        Issue firstIssue = builder
                .setLineStart(1)
                .setPriority(Priority.LOW)
                .setFileName("first issue")
                .build();
        Issue secondIssue = builder
                .setLineStart(2)
                .setPriority(Priority.NORMAL)
                .setFileName("second issue")
                .build();

        issues.add(firstIssue);
        issues.add(secondIssue);

        SortedSet<String> files = new TreeSet<>();
        files.add(firstIssue.getFileName());
        files.add(secondIssue.getFileName());

        assertThat(issues)
                .hasSize(2)
                .hasFiles(files)
                .hasHighPrioritySize(0)
                .hasLowPrioritySize(1)
                .hasNormalPrioritySize(1)
                .hasIssueOnIndex(firstIssue, 0)
                .hasNumberOfFiles(2)
                .hasToString(String.format("%d issues", issues.size()));
    }


}