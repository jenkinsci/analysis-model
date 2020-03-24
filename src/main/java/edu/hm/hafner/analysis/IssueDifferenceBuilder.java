package edu.hm.hafner.analysis;

/***
 * Creates new {@link IssueDifference issueDifference} using the builder pattern. All properties that have not been set in the builder will
 * be set to their default value.
 *
 * <p>Example:</p>
 *  * <blockquote><pre>
 *  * IssueDifference issueDifference = new IssueDifferenceBuilder()
 *  *                      .setCurrentIssues(new Report().add(createIssue(currentMessage, currentFingerprint)))
 *  *                      .setReferenceId("2")
 *  *                      .setReferenceIssues(new Report().add(createIssue("OLD", "OLD")))
 *  *                      .build();
 *  * </pre></blockquote>
 *
 * @author budelmann
 */

public class IssueDifferenceBuilder {

    private Report currentIssues = null;
    private String referenceId = null;
    private Report referenceIssues = null;

    /**
     * Sets the issues of the current report.
     *
     * @param currentIssues
     *           the current issues
     *
     * @return this
     */
    public IssueDifferenceBuilder setCurrentIssues(final Report currentIssues) {
        this.currentIssues = currentIssues;
        return this;
    }

    /**
     * Sets the id identifying the reference report.
     *
     * @param referenceId
     *          the identifying id
     *
     * @return this
     */
    public IssueDifferenceBuilder setReferenceId(final String referenceId) {
        this.referenceId = referenceId;
        return this;
    }

    /**
     * Sets the issues of a previous report (reference).
     *
     * @param referenceIssues
     *          the issues of a previous report
     *
     * @return this
     */
    public IssueDifferenceBuilder setReferenceIssues(final Report referenceIssues) {
        this.referenceIssues = referenceIssues;
        return this;
    }

    /**
     * Creates a new {@link IssueDifference} based on the specified properties.
     *
     * @return the created IssueDifference
     */
    public IssueDifference build() {
        return new IssueDifference(currentIssues, referenceId, referenceIssues);
    }
}
