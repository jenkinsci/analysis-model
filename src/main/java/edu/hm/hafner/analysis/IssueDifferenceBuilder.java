package edu.hm.hafner.analysis;

public class IssueDifferenceBuilder {

    /**
     * The current issue.
     */
    private Report currentIssues;

    /**
     * The reference id.
     */
    private String referenceId;

    /**
     * The reference Issue.
     */
    private Report referenceIssues;

    /**
     * Setter for the current issues.
     * @param currentIssue the issue
     */
    public IssueDifferenceBuilder setCurrentIssues(final Report currentIssue) {
        this.currentIssues = currentIssue;
        return this;
    }

    /**
     * The reference id.
     * @param refID from the issue
     */
    public IssueDifferenceBuilder setReferenceId(final String refID) {
        this.referenceId = refID;
        return this;
    }

    /**
     * The reference Issue.
     *
     * @param referenceIssues the reference Issues
     */
    public IssueDifferenceBuilder setReferenceIssues(final Report referenceIssues) {
        this.referenceIssues = referenceIssues;
        return this;
    }

    /**
     * Builder for the IssueDifference.
     *
     * @return new IssueDifference object.
     */
    IssueDifference build() {
        return new IssueDifference(this.currentIssues, this.referenceId, this.referenceIssues);
    }
}
