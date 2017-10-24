package edu.hm.hafner.analysis;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import static edu.hm.hafner.analysis.IssueAssert.assertThat;

/**
 * Tests the class {@link Issues}.
 *
 * @author slausch
 */
class IssuesTest {
    /**
     * Builds a dummy Issue for testing purposes.
     * @return Issue dummy
     */
    private Issue buildTestIssue(){
        Issue issue = new IssueBuilder()
                .setLineStart(1)
                .setLineEnd(2)
                .setMessage(" message  ")
                .setFileName("\\filename")
                .setCategory("category")
                .setPriority(Priority.NORMAL)
                .setColumnStart(1)
                .setColumnEnd(2)
                .setDescription("\ndescription with spaces  \t")
                .setPackageName("packageName")
                .setType("type")
                .build();
        return issue;
    }
    /**Verifies correct setup of initialized Issues.*/
    @Test
    void testInitialSizeZero(){
        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        Issues issues = new Issues();
        Issue testingIssue = buildTestIssue();

        softly.assertThat(issues).hasSize(0);
        softly.assertThat(issues).hasNormalPrioritySize(0);
        softly.assertThat(issues).hasLowPrioritySize(0);
        softly.assertThat(issues).hasHighPrioritySize(0);
        softly.assertThat(issues).hasNumberOfFiles(0);
        softly.assertThatThrownBy(()-> issues.get(0))
                .isInstanceOf(IndexOutOfBoundsException.class);
        softly.assertThatThrownBy(()-> issues.findById(testingIssue.getId()))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("No issue found with id %s.", testingIssue.getId());
        softly.assertAll();
    }
    /**Verifies whether Issues changes correctly when adding and removing one Issue.*/
    @Test
    void testIssuesAddAndRemoveOneElement() {
        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        Issues issues = new Issues();
        Issue testingIssue = buildTestIssue();
        //Add a new issue and test whether issues changes accordingly
        issues.add(testingIssue);
        softly.assertThat(issues).hasSize(1);
        softly.assertThat(issues).hasLowPrioritySize(0);
        softly.assertThat(issues).hasNormalPrioritySize(1);
        softly.assertThat(issues).hasHighPrioritySize(0);
        softly.assertThat(issues).contains(testingIssue);
        softly.assertThat(issues).hasNumberOfFiles(1);
        //Remove the issue and test whether issues changes accordingly
        issues.remove(testingIssue.getId());
        softly.assertThat(issues).hasSize(0);
        softly.assertThat(issues).hasLowPrioritySize(0);
        softly.assertThat(issues).hasNormalPrioritySize(0);
        softly.assertThat(issues).hasHighPrioritySize(0);
        softly.assertThat(issues).hasNumberOfFiles(0);
        softly.assertThatThrownBy(() -> issues.get(0))
                .isInstanceOf(IndexOutOfBoundsException.class);
        softly.assertThatThrownBy(() -> issues.findById(testingIssue.getId()))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("No issue found with id %s.", testingIssue.getId());

        softly.assertAll();
    }
    /**Verifies that it is not possible to delete an Issue twice.*/
    @Test
    void testNoDoubleDeletionPossible(){
        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        Issues issues = new Issues();
        Issue testingIssue = buildTestIssue();
        issues.add(testingIssue);
        issues.remove(testingIssue.getId());
        softly.assertThatThrownBy(()-> issues.remove(testingIssue.getId()))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("No issue found with id %s.", testingIssue.getId());
        softly.assertAll();
    }
    /**Verifies whether Issues can find an specific Issue by ID.*/
    @Test
    void testFindingIssuesById(){
        Issues issues = new Issues();
        Issue testingIssue = buildTestIssue();
        issues.add(testingIssue);
        Issue foundIssue = issues.findById(testingIssue.getId());
        assertThat(testingIssue).isEqualTo(foundIssue);
    }
    /**Verifies whether Issues can add Issue elements from another Collection.*/
    @Test
    void testAddingIssuesFromCollection(){
        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        Issues issues = new Issues();
        Collection<Issue> issuesToAdd = new ArrayList<>();

        issuesToAdd.add(buildTestIssue());
        issuesToAdd.add(new IssueBuilder().build());

        issues.addAll(issuesToAdd);

        softly.assertThat(issues).hasSize(2);
        softly.assertThat(issues).hasLowPrioritySize(0);
        softly.assertThat(issues).hasNormalPrioritySize(2);
        softly.assertThat(issues).hasHighPrioritySize(0);
        softly.assertThat(issues).hasNumberOfFiles(2);
        softly.assertAll();
    }
    /**Verifies whether you can create a correct copy of Issues.*/
    @Test
    void testCopyingIssues(){
        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        Issues issues = new Issues();
        Issue testingIssue = buildTestIssue();
        issues.add(testingIssue);

        Issues copyOfIssues = issues.copy();

        softly.assertThat(copyOfIssues).hasSize(1);
        softly.assertThat(copyOfIssues).hasLowPrioritySize(0);
        softly.assertThat(copyOfIssues).hasNormalPrioritySize(1);
        softly.assertThat(copyOfIssues).hasHighPrioritySize(0);
        softly.assertThat(copyOfIssues).hasNumberOfFiles(1);
    }
    /**Verifies whether Issues can find all contained Issue elements with a specific Property.*/
    @Test
    void testFindingIssuesByProperty(){
        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        Predicate<Issue> issuesWithNormalPriority = (Issue issue) -> issue.getPriority().equals(Priority.NORMAL);
        Issues issues = new Issues();
        Issue testingIssue = buildTestIssue();
        issues.add(testingIssue);

        ImmutableList<Issue> issuesMatching = issues.findByProperty(issuesWithNormalPriority);
        softly.assertThat(issuesMatching).hasSize(1);
        softly.assertAll();
        assertThat(testingIssue).isEqualTo(issuesMatching.get(0));

    }

}