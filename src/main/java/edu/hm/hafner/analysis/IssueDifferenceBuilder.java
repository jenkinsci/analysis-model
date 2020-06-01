package edu.hm.hafner.analysis;

public class IssueDifferenceBuilder {

    //final Report currentIssues, final String referenceId, final Report referenceIssues

    private Report currentIssues = null;
    private String referenceId = null;
    private Report referenceIssues = null;

    public IssueDifference build() {
        if (currentIssues == null || referenceId == null || referenceIssues == null) {
            throw new IllegalStateException("All values must be set");
        }

        return new IssueDifference(currentIssues, referenceId, referenceIssues);
    }

    public IssueDifferenceBuilder withCurrentIssues(final Report aCurrentIssues) {
        currentIssues = aCurrentIssues;
        return this;
    }

    public IssueDifferenceBuilder withReferenceIssues(final Report aReferenceIssues) {
        referenceIssues = aReferenceIssues;
        return this;
    }

    public IssueDifferenceBuilder withReferenceId(final String aReferenceId) {
        referenceId = aReferenceId;
        return this;
    }




}
