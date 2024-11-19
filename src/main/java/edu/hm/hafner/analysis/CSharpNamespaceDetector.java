package edu.hm.hafner.analysis;

import java.util.regex.Pattern;

import edu.hm.hafner.analysis.PackageDetectors.FileSystem;
import edu.hm.hafner.util.VisibleForTesting;

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

    @VisibleForTesting
    CSharpNamespaceDetector(final FileSystem fileSystem) {
        super(fileSystem);
    }

    @Override
    public boolean accepts(final String fileName) {
        return fileName.endsWith(".cs");
    }

    @Override
    Pattern getPattern() {
        return NAMESPACE_PATTERN;
    }
}
