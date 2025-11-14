package edu.hm.hafner.analysis;

import edu.umd.cs.findbugs.annotations.CheckForNull;

import java.io.Serial;
import java.io.Serializable;

/**
 * Represents a file location associated with an issue. This can be used to represent additional files that are related
 * to a warning, such as header files referenced in C++ reorder warnings, or multiple files traced in execution paths
 * for tools like Fortify and Coverity.
 *
 * @author Akash Manna
 */
public class FileLocation implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String fileName;
    private final int lineStart;
    private final int lineEnd;
    private final int columnStart;
    private final int columnEnd;
    @CheckForNull
    private final String message;

    /**
     * Creates a new {@link FileLocation} with the specified parameters.
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
     * @param message
     *         optional message describing this location (can be null)
     */
    public FileLocation(final String fileName, final int lineStart, final int lineEnd,
            final int columnStart, final int columnEnd, @CheckForNull final String message) {
        this.fileName = fileName;
        this.lineStart = lineStart;
        this.lineEnd = lineEnd;
        this.columnStart = columnStart;
        this.columnEnd = columnEnd;
        this.message = message;
    }

    /**
     * Creates a new {@link FileLocation} with the specified file name and line range.
     *
     * @param fileName
     *         the name of the file
     * @param lineStart
     *         the first line (lines start at 1; 0 indicates the whole file)
     * @param lineEnd
     *         the last line (lines start at 1)
     */
    public FileLocation(final String fileName, final int lineStart, final int lineEnd) {
        this(fileName, lineStart, lineEnd, 0, 0, null);
    }

    /**
     * Creates a new {@link FileLocation} with the specified file name and line.
     *
     * @param fileName
     *         the name of the file
     * @param lineStart
     *         the line number (lines start at 1; 0 indicates the whole file)
     */
    public FileLocation(final String fileName, final int lineStart) {
        this(fileName, lineStart, lineStart, 0, 0, null);
    }

    /**
     * Creates a new {@link FileLocation} with the specified file name and message.
     *
     * @param fileName
     *         the name of the file
     * @param lineStart
     *         the first line (lines start at 1; 0 indicates the whole file)
     * @param message
     *         optional message describing this location
     */
    public FileLocation(final String fileName, final int lineStart, @CheckForNull final String message) {
        this(fileName, lineStart, lineStart, 0, 0, message);
    }

    /**
     * Returns the file name of this location.
     *
     * @return the file name
     */
    public String getFileName() {
        return fileName;
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
     * Returns the optional message describing this location.
     *
     * @return the message, or null if not set
     */
    @CheckForNull
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        var builder = new StringBuilder(fileName);
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
        if (message != null) {
            builder.append(" - ").append(message);
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

        var that = (FileLocation) o;

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
        if (!fileName.equals(that.fileName)) {
            return false;
        }
        return message != null ? message.equals(that.message) : that.message == null;
    }

    @Override
    public int hashCode() {
        var result = fileName.hashCode();
        result = 31 * result + lineStart;
        result = 31 * result + lineEnd;
        result = 31 * result + columnStart;
        result = 31 * result + columnEnd;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }
}
