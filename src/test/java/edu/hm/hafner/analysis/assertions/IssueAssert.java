package edu.hm.hafner.analysis.assertions;

import java.util.Objects;

import org.assertj.core.api.AbstractAssert;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Priority;

/**
 * Custom Assertion for Issue.
 *
 * @author Mark Tripolt
 */
public class IssueAssert extends AbstractAssert<IssueAssert, Issue> {
    /**
     * Custom constructor.
     *
     * @param actual issue
     */
    public IssueAssert(final Issue actual) {
        super(actual, IssueAssert.class);
    }

    /**
     * Custom assertThat function for Issue.
     *
     * @param actual issue
     * @return custom assertion
     */
    public static IssueAssert assertThat(final Issue actual) {
        return new IssueAssert(actual);
    }

    /**
     * Check for id.
     *
     * @param id expected
     * @return custom assertion
     */
    public IssueAssert hasId(final int id) {
        isNotNull();
        if (!Objects.equals(actual.getId(), id)) {
            failWithMessage("Expected Issue Id to be <%s> but was <%s>", id, actual.getId());
        }
        return this;
    }

    /**
     * Check for File Name.
     *
     * @param name expected
     * @return custom assertion
     */
    public IssueAssert hasFileName(final String name) {
        isNotNull();
        if (!Objects.equals(actual.getFileName(), name)) {
            failWithMessage("Expected Issue File Name to be <%s> but was <%s>", name, actual.getFileName());
        }
        return this;
    }

    /**
     * Check for category.
     *
     * @param category expected
     * @return custom assertion
     */
    public IssueAssert hasCategory(final String category) {
        isNotNull();
        if (!Objects.equals(actual.getCategory(), category)) {
            failWithMessage("Expected Issue Category to be <%s> but was <%s>", category, actual.getCategory());
        }
        return this;
    }

    /**
     * Check for Type.
     *
     * @param type expected
     * @return custom assertion
     */
    public IssueAssert hasType(final String type) {
        isNotNull();
        if (!Objects.equals(actual.getType(), type)) {
            failWithMessage("Expected Issue Type to be <%s> but was <%s>", type, actual.getType());
        }
        return this;
    }

    /**
     * Check for Priority.
     *
     * @param priority expected
     * @return custom assertion
     */
    public IssueAssert hasPriority(final Priority priority) {
        isNotNull();
        if (!Objects.equals(actual.getPriority(), priority)) {
            failWithMessage("Expected Issue Priority to be <%s> but was <%s>", priority, actual.getPriority());
        }
        return this;
    }

    /**
     * Check for message.
     *
     * @param message expected
     * @return custom assertion
     */
    public IssueAssert hasMessage(final String message) {
        isNotNull();
        if (!Objects.equals(actual.getMessage(), message)) {
            failWithMessage("Expected Issue Message to be <%s> but was <%s>", message, actual.getMessage());
        }
        return this;
    }

    /**
     * Check for description.
     *
     * @param description expected
     * @return custom assertion
     */
    public IssueAssert hasDescription(final String description) {
        isNotNull();
        if (!Objects.equals(actual.getDescription(), description)) {
            failWithMessage("Expected Issue Description to be <%s> but was <%s>", description, actual.getDescription());
        }
        return this;
    }

    /**
     * CHeck for LineStart.
     *
     * @param lineStart expected
     * @return custom assertion
     */
    public IssueAssert hasLineStart(final int lineStart) {
        isNotNull();
        if (!Objects.equals(actual.getLineStart(), lineStart)) {
            failWithMessage("Expected Issue LineStart to be <%s> but was <%s>", lineStart, actual.getLineStart());
        }
        return this;
    }

    /**
     * Check for LineEnd.
     *
     * @param lineEnd expected
     * @return custom assertion
     */
    public IssueAssert hasLineEnd(final int lineEnd) {
        isNotNull();
        if (!Objects.equals(actual.getLineEnd(), lineEnd)) {
            failWithMessage("Expected Issue LineEnd to be <%s> but was <%s>", lineEnd, actual.getLineEnd());
        }
        return this;
    }

    /**
     * Check for ColumnStart.
     *
     * @param columnStart expected
     * @return custom assertion
     */
    public IssueAssert hasColumnStart(final int columnStart) {
        isNotNull();
        if (!Objects.equals(actual.getColumnStart(), columnStart)) {
            failWithMessage("Expected Issue ColumnStart to be <%s> but was <%s>", columnStart, actual.getColumnStart());
        }
        return this;
    }

    /**
     * CHeck for ColumnEnd.
     *
     * @param columnEnd expected
     * @return custom assertion
     */
    public IssueAssert hasColumnEnd(final int columnEnd) {
        isNotNull();
        if (!Objects.equals(actual.getColumnEnd(), columnEnd)) {
            failWithMessage("Expected Issue ColumnEnd to be <%s> but was <%s>", columnEnd, actual.getColumnEnd());
        }
        return this;
    }

    /**
     * Check for PackageName.
     *
     * @param packageName expected
     * @return custom assertion
     */
    public IssueAssert hasPackageName(final String packageName) {
        isNotNull();
        if (!Objects.equals(actual.getPackageName(), packageName)) {
            failWithMessage("Expected Issue PackageName to be <%s> but was <%s>", packageName, actual.getPackageName());
        }
        return this;
    }

    /**
     * Check for Fingerprint.
     *
     * @param fingerprint expected
     * @return custom assertion
     */
    public IssueAssert hasFingerprint(final String fingerprint) {
        isNotNull();
        if (!Objects.equals(actual.getFingerprint(), fingerprint)) {
            failWithMessage("Expected Issue Fingerprint to be <%s> but was <%s>", fingerprint, actual.getFingerprint());
        }
        return this;
    }


}
