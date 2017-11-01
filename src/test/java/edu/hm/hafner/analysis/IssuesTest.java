package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import edu.hm.hafner.analysis.assertj.AnalysisSoftAssertions;
import edu.hm.hafner.util.NoSuchElementException;
import static edu.hm.hafner.analysis.assertj.IssueAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

/**
 * Tests the class {@link Issues}.
 *
 * @author Andreas Moser
 */
class IssuesTest {
    /** Verifies that a issues is correctly added to issues. */
    @Test
    void testAddMethodAddsIssueCorrect() {
        Issues issues = new Issues();
        Issue highPrioIssue = new IssueBuilder().setPriority(Priority.HIGH).build();
        int expectedSizeOfHighPrio = 1;
        int expectedSizeOfNotHighPrio = 0;

        issues.add(highPrioIssue);
        ImmutableSet<Issue> allIssues = issues.all();

        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        softly.assertThat(allIssues).as("List with all issues").containsExactlyInAnyOrder(highPrioIssue);
        softly.assertThat(issues).hasSize(expectedSizeOfHighPrio);
        softly.assertThat(issues).hasHighPrioritySize(expectedSizeOfHighPrio);
        softly.assertThat(issues).hasNormalPrioritySize(expectedSizeOfNotHighPrio);
        softly.assertThat(issues).hasLowPrioritySize(expectedSizeOfNotHighPrio);
        softly.assertAll();
    }

    /** Verifies that a collection of issues can be added to issues. */
    @Test
    void testAddAllMethodAddsACollectionOfIssuesCorrect() {
        Issues issues = new Issues();
        Issue highPrioIssue = new IssueBuilder().setPriority(Priority.HIGH).build();
        Issue normalPrioIssue = new IssueBuilder().setPriority(Priority.NORMAL).build();
        Issue lowPrioIssue = new IssueBuilder().setPriority(Priority.LOW).build();
        List<Issue> issueList = new ArrayList<>();
        issueList.add(highPrioIssue);
        issueList.add(normalPrioIssue);
        issueList.add(lowPrioIssue);
        int expectedSize = 3;
        int highPrioSize = 1;
        int normalPrioSize = 1;
        int lowPrioSize = 1;

        issues.addAll(issueList);
        ImmutableSet<Issue> allIssues = issues.all();

        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        softly.assertThat(allIssues).as("List with all issues").containsExactlyInAnyOrder(highPrioIssue, normalPrioIssue, lowPrioIssue);
        softly.assertThat(issues).hasSize(expectedSize)
                .hasHighPrioritySize(highPrioSize)
                .hasNormalPrioritySize(normalPrioSize)
                .hasLowPrioritySize(lowPrioSize);
        softly.assertAll();
    }

    /** Verfies that a issue can be removes from issues. */
    @Test
    void testRemoveMethodRemovesIssueCorrect() {
        Issue highPrioIssue = new IssueBuilder().setPriority(Priority.HIGH).build();
        Issues issues = getIssuesWithGivenIssue(highPrioIssue);
        int expectedSize = 0;


        Issue removedIssue = issues.remove(highPrioIssue.getId());

        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        softly.assertThat(removedIssue).as("Removed issue").isSameAs(highPrioIssue);
        softly.assertThat(issues).hasSize(expectedSize);
        softly.assertAll();
    }

    /** Verifies that an {@link NoSuchElementException} is thrown if an issue that is not in issues is removed. */
    @Test
    void testRemoveMethodThrowsExceptionWhenRemovingANotContainedElement() {
        Issues issues = getIssuesWithOneIssue(Priority.HIGH);
        Issue notAddedIssue = new IssueBuilder().setPriority(Priority.NORMAL).build();
        int expectedSize = 1;


        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        softly.assertThatThrownBy(() -> issues.remove(notAddedIssue.getId()))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining(notAddedIssue.getId().toString())
                .hasNoCause();
        softly.assertThat(issues).hasSize(expectedSize);
        softly.assertAll();
    }

    /** Verifies that a issue of issues can be found by id. */
    @Test
    void testFindByIdReturnsTheCorrectElement() {
        Issue highPrioIssue = new IssueBuilder().setPriority(Priority.HIGH).build();
        Issue normalPrioIssue = new IssueBuilder().setPriority(Priority.NORMAL).build();
        UUID highPrioIssueID = highPrioIssue.getId();
        Issues issues = getIssuesWithGivenIssue(highPrioIssue, normalPrioIssue);

        Issue issueFoundByID = issues.findById(highPrioIssueID);

        assertThat(issueFoundByID).as("Issue found by id").isSameAs(highPrioIssue);

    }

    /** Verifies that an {@link NoSuchElementException} is thrown if searching for an id that is not in issues. */
    @Test
    void testIfFindByIdThrowsNoSuchElementExceptionWhenSearchingANotContainedID() {
        Issue highPrioIssue = new IssueBuilder().setPriority(Priority.HIGH).build();
        Issue normalPrioIssue = new IssueBuilder().setPriority(Priority.NORMAL).build();
        UUID notContainedUUID = normalPrioIssue.getId();
        Issues issues = getIssuesWithGivenIssue(highPrioIssue);

        assertThatThrownBy(() -> issues.findById(notContainedUUID))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining(notContainedUUID.toString())
                .hasNoCause();
    }

    /** Verifies that an issue can be found by property. */
    @Test
    void testFindByPropertyReturnsAllTheElementsWithGivenCriterion() {
        Issue highPrioIssue = new IssueBuilder().setPriority(Priority.HIGH).build();
        Issue highPrioIssue2 = new IssueBuilder().setPriority(Priority.HIGH).build();
        Issue lowPrioIssue = new IssueBuilder().setPriority(Priority.LOW).build();
        Issues issues = getIssuesWithGivenIssue(highPrioIssue, highPrioIssue2, lowPrioIssue);

        Predicate<Issue> criterionHighPrio = (Issue issue) -> {
            return issue.getPriority() == Priority.HIGH;
        };

        ImmutableList<Issue> highPrioIssues = issues.findByProperty(criterionHighPrio);

        assertThat(highPrioIssues).as("List of all issues with high priority").containsExactlyInAnyOrder(highPrioIssue, highPrioIssue2);
    }

    /** Verifies that it is possible to iterate over all issue's by using an iterator. */
    @Test
    void testIssuesIterator() {
        Issue highPrioIssue = new IssueBuilder().setPriority(Priority.HIGH).build();
        Issue highPrioIssue2 = new IssueBuilder().setPriority(Priority.HIGH).build();
        Issue lowPrioIssue = new IssueBuilder().setPriority(Priority.LOW).build();
        Issues issues = getIssuesWithGivenIssue(highPrioIssue, highPrioIssue2, lowPrioIssue);

        Iterator<Issue> issueIterator = issues.iterator();

        assertThat(issueIterator).as("Iterator over issues").containsExactlyInAnyOrder(highPrioIssue, highPrioIssue2, lowPrioIssue);
    }

    /** Verifies an issue can be get by index. */
    @Test
    void testGetMethodReturnsTheCorrectElementAtTheGivenPosition() {
        Issues issues = getIssuesWithThreeIssue(Priority.HIGH, Priority.NORMAL, Priority.NORMAL);
        int wantedIndex = 1;

        Iterator<Issue> issueIterator = issues.iterator();
        Issue issueAtWantedIndexByIterator = issueIterator.next();
        for (int i = 1; i <= wantedIndex && issueIterator.hasNext(); i++) {
            issueAtWantedIndexByIterator = issueIterator.next();
        }
        Issue issueAtWantedIndexByGetMethod = issues.get(wantedIndex);

        assertThat(issueAtWantedIndexByGetMethod).as("Issue at given index of issues").isSameAs(issueAtWantedIndexByIterator);
    }

    /** Verifies toString() returns a correct String. */
    @Test
    void testToStringMethod() {
        Issues issues = getIssuesWithThreeIssue(Priority.HIGH, Priority.NORMAL, Priority.NORMAL);
        String expectedString = "3 issues";


        String issuesToString = issues.toString();

        assertThat(issuesToString).as("Issues object represented as String").isEqualTo(expectedString);
    }

    /** Verifies issues can be get by file name and the right number of files is returned. */
    @Test
    void testGetFilesMethodReturnsTheCorrectFileNamesAndNumberOfFiles() {
        String testFileName1 = "testFile1";
        String testFileName2 = "testFile2";
        Issue testIssue1 = new IssueBuilder().setFileName(testFileName1).build();
        Issue testIssue2 = new IssueBuilder().setFileName(testFileName2).build();
        Issues issues = getIssuesWithGivenIssue(testIssue1, testIssue2);
        int expectedNumberOfFiles = 2;

        SortedSet<String> files = issues.getFiles();

        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        softly.assertThat(files).as("Set of all files of issues").containsExactlyInAnyOrder(testFileName1, testFileName2);
        softly.assertThat(issues).hasNumberOfFiles(expectedNumberOfFiles);
        softly.assertAll();
    }

    /** Verifies issue can be get by property. */
    @Test
    void testGetPropertiesMethodReturnsTheCorrectProperties() {
        Priority expectedPriorityLow = Priority.LOW;
        Priority expectedPriorityNormal = Priority.NORMAL;
        Priority expectedPriorityHigh = Priority.HIGH;
        Issues issues = getIssuesWithThreeIssue(expectedPriorityLow, expectedPriorityNormal, expectedPriorityHigh);

        Function<Issue, Priority> getPrioritiesFunction = (final Issue issue) -> {
            return issue.getPriority();
        };


        SortedSet<Priority> issuesPriorities = issues.getProperties(getPrioritiesFunction);

        assertThat(issuesPriorities).as("All issue priorities contained in issues").containsExactlyInAnyOrder(expectedPriorityHigh, expectedPriorityNormal, expectedPriorityLow);
    }

    /** Verifies issues class can be copied. */
    @Test
    void testCopyMethod() {
        Issues issues = getIssuesWithThreeIssue(Priority.HIGH, Priority.NORMAL, Priority.NORMAL);
        ImmutableSet<Issue> expectedElements = issues.all();

        Issues copiedIssues = issues.copy();
        ImmutableSet<Issue> copiedElements = copiedIssues.all();

        assertThat(copiedElements).as("All elements of issues").containsExactlyElementsOf(expectedElements);
    }

    /**
     * Generates a issues object containing one issue. The issue has the given parameters.
     *
     * @param priority The priority of the issue.
     * @return issues A issues object containing one issue.
     */
    private Issues getIssuesWithOneIssue(final Priority priority) {
        Issues issues = new Issues();
        Issue issue = new IssueBuilder().setPriority(priority).build();
        issues.add(issue);

        return issues;
    }

    /**
     * Generates a issues object containing three issues. The issues has the given parameters.
     *
     * @param priority1 The priority of the first issue.
     * @param priority2 The priority of the second issue.
     * @param priority3 The priority of the third issue.
     * @return issues A issues object containing three issue.
     */
    private Issues getIssuesWithThreeIssue(final Priority priority1, final Priority priority2, final Priority priority3) {
        Issues issues = new Issues();
        Issue issue1 = new IssueBuilder().setPriority(priority1).build();
        Issue issue2 = new IssueBuilder().setPriority(priority2).build();
        Issue issue3 = new IssueBuilder().setPriority(priority3).build();
        issues.add(issue1);
        issues.add(issue2);
        issues.add(issue3);

        return issues;
    }

    /**
     * Returns a issues object containing the given issue objects.
     *
     * @param givenIssue The issue objects to add to the returning issues object.
     * @return issues An issues object containing all given issue objects.
     */
    private Issues getIssuesWithGivenIssue(final Issue... givenIssue) {
        Issues issues = new Issues();

        for (Issue issue : givenIssue) {
            issues.add(issue);
        }

        return issues;
    }


}