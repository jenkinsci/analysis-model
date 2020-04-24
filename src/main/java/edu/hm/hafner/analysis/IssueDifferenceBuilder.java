package edu.hm.hafner.analysis;

public class IssueDifferenceBuilder {
    private Report currentIssues;
    private String referenceId;
    private Report referenceIssues;

    public IssueDifferenceBuilder(final Report currentIssues, final String referenceId, final Report referenceIssues) {
        this.currentIssues = currentIssues;
        this.referenceId = referenceId;
        this.referenceIssues = referenceIssues;
    }

    public IssueDifferenceBuilder() {

    }

    public IssueDifferenceBuilder setCurrentIssues(final Report currentIssues) {
        this.currentIssues = currentIssues.copy();
        return this;
    }

    public IssueDifferenceBuilder setReferenceIssues(final Report referenceIssues) {
        this.referenceIssues = referenceIssues.copy();
        return this;
    }

    public IssueDifferenceBuilder setReferenceId(final String referenceId) {
        this.referenceId = referenceId;
        return this;
    }

    public IssueDifference build(){
        return new IssueDifference(currentIssues, referenceId, referenceIssues);
    }
}
