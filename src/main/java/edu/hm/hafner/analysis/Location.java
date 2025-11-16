package edu.hm.hafner.analysis;

import java.io.Serial;
import java.io.Serializable;

import edu.hm.hafner.util.TreeString;

/**
 * Represents a file location associated with an issue. This can be used to represent additional files that are related to a warning, 
 * such as header files referenced in C++ reorder warnings, or multiple files traced in execution paths for tools like Fortify and Coverity.
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
        this.lineStart = lineStart;
        this.lineEnd = lineEnd;
        this.columnStart = columnStart;
        this.columnEnd = columnEnd;
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
     * Returns the file name of this location.
     *
     * @return the file name
     */
    public TreeString getFileName() {
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
