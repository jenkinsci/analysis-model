package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

import com.google.common.collect.ImmutableSet;

import edu.hm.hafner.util.NoSuchElementException;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static edu.hm.hafner.analysis.IssuesAssert.assertThat;


/**
 * Tests the class {@link Issues}.
 *
 * @author Michael Schmid
 */
class IssuesTest {

    /** Verify that the add method adds the issue and increases the priority counter */
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

    /** Verfiy that a new issues object hasn't any issue */
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

    /** Verfiy that the remove method removes the issue and decreases the priority counter*/
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

    /** Verify that the findById method and the remove method throw a NoSuchElementException if the issue isn't in issues */
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

    /** Verfiy that the addAll method keeps the order of the list */
    @SuppressWarnings({"JUnitTestMethodWithNoAssertions", "NestedConditionalExpression"})
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

    /** Verify that all method delivers all issues */
    @SuppressWarnings("NestedConditionalExpression")
    @Test
    void allTest() {
        Issues sut = new Issues();
        int testObjects = 10;
        for (int index = 0; index < testObjects; index++) {
            sut.add(new IssueBuilder()
                    .setMessage("Issue " + index)
                    .setLineStart(index)
                    .setLineEnd(index * index)
                    .setDescription("Issue build while testing the all method of Issues")
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
            Assertions.assertThat(setOfIssues).contains(it.next());
        }

    }

    /** Verfiy the the copy method delivers an independent issues object with the same issue objects in it */
    @SuppressWarnings("NestedConditionalExpression")
    @Test
    void copyTest() {
        Issues sut = new Issues();
        int testObjects = 10;
        for (int index = 0; index < testObjects; index++) {
            sut.add(new IssueBuilder()
                    .setMessage("Issue " + index)
                    .setLineStart(index)
                    .setLineEnd(index * index)
                    .setDescription("Issue build while testing the copy method of Issues")
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
        Assertions.assertThat(sutSet).isEqualTo(copySet).as("The issues object and the copy of it does not contains the equal elements");

        // Check if the issues object and it's copy have the same references to the issue objects
        for (Iterator<Issue> it = copy.iterator(); it.hasNext(); ) {
            it.next().setFingerprint("modified copy");
        }

        for (Iterator<Issue> it = sut.iterator(); it.hasNext(); ) {
            IssueAssert.assertThat(it.next()).hasFingerprint("modified copy");
        }

        // Check if the issues object is independent of it's copy
        copy.remove(copy.get(0).getId());
        for (Issue issue: sutSet) {
            asserter.assertContains(issue).as("The issues object and it's copy are not independend");
        }

    }

    /** Verify that the findByProperty method delivers the matching issues */
    @SuppressWarnings("NestedConditionalExpression")
    @Test
    void findByPropertyTest() {
        Issues sut = new Issues();
        int testObjects = 10;
        for (int index = 0; index < testObjects; index++) {
            sut.add(new IssueBuilder()
                    .setMessage("Issue " + index)
                    .setLineStart(index)
                    .setLineEnd(index * index)
                    .setDescription("Issue build while testing the findByProperty method of Issues")
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
            Assertions.assertThat(issue).isEqualTo(sutIterator.next()).as("findByProperty with true as predicate doesn't deliver the whole list");
        }

        Assertions.assertThat(sut.findByProperty(issue -> false)).hasSize(0).as("findByProperty with false as predicate doesn't deliver a empty list");

        Assertions.assertThat(sut.findByProperty(issue -> issue.getPriority() == Priority.LOW)).hasSize((testObjects + 2) / 3).as("findByProperty doesn't work with predicate issue.getPriority() == Priority.LOW");
        Assertions.assertThat(sut.findByProperty(issue -> issue.getPriority() == Priority.NORMAL)).hasSize((testObjects + 1) / 3).as("findByProperty doesn't work with predicate issue.getPriority() == Priority.NORMAL");
        Assertions.assertThat(sut.findByProperty(issue -> issue.getPriority() == Priority.HIGH)).hasSize(testObjects / 3).as("findByProperty doesn't work with predicate issue.getPriority() == Priority.HIGH");

    }

    /** Verify that the getProperty method delivers an ordered set of the desired issue properties */
    @SuppressWarnings("NestedConditionalExpression")
    @Test
    void getPropertiesTest() {
        Issues sut = new Issues();
        int testObjects = 10;
        for (int index = 0; index < testObjects; index++) {
            sut.add(new IssueBuilder()
                    .setMessage("Issue " + index)
                    .setLineStart(index)
                    .setLineEnd(index * index)
                    .setDescription("Issue build while testing the getProperties method of Issues")
                    .setPriority(index % 3 == 0 ? Priority.LOW : index % 3 == 1 ? Priority.NORMAL : Priority.HIGH)
                    .setFileName("Test" + (testObjects - index) + ".java")
                    .setPackageName("edu.hm.hafner.analysis")
                    .build());
        }

        Assertions.assertThat(sut.getNumberOfFiles()).isEqualTo(testObjects);

        int[] fileIndexOrder = {1, 10, 2, 3, 4, 5, 6, 7, 8, 9};
        int orderIndex = 0;
        for (String filename : sut.getFiles()) {
            Assertions.assertThat(filename).isEqualTo("Test" + fileIndexOrder[orderIndex] + ".java");
            orderIndex++;
        }

        int lineEndBase = 0;
        for (int lineEnd : sut.getProperties(issue -> issue.getLineEnd())) {
            Assertions.assertThat(lineEnd).isEqualTo(lineEndBase * lineEndBase);
            lineEndBase++;
        }

        Assertions.assertThat(sut.getProperties(issue -> issue.getPriority()).size()).isEqualTo(3);
    }

}