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

    /**
     * Creates a new {@link PackageNameResolver}.
     */
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
     * @param report
     *         the issues to analyze
     * @param charset
     *         the character set to use when reading the source files
     */
    public void run(final Report report, final Charset charset) {
        Set<String> filesWithoutPackageName = report.stream()
                .filter(issue -> !issue.hasPackageName())
                .map(Issue::getFileName)
                .collect(Collectors.toSet());
        
        if (filesWithoutPackageName.isEmpty()) {
            report.logInfo("-> all affected files already have a valid package name");
        
            return;
        }
        
        Map<String, String> packagesOfFiles = filesWithoutPackageName.stream()
                .collect(Collectors.toMap(identity(), findPackage(charset)));

        report.stream().forEach(issue -> {
            if (!issue.hasPackageName()) {
                issue.setPackageName(packagesOfFiles.get(issue.getFileName()));
            }
        });
        report.logInfo("-> resolved package names of %d affected files", filesWithoutPackageName.size());
    }

    private Function<String, String> findPackage(final Charset charset) {
        return fileName -> packageDetectors.detectPackageName(fileName, charset);
    }
}
