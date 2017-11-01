package edu.hm.hafner.analysis;

import java.util.Objects;

import org.assertj.core.api.AbstractAssert;

/**
 * Custom assertions for {@link Issue}.
 *
 * @author Joscha Behrmann
 */
@SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
public class IssueAssert extends AbstractAssert<IssueAssert, Issue> {
    private static final String EXPECTED_BUT_WAS_MESSAGE = "%nExpecting:%n <%s>%nto be equal to:%n <%s>%nbut was not.";

    public IssueAssert(final Issue actual) {
        super(actual, IssueAssert.class);
    }

    public static IssueAssert assertThat(final Issue actual) {
        return new IssueAssert(actual);
    }

    public IssueAssert hasFileName(final String fileName) {
        isNotNull();

        if (!Objects.equals(actual.getFileName(), fileName)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "fileName", fileName);
        }

        return this;
    }

    public IssueAssert hasLineStart(final int lineStart) {
        isNotNull();

        if (actual.getLineStart() != lineStart) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "lineStart", lineStart); }

        return this;
    }

    public IssueAssert hasLineEnd(final int lineEnd) {
        isNotNull();

        if (actual.getLineEnd() != lineEnd) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "lineEnd", lineEnd);
        }

        return this;
    }

    public IssueAssert hasColumnStart(final int columnStart) {
        isNotNull();

        if (actual.getColumnStart() != columnStart) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "columnStart", columnStart);
        }

        return this;
    }

    public IssueAssert hasColumnEnd(final int columnEnd) {
        isNotNull();

        if (actual.getColumnEnd() != columnEnd) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "columnEnd", columnEnd);
        }

        return this;
    }

    public IssueAssert hasCategory(final String category) {
        isNotNull();

        if (!actual.getCategory().equals(category)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "category", category);
        }

        return this;
    }

    public IssueAssert hasType(final String type) {
        isNotNull();

        if (!actual.getType().equals(type)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "type", type);
        }

        return this;
    }

    public IssueAssert hasPackageName(final String packageName) {
        isNotNull();

        if (!actual.getPackageName().equals(packageName)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "packageName", packageName);
        }

        return this;
    }

    public IssueAssert hasPriority(final Priority priority) {
        isNotNull();

        if (actual.getPriority() != priority) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "priority", priority);
        }

        return this;
    }

    public IssueAssert hasMessage(final String message) {
        isNotNull();

        if (!actual.getMessage().equals(message)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "message", message);
        }

        return this;
    }

    public IssueAssert hasDescription(final String description) {
        isNotNull();

        if (!actual.getDescription().equals(description)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "description", description);
        }

        return this;
    }

    public IssueAssert hasFingerprint(final String fingerprint) {
        isNotNull();

        if (!actual.getFingerprint().equals(fingerprint)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "fingerprint", fingerprint);
        }

        return this;
    }
}
