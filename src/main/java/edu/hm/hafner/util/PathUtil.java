package edu.hm.hafner.util;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import edu.umd.cs.findbugs.annotations.Nullable;

/**
 * Utilities for {@link Path} instances.
 *
 * @author Ullrich Hafner
 */
public class PathUtil {
    private static final String BACK_SLASH = "\\";
    private static final String SLASH = "/";
    private static final String DRIVE_LETTER_PREFIX = "^[a-z]:/.*"; 

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
        return makeUnixPath(path.toAbsolutePath().normalize().toRealPath(LinkOption.NOFOLLOW_LINKS).toString());
    }

    /**
     * Returns the string representation of the specified path. The path will be actually resolved in the file system
     * and will be returned as fully qualified absolute path. In case of an error, i.e. if the file is not found, the
     * provided {@code path} will be returned unchanged (but normalized using the UNIX path separator and upper case
     * drive letter).
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
            return makeUnixPath(path);
        }
    }

    /**
     * Returns the string representation of the specified path. The path will be actually resolved in the file system
     * and will be returned as fully qualified absolute path. In case of an error, i.e. if the file is not found, the
     * provided {@code path} will be returned unchanged (but normalized using the UNIX path separator and upper case
     * drive letter).
     *
     * @param path
     *         the path to get the absolute path for
     *
     * @return the absolute path
     */
    public String getAbsolutePath(final Path path) {
        try {
            return makeUnixPath(toString(path));
        }
        catch (IOException | InvalidPathException ignored) {
            return makeUnixPath(path.toString());
        }
    }

    private String makeUnixPath(final String fileName) {
        String unixStyle = fileName.replace(BACK_SLASH, SLASH);
        if (unixStyle.matches(DRIVE_LETTER_PREFIX)) {
            unixStyle = StringUtils.capitalize(unixStyle);
        }
        return unixStyle;
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
    public String createAbsolutePath(final @Nullable String directory, final String fileName) {
        if (isAbsolute(fileName) || StringUtils.isBlank(directory)) {
            return makeUnixPath(fileName);
        }
        String path = makeUnixPath(directory);

        String separator;
        if (path.endsWith(SLASH)) {
            separator = StringUtils.EMPTY;
        }
        else {
            separator = SLASH;
        }
        String normalized = FilenameUtils.normalize(String.join(separator, path, fileName));
        return makeUnixPath(normalized == null ? fileName : normalized);
    }

    /**
     * Returns whether the specified file name is an absolute path.
     *
     * @param fileName
     *         the file name to test
     *
     * @return {@code true} if this path is an absolute path, {@code false} if a relative path
     */
    public boolean isAbsolute(final String fileName) {
        return FilenameUtils.getPrefixLength(fileName) > 0;
    }
}
