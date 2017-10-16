package edu.hm.hafner.analysis.assertj;

import java.util.Objects;
import java.util.UUID;

import org.assertj.core.api.AbstractAssert;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Priority;

public class IssueAssert extends AbstractAssert<IssueAssert, Issue> {
    public IssueAssert(final Issue actual) {
        super(actual, IssueAssert.class);
    }

    public static IssueAssert assertThat(final Issue actual) {
        return new IssueAssert(actual);
    }

    public IssueAssert hasId(final UUID id) {
        isNotNull();

        if (!Objects.equals(actual.getId(), id)) {
            failWithMessage("Expected issue's id to be <%s> but was <%s>", id, actual.getId());
        }
        return this;
    }

    public IssueAssert hasFileName(final String fileName) {
        isNotNull();

        if (!Objects.equals(actual.getFileName(), fileName)) {
            failWithMessage("Expected issue's file name to be <%s> but was <%s>", fileName, actual.getFileName());
        }
        return this;
    }

    public IssueAssert hasCategory(final String category) {
        isNotNull();

        if (!Objects.equals(actual.getCategory(), category)) {
            failWithMessage("Expected issue's category to be <%s> but was <%s>", category, actual.getCategory());
        }
        return this;
    }

    public IssueAssert hasType(final String type) {
        isNotNull();

        if (!Objects.equals(actual.getType(), type)) {
            failWithMessage("Expected issue's type to be <%s> but was <%s>", type, actual.getType());
        }
        return this;
    }

    public IssueAssert hasPriority(final Priority priority) {
        isNotNull();

        if (!Objects.equals(actual.getPriority(), priority)) {
            failWithMessage("Expected issue's priority to be <%s> but was <%s>", priority, actual.getPriority());
        }
        return this;
    }

    public IssueAssert hasMessage(final String message) {
        isNotNull();

        if (!Objects.equals(actual.getMessage(), message)) {
            failWithMessage("Expected issue's message to be <%s> but was <%s>", message, actual.getMessage());
        }
        return this;
    }

    public IssueAssert hasDescription(final String description) {
        isNotNull();

        if (!Objects.equals(actual.getDescription(), description)) {
            failWithMessage("Expected issue's description to be <%s> but was <%s>", description, actual.getDescription());
        }
        return this;
    }

    public IssueAssert hasLineStart(final int lineStart) {
        isNotNull();

        if (actual.getLineStart() != lineStart) {
            failWithMessage("Expected issue's line start to be <%d> but was <%d>", lineStart, actual.getLineStart());
        }
        return this;
    }

    public IssueAssert hasLineEnd(final int lineEnd) {
        isNotNull();

        if (actual.getLineEnd() != lineEnd) {
            failWithMessage("Expected issue's line end to be <%d> but was <%d>", lineEnd, actual.getLineEnd());
        }
        return this;
    }

    public IssueAssert hasColumnStart(final int columnStart) {
        isNotNull();

        if (actual.getColumnStart() != columnStart) {
            failWithMessage("Expected issue's column start to be <%d> but was <%d>", columnStart, actual.getColumnStart());
        }
        return this;
    }

    public IssueAssert hasColumnEnd(final int columnEnd) {
        isNotNull();

        if (actual.getColumnEnd() != columnEnd) {
            failWithMessage("Expected issue's column end to be <%d> but was <%d>", columnEnd, actual.getColumnEnd());
        }
        return this;
    }

    public IssueAssert hasPackageName(final String packageName) {
        isNotNull();

        if (!Objects.equals(actual.getPackageName(), packageName)) {
            failWithMessage("Expected issue's package name to be <%s> but was <%s>", packageName, actual.getPackageName());
        }
        return this;
    }

    public IssueAssert hasFingerprint(final String fingerprint) {
        isNotNull();

        if (!Objects.equals(actual.getFingerprint(), fingerprint)) {
            failWithMessage("Expected issue's fingerprint to be <%s> but was <%s>", fingerprint, actual.getFingerprint());
        }
        return this;
    }
}
