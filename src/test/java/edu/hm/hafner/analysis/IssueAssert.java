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

    private static final String EXPECTED_BUT_WAS_MESSAGE = "%nExpecting:%n <%s>%nto be equal to:%n <%s>%nbut was not.";

    public IssueAssert(final Issue actual) {
        super(actual, IssueAssert.class);
    }

    public static IssueAssert assertThat(final Issue actualIssue) {
        return new IssueAssert(actualIssue);
    }

    public IssueAssert hasFileName(final String fileName) {
        checkString(fileName, actual.getFileName());
        return this;
    }

    public IssueAssert hasCategory(final String category) {
        checkString(category, actual.getCategory());
        return this;
    }

    public IssueAssert hasType(final String type) {
        checkString(type, actual.getType());
        return this;
    }

    public IssueAssert hasPriority(final Priority priority) {
        isNotNull();
        if (!Objects.equals(actual.getPriority(), priority)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, priority, actual.getPriority());
        }
        return this;
    }

    public IssueAssert hasMessage(final String message) {
        checkString(message, actual.getMessage());
        return this;
    }

    public IssueAssert hasDescription(final String description) {
        checkString(description, actual.getDescription());
        return this;
    }

    public IssueAssert hasPackageName(final String packageName) {
        checkString(packageName, actual.getPackageName());
        return this;
    }

    public IssueAssert hasFingerprint(final String fingerprint) {
        checkString(fingerprint, actual.getFingerprint());
        return this;
    }

    public IssueAssert hasLineStart(final int lineStart) {
        checkInt(lineStart, actual.getLineStart());
        return this;
    }

    public IssueAssert hasLineEnd(final int lineEnd) {
        checkInt(lineEnd, actual.getLineEnd());
        return this;
    }

    public IssueAssert hasColumnStart(final int columnStart) {
        checkInt(columnStart, actual.getColumnStart());
        return this;
    }

    public IssueAssert hasColumnEnd(final int columnEnd) {
        checkInt(columnEnd, actual.getColumnEnd());
        return this;
    }

    private void checkString(final String expected, final String actualString) {
        isNotNull();
        if (!Objects.equals(actualString, expected)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE,expected, actualString);
        }
    }

    private void checkInt(final int expected, final int actualInt) {
        isNotNull();
        if (actualInt != expected) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, expected, actualInt);
        }
    }
}