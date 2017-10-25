package edu.hm.hafner.analysis.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale.Category;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.AnalysisSoftAssertions;

/**
 * Created by Sarah on 19.10.2017.
 */
public class IssuesTest {

    @Test
    void addIssueToIssues(){
        Issues issues = new Issues();
        Issue issue = new IssueBuilder().setPriority(Priority.HIGH).build();
        int expectedSize = 1;
        Issue returnedIssue = issues.add(issue);
        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        softly.assertThat(returnedIssue).hasPriority(Priority.HIGH);
        softly.assertThat(issues).hasSize(expectedSize).
                hasHighPrioritySize(1).
                hasLowPrioritySize(0).
                hasNormalPrioritySize(0);
    }

    @Test
    void addCollectionToIsses(){
        Issues issues1 = new Issues();
        Issues issues2 = new Issues();
        Issue issue1 = new IssueBuilder().setPriority(Priority.HIGH).build();
        issues2.add(issue1);
        int expectedSize = 1;
        Collection<? extends Issue> returnedIssues = issues1.addAll(issues2.all());
        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        softly.assertThat(returnedIssues).hasSize(expectedSize);
    }

    @Test
    void removeElementFromListWithOneElement(){
        Issue issue1 = new IssueBuilder().build();
        Issues issues = new Issues();
        issues.add(issue1);
        int expectedSize = 0;
        UUID id = issue1.getId();

        Issue removedIssue = issues.remove(id);

        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        softly.assertThat(issues).hasSize(expectedSize);
        softly.assertThat(removedIssue).hasUuid(id);
    }

    @Test
    void removeElementFromListWithTwoEments(){
        Issue issue1 = new IssueBuilder().build();
        Issue issue2 = new IssueBuilder().build();
        Issues issues = new Issues();
        issues.add(issue1);
        issues.add(issue2);
        int expectedSize = 1;
        UUID id = issue1.getId();

        Issue removedIssue = issues.remove(id);

        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        softly.assertThat(issues).hasSize(expectedSize);
        softly.assertThat(removedIssue).hasUuid(id);
    }

    @Test
    void findElementById(){
        Issue issue1 = new IssueBuilder().build();
        Issue issue2 = new IssueBuilder().build();
        Issues issues = new Issues();
        issues.add(issue1);
        issues.add(issue2);
        int expectedSize = 2;
        UUID id = issue1.getId();

        Issue foundIssue = issues.findById(id);

        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        softly.assertThat(issues).hasSize(expectedSize);
        softly.assertThat(foundIssue).hasUuid(id);
    }

    @Test
    void raiseNoSuchElementExceptionFindById(){
        Issue issue1 = new IssueBuilder().build();
        Issue issue2 = new IssueBuilder().build();
        Issue issue3 = new IssueBuilder().build();
        Issues issues = new Issues();
        issues.add(issue1);
        issues.add(issue2);
        UUID id = issue3.getId();

        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();

        softly.assertThatThrownBy(() -> issues.findById(id)) .isInstanceOf(NoSuchElementException.class);
    }
}
