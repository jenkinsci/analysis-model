package edu.hm.hafner.analysis.Assertions;

import java.util.SortedSet;

import org.assertj.core.api.AbstractAssert;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;

/**
 * Custom assertion for issues.
 * @author Tom Maier
 * @author Johannes Arzt
 */
public class IssuesAssert extends AbstractAssert<IssuesAssert, Issues> {

    /**
     * Custom constructor.
     *
     * @param actual current issues
     */
    public IssuesAssert(final Issues actual) {
        super(actual, IssuesAssert.class);
    }

    /**
     * Custom assert function for issues.
     *
     * @param actual current issues
     * @return custom assertion
     */
    public static IssuesAssert assertThat(final Issues actual) {
        return new IssuesAssert(actual);
    }

    /**
     * Check issues for specific size.
     *
     * @param size filter for size
     * @return custom assertion
     */
    public IssuesAssert hasSize(final int size) {
        isNotNull();

        if (actual.getSize() != size) {
            failWithMessage("Expected size to be <%s> but was <%s>", size, actual.getSize());
        }
        return this;
    }

    /**
     * Check issues for high priority size.
     *
     * @param highPrioritySize filter high priority size
     * @return custom assertion
     */
    public IssuesAssert hasHighPrioritySize(final int highPrioritySize) {
        isNotNull();

        if (actual.getHighPrioritySize() != highPrioritySize) {
            failWithMessage("Expected highPrioritySize to be <%s> but was <%s>", highPrioritySize, actual.getHighPrioritySize());
        }
        return this;
    }

    /**
     * Check issues for normal priority size.
     *
     * @param normalPrioritySize filter normal priority size
     * @return custom assertion
     */
    public IssuesAssert hasNormalPrioritySize(final int normalPrioritySize) {
        isNotNull();

        if (actual.getNormalPrioritySize() != normalPrioritySize) {
            failWithMessage("Expected normalPrioritySize to be <%s> but was <%s>", normalPrioritySize, actual.getNormalPrioritySize());
        }
        return this;
    }

    /**
     * Check issues for low priority size.
     *
     * @param lowPrioritySize filter low priority size
     * @return custom assertion
     */
    public IssuesAssert hasLowPrioritySize(final int lowPrioritySize) {
        isNotNull();

        if (actual.getLowPrioritySize() != lowPrioritySize) {
            failWithMessage("Expected lowPrioritySize to be <%s> but was <%s>", lowPrioritySize, actual.getLowPrioritySize());
        }
        return this;
    }

    /**
     * Check issues for number of files.
     *
     * @param numberOfFiles filter number of files
     * @return custom assertion
     */
    public IssuesAssert hasNumberOfFiles(final int numberOfFiles) {
        isNotNull();

        if (actual.getNumberOfFiles() != numberOfFiles) {
            failWithMessage("Expected numberOfFiles to be <%s> but was <%s>", numberOfFiles, actual.getNumberOfFiles());
        }
        return this;
    }

    /**
     * Check if issue is at specific index.
     *
     * @param issue current issue
     * @param index position
     * @return custom assertion
     */
    public IssuesAssert hasIssueOnIndex(final Issue issue, final int index) {
        isNotNull();

        if (actual.get(index) != issue) {
            failWithMessage("Expected issue to be <%s> on the index %s but was <%s>", issue, index, actual.get(index));
        }
        return this;
    }

    /**
     * Check if issues has specific files.
     *
     * @param files files in a set
     * @return custom assertion
     */
    public IssuesAssert hasFiles(final SortedSet<String> files) {
        isNotNull();

        if (!actual.getFiles().equals(files)) {
            failWithMessage("Expected sets are not the same");
        }
        return this;
    }


}
