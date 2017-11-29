package edu.hm.hafner.analysis;

/**
 * Checks whether issues should be included or not.
 */
public interface IssueFilter {

    /**
     * Filter including all {@link Issue}s,
     */
    IssueFilter NO_OP = issue -> true;

    /**
     * Checkes whether an issue is included in the result set.
     *
     * @param issue
     *         to check
     *
     * @return is included
     */
    boolean isIncluded(final Issue issue);
}
