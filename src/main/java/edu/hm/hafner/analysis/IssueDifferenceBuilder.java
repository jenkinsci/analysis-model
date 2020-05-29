package edu.hm.hafner.analysis;

/**
 * Creates new {@link IssueDifference} using the builder pattern. All properties that have not been set in the builder will
 * be set to their default value.

 * @author Tobias Karius
 */

public class IssueDifferenceBuilder {

    /**
     * The issues of the current report.
     */
    private Report currentIssues;

    /**
     * The ID identifying the reference report.
     */
    private Report referenceIssues;

    /**
     * The issues of a previous report (reference).
     */
    private String referenceId;

    /**
     * Sets the issues of the current report.
     * @param currentIssues the issues of the current report
     */
    public void setCurrentIssues(final Report currentIssues) {
        this.currentIssues = currentIssues;
    }

    /**
     * Sets the ID identifying the reference report.
     * @param referenceIssues the ID identifying the reference report
     */
    public void setReferenceIssues(final Report referenceIssues) {
        this.referenceIssues = referenceIssues;
    }

    /**
     * Sets the issues of a previous report.
     * @param referenceId the issues of a previous report
     */
    public void setReferenceId(final String referenceId) {
        this.referenceId = referenceId;
    }

    /**
     * Creates a new {@link IssueDifference} based on the specified properties.
     * @return the created IssueDifference
     */
    public IssueDifference build() {
        return new IssueDifference(currentIssues, referenceId, referenceIssues);
    }

}
