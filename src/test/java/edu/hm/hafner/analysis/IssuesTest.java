package edu.hm.hafner.analysis;

import java.util.ArrayList;
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
    @Test
    void addIssueCheckContains() {
        Issues sut = new Issues();
        Issue issue = new IssueBuilder().build();
        sut.add(issue);
        assertThat(sut)
                .contains(issue)
                .hasSize(1)
                .hasSizeOfPriorityHigh(0)
                .hasSizeOfPriorityLow(0)
                .hasSizeOfPriorityNormal(1)
                .hasToString("1 issues");
    }

    /** Verify that the remove method removes the issue and decreases the priority counter*/
    @Test
    void addAndRemoveIssue() {
        Issues sut = new Issues();
        Issue issue = new IssueBuilder().build();
        sut.add(issue);
        assertThat(sut)
                .contains(issue)
                .hasSize(1)
                .hasSizeOfPriorityNormal(1)
                .hasToString("1 issues");

        IssueAssert.assertThat(sut.findById(issue.getId())).isEqualTo(issue);

        sut.remove(issue.getId());
        assertThat(sut)
                .doesNotContain(issue)
                .hasSize(0)
                .hasSizeOfPriorityNormal(0)
                .hasToString("0 issues");
    }

    /** Verify that the findById method and the remove method throw a NoSuchElementException if the issue isn't in issues */
    @Test
    void removeIssueIssuesDoesNotContain() {
        Issues sut = new Issues();
        Issue issue = new IssueBuilder().build();

        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> sut.findById(issue.getId()))
                .withMessageContaining(issue.getId().toString());

        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> sut.remove(issue.getId()))
                .withMessageContaining(issue.getId().toString());

    }

    /** Verify that the addAll method keeps the order of the list */
    @Test
    void addAllTest() {
        Issues sut = new Issues();

        int testObjectsPerPriority = 10;
        int testObjects = 3 * testObjectsPerPriority;
        List<Issue> issueList = generateList(testObjectsPerPriority);

        sut.addAll(issueList);
        assertThat(sut)
                .hasSize(testObjects)
                .hasSizeOfPriorityLow(testObjectsPerPriority)
                .hasSizeOfPriorityNormal(testObjectsPerPriority)
                .hasSizeOfPriorityHigh(testObjectsPerPriority)
                .containsExactly(issueList);

        // Check if the issues object is independent of the list
        Issue removedIssue = issueList.get(0);
        issueList.remove(0);
        IssueAssert.assertThat(sut.get(0)).isEqualTo(removedIssue);

    }

    /** Verify that all method delivers all issues */
    @Test
    void allTest() {
        int testObjectsPerPriority = 10;
        int testObjects = 3 * testObjectsPerPriority;
        Issues sut = generateIssues(testObjectsPerPriority);

        ImmutableSet<Issue> setOfIssues = sut.all();

        assertThat(sut)
                .hasSize(testObjects)
                .hasSizeOfPriorityLow(testObjectsPerPriority)
                .hasSizeOfPriorityNormal(testObjectsPerPriority)
                .hasSizeOfPriorityHigh(testObjectsPerPriority)
                .containsExactly(setOfIssues);
    }

    /** Verify the the copy method delivers an independent issues object with the same issue objects in it */
    @Test
    void copyTest() {
        int testObjectsPerPriority = 10;
        int testObjects = 3 * testObjectsPerPriority;
        Issues sut = generateIssues(testObjectsPerPriority);

        Issues copy = sut.copy();
        ImmutableSet<Issue> sutSet = sut.all();
        ImmutableSet<Issue> copySet = copy.all();

        IssuesAssert asserter = assertThat(sut)
                .hasSize(testObjects)
                .hasSizeOfPriorityLow(testObjectsPerPriority)
                .hasSizeOfPriorityNormal(testObjectsPerPriority)
                .hasSizeOfPriorityHigh(testObjectsPerPriority);

        Assertions.assertThat(sutSet).isEqualTo(copySet);

        // Check if the issues object and it's copy have the same references to the issue objects
        Iterable<Issue> copyIterable = () -> copy.iterator();
        for (Issue issue : copyIterable) {
            issue.setFingerprint("modified copy");
        }

        Iterable<Issue> sutIterable = () -> sut.iterator();
        for (Issue issue : sutIterable) {
            IssueAssert.assertThat(issue).hasFingerprint("modified copy");
        }

        // Check if the issues object is independent of it's copy
        copy.remove(copy.get(0).getId());
        assertThat(sut).containsExactly(sutSet).as("The issues object and it's copy are not independent");

    }

    /** Verify that the findByProperty method delivers the matching issues */
    @Test
    void findByPropertyTest() {
        int testObjectsPerPriority = 10;
        int testObjects = 3 * testObjectsPerPriority;
        Issues sut = generateIssues(testObjectsPerPriority);

        List<Issue> allIssues = sut.findByProperty(issue -> true);
        assertThat(sut).containsExactly(allIssues);

        Assertions.assertThat(sut.findByProperty(issue -> false)).hasSize(0).as("findByProperty with false as predicate doesn't deliver a empty list");

        Assertions.assertThat(sut.findByProperty(issue -> issue.getPriority() == Priority.LOW))
                .hasSize(testObjectsPerPriority).as("findByProperty doesn't work with predicate issue.getPriority() == Priority.LOW");
        Assertions.assertThat(sut.findByProperty(issue -> issue.getPriority() == Priority.NORMAL))
                .hasSize(testObjectsPerPriority).as("findByProperty doesn't work with predicate issue.getPriority() == Priority.NORMAL");
        Assertions.assertThat(sut.findByProperty(issue -> issue.getPriority() == Priority.HIGH))
                .hasSize(testObjectsPerPriority).as("findByProperty doesn't work with predicate issue.getPriority() == Priority.HIGH");

    }

    /** Verify that the getProperty method delivers an ordered set of the desired issue properties */
    @SuppressWarnings("NestedConditionalExpression")
    @Test
    void getPropertiesTest() {
        int testObjectsPerPriority = 11;
        int testObjects = 3 * testObjectsPerPriority;
        Issues sut = generateIssues(testObjectsPerPriority);

        Assertions.assertThat(sut.getNumberOfFiles()).isEqualTo(testObjectsPerPriority);

        int[] fileIndexOrder = {0, 1, 10, 2, 3, 4, 5, 6, 7, 8, 9};
        int orderIndex = 0;
        for (String filename : sut.getFiles()) {
            Assertions.assertThat(filename).isEqualTo("Test_" + fileIndexOrder[orderIndex] + ".java");
            orderIndex++;
        }

        int lineEndBase = 0;
        for (int lineEnd : sut.getProperties(issue -> issue.getLineEnd())) {
            Assertions.assertThat(lineEnd).isEqualTo(lineEndBase * lineEndBase);
            lineEndBase++;
        }

        Assertions.assertThat(sut.getProperties(issue -> issue.getPriority()).size()).isEqualTo(3);
    }

    /**
     * Generate a issues test object.
     * @param testObjectsPerPriority number of issues per priority to create
     * @return Issues object to test
     */
    private Issues generateIssues(final int testObjectsPerPriority) {
        Issues issues = new Issues();
        for (int index = 0; index < testObjectsPerPriority; index++) {
            issues.add(new IssueBuilder()
                    .setMessage("Issue " + index)
                    .setLineStart(index)
                    .setLineEnd(index * index)
                    .setDescription("Issue build with generateIssues - Priority.LOW")
                    .setPriority(Priority.LOW)
                    .setFileName("Test_" + index + ".java")
                    .build());

            issues.add(new IssueBuilder()
                    .setMessage("Issue " + index)
                    .setLineStart(index)
                    .setLineEnd(index * index)
                    .setDescription("Issue build with generateIssues - Priority.NORMAL")
                    .setPriority(Priority.NORMAL)
                    .setFileName("Test_" + index + ".java")
                    .build());

            issues.add(new IssueBuilder()
                    .setMessage("Issue " + index)
                    .setLineStart(index)
                    .setLineEnd(index * index)
                    .setDescription("Issue build with generateIssues - Priority.HIGH")
                    .setPriority(Priority.HIGH)
                    .setFileName("Test_" + index + ".java")
                    .build());
        }

        return issues;
    }

    /**
     * Generate a list of issues.
     * @param testObjectsPerPriority number of issues per priority to create
     * @return list of issues
     */
    private List<Issue> generateList(final int testObjectsPerPriority) {
        List<Issue> list = new ArrayList<>();
        for (int index = 0; index < testObjectsPerPriority; index++) {
            list.add(new IssueBuilder()
                    .setMessage("Issue " + index)
                    .setLineStart(index)
                    .setLineEnd(index * index)
                    .setDescription("Issue build with generateIssues - Priority.LOW")
                    .setPriority(Priority.LOW)
                    .setFileName("Test_" + index + ".java")
                    .build());

            list.add(new IssueBuilder()
                    .setMessage("Issue " + index)
                    .setLineStart(index)
                    .setLineEnd(index * index)
                    .setDescription("Issue build with generateIssues - Priority.NORMAL")
                    .setPriority(Priority.NORMAL)
                    .setFileName("Test_" + index + ".java")
                    .build());

            list.add(new IssueBuilder()
                    .setMessage("Issue " + index)
                    .setLineStart(index)
                    .setLineEnd(index * index)
                    .setDescription("Issue build with generateIssues - Priority.HIGH")
                    .setPriority(Priority.HIGH)
                    .setFileName("Test_" + index + ".java")
                    .build());
        }

        return list;
    }

}