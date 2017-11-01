package edu.hm.hafner.analysis.assertj;

import java.util.Objects;

import org.assertj.core.api.AbstractAssert;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Priority;

/**
 * Fluent assertions for {@link Issue}.
 */
public class IssueAssert extends AbstractAssert<IssueAssert, Issue> {

    private static final String EXPECTED_BUT_WAS_MESSAGE = "%nExpecting %s of:%n <%s>%nto be:%n <%s>%nbut was:%n <%s>.";

    /**
     * Ctor for creating a new instance.
     *
     * @param issue Issue assertions are run on
     */
    public IssueAssert(final Issue issue) {
        super(issue, IssueAssert.class);
    }

    /**
     * Assert the id is set.
     *
     * @return IssueAssert instance
     */
    public IssueAssert hasId() {
        isNotNull();

        if (actual.getId() == null) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "id", actual, "not null", actual.getId());
        }

        return this;
    }

    /**
     * Assert the filename matches the expectation.
     *
     * @param expected expected value
     * @return IssueAssert instance
     */
    public IssueAssert hasFilename(final String expected) {
        isNotNull();

        if (!Objects.equals(actual.getFileName(), expected)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "filename", actual, expected, actual.getFileName());
        }

        return this;
    }

    /**
     * Assert the category matches the expectation.
     *
     * @param expected expected value
     * @return IssueAssert instance
     */
    public IssueAssert hasCategory(final String expected) {
        isNotNull();

        if (!Objects.equals(actual.getCategory(), expected)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "category", actual, expected, actual.getCategory());
        }

        return this;
    }

    /**
     * Assert the column start matches the expectation.
     *
     * @param expected expected value
     * @return IssueAssert instance
     */
    public IssueAssert hasColumnStart(final int expected) {
        isNotNull();

        if (!Objects.equals(actual.getColumnStart(), expected)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "columnStart", actual, expected, actual.getColumnStart());
        }

        return this;
    }

    /**
     * Assert the column end matches the expectation.
     *
     * @param expected expected value
     * @return IssueAssert instance
     */
    public IssueAssert hasColumnEnd(final int expected) {
        isNotNull();

        if (!Objects.equals(actual.getColumnEnd(), expected)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "columnEnd", actual, expected, actual.getColumnEnd());
        }

        return this;
    }

    /**
     * Assert the line start matches the expectation.
     *
     * @param expected expected value
     * @return IssueAssert instance
     */
    public IssueAssert hasLineStart(final int expected) {
        isNotNull();

        if (!Objects.equals(actual.getLineStart(), expected)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "lineStart", actual, expected, actual.getLineStart());
        }

        return this;
    }

    /**
     * Assert the line end matches the expectation.
     *
     * @param expected expected value
     * @return IssueAssert instance
     */
    public IssueAssert hasLineEnd(final int expected) {
        isNotNull();

        if (!Objects.equals(actual.getLineEnd(), expected)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "lineEnd", actual, expected, actual.getLineEnd());
        }

        return this;
    }

    /**
     * Assert the ptype matches the expectation.
     *
     * @param expected expected value
     * @return IssueAssert instance
     */
    public IssueAssert hasType(final String expected) {
        isNotNull();

        if (!Objects.equals(actual.getType(), expected)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "type", actual, expected, actual.getType());
        }

        return this;
    }

    /**
     * Assert the message matches the expectation.
     *
     * @param expected expected value
     * @return IssueAssert instance
     */
    public IssueAssert hasMessage(final String expected) {
        isNotNull();

        if (!Objects.equals(actual.getMessage(), expected)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "message", actual, expected, actual.getMessage());
        }

        return this;
    }

    /**
     * Assert the description matches the expectation.
     *
     * @param expected expected value
     * @return IssueAssert instance
     */
    public IssueAssert hasDescription(final String expected) {
        isNotNull();

        if (!Objects.equals(actual.getDescription(), expected)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "description", actual, expected, actual.getDescription());
        }

        return this;
    }

    /**
     * Assert the package name matches the expectation.
     *
     * @param expected expected value
     * @return IssueAssert instance
     */
    public IssueAssert hasPackageName(final String expected) {
        isNotNull();

        if (!Objects.equals(actual.getPackageName(), expected)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "packageName", actual, expected, actual.getPackageName());
        }

        return this;
    }

    /**
     * Assert the priority matches the expectation.
     *
     * @param expected expected value
     * @return IssueAssert instance
     */
    public IssueAssert hasPriority(final Priority expected) {
        isNotNull();

        if (!Objects.equals(actual.getPriority(), expected)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "priority", actual, expected, actual.getPriority());
        }

        return this;
    }

    /**
     * Assert the fingerprint matches the expectation.
     *
     * @param expected fingerprint to match
     * @return this
     */
    public IssueAssert hasFingerprint(final String expected) {
        isNotNull();

        if (!Objects.equals(actual.getFingerprint(), expected)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "fingerprint", actual, expected, actual.getFingerprint());
        }

        return this;
    }

    /**
     * Assert the issue has a different id.
     *
     * @param issue the issue with an expected different id.
     * @return this
     */
    public IssueAssert hasNotSameId(final Issue issue) {
        isNotNull();

        if (Objects.equals(actual.getId(), issue.getId())) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "id", actual, issue.getId(), actual.getId());
        }

        return this;
    }
}
