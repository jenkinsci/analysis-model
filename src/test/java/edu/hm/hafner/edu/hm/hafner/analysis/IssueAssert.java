package edu.hm.hafner.edu.hm.hafner.analysis;

import java.util.Objects;

import org.assertj.core.api.AbstractAssert;


import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Priority;

public class IssueAssert extends AbstractAssert<IssueAssert, Issue> {


    public IssueAssert(final Issue actual) {
        super(actual, IssueAssert.class);
    }


   public static IssueAssert assertThat(final Issue actual){
        return new IssueAssert(actual);
   }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssueAssert hasFileName(final String filename) {
        // check actual not null
        this.isNotNull();
        // check condition
        propertyEqualsCheck(actual.getFileName(), filename, "filename");
        // Return this for Fluent.
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssueAssert hasLineStart(final int lineStart) {
        // check actual not null
        this.isNotNull();
        // check condition
        propertyEqualsCheck(actual.getLineStart(), lineStart, "lineStart");
        // Return this for Fluent.
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssueAssert hasLineEnd(final int lineEnd) {
        // check actual not null
        this.isNotNull();
        // check condition
        propertyEqualsCheck(actual.getLineEnd(), lineEnd, "lineEnd");
        // Return this for Fluent.
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssueAssert hasColumnStart(final int columnStart) {
        // check actual not null
        this.isNotNull();
        // check condition
        propertyEqualsCheck(actual.getColumnStart(), columnStart, "columnStart");
        // Return this for Fluent.
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssueAssert hasColumnEnd(final int columnEnd) {
        // check actual not null
        this.isNotNull();
        // check condition
        propertyEqualsCheck(actual.getColumnEnd(), columnEnd, "columnEnd");
        // Return this for Fluent.
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssueAssert hasCategory(final String category) {
        // check actual not null
        this.isNotNull();
        // check condition
        propertyEqualsCheck(actual.getCategory(), category, "category");
        // Return this for Fluent.
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssueAssert hasType(final String type) {
        // check actual not null
        this.isNotNull();
        // check condition
        propertyEqualsCheck(actual.getType(), type, "type");
        // Return this for Fluent.
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssueAssert hasPackagename(final String packagename) {
        // check actual not null
        this.isNotNull();
        // check condition
        propertyEqualsCheck(actual.getPackageName(), packagename, "packagename");
        // Return this for Fluent.
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssueAssert hasPriority(final Priority priority) {
        // check actual not null
        this.isNotNull();
        // check condition
        propertyEqualsCheck(actual.getPriority(), priority, "priority");
        // Return this for Fluent.
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssueAssert hasDescription(final String description) {
        // check actual not null
        this.isNotNull();
        // check condition
        propertyEqualsCheck(actual.getDescription(), description, "description");
        // Return this for Fluent.
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssueAssert hasMessage(final String message) {
        // check actual not null
        this.isNotNull();
        // check condition
        propertyEqualsCheck(actual.getMessage(), message, "message");
        // Return this for Fluent.
        return this;
    }

    private <T> void propertyEqualsCheck(final T actualValue, final T expected, final String propertyName){
        if (!Objects.equals(actualValue, expected)) {
            failWithMessage("Expected issue's "+propertyName+" to be <%s> but was <%s>", expected, actualValue);
        }
    }
}
