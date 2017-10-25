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
        int expectedSize = 1;

        issues.add(highPrioIssue);
        ImmutableSet<Issue> allIssues = issues.all();

        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        softly.assertThat(issues).as("Issues object containing 1 issue").hasSize(expectedSize);
        softly.assertThat(allIssues).as("List with all issues").containsExactly(highPrioIssue);
        softly.assertThat(issues).as("Number of added high priority issues").hasHighPrioritySize(expectedSize);
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
        softly.assertThat(issues).as("Issues object with 3 issues").hasSize(expectedSize)
                .hasHighPrioritySize(highPrioSize)
                .hasNormalPrioritySize(normalPrioSize)
                .hasLowPrioritySize(lowPrioSize);
        softly.assertThat(allIssues).as("List with all issues").containsExactly(highPrioIssue, normalPrioIssue, lowPrioIssue);
        softly.assertAll();
    }

    /** Verfies that a issue can be removes from issues. */
    @Test
    void testRemoveMethodRemovesIssueCorrect() {
        Issues issues = new Issues();
        Issue highPrioIssue = new IssueBuilder().setPriority(Priority.HIGH).build();
        issues.add(highPrioIssue);
        int expectedSize = 0;


        Issue removedIssue = issues.remove(highPrioIssue.getId());

        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        softly.assertThat(issues).as("Issues object with 0 issues.").hasSize(expectedSize);
        softly.assertThat(removedIssue).as("Removed issue").isSameAs(highPrioIssue);
        softly.assertAll();
    }

    /** Verifies that an {@link NoSuchElementException} is thrown if an issue that is not in issues is removed. */
    @Test
    void testRemoveMethodThrowsExceptionWhenRemovingANotContainedElement() {
        Issues issues = new Issues();
        Issue highPrioIssue = new IssueBuilder().setPriority(Priority.HIGH).build();
        Issue notAddedIssue = new IssueBuilder().setPriority(Priority.NORMAL).build();
        issues.add(highPrioIssue);
        int expectedSize = 1;


        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        softly.assertThatThrownBy(() -> issues.remove(notAddedIssue.getId()))
                .isInstanceOf(NoSuchElementException.class)
                .hasNoCause();
        softly.assertThat(issues).as("Issues object with 1 issue").hasSize(expectedSize);
        softly.assertAll();
    }

    /** Verifies that a issue of issues can be found by id. */
    @Test
    void testFindByIdReturnsTheCorrectElement() {
        Issues issues = new Issues();
        Issue highPrioIssue = new IssueBuilder().setPriority(Priority.HIGH).build();
        Issue normalPrioIssue = new IssueBuilder().setPriority(Priority.NORMAL).build();
        UUID highPrioIssueID = highPrioIssue.getId();
        issues.add(highPrioIssue);
        issues.add(normalPrioIssue);

        Issue issueFoundByID = issues.findById(highPrioIssueID);

        assertThat(issueFoundByID).as("Issue found by id").isSameAs(highPrioIssue);
    }

    /** Verifies that an {@link NoSuchElementException} is thrown if searching for an id that is not in issues. */
    @Test
    void testIfFindByIdThrowsNoSuchElementExceptionWhenSearchingANotContainedID() {
        Issues issues = new Issues();
        Issue highPrioIssue = new IssueBuilder().setPriority(Priority.HIGH).build();
        Issue normalPrioIssue = new IssueBuilder().setPriority(Priority.NORMAL).build();
        UUID notContainedUUID = normalPrioIssue.getId();
        issues.add(highPrioIssue);

        assertThatThrownBy(() -> issues.findById(notContainedUUID))
                .isInstanceOf(NoSuchElementException.class)
                .hasNoCause();
    }

    /** Verifies that an issue can be found by property. */
    @Test
    void testFindByPropertyReturnsAllTheElementsWithGivenCriterion() {
        Issues issues = new Issues();
        Issue highPrioIssue = new IssueBuilder().setPriority(Priority.HIGH).build();
        Issue highPrioIssue2 = new IssueBuilder().setPriority(Priority.HIGH).build();
        Issue lowPrioIssue = new IssueBuilder().setPriority(Priority.LOW).build();
        issues.add(highPrioIssue);
        issues.add(highPrioIssue2);
        issues.add(lowPrioIssue);
        Predicate<Issue> criterionHighPrio = new Predicate<Issue>() {
            @Override
            public boolean test(final Issue issue) {
                return issue.getPriority() == Priority.HIGH;
            }
        };

        ImmutableList<Issue> highPrioIssues = issues.findByProperty(criterionHighPrio);

        assertThat(highPrioIssues).as("List of all issues with high priority").containsExactly(highPrioIssue, highPrioIssue2);
    }

    /** Verifies that it is possible to iterate over all issue's by using an iterator. */
    @Test
    void testIssuesIterator() {
        Issues issues = new Issues();
        Issue highPrioIssue = new IssueBuilder().setPriority(Priority.HIGH).build();
        Issue highPrioIssue2 = new IssueBuilder().setPriority(Priority.HIGH).build();
        Issue lowPrioIssue = new IssueBuilder().setPriority(Priority.LOW).build();
        issues.add(highPrioIssue);
        issues.add(highPrioIssue2);
        issues.add(lowPrioIssue);
        int expectedIteratorSize = 3;

        Iterator<Issue> issueIterator = issues.iterator();

        assertThat(issueIterator).as("Iterator over issues").containsExactly(highPrioIssue, highPrioIssue2, lowPrioIssue);


    }


    /** Verifies that getSize() returns the correct size. */
    @Test
    void testGetSizeMethodsReturnsCorrectSize() {
        Issues issues = new Issues();
        Issue highPrioIssue = new IssueBuilder().setPriority(Priority.HIGH).build();
        Issue highPrioIssue2 = new IssueBuilder().setPriority(Priority.HIGH).build();
        Issue lowPrioIssue = new IssueBuilder().setPriority(Priority.LOW).build();
        int expectedSize = 3;
        issues.add(highPrioIssue);
        issues.add(highPrioIssue2);
        issues.add(lowPrioIssue);

        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        softly.assertThat(issues).as("Issues object with 3 issues").hasSize(expectedSize);
        softly.assertThat(issues.size()).as("Size of issues object with 3 issues").isEqualTo(expectedSize);
        softly.assertAll();
    }

    /** Verifies that the correct priority sizes are returned. */
    @Test
    void testGetHighNormalAndLowPrioritySizeMethods() {
        Issues issues = new Issues();
        Issue normalPrioIssue = new IssueBuilder().setPriority(Priority.NORMAL).build();
        Issue lowPrioIssue = new IssueBuilder().setPriority(Priority.LOW).build();
        Issue highPrioIssue = new IssueBuilder().setPriority(Priority.HIGH).build();
        Issue highPrioIssue2 = new IssueBuilder().setPriority(Priority.HIGH).build();
        int expectedHighPrioSize = 2;
        int expectedNormalPrioSize = 1;
        int expectedLowPrioSize = 1;
        issues.add(highPrioIssue);
        issues.add(highPrioIssue2);
        issues.add(normalPrioIssue);
        issues.add(lowPrioIssue);

        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        softly.assertThat(issues).as("Issues with high priority size 2").hasHighPrioritySize(expectedHighPrioSize);
        softly.assertThat(issues).as("Issues with normal priority size 1").hasNormalPrioritySize(expectedNormalPrioSize);
        softly.assertThat(issues).as("Issues with normal priority size 1").hasLowPrioritySize(expectedLowPrioSize);
        softly.assertAll();
    }

    /** Verifies an issue can be get by index. */
    @Test
    void testGetMethodReturnsTheCorrectElementAtTheGivenPosition() {
        Issues issues = new Issues();
        Issue highPrioIssue = new IssueBuilder().setPriority(Priority.HIGH).build();
        Issue normalPrioIssue = new IssueBuilder().setPriority(Priority.NORMAL).build();
        Issue normalPrioIssue2 = new IssueBuilder().setPriority(Priority.NORMAL).build();
        int wantedIndex = 1;
        issues.add(highPrioIssue);
        issues.add(normalPrioIssue);
        issues.add(normalPrioIssue2);

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
        Issues issues = new Issues();
        Issue highPrioIssue = new IssueBuilder().setPriority(Priority.HIGH).build();
        Issue normalIssue = new IssueBuilder().setPriority(Priority.NORMAL).build();
        String expectedString = "2 issues";
        issues.add(highPrioIssue);
        issues.add(normalIssue);

        String issuesToString = issues.toString();

        assertThat(issuesToString).as("Issues object represented as String").isEqualTo(expectedString);
    }

    /** Verifies isuues can be get by file name. */
    @Test
    void testGetFilesMethodReturnsTheCorrectFileNames() {
        Issues issues = new Issues();
        String testFileName = "testFile";
        Issue testIssue = new IssueBuilder().setFileName(testFileName).build();
        issues.add(testIssue);

        SortedSet<String> files = issues.getFiles();
        String fileNameGetByGetFileMethod = files.first();

        assertThat(fileNameGetByGetFileMethod).as("File name of the test issue").isEqualTo(testFileName);
    }

    /** Verifies getNumberOfFiles() method returns the correct size. */
    @Test
    void testGetNumberOfFilesMethodReturnsTheCorrectSize() {
        Issues issues = new Issues();
        String testFileName1 = "testFile1";
        String testFileName2 = "testFile2";
        Issue testIssue1 = new IssueBuilder().setFileName(testFileName1).build();
        Issue testIssue2 = new IssueBuilder().setFileName(testFileName2).build();
        issues.add(testIssue1);
        issues.add(testIssue2);
        int expectedNumberOfFiles = 2;

        int numberOfFiles = issues.getNumberOfFiles();

        assertThat(numberOfFiles).as("Number of affected files").isEqualTo(expectedNumberOfFiles);
    }

    /** Verifies issue can be get by property. */
    @Test
    void testGetPropertiesMethodReturnsTheCorrectProperties() {
        Issues issues = new Issues();
        Issue lowPrioIssue = new IssueBuilder().setPriority(Priority.LOW).build();
        issues.add(lowPrioIssue);
        Priority expectedProperty = Priority.LOW;
        Function<Issue, Priority> getPrioritiesFunction = new Function<Issue, Priority>() {
            @Override
            public Priority apply(final Issue issue) {
                return issue.getPriority();
            }
        };


        SortedSet<Priority> issuesPriorities = issues.getProperties(getPrioritiesFunction);
        Priority firstPriority = issuesPriorities.first();

        assertThat(firstPriority).as("Issue priority contained in issues").isEqualTo(expectedProperty);
    }

    /** Verifies issues class can be copied. */
    @Test
    void testCopyMethod() {
        Issues issues = new Issues();
        Issue normalPrioIssue = new IssueBuilder().setPriority(Priority.NORMAL).build();
        Issue lowPrioIssue = new IssueBuilder().setPriority(Priority.LOW).build();
        Issue highPrioIssue = new IssueBuilder().setPriority(Priority.HIGH).build();
        Issue highPrioIssue2 = new IssueBuilder().setPriority(Priority.HIGH).build();
        issues.add(highPrioIssue);
        issues.add(highPrioIssue2);
        issues.add(normalPrioIssue);
        issues.add(lowPrioIssue);
        ImmutableSet<Issue> expectedElements = issues.all();

        Issues copiedIssues = issues.copy();
        ImmutableSet<Issue> copiedElements = copiedIssues.all();

        assertThat(copiedElements).as("All elements of issues").isEqualTo(expectedElements);
    }
}