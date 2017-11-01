package edu.hm.hafner.analysis.assertj;

import java.util.Objects;
import java.util.UUID;

import org.assertj.core.api.AbstractAssert;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Priority;

/**
 * Assertions for {@link Issue}.
 *
 * @author Marcel Binder
 */
@SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
public class IssueAssert extends AbstractAssert<IssueAssert, Issue> {
    private static final String EXPECTED_BUT_WAS_MESSAGE = "%nExpecting %s of:%n <%s>%nto be:%n <%s>%nbut was:%n <%s>.";

    /**
     * Creates a new {@link IssueAssert} to make assertions on actual {@link Issue}.
     *
     * @param actual the issue we want to make assertions on
     */
    public IssueAssert(final Issue actual) {
        super(actual, IssueAssert.class);
    }

    /**
     * Creates a new {@link IssueAssert} to make assertions on actual {@link Issue}.
     *
     * @param actual the issue we want to make assertions on
     * @return a new {@link IssueAssert}
     */
    @SuppressWarnings("ParameterHidesMemberVariable")
    public static IssueAssert assertThat(final Issue actual) {
        return new IssueAssert(actual);
    }

    /**
     * Checks whether an Issue has a specific id.
     *
     * @param id id specifying id.
     * @return this
     */
    public IssueAssert hasId(final UUID id) {
        isNotNull();

        if (!Objects.equals(actual.getId(), id)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "id", actual, id, actual.getId());
        }

        return this;
    }

    /**
     * Checks whether an Issue has a specific filename.
     *
     * @param fileName String specifying filename.
     * @return this
     */
    public IssueAssert hasFileName(final String fileName) {
        isNotNull();

        if (!Objects.equals(actual.getFileName(), fileName)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "file name", actual, fileName, actual.getFileName());
        }

        return this;
    }

    /**
     * Checks whether an Issue has a specific category.
     *
     * @param category String specifying category.
     * @return this
     */
    public IssueAssert hasCategory(final String category) {
        isNotNull();

        if (!Objects.equals(actual.getCategory(), category)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "category", actual, category, actual.getCategory());
        }

        return this;
    }

    /**
     * Checks whether an Issue has a specific type.
     *
     * @param type String specifying type.
     * @return this
     */
    public IssueAssert hasType(final String type) {
        isNotNull();

        if (!Objects.equals(actual.getType(), type)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "type", actual, type, actual.getType());
        }

        return this;
    }

    /**
     * Checks whether an Issue has a specific priority.
     *
     * @param priority Priority specifying priority.
     * @return this
     */
    public IssueAssert hasPriority(final Priority priority) {
        isNotNull();

        if (!Objects.equals(actual.getPriority(), priority)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "priority", actual, priority, actual.getPriority());
        }

        return this;
    }

    /**
     * Checks whether an Issue has a specific message.
     *
     * @param message String specifying message.
     * @return this
     */
    public IssueAssert hasMessage(final String message) {
        isNotNull();

        if (!Objects.equals(actual.getMessage(), message)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "message", actual, message, actual.getMessage());
        }

        return this;
    }

    /**
     * Checks whether an Issue has a specific description.
     *
     * @param description String specifying description.
     * @return this
     */
    public IssueAssert hasDescription(final String description) {
        isNotNull();

        if (!Objects.equals(actual.getDescription(), description)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "description", actual, description, actual.getDescription());
        }
        return this;
    }

    /**
     * Checks whether an Issue starts at a specific line.
     *
     * @param lineStart int specifying lineStart.
     * @return this
     */
    public IssueAssert hasLineStart(final int lineStart) {
        isNotNull();

        if (actual.getLineStart() != lineStart) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "line start", actual, lineStart, actual.getLineStart());
        }
        return this;
    }

    /**
     * Checks whether an Issue ends at a specific line.
     *
     * @param lineEnd int specifying lineEnd.
     * @return this
     */
    public IssueAssert hasLineEnd(final int lineEnd) {
        isNotNull();

        if (actual.getLineEnd() != lineEnd) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "line end", actual, lineEnd, actual.getLineEnd());
        }
        return this;
    }

    /**
     * Checks whether an Issue starts at a specific column.
     *
     * @param columnStart int specifying columnStart.
     * @return this
     */
    public IssueAssert hasColumnStart(final int columnStart) {
        isNotNull();

        if (actual.getColumnStart() != columnStart) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "column start", actual, columnStart, actual.getColumnStart());
        }
        return this;
    }

    /**
     * Checks whether an Issue ends at a specific column.
     *
     * @param columnEnd int specifying columnEnd.
     * @return this
     */
    public IssueAssert hasColumnEnd(final int columnEnd) {
        isNotNull();

        if (actual.getColumnEnd() != columnEnd) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "column end", actual, columnEnd, actual.getColumnEnd());
        }
        return this;
    }

    /**
     * Checks whether an Issue has a specific packageName.
     *
     * @param packageName String specifying packageName.
     * @return this
     */
    public IssueAssert hasPackageName(final String packageName) {
        isNotNull();

        if (!Objects.equals(actual.getPackageName(), packageName)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "package name", actual, packageName, actual.getPackageName());
        }
        return this;
    }

    /**
     * Checks whether an Issue has a specific fingerprint.
     *
     * @param fingerprint String specifying fingerprint.
     * @return this
     */
    public IssueAssert hasFingerprint(final String fingerprint) {
        isNotNull();

        if (!Objects.equals(actual.getFingerprint(), fingerprint)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "fingerprint", actual, fingerprint, actual.getFingerprint());
        }
        return this;
    }
}
