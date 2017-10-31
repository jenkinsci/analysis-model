package edu.hm.hafner.analysis.Assertions;

import java.util.Objects;
import java.util.UUID;

import org.assertj.core.api.AbstractAssert;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Priority;

/**
 * Custom assertion for issue.
 * @author Tom Maier
 * @author Johannes Arzt
 */
public class IssueAssert extends AbstractAssert<IssueAssert, Issue> {

    private static final String EXPECTED_BUT_WAS_MESSAGE = "%nExpecting:%n <%s>%nto be equal to:%n <%s>%nbut was not.";

    /**
     * Custom constructor.
     *
     * @param actual current issue
     */
    public IssueAssert(final Issue actual) {
        super(actual, IssueAssert.class);
    }

    /**
     * Custom assert function for issue.
     *
     * @param actual current issue
     * @return custom assertion
     */
    public static IssueAssert assertThat(final Issue actual) {
        return new IssueAssert(actual);
    }

    /**
     * Check issue for specific file name.
     *
     * @param fileName filter for file name
     * @return custom assertion
     */
    public IssueAssert hasFileName(final String fileName) {
        isNotNull();

        if (!Objects.equals(actual.getFileName(), fileName)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, fileName, actual.getFileName());
        }
        return this;
    }


    /**
     * Check issue for specific category.
     *
     * @param category filter for category
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
     * Check issue for specific package name.
     *
     * @param packageName filter for package name
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
     * Check issue for specific description.
     *
     * @param description filter for description
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
     * Check issue for specific line start.
     *
     * @param lineStart filter for line start
     * @return custom assertion
     */
    public IssueAssert hasLineStart(final int lineStart) {
        isNotNull();

        if (actual.getLineStart() != lineStart) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, lineStart, actual.getLineStart());

        }
        return this;
    }

    /**
     * Check issue for specific column start.
     *
     * @param columnStart filter for column start
     * @return custom assertion
     */
    public IssueAssert hasColumnStart(final int columnStart) {
        isNotNull();

        if (actual.getColumnStart() != columnStart) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, columnStart, actual.getColumnStart());

        }
        return this;
    }

    /**
     * Check issue for specific id.
     *
     * @param id filter for id
     * @return custom assertion
     */
    public IssueAssert hasId(final UUID id) {
        isNotNull();

        if (!actual.getId().equals(id)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, id, actual.getId());

        }
        return this;
    }

    /**
     * Check issue for specific type.
     *
     * @param type filter for type
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
     * Check issue for specific message.
     *
     * @param message filter for message
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
     * Check issue for specific fingerprint.
     *
     * @param fingerprint filter for fingerprint
     * @return custom assertion
     */
    public IssueAssert hasFingerprint(final String fingerprint) {
        isNotNull();

        if (!Objects.equals(actual.getFingerprint(), fingerprint)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, fingerprint, actual.getFingerprint());
        }
        return this;
    }

    /**
     * Check issue for specific line end.
     *
     * @param lineEnd filter for line end
     * @return custom assertion
     */
    public IssueAssert hasLineEnd(final int lineEnd) {
        isNotNull();

        if (actual.getLineEnd() != lineEnd) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, lineEnd, actual.getLineEnd());
        }
        return this;
    }


    /**
     * Check issue for specific column end.
     *
     * @param columnEnd filter for column end
     * @return custom assertion
     */
    public IssueAssert hasColumnEnd(final int columnEnd) {
        isNotNull();

        if (actual.getColumnEnd() != columnEnd) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, columnEnd, actual.getColumnEnd());
        }
        return this;
    }

    /**
     * Check issue for specific priority.
     *
     * @param priority filter for priority
     * @return custom assertion
     */
    public IssueAssert hasPriority(final Priority priority) {
        isNotNull();

        if (actual.getPriority() != priority) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, priority, actual.getPriority());
        }
        return this;
    }
}
