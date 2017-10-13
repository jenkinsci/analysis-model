package edu.hm.hafner.analysis;

import java.util.Objects;

import org.assertj.core.api.AbstractAssert;

public class IssueAssert extends AbstractAssert<IssueAssert, Issue> {

  /*  public IssueAssert(final Issue issue, final Class<?> selfType) {
        super(issue, selfType);
    }*/

    public IssueAssert(Issue actual) {
        super(actual, IssueAssert.class);
    }

    public static IssueAssert assertThat(Issue actual) {
        return new IssueAssert(actual);
    }

    public IssueAssert hasFileName(String fileName) {
        isNotNull();

        if (!Objects.equals(actual.getFileName(), fileName)) {
            failWithMessage("Expected file Name to be <%s> but was <%s>", fileName, actual.getFileName());
        }
        return this;
    }

    public IssueAssert hasCategory(String category) {
        isNotNull();

        if (!Objects.equals(actual.getCategory(), category)) {
            failWithMessage("Expected Category to be <%s> but was <%s>", category, actual.getCategory());
        }
        return this;
    }

    public IssueAssert hasType(String type) {
        isNotNull();

        if (!Objects.equals(actual.getType(), type)) {
            failWithMessage("Expected Type to be <%s> but was <%s>", type, actual.getType());
        }
        return this;
    }

    public IssueAssert hasMessage(String message) {
        isNotNull();

        if (!Objects.equals(actual.getMessage(), message)) {
            failWithMessage("Expected Message to be <%s> but was <%s>", message, actual.getMessage());
        }
        return this;
    }

    public IssueAssert hasFingerprint(String fingerprint) {
        isNotNull();

        if (!Objects.equals(actual.getFingerprint(), fingerprint)) {
            failWithMessage("Expected Fingerprint to be <%s> but was <%s>", fingerprint, actual.getFingerprint());
        }
        return this;
    }

    public IssueAssert hasLineEnd(int lineEnd) {
        isNotNull();

        if (actual.getLineEnd() != lineEnd) {
            failWithMessage("Expected LineEnd to be <%s> but was <%s>", lineEnd, actual.getLineEnd());
        }
        return this;
    }

    public IssueAssert hasColumnEnd(int columnEnd) {
        isNotNull();

        if (actual.getColumnEnd() != columnEnd) {
            failWithMessage("Expected ColumnEnd to be <%s> but was <%s>", columnEnd, actual.getColumnEnd());
        }
        return this;
    }

    public IssueAssert hasPriority(Priority priority) {
        isNotNull();

        if (actual.getPriority() != priority) {
            failWithMessage("Expected Priority to be <%s> but was <%s>", priority, actual.getPriority());
        }
        return this;
    }
}
