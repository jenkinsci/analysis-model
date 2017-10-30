package edu.hm.hafner.analysis;

import java.util.Objects;

import org.assertj.core.api.AbstractAssert;

/**
 * Custom assert to test the class {@link Issue}.
 *
 * @author Michael Schmid
 */
@SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
public class IssueAssert extends AbstractAssert<IssueAssert, Issue> {


    public IssueAssert(final Issue actual) {
        super(actual, IssueAssert.class);
    }

    public static IssueAssert assertThat(final Issue actualIssue) {
        return new IssueAssert(actualIssue);
    }

    public IssueAssert hasFileName(final String fileName) {
        checkString("filename", fileName, actual.getFileName());
        return this;
    }

    public IssueAssert hasCategory(final String category) {
        checkString("category", category, actual.getCategory());
        return this;
    }

    public IssueAssert hasType(final String type) {
        checkString("type", type, actual.getType());
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
        checkString("message", message, actual.getMessage());
        return this;
    }

    public IssueAssert hasDescription(final String description) {
        checkString("description", description, actual.getDescription());
        return this;
    }

    public IssueAssert hasPackageName(final String packageName) {
        checkString("packageName", packageName, actual.getPackageName());
        return this;
    }

    public IssueAssert hasFingerprint(final String fingerprint) {
        checkString("fingerprint", fingerprint, actual.getFingerprint());
        return this;
    }

    public IssueAssert hasLineStart(final int lineStart) {
        checkInt("lineStart", lineStart, actual.getLineStart());
        return this;
    }

    public IssueAssert hasLineEnd(final int lineEnd) {
        checkInt("lineEnd", lineEnd, actual.getLineEnd());
        return this;
    }

    public IssueAssert hasColumnStart(final int columnStart) {
        checkInt("columnStart", columnStart, actual.getColumnStart());
        return this;
    }

    public IssueAssert hasColumnEnd(final int columnEnd) {
        checkInt("columnEnd", columnEnd, actual.getColumnEnd());
        return this;
    }

    private void checkString(final String usage, final String expected, final String actualString) {
        isNotNull();
        if (!Objects.equals(actualString, expected)) {
            failWithMessage("Expected issue's %s to be <%s> but was <%s>", usage, expected, actualString);
        }
    }

    private void checkInt(final String usage, final int expected, final int actualInt) {
        isNotNull();
        if (actualInt != expected) {
            failWithMessage("Expected issue's %s to be <%s> but was <%s>", usage, expected, actualInt);
        }
    }
}