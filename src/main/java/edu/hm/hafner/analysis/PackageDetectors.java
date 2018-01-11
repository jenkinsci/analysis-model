package edu.hm.hafner.analysis;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

/**
 * Provides convenient methods to determine the package or namespace names of a file.
 *
 * @author Ullrich Hafner
 */
public final class PackageDetectors {
    /** If no package could be assigned this value is used as package name. */
    static final String UNDEFINED_PACKAGE = "-";

    private static final List<AbstractPackageDetector> DETECTORS = Arrays.asList(
            new JavaPackageDetector(), new CSharpNamespaceDetector());

    /**
     * Detects the package name of the specified file based on several detector strategies.
     *
     * @param fileName
     *         the filename of the file to scan
     * @param charset
     *         the charset to use when reading the source files
     *
     * @return the package name or the String {@link #UNDEFINED_PACKAGE} if no package could be detected
     */
    public static String detectPackageName(final String fileName, final Charset charset) {
        for (AbstractPackageDetector detector : DETECTORS) {
            if (detector.accepts(fileName)) {
                return detector.detectPackageName(fileName, charset);
            }
        }
        return UNDEFINED_PACKAGE;
    }

    private PackageDetectors() {
        // prevents instantiation
    }
}

