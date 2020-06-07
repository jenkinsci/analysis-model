package edu.hm.hafner.analysis;

import edu.umd.cs.findbugs.annotations.Nullable;

public class IssueDifferenceBuilder {

    @Nullable
    private Report currentIssues;
    private String referenceId = "";
    @Nullable
    private Report referenceIssues;

    public IssueDifferenceBuilder setCurrentIssues(final Report currentIssues) {
        this.currentIssues = currentIssues;
        return this;
    }

    public IssueDifferenceBuilder setReferenceId(final String referenceId) {
        this.referenceId = referenceId;
        return this;
    }

    public IssueDifferenceBuilder setReferenceIssues(final Report referenceIssues) {
        this.referenceIssues = referenceIssues;
        return this;
    }

    public IssueDifference build(){
        return new IssueDifference(currentIssues, referenceId, referenceIssues);
    }
}
