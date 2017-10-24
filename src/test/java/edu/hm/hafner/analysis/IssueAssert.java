package edu.hm.hafner.analysis;

import java.util.Objects;

import org.assertj.core.api.AbstractAssert;


public class IssueAssert extends AbstractAssert<IssueAssert, Issue> {


    public IssueAssert(final Issue actual) {
        super(actual, IssueAssert.class);
    }

    public static IssueAssert assertThat(final Issue actual) {
        return new IssueAssert(actual);
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssueAssert hasFileName(final String fileName) {
        checkString("filename", fileName, actual.getFileName());
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssueAssert hasCategory(final String category) {
        checkString("category", category, actual.getCategory());
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssueAssert hasType(final String type) {
        checkString("type", type, actual.getType());
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssueAssert hasPriority(final Priority priority) {
        isNotNull();
        if (!Objects.equals(actual.getPriority(), priority)) {
            failWithMessage("Expected issue's priority to be <%s> but was <%s>", priority, actual.getPriority());
        }
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssueAssert hasMessage(final String message) {
        checkString("message", message, actual.getMessage());
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssueAssert hasDescription(final String description) {
        checkString("description", description, actual.getDescription());
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssueAssert hasPackageName(final String packageName) {
        checkString("packageName", packageName, actual.getPackageName());
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssueAssert hasFingerprint(final String fingerprint) {
        checkString("fingerprint", fingerprint, actual.getFingerprint());
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssueAssert hasLineStart(final int lineStart) {
        checkInt("lineStart", lineStart, actual.getLineStart());
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssueAssert hasLineEnd(final int lineEnd) {
        checkInt("lineEnd", lineEnd, actual.getLineEnd());
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssueAssert hasColumnStart(final int columnStart) {
        checkInt("columnStart", columnStart, actual.getColumnStart());
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssueAssert hasColumnEnd(final int columnEnd) {
        checkInt("columnEnd", columnEnd, actual.getColumnEnd());
        return this;
    }

    private void checkString(final String usage, final String expected, final String actual) {
        isNotNull();
        if (!Objects.equals(actual, expected)) {
            failWithMessage("Expected issue's %s to be <%s> but was <%s>", usage, expected, actual);
        }
    }

    private void checkInt(final String usage, final int expected, final int actual) {
        isNotNull();
        if (actual != expected) {
            failWithMessage("Expected issue's %s to be <%s> but was <%s>", usage, expected, actual);
        }
    }
}