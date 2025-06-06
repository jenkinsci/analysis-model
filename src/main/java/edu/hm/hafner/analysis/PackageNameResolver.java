package edu.hm.hafner.analysis;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import edu.hm.hafner.util.PackageDetectorFactory;
import edu.hm.hafner.util.PackageDetectorRunner;
import edu.hm.hafner.util.VisibleForTesting;

/**
 * Resolves packages or namespace names for a set of issues.
 *
 * @author Ullrich Hafner
 */
public class PackageNameResolver {
    private final PackageDetectorRunner runner;

    /**
     * Creates a new {@link PackageNameResolver}.
     */
    public PackageNameResolver() {
        this(PackageDetectorFactory.createPackageDetectors());
    }

    @VisibleForTesting
    PackageNameResolver(final PackageDetectorRunner runner) {
        this.runner = runner;
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
                .map(Issue::getAbsolutePath)
                .collect(Collectors.toSet());

        if (filesWithoutPackageName.isEmpty()) {
            report.logInfo("-> all affected files already have a valid package name");

            return;
        }

        Map<String, String> packagesOfFiles = new HashMap<>();
        filesWithoutPackageName.stream()
                .map(f -> extractPackageName(f, charset))
                .flatMap(Optional::stream)
                .forEach(e -> packagesOfFiles.put(e.getKey(), e.getValue()));

        try (var builder = new IssueBuilder()) {
            report.stream().forEach(issue -> {
                if (!issue.hasPackageName()) {
                    issue.setPackageName(builder.internPackageName(packagesOfFiles.get(issue.getAbsolutePath())));
                }
            });
        }
        report.logInfo("-> resolved package names of %d affected files", filesWithoutPackageName.size());
    }

    private Optional<Map.Entry<String, String>> extractPackageName(final String fileName, final Charset charset) {
        return runner.detectPackageName(fileName, charset).map(r -> Map.entry(fileName, r));
    }
}
