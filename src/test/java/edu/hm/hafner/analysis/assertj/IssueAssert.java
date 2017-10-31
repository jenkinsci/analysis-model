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

    public IssueAssert hasId(final UUID id) {
        isNotNull();

        if (!Objects.equals(actual.getId(), id)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "id", actual, id, actual.getId());
        }
        return this;
    }

    public IssueAssert hasFileName(final String fileName) {
        isNotNull();

        if (!Objects.equals(actual.getFileName(), fileName)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "file name", actual, fileName, actual.getFileName());
        }
        return this;
    }

    public IssueAssert hasCategory(final String category) {
        isNotNull();

        if (!Objects.equals(actual.getCategory(), category)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "category", actual, category, actual.getCategory());
        }
        return this;
    }

    public IssueAssert hasType(final String type) {
        isNotNull();

        if (!Objects.equals(actual.getType(), type)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "type", actual, type, actual.getType());
        }
        return this;
    }

    public IssueAssert hasPriority(final Priority priority) {
        isNotNull();

        if (!Objects.equals(actual.getPriority(), priority)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "priority", actual, priority, actual.getPriority());
        }
        return this;
    }

    public IssueAssert hasMessage(final String message) {
        isNotNull();

        if (!Objects.equals(actual.getMessage(), message)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "message", actual, message, actual.getMessage());
        }
        return this;
    }

    public IssueAssert hasDescription(final String description) {
        isNotNull();

        if (!Objects.equals(actual.getDescription(), description)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "description", actual, description, actual.getDescription());
        }
        return this;
    }

    public IssueAssert hasLineStart(final int lineStart) {
        isNotNull();

        if (actual.getLineStart() != lineStart) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "line start", actual, lineStart, actual.getLineStart());
        }
        return this;
    }

    public IssueAssert hasLineEnd(final int lineEnd) {
        isNotNull();

        if (actual.getLineEnd() != lineEnd) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "line end", actual, lineEnd, actual.getLineEnd());
        }
        return this;
    }

    public IssueAssert hasColumnStart(final int columnStart) {
        isNotNull();

        if (actual.getColumnStart() != columnStart) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "column start", actual, columnStart, actual.getColumnStart());
        }
        return this;
    }

    public IssueAssert hasColumnEnd(final int columnEnd) {
        isNotNull();

        if (actual.getColumnEnd() != columnEnd) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "column end", actual, columnEnd, actual.getColumnEnd());
        }
        return this;
    }

    public IssueAssert hasPackageName(final String packageName) {
        isNotNull();

        if (!Objects.equals(actual.getPackageName(), packageName)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "package name", actual, packageName, actual.getPackageName());
        }
        return this;
    }

    public IssueAssert hasFingerprint(final String fingerprint) {
        isNotNull();

        if (!Objects.equals(actual.getFingerprint(), fingerprint)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "fingerprint", actual, fingerprint, actual.getFingerprint());
        }
        return this;
    }
}
