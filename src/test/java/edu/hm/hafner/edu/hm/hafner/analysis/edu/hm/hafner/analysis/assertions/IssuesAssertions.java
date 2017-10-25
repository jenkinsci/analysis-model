package edu.hm.hafner.edu.hm.hafner.analysis.edu.hm.hafner.analysis.assertions;

import java.util.Collection;
import java.util.Objects;
import java.util.SortedSet;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.assertj.core.api.AbstractAssert;
import com.google.common.collect.ImmutableSet;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;


/**
 * Custome assertion for issues.
 */
@SuppressWarnings("UnusedReturnValue")
public class IssuesAssertions extends AbstractAssert<IssuesAssertions, Issues> {

    //<editor-fold desc="Creation">
    public IssuesAssertions(final Issues actual) {
        super(actual, IssuesAssertions.class);
    }
    public static IssuesAssertions assertThat(final Issues actualIssues){
        return new IssuesAssertions(actualIssues);
    }
    //</editor-fold>

    /**
     * Check if a element exists.
     * @param issue = expected.
     * @return assertions.
     */
    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasElement(final Issue issue) {
        // check actual not null
        isNotNull();
        // check condition
        if (getStreamOfAllIssues().noneMatch((elem) -> issue.equals(elem))) {
            failWithMessage("Element <%s> was not in list but should", issue);
        }
        return this;
    }

    /**
     * Check if a element not exits.
     * @param issue = not expected.
     * @return assertions.
     */
    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasNotElement(final Issue issue) {
        // check actual not null
       isNotNull();
        // check condition
        if (getStreamOfAllIssues().anyMatch((elem) -> issue.equals(elem))) {
            failWithMessage("Element <%s> was in list but shouldn't", issue);
        }
        // Return this for Fluent.
        return this;
    }

    /**
     * Check if a list of elements exist and get over iterator.
     * @param issues = expected.
     * @return assertion.
     */
    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasElements(final Collection<? extends Issue> issues) {
        // check actual not null
        isNotNull();
        // check condition
        Collection<Issue> currentIssues = getStreamOfAllIssues().collect(Collectors.toList());
        if (currentIssues.size() != issues.size()) {
            failWithMessage("Expected issue's element size to be <%s> but was <%s>", issues.size(), currentIssues.size());
        }
        else if(currentIssues.stream().anyMatch(f -> !issues.contains(f))){
            failWithMessage("Expected issue's elements to be <%s> but was <%s>", issueListToString(issues), issueListToString(currentIssues));
        }
        // Return this for Fluent.
        return this;
    }

    /**
     * Check if a list of elements exists and check over all() method.
     * @param issues = expected.
     * @return assertion.
     */
    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasAllElements(final Collection<? extends Issue> issues) {
        // check actual not null
        isNotNull();
        // check condition
        Collection<Issue> currentIssues = actual.all();
        if (currentIssues.size() != issues.size()) {
            failWithMessage("Expected issue's element size to be <%s> but was <%s>", issues.size(), currentIssues.size());
        }
        else if(currentIssues.stream().anyMatch(f -> !issues.contains(f))){
            failWithMessage("Expected issue's elements to be <%s> but was <%s>", issueListToString(issues), issueListToString(currentIssues));
        }
        // Return this for Fluent.
        return this;
    }

    /**
     * Proof size.
     * @param size = expected.
     * @return assertion.
     */
    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasSize(final int size) {
        // check actual not null
        isNotNull();
        // check condition
        propertyEqualsCheck(actual.getSize(), size, "size");
        // Return this for Fluent.
        return this;
    }

    /**
     * Proof count of elements with high priority.
     * @param size expected.
     * @return assertion.
     */
    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasHighPrioritySize(final int size) {
        // check actual not null
        isNotNull();
        // check condition
        propertyEqualsCheck(actual.getHighPrioritySize(), size, "high priority size");
        // Return this for Fluent.
        return this;
    }

    /**
     * Proof count of elements with normal priority.
     * @param size expected.
     * @return assertion.
     */
    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasNormalPrioritySize(final int size) {
        // check actual not null
        isNotNull();
        // check condition
        propertyEqualsCheck(actual.getNormalPrioritySize(), size, "normal priority size");
        // Return this for Fluent.
        return this;
    }

    /**
     * Proof count of elements with low priority.
     * @param size expected.
     * @return assertion.
     */
    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasLowPrioritySize(final int size) {
        // check actual not null
        isNotNull();
        // check condition
        propertyEqualsCheck(actual.getLowPrioritySize(), size, "low priority size");
        // Return this for Fluent.
        return this;
    }

    /**
     * Proof if a element exists at a special position.
     * @param index = position.
     * @param issue = expected.
     * @return assertion.
     */
    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasElementAtPosition(final int index, final Issue issue) {
        // check actual not null
        isNotNull();
        // check condition
        long count = getStreamOfAllIssues().count();
        if ( count <= index) {
            failWithMessage("Expected issues's get-Method index <%d> out of range <%d>", index, count);
        }
        Issue getIssue = actual.get(index);
        if (!Objects.equals(getIssue, issue)) {
            failWithMessage("Expected issues contains <%s> at position <%d> but was <%s>", issue, index, getIssue);
        }
        // Return this for Fluent.
        return this;
    }

    /**
     * Check files from elements.
     * @param files = expected.
     * @return assertions.
     */
    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasFiles(final SortedSet<String> files) {
        // check actual not null
        isNotNull();
        // check condition
        propertyEqualsCheck(actual.getFiles(), files, "files");
        // Return this for Fluent.
        return this;
    }

    /**
     * Check number of files.
     * @param numberOfFiles = expected.
     * @return assertions.
     */
    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasNumberOfFiles(final int numberOfFiles) {
        // check actual not null
        isNotNull();
        // check condition
        propertyEqualsCheck(actual.getNumberOfFiles(), numberOfFiles, "NumberOfFiles");
        // Return this for Fluent.
        return this;
    }

    /**
     * Check if a issues is a ordered copy of a other.
     * @param issues = expected.
     * @return assertions.
     */
    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions isACopyOf(final Issues issues){
        // check actual not null
        isNotNull();
        // check condition
        if(actual == issues){
            failWithMessage("Issues is no copy its the same");
        }
        ImmutableSet<Issue> actualList = actual.all();
        ImmutableSet<Issue> expectedList = issues.all();
        if (actualList.size() != expectedList.size()) {
            failWithMessage("Issues has a size of <%s> but expect <%s>", actualList.size(), expectedList.size());
        }
        Iterator<Issue> actualIssueIterator = actual.iterator();
        Iterator<Issue> expectedIssueIterator = issues.iterator();
        while(actualIssueIterator.hasNext()){
            Issue actualIssue = actualIssueIterator.next();
            Issue expectedIssue = expectedIssueIterator.next();
            if(!Objects.equals(actualIssue, expectedIssue)){
                failWithMessage("Issues expect element<%s> but was <%s>", expectedIssue, actualIssue);
            }
        }

        return this;
    }


    /**
     * Check toString.
     * @param expected = expected string.
     * @return assertion.
     */
    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions isString(final String expected) {
        // check actual not null
        isNotNull();
        // check condition
        propertyEqualsCheck(actual.toString(), expected, "toString");
        return this;
    }

    //<editor-fold desc="Helper">

    /**
     * Get elements of stream.
     * @return stream.
     */
    private Stream<Issue> getStreamOfAllIssues() {

        return actual.all().stream();
    }

    /**
     * Easy message generation.
     * @param actualValue = actual.
     * @param expected = expected.
     * @param propertyName = Name of property.
     * @param <T> = generic.
     */
    private <T> void propertyEqualsCheck(final T actualValue, final T expected, final String propertyName){
        if (!Objects.equals(actualValue, expected)) {
            failWithMessage("Expected issues's "+propertyName+" to be <%s> but was <%s>", expected, actualValue);
        }
    }

    /**
     * Helper to male a list to string.
     * @param issues = list.
     * @return string.
     */
    private String issueListToString(final Collection<? extends Issue> issues){
        return "{" + issues.stream().map(Issue::toString).reduce((a,b) -> a + "," + b)+ "}";
    }
    //</editor-fold>



}
