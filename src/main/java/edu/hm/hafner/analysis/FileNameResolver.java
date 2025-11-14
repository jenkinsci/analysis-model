package edu.hm.hafner.analysis;

import edu.hm.hafner.util.PathUtil;

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
        Set<String> filesToProcess = report.getFiles()
                .stream()
                .filter(fileName -> isInterestingFileName(fileName, skipFileNamePredicate))
                .collect(Collectors.toSet());

        if (filesToProcess.isEmpty()) {
            report.logInfo(NOTHING_TO_DO);

            return;
        }

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

    private String makeRelative(final String sourceDirectoryPrefix, final String fileName) {
        return PATH_UTIL.getRelativePath(sourceDirectoryPrefix, fileName);
    }

    private boolean isInterestingFileName(final String fileName,
            final Predicate<String> skipFileNamePredicate) {
        return !"-".equals(fileName) && !skipFileNamePredicate.test(fileName);
    }
}
