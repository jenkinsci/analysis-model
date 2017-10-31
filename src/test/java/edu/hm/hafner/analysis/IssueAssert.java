package edu.hm.hafner.analysis;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Priority;

import org.assertj.core.api.AbstractAssert;


/**
 * AssertJ for Issue. This class allows fluent interface for Issue
 *
 * @author Sebastian Balz
 */
public class IssueAssert extends AbstractAssert<IssueAssert, Issue> {

    /**
     * Test columnEnd.
     *
     * @param columnEnd columnEnd
     * @return this
     */
    public IssueAssert hasColumnEnd(int columnEnd) {
        isNotNull();
        if (actual.getColumnEnd() != columnEnd) {
            failWithMessage("\nExpecting ColumnEnd of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual, actual.getColumnStart(), columnEnd);
        }
        return this;
    }

    /**
     * Test columnStart.
     *
     * @param columnStart columnStart
     * @return this
     */
    public IssueAssert hasColumnStart(int columnStart) {
        isNotNull();
        if (actual.getColumnStart() != columnStart) {
            failWithMessage("\nExpecting ColumnStart of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual, actual.getColumnStart(), columnStart);
        }
        return this;
    }

    /**
     * Test lineEnd.
     *
     * @param lineEnd lineEnd
     * @return this
     */
    public IssueAssert hasLineEnd(int lineEnd) {
        isNotNull();
        if (actual.getLineEnd() != lineEnd) {
            failWithMessage("\nExpecting LineEnd of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual, actual.getLineEnd(), lineEnd);
        }
        return this;
    }

    /**
     * Test lineStart.
     *
     * @param lineStart lineStart
     * @return this
     */
    public IssueAssert hasLineStart(int lineStart) {
        isNotNull();
        if (actual.getLineStart() != lineStart) {
            failWithMessage("\nExpecting LineStart of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual, actual.getLineStart(), lineStart);
        }
        return this;
    }

    /**
     * Test packageName.
     *
     * @param packageName packageName
     * @return this
     */
    public IssueAssert hasPackageName(String packageName) {
        isNotNull();
        isNotNull();
        if (!actual.getPackageName().equals(packageName)) {
            failWithMessage("\nExpecting PackageName of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual, actual.getPackageName(), packageName);
        }
        return this;
    }

    /**
     * Test description.
     *
     * @param description description
     * @return this
     */
    public IssueAssert hasDescription(String description) {
        isNotNull();
        isNotNull();
        if (!actual.getDescription().equals(description)) {
            failWithMessage("\nExpecting Description of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual, actual.getDescription(), description);
        }
        return this;
    }

    /**
     * Test message.
     *
     * @param message message
     * @return this
     */
    public IssueAssert hasMessage(String message) {
        isNotNull();
        isNotNull();
        if (!actual.getMessage().equals(message)) {
            failWithMessage("\nExpecting Message of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual, actual.getMessage(), message);
        }
        return this;
    }

    /**
     * Test priority.
     *
     * @param priority priority
     * @return this
     */
    public IssueAssert hasPriority(Priority priority) {
        isNotNull();
        isNotNull();
        if (!actual.getPriority().equals(priority)) {
            failWithMessage("\nExpecting Priority of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual, actual.getPriority(), priority);
        }
        return this;
    }

    /**
     * Test type.
     *
     * @param type type
     * @return this
     */
    public IssueAssert hasType(String type) {
        isNotNull();
        isNotNull();
        if (!actual.getType().equals(type)) {
            failWithMessage("\nExpecting Type of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual, actual.getType(), type);
        }
        return this;
    }

    /**
     * Test category.
     *
     * @param category category
     * @return this
     */
    public IssueAssert hasCategory(String category) {
        isNotNull();
        isNotNull();
        if (!actual.getCategory().equals(category)) {
            failWithMessage("\nExpecting Category of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual, actual.getCategory(), category);
        }
        return this;
    }

    /**
     * Test ordinal.
     *
     * @param ordinal ordinal
     * @return this
     */
    public IssueAssert hasOrdinal(int ordinal) {
        isNotNull();
        isNotNull();
        if (actual.getOrdinal() != ordinal) {
            failWithMessage("\nExpecting ordinal of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual, actual.getOrdinal(), ordinal);
        }
        return this;
    }

    /**
     * Test FileName.
     *
     * @param name name
     * @return this
     */
    public IssueAssert hasFileName(String name) {
        isNotNull();
        isNotNull();
        if (!actual.getFileName().equals(name)) {
            failWithMessage("\nExpecting FileName of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual, actual.getFileName(), name);
        }
        return this;
    }

    /**
     * Test FileName.
     *
     * @param name name
     * @return this
     */
    public IssueAssert hasFileNameContaining(String name) {
        isNotNull();
        if (!actual.getFileName().contains(name)) {
            failWithMessage("\nExpecting FileName of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual, actual.getFileName(), name);
        }
        return this;
    }

    /**
     * Fluent interface for Issue.
     *
     * @param actual actual
     * @return this
     */
    public static IssueAssert assertThat(Issue actual) {
        return new IssueAssert(actual, IssueAssert.class);
    }

    /**
     * init.
     *
     * @param issue    issue
     * @param selfType type
     */
    private IssueAssert(Issue issue, Class<?> selfType) {
        super(issue, selfType);
    }

    /**
     * assert.
     *
     * @param i par
     */
    IssueAssert(final Issue i) {
        super(i, IssueAssert.class);
    }
}
