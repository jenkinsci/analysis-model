package edu.hm.hafner.analysis;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.List;

import com.google.errorprone.annotations.MustBeClosed;

import edu.hm.hafner.util.VisibleForTesting;

/**
 * Provides convenient methods to determine the package or namespace names of a file.
 *
 * @author Ullrich Hafner
 */
class PackageDetectors {
    /** If no package could be assigned this value is used as package name. */
    static final String UNDEFINED_PACKAGE = "-";

    private final List<AbstractPackageDetector> detectors;

    @VisibleForTesting
    PackageDetectors(final List<AbstractPackageDetector> detectors) {
        this.detectors = detectors;
    }

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
    public String detectPackageName(final String fileName, final Charset charset) {
        for (AbstractPackageDetector detector : detectors) {
            if (detector.accepts(fileName)) {
                return detector.detectPackageName(fileName, charset);
            }
        }
        return UNDEFINED_PACKAGE;
    }

    /**
     * Facade for file system operations. May be replaced by stubs in test cases.
     */
    @VisibleForTesting
    static class FileSystem {
        @MustBeClosed
        InputStream openFile(final String fileName) throws IOException, InvalidPathException {
            return Files.newInputStream(Paths.get(fileName));
        }
    }
}
