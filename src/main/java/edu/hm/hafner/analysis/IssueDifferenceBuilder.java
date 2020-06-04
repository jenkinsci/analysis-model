package edu.hm.hafner.analysis;

/**
 * A builder for the IssueDifference Class.
 */
public class IssueDifferenceBuilder {
    private boolean isCurrentIssueSet;
    private Report currentIssues;

    private boolean isReferenceIdSet;
    private String referenceId;

    private boolean isReferenceIssuesSet;
    private Report referenceIssues;

    /**
     * Builds a new IssueDifference from the set values.
     * @return The new IssueDifference
     */
    public IssueDifference build() {
        if(!isCurrentIssueSet) {
            throw new IllegalStateException("CurrentIssue has to be set in order to build a IssueDifference");
        }
        if(!isReferenceIdSet) {
            throw new IllegalStateException("ReferenceId has to be set in order to build a IssueDifference");
        }
        if(!isReferenceIssuesSet) {
            throw new IllegalStateException("ReferenceIssue has to be set in order to build a IssueDifference");
        }
        return new IssueDifference(currentIssues, referenceId, referenceIssues);
    }

    /**
     * @param currentIssues The Issue of the current Report.
     * @return The builder instance
     */
    public IssueDifferenceBuilder setCurrentIssues(Report currentIssues) {
        if(isCurrentIssueSet) {
            throw new IllegalStateException("CurrentIssue can only be set once!");
        }
        isCurrentIssueSet = true;
        this.currentIssues = currentIssues;
        return this;
    }

    /**
     * @param referenceId The ID of the report.
     * @return The builder instance
     */
    public IssueDifferenceBuilder setReferenceId(String referenceId) {
        if(isReferenceIdSet) {
            throw new IllegalStateException("ReferenceId can only be set once!");
        }
        isReferenceIdSet = true;
        this.referenceId = referenceId;
        return this;
    }

    /**
     * @param referenceIssues The Issue of the previous Report.
     * @return The builder instance
     */
    public IssueDifferenceBuilder setReferenceIssues(Report referenceIssues) {
        if(isReferenceIssuesSet) {
            throw new IllegalStateException("ReferenceIssue can only be set once!");
        }
        isReferenceIssuesSet = true;
        this.referenceIssues = referenceIssues;
        return this;
    }
}
