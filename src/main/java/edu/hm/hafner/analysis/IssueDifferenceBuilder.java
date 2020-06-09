package edu.hm.hafner.analysis;
/**
 * Eine Builderklasse um das Erstellen eines LineDifference Objekts zu vereinfachen.
 * @author Michael Schober
 */
public class IssueDifferenceBuilder {
    private Report currentIssues;
    private Report referenceIssues;
    private String referenceID;

    IssueDifferenceBuilder setCurrentIssues(final Report currentIssues) {
        this.currentIssues = currentIssues;
        return this;
    }

    IssueDifferenceBuilder setReferenceIssues(final Report referenceIssues) {
        this.referenceIssues = referenceIssues;
        return this;
    }

    IssueDifferenceBuilder setReferenceID(final String referenceID) {
        this.referenceID = referenceID;
        return this;
    }
    /**
     * Eine neue IssueDifference.
     * @return IsseDifference
     */
    IssueDifference build() {
        return new IssueDifference(currentIssues, referenceID, referenceIssues);
    }
}
