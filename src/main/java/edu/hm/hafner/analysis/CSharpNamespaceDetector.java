package edu.hm.hafner.analysis;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static edu.hm.hafner.analysis.PackageDetectors.*;

/**
 * Detects the namespace of a C# workspace file.
 *
 * @author Ullrich Hafner
 */
class CSharpNamespaceDetector extends AbstractPackageDetector {
    private static final Pattern NAMESPACE_PATTERN = Pattern.compile("^\\s*namespace\\s+([^{]*)\\s*\\{?\\s*$");

    CSharpNamespaceDetector() {
        this(new FileSystem());
    }

    CSharpNamespaceDetector(final FileSystem fileSystem) {
        super(fileSystem);
    }

    @Override
    public boolean accepts(final String fileName) {
        return fileName.endsWith(".cs");
    }

    @Override
    public String detectPackageName(final Stream<String> lines) {
        return lines.map(NAMESPACE_PATTERN::matcher)
                .filter(Matcher::matches)
                .findFirst()
                .map(matcher -> matcher.group(1))
                .orElse(UNDEFINED_PACKAGE).trim();
    }
}

