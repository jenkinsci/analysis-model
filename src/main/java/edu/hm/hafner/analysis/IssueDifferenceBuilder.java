package edu.hm.hafner.analysis;

/**
 * Creates a new {@link IssueDifference} using the Builder Pattern.
 *
 * @author Bastian Kersting
 */
public class IssueDifferenceBuilder {

    private Report newIssues = null;
    private Report fixedIssue = null;
    private String referenceId = null;

    /**
     * Creates a new IssueDifferenceBuilder.
     */
    public IssueDifferenceBuilder() { }

    /**
     * Sets the current Issue.
     * @param currentIssues the issues of the current report
     * @return this
     */
    public IssueDifferenceBuilder setCurrentIssue(final Report currentIssues) {
        this.newIssues = currentIssues.copy();
        return this;
    }

    /**
     * Sets the references Issues.
     * @param referenceIssues the issues of a previous report (reference)
     * @return this
     */
    public IssueDifferenceBuilder setReferenceIssues(final Report referenceIssues) {
        this.fixedIssue = referenceIssues.copy();
        return this;
    }

    /**
     * Sets the reference ID.
     * @param id ID identifying the reference report
     * @return this
     */
    public IssueDifferenceBuilder setReferenceId(final String id) {
        this.referenceId = id;
        return this;
    }

    /**
     * Creates a new {@link IssueDifference} based on the specified properties.
     *
     * @return the created issueDifference
     */
    public IssueDifference build() {
        if (newIssues == null || referenceId == null || fixedIssue == null) {
            throw new IllegalStateException("Can't build empty IssueDifference");
        }
        return new IssueDifference(newIssues, referenceId, fixedIssue);
    }

    public Report getNewIssues() {
        return newIssues;
    }

    public Report getFixedIssue() {
        return fixedIssue;
    }

    public String getReferenceId() {
        return referenceId;
    }
}
