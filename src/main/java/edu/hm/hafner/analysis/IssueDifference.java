package edu.hm.hafner.analysis;

import java.util.Optional;

/**
 * Computes old, new, and fixed issues based on the reports of two consecutive static analysis runs for the same
 * software artifact.
 *
 * @author Ulli Hafner
 */
public class IssueDifference {
    private final Issues newIssues;
    private final Issues fixedIssues;

    /**
     * Creates a new instance of {@link IssueDifference}.
     *
     * @param currentIssues   the issues of the current report
     * @param referenceIssues the issues of a previous report (reference)
     */
    public IssueDifference(final Issues currentIssues, final Issues referenceIssues) {
        newIssues = currentIssues.copy();
        fixedIssues = referenceIssues.copy();

        Issues oldIssues = new Issues(); // TODO: do we need old issues?

        for (Issue current : currentIssues) {
            Optional<Issue> referenceToRemove = findReferenceByEquals(current);

            if (!referenceToRemove.isPresent()) {
                referenceToRemove = findReferenceByFingerprint(current);
            }

            if (referenceToRemove.isPresent()) {
                oldIssues.add(current);
                newIssues.remove(current.getId());
                fixedIssues.remove(referenceToRemove.get().getId());

                // TODO: current.setBuild(referenceToRemove.getBuild());
            }
        }
    }

    private Optional<Issue> findReferenceByFingerprint(final Issue current) {
        for (Issue reference : fixedIssues) {
            if (current.getFingerprint().equals(reference.getFingerprint())) {
                return Optional.of(reference);
            }
        }
        return Optional.empty();
    }

    private Optional<Issue> findReferenceByEquals(final Issue current) {
        for (Issue reference : fixedIssues) {
            if (current.equals(reference)) {
                return Optional.of(reference);
            }
        }
        return Optional.empty();
    }

    /**
     * Returns the new issues. I.e. all issues, that are part of the current report but have not been shown up in the
     * previous report.
     *
     * @return the new issues
     */
    public Issues getNewIssues() {
        return newIssues;
    }

    /**
     * Returns the fixed issues. I.e. all issues, that are part of the previous report but are not present in the
     * current report anymore.
     *
     * @return the new issues
     */
    public Issues getFixedIssues() {
        return fixedIssues;
    }
}

