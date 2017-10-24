package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableSet;

import edu.hm.hafner.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static edu.hm.hafner.analysis.IssuesAssert.assertThat;

class IssuesTest {

    @SuppressWarnings("JUnitTestMethodWithNoAssertions")
    @Test
    void addIssueCheckContains() {
        Issues sut = new Issues();
        Issue issue = new IssueBuilder().build();
        sut.add(issue);
        assertThat(sut)
                .assertContains(issue)
                .hasSize(1)
                .hasSizeOfPriorityHigh(0)
                .hasSizeOfPriorityLow(0)
                .hasSizeOfPriorityNormal(1)
                .hasToString("1 issues");
    }

    @SuppressWarnings("JUnitTestMethodWithNoAssertions")
    @Test
    void noAddIssueCheckNotContains() {
        Issues sut = new Issues();
        Issue issue = new IssueBuilder().build();
        assertThat(sut)
                .assertDoesNotContain(issue)
                .hasSize(0)
                .hasSizeOfPriorityHigh(0)
                .hasSizeOfPriorityLow(0)
                .hasSizeOfPriorityNormal(0)
                .hasToString("0 issues");
    }

    @SuppressWarnings("JUnitTestMethodWithNoAssertions")
    @Test
    void addAndRemoveIssue() {
        Issues sut = new Issues();
        Issue issue = new IssueBuilder().build();
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
        Issues sut = new Issues();
        Issue issue = new IssueBuilder().build();
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

    @SuppressWarnings("JUnitTestMethodWithNoAssertions")
    @Test
    void addAllTest() {
        Issues sut = new Issues();
        List<Issue> issueList = new ArrayList<>();
        int testObjects = 10;
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

    @Test
    void allTest() {
        Issues sut = new Issues();
        int testObjects = 10;
        for (int index = 0; index < testObjects; index++) {
            sut.add(new IssueBuilder()
                    .setMessage("Issue " + index)
                    .setLineStart(index)
                    .setLineEnd(index * index)
                    .setDescription("Issue build while testing the addAll method of Issues")
                    .setPriority(index % 3 == 0 ? Priority.LOW : index % 3 == 1 ? Priority.NORMAL : Priority.HIGH)
                    .build());
        }

        IssuesAssert asserter = assertThat(sut)
                .hasSize(testObjects)
                .hasSizeOfPriorityLow((testObjects + 2) / 3)
                .hasSizeOfPriorityNormal((testObjects + 1) / 3)
                .hasSizeOfPriorityHigh(testObjects / 3);

        // Check if all() delivers all issues
        ImmutableSet<Issue> setOfIssues = sut.all();
        for (Issue issue: setOfIssues) {
            asserter.assertContains(issue);
        }
        for (Iterator<Issue> it = sut.iterator(); it.hasNext(); ) {
            org.assertj.core.api.Assertions.assertThat(setOfIssues).contains(it.next());
        }

    }

    @Test
    void copyTest() {
        Issues sut = new Issues();
        int testObjects = 10;
        for (int index = 0; index < testObjects; index++) {
            sut.add(new IssueBuilder()
                    .setMessage("Issue " + index)
                    .setLineStart(index)
                    .setLineEnd(index * index)
                    .setDescription("Issue build while testing the addAll method of Issues")
                    .setPriority(index % 3 == 0 ? Priority.LOW : index % 3 == 1 ? Priority.NORMAL : Priority.HIGH)
                    .build());
        }

        IssuesAssert asserter = assertThat(sut)
                .hasSize(testObjects)
                .hasSizeOfPriorityLow((testObjects + 2) / 3)
                .hasSizeOfPriorityNormal((testObjects + 1) / 3)
                .hasSizeOfPriorityHigh(testObjects / 3);

        Issues copy = sut.copy();
        ImmutableSet<Issue> sutSet = sut.all();
        ImmutableSet<Issue> copySet = copy.all();
        org.assertj.core.api.Assertions.assertThat(sutSet).isEqualTo(copySet).as("The issues object and the copy of it does not contains the equal elements");

        // Check if the issues object and it's copy have the same references to the issue objects
        for (Iterator<Issue> it = copy.iterator(); it.hasNext(); ) {
            it.next().setFingerprint("modified copy");
        }

        for (Iterator<Issue> it = sut.iterator(); it.hasNext(); ) {
            IssueAssert.assertThat(it.next()).hasFingerprint("modified copy");
        }

        // Check if the issues object is independed of it's copy
        copy.remove(copy.get(0).getId());
        for (Issue issue: sutSet) {
            asserter.assertContains(issue).as("The issues object and it's copy are not independend");
        }

    }

    @Test
    void findByPropertyTest() {
        Issues sut = new Issues();
        int testObjects = 10;
        for (int index = 0; index < testObjects; index++) {
            sut.add(new IssueBuilder()
                    .setMessage("Issue " + index)
                    .setLineStart(index)
                    .setLineEnd(index * index)
                    .setDescription("Issue build while testing the addAll method of Issues")
                    .setPriority(index % 3 == 0 ? Priority.LOW : index % 3 == 1 ? Priority.NORMAL : Priority.HIGH)
                    .build());
        }

        IssuesAssert asserter = assertThat(sut)
                .hasSize(testObjects)
                .hasSizeOfPriorityLow((testObjects + 2) / 3)
                .hasSizeOfPriorityNormal((testObjects + 1) / 3)
                .hasSizeOfPriorityHigh(testObjects / 3);

        List<Issue> allIssues = sut.findByProperty(issue -> true);
        Iterator<Issue> sutIterator = sut.iterator();
        for (Issue issue: allIssues) {
            org.assertj.core.api.Assertions.assertThat(issue).isEqualTo(sutIterator.next()).as("findByProperty with true as predicate doesn't deliver the whole list");
        }

        org.assertj.core.api.Assertions.assertThat(sut.findByProperty(issue -> false)).hasSize(0).as("findByProperty with false as predicate doesn't deliver a empty list");

        org.assertj.core.api.Assertions.assertThat(sut.findByProperty(issue -> issue.getPriority() == Priority.LOW)).hasSize((testObjects + 2) / 3).as("findByProperty doesn't work with predicate issue.getPriority() == Priority.LOW");
        org.assertj.core.api.Assertions.assertThat(sut.findByProperty(issue -> issue.getPriority() == Priority.NORMAL)).hasSize((testObjects + 1) / 3).as("findByProperty doesn't work with predicate issue.getPriority() == Priority.NORMAL");
        org.assertj.core.api.Assertions.assertThat(sut.findByProperty(issue -> issue.getPriority() == Priority.HIGH)).hasSize(testObjects / 3).as("findByProperty doesn't work with predicate issue.getPriority() == Priority.HIGH");

    }

}