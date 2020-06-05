package edu.hm.hafner.analysis;

/**
 * IssueDifference Builder.
 */
public class IssueDifferenceBuilder {

    private Report currentIssue;

    private String referenceReportId;

    private Report previousReport;

    /**
     * Creates a new IssueDifference from Builder parameters.
     * @return IssueDifference
     */
    public IssueDifference build() {
        if (currentIssue == null) {
            throw new IllegalStateException("Set Current Issues first");
        }
        if (referenceReportId == null) {
            throw new IllegalStateException("Set Reference Report ID");
        }
        if (previousReport == null) {
            throw new IllegalStateException("Set Previous Report");
        }

        return new IssueDifference(currentIssue, referenceReportId, previousReport);
    }

    /**
     * Sets the current Issues.
     * @param currentIssue current Issue
     * @return Builder instance
     */
    public IssueDifferenceBuilder setCurrentIssues(final Report currentIssue) {
        if (currentIssue == null) {
            throw new NullPointerException("Current Issues cant be Null");
        }
        if (this.currentIssue != null) {
            throw new IllegalStateException("Current Issues can only be set once");
        }

        this.currentIssue = currentIssue;
        return this;
    }

    /**
     * Sets current Reference Report ID.
     * @param referenceReportId Reference Report ID
     * @return Builder instance
     */
    public IssueDifferenceBuilder setReferenceReportId(final String referenceReportId) {
        if (referenceReportId == null)  {
            throw new NullPointerException("Reference Report ID cant be Null");
        }
        if (this.referenceReportId != null) {
            throw new IllegalStateException("Reference Report ID can only be set once");
        }

        this.referenceReportId = referenceReportId;
        return this;
    }

    /**
     * Sets Previous Report.
     * @param previousReport Previous Report
     * @return Builder instance
     */
    public IssueDifferenceBuilder setPreviousReport(final Report previousReport) {
        if (previousReport == null)  {
            throw new NullPointerException("Previous Report cant be Null");
        }
        if (this.previousReport != null) {
            throw new IllegalStateException("Previous Report can only be set once");
        }

        this.previousReport = previousReport;
        return this;
    }
}
