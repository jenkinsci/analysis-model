package edu.hm.hafner.analysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.apache.commons.io.input.BOMInputStream;

import edu.hm.hafner.util.VisibleForTesting;

/**
 * Base class for package detectors.
 *
 * @author Ullrich Hafner
 */
// FIXME: Add method that uses the specified encoding when reading the files
public abstract class AbstractPackageDetector {
    /** Identifies an unknown package. */
    protected static final String UNKNOWN_PACKAGE = "-";

    /**
     * Detects the package or namespace name of the specified file.
     *
     * @param fileName
     *         the file name of the file to scan
     *
     * @return the detected package or namespace name
     */
    public String detectPackageName(final String fileName) {
        if (accepts(fileName)) {
            try (InputStream stream = Files.newInputStream(Paths.get(fileName))) {
                return detectPackageName(stream);
            }
            catch (IOException | InvalidPathException ignore) {
                // ignore IO errors
            }
        }
        return UNKNOWN_PACKAGE;
    }

    @VisibleForTesting
    String detectPackageName(final InputStream stream) throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(new BOMInputStream(stream)))) {
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
