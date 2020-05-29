package edu.hm.hafner.analysis;

public class IssueDifferenceBuilder {

    /**
     * the current issue.
     */
    private Report currentIssues;

    /**
     * the reference id.
     */
    private String referenceId;

    /**
     * the reference Issue.
     */
    private Report referenceIssues;

    /**
     * Setter for the current issues.
     * @param currentIssue the issue
     * @return this.
     */
    public IssueDifferenceBuilder setCurrentIssues(final Report currentIssue) {
        this.currentIssues = currentIssue;
        return this;
    }

    /**
     * The reference id.
     * @param refID of the issue
     * @return this.
     */
    public IssueDifferenceBuilder setReferenceId(final String refID) {
        this.referenceId = refID;
        return this;
    }

    /**
     * The reference Issue.
     * @param referenceIssues the reference Issues
     * @return this.
     */
    public IssueDifferenceBuilder setReferenceIssues(final Report referenceIssues) {
        this.referenceIssues = referenceIssues;
        return this;
    }

    /**
     * builder for the IssueDifference.
     * @return new IssueDifference object.
     */
    IssueDifference build() {
        return new IssueDifference(this.currentIssues, this.referenceId, this.referenceIssues);
    }
}
