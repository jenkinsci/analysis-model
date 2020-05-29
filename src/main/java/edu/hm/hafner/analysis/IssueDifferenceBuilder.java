package edu.hm.hafner.analysis;
/**
 * Builder for IssueDifference
 *
 * @author Viet Phuoc Ho (v.ho@hm.edu)
 */
public class IssueDifferenceBuilder {

    /**
     * The current Issue.
     */
    private Report currentIssues;

    /**
     * The ID of the Reference for this Issue.
     */
    private String referenceId;

    /**
     * The referenced Issue
     */
    private Report referenceIssues;

    /**
     * Setter for the current issues.
     * @param currentIssue
     *                  the current issue
     */
    public IssueDifferenceBuilder setCurrentIssues(final Report currentIssue) {
        this.currentIssues = currentIssue;
        return this;
    }

    /**
     * Setter for the reference ID.
     * @param referenceID
     *                  of the issue
     */
    public IssueDifferenceBuilder setReferenceId(final String referenceID) {
        this.referenceId = referenceID;
        return this;
    }

    /**
     * Setter for the ReferenceIssues.
     * @param referenceIssues the reference Issues
     */
    public IssueDifferenceBuilder setReferenceIssues(final Report referenceIssues) {
        this.referenceIssues = referenceIssues;
        return this;
    }

    /**
     * Builder for the IssueDifference
     * @return new IssueDifference object.
     */
    IssueDifference build() {
        return new IssueDifference(this.currentIssues, this.referenceId, this.referenceIssues);
    }
}