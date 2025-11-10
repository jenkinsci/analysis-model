package edu.hm.hafner.analysis;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.util.TreeString;
import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Represents a file location with a file name, path, line range, and column range.
 * This is used to store additional file locations for issues that span multiple files.
 *
 * @author Akash Manna
 */
public class FileLocation implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String pathName;
    private final TreeString fileName;
    private final int lineStart;
    private final int lineEnd;
    private final int columnStart;
    private final int columnEnd;

    /**
     * Creates a new instance of {@link FileLocation}.
     *
     * @param pathName
     *                    the path that contains the affected file
     * @param fileName
     *                    the name of the file
     * @param lineStart
     *                    the first line (lines start at 1; 0 indicates the whole
     *                    file)
     * @param lineEnd
     *                    the last line (lines start at 1)
     * @param columnStart
     *                    the first column (columns start at 1, 0 indicates the
     *                    whole line)
     * @param columnEnd
     *                    the last column (columns start at 1)
     */
    public FileLocation(@CheckForNull final String pathName, final TreeString fileName,
            final int lineStart, final int lineEnd,
            final int columnStart, final int columnEnd) {
        this.pathName = StringUtils.defaultIfBlank(pathName, StringUtils.EMPTY);
        this.fileName = fileName;

        int providedLineStart = defaultInteger(lineStart);
        int providedLineEnd = defaultInteger(lineEnd) == 0 ? providedLineStart : defaultInteger(lineEnd);
        if (providedLineStart == 0) {
            this.lineStart = providedLineEnd;
            this.lineEnd = providedLineEnd;
        } else {
            this.lineStart = Math.min(providedLineStart, providedLineEnd);
            this.lineEnd = Math.max(providedLineStart, providedLineEnd);
        }

        int providedColumnStart = defaultInteger(columnStart);
        int providedColumnEnd = defaultInteger(columnEnd) == 0 ? providedColumnStart : defaultInteger(columnEnd);
        if (providedColumnStart == 0) {
            this.columnStart = providedColumnEnd;
            this.columnEnd = providedColumnEnd;
        } else {
            // if the line ends on the next line, columnStart can be greater than columnEnd
            this.columnStart = providedLineStart < providedLineEnd
                    ? providedColumnStart
                    : Math.min(providedColumnStart, providedColumnEnd);
            this.columnEnd = providedLineStart < providedLineEnd
                    ? providedColumnEnd
                    : Math.max(providedColumnStart, providedColumnEnd);
        }
    }

    private int defaultInteger(final int value) {
        return value < 0 ? 0 : value;
    }

    /**
     * Returns the path that contains the affected file.
     *
     * @return the path
     */
    public String getPathName() {
        return pathName;
    }

    /**
     * Returns the name of the affected file.
     *
     * @return the file name
     */
    public String getFileName() {
        return fileName.toString();
    }

    /**
     * Returns the absolute path to the file.
     *
     * @return the absolute path
     */
    public String getAbsolutePath() {
        if (StringUtils.isBlank(pathName)) {
            return getFileName();
        }
        return pathName + "/" + getFileName();
    }

    /**
     * Returns the first line (lines start at 1; 0 indicates the whole file).
     *
     * @return the first line
     */
    public int getLineStart() {
        return lineStart;
    }

    /**
     * Returns the last line (lines start at 1).
     *
     * @return the last line
     */
    public int getLineEnd() {
        return lineEnd;
    }

    /**
     * Returns the first column (columns start at 1, 0 indicates the whole line).
     *
     * @return the first column
     */
    public int getColumnStart() {
        return columnStart;
    }

    /**
     * Returns the last column (columns start at 1).
     *
     * @return the last column
     */
    public int getColumnEnd() {
        return columnEnd;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        var that = (FileLocation) o;
        return lineStart == that.lineStart
                && lineEnd == that.lineEnd
                && columnStart == that.columnStart
                && columnEnd == that.columnEnd
                && Objects.equals(pathName, that.pathName)
                && Objects.equals(fileName, that.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pathName, fileName, lineStart, lineEnd, columnStart, columnEnd);
    }

    @Override
    public String toString() {
        return String.format(java.util.Locale.ENGLISH, "%s(%d:%d-%d:%d)", getAbsolutePath(), lineStart, columnStart,
                lineEnd, columnEnd);
    }
}
