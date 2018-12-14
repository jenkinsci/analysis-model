package edu.hm.hafner.util;

import javax.annotation.CheckForNull;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang3.StringUtils;

/**
 * Utilities for {@link Path} instances.
 *
 * @author Ullrich Hafner
 */
public class PathUtil {
    private static final String BACK_SLASH = "\\";
    private static final String SLASH = "/";

    /**
     * Returns the string representation of the specified path. The path will be actually resolved in the file system
     * and will be returned as fully qualified absolute path. In case of an error, an exception will be thrown.
     *
     * @param path
     *         the path to get the absolute path for
     *
     * @return the absolute path
     * @throws IOException
     *         if the path could not be found
     */
    public String toString(final Path path) throws IOException {
        return normalize(path.toAbsolutePath().normalize().toRealPath().toString());
    }

    /**
     * Returns the string representation of the specified path. The path will be actually resolved in the file system
     * and will be returned as fully qualified absolute path. In case of an error, i.e. if the file is not found, the
     * provided {@code path} will be returned unchanged (but normalized using the UNIX path separator).
     *
     * @param path
     *         the path to get the absolute path for
     *
     * @return the absolute path
     */
    public String getAbsolutePath(final String path) {
        try {
            return getAbsolutePath(Paths.get(path));
        }
        catch (InvalidPathException ignored) {
            return normalize(path);
        }
    }

    /**
     * Returns the string representation of the specified path. The path will be actually resolved in the file system
     * and will be returned as fully qualified absolute path. In case of an error, i.e. if the file is not found, the
     * provided {@code path} will be returned unchanged (but normalized using the UNIX path separator).
     *
     * @param path
     *         the path to get the absolute path for
     *
     * @return the absolute path
     */
    public String getAbsolutePath(final Path path) {
        try {
            return normalize(toString(path));
        }
        catch (IOException | InvalidPathException ignored) {
            return normalize(path.toString());
        }
    }

    private String normalize(final String fileName) {
        return fileName.replace(BACK_SLASH, SLASH);
    }

    /**
     * Returns the absolute path of the specified file in the given directory.
     *
     * @param directory
     *         the directory that contains the file
     * @param fileName
     *         the file name
     *
     * @return the absolute path
     */
    public String createAbsolutePath(final @CheckForNull String directory, final String fileName) {
        if (isAbsolute(fileName) || StringUtils.isBlank(directory)) {
            return fileName;
        }
        String normalized = normalize(directory);
        if (normalized.endsWith(SLASH)) {
            return normalized + fileName;
        }
        return normalized + SLASH + fileName;
    }

    private boolean isAbsolute(final String fileName) {
        try {
            return Paths.get(fileName).isAbsolute();
        }
        catch (InvalidPathException ignored) {
            return false;
        }
    }
}
