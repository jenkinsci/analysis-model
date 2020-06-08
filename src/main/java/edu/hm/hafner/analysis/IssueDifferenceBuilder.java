package edu.hm.hafner.analysis;

public class IssueDifferenceBuilder {
    /**
     * Current Issues
     */
    private Report currentIssues;

    /**
     * Refrence Id
     */
    private String refrenceId;

    /**
     * Refrence Issues
     */
    private Report refrenceIssues;

    public IssueDifferenceBuilder() {
    }

    /**
     * Set the value of the current Issues
     * @param currentIssues
     * @return this
     */
    public IssueDifferenceBuilder setCurrentIssues(final Report currentIssues) {
        this.currentIssues = currentIssues;
        return this;
    }

    /**
     * Set the value of the refrence Id
     * @param refrenceId
     * @return this
     */
    public IssueDifferenceBuilder setRefrenceId(final String refrenceId) {
        this.refrenceId = refrenceId;
        return this;
    }

    /**
     * Set the value of the refrence Issues
     * @param refrenceIssues
     * @return this
     */
    public IssueDifferenceBuilder setRefrenceIssues(final Report refrenceIssues) {
        this.refrenceIssues = refrenceIssues;
        return this;
    }

    /**
     *  Builds an IssueDiffrence Object
     * @return a new IssueDiffrence Object
     */
    public IssueDifference build(){
        return new IssueDifference(currentIssues, refrenceId, refrenceIssues);
    }

}
