package edu.hm.hafner.analysis;


/**
 * Creates new {@link IssueDifference} using the builder pattern.
 *
 * @author Elena Lilova
 */

public class IssueDifferenceBuilder {

    private Report currentIssues;
    private Report referenceIssues;
    private String referenceID;

    /**
     * Sets the the currentIssues
     *
     * @return this
     */
    public IssueDifferenceBuilder setCurrentIssues(final Report currentIssues) {
        this.currentIssues = currentIssues;
        return this;
    }

    /**
     * Sets the the referenceIssues
     *
     * @return this
     */
    public IssueDifferenceBuilder setReferenceIssues(final Report referenceIssues) {
        this.referenceIssues = referenceIssues.copy();
        return this;
    }

    /**
     * Sets the the referenceId
     *
     * @return this
     */
    public IssueDifferenceBuilder setReferenceID(final String referenceId) {
        this.referenceID = referenceId;
        return this;
    }

    /**
     * Creates a new {@link IssueDifference} based on the specified properties.
     *
     * @return the created IssueDifference
     */
    public IssueDifference build() {
        return new IssueDifference(currentIssues, referenceID, referenceIssues);
    }

}
