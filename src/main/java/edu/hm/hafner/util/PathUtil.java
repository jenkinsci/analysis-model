package edu.hm.hafner.util;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utilities for {@link Path} instances.
 *
 * @author Ullrich Hafner
 */
public class PathUtil {
    private static final String BACK_SLASH = "\\";
    private static final String SLASH = "/";

    public String toString(final Path path) throws IOException {
        return normalize(path.toAbsolutePath().normalize().toRealPath().toString());
    }

    public String getAbsolutePath(final String path) {
        try {
            return getAbsolutePath(Paths.get(path));
        }
        catch (InvalidPathException ignored) {
            return path;
        }
    }

    public String getAbsolutePath(final Path path) {
        try {
            return new PathUtil().toString(path);
        }
        catch (IOException | InvalidPathException ignored) {
            return path.toString();
        }
    }

    private String normalize(final String fileName) {
        return fileName.replace(BACK_SLASH, SLASH);
    }

}
