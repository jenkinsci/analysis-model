package edu.hm.hafner.edu.hm.hafner.analysis;

import java.util.Objects;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.description.Description;


import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Priority;

public class IssueCustomAssert extends AbstractAssert<IssueCustomAssert, Issue> {


    public IssueCustomAssert(final Issue actual) {
        super(actual, Issue.class);
    }

   @org.jetbrains.annotations.NotNull
   public static IssueCustomAssert assertThat(Issue actual){
        return new IssueCustomAssert(actual);
   }

    public IssueCustomAssert hasName(final String filename) {
        // check actual not null
        this.isNotNull();
        // check condition
        propertyEqualsCheck(actual.getFileName(), filename, "filename");
        // Return this for Fluent.
        return this;
    }

    public IssueCustomAssert hasCategory(final String category) {
        // check actual not null
        this.isNotNull();
        // check condition
        propertyEqualsCheck(actual.getCategory(), category, "category");
        // Return this for Fluent.
        return this;
    }

    public IssueCustomAssert hasType(final String type) {
        // check actual not null
        this.isNotNull();
        // check condition
        propertyEqualsCheck(actual.getType(), type, "type");
        // Return this for Fluent.
        return this;
    }

    public IssueCustomAssert hasPriority(final Priority priority) {
        // check actual not null
        this.isNotNull();
        // check condition
        propertyEqualsCheck(actual.getPriority(), priority, "priority");
        // Return this for Fluent.
        return this;
    }

    public IssueCustomAssert hasPriority(final String message) {
        // check actual not null
        this.isNotNull();
        // check condition
        propertyEqualsCheck(actual.getMessage(), message, "message");
        // Return this for Fluent.
        return this;
    }

    public IssueCustomAssert hasLineStart(final int lineStart) {
        // check actual not null
        this.isNotNull();
        // check condition
        propertyEqualsCheck(actual.getLineStart(), lineStart, "lineStart");
        // Return this for Fluent.
        return this;
    }

    private <T> void propertyEqualsCheck(T actualValue, T expected, String propertyName){
        if (!Objects.equals(actualValue, expected)) {
            failWithMessage("Expected issue's "+propertyName+" to be <%s> but was <%s>", expected, actualValue);
        }
    }
}
