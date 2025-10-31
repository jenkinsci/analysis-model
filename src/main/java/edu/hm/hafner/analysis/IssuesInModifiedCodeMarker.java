package edu.hm.hafner.analysis;

import edu.hm.hafner.util.PathUtil;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Computes old, new, and fixed issues based on the reports of two consecutive static analysis runs for the same
 * software artifact.
 *
 * @author Ullrich Hafner
 */
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

    /**
     * Finds and marks all issues that are in modified files, regardless of whether they affect specific lines.
     *
     * @param report
     *         the report with the issues to scan
     * @param modifiedFiles
     *         a set of modified file names
     */
    public void markIssuesInModifiedFiles(final Report report, final Set<String> modifiedFiles) {
        for (String fileName : modifiedFiles) {
            report.filter(issue -> affectsModifiedFile(issue, fileName))
                    .stream()
                    .forEach(Issue::markAsPartOfModifiedCode);
        }
    }

    private boolean affectsChangedLineInFile(final Issue issue, final String fileName, final Set<Integer> lines) {
        var normalizedPath = PATH_UTIL.getRelativePath(fileName);

        return issue.getFileName().endsWith(normalizedPath) && lines.stream().anyMatch(issue::affectsLine);
    }

    private boolean affectsModifiedFile(final Issue issue, final String fileName) {
        var normalizedPath = PATH_UTIL.getRelativePath(fileName);

        return issue.getFileName().endsWith(normalizedPath);
    }
}
