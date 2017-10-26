package edu.hm.hafner;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Priority;

import org.assertj.core.api.AbstractAssert;

/**
 * *****************************************************************
 * Hochschule Muenchen Fakultaet 07 (Informatik).		**
 * Autor: Sebastian Balz
 * Datum 16.10.2017
 *  Software Win 7 JDK8 Win 10 JDK8 Ubuntu 15.4 OpenJDK7	**
 * edu.hm.hafner.analysis
 *
 */
public class IssueAssert extends AbstractAssert<IssueAssert, Issue> {

    /**
     * Test columnEnd.
     * @param columnEnd columnEnd
     * @return this
     */
    public IssueAssert hasColumnEnd(int columnEnd) {
        if (actual.getColumnEnd() != (columnEnd)) {
            failWithMessage("\nExpecting ColumnEnd of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual, actual.getColumnStart(), columnEnd);
        }
        return this;
    }

    /**
     * Test columnStart.
     * @param columnStart columnStart
     * @return this
     */
    public IssueAssert hasColumnStart(int columnStart) {
        if (actual.getColumnStart() != (columnStart)) {
            failWithMessage("\nExpecting ColumnStart of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual, actual.getColumnStart(), columnStart);
        }
        return this;
    }

    /**
     * Test lineEnd.
     * @param lineEnd lineEnd
     * @return this
     */
    public IssueAssert hasLineEnd(int lineEnd) {
        if (actual.getLineEnd() != (lineEnd)) {
            failWithMessage("\nExpecting LineEnd of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual, actual.getLineEnd(), lineEnd);
        }
        return this;
    }

    /**
     * Test lineStart.
     * @param lineStart lineStart
     * @return this
     */
    public IssueAssert hasLineStart(int lineStart) {
        if (actual.getLineStart() != (lineStart)) {
            failWithMessage("\nExpecting LineStart of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual, actual.getLineStart(), lineStart);
        }
        return this;
    }

    /**
     * Test packageName.
     * @param packageName packageName
     * @return this
     */
    public IssueAssert hasPackageName(String packageName) {
        isNotNull();
        if (!actual.getPackageName().equals(packageName)) {
            failWithMessage("\nExpecting PackageName of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual, actual.getPackageName(), packageName);
        }
        return this;
    }

    /**
     * Test description.
     * @param description description
     * @return this
     */
    public IssueAssert hasDescription(String description) {
        isNotNull();
        if (!actual.getDescription().equals(description)) {
            failWithMessage("\nExpecting Description of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual, actual.getDescription(), description);
        }
        return this;
    }

    /**
     * Test message.
     * @param message message
     * @return this
     */
    public IssueAssert hasMessage(String message) {
        isNotNull();
        if (!actual.getMessage().equals(message)) {
            failWithMessage("\nExpecting Message of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual, actual.getMessage(), message);
        }
        return this;
    }

    /**
     * Test priority.
     * @param priority priority
     * @return this
     */
    public IssueAssert hasPriority(Priority priority) {
        isNotNull();
        if (!actual.getPriority().equals(priority)) {
            failWithMessage("\nExpecting Priority of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual, actual.getPriority(), priority);
        }
        return this;
    }

    /**
     * Test type.
     * @param type type
     * @return this
     */
    public IssueAssert hasType(String type) {
        isNotNull();
        if (!actual.getType().equals(type)) {
            failWithMessage("\nExpecting Type of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual, actual.getType(), type);
        }
        return this;
    }

    /**
     * Test category.
     * @param category category
     * @return this
     */
    public IssueAssert hasCategory(String category) {
        isNotNull();
        if (!actual.getCategory().equals(category)) {
            failWithMessage("\nExpecting Category of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual, actual.getCategory(), category);
        }
        return this;
    }

    /**
     * Test FileName.
     * @param name name
     * @return this
     */
    public IssueAssert hasFileName(String name) {
        isNotNull();
        if (!actual.getFileName().equals(name)) {
            failWithMessage("\nExpecting FileName of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual, actual.getFileName(), name);
        }
        return this;
    }

    /**
     * Test FileName.
     * @param name name
     * @return this
     */
    public IssueAssert containsFileName(String name) {
        isNotNull();
        if (!actual.getFileName().contains(name)) {
            failWithMessage("\nExpecting FileName of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual, actual.getFileName(), name);
        }
        return this;
    }

    /**
     * Test Issue.
     * @param actual actual
     * @return this
     */
    public static IssueAssert assertThat(Issue actual) {
        return new IssueAssert(actual, IssueAssert.class);
    }

    /**
     * init.
     * @param issue issue
     * @param selfType type
     */
    private IssueAssert(Issue issue, Class<?> selfType) {
        super(issue, selfType);
    }

    /**
     * assert.
     * @param i par
     */
    IssueAssert(final Issue i) {
        super(i, IssueAssert.class);
    }
}
