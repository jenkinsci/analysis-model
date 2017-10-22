package edu.hm.hafner.analysis;

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

}