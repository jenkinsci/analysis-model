package edu.hm.hafner.analysis;

import java.util.regex.Pattern;

import edu.hm.hafner.analysis.PackageDetectors.FileSystem;
import edu.hm.hafner.util.VisibleForTesting;

/**
 * Detects the package name of a Kotlin file.
 *
 * @author Bastian Kersting
 */
class KotlinPackageDetector extends AbstractPackageDetector {
    private static final Pattern PACKAGE_PATTERN = Pattern.compile(
            "^\\s*package\\s*([a-z]+[.\\w]*)\\s*.*");

    KotlinPackageDetector() {
        this(new FileSystem());
    }

    @VisibleForTesting
    KotlinPackageDetector(final FileSystem fileSystem) {
        super(fileSystem);
    }

    @Override
    Pattern getPattern() {
        return PACKAGE_PATTERN;
    }

    @Override
    boolean accepts(final String fileName) {
        return fileName.endsWith(".kt");
    }
}
