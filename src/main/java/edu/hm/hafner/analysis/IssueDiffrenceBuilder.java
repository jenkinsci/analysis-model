package edu.hm.hafner.analysis;

import java.util.Objects;

/**
 * A Builder for an IssueDifference Object.
 *
 * @author Thorsten Schartel
 */
public class IssueDiffrenceBuilder {
    /**
     * Current Issues.
     */
    private Report currentIssues;
    /**
     * Reference Issues.
     */
    private Report referenceIssues;
    /**
     * ReferenceId as String.
     */
    private String referenceId;

    /**
     * Sets the Current Issue.
     *
     * @param currentIssues
     *         current Issues.
     * @return this Builder.
     */
    public IssueDiffrenceBuilder setCurrentIssues(Report currentIssues) {
        this.currentIssues = currentIssues;
        return this;
    }

    /**
     * Sets the Reference Issue.
     *
     * @param referenceIssues
     *         referenced Issues.
     * @return this Builder.
     */
    public IssueDiffrenceBuilder setReferenceIssues(final Report referenceIssues) {
        this.referenceIssues = referenceIssues;
        return this;
    }

    /**
     * Sets the Reference ID.
     *
     * @param referenceId
     *         the reference ID.
     * @return this Builder.
     */
    public IssueDiffrenceBuilder setReferenceId(final String referenceId) {
        this.referenceId = referenceId;
        return this;
    }

    /**
     * Builder for new IssueDifference.
     * @return IssueDifference Object.
     */
    public IssueDifference build() {
        return new IssueDifference(currentIssues, referenceId, referenceIssues);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IssueDiffrenceBuilder that = (IssueDiffrenceBuilder) o;
        return Objects.equals(currentIssues, that.currentIssues) &&
                Objects.equals(referenceIssues, that.referenceIssues) &&
                Objects.equals(referenceId, that.referenceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentIssues, referenceIssues, referenceId);
    }
}
