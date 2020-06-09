package edu.hm.hafner.analysis;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import edu.hm.hafner.util.VisibleForTesting;

import static edu.hm.hafner.analysis.PackageDetectors.*;

/**
 * Detects the package name of a Java file.
 *
 * @author Ullrich Hafner
 */
class JavaPackageDetector extends AbstractPackageDetector {
    private static final Pattern PACKAGE_PATTERN = Pattern.compile(
            "^\\s*package\\s*([a-z]+[.\\w]*)\\s*;.*");

    JavaPackageDetector() {
        this(new FileSystem());
    }

    @VisibleForTesting
    JavaPackageDetector(final FileSystem fileSystem) {
        super(fileSystem);
    }

    @Override
    public String detectPackageName(final Stream<String> lines) {
        return lines.map(PACKAGE_PATTERN::matcher)
                .filter(Matcher::matches)
                .findFirst()
                .map(matcher -> matcher.group(1))
                .orElse(UNDEFINED_PACKAGE);
    }

    @Override
    public boolean accepts(final String fileName) {
        return fileName.endsWith(".java");
    }
}

