package edu.hm.hafner.analysis;

import java.util.Objects;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.description.Description;

/**
 * Custom assertions for {@link Issue}.
 */
public class IssueAssert extends AbstractAssert<IssueAssert, Issue> {

    public IssueAssert(final Issue actual) {
        super(actual, IssueAssert.class);
    }

    public static IssueAssert assertThat(Issue actual) {
        return new IssueAssert(actual);
    }

    public IssueAssert hasFileName(String fileName) {
        isNotNull();

        if (!Objects.equals(actual.getFileName(), fileName)) {
            failWithMessage("Expected fileName to be <%s> but was <%s>", fileName, actual.getFileName());
        }

        return this;
    }

    public IssueAssert hasLineStart(int lineStart) {
        isNotNull();

        if (actual.getLineStart() != lineStart) {
            failWithMessage("Expected lineStart to be <%s> but was <%s>", lineStart, actual.getLineStart());
        }

        return this;
    }

    public IssueAssert hasLineEnd(int lineEnd) {
        isNotNull();

        if (actual.getLineEnd() != lineEnd) {
            failWithMessage("Expected lineEnd to be <%s> but was <%s>", lineEnd, actual.getLineEnd());
        }

        return this;
    }

    public IssueAssert hasColumnStart(int columnStart) {
        isNotNull();

        if (actual.getColumnStart() != columnStart) {
            failWithMessage("Expected columnStart to be <%s> but was <%s>", columnStart, actual.getColumnStart());
        }

        return this;
    }

    public IssueAssert hasColumnEnd(int columnEnd) {
        isNotNull();

        if (actual.getColumnEnd() != columnEnd) {
            failWithMessage("Expected columnEnd to be <%s> but was <%s>", columnEnd, actual.getColumnEnd());
        }

        return this;
    }

    //TODO actual.getCategory() is null?
    public IssueAssert hasCategory(String category) {
        isNotNull();

        if (!actual.getCategory().equals(category)) {
            failWithMessage("Expected category to be <%s> but was <%s>", category, actual.getCategory());
        }

        return this;
    }

    public IssueAssert hasType(String type) {
        isNotNull();

        if (!actual.getType().equals(type)) {
            failWithMessage("Expected type to be <%s> but was <%s>", type, actual.getType());
        }

        return this;
    }

    public IssueAssert hasPackageName(String packageName) {
        isNotNull();

        if (!actual.getPackageName().equals(packageName)) {
            failWithMessage("Expected packageName to be <%s> but was <%s>", packageName, actual.getPackageName());
        }

        return this;
    }

    public IssueAssert hasPriority(Priority priority) {
        isNotNull();

        if (actual.getPriority() != priority) {
            failWithMessage("Expected priority to be <%s> but was <%s>", priority, actual.getPriority());
        }

        return this;
    }

    public IssueAssert hasMessage(String message) {
        isNotNull();

        if (!actual.getMessage().equals(message)) {
            failWithMessage("Expected message to be <%s> but was <%s>", message, actual.getMessage());
        }

        return this;
    }

    public IssueAssert hasDescription(String description) {
        isNotNull();

        if (!actual.getDescription().equals(description)) {
            failWithMessage("Expected description to be <%s> but was <%s>", description, actual.getDescription());
        }

        return this;
    }
}
