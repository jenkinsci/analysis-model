package edu.hm.hafner.analysis.assertions;

import java.util.Iterator;
import java.util.Objects;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import com.google.common.collect.ImmutableSet;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;


/**
 * Custom assertion for {@link Issues}
 * @author Raphael Furch
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
        Assertions.assertThat(actual.iterator()).contains(issue);
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
        Assertions.assertThat(actual.iterator()).doesNotContain(issue);
        // Return this for Fluent.
        return this;
    }

    /**
     * Check if a list of elements exist and get over iterator.
     * @param issues = expected.
     * @return assertion.
     */
    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasElements(final Issue... issues) {
        // check actual not null
        isNotNull();
        // check condition
        Assertions.assertThat(actual.iterator()).contains(issues);
        // Return this for Fluent.
        return this;
    }

    /**
     * Check if a list of elements exists and check over all() method.
     * @param issues = expected.
     * @return assertion.
     */
    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasAllElements(final Issue... issues) {
        // check actual not null
        isNotNull();
        // check condition
        Assertions.assertThat(actual.iterator()).containsExactly(issues);
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
        propertyEqualsCheck(actual.getSize(), size);
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
        propertyEqualsCheck(actual.getHighPrioritySize(), size);
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
        propertyEqualsCheck(actual.getNormalPrioritySize(), size);
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
        propertyEqualsCheck(actual.getLowPrioritySize(), size);
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
        Assertions.assertThat(actual.all().asList().get(index)).isSameAs(issue);
        // Return this for Fluent.
        return this;
    }

    /**
     * Check files from elements.
     * @param files = expected.
     * @return assertions.
     */
    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasFiles(final String... files) {
        // check actual not null
        isNotNull();
        // check condition
        Assertions.assertThat(actual.getFiles()).containsExactly(files);
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
        propertyEqualsCheck(actual.getNumberOfFiles(), numberOfFiles);
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
        propertyEqualsCheck(actual.toString(), expected);
        return this;
    }

    //<editor-fold desc="Helper">



    /**
     * Easy message generation.
     * @param actualValue = actual.
     * @param expected = expected.
       * @param <T> = generic.
     */
    private <T> void propertyEqualsCheck(final T actualValue, final T expected){
        if (!Objects.equals(actualValue, expected)) {
            failWithMessage("%nExpecting:%n <%s>%nto be equal to:%n <%s>%nbut was not.", expected, actualValue);
        }
    }


    //</editor-fold>



}
