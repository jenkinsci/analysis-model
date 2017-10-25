package edu.hm.hafner.edu.hm.hafner.analysis.edu.hm.hafner.analysis.assertions;

import java.util.Objects;
import org.assertj.core.api.AbstractAssert;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Priority;

/**
 * Custome assertion for Issue class.
 */
@SuppressWarnings("UnusedReturnValue")
public class IssueAssertions extends AbstractAssert<IssueAssertions, Issue> {


    /**
     * C.
     * @param actual = current issue.
     */
    public IssueAssertions(final Issue actual) {
        super(actual, IssueAssertions.class);
    }


    /**
     * Static creation.
     * @param actualIssue = current issue.
     * @return assertion.
     */
    public static IssueAssertions assertThat(final Issue actualIssue) {
        return new IssueAssertions(actualIssue);
    }


    /**
     * Proof filename.
     * @param filename = expected.
     * @return assertion.
     */
    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssueAssertions hasFileName(final String filename) {
        // check actual not null
        isNotNull();
        // check condition
        propertyEqualsCheck(actual.getFileName(), filename, "filename");
        // Return this for Fluent.
        return this;
    }

    /**
     * Proof lineStart.
     * @param lineStart = expected.
     * @return assertion.
     */
    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssueAssertions hasLineStart(final int lineStart) {
        // check actual not null
        isNotNull();
        // check condition
        propertyEqualsCheck(actual.getLineStart(), lineStart, "lineStart");
        // Return this for Fluent.
        return this;
    }

    /**
     * Proof lineEnd.
     * @param lineEnd = expected.
     * @return assertions.
     */
    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssueAssertions hasLineEnd(final int lineEnd) {
        // check actual not null
        isNotNull();
        // check condition
        propertyEqualsCheck(actual.getLineEnd(), lineEnd, "lineEnd");
        // Return this for Fluent.
        return this;
    }

    /**
     * Proof columnStart.
     * @param columnStart = expected.
     * @return assertions.
     */
    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssueAssertions hasColumnStart(final int columnStart) {
        // check actual not null
        isNotNull();
        // check condition
        propertyEqualsCheck(actual.getColumnStart(), columnStart, "columnStart");
        // Return this for Fluent.
        return this;
    }

    /**
     * Proof columnEnd.
     * @param columnEnd = expected.
     * @return assertions.
     */
    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssueAssertions hasColumnEnd(final int columnEnd) {
        // check actual not null
        isNotNull();
        // check condition
        propertyEqualsCheck(actual.getColumnEnd(), columnEnd, "columnEnd");
        // Return this for Fluent.
        return this;
    }

    /**
     * Proof category.
     * @param category = expected.
     * @return assertion.
     */
    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssueAssertions hasCategory(final String category) {
        // check actual not null
        isNotNull();
        // check condition
        propertyEqualsCheck(actual.getCategory(), category, "category");
        // Return this for Fluent.
        return this;
    }

    /**
     * Proof type.
     * @param type = expected.
     * @return assertions.
     */
    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssueAssertions hasType(final String type) {
        // check actual not null
        isNotNull();
        // check condition
        propertyEqualsCheck(actual.getType(), type, "type");
        // Return this for Fluent.
        return this;
    }

    /**
     * Proof package name.
     * @param packagename = expected.
     * @return assertion.
     */
    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssueAssertions hasPackagename(final String packagename) {
        // check actual not null
        isNotNull();
        // check condition
        propertyEqualsCheck(actual.getPackageName(), packagename, "packagename");
        // Return this for Fluent.
        return this;
    }

    /**
     * Proof priority.
     * @param priority = expected.
     * @return assertion.
     */
    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssueAssertions hasPriority(final Priority priority) {
        // check actual not null
        isNotNull();
        // check condition
        propertyEqualsCheck(actual.getPriority(), priority, "priority");
        // Return this for Fluent.
        return this;
    }

    /**
     * Proof description.
     * @param description = expected.
     * @return assertion.
     */
    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssueAssertions hasDescription(final String description) {
        // check actual not null
        isNotNull();
        // check condition
        propertyEqualsCheck(actual.getDescription(), description, "description");
        // Return this for Fluent.
        return this;
    }

    /**
     * Proof message.
     * @param message = expected.
     * @return assertion.
     */
    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssueAssertions hasMessage(final String message) {
        // check actual not null
        isNotNull();
        // check condition
        propertyEqualsCheck(actual.getMessage(), message, "message");
        // Return this for Fluent.
        return this;
    }

    /**
     * Proof fingerprint.
     * @param fingerprint = expected.
     * @return assertion.
     */
    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssueAssertions hasFingerprint(final String fingerprint) {
        // check actual not null
        isNotNull();
        // check condition
        propertyEqualsCheck(actual.getFingerprint(), fingerprint, "fingerprint");
        // Return this for Fluent.
        return this;
    }

    /**
     * Proof hashCode.
     * @param hashCode = expected.
     * @return assertion.
     */
    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssueAssertions hasHashCode(final int hashCode) {
        // check actual not null
        isNotNull();
        // check condition
        propertyEqualsCheck(actual.hashCode(), hashCode, "hashCode");
        // Return this for Fluent.
        return this;
    }

    /**
     * Proof other hashCode.
     * @param hashCode = expected.
     * @return assertion.
     */
    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssueAssertions hasNotHashCode(final int hashCode) {
        // check actual not null
        isNotNull();
        // check condition
        if (Objects.equals(actual.hashCode(), hashCode)) {
            failWithMessage("Expected issue's hashCode to be <%s> but was <%s>", hashCode, actual.hashCode());
        }
        // Return this for Fluent.
        return this;
    }

    /**
     * Proof toString.
     * @param issuesAsString = expected.
     * @return assertion.
     */
    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssueAssertions isString(final String issuesAsString) {
        // check actual not null
        isNotNull();
        // check condition
        propertyEqualsCheck(actual.toString(), issuesAsString, "toString");
        // Return this for Fluent.
        return this;
    }

    /**
     * Proof if a id exits.
     * @return assertion.
     */
    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssueAssertions hasAId() {
        // check actual not null
        isNotNull();
        // check condition
        if (actual.getId() == null) {
            failWithMessage("Expected issue has a Id");
        }
        // Return this for Fluent.
        return this;
    }

    /**
     * Easy check a property and generate message.
     * @param actualValue = actual.
     * @param expected = expected.
     * @param propertyName = name of property.
     * @param <T> = generic.
     */
    private <T> void propertyEqualsCheck(final T actualValue, final T expected, final String propertyName){
        if (!Objects.equals(actualValue, expected)) {
            failWithMessage("Expected issue's "+propertyName+" to be <%s> but was <%s>", expected, actualValue);
        }
    }
}
