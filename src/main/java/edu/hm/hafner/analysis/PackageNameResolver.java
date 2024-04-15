package edu.hm.hafner.analysis;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import edu.hm.hafner.util.VisibleForTesting;

import static edu.hm.hafner.analysis.PackageDetectors.*;
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
        List<AbstractPackageDetector> detectors = new ArrayList<>(Arrays.asList(
                new JavaPackageDetector(fileSystem),
                new CSharpNamespaceDetector(fileSystem),
                new KotlinPackageDetector(fileSystem)
        ));
        packageDetectors = new PackageDetectors(detectors);
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

        Map<String, String> packagesOfFiles = filesWithoutPackageName.stream()
                .collect(Collectors.toMap(identity(),
                        fileName -> packageDetectors.detectPackageName(fileName, charset)));

        try (IssueBuilder builder = new IssueBuilder()) {
            report.stream().forEach(issue -> {
                if (!issue.hasPackageName()) {
                    issue.setPackageName(builder.internPackageName(packagesOfFiles.get(issue.getAbsolutePath())));
                }
            });
        }
        report.logInfo("-> resolved package names of %d affected files", filesWithoutPackageName.size());
    }
}
