package edu.hm.hafner.analysis;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.collections.impl.factory.Lists;

import edu.hm.hafner.util.PathUtil;
import edu.hm.hafner.util.TreeStringBuilder;
import edu.hm.hafner.util.VisibleForTesting;

/**
 * Resolves absolute paths of the affected files of a set of issues.
 *
 * @author Ullrich Hafner
 */
public class AbsolutePathGenerator {
    static final String NOTHING_TO_DO = "-> none of the issues requires resolving of absolute path";

    /**
     * Resolves absolute paths of the affected files of the specified set of issues.
     *
     * @param report
     *         the issues to resolve the paths
     * @param sourceDirectories
     *         collection of source paths to search for the affected files
     * @param skipFileNamePredicate
     *         skip specific files based on the file name
     */
    public void run(final Report report, final Collection<String> sourceDirectories,
            final Predicate<String> skipFileNamePredicate) {
        Set<String> filesToProcess = report.getFiles()
                .stream()
                .filter(fileName -> isInterestingFileName(fileName, skipFileNamePredicate))
                .collect(Collectors.toSet());

        if (filesToProcess.isEmpty()) {
            report.logInfo(NOTHING_TO_DO);
            resolvePathNames(report);
            return;
        }

        FilteredLog log = new FilteredLog(report, "Can't resolve absolute paths for some files:");

        IssueBuilder builder = new IssueBuilder();
        Map<String, String> pathMapping = resolveAbsoluteNames(filesToProcess, sourceDirectories, log);
        report.stream()
                .filter(issue -> pathMapping.containsKey(issue.getFileName()))
                .forEach(issue -> issue.setFileName(builder.internFileName(pathMapping.get(issue.getFileName()))));

        log.logSummary();
    }

    @VisibleForTesting
    void run(final Report report, final Path workspace, final Path additionalDirectory) {
        run(report, Lists.fixedSize.of(workspace.toString(), additionalDirectory.toString()), f -> false);
    }

    /**
     * Resolves all filenames: since we might compare the absolute filename later on it makes sense to store the correct
     * absolute path.
     *
     * @param report
     *         the report
     */
    private void resolvePathNames(final Report report) {
        PathUtil pathUtil = new PathUtil();
        TreeStringBuilder builder = new TreeStringBuilder();
        report.stream()
                .forEach(issue -> issue.setFileName(builder.intern(pathUtil.getAbsolutePath(issue.getFileName()))));
    }

    private boolean isInterestingFileName(final String fileName,
            final Predicate<String> skipFileNamePredicate) {
        return !"-".equals(fileName) && !skipFileNamePredicate.test(fileName);
    }

    private Map<String, String> resolveAbsoluteNames(final Set<String> affectedFiles,
            final Collection<String> sourceDirectories, final FilteredLog log) {
        Map<String, String> pathMapping = new HashMap<>();
        int errors = 0;
        int unchanged = 0;
        int changed = 0;

        for (String fileName : affectedFiles) {
            Optional<String> absolutePath = resolveAbsolutePath(fileName, sourceDirectories);
            if (absolutePath.isPresent()) {
                String resolved = absolutePath.get();
                pathMapping.put(fileName, resolved);
                if (fileName.equals(resolved)) {
                    unchanged++;
                }
                else {
                    changed++;
                }
            }
            else {
                log.logError("- %s", fileName);
                errors++;
            }
        }
        log.logInfo("-> %d resolved, %d unresolved, %d already resolved", changed, errors, unchanged);

        return pathMapping;
    }

    private Optional<String> resolveAbsolutePath(final String fileName, final Collection<String> sourceDirectories) {
        PathUtil pathUtil = new PathUtil();
        if (pathUtil.isAbsolute(fileName)) {
            return resolveAbsolutePath(fileName);
        }
        return resolveRelativePath(fileName, sourceDirectories);
    }

    private Optional<String> resolveRelativePath(final String fileName, final Collection<String> sourceDirectories) {
        return sourceDirectories.stream()
                .map(path -> resolveAbsolutePath(path, fileName))
                .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
                .findFirst();
    }

    private Optional<String> resolveAbsolutePath(final String first, final String... other) {
        try {
            return Optional.of(new PathUtil().toString(Paths.get(first, other)));
        }
        catch (IOException | InvalidPathException ignored) {
            return Optional.empty();
        }
    }
}
