package edu.hm.hafner.analysis;

/**
 * Builderclass for {@link IssueDifference}.
 * There are no default properties. build() throws an IllegalStateException,
 * if it gets called without all properties beeing assigned before.
 * One can change the properties by using the Fluent Interface.
 *
 * <pre>{@code
 * IssueDifference issuediff = new IssueDifferenceBuilder()
 *                      .setCurrentIssues(new Report())
 *                      .setReferenceId("15218")
 *                      .setReferenceIssues(new Report())
 *                      .build();
 * }</pre>
 *
 * @author Daniel Soukup
 */

public class IssueDifferenceBuilder {
    private Report currentIssues;
    private String referenceId;
    private Report referenceIssues;

    /**
     * Sets the Report for the current Issues.
     *
     * @param currentIssues
     *         the Report with the current Issues
     * @return this
     */
    public IssueDifferenceBuilder setCurrentIssues(final Report currentIssues) {
        this.currentIssues = currentIssues.copy();
        return this;
    }

    /**
     * Sets the Report for the reference Issues.
     *
     * @param referenceIssues
     *         the Report with the Issues to compare with the current one
     * @return this
     */
    public IssueDifferenceBuilder setReferenceIssues(final Report referenceIssues) {
        this.referenceIssues = referenceIssues.copy();
        return this;
    }

    /**
     * Sets the identifying number of the IssueDifference.
     *
     * @param referenceId
     *         the identifying number
     * @return this
     */
    public IssueDifferenceBuilder setReferenceId(final String referenceId) {
        this.referenceId = referenceId;
        return this;
    }

    /**
     * Creates a new {@link IssueDifference} Object based on the defined properties.
     * @return the created IssueDifference Object
     */
    public IssueDifference build() {
        if (currentIssues == null || referenceIssues == null || referenceId == null) {
            throw new IllegalStateException("Unable to build IssueDifference before all properties are defined");
        }
        return new IssueDifference(currentIssues, referenceId, referenceIssues);
    }
}
