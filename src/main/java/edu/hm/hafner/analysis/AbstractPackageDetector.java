package edu.hm.hafner.analysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.InvalidPathException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.commons.io.input.BOMInputStream;

import edu.hm.hafner.util.VisibleForTesting;

import static edu.hm.hafner.analysis.PackageDetectors.*;

/**
 * Base class for package detectors.
 *
 * @author Ullrich Hafner
 */
abstract class AbstractPackageDetector {
    private final FileSystem fileSystem;

    /**
     * Creates a new instance of {@link AbstractPackageDetector}.
     *
     * @param fileSystem
     *         file system facade
     */
    AbstractPackageDetector(final FileSystem fileSystem) {
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
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(BOMInputStream.builder().setInputStream(stream).get(), charset))) {
            return detectPackageName(buffer.lines());
        }
    }

    /**
     * Detects the package or namespace name of the specified input stream. The stream will be closed automatically by
     * the caller of this method.
     *
     * @param lines
     *         the content of the file to scan
     *
     * @return the detected package or namespace name
     */
    String detectPackageName(final Stream<String> lines) {
        Pattern pattern = getPattern();
        return lines.map(pattern::matcher)
                .filter(Matcher::matches)
                .findFirst()
                .map(matcher -> matcher.group(1))
                .orElse(UNDEFINED_PACKAGE).trim();
    }

    /**
     * Returns the Pattern for the Package Name in this kind of file.
     * @return the Pattern.
     */
    abstract Pattern getPattern();

    /**
     * Returns whether this classifier accepts the specified file for processing.
     *
     * @param fileName
     *         the file name
     *
     * @return {@code true} if the classifier accepts the specified file for processing.
     */
    abstract boolean accepts(String fileName);
}
