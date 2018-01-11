package edu.hm.hafner.analysis;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Detects the package name of a Java file.
 *
 * @author Ullrich Hafner
 */
public class JavaPackageDetector extends AbstractPackageDetector {
    private static final Pattern PACKAGE_PATTERN = Pattern.compile(
            "^\\s*package\\s*([a-z]+(\\.[a-zA-Z_][a-zA-Z0-9_]*)*)\\s*;.*");

    @Override
    public String detectPackageName(final Stream<String> lines) {
        return lines.map(PACKAGE_PATTERN::matcher)
                .filter(Matcher::matches)
                .findFirst()
                .map(matcher -> matcher.group(1))
                .orElse(UNKNOWN_PACKAGE);
    }

    @Override
    public boolean accepts(final String fileName) {
        return fileName.endsWith(".java");
    }
}

