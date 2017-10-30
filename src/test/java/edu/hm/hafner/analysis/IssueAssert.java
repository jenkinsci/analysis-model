package edu.hm.hafner.analysis;

import org.assertj.core.api.AbstractAssert;

import java.util.Objects;
import java.util.UUID;

/**
 * This class provides custom AssertJ assertion for {@link Issue}
 */
public class IssueAssert extends AbstractAssert<IssueAssert, Issue> {

    private static final String EXPECTED_BUT_WAS_MESSAGE = "%nExpecting:%n <%s>%nto be equal to:%n <%s>%n but was not.";

    public IssueAssert(Issue actual) {
        super(actual, IssueAssert.class);
    }

    public static IssueAssert assertThat(Issue actual) {
        return new IssueAssert(actual);
    }

    /**
     * Checks whether an Issue has a specific filename.
     *
     * @param filename - String specifying filename.
     * @return this
     */
    public IssueAssert hasFileName(String filename) {
        isNotNull();

        if (!Objects.equals(actual.getFileName(), filename)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, filename, actual.getFileName());
        }
        return this;
    }

    /**
     * Checks whether an Issue has a specific category.
     *
     * @param category - String specifying category.
     * @return this
     */
    public IssueAssert hasCategory(String category) {
        isNotNull();

        if (!Objects.equals(actual.getCategory(), category)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, category, actual.getCategory());
        }
        return this;
    }

    /**
     * Checks whether an Issue has a specific type.
     *
     * @param type - String specifying type.
     * @return this
     */
    public IssueAssert hasType(String type) {
        isNotNull();

        if (!Objects.equals(actual.getType(), type)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, type, actual.getType());
        }
        return this;
    }

    /**
     * Checks whether an Issue has a specific priority.
     *
     * @param priority - Priority specifying priority.
     * @return this
     */
    public IssueAssert hasPriority(Priority priority) {
        isNotNull();

        if (!Objects.equals(actual.getPriority(), priority)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, priority, actual.getPriority());
        }
        return this;
    }

    /**
     * Checks whether an Issue has a specific message.
     *
     * @param message - String specifying message.
     * @return this
     */
    public IssueAssert hasMessage(String message) {
        isNotNull();

        if (!Objects.equals(actual.getMessage(), message)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, message, actual.getMessage());
        }
        return this;
    }

    /**
     * Checks whether an Issue has a specific description.
     *
     * @param description - String specifying description.
     * @return this
     */
    public IssueAssert hasDescription(String description) {
        isNotNull();

        if (!Objects.equals(actual.getDescription(), description)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, description, actual.getDescription());
        }
        return this;
    }

    /**
     * Checks whether an Issue has a specific packageName.
     *
     * @param packageName - String specifying packageName.
     * @return this
     */
    public IssueAssert hasPackageName(String packageName) {
        isNotNull();

        if (!Objects.equals(actual.getPackageName(), packageName)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, packageName, actual.getPackageName());
        }
        return this;
    }

    /**
     * Checks whether an Issue starts at a specific line.
     *
     * @param lineStart - int specifying lineStart.
     * @return this
     */
    public IssueAssert hasLineStart(int lineStart) {
        isNotNull();

        if (actual.getLineStart() != lineStart) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, lineStart, actual.getLineStart());
        }
        return this;
    }

    /**
     * Checks whether an Issue ends at a specific line.
     *
     * @param lineEnd - int specifying lineEnd.
     * @return this
     */
    public IssueAssert hasLineEnd(int lineEnd) {
        isNotNull();

        if (actual.getLineEnd() != lineEnd) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, lineEnd, actual.getLineEnd());
        }
        return this;
    }

    /**
     * Checks whether an Issue starts at a specific column.
     *
     * @param columnStart - int specifying columnStart.
     * @return this
     */
    public IssueAssert hasColumnStart(int columnStart) {
        isNotNull();

        if (actual.getColumnStart() != columnStart) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, columnStart, actual.getColumnStart());
        }
        return this;
    }

    /**
     * Checks whether an Issue ends at a specific column.
     *
     * @param columnEnd - int specifying columnEnd.
     * @return this
     */
    public IssueAssert hasColumnEnd(int columnEnd) {
        isNotNull();

        if (actual.getColumnEnd() != columnEnd) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, columnEnd, actual.getColumnEnd());
        }
        return this;
    }

    /**
     * Checks whether an Issue has a specific UUID.
     *
     * @param UUID - UUID specifying UUID.
     * @return this
     */
    public IssueAssert hasUUID(UUID UUID) {
        isNotNull();

        if (!Objects.equals(actual.getId(), UUID)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, UUID, actual.getId());
        }

        return this;
    }

    /**
     * Checks whether an Issue has a specific fingerprint.
     *
     * @param fingerprint - String specifying fingerprint.
     * @return this
     */
    public IssueAssert hasFingerprint(String fingerprint) {
        isNotNull();

        if (!Objects.equals(actual.getFingerprint(), fingerprint)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, fingerprint, actual.getFingerprint());
        }
        return this;
    }
}
