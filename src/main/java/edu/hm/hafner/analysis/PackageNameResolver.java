package edu.hm.hafner.analysis;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static edu.hm.hafner.analysis.PackageDetectors.*;
import edu.hm.hafner.util.VisibleForTesting;
import static java.util.function.Function.*;

/**
 * Resolves packages or namespace names for a set of issues.
 *
 * @author Ullrich Hafner
 */
public class PackageNameResolver {
    private final PackageDetectors packageDetectors;

    public PackageNameResolver() {
        this(new FileSystem());
    }

    @VisibleForTesting
    PackageNameResolver(final FileSystem fileSystem) {
        packageDetectors = new PackageDetectors(fileSystem);
    }

    /**
     * Resolves packages or namespace names for the specified set of issues.
     *
     * @param issues
     *         the issues to analyze
     * @param builder
     *         the issue builder to create the new issues with
     * @param charset
     *         the character set to use when reading the source files
     */
    public void run(final Issues issues,
            final IssueBuilder builder, final Charset charset) {
        Set<String> filesWithoutPackageName = issues.stream()
                .filter(issue -> !issue.hasPackageName())
                .map(issue -> issue.getFileName())
                .collect(Collectors.toSet());
        Map<String, String> packagesOfFiles = filesWithoutPackageName.stream()
                .collect(Collectors.toMap(identity(), findPackage(charset)));

        issues.stream().forEach(issue -> {
            if (!issue.hasPackageName()) {
                issue.setPackageName(packagesOfFiles.get(issue.getFileName()));
            }
        });
        issues.logInfo("Resolved package names of %d affected files", filesWithoutPackageName.size());
    }

    private Function<String, String> findPackage(final Charset charset) {
        return fileName -> packageDetectors.detectPackageName(fileName, charset);
    }
}
