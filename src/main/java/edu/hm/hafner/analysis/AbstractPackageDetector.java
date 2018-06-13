package edu.hm.hafner.analysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.InvalidPathException;
import java.util.stream.Stream;

import org.apache.commons.io.input.BOMInputStream;

import static edu.hm.hafner.analysis.PackageDetectors.*;
import edu.hm.hafner.util.VisibleForTesting;

/**
 * Base class for package detectors.
 *
 * @author Ullrich Hafner
 */
public abstract class AbstractPackageDetector {
    private final FileSystem fileSystem;

    /**
     * Creates a new instance of {@link AbstractPackageDetector}.
     *
     * @param fileSystem
     *         file system facade
     */
    protected AbstractPackageDetector(final FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    /**
     * Detects the package or namespace name of the specified file.
     *
     * @param fileName
     *         the file name of the file to scan
     * @param charset
     *         the charset to use when reading the source files
     *
     * @return the detected package or namespace name
     */
    public String detectPackageName(final String fileName, final Charset charset) {
        if (accepts(fileName)) {
            try (InputStream stream = fileSystem.openFile(fileName)) {
                return detectPackageName(stream, charset);
            }
            catch (IOException | InvalidPathException ignore) {
                // ignore IO errors
            }
        }
        return UNDEFINED_PACKAGE;
    }

    @VisibleForTesting
    String detectPackageName(final InputStream stream, final Charset charset) throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(new BOMInputStream(stream), charset))) {
            return detectPackageName(buffer.lines());
        }
    }

    /**
     * Returns whether this classifier accepts the specified file for processing.
     *
     * @param fileName
     *         the file name
     *
     * @return {@code true} if the classifier accepts the specified file for processing.
     */
    abstract boolean accepts(String fileName);

    /**
     * Detects the package or namespace name of the specified input stream. The stream will be closed automatically by
     * the caller of this method.
     *
     * @param lines
     *         the content of the file to scan
     *
     * @return the detected package or namespace name
     */
    abstract String detectPackageName(Stream<String> lines);
}
