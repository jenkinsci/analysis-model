package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static edu.hm.hafner.analysis.IssuesAssert.assertThat;

class IssuesTest {

    @Test
    void addIssueCheckContains() {
        final Issues sut = new Issues();
        final Issue issue = new IssueBuilder().build();
        sut.add(issue);
        assertThat(sut)
                .assertContains(issue)
                .hasSize(1)
                .hasSizeOfPriorityHigh(0)
                .hasSizeOfPriorityLow(0)
                .hasSizeOfPriorityNormal(1)
                .hasToString("1 issues");
    }

    @Test
    void noAddIssueCheckNotContains() {
        final Issues sut = new Issues();
        final Issue issue = new IssueBuilder().build();
        assertThat(sut)
                .assertDoesNotContain(issue)
                .hasSize(0)
                .hasSizeOfPriorityHigh(0)
                .hasSizeOfPriorityLow(0)
                .hasSizeOfPriorityNormal(0)
                .hasToString("0 issues");
    }

    @Test
    void addAndRemoveIssue() {
        final Issues sut = new Issues();
        final Issue issue = new IssueBuilder().build();
        sut.add(issue);
        assertThat(sut)
                .assertContains(issue)
                .hasSize(1)
                .hasSizeOfPriorityHigh(0)
                .hasSizeOfPriorityLow(0)
                .hasSizeOfPriorityNormal(1)
                .hasToString("1 issues");
        sut.findById(issue.getId());
        sut.remove(issue.getId());
        assertThat(sut)
                .assertDoesNotContain(issue)
                .hasSize(0)
                .hasSizeOfPriorityHigh(0)
                .hasSizeOfPriorityLow(0)
                .hasSizeOfPriorityNormal(0)
                .hasToString("0 issues");
    }

    @Test
    void removeIssueIssuesDoesNotContain() {
        final Issues sut = new Issues();
        final Issue issue = new IssueBuilder().build();
        assertThat(sut)
                .assertDoesNotContain(issue)
                .hasSize(0)
                .hasSizeOfPriorityHigh(0)
                .hasSizeOfPriorityLow(0)
                .hasSizeOfPriorityNormal(0)
                .hasToString("0 issues");

        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> sut.findById(issue.getId()))
                .withMessageContaining(issue.getId().toString());

        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> sut.remove(issue.getId()))
                .withMessageContaining(issue.getId().toString());

    }

    @Test
    void addAllTest() {
        final Issues sut = new Issues();
        final List<Issue> issueList = new ArrayList<>();
        final int testObjects = 10;
        for (int index = 0; index < testObjects; index++) {
            issueList.add(new IssueBuilder()
                    .setMessage("Issue " + index)
                    .setLineStart(index)
                    .setLineEnd(index * index)
                    .setDescription("Issue build while testing the addAll method of Issues")
                    .setPriority(index % 3 == 0 ? Priority.LOW : index % 3 == 1 ? Priority.NORMAL : Priority.HIGH)
                    .build());
        }

        sut.addAll(issueList);
        IssuesAssert asserter = assertThat(sut)
                .hasSize(testObjects)
                .hasSizeOfPriorityLow((testObjects + 2) / 3)
                .hasSizeOfPriorityNormal((testObjects + 1) / 3)
                .hasSizeOfPriorityHigh(testObjects / 3);

        // Check if every element of the list is a element of the issues object
        for (Issue issue: issueList) {
            asserter.assertContains(issue);
        }

        // Check if the order is the same
        for (int index = 0; index < testObjects; index++) {
            IssueAssert.assertThat(sut.get(index))
                    .isEqualTo(issueList.get(index))
                    .as("The order of the issues object and the list of isses is not the same");
        }

        // Check if the issues object is independed of the list
        Issue removedIssue = issueList.get(0);
        issueList.remove(0);
        IssueAssert.assertThat(sut.get(0)).isEqualTo(removedIssue);

    }

}