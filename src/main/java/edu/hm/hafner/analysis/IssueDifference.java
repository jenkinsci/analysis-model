package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Computes old, new, and fixed issues based on the reports of two consecutive static analysis runs for the same
 * software artifact.
 *
 * @author Ullrich Hafner
 */
public class IssueDifference {
    private final Report newIssues;
    private final Report fixedIssues;
    private final Report outstandingIssues;

    /**
     * Creates a new instance of {@link IssueDifference}.
     *
     * @param currentIssues
     *         the issues of the current report
     * @param referenceId
     *         ID identifying the reference report
     * @param referenceIssues
     *         the issues of a previous report (reference)
     */
    public IssueDifference(final Report currentIssues, final String referenceId, final Report referenceIssues) {
        newIssues = currentIssues.copy();
        fixedIssues = referenceIssues.copy();
        outstandingIssues = new Report();

        List<UUID> removed = matchIssuesByEquals(currentIssues);
        Report secondPass = currentIssues.copy();
        removed.forEach(secondPass::remove);
        matchIssuesByFingerprint(secondPass);

        newIssues.forEach(issue -> issue.setReference(referenceId));
    }

    private List<UUID> matchIssuesByEquals(final Report currentIssues) {
        List<UUID> removedIds = new ArrayList<>();
        for (Issue current : currentIssues) {
            List<Issue> equalIssues = findReferenceByEquals(current);

            if (!equalIssues.isEmpty()) {
                removedIds.add(remove(current, selectIssueWithSameFingerprint(current, equalIssues)));
            }
        }
        return removedIds;
    }

    private void matchIssuesByFingerprint(final Report currentIssues) {
        for (Issue current : currentIssues) {
            findReferenceByFingerprint(current).ifPresent(issue -> remove(current, issue));
        }
    }

    private UUID remove(final Issue current, final Issue oldIssue) {
        UUID id = current.getId();
        Issue issueWithLatestProperties = newIssues.remove(id);
        issueWithLatestProperties.setReference(oldIssue.getReference());
        outstandingIssues.add(issueWithLatestProperties);
        fixedIssues.remove(oldIssue.getId());
        return id;
    }

    private Issue selectIssueWithSameFingerprint(final Issue current, final List<Issue> equalIssues) {
        return equalIssues.stream()
                .filter(issue -> issue.getFingerprint().equals(current.getFingerprint()))
                .findFirst()
                .orElse(equalIssues.get(0));
    }

    private Optional<Issue> findReferenceByFingerprint(final Issue current) {
        for (Issue reference : fixedIssues) {
            if (current.getFingerprint().equals(reference.getFingerprint())) {
                return Optional.of(reference);
            }
        }
        return Optional.empty();
    }

    private List<Issue> findReferenceByEquals(final Issue current) {
        List<Issue> equalIssues = new ArrayList<>();
        for (Issue reference : fixedIssues) {
            if (current.equals(reference)) {
                equalIssues.add(reference);
            }
        }
        return equalIssues;
    }

    /**
     * Returns the outstanding issues. I.e. all issues, that are part of the previous report and that are still part of
     * the current report.
     *
     * @return the outstanding issues
     */
    public Report getOutstandingIssues() {
        return outstandingIssues;
    }

    /**
     * Returns the new issues. I.e. all issues, that are part of the current report but that have not been shown up in
     * the previous report.
     *
     * @return the new issues
     */
    public Report getNewIssues() {
        return newIssues;
    }

    /**
     * Returns the fixed issues. I.e. all issues, that are part of the previous report but that are not present in the
     * current report anymore.
     *
     * @return the fixed issues
     */
    public Report getFixedIssues() {
        return fixedIssues;
    }

    /**
     * Builder for the IssueDifference Class.
     *
     * @author Colin Mikolajczak
     */
    public static class IssueDifferenceBuilder {
        private Report newIssues;
        private Report fixedIssues;
        private String referenceId;

        /**
         * Sets the currentIssues.
         *
         * @param currentIssues
         *         the issues of the currentReport
         *
         * @return this
         */
        public IssueDifferenceBuilder setNewIssues(final Report currentIssues) {
            this.newIssues = currentIssues;
            return this;
        }

        /**
         * Sets the Reference issues.
         *
         * @param referenceIssues
         *         the issues of the reference report
         *
         * @return this
         */
        public IssueDifferenceBuilder setReferenceIssues(final Report referenceIssues) {
            this.fixedIssues = referenceIssues;
            return this;
        }

        /**
         * Sets the reference ID.
         *
         * @param referenceId
         *         ID identifying the the reference report
         *
         * @return this
         */
        public IssueDifferenceBuilder setReferenceId(final String referenceId) {
            this.referenceId = referenceId;
            return this;
        }

        /**
         * Building the defined IssueDifference.
         *
         * @return the created IssueDifference
         * @throws NullPointerException
         *         when one of the essential fields is not set
         */
        public IssueDifference build() {
            return new IssueDifference(Objects.requireNonNull(this.newIssues), Objects.requireNonNull(this.referenceId),
                    Objects.requireNonNull(this.fixedIssues));
        }
    }
}

