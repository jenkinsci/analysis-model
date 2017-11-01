package edu.hm.hafner.analysis.assertj;

import org.assertj.core.api.AbstractAssert;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Priority;

/**
 * A custom AssertJ assertion to make assertions specific to {@link Issue}.
 *
 * @author Andreas Moser
 */
public class IssueAssert extends AbstractAssert<IssueAssert, Issue> {
    private static final String EXPECTED_BUT_WAS_MESSAGE = "%nExpecting:%n <%s>%nto be equal to:%n <%s>%nbut was not.";

    /**
     * Creates a new @link {@link IssueAssert}.
     *
     * @param actual The Issue to make assertions on.
     */
    public IssueAssert(final Issue actual) {
        super(actual, IssueAssert.class);
    }

    /**
     * An entry point for IssueAssert.
     *
     * @param actual The Issue to make assertions on.
     * @return A new @link {@link IssueAssert}
     */
    public static IssueAssert assertThat(final Issue actual) {
        return new IssueAssert(actual);
    }

    /**
     * Verifies that the actual Issue's lineStart is equal to the given one.
     *
     * @param line The given lineStart to compare the actual Issue's lineStart to.
     * @return this assertion object.
     * @throws AssertionError - if the actual Issue's lineStart is not equal to the given one.
     */
    public IssueAssert hasLineStart(final int line) {
        isNotNull();

        if (actual.getLineStart() != line) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, line, actual.getLineStart());
        }

        return this;
    }

    /**
     * Verifies that the actual Issue's lineEnd is equal to the given one.
     *
     * @param line The given lineEnd to compare the actual Issue's lineEnd to.
     * @return this assertion object.
     * @throws AssertionError - if the actual Issue's lineEnd is not equal to the given one.
     */
    public IssueAssert hasLineEnd(final int line) {
        isNotNull();

        if (actual.getLineEnd() != line) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, line, actual.getLineEnd());
        }

        return this;
    }

    /**
     * Verifies that the actual Issue's columnStart is equal to the given one.
     *
     * @param column The given columnStart to compare the actual Issue's columnStart to.
     * @return this assertion object.
     * @throws AssertionError - if the actual Issue's columnStart is not equal to the given one.
     */
    public IssueAssert hasColumnStart(final int column) {
        isNotNull();

        if (actual.getColumnStart() != column) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, column, actual.getColumnStart());
        }

        return this;
    }

    /**
     * Verifies that the actual Issue's columnEnd is equal to the given one.
     *
     * @param column The given columnEnd to compare the actual Issue's columnEnd to.
     * @return this assertion object.
     * @throws AssertionError - if the actual Issue's columnEnd is not equal to the given one.
     */
    public IssueAssert hasColumnEnd(final int column) {
        isNotNull();

        if (actual.getColumnEnd() != column) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, column, actual.getColumnEnd());
        }

        return this;
    }

    /**
     * Verifies that the actual Issue's message is equal to the given one.
     *
     * @param message The given message to compare the actual Issue's message to.
     * @return this assertion object.
     * @throws AssertionError - if the actual Issue's message is not equal to the given one.
     */
    public IssueAssert hasMessage(final String message) {
        isNotNull();

        if (!actual.getMessage().equals(message)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, message, actual.getMessage());
        }

        return this;
    }

    /**
     * Verifies that the actual Issue's fileName is equal to the given one.
     *
     * @param fileName The given fileName to compare the actual Issue's fileName to.
     * @return this assertion object.
     * @throws AssertionError - if the actual Issue's fileName is not equal to the given one.
     */
    public IssueAssert hasFileName(final String fileName) {
        isNotNull();

        if (!actual.getFileName().equals(fileName)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, fileName, actual.getFileName());
        }

        return this;
    }

    /**
     * Verifies that the actual Issue's type is equal to the given one.
     *
     * @param type The given type to compare the actual Issue's type to.
     * @return this assertion object.
     * @throws AssertionError - if the actual Issue's type is not equal to the given one.
     */
    public IssueAssert hasType(final String type) {
        isNotNull();

        if (!actual.getType().equals(type)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, type, actual.getType());
        }

        return this;
    }

    /**
     * Verifies that the actual Issue's category is equal to the given one.
     *
     * @param category The given category to compare the actual Issue's category to.
     * @return this assertion object.
     * @throws AssertionError - if the actual Issue's category is not equal to the given one.
     */
    public IssueAssert hasCategory(final String category) {
        isNotNull();

        if (!actual.getCategory().equals(category)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, category, actual.getCategory());
        }

        return this;
    }

    /**
     * Verifies that the actual Issue's priority is equal to the given one.
     *
     * @param priority The given priority to compare the actual Issue's priority to.
     * @return this assertion object.
     * @throws AssertionError - if the actual Issue's priority is not equal to the given one.
     */
    public IssueAssert hasPriority(final Priority priority) {
        isNotNull();

        if (actual.getPriority() != priority) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, priority, actual.getPriority());
        }

        return this;
    }

    /**
     * Verifies that the actual Issue's packageName is equal to the given one.
     *
     * @param packageName The given packageName to compare the actual Issue's packageName to.
     * @return this assertion object.
     * @throws AssertionError - if the actual Issue's packageName is not equal to the given one.
     */
    public IssueAssert hasPackageName(final String packageName) {
        isNotNull();

        if (!actual.getPackageName().equals(packageName)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, packageName, actual.getPackageName());
        }

        return this;
    }

    /**
     * Verifies that the actual Issue's description is equal to the given one.
     *
     * @param description The given description to compare the actual Issue's description to.
     * @return this assertion object.
     * @throws AssertionError - if the actual Issue's description is not equal to the given one.
     */
    public IssueAssert hasDescription(final String description) {
        isNotNull();

        if (!actual.getDescription().equals(description)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, description, actual.getDescription());
        }

        return this;
    }


}
