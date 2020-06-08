package edu.hm.hafner.analysis;

/**
 * Provides a builder for class {@link IssueDifference}
 *
 * @author Matthias König
 */

public class IssueDifferenceBuilder {
    Report newIssues;
    Report fixedIssues;
    Report outstandingIssues;
    Report currentIssues;
    Report referenceIssues;
    String referenceId;



    public IssueDifferenceBuilder outstandingIssues (Report outstandingIssuesReported) {
        outstandingIssues = outstandingIssuesReported;
        return this;
    }

    public IssueDifferenceBuilder newIssues (Report newIssuesReported) {
        newIssues = newIssuesReported.copy();
        return this;
    }

    public IssueDifferenceBuilder fixedIssues (Report fixedIssuesReported) {
        fixedIssues = fixedIssuesReported.copy();
        return this;
    }

    public IssueDifferenceBuilder currentIssues (Report currentIssuesReported) {
        fixedIssues = currentIssues;
        return this;
    }

    public IssueDifference build() {
        return new IssueDifference(this, referenceId);
    }




}
