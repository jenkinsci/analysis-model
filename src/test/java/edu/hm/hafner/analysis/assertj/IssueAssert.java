package edu.hm.hafner.analysis.assertj;

import org.assertj.core.api.AbstractAssert;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Priority;

public class IssueAssert extends AbstractAssert<IssueAssert, Issue> {
    //TODO javadoc

    public IssueAssert(Issue actual) {
        super(actual, IssueAssert.class);
    }

    public static IssueAssert assertThat(Issue actual) {
        return new IssueAssert(actual);
    }

    public IssueAssert hasLineStart(final int line) {
        isNotNull();

        if (actual.getLineStart() != line) {
            failWithMessage("Expected issue's line start to be <%s> but was <%s>", line, actual.getLineStart());
        }

        return this;
    }

    public IssueAssert hasLineEnd(final int line) {
        isNotNull();

        if (actual.getLineEnd() != line) {
            failWithMessage("Expected issue's line end to be <%s> but was <%s>", line, actual.getLineEnd());
        }

        return this;
    }

    public IssueAssert hasColumnStart(final int column) {
        isNotNull();

        if (actual.getColumnStart() != column) {
            failWithMessage("Expected issue's column start to be <%s> but was <%s>", column, actual.getColumnStart());
        }

        return this;
    }

    public IssueAssert hasColumnEnd(final int column) {
        isNotNull();

        if (actual.getColumnEnd() != column) {
            failWithMessage("Expected issue's column end to be <%s> but was <%s>", column, actual.getColumnEnd());
        }

        return this;
    }

    public IssueAssert hasMessage(final String message) {
        isNotNull();

        if (!actual.getMessage().equals(message)) {
            failWithMessage("Expected issue's message to be <%s> but was <%s>", message, actual.getMessage());
        }

        return this;
    }

    public IssueAssert hasFileName(final String fileName) {
        isNotNull();

        if (!actual.getFileName().equals(fileName)) {
            failWithMessage("Expected issue's fileName to be <%s> but was <%s>", fileName, actual.getFileName());
        }

        return this;
    }

    public IssueAssert hasType(final String type) {
        isNotNull();

        if (!actual.getType().equals(type)) {
            failWithMessage("Expected issue's type to be <%s> but was <%s>", type, actual.getType());
        }

        return this;
    }

    public IssueAssert hasCategory(final String category) {
        isNotNull();

        if (!actual.getCategory().equals(category)) {
            failWithMessage("Expected issue's category to be <%s> but was <%s>", category, actual.getCategory());
        }

        return this;
    }


    public IssueAssert hasPriority(final Priority priority) {
        isNotNull();

        if (actual.getPriority() != priority) {
            failWithMessage("Expected issue's priority to be <%s> but was <%s>", priority, actual.getPriority());
        }

        return this;
    }

    public IssueAssert hasPackageName(final String packageName) {
        isNotNull();

        if (!actual.getPackageName().equals(packageName)) {
            failWithMessage("Expected issue's packageName to be <%s> but was <%s>", packageName, actual.getPackageName());
        }

        return this;
    }

    public IssueAssert hasDescription(final String description) {
        isNotNull();

        if (!actual.getDescription().equals(description)) {
            failWithMessage("Expected issue's description to be <%s> but was <%s>", description, actual.getDescription());
        }

        return this;
    }


}
