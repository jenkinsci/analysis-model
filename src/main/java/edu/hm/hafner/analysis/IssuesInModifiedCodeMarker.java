package edu.hm.hafner.analysis;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import edu.hm.hafner.util.PathUtil;

/**
 * Computes old, new, and fixed issues based on the reports of two consecutive static analysis runs for the same
 * software artifact.
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings("PMD.DataClass")
public class IssuesInModifiedCodeMarker {
    private static final PathUtil PATH_UTIL = new PathUtil();

    /**
     * Finds and marks all issues that are part the changes in a source control diff.
     *
     * @param report
     *         the report with the issues to scan
     * @param modifiedLinesInFilesMapping
     *         a mapping modified lines within files
     */
    public void markIssuesInModifiedCode(final Report report, final Map<String, Set<Integer>> modifiedLinesInFilesMapping) {
        for (Entry<String, Set<Integer>> include : modifiedLinesInFilesMapping.entrySet()) {
            report.filter(issue -> affectsChangedLineInFile(issue, include.getKey(), include.getValue()))
                    .stream()
                    .forEach(Issue::markAsPartOfModifiedCode);
        }
    }

    private boolean affectsChangedLineInFile(final Issue issue, final String fileName, final Set<Integer> lines) {
        var normalizedPath = PATH_UTIL.getRelativePath(fileName);

        if (!issue.getFileName().endsWith(normalizedPath)) { // check only the suffix
            return false;
        }
        return lines.stream().anyMatch(issue::affectsLine);
    }
}
