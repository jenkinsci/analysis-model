package edu.hm.hafner.analysis.assertj;

import java.util.Objects;

import org.assertj.core.api.AbstractAssert;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import static org.assertj.core.api.Assertions.*;

/**
 * Fluent assertions for {@link Issues}.
 */
public class IssuesAssert extends AbstractAssert<IssuesAssert, Issues> {

    private static final String EXPECTED_BUT_WAS_MESSAGE = "%nExpecting %s of:%n <%s>%nto be:%n <%s>%nbut was:%n <%s>.";

    public IssuesAssert(final Issues issues) {
        super(issues, IssuesAssert.class);
    }

    /**
     * Assert that the requested issue is present.
     *
     * @param expected issue to be present
     * @return this
     */
    public IssuesAssert hasIssue(final Issue expected) {
        isNotNull();

        if (actual.findByProperty(expected::equals).isEmpty()) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "issue", actual, expected, "not found");
        }

        return this;
    }


    /**
     * Assert that there are no issues present.
     */
    public IssuesAssert isEmpty() {
        isNotNull();

        if (!actual.all().isEmpty()) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "empty issues", actual, "empty", "not empty");
        }

        return this;
    }

    /**
     * Assert that there is single issues.
     *
     * @return this
     */
    public IssuesAssert isSingle() {
        isNotNull();

        if (actual.size() != 1) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "single issue", actual, "1", actual.getSize());
        }

        return this;
    }

    /**
     * Assert that there are issues present.
     *
     * @return this
     */
    public IssuesAssert isNotEmpty() {
        isNotNull();

        if (actual.all().isEmpty()) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "not empty issues", actual, "not empty", "empty");
        }

        return this;
    }

    /**
     * Assert that the number of present issues matches the expectation.
     *
     * @param expected number of issues
     * @return this
     */
    public IssuesAssert hasSize(final int expected) {
        isNotNull();

        if (!Objects.equals(actual.getSize(), expected)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "issues size", actual, expected, actual.getSize());
        }

        return this;
    }


    /**
     * Assert that there are no high priority issues present.
     *
     * @return this
     */
    public IssuesAssert hasNoHighPriorityIssues() {
        return hasHighPriorityIssues(0);
    }

    /**
     * Assert that there is the expected number of high priority issues.
     *
     * @param expected number of high priority issues
     * @return this
     */
    public IssuesAssert hasHighPriorityIssues(final int expected) {
        isNotNull();

        if (!Objects.equals(actual.getHighPrioritySize(), expected)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "issues with high priority size", actual, expected, actual.getHighPrioritySize());
        }

        return this;
    }


    /**
     * Assert that there are no low priority issues.
     *
     * @return this
     */
    public IssuesAssert hasNoLowPriorityIssues() {
        return hasLowPriorityIssues(0);
    }

    /**
     * Assert that there is the expected number of normal priority issues.
     *
     * @param expected number of normal priority issues.
     * @return this
     */
    public IssuesAssert hasNormalPriorityIssues(final int expected) {
        isNotNull();

        if (!Objects.equals(actual.getNormalPrioritySize(), expected)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "issues with normal priority size", actual, expected, actual.getNormalPrioritySize());
        }
        return this;
    }

    /**
     * Assert that there are no normal priority issues.
     *
     * @return this
     */
    public IssuesAssert hasNoNormalPriorityIssues() {
        return hasNormalPriorityIssues(0);
    }

    /**
     * Assert that there is the expected number of low priority issues.
     *
     * @param expected number of low priority issues
     * @return this
     */
    public IssuesAssert hasLowPriorityIssues(final int expected) {
        isNotNull();

        if (!Objects.equals(actual.getLowPrioritySize(), expected)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "issues with low priority size", actual, expected, actual.getLowPrioritySize());
        }

        return this;
    }

    /**
     * Asserts that there is the same issue store at the index.
     *
     * @param index issue is stored at
     * @param issue that should be stored at index
     * @return this
     */
    public IssuesAssert hasIssueAt(int index, Issue issue) {
        isNotNull();

        if (!Objects.equals(actual.get(index), issue)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "has issue", actual, issue, actual.get(index));
        }

        return this;
    }

    /**
     * Asserts that the issues at all are of expected size.
     *
     * @param expected size of the collection.
     * @return this
     */
    public IssuesAssert hasAllWithSize(final int expected) {
        isNotNull();

        if (!Objects.equals(actual.all().size(), expected)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "issues size", actual, expected, actual.all().size());
        }

        return this;
    }

    /**
     * Asserts that the expected number of files is present.
     *
     * @param expected number of files
     * @return this
     */
    public IssuesAssert hasNumberOfFiles(final int expected) {
        isNotNull();

        if (!Objects.equals(actual.getNumberOfFiles(), expected)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "number of files", actual, expected, actual.getNumberOfFiles());
        }

        return this;
    }

    /**
     * Asserts that the expected string is generated when toString is called.
     *
     * @param values values to be expected in result
     * @return this
     */
    public IssuesAssert hasStringRepresentationContaining(final CharSequence... values) {
        isNotNull();

        for (int i = 0; i < values.length; ++i) {
            if (!actual.toString().contains(values[i])) {
                failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "toString contains", actual, values[i], actual.toString());
            }
        }

        return this;
    }

    /**
     * Asserts that the expected issues are present.
     *
     * @param issues expected issues
     * @return this
     */
    public IssuesAssert hasIssues(Issue... issues) {
        assertThat(actual).contains(issues);

        return this;
    }

}

