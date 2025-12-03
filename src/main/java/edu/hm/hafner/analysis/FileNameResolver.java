package edu.hm.hafner.analysis;

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import edu.hm.hafner.util.PathUtil;

/**
 * Resolves the affected files of a set of issues in a given source directory. Replaces all file names with the
 * relative file names in this folder. File names that cannot be resolved will be left unchanged.
 *
 * @author Ullrich Hafner
 */
public class FileNameResolver {
    static final String NOTHING_TO_DO = "-> none of the issues requires resolving of paths";
    private static final PathUtil PATH_UTIL = new PathUtil();

    /**
     * Resolves the file names of the affected files of the specified set of issues.
     *
     * @param report
     *         the issues to resolve the paths
     * @param sourceDirectoryPrefix
     *         absolute source path that should be used as parent folder to search for files
     * @param skipFileNamePredicate
     *         skip specific files based on the file name
     */
    public void run(final Report report, final String sourceDirectoryPrefix,
            final Predicate<String> skipFileNamePredicate) {
        run(report, sourceDirectoryPrefix, skipFileNamePredicate, "", "");
    }

    /**
     * Resolves the file names of the affected files of the specified set of issues with optional path remapping.
     * This is useful when the paths in the report are generated in a different environment (e.g., inside a Docker
     * container) and need to be remapped to the actual workspace paths.
     *
     * @param report
     *         the issues to resolve the paths
     * @param sourceDirectoryPrefix
     *         absolute source path that should be used as parent folder to search for files
     * @param skipFileNamePredicate
     *         skip specific files based on the file name
     * @param sourcePathPrefix
     *         the path prefix to be replaced (e.g., path inside docker container). Empty string means no remapping.
     * @param targetPathPrefix
     *         the path prefix to replace with (e.g., path in Jenkins workspace). Empty string means no remapping.
     */
    public void run(final Report report, final String sourceDirectoryPrefix,
            final Predicate<String> skipFileNamePredicate, final String sourcePathPrefix,
            final String targetPathPrefix) {
        Set<String> filesToProcess = report.getFiles()
                .stream()
                .filter(fileName -> isInterestingFileName(fileName, skipFileNamePredicate))
                .collect(Collectors.toSet());

        if (filesToProcess.isEmpty()) {
            report.logInfo(NOTHING_TO_DO);

            return;
        }

        filesToProcess = applyPathMapping(report, sourcePathPrefix, targetPathPrefix, skipFileNamePredicate);

        Map<String, String> pathMapping = filesToProcess.parallelStream()
                .collect(Collectors.toMap(fileName -> fileName,
                        fileName -> makeRelative(sourceDirectoryPrefix, fileName)))
                .entrySet().parallelStream()
                .filter(entry -> PATH_UTIL.exists(entry.getValue(), sourceDirectoryPrefix))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        try (var builder = new IssueBuilder()) {
            report.stream()
                    .filter(issue -> pathMapping.containsKey(issue.getFileName()))
                    .forEach(issue -> issue.setFileName(sourceDirectoryPrefix,
                            builder.internFileName(pathMapping.get(issue.getFileName()))));
        }
        report.logInfo("-> resolved paths in source directory (%d found, %d not found)",
                pathMapping.size(), filesToProcess.size() - pathMapping.size());
    }

    /**
     * Applies path mapping to the issues in the report. This remaps file paths from one prefix to another, which is
     * useful when the report was generated in a different environment (e.g., inside a Docker container).
     *
     * @param report
     *         the issues to remap paths for
     * @param sourcePathPrefix
     *         the path prefix to be replaced (e.g., path inside docker container). Empty string means no remapping.
     * @param targetPathPrefix
     *         the path prefix to replace with (e.g., path in Jenkins workspace). Empty string means no remapping.
     * @param skipFileNamePredicate
     *         skip specific files based on the file name
     *
     * @return the updated set of files to process after remapping
     */
    private Set<String> applyPathMapping(final Report report, final String sourcePathPrefix,
            final String targetPathPrefix, final Predicate<String> skipFileNamePredicate) {
        boolean shouldRemap = !sourcePathPrefix.isEmpty() && !targetPathPrefix.isEmpty();
        if (shouldRemap) {
            report.logInfo("-> remapping paths from '%s' to '%s'", sourcePathPrefix, targetPathPrefix);
            try (var builder = new IssueBuilder()) {
                report.stream()
                        .filter(issue -> issue.getFileName().startsWith(sourcePathPrefix))
                        .forEach(issue -> {
                            String originalPath = issue.getFileName();
                            String remappedPath = targetPathPrefix + originalPath.substring(sourcePathPrefix.length());
                            issue.setFileName(issue.getPath(), builder.internFileName(remappedPath));
                        });
            }
        }

        return report.getFiles()
                .stream()
                .filter(fileName -> isInterestingFileName(fileName, skipFileNamePredicate))
                .collect(Collectors.toSet());
    }

    private String makeRelative(final String sourceDirectoryPrefix, final String fileName) {
        return PATH_UTIL.getRelativePath(sourceDirectoryPrefix, fileName);
    }

    private boolean isInterestingFileName(final String fileName,
            final Predicate<String> skipFileNamePredicate) {
        return !"-".equals(fileName) && !skipFileNamePredicate.test(fileName);
    }
}
