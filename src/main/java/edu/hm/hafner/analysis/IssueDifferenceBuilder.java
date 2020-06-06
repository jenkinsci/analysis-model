package edu.hm.hafner.analysis;

import java.util.Objects;

/**
 * Creates a new IssueDifference with builder pattern.
 * @author Fabian Diener
 */

public class IssueDifferenceBuilder {

    /**
     * The current issue
     */
    private Report currentIssues;
    /**
     * The referenceID of the issue
     */
    private String referenceId;
    /**
     * the reference issue
     */
    private Report referenceIssues;

    /**
     * Sets the current Issue
     * @param currentIssue
     * @return this
     */
    public IssueDifferenceBuilder setCurrentIssues(final Report currentIssue) {
        this.currentIssues = Objects.requireNonNull(currentIssue);
        return this;
    }
    /**
     * Sets the referenceID
     * @param referenceID
     * @return this
     */
    public IssueDifferenceBuilder setReferenceID(final String referenceID) {
        this.referenceId = Objects.requireNonNull(referenceID);
        return this;
    }
    /**
     * Sets the reference issue
     * @param referenceIssues
     * @return this
     */

    public IssueDifferenceBuilder setReferenceIssues(final Report referenceIssues) {
        this.referenceIssues = Objects.requireNonNull(referenceIssues);
        return this;
    }

    /**
     * Builds the new IssueDifference.
     * @return new IssueDiffernce
     */

    public IssueDifference build() {
        if (getCurrentIssues() == null | getReferenceIssues() == null | getReferenceId() == null) {
            throw new IllegalStateException("All fields must have a value");
        }
        return new IssueDifference(getCurrentIssues(), getReferenceId(), getReferenceIssues());
    }

    public Report getCurrentIssues() {
        return currentIssues;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public Report getReferenceIssues() {
        return referenceIssues;
    }
}
