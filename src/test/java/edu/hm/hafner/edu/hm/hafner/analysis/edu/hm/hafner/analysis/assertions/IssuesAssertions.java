package edu.hm.hafner.edu.hm.hafner.analysis.edu.hm.hafner.analysis.assertions;

import java.util.Objects;
import java.util.SortedSet;
import java.util.stream.Stream;

import org.assertj.core.api.AbstractAssert;

import com.google.common.collect.Streams;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;

public class IssuesAssertions extends AbstractAssert<IssuesAssertions, Issues> {

    //<editor-fold desc="Creation">
    public IssuesAssertions(final Issues actual) {
        super(actual, IssueAssertions.class);
    }
    public static IssuesAssertions assertThat(final Issues actual){
        return new IssuesAssertions(actual);
    }
    //</editor-fold>

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasElement(final Issue issue) {
        // check actual not null
        this.isNotNull();
        // check condition
        if (!this.getIteratorOfActualAsStream().anyMatch(i -> Objects.equals(i, issue))) {
            failWithMessage("Element <%s> was not in list", issue);
        }
        // Return this for Fluent.
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasSize(final int size) {
        // check actual not null
        this.isNotNull();
        // check condition
        propertyEqualsCheck(actual.getSize(), size, "size");
        // Return this for Fluent.
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasHighPrioritySize(final int size) {
        // check actual not null
        this.isNotNull();
        // check condition
        propertyEqualsCheck(actual.getHighPrioritySize(), size, "high priority size");
        // Return this for Fluent.
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasNormalPrioritySize(final int size) {
        // check actual not null
        this.isNotNull();
        // check condition
        propertyEqualsCheck(actual.getNormalPrioritySize(), size, "normal priority size");
        // Return this for Fluent.
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasLowPrioritySize(final int size) {
        // check actual not null
        this.isNotNull();
        // check condition
        propertyEqualsCheck(actual.getLowPrioritySize(), size, "low priority size");
        // Return this for Fluent.
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasElementAtPosition(final int index, final Issue issue) {
        // check actual not null
        this.isNotNull();
        // check condition
        final long count = getIteratorOfActualAsStream().count();
        if ( count <= index) {
            failWithMessage("Expected issues's get-Method index <%d> out of range <%d>", index, count);
        }
        final Issue getIssue = actual.get(index);
        if (!Objects.equals(getIssue, issue)) {
            failWithMessage("Expected issues contains <%s> at position <%d> but was <%s>", issue, index, getIssue);
        }
        // Return this for Fluent.
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasFiles(final SortedSet<String> files) {
        // check actual not null
        this.isNotNull();
        // check condition
        propertyEqualsCheck(actual.getFiles(), files, "files");
        // Return this for Fluent.
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasNumberOfFiles(final int numberOfFiles) {
        // check actual not null
        this.isNotNull();
        // check condition
        propertyEqualsCheck(actual.getNumberOfFiles(), numberOfFiles, "NumberOfFiles");
        // Return this for Fluent.
        return this;
    }


    private Stream<Issue> getIteratorOfActualAsStream() {

        return Streams.stream(this.actual.iterator());
    }

    private <T> void propertyEqualsCheck(final T actualValue, final T expected, final String propertyName){
        if (!Objects.equals(actualValue, expected)) {
            failWithMessage("Expected issues's "+propertyName+" to be <%s> but was <%s>", expected, actualValue);
        }
    }



}
