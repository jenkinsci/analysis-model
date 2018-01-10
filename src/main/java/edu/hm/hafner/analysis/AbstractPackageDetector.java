package edu.hm.hafner.analysis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

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
        FileInputStream input = null;
        try {
            if (accepts(fileName)) {
                input = new FileInputStream(new File(fileName));
                return detectPackageName(input);
            }
        }
        catch (FileNotFoundException ignored) {
            // ignore and return empty string
        }
        finally {
            IOUtils.closeQuietly(input);
        }
        return UNKNOWN_PACKAGE;
    }

    /**
     * Returns whether this classifier accepts the specified file for processing.
     *
     * @param fileName
     *            the file name
     * @return {@code true} if the classifier accepts the specified file for processing.
     */
    abstract boolean accepts(String fileName);

    /**
     * Detects the package or namespace name of the specified input stream. The
     * stream will be closed automatically by the caller of this method.
     *
     * @param stream
     *            the content of the file to scan
     * @return the detected package or namespace name
     */
    abstract String detectPackageName(InputStream stream);
}
