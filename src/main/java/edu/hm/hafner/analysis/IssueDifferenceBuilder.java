package edu.hm.hafner.analysis;
/**
 * Eine Builderklasse um das Erstellen eines LineDifference Objekts zu vereinfachen.
 * @author Michael Schober
 */
public class IssueDifferenceBuilder {
    private Report currentIssues;
    private Report referenceIssues;
    private String referenceID;

    public IssueDifferenceBuilder setCurrentIssues(final Report currentIssues) {
        this.currentIssues = currentIssues;
        return this;
    }

    public IssueDifferenceBuilder setReferenceIssues(final Report referenceIssues) {
        this.referenceIssues = referenceIssues;
        return this;
    }

    public IssueDifferenceBuilder setReferenceID(final String referenceID) {
        this.referenceID = referenceID;
        return this;
    }
    /**
     * Eine neue IssueDifference.
     * @return IsseDifference
     */
    public IssueDifference build() {
        return new IssueDifference(currentIssues, referenceID, referenceIssues);
    }
}
