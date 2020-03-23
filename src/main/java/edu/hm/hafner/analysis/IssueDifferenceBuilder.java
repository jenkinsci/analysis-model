package edu.hm.hafner.analysis;

/**
 * Creates new {@link IssueDifference issueDifferences} using the builder pattern.
 *
 * <p>Example:</p>
 * <blockquote><pre>
 * IssueDifference issueDifference = new IssueDifferenceBuilder()
 *                    .setCurrentIssues(currentIssues)
 *                    .setReferenceIssues(referenceIssues)
 *                    .setReferenceId(referenceId)
 *                    .build();
 * </pre></blockquote>
 *
 * @author walli545
 */
public class IssueDifferenceBuilder {
    private Report currentIssues;
    private Report referenceIssues;
    private String referenceId;

    /**
     * Sets the issues of the current report.
     *
     * @param currentIssues
     *         the issues of the current report
     *
     * @return this
     */
    public IssueDifferenceBuilder setCurrentIssues(final Report currentIssues) {
        this.currentIssues = currentIssues;
        return this;
    }

    /**
     * Sets the issues of a previous report (reference).
     *
     * @param referenceIssues
     *         the issues of a previous report
     *
     * @return this
     */
    public IssueDifferenceBuilder setReferenceIssues(final Report referenceIssues) {
        this.referenceIssues = referenceIssues;
        return this;
    }

    /**
     * Sets the ID identifying the reference report.
     *
     * @param referenceId
     *         the ID identifying the reference report
     *
     * @return this
     */
    public IssueDifferenceBuilder setReferenceId(final String referenceId) {
        this.referenceId = referenceId;
        return this;
    }

    /**
     * Creates a new {@link IssueDifference} using the specified properties.
     *
     * @return the newly created {@link IssueDifference}
     */
    public IssueDifference build() {
        return new IssueDifference(getCurrentIssues(), getReferenceId(), getReferenceIssues());
    }

    private Report getCurrentIssues() {
        return currentIssues;
    }

    private Report getReferenceIssues() {
        return referenceIssues;
    }

    private String getReferenceId() {
        return referenceId;
    }
}
