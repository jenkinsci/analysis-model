package edu.hm.hafner.analysis;

import edu.hm.hafner.util.TreeString;

import java.io.Serial;
import java.io.Serializable;

/**
 * Represents a location of an issue within a file. It includes the file name, line and column ranges. An issue
 * can have multiple locations if it spans multiple lines or columns, or files.
 *
 * @author Akash Manna
 */
public class Location implements Serializable {
    @Serial
    private static final long serialVersionUID = 6L;

    private final TreeString fileName;
    private final int lineStart;
    private final int lineEnd;
    private final int columnStart;
    private final int columnEnd;

    /**
     * Creates a new {@link Location} with the specified parameters.
     *
     * @param fileName
     *         the name of the file
     * @param lineStart
     *         the first line (lines start at 1; 0 indicates the whole file)
     * @param lineEnd
     *         the last line (lines start at 1)
     * @param columnStart
     *         the first column (columns start at 1, 0 indicates the whole line)
     * @param columnEnd
     *         the last column (columns start at 1)
     */
    public Location(final TreeString fileName, final int lineStart, final int lineEnd,
            final int columnStart, final int columnEnd) {
        this.fileName = fileName;

        int providedLineStart = defaultInteger(lineStart);
        int providedLineEnd = defaultInteger(lineEnd) == 0 ? providedLineStart : defaultInteger(lineEnd);
        if (providedLineStart == 0) {
            this.lineStart = providedLineEnd;
            this.lineEnd = providedLineEnd;
        }
        else {
            this.lineStart = Math.min(providedLineStart, providedLineEnd);
            this.lineEnd = Math.max(providedLineStart, providedLineEnd);
        }

        int providedColumnStart = defaultInteger(columnStart);
        int providedColumnEnd = defaultInteger(columnEnd) == 0 ? providedColumnStart : defaultInteger(columnEnd);
        if (providedColumnStart == 0) {
            this.columnStart = providedColumnEnd;
            this.columnEnd = providedColumnEnd;
        }
        else {
            // if the line ends on the next line, columnStart can be greater then columnEnd
            this.columnStart = providedLineStart < providedLineEnd ? providedColumnStart : Math.min(providedColumnStart, providedColumnEnd);
            this.columnEnd = providedLineStart < providedLineEnd ? providedColumnEnd : Math.max(providedColumnStart, providedColumnEnd);
        }
    }

    /**
     * Creates a default Integer representation for undefined input parameters.
     *
     * @param integer
     *         the integer to check
     *
     * @return the valid integer value or 0 if the specified {@link Integer} is {@code null} or less than 0
     */
    private int defaultInteger(final int integer) {
        return Math.max(integer, 0);
    }

    /**
     * Creates a new {@link Location} with the specified file name and line range.
     *
     * @param fileName
     *         the name of the file
     * @param lineStart
     *         the first line (lines start at 1; 0 indicates the whole file)
     * @param lineEnd
     *         the last line (lines start at 1)
     */
    public Location(final TreeString fileName, final int lineStart, final int lineEnd) {
        this(fileName, lineStart, lineEnd, 0, 0);
    }

    /**
     * Creates a new {@link Location} with the specified file name and line.
     *
     * @param fileName
     *         the name of the file
     * @param lineStart
     *         the line number (lines start at 1; 0 indicates the whole file)
     */
    public Location(final TreeString fileName, final int lineStart) {
        this(fileName, lineStart, lineStart, 0, 0);
    }

    /**
     * Creates a new {@link Location} with the specified file name.
     *
     * @param fileName
     *         the name of the file
     */
    public Location(final TreeString fileName) {
        this(fileName, 0, 0, 0, 0);
    }

    TreeString getFileNameTreeString() {
        return fileName;
    }

    /**
     * Returns the file name of this location.
     *
     * @return the file name
     */
    public String getFileName() {
        return fileName.toString();
    }

    /**
     * Returns the first line of this location (lines start at 1; 0 indicates the whole file).
     *
     * @return the first line
     */
    public int getLineStart() {
        return lineStart;
    }

    /**
     * Returns the last line of this location (lines start at 1).
     *
     * @return the last line
     */
    public int getLineEnd() {
        return lineEnd;
    }

    /**
     * Returns the first column of this location (columns start at 1, 0 indicates the whole line).
     *
     * @return the first column
     */
    public int getColumnStart() {
        return columnStart;
    }

    /**
     * Returns the last column of this location (columns start at 1).
     *
     * @return the last column
     */
    public int getColumnEnd() {
        return columnEnd;
    }

    /**
     * Returns whether the specified line is contained in this range.
     *
     * @param line the line to check
     * @return {@code true} if the line is contained in this range, {@code false} otherwise
     */
    public boolean contains(final int line) {
        return line >= lineStart && line <= lineEnd;
    }

    @Override
    public String toString() {
        var builder = new StringBuilder(fileName.toString());
        if (lineStart > 0) {
            builder.append(':').append(lineStart);
            if (lineEnd != lineStart) {
                builder.append('-').append(lineEnd);
            }
            if (columnStart > 0) {
                builder.append(':').append(columnStart);
                if (columnEnd != columnStart) {
                    builder.append('-').append(columnEnd);
                }
            }
        }
        return builder.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        var that = (Location) o;

        if (lineStart != that.lineStart) {
            return false;
        }
        if (lineEnd != that.lineEnd) {
            return false;
        }
        if (columnStart != that.columnStart) {
            return false;
        }
        if (columnEnd != that.columnEnd) {
            return false;
        }
        return fileName.equals(that.fileName);
    }

    @Override
    public int hashCode() {
        var result = fileName.hashCode();
        result = 31 * result + lineStart;
        result = 31 * result + lineEnd;
        result = 31 * result + columnStart;
        result = 31 * result + columnEnd;
        return result;
    }
}
