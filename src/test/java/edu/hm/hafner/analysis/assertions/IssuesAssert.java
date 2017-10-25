package edu.hm.hafner.analysis.assertions;

import java.util.Objects;
import java.util.SortedSet;

import org.assertj.core.api.AbstractAssert;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;

/**
 * Custom Assertions for Issues.
 *
 * @author Mark Tripolt
 */
public class IssuesAssert extends AbstractAssert<IssuesAssert, Issues> {

    /**
     * Custom constructor.
     *
     * @param actual issue
     */
    public IssuesAssert(final Issues actual) {
        super(actual, IssuesAssert.class);
    }

    /**
     * Custom assertThat function for issues.
     *
     * @param actual issue
     * @return custom assertion
     */
    public static IssuesAssert assertThat(final Issues actual) {
        return new IssuesAssert(actual);
    }

    /**
     * Check for size.
     *
     * @param size wanted
     * @return custom assertion
     */
    public IssuesAssert hasSize(final int size) {
        isNotNull();
        if (!Objects.equals(actual.getSize(), size)) {
            failWithMessage("Expected Issues Size to be <%s> but was <%s>", size, actual.getSize());
        }
        return this;
    }

    /**
     * Check for number of files.
     *
     * @param numberOfFiles wanted
     * @return custom assertion
     */
    public IssuesAssert hasNumberOfFiles(final int numberOfFiles) {
        isNotNull();
        if (!Objects.equals(actual.getNumberOfFiles(), numberOfFiles)) {
            failWithMessage("Expected numberOfFiles to be <%s> but was <%s>", numberOfFiles, actual.getNumberOfFiles());
        }
        return this;
    }

    /**
     * Check for issue at a specific index.
     *
     * @param issue wanted
     * @param index expected
     * @return custom assertion
     */
    public IssuesAssert hasIssueOnIndex(final Issue issue, final int index) {
        isNotNull();
        if (!Objects.equals(actual.get(index), issue)) {
            failWithMessage("Expected issue to be <%s> on the index %s but was <%s>", issue, index, actual.get(index));
        }
        return this;
    }

    /**
     * Check for files.
     *
     * @param files wanted
     * @return custom assertion
     */
    public IssuesAssert hasFiles(final SortedSet<String> files) {
        isNotNull();

        if (!Objects.equals(actual.getFiles(), files)) {
            failWithMessage("Expected Files are not equal.");
        }
        return this;
    }


    /**
     * Check for high priority size.
     *
     * @param size wanted
     * @return custom assertion
     */
    public IssuesAssert hasHighPrioritySize(final int size) {
        isNotNull();
        if (!Objects.equals(actual.getHighPrioritySize(), size)) {
            failWithMessage("Expected High Priority Issues Size to be <%s> but was <%s>", size, actual.getHighPrioritySize());
        }
        return this;
    }

    /**
     * Check for normal priority size.
     *
     * @param size wanted
     * @return custom assertion
     */
    public IssuesAssert hasNormalPrioritySize(final int size) {
        isNotNull();
        if (!Objects.equals(actual.getNormalPrioritySize(), size)) {
            failWithMessage("Expected Normal Priority Issues Size to be <%s> but was <%s>", size, actual.getNormalPrioritySize());
        }
        return this;
    }

    /**
     * Check for low priority size.
     *
     * @param size wanted
     * @return custom assertion
     */
    public IssuesAssert hasLowPrioritySize(final int size) {
        isNotNull();
        if (!Objects.equals(actual.getLowPrioritySize(), size)) {
            failWithMessage("Expected Low Priority Issues Size to be <%s> but was <%s>", size, actual.getLowPrioritySize());
        }
        return this;
    }

}
