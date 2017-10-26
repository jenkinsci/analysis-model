package edu.hm.hafner;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Priority;
import org.assertj.core.api.AbstractAssert;

class IssueAssert extends AbstractAssert<IssueAssert, Issue> {

    /**
     * Test columnEnd.
     */
    IssueAssert hasColumnEnd(int columnEnd) {
        if (actual.getColumnEnd() != (columnEnd))
            failWithMessage("\nExpecting ColumnEnd of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual, actual.getColumnStart() ,columnEnd);
        return this;
    }

    /**
     * Test columnStart.
     */
    IssueAssert hasColumnStart(int columnStart) {
        if (actual.getColumnStart() != (columnStart))
            failWithMessage("\nExpecting ColumnStart of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual,actual.getColumnStart() ,columnStart);
        return this;
    }

    /**
     * Test lineEnd.
     */
    IssueAssert hasLineEnd(int lineEnd) {
        if (actual.getLineEnd() != (lineEnd))
            failWithMessage("\nExpecting LineEnd of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual,actual.getLineEnd() ,lineEnd);
        return this;
    }

    /**
     * Test lineStart.
     */
    IssueAssert hasLineStart(int lineStart) {
        if (actual.getLineStart() != (lineStart))
            failWithMessage("\nExpecting LineStart of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual, actual.getLineStart(),lineStart);
        return this;
    }

    /**
     * Test packageName.
     */
    IssueAssert hasPackageName(String packageName) {
        isNotNull();
        if (!actual.getPackageName().equals(packageName))
            failWithMessage( "\nExpecting PackageName of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual,actual.getPackageName() , packageName);
        return this;
    }

    /**
     * Test description.
     */
    IssueAssert hasDescription(String description) {
        isNotNull();
        if (!actual.getDescription().equals(description))
            failWithMessage("\nExpecting Description of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual, actual.getDescription(), description);
        return this;
    }

    /**
     * Test message.
     */
    IssueAssert hasMessage(String message) {
        isNotNull();
        if (!actual.getMessage().equals(message))
            failWithMessage("\nExpecting Message of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual,actual.getMessage(), message);
        return this;
    }

    /**
     * Test priority.
     */
    IssueAssert hasPriority(Priority priority) {
        isNotNull();
        if (!actual.getPriority().equals(priority))
            failWithMessage("\nExpecting Priority of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual,actual.getPriority(), priority);
        return this;
    }

    /**
     * Test type.
     */
    IssueAssert hasType(String type) {
        isNotNull();
        if (!actual.getType().equals(type))
            failWithMessage("\nExpecting Type of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual, actual.getType(),type);
        return this;
    }

    /**
     * Test category.
     */
    IssueAssert hasCategory(String category) {
        isNotNull();
        if (!actual.getCategory().equals(category))
            failWithMessage("\nExpecting Category of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual,actual.getCategory(),category);
        return this;
    }

    /**
     * Test FileName.
     */
    IssueAssert hasFileName(String name) {
        isNotNull();
        if (!actual.getFileName().equals(name))
            failWithMessage("\nExpecting FileName of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual,actual.getFileName() , name);
        return this;
    }

    /**
     * Test FileName.
     */
    IssueAssert containsFileName(String name) {
        isNotNull();
        if (!actual.getFileName().contains(name))
            failWithMessage("\nExpecting FileName of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual, actual.getFileName() ,name);
        return this;
    }

    /**
     * Test Issue.
     */
    static IssueAssert assertThat(Issue actual) {
        return new IssueAssert(actual, IssueAssert.class);
    }

    private IssueAssert(Issue issue, Class<?> selfType) {
        super(issue, selfType);
    }

    IssueAssert(final Issue i) {
        super(i, IssueAssert.class);
    }
}
