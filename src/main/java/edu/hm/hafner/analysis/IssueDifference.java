package edu.hm.hafner.analysis;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;

/**
 * Computes old, new, and fixed issues based on the reports of two consecutive static analysis runs for the same
 * software artifact.
 *
 * @author Ullrich Hafner
 */
public class IssueDifference {
    private static final List<Issue> EMPTY = Collections.emptyList();

    private final Report newIssues;
    private final Report newIssuesInChangedCode;
    private final Report fixedIssues;
    private final Report outstandingIssues;
    private final Map<Integer, List<Issue>> referencesByHash;
    private final Map<String, List<Issue>> referencesByFingerprint;

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
    public IssueDifference(final Report currentIssues, final String referenceId,
            final Report referenceIssues) {
        this(currentIssues, referenceId, referenceIssues, Map.of());
    }

    /**
     * Creates a new instance of {@link IssueDifference}.
     *
     * @param currentIssues
     *         the issues of the current report
     * @param referenceId
     *         ID identifying the reference report
     * @param referenceIssues
     *         the issues of a previous report (reference)
     * @param includes
     *         A mapping of files to changed lines. Using this mapping, we can identify which new issues are part
     *         of the changes and which issues are indirectly caused by the changes.
     */
    public IssueDifference(final Report currentIssues, final String referenceId,
            final Report referenceIssues, final Map<String, Integer> includes) {
        newIssues = currentIssues.copy();
        fixedIssues = referenceIssues.copy();
        outstandingIssues = referenceIssues.copyEmptyInstance();
        newIssuesInChangedCode = currentIssues.copyEmptyInstance();

        referencesByHash = new HashMap<>();
        referencesByFingerprint = new HashMap<>();

        for (Issue issue : referenceIssues) {
            addIssueToMap(referencesByHash, issue.hashCode(), issue);
            addIssueToMap(referencesByFingerprint, issue.getFingerprint(), issue);
        }

        List<UUID> removed = matchIssuesByEquals(currentIssues);
        var secondPass = currentIssues.copy();
        removed.forEach(secondPass::remove);
        matchIssuesByFingerprint(secondPass);

        newIssues.forEach(issue -> issue.setReference(referenceId));
        if (!includes.isEmpty()) {
            findIssuesInChangedCode(includes);
        }
    }

    private void findIssuesInChangedCode(final Map<String, Integer> includes) {
        for (Entry<String, Integer> include : includes.entrySet()) {
            newIssues.filter(issue -> isInFileAtPosition(issue, include.getKey(), include.getValue()))
                    .stream()
                    .map(Issue::getId)
                    .map(newIssues::remove)
                    .forEach(newIssuesInChangedCode::add);
        }
    }

    private boolean isInFileAtPosition(final Issue issue, final String fileName, final int line) {
        return issue.getFileName().endsWith(fileName) && issue.affectsLine(line);
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

    private <K> void addIssueToMap(final Map<K, List<Issue>> map, final K key, final Issue issue) {
        map.computeIfAbsent(key, k -> new ArrayList<>()).add(issue);
    }

    @SuppressWarnings("NullAway")
    private <K> void removeIssueFromMap(final Map<K, List<Issue>> map, final K key, final Issue issue) {
        List<Issue> issues = map.get(key);
        issues.remove(issue);
        if (issues.isEmpty()) {
            map.remove(key);
        }
    }

    private UUID remove(final Issue current, final Issue oldIssue) {
        var id = current.getId();
        var issueWithLatestProperties = newIssues.remove(id);
        issueWithLatestProperties.setReference(oldIssue.getReference());
        outstandingIssues.add(issueWithLatestProperties);
        fixedIssues.remove(oldIssue.getId());
        removeIssueFromMap(referencesByFingerprint, oldIssue.getFingerprint(), oldIssue);
        removeIssueFromMap(referencesByHash, oldIssue.hashCode(), oldIssue);
        return id;
    }

    private Issue selectIssueWithSameFingerprint(final Issue current, final List<Issue> equalIssues) {
        return equalIssues.stream()
                .filter(issue -> issue.getFingerprint().equals(current.getFingerprint()))
                .findFirst()
                .orElse(equalIssues.get(0));
    }

    private Optional<Issue> findReferenceByFingerprint(final Issue current) {
        return referencesByFingerprint.getOrDefault(current.getFingerprint(), EMPTY).stream().findAny();
    }

    private List<Issue> findReferenceByEquals(final Issue current) {
        List<Issue> equalIssues = new ArrayList<>();

        for (Issue reference : referencesByHash.getOrDefault(current.hashCode(), EMPTY)) {
            if (current.equals(reference)) {
                equalIssues.add(reference);
            }
        }

        return equalIssues;
    }

    /**
     * Returns the outstanding issues. I.e., all issues that are part of the previous report and that are still part of
     * the current report.
     *
     * @return the outstanding issues
     */
    @SuppressFBWarnings("EI")
    public Report getOutstandingIssues() {
        return outstandingIssues;
    }

    /**
     * Returns the new issues. I.e., all issues that are part of the current report but that have not been shown up in
     * the previous report. If the difference is computed for a specific set of changed files, then this set contains
     * only the new issues that are not part of the changes. These issues are indirectly caused by the changes.
     *
     * @return the new issues
     */
    @SuppressFBWarnings("EI")
    public Report getNewIssues() {
        return newIssues;
    }

    /**
     * Returns the fixed issues. I.e., all issues that are part of the previous report but that are not present in the
     * current report anymore.
     *
     * @return the fixed issues
     */
    @SuppressFBWarnings("EI")
    public Report getFixedIssues() {
        return fixedIssues;
    }
}
