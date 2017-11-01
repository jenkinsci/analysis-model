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

    private static final String EXPECTED_BUT_WAS_MESSAGE = "%nExpecting:%n <%s>%nto be equal to:%n <%s>%n but was not.";

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
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, id, actual.getId());
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
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, name, actual.getFileName());
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
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, category, actual.getCategory());
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
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, type, actual.getType());
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
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, priority, actual.getPriority());
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
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, message, actual.getMessage());
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
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, description, actual.getDescription());
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
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, lineStart, actual.getLineStart());
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
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, lineEnd, actual.getLineEnd());
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
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, columnStart, actual.getColumnStart());
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
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, columnEnd, actual.getColumnEnd());
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
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, packageName, actual.getPackageName());
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
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, fingerprint, actual.getFingerprint());
        }
        return this;
    }


}
