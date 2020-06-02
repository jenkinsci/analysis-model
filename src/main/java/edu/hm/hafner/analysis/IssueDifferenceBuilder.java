package edu.hm.hafner.analysis;

import java.util.Objects;
import java.util.UUID;

/**
 * Creates new {@link IssueDifference issueDifferences} using the builder pattern.
 *
 * <p>Example:</p>
 * <blockquote><pre>
 * IssueDifference issueDifference = new IssueDifferenceBuilder()
 *                    .setCurrentIssues(currentIssues)
 *                    .setReferenceId(referenceId)
 *                    .setReferenceIssues(referenceIssues)
 *                    .build();
 * </pre></blockquote>
 *
 * @author mbauerness
 */
public class IssueDifferenceBuilder {

    private Report currentIssues;
    private String referenceId;
    private Report referenceIssues;

    /**
     * Sets the issues of the current report.
     *
     * @param currentIssues
     *          the current issues
     *
     * @throws NullPointerException
     *          if currentIssues is {@code null}
     *
     * @return this
     */
    public IssueDifferenceBuilder setCurrentIssues(final Report currentIssues) {
        Objects.requireNonNull(currentIssues);

        this.currentIssues = currentIssues;
        return this;
    }

    /**
     * Sets the ID identifying the reference report.
     *
     * @param referenceId
     *          the reference id
     *
     * @throws NullPointerException
     *          if referenceId is {@code null}
     * @return this
     */
    public IssueDifferenceBuilder setReferenceId(final String referenceId) {
        Objects.requireNonNull(referenceId);

        this.referenceId = referenceId;
        return this;
    }

    /**
     * Sets the issues of a previous report (reference).
     *
     * @param referenceIssues
     *          the reference issues
     *
     * @throws NullPointerException
     *          if referenceIssues is {@code null}
     * @return this
     */
    public IssueDifferenceBuilder setReferenceIssues(final Report referenceIssues) {
        Objects.requireNonNull(referenceIssues);

        this.referenceIssues = referenceIssues;
        return this;
    }

    /**
     * Creates a new {@link IssueDifference} based on the specified properties.
     *
     * @throws IllegalStateException
     *          if one value is {@code null}
     *
     * @return the created {@link IssueDifference}
     */
    public IssueDifference build() {
        if (getCurrentIssues() == null || getReferenceId() == null || getReferenceIssues() == null) {
            throw new IllegalStateException("All fields must be set!");
        }

        return new IssueDifference(getCurrentIssues(), getReferenceId(), getReferenceIssues());
    }

    private Report getCurrentIssues() {
        return currentIssues;
    }

    private String getReferenceId() {
        return referenceId;
    }

    private Report getReferenceIssues() {
        return referenceIssues;
    }
}
