package edu.hm.hafner.analysis;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
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
    private static final boolean IS_WINDOWS = File.pathSeparatorChar == ';';

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
                .map(entry -> {
                    Optional<String> actualPath = findActualFilePath(entry.getValue(), sourceDirectoryPrefix);
                    return actualPath.map(path -> Map.entry(entry.getKey(), path));
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
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

    /**
     * Finds the actual file path with correct capitalization on Windows.
     * On Windows, file systems are case-insensitive but case-preserving, so we need to find the actual file name as it exists on disk.
     *
     * @param relativeFileName
     *         the relative file name (possibly with wrong capitalization)
     * @param sourceDirectoryPrefix
     *         the source directory prefix
     *
     * @return the actual relative file name with correct capitalization, or empty if not found
     */
    private Optional<String> findActualFilePath(final String relativeFileName, final String sourceDirectoryPrefix) {
        if (!IS_WINDOWS) {
            return PATH_UTIL.exists(relativeFileName, sourceDirectoryPrefix)
                    ? Optional.of(relativeFileName)
                    : Optional.empty();
        }

        try {
            var basePath = Path.of(sourceDirectoryPrefix);
            var targetPath = basePath.resolve(relativeFileName);

            if (!Files.exists(targetPath)) {
                return Optional.empty();
            }

            var realPath = targetPath.toRealPath();

            var relativePath = basePath.relativize(realPath);
            String actualRelativeFileName = relativePath.toString().replace('\\', '/');

            return Optional.of(actualRelativeFileName);
        }
        catch (IOException | InvalidPathException exception) {
            return Optional.empty();
        }
    }
}
