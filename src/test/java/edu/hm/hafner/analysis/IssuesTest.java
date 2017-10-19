package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.SortedSet;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

import static edu.hm.hafner.analysis.assertj.IssuesAssert.assertThat;
import static edu.hm.hafner.analysis.assertj.IssueAssert.assertThat;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;

import edu.hm.hafner.util.NoSuchElementException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

class IssuesTest {
    //TODO javadoc

    @Test
    void testAddMethodAddsIssueCorrect() {
        final Issues issues = new Issues();
        final Issue highPrioIssue = new IssueBuilder().setPriority(Priority.HIGH).build();
        final int expectedSize = 1;

        issues.add(highPrioIssue);
        ImmutableSet<Issue> allIssues = issues.all();

        assertThat(allIssues).as("Number of added issues").hasSize(expectedSize);
        assertThat(allIssues.contains(highPrioIssue)).as("Contains the added issue").isTrue();
        assertThat(issues).as("Number of added high priority issues").hasHighPrioritySize(expectedSize);
    }

    @Test
    void testAddAllMethodAddsACollectionOfIssuesCorrect() {
        final Issues issues = new Issues();
        final Issue highPrioIssue = new IssueBuilder().setPriority(Priority.HIGH).build();
        final Issue normalPrioIssue = new IssueBuilder().setPriority(Priority.NORMAL).build();
        final Issue lowPrioIssue = new IssueBuilder().setPriority(Priority.LOW).build();
        ArrayList<Issue> issueList = new ArrayList<>();
        issueList.add(highPrioIssue);
        issueList.add(normalPrioIssue);
        issueList.add(lowPrioIssue);
        final int expectedSize = 3;
        final int highPrioSize = 1;
        final int normalPrioSize = 1;
        final int lowPrioSize = 1;

        issues.addAll(issueList);
        ImmutableSet<Issue> allIssues = issues.all();

        assertThat(allIssues).as("Number of added issues").hasSize(expectedSize);
        assertThat(issues).as("Number of added high priority issues").hasHighPrioritySize(highPrioSize);
        assertThat(issues).as("Number of added normal priority issues").hasNormalPrioritySize(normalPrioSize);
        assertThat(issues).as("Number of added low priority issues").hasLowPrioritySize(lowPrioSize);
        for (Issue issue : issueList) {
            assertThat(allIssues.contains(issue)).as("Contains the added issue").isTrue();
        }
    }

    @Test
    void testRemoveMethodRemovesIssueCorrect() {
        final Issues issues = new Issues();
        final Issue highPrioIssue = new IssueBuilder().setPriority(Priority.HIGH).build();
        issues.add(highPrioIssue);
        final int expectedSize = 0;


        Issue removedIssue = issues.remove(highPrioIssue.getId());
        ImmutableSet<Issue> allIssues = issues.all();

        assertThat(allIssues).as("Number of issues").hasSize(expectedSize);
        assertThat(removedIssue).as("Removed issue").isSameAs(highPrioIssue);
    }

    @Test
    void testRemoveMethodThrowsExceptionWhenRemovingANotContainedElement() {
        final Issues issues = new Issues();
        final Issue highPrioIssue = new IssueBuilder().setPriority(Priority.HIGH).build();
        final Issue notAddedIssue = new IssueBuilder().setPriority(Priority.NORMAL).build();
        issues.add(highPrioIssue);
        final int expectedSize = 1;



        assertThatThrownBy(() -> issues.remove(notAddedIssue.getId()))
                .isInstanceOf(NoSuchElementException.class)
                .hasNoCause();

        ImmutableSet<Issue> allIssues = issues.all();
        assertThat(allIssues).as("Number of issues").hasSize(expectedSize);
    }

    @Test
    @Disabled
    //TODO testIfAllMethodReturnsCopyOfInternElements is not working correctly
    void testIfAllMethodReturnsCopyOfInternElements() {
        final Issues issues = new Issues();
        final Issue highPrioIssue = new IssueBuilder().setPriority(Priority.HIGH).build();
        issues.add(highPrioIssue);

        final Issue issueGetByAllMethod = issues.all().iterator().next();

        assertThat(issueGetByAllMethod).as("Issue reference get by issues all method").isNotSameAs(highPrioIssue);
    }

    @Test
    void testFindByIdReturnsTheCorrectElement(){
        final Issues issues = new Issues();
        final Issue highPrioIssue = new IssueBuilder().setPriority(Priority.HIGH).build();
        final Issue normalPrioIssue = new IssueBuilder().setPriority(Priority.NORMAL).build();
        final UUID highPrioIssueID = highPrioIssue.getId();
        issues.add(highPrioIssue);
        issues.add(normalPrioIssue);

        Issue issueFoundByID = issues.findById(highPrioIssueID);

        assertThat(issueFoundByID).as("Issue found by id").isSameAs(highPrioIssue);
    }

    @Test
    void testIfFindByIdThrowsNoSuchElementExceptionWhenSearchingANotContainedID(){
        final Issues issues = new Issues();
        final Issue highPrioIssue = new IssueBuilder().setPriority(Priority.HIGH).build();
        final Issue normalPrioIssue = new IssueBuilder().setPriority(Priority.NORMAL).build();
        final UUID notContainedUUID = normalPrioIssue.getId();
        issues.add(highPrioIssue);

        assertThatThrownBy(() -> issues.findById(notContainedUUID))
                .isInstanceOf(NoSuchElementException.class)
                .hasNoCause();
    }

    @Test
    void testFindByPropertyReturnsAllTheElementsWithGivenCriterion(){
        final Issues issues = new Issues();
        final Issue highPrioIssue = new IssueBuilder().setPriority(Priority.HIGH).build();
        final Issue highPrioIssue2 = new IssueBuilder().setPriority(Priority.HIGH).build();
        final Issue lowPrioIssue = new IssueBuilder().setPriority(Priority.LOW).build();
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

        for(Issue issue : highPrioIssues){
            assertThat(issue).as("Issue with high priority").hasPriority(Priority.HIGH);
        }
    }

    //TODO iterator test


    @Test
    void testGetSizeMethodsReturnsCorrectSize(){
        final Issues issues = new Issues();
        final Issue highPrioIssue = new IssueBuilder().setPriority(Priority.HIGH).build();
        final Issue highPrioIssue2 = new IssueBuilder().setPriority(Priority.HIGH).build();
        final Issue lowPrioIssue = new IssueBuilder().setPriority(Priority.LOW).build();
        final int expectedSize = 3;
        issues.add(highPrioIssue);
        issues.add(highPrioIssue2);
        issues.add(lowPrioIssue);

        assertThat(issues).as("Issues object with 3 issues").hasSize(expectedSize);
        assertThat(issues.size()).as("Size of issues object with 3 issues").isEqualTo(expectedSize);
    }

    @Test
    void testGetHighNormalAndLowPrioritySizeMethods(){
        final Issues issues = new Issues();
        final Issue normalPrioIssue = new IssueBuilder().setPriority(Priority.NORMAL).build();
        final Issue lowPrioIssue = new IssueBuilder().setPriority(Priority.LOW).build();
        final Issue highPrioIssue = new IssueBuilder().setPriority(Priority.HIGH).build();
        final Issue highPrioIssue2 = new IssueBuilder().setPriority(Priority.HIGH).build();
        final int expectedHighPrioSize = 2;
        final int expectedNormalPrioSize = 1;
        final int expectedLowPrioSize = 1;
        issues.add(highPrioIssue);
        issues.add(highPrioIssue2);
        issues.add(normalPrioIssue);
        issues.add(lowPrioIssue);

        assertThat(issues).as("Issues with high priority size 2").hasHighPrioritySize(expectedHighPrioSize);
        assertThat(issues).as("Issues with normal priority size 1").hasNormalPrioritySize(expectedNormalPrioSize);
        assertThat(issues).as("Issues with normal priority size 1").hasLowPrioritySize(expectedLowPrioSize);
    }

    //TODO get method
    /*
    @Test
    void testGetMethodReturnsTheCorrectElementAtTheGivenPosition(){
        final Issues issues = new Issues();
        final Issue highPrioIssue = new IssueBuilder().setPriority(Priority.HIGH).build();
        final Issue normalIssue = new IssueBuilder().setPriority(Priority.NORMAL).build();
        final int wantedIndex = 1;

        issues.add(highPrioIssue);
        issues.add(normalIssue);
        //final Issue issueAtWantedIndex = issues.

    }*/

    @Test
    void testToStringMethod(){
        final Issues issues = new Issues();
        final Issue highPrioIssue = new IssueBuilder().setPriority(Priority.HIGH).build();
        final Issue normalIssue = new IssueBuilder().setPriority(Priority.NORMAL).build();
        final String expectedString = "2 issues";
        issues.add(highPrioIssue);
        issues.add(normalIssue);

        final String issuesToString = issues.toString();

        assertThat(issuesToString).as("Issues object represented as String").isEqualTo(expectedString);
    }

    @Test
    void testGetFilesMethodReturnsTheCorrectFileNames(){
        final Issues issues = new Issues();
        final String testFileName = "testFile";
        final Issue testIssue = new IssueBuilder().setFileName(testFileName).build();
        issues.add(testIssue);

        SortedSet<String> files = issues.getFiles();
        String fileNameGetByGetFileMethod = files.first();

        assertThat(fileNameGetByGetFileMethod).as("File name of the test issue").isEqualTo(testFileName);
    }

    @Test
    void testGetNumberOfFilesMethodReturnsTheCorrectSize(){
        final Issues issues = new Issues();
        final String testFileName1 = "testFile1";
        final String testFileName2 = "testFile2";
        final Issue testIssue1 = new IssueBuilder().setFileName(testFileName1).build();
        final Issue testIssue2 = new IssueBuilder().setFileName(testFileName2).build();
        issues.add(testIssue1);
        issues.add(testIssue2);
        final int expectedNumberOfFiles = 2;

        final int numberOfFiles = issues.getNumberOfFiles();

        assertThat(numberOfFiles).as("Number of affected files").isEqualTo(expectedNumberOfFiles);
    }

    @Test
    void testGetPropertiesMethodReturnsTheCorrectProperties(){
        final Issues issues = new Issues();
        final Issue lowPrioIssue = new IssueBuilder().setPriority(Priority.LOW).build();
        issues.add(lowPrioIssue);
        final Priority expectedProperty = Priority.LOW;
        final Function<Issue, Priority> getPrioritiesFunction = new Function<Issue, Priority>() {
            @Override
            public Priority apply(final Issue issue) {
                return issue.getPriority();
            }
        };


        SortedSet<Priority> issuesPriorities = issues.getProperties(getPrioritiesFunction);
        final Priority firstPriority = issuesPriorities.first();

        assertThat(firstPriority).as("Issue priority contained in issues").isEqualTo(expectedProperty);
    }

    @Test
    void testCopyMethod(){
        final Issues issues = new Issues();
        final Issue normalPrioIssue = new IssueBuilder().setPriority(Priority.NORMAL).build();
        final Issue lowPrioIssue = new IssueBuilder().setPriority(Priority.LOW).build();
        final Issue highPrioIssue = new IssueBuilder().setPriority(Priority.HIGH).build();
        final Issue highPrioIssue2 = new IssueBuilder().setPriority(Priority.HIGH).build();
        issues.add(highPrioIssue);
        issues.add(highPrioIssue2);
        issues.add(normalPrioIssue);
        issues.add(lowPrioIssue);
        final ImmutableSet<Issue> expectedElements = issues.all();

        final Issues copiedIssues = issues.copy();
        final ImmutableSet<Issue> copiedElements = copiedIssues.all();

        assertThat(copiedElements).as("All elements of issues").isEqualTo(expectedElements);
    }


}