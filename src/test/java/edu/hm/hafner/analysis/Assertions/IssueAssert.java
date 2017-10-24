package edu.hm.hafner.analysis.Assertions;

import java.util.Objects;
import java.util.UUID;

import org.assertj.core.api.AbstractAssert;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Priority;

/**
 * Custom assertion for issue.
 */
public class IssueAssert extends AbstractAssert<IssueAssert, Issue> {

    /**
     * Custom constructor.
     *
     * @param actual current issue
     */
    public IssueAssert(Issue actual) {
        super(actual, IssueAssert.class);
    }

    /**
     * Custom assert function for issue.
     *
     * @param actual current issue
     * @return custom assertion
     */
    public static IssueAssert assertThat(Issue actual) {
        return new IssueAssert(actual);
    }

    /**
     * Check issue for specific file name.
     *
     * @param fileName filter for file name
     * @return custom assertion
     */
    public IssueAssert hasFileName(String fileName) {
        isNotNull();

        if (!Objects.equals(actual.getFileName(), fileName)) {
            failWithMessage("Expected file Name to be <%s> but was <%s>", fileName, actual.getFileName());
        }
        return this;
    }


    /**
     * Check issue for specific category.
     *
     * @param category filter for category
     * @return custom assertion
     */
    public IssueAssert hasCategory(String category) {
        isNotNull();

        if (!Objects.equals(actual.getCategory(), category)) {
            failWithMessage("Expected category to be <%s> but was <%s>", category, actual.getCategory());
        }
        return this;
    }


    /**
     * Check issue for specific package name.
     *
     * @param packageName filter for package name
     * @return custom assertion
     */
    public IssueAssert hasPackageName(String packageName) {
        isNotNull();

        if (!Objects.equals(actual.getPackageName(), packageName)) {
            failWithMessage("Expected package Name to be <%s> but was <%s>", packageName, actual.getPackageName());
        }
        return this;
    }


    /**
     * Check issue for specific description.
     *
     * @param description filter for description
     * @return custom assertion
     */
    public IssueAssert hasDescription(String description) {
        isNotNull();

        if (!Objects.equals(actual.getDescription(), description)) {
            failWithMessage("Expected description to be <%s> but was <%s>", description, actual.getDescription());
        }
        return this;
    }

    /**
     * Check issue for specific line start.
     *
     * @param lineStart filter for line start
     * @return custom assertion
     */
    public IssueAssert hasLineStart(int lineStart) {
        isNotNull();

        if (actual.getLineStart() != lineStart) {
            failWithMessage("Expected line start to be <%s> but was <%s>", lineStart, actual.getLineStart());

        }
        return this;
    }

    /**
     * Check issue for specific column start.
     *
     * @param columnStart filter for column start
     * @return custom assertion
     */
    public IssueAssert hasColumnStart(int columnStart) {
        isNotNull();

        if (actual.getColumnStart() != columnStart) {
            failWithMessage("Expected column start to be <%s> but was <%s>", columnStart, actual.getColumnStart());

        }
        return this;
    }

    /**
     * Check issue for specific id.
     *
     * @param id filter for id
     * @return custom assertion
     */
    public IssueAssert hasId(UUID id) {
        isNotNull();

        if (!actual.getId().equals(id)) {
            failWithMessage("Expected id to be <%s> but was <%s>", id, actual.getId());

        }
        return this;
    }

    /**
     * Check issue for specific type.
     *
     * @param type filter for type
     * @return custom assertion
     */
    public IssueAssert hasType(String type) {
        isNotNull();

        if (!Objects.equals(actual.getType(), type)) {
            failWithMessage("Expected Type to be <%s> but was <%s>", type, actual.getType());
        }
        return this;
    }

    /**
     * Check issue for specific message.
     *
     * @param message filter for message
     * @return custom assertion
     */
    public IssueAssert hasMessage(String message) {
        isNotNull();

        if (!Objects.equals(actual.getMessage(), message)) {
            failWithMessage("Expected Message to be <%s> but was <%s>", message, actual.getMessage());
        }
        return this;
    }

    /**
     * Check issue for specific fingerprint.
     *
     * @param fingerprint filter for fingerprint
     * @return custom assertion
     */
    public IssueAssert hasFingerprint(String fingerprint) {
        isNotNull();

        if (!Objects.equals(actual.getFingerprint(), fingerprint)) {
            failWithMessage("Expected Fingerprint to be <%s> but was <%s>", fingerprint, actual.getFingerprint());
        }
        return this;
    }

    /**
     * Check issue for specific line end.
     *
     * @param lineEnd filter for line end
     * @return custom assertion
     */
    public IssueAssert hasLineEnd(int lineEnd) {
        isNotNull();

        if (actual.getLineEnd() != lineEnd) {
            failWithMessage("Expected LineEnd to be <%s> but was <%s>", lineEnd, actual.getLineEnd());
        }
        return this;
    }


    /**
     * Check issue for specific column end.
     *
     * @param columnEnd filter for column end
     * @return custom assertion
     */
    public IssueAssert hasColumnEnd(int columnEnd) {
        isNotNull();

        if (actual.getColumnEnd() != columnEnd) {
            failWithMessage("Expected ColumnEnd to be <%s> but was <%s>", columnEnd, actual.getColumnEnd());
        }
        return this;
    }

    /**
     * Check issue for specific priority.
     *
     * @param priority filter for priority
     * @return custom assertion
     */
    public IssueAssert hasPriority(Priority priority) {
        isNotNull();

        if (actual.getPriority() != priority) {
            failWithMessage("Expected Priority to be <%s> but was <%s>", priority, actual.getPriority());
        }
        return this;
    }
}
